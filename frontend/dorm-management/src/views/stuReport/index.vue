<script setup>
import { ref, onMounted } from 'vue'
import BaseChart from '@/components/BaseChart.vue'
// 1. 引入你的API函数
import { statisticsStuGenderCountApi, statisticsMajorStuCountApi, statisticsStuBuildingCountApi, statisticsBedsOccupancyRateApi } from '@/api/stuReport';

// 2. 将复制官方的option = 后面的代码放入ref里面替换即可
const genderOption = ref({
  title: {
    text: '学生性别分布',
    subtext: '在住学生统计',
    left: 'center',
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c}人 ({d}%)'
  },
  //原配置（页面上会有按钮一样的，点击之后隐藏一部分报表）
  // legend: {
  //   orient: 'vertical',
  //   left: 'left'
  // },
  // ✅ 修改后（隐藏图例）
  legend: {
    show: false  // 关键：设置为false隐藏图例
  },
  color: ['#5470c6', '#ee6666'], // 男-蓝，女-红
  series: [
    {
      name: '学生性别',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '55%'],
      data: [], // 🎯 初始为空，等待loadGenderData方法填充
      label: { show: true, formatter: '{b}: {c}人 ({d}%)' }
    }
  ]
})

//专业学生分布
// 图表配置
const majorOption = ref({
  title: {
    text: '专业学生分布',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    },
    formatter: '{b}: {c}人'
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '15%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    axisLabel: {
      color: '#ccc'
    },
    splitLine: {
      lineStyle: {
        color: '#444'
      }
    }
  },
  yAxis: {
    type: 'category',
    data: [], // 专业名称（从后端获取）
    axisLabel: {
      color: '#ccc',
      fontSize: 12
    }
  },
  series: [
    {
      name: '学生人数',
      type: 'bar',
      data: [], // 学生数量（从后端获取）
      itemStyle: {
        color: '#188df0'
      },
      label: {
        show: true,
        position: 'right',
        color: '#fff'
      }
    }
  ]
})

//按照楼栋统计学生
const buildingOption = ref({
  title: {
    text: '楼栋学生分布',
    subtext: '在住学生统计',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [],
      type: 'bar'
    }
  ]
})

//宿舍床位使用率
const gaugeOption = ref({
  tooltip: {
    trigger: 'item',
    formatter: '{a}<br/>{b}: {c}%'
  },
  title: {
    text: '全校床位总体占用率',
    left: 'center',
    textStyle: { color: '#fff', fontSize: 18 }
  },
  series: [
    {
      type: 'gauge',
      center: ['50%', '60%'],
      radius: '90%',
      startAngle: 180,
      endAngle: 0,
      min: 0,
      max: 100,
      progress: {
        show: true,
        width: 25,
        itemStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 1,
            y2: 0,
            colorStops: [
              { offset: 0, color: '#91cc75' },   // 绿色
              { offset: 0.5, color: '#fac858' }, // 黄色
              { offset: 1, color: '#ee6666' }    // 红色
            ]
          }
        }
      },
      axisLine: {
        lineStyle: { width: 25, color: [[1, 'rgba(255,255,255,0.1)']] }
      },
      axisTick: { show: false },
      splitLine: { show: false },
      axisLabel: { show: false },
      anchor: { show: false },
      title: {
        show: true,
        offsetCenter: [0, '-30%'],
        fontSize: 16,
        color: '#ccc'
      },
      detail: {
        valueAnimation: true,
        fontSize: 48,
        fontWeight: 'bold',
        offsetCenter: [0, '10%'],
        color: '#fff',
        formatter: '{value}%'
      },
      data: [
        {
          value: null,  // 这里放计算出的占用率
          name: '占用率'
        }
      ]
    }
  ]
});

//统计全校床铺占用率
const loadBedsData = async () => {
  try{
    const result = await statisticsBedsOccupancyRateApi();
    if(result.code === 1) {
      //赋值
      gaugeOption.value.series[0].data[0].value = result.data;
    }else {
      console.warn('统计床铺占用率数据不符合预期',result)
    }
  }catch(error) {
    console.log('统计全校床铺占用率失败');
  }
}

