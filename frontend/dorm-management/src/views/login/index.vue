<script setup>
import { ref, onMounted } from 'vue'
import { empLogin } from '@/api/login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

let loginForm = ref({ username: '', password: '' })

//定义路由
const router = useRouter()

// 重置表单
const resetForm = () => {
  loginForm.value = { username: '', password: '' }
}
// 登录处理
const handleLogin = async () => {
  try {
    const result = await empLogin(loginForm.value)
    if (result.code == 1) {
      ElMessage.success('登陆成功！')
      //将token存入浏览器本地
      localStorage.setItem('loginUser', JSON.stringify(result.data))
      //跳转到首页/会重定向到首页
      router.push('/')
    } else {
      ElMessage.error(result.msg);
    }
  } catch (error) {
    ElMessage.error(error.msg)
  }
}

// 粒子动画逻辑
onMounted(() => {
  const canvas = document.getElementById('particleCanvas')
  const ctx = canvas.getContext('2d')

  // 自适应屏幕
  const resizeCanvas = () => {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
  }
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)

  // 粒子配置
  const particleConfig = {
    count: 80,
    size: 2,
    color: 'rgba(96, 165, 250, 0.7)',
    lineColor: 'rgba(96, 165, 250, 0.3)',
    lineDistance: 120,
    speed: 0.5,
    mouseAttractDistance: 150,
    mouseAttractForce: 1.2
  }

  const particles = []
  const mouse = { x: null, y: null }

  // 鼠标移动监听
  window.addEventListener('mousemove', (e) => {
    mouse.x = e.clientX
    mouse.y = e.clientY
  })

  class Particle {
    constructor() {
      this.x = Math.random() * canvas.width
      this.y = Math.random() * canvas.height
      this.vx = (Math.random() - 0.5) * particleConfig.speed
      this.vy = (Math.random() - 0.5) * particleConfig.speed
      this.size = particleConfig.size
    }

    update() {
      if (mouse.x && mouse.y) {
        const dx = mouse.x - this.x
        const dy = mouse.y - this.y
        const distance = Math.sqrt(dx * dx + dy * dy)

        if (distance < particleConfig.mouseAttractDistance) {
          const force = (particleConfig.mouseAttractDistance - distance) / particleConfig.mouseAttractDistance
          this.vx += (dx / distance) * force * particleConfig.mouseAttractForce
          this.vy += (dy / distance) * force * particleConfig.mouseAttractForce
        }
      }

      this.vx *= 0.98
      this.vy *= 0.98
      this.x += this.vx
      this.y += this.vy

      if (this.x < 0 || this.x > canvas.width) this.vx *= -1
      if (this.y < 0 || this.y > canvas.height) this.vy *= -1
    }

    draw() {
      ctx.beginPath()
      ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2)
      ctx.fillStyle = particleConfig.color
      ctx.fill()
    }
  }

  function drawLines() {
    for (let i = 0; i < particles.length; i++) {
      for (let j = i + 1; j < particles.length; j++) {
        const p1 = particles[i]
        const p2 = particles[j]
        const dx = p1.x - p2.x
        const dy = p1.y - p2.y
        const distance = Math.sqrt(dx * dx + dy * dy)

        if (distance < particleConfig.lineDistance) {
          ctx.beginPath()
          ctx.strokeStyle = particleConfig.lineColor
          ctx.lineWidth = 0.5
          ctx.moveTo(p1.x, p1.y)
          ctx.lineTo(p2.x, p2.y)
          ctx.stroke()
        }
      }
    }
  }

  function initParticles() {
    particles.length = 0
    for (let i = 0; i < particleConfig.count; i++) {
      particles.push(new Particle())
    }
  }

  function animate() {
    ctx.fillStyle = 'rgba(15, 23, 42, 0.1)'
    ctx.fillRect(0, 0, canvas.width, canvas.height)

    particles.forEach(particle => {
      particle.update()
      particle.draw()
    })

    drawLines()
    requestAnimationFrame(animate)
  }

  initParticles()
  animate()
})

//实现回车键完成跳转下一行以及登录功能
const inputRefs = ref([])
//回车处理函数
const handleEnter = (index) => {
  if (index < inputRefs.value.length - 1) {
    inputRefs.value[index + 1]?.focus() // 聚焦下一个
  }else{
    // 如果是最后一个，提交登录
    handleLogin();
  }
}

</script>

<template>
  <!-- 粒子背景 Canvas -->
  <canvas id="particleCanvas" class="particle-canvas"></canvas>

  <!-- 登录表单 -->
  <div class="login-container">
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">宿舍管理系统</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" :ref="el => inputRefs[0] = el"
            @keydown.enter="() => handleEnter(0)"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码" :ref="el => inputRefs[1] = el"
            @keydown.enter="() => handleEnter(1)"></el-input>
        </el-form-item>

        <el-form-item>
          <div class="button-group">
            <el-button class="button login-btn" type="primary" @click="handleLogin">登 录</el-button>
            <el-button class="button reset-btn" type="info" @click="resetForm">重 置</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
/* Canvas 粒子背景 */
.particle-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #0f172a;
  z-index: -1;
}

/* 登录容器 - 向上移动 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  transform: translateY(-10%);
  /* 向上移动10% */
}

.login-form {
  max-width: 400px;
  width: 100%;
  padding: 30px;
  margin: 0 auto;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow:
    0 4px 20px rgba(0, 0, 0, 0.3),
    0 0 15px rgba(96, 165, 250, 0.1);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

/* 标题样式 */
.title {
  font-size: 28px;
  font-family: '楷体', 'Microsoft YaHei', sans-serif;
  text-align: center;
  margin-bottom: 30px;
  color: #1e293b;
  font-weight: bold;
  letter-spacing: 1px;
}

/* 表单项 label 文字颜色修正 */
.login-form :deep(.el-form-item__label) {
  color: #1e293b !important;
  font-weight: 500;
  font-size: 15px;
}

/* 输入框样式优化 */
.login-form :deep(.el-input__wrapper) {
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

/* 输入框聚焦状态 */
.login-form :deep(.el-input__wrapper):focus,
.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #3b82f6;
  box-shadow: 0 0 0 1px #3b82f6;
}

/* 按钮组 - 水平排列 */
.button-group {
  display: flex;
  gap: 15px;
  width: 100%;
}

/* 按钮基础样式 */
.button {
  flex: 1;
  font-weight: 500;
  letter-spacing: 1px;
  border-radius: 8px;
  height: 40px;
}

/* 登录按钮强调色 */
.login-btn {
  background: linear-gradient(to right, #3b82f6, #1d4ed8);
  border-color: #1d4ed8;
}

/* 重置按钮 */
.reset-btn {
  background-color: #e2e8f0;
  border-color: #cbd5e1;
  color: #475569;
}

.reset-btn:hover {
  background-color: #cbd5e1;
}

.login-form :deep(.el-input__inner) {
  color: #1e293b !important;
  /* 深灰色文字，接近黑色 */
  font-weight: 500;
  /* 稍微加粗，更清晰 */
}

/* 兼容新版本 Element Plus（使用 wrapper 的情况） */
.login-form :deep(.el-input__wrapper input) {
  color: #1e293b !important;
  font-weight: 500;
}
</style>