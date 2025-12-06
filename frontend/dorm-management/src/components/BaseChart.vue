<template>
  <div ref="chartRef" class="chart-container"></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch, nextTick, computed } from 'vue'
import * as echarts from 'echarts/core'
import {
  LineChart,
  BarChart,
  PieChart,
  GaugeChart, //仪表盘
  ScatterChart, //散点图
} from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  PolarComponent,
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  LineChart,
  BarChart,
  PieChart,
  CanvasRenderer,
  PolarComponent,
  GaugeChart,
  ScatterChart
])

const props = defineProps({
  option: {
    type: Object,
    required: true
  },
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '400px'
  },
  theme: {
    type: String,
    default: '' // 'dark' 或 ''（默认）
  },
  transparent: {  // 新增：是否透明背景
    type: Boolean,
    default: true
  }
})

const chartRef = ref(null)
let chartInstance = null

// 计算合并后的配置（强制透明背景）
const mergedOption = computed(() => {
  const baseOption = { ...props.option }

  // 如果启用透明背景
  if (props.transparent) {
    // 确保背景透明
    baseOption.backgroundColor = 'transparent'

    // 针对暗黑主题，还需要调整其他组件的颜色
    if (props.theme === 'dark') {
      // 确保文字颜色适合暗黑背景
      if (!baseOption.textStyle) {
        baseOption.textStyle = {}
      }
      if (!baseOption.textStyle.color) {
        baseOption.textStyle.color = '#ccc'
      }

      // 确保标题颜色
      if (baseOption.title) {
        if (!baseOption.title.textStyle) {
          baseOption.title.textStyle = {}
        }
        if (!baseOption.title.textStyle.color) {
          baseOption.title.textStyle.color = '#fff'
        }
        if (baseOption.title.subtextStyle && !baseOption.title.subtextStyle.color) {
          baseOption.title.subtextStyle.color = '#aaa'
        }
      }

      // 确保图例颜色
      if (baseOption.legend) {
        if (!baseOption.legend.textStyle) {
          baseOption.legend.textStyle = {}
        }
        if (!baseOption.legend.textStyle.color) {
          baseOption.legend.textStyle.color = '#ccc'
        }
      }
    }
  }

  return baseOption
})

const initChart = () => {
  if (!chartRef.value) return

  if (chartInstance) {
    chartInstance.dispose()
  }

  // 使用主题初始化
  chartInstance = echarts.init(chartRef.value, props.theme)
  chartInstance.setOption(mergedOption.value)
}

onMounted(() => {
  nextTick(() => {
    initChart()
  })
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
  }
  window.removeEventListener('resize', handleResize)
})

// 监听 theme 变化
watch(
  () => props.theme,
  () => {
    nextTick(() => {
      initChart()
    })
  }
)

// 监听 option 变化
watch(
  mergedOption,
  (newOption) => {
    if (chartInstance) {
      chartInstance.setOption(newOption, {
        replaceMerge: ['xAxis', 'yAxis', 'series']
      })
    }
  },
  { deep: true }
)

const handleResize = () => {
  if (chartInstance) {
    chartInstance.resize()
  }
}
</script>

<style scoped>
.chart-container {
  width: 100%;
  height: 400px;
  background: transparent !important;
  /* 确保容器背景透明 */
}
</style>