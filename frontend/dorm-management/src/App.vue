<!-- App.vue -->
<script setup>
// 删除 Layout 的导入
</script>

<template>
  <!-- 只有一个 router-view，由路由系统决定渲染谁 -->
  <router-view />
</template>

<style>
/* 全局样式 - 放在 App.vue 中（不带 scoped） */
html, body {
  margin: 0;
  padding: 0;
  overflow: hidden !important;
  height: 100vh;
  width: 100vw;
  position: fixed;
}

#app {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* 隐藏所有滚动条 */
::-webkit-scrollbar {
  display: none;
  width: 0;
  height: 0;
}

* {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>