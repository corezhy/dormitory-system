<script setup>
import { ref, onMounted } from 'vue'
import BaseChart from '@/components/BaseChart.vue'
import { violationStuCountApi, deptEmpCountApi } from '@/api/managementReport'

/* 按照专业统计违纪学生 */
const violationOption = ref({
  title: {
    text: '专业违纪学生统计',
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
  yAxis: [
    {
      type: 'category',
      data: [], //专业名称
      axisTick: {
        alignWithLabel: true
      }
    }
  ],
  xAxis: [
    {
      type: 'value'
    }
  ],
  series: [
    {
      name: '违纪人数',
      type: 'bar',
      barWidth: '60%',
      data: [] //存储该专业违纪学生次数
    }
  ]
})

//专业违纪学生统计
const loadViolationCount = async () => {
  try {
    const result = await violationStuCountApi();
    if (result.code === 1) {
      violationOption.value.yAxis[0].data = result.data.map(item => item.majorName);
      violationOption.value.series[0].data = result.data.map(item => item.violationCount);
    } else {
      console.warn('专业违纪学生统计数据有误', result);
      violationOption.value.yAxis[0].data = [];
      violationOption.value.series[0].data = [];
    }
  } catch (error) {
    console.error('专业违纪学生统计失败', error);
    violationOption.value.yAxis[0].data = [];
    violationOption.value.series[0].data = [];
  }
}

//统计各部门员工人数
const empCountOption = ref({
  title: {
    text: '各部门员工人数',
    //subtext: 'Fake Data',
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: (params) => {
      const { marker, name, value } = params;
      return `${marker}${name}<br/>员工总数：${value}`;
    }
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '员工总数',
      type: 'pie',
      radius: '50%',
      data: [

      ], //存入各部门员工人数返回VO
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
})

//统计部门员工总数
const loadEmpCount = async () => {
  try {
    const result = await deptEmpCountApi();
    if (result.code === 1) {
      // 替换整个 data 数组
      empCountOption.value.series[0].data = result.data.map(item => ({
        name: item.deptName,
        value: item.employeeCount
      }))
    } else {
      console.warn('统计部门员工总数有误', result);
      empCountOption.value.series[0].data = [];
    }
  } catch (error) {
    console.error('统计部门员工总数失败', error);
    empCountOption.value.series[0].data = [];
  }
}

//钩子函数
onMounted(() => {
  //专业违纪学生统计
  loadViolationCount();
  //统计部门员工总数
  loadEmpCount();
})

</script>

<template>
  <div class="dashboard">
    <div class="chart-grid">
      <!-- 违纪学生分布 -->
      <div class="chart-card">
        <BaseChart :option="violationOption" theme="dark" height="300px" />
      </div>
      <!-- 各部门员工人数 -->
      <div class="chart-card">
        <BaseChart :option="empCountOption" theme="dark" height="300px" />
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
  grid-template-columns: repeat(1, 1fr);
  /* 1列 */
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