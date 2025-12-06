import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

/* 在引入 Element Plus 之前设置暗黑模式 */
import 'element-plus/theme-chalk/dark/css-vars.css' // 引入暗黑主题

// 添加 Menu 组件的暗黑主题样式
const style = document.createElement('style')
style.innerHTML = `
  .el-menu {
    --el-menu-bg-color: var(--el-bg-color-overlay);
    --el-menu-hover-bg-color: var(--el-color-primary-light-9);
    --el-menu-active-bg-color: var(--el-color-primary-light-9);
    border-right: none;
  }
  
  .el-menu-item {
    color: var(--el-text-color-primary);
  }
  
  .el-menu-item:hover {
    background-color: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
  }
  
  .el-menu-item.is-active {
    background-color: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
    border-right: 2px solid var(--el-color-primary);
  }
  
  .el-sub-menu__title {
    color: var(--el-text-color-primary);
  }
  
  .el-sub-menu__title:hover {
    background-color: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
  }
`
document.head.appendChild(style)

document.documentElement.classList.add('dark') // 启用暗黑模式

/* 引入Element-plus组件库 */
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

/* Element-plus中文导入 */
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
app.use(ElementPlus, {locale: zhCn})

/* 注册所有图标 */
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(router)
app.mount('#app')