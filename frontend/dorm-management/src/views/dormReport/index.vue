<script setup>
import { ref, onMounted } from 'vue'
import BaseChart from '@/components/BaseChart.vue'
//导入api
import { statisticsDormAndBedCountApi, statisticStopDormApi } from '@/api/dormReport';

//统计各楼栋宿舍数量和床位总数（柱线组合图）
const buildingOption = ref({
  title: {
    text: '各楼栋宿舍数量与床位总数',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow' //或 'cross'
    }
  },
  xAxis: {
    type: 'category',
    data: [] //楼栋名称
  },
  yAxis: [{ //改为数组，用于实现组合图
    type: 'value',
    name: '宿舍数量',
    position: 'left',
    axisLabel: {
      formatter: '{value} 间'
    }
  },
  {
    type: 'value',
    name: '床位总数',
    position: 'right',
    alignTicks: true,
    axisLabel: {
      formatter: '{value} 张'
    }
  }
  ],
  series: [
    {
      name: '宿舍数量',
      type: 'bar',
      data: [], //各楼栋宿舍数量数组
      yAxisIndex: 0
    },
    {
      name: '床位总数',
      type: 'line',
      data: [], //各宿舍床位总数数组
      yAxisIndex: 1,
      smooth: true, //平滑折线
      lineStyle: {
        width: 3
      },
      markPoint: {
        data: [{ type: 'max', name: '最大值' }]
      }
    }
  ]
})

//停用/维修宿舍分布（散点图）
const disabledRoomsOption = ref({
  title: {
    text: '停用宿舍分布图',
    subtext: '鼠标悬停查看每层停用数量',
    left: 'center',
    textStyle: { 
      color: '#fff',
      fontSize: 18
    },
    subtextStyle: {
      color: '#aaa',
      fontSize: 12
    }
  },
  
  tooltip: {
    trigger: 'item',
    backgroundColor: 'rgba(0,0,0,0.8)',
    borderColor: '#666',
    borderWidth: 1,
    padding: [10, 15],
    textStyle: {
      color: '#fff',
      fontSize: 13
    },
    formatter: function(params) {
      const buildingName = params.data[3] || disabledRoomsOption.value.xAxis.data[Math.round(params.data[0])];
      const floorNum = params.data[4] || params.data[1];
      const floorTotal = params.data[5] || 1;
      
      return `
        <div style="color:#ee6666;font-weight:bold;margin-bottom:8px">🚫 停用宿舍信息</div>
        <div style="margin-bottom:4px">
          <span style="color:#ccc">楼栋: </span>
          <span style="color:#fff">${buildingName}</span>
        </div>
        <div style="margin-bottom:4px">
          <span style="color:#ccc">楼层: </span>
          <span style="color:#fff">${floorNum}楼</span>
        </div>
        <div style="margin-bottom:8px">
          <span style="color:#ccc">本层停用数: </span>
          <span style="color:#ff9900;font-weight:bold">${floorTotal}间</span>
        </div>
      `;
    }
  },
  
  xAxis: {
    type: 'category',
    name: '楼栋',
    nameLocation: 'middle',
    nameGap: 30,
    axisLabel: {
      color: '#ccc',
      fontSize: 12,
      rotate: 30  // ✅ 固定旋转30度，防止文字重叠
    },
    axisLine: {
      lineStyle: { color: '#666' }
    },
    boundaryGap: true,
    data: []
  },
  
  yAxis: {
    type: 'value',
    name: '楼层',
    nameLocation: 'middle',
    nameGap: 35,
    min: 0,
    max: 10,
    interval: 1,
    axisLabel: {
      color: '#ccc',
      formatter: '{value}F'
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(255,255,255,0.1)',
        type: 'dashed'
      }
    }
  },
  
  series: [
    {
      name: '停用宿舍',
      type: 'scatter',
      symbolSize: 14,
      symbol: 'circle',
      data: [],
      itemStyle: {
        color: '#ee6666',
        borderColor: '#fff',
        borderWidth: 1,
        opacity: 0.8
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 15,
          shadowColor: 'rgba(255, 100, 100, 0.8)',
          borderWidth: 2
        }
      }
    }
  ]
});