//统计楼栋下学生
const loadBuildingData = async () => {
  try {
    const result = await statisticsStuBuildingCountApi();
    if (result.code === 1) {
      //楼栋名称赋值
      buildingOption.value.xAxis.data = result.data.map(item => item.buildingName);
      buildingOption.value.series[0].data = result.data.map(item => item.residentCount);
    } else {
      console.warn('统计楼栋学生发生数据不符合预期', result);
      buildingOption.value.xAxis.data = [];
      buildingOption.value.series[0].data = [];
    }
  } catch (error) {
    console.error('统计楼栋下学生失败', error);
    //设为空值
    buildingOption.value.xAxis.data = [];
    buildingOption.value.series[0].data = [];
  }
}


//加载专业数据
const loadMajorData = async () => {
  try {
    console.log('开始获取专业统计数据...')
    const result = await statisticsMajorStuCountApi()

    if (result.code === 1 && result.data) {
      const data = result.data

      // 提取专业名称和学生数量
      const majorNames = data.map(item => item.majorName)
      const studentCounts = data.map(item => item.studentCount)

      // 更新图表配置
      majorOption.value.yAxis.data = majorNames
      majorOption.value.series[0].data = studentCounts

      console.log('专业数据加载成功：', data)
    } else {
      console.warn('专业数据API返回的数据格式不符合预期:', result)
      majorOption.value.yAxis.data = []
      majorOption.value.series[0].data = []
    }
  } catch (error) {
    console.error('❌ 获取专业数据失败:', error)
    majorOption.value.yAxis.data = []
    majorOption.value.series[0].data = []
  }
}


// 3. ✅ 关键：单独定义一个异步方法来获取数据并更新图表
const loadGenderData = async () => {
  try {
    console.log('开始获取性别统计数据...')

    // 3.1 调用API
    const result = await statisticsStuGenderCountApi()

    // 3.2 根据你的后端数据结构进行处理
    // 假设后端返回格式为: { code:200, data: { maleCount:65, femaleCount:35 } }
    if (result.data) {
      const { maleCount = 0, femaleCount = 0 } = result.data

      // 3.3 更新图表配置的data部分
      genderOption.value.series[0].data = [
        { value: maleCount, name: '男' },
        { value: femaleCount, name: '女' }
      ]

      console.log('性别数据加载成功，男:', maleCount, '女:', femaleCount)
    } else {
      console.warn('性别数据API返回的数据格式不符合预期:', result)
      // 可选：给用户一个错误提示，或显示零数据
      genderOption.value.series[0].data = [
        { value: 0, name: '男' },
        { value: 0, name: '女' }
      ]
    }

  } catch (error) {
    console.error('❌ 获取性别数据失败:', error)
    // 可选：给用户一个错误提示，或显示零数据
    genderOption.value.series[0].data = [
      { value: 0, name: '男' },
      { value: 0, name: '女' }
    ]
  }
}

// 4. 在onMounted钩子中调用这个独立的方法
onMounted(() => {
  loadGenderData() // 学生性别统计
  loadMajorData() //学生专业统计
  loadBuildingData() //统计楼栋下学生
  loadBedsData() //统计全校床铺占用率
})
</script>

<template>
  <div class="dashboard">
    <div class="chart-grid">
      <!-- 性别分布图表 -->
      <div class="chart-card">
        <BaseChart :option="genderOption" theme="dark" height="100%" />
      </div>

      <!-- 专业分布图表 -->
      <div class="chart-card">
        <BaseChart :option="majorOption" theme="dark" height="100%" />
      </div>
      <!-- 楼栋学生统计报表 -->
      <div class="chart-card">
        <BaseChart :option="buildingOption" theme="dark" height="100%" />
      </div>
      <!-- 全校床位使用率 -->
      <div class="chart-card">
        <BaseChart :option="gaugeOption" theme="dark" height="100%" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 20px;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  /* 2列 */
  grid-template-rows: repeat(2, 1fr);
  /* 2行 */
  gap: 20px;
  height: calc(100vh - 100px);
  /* 视口高度减去padding等 */
}

.chart-card {
  background: rgba(255, 255, 255, 0.05);
  /* 卡片背景 */
  border-radius: 8px;
  padding: 16px;
  display: flex;
  flex-direction: column;
}

.chart-card h3 {
  margin: 0 0 16px 0;
  color: #fff;
  font-size: 16px;
}

/* 使图表填满卡片 */
.chart-card :deep(.chart-container) {
  flex: 1;
  min-height: 0;
  /* 重要：允许图表缩小 */
}
</style>