<script setup>
import { ref, onMounted } from 'vue'
import BaseChart from '@/components/BaseChart.vue'
// 导入真实的API方法
import { 
  statisticsCheckinTrendApi
} from '@/api/timeReport'

// 时间筛选
const timeRange = ref('month')
const customDateRange = ref([])

// 图表配置
const monthlyCheckinOption = ref({
  title: {
    text: '入住人数趋势',
    subtext: '近1月统计',
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
    trigger: 'axis',
    backgroundColor: 'rgba(0, 0, 0, 0.8)',
    borderColor: '#666',
    borderWidth: 1,
    padding: [10, 15],
    textStyle: {
      color: '#fff',
      fontSize: 13
    },
    formatter: function (params) {
      return `
        <div style="font-weight:bold;margin-bottom:5px">📅 ${params[0].name}</div>
        <div>
          <span style="color:#ccc">新增入住: </span>
          <span style="color:#91cc75;font-size:16px">${params[0].value}</span>
          <span style="color:#ccc"> 人</span>
        </div>
      `;
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '10%',
    top: '18%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [],
    axisLabel: {
      color: '#ccc',
      fontSize: 12,
      rotate: 0
    },
    axisLine: {
      lineStyle: {
        color: '#666'
      }
    }
  },
  yAxis: {
    type: 'value',
    name: '人数',
    nameTextStyle: {
      color: '#ccc'
    },
    axisLabel: { 
      color: '#ccc',
      formatter: '{value}人' 
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(255, 255, 255, 0.1)',
        type: 'dashed'
      }
    }
  },
  series: [{
    name: '新增入住',
    type: 'line',
    smooth: true,
    symbol: 'circle',
    symbolSize: 8,
    lineStyle: { 
      width: 3, 
      color: '#5470c6' 
    },
    itemStyle: { 
      color: '#5470c6',
      borderColor: '#fff',
      borderWidth: 2
    },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(84, 112, 198, 0.3)' },
          { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }
        ]
      }
    },
    data: []
  }]
})

// 加载数据
const loadCheckinTrendData = async (rangeType = 'month') => {
  try {
    const params = { rangeType };
    
    // 如果是自定义范围，添加日期参数
    if (rangeType === 'custom' && customDateRange.value.length === 2) {
      params.startDate = customDateRange.value[0];
      params.endDate = customDateRange.value[1];
    }
    
    const result = await statisticsCheckinTrendApi(params);
    
    if (result.code === 1 && result.data) {
      const data = result.data;
      
      // 更新图表数据
      monthlyCheckinOption.value.xAxis.data = data.labels || [];
      monthlyCheckinOption.value.series[0].data = data.values || [];
      
      // 动态调整显示
      const labelCount = (data.labels || []).length;
      if (labelCount > 8) {
        monthlyCheckinOption.value.xAxis.axisLabel.rotate = 45;
      } else {
        monthlyCheckinOption.value.xAxis.axisLabel.rotate = 0;
      }
      
      // 更新标题
      const rangeTextMap = {
        day: '近1天',
        week: '近1周', 
        month: '近1月',
        quarter: '近3月',
        year: '近1年',
        custom: '自定义范围'
      }
      
      const rangeText = rangeTextMap[rangeType] || '近1月';
      const total = (data.values || []).reduce((sum, value) => sum + value, 0);
      monthlyCheckinOption.value.title.subtext = `${rangeText} · 共 ${total} 人入住`;
      
    } else {
      throw new Error(result.msg || 'API返回错误');
    }
    
  } catch (error) {
    console.error('获取数据失败:', error);
    monthlyCheckinOption.value.xAxis.data = [];
    monthlyCheckinOption.value.series[0].data = [];
    monthlyCheckinOption.value.title.subtext = '数据加载失败';
  }
}

// 时间筛选切换
const handleTimeRangeChange = (range) => {
  if (range !== 'custom') {
    // 切换到非自定义时，清空日期选择器
    customDateRange.value = [];
  }
  timeRange.value = range;
  loadCheckinTrendData(range);
}

// 自定义日期选择
const handleCustomDateChange = (dates) => {
  if (dates && dates.length === 2) {
    customDateRange.value = dates;
    timeRange.value = 'custom';
    loadCheckinTrendData('custom');
  }
}

// 清空日期选择器
const clearCustomDate = () => {
  customDateRange.value = [];
  // 清空后切换到默认的月份视图
  timeRange.value = 'month';
  loadCheckinTrendData('month');
}

// 初始化加载
onMounted(() => {
  loadCheckinTrendData('month');
})
</script>

<template>
  <div class="dashboard">
    <!-- 时间筛选器 -->
    <div class="time-filter">
      <div class="time-range-controls">
        <el-radio-group v-model="timeRange" @change="handleTimeRangeChange" class="radio-group">
          <el-radio-button label="day">近1天</el-radio-button>
          <el-radio-button label="week">近1周</el-radio-button>
          <el-radio-button label="month">近1月</el-radio-button>
          <el-radio-button label="quarter">近3月</el-radio-button>
          <el-radio-button label="year">近1年</el-radio-button>
        </el-radio-group>
      </div>
      
      <div class="date-picker-wrapper">
        <el-date-picker
          v-model="customDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          @change="handleCustomDateChange"
          class="date-picker"
          :clearable="false"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        />
        <el-button 
          v-if="customDateRange.length > 0"
          @click="clearCustomDate"
          class="clear-btn"
          type="danger"
          plain
          size="small"
          :icon="RefreshLeft"
        >
          清空
        </el-button>
      </div>
    </div>
    
    <!-- 图表区域 -->
    <div class="chart-container">
      <div class="chart-card">
        <BaseChart 
          :option="monthlyCheckinOption" 
          theme="dark" 
          :height="'350px'" 
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.time-filter {
  margin-bottom: 20px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 15px;
}

.time-range-controls {
  flex: 1;
  min-width: 400px;
}

.date-picker-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
}

.date-picker {
  width: 240px;
}

.clear-btn {
  white-space: nowrap;
  height: 32px;
}

.chart-container {
  flex: 1;
  min-height: 0; /* 重要：允许图表容器压缩 */
}

.chart-card {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 16px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 深色主题适配 */
:deep(.el-radio-group) {
  --el-radio-button-checked-bg-color: #409eff;
  --el-radio-button-checked-text-color: #fff;
  --el-radio-button-checked-border-color: #409eff;
  --el-radio-button-hover-color: #666;
}

:deep(.el-radio-button__inner) {
  background: rgba(255, 255, 255, 0.1);
  color: #ccc;
  border: 1px solid #666;
}

:deep(.el-radio-button__orig-radio:checked + .el-radio-button__inner) {
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
  box-shadow: none;
}

:deep(.el-date-editor) {
  --el-bg-color: rgba(255, 255, 255, 0.1);
  --el-text-color-primary: #fff;
  --el-border-color-light: #666;
  --el-border-color-hover: #888;
}

:deep(.el-range-separator) {
  color: #ccc;
}

:deep(.el-input__inner) {
  color: #fff;
}

:deep(.el-input__prefix) .el-icon,
:deep(.el-input__suffix) .el-icon {
  color: #ccc;
}

/* 使图表填满卡片 */
.chart-card :deep(.chart-container) {
  flex: 1;
  min-height: 0;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .time-filter {
    flex-direction: column;
    align-items: stretch;
  }
  
  .time-range-controls {
    min-width: 100%;
  }
  
  .date-picker-wrapper {
    width: 100%;
    justify-content: space-between;
  }
  
  .date-picker {
    flex: 1;
  }
}
</style>