//统计停用/维修宿舍分布
const loadStopDorm = async () => {
  try {
    const result = await statisticStopDormApi();
    
    if (result.code === 1 && result.data) {
      const rooms = result.data;
      
      // 1. 提取楼栋（去重排序）
      const buildings = [...new Set(rooms.map(r => r.name))];
      buildings.sort();
      
      // 2. 设置x轴
      disabledRoomsOption.value.xAxis.data = buildings;
      
      // 3. 🔥 如果楼栋少，可以不旋转标签
      if (buildings.length <= 3) {
        disabledRoomsOption.value.xAxis.axisLabel.rotate = 0;
      }
      
      // 4. 统计楼层数量
      const floorStats = {};
      rooms.forEach(room => {
        const key = `${room.name}-${room.floorNumber}`;
        floorStats[key] = (floorStats[key] || 0) + 1;
      });
      
      // 5. 转换数据（添加水平偏移）
      const scatterData = rooms.map(room => {
        const buildingIndex = buildings.indexOf(room.name);
        const key = `${room.name}-${room.floorNumber}`;
        const floorRoomCount = floorStats[key] || 1;
        
        // 找到这个宿舍在楼层中的顺序
        const sameFloorRooms = rooms.filter(r => 
          r.name === room.name && r.floorNumber === room.floorNumber
        );
        const roomOrder = sameFloorRooms.findIndex(r => 
          r.fullCode === room.fullCode
        );
        
        // 计算水平偏移
        let horizontalOffset = 0;
        if (floorRoomCount > 1) {
          horizontalOffset = (roomOrder / (floorRoomCount - 1) - 0.5) * 0.4;
        }
        
        return [
          buildingIndex + horizontalOffset,
          room.floorNumber,
          room.fullCode,
          room.name,
          room.floorNumber,
          floorRoomCount  // 本层停用宿舍数
        ];
      });
      
      // 6. 设置数据
      disabledRoomsOption.value.series[0].data = scatterData;
      
      // 7. 调整y轴
      if (rooms.length > 0) {
        const maxFloor = Math.max(...rooms.map(r => r.floorNumber));
        disabledRoomsOption.value.yAxis.max = maxFloor + 1;
      }
      
      console.log(`✅ 加载 ${rooms.length} 个停用宿舍`);
    }
  } catch(error) {
    console.error('统计停用/维修宿舍失败', error);
    disabledRoomsOption.value.xAxis.data = [];
    disabledRoomsOption.value.series[0].data = [];
  }
}

//统计各楼栋宿舍数量和床位总数方法
const loadBuidingDormAndBedCount = async () => {
  try {
    const result = await statisticsDormAndBedCountApi();
    if (result.code === 1) {
      buildingOption.value.xAxis.data = result.data.map(item => item.name);
      buildingOption.value.series[0].data = result.data.map(item => item.roomCount);
      buildingOption.value.series[1].data = result.data.map(item => item.totalBeds);
    } else {
      console.warn('统计各楼栋宿舍数量和床位总数数据有误', result);
      //数据置为空值
      buildingOption.value.xAxis.data = [];
      buildingOption.value.series[0].data = [];
      buildingOption.value.series[1].data = [];
    }
  } catch (error) {
    console.error('统计各楼栋宿舍数量和床位总数失败', error);
    //数据置为空值
    buildingOption.value.xAxis.data = [];
    buildingOption.value.series[0].data = [];
    buildingOption.value.series[1].data = [];
  }
}

//钩子函数
onMounted(() => {
  //调用统计各楼栋宿舍数量和床位总数方法
  loadBuidingDormAndBedCount();
  //停用/维修宿舍分布
  loadStopDorm();
})

</script>

<template>
  <div class="dashboard">
    <div class="chart-grid">
      <div class="chart-card">
        <!-- 统计各宿舍数量与床位总数（柱线统计图） -->
        <BaseChart :option="buildingOption" theme="dark" height="300px" />
      </div>
      <!-- 停用/维修宿舍分布（散点图） -->
      <div class="chart-card">
        <BaseChart :option="disabledRoomsOption" theme="dark" height="300px" />
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