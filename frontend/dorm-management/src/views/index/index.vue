<!-- <template>
  <div class="full-screen-image">
    <img src="../../assets/index.jpg" alt="Full screen image">
  </div>
</template>

<script setup>
// 这里可以添加你的逻辑
</script>

<style scoped>
.full-screen-image {
  position: relative;
  width: 100%;
  height: 100%;
}

.full-screen-image img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持比例填充 */
}
</style> -->
<!-- AboutMe.vue -->
<template>
  <div class="about-me-page">
    <!-- 视频背景 -->
    <div class="video-bg">
      <video autoplay muted loop class="bg-video">
        <source src="@/assets/video/zx.mp4" type="video/mp4" />
        您的浏览器不支持视频标签。
      </video>
      <!-- 融合遮罩 -->
      <div class="overlay"></div>
    </div>

    <!-- 页面内容 -->
    <div class="about-content">
      <div class="profile-header">
        <img src="@/assets/images/my.jpg" alt="我的头像" class="avatar" />
        <h2>你好，我是望舒</h2>
        <p>一名热爱编程、梦想成为全栈开发工程师的Java练习生</p>
      </div>

      <div class="content-wrapper">
        <!-- 左侧：关于我 -->
        <section class="about-section">
          <h3>📌 关于我</h3>
          <p>
            我是一名计算机专业的大学生，目前专注于 <strong>Java 后端开发</strong>。犹记得那年第一次打开IDEA，
            <strong>书写的Hello World，</strong>由此打开了我的编程生涯。后面经过了一年多的学习，我已经将JavaWeb的技术基本掌握，
            可是那些都是跟着老师做，并不知道自己能不能独立开发项目，所以我自己从零搭建了<strong>宿舍管理系统</strong>。
            借着这个首页为后来想学Java的同学提供些许方向。
          </p>

          <h3>🎯 学习路线</h3>
          <ul>
            <li><strong>前端网页制作</strong>：熟悉HTML,CSS,JavaScript为后期使用vue做准备</li>
            <li><strong>Java基础</strong>：掌握Java基本语法，熟悉面向对象三大特征，集合，流，多线程等</li>
            <li><strong>JavaWeb</strong>：掌握SpringMVC，Mybatis，单体项目可以写苍穹外卖，不要说什么烂大街，学的是思路和开发流程</li>
            <li><strong>SpringCloud微服务</strong>：掌握网关，Nacos，Redis，RabbitMQ，ElasticSearch</li>
          </ul>

          <h3>🛠️ 正在学习</h3>
          <ul>
            <li>Spring Boot 自动配置原理与源码浅析</li>
            <li>MyBatis-Plus 高级特性与代码生成器实践</li>
            <li>构建一个基于 SSM 的个人博客系统（无框架依赖）</li>
            <li>LeetCode 算法训练与常见设计模式应用</li>
          </ul>

          <h3>📊 技能掌握</h3>
          <div class="skills">
            <div class="skill-item">
              <label>Java</label>
              <div class="skill-bar"><div class="skill-progress java"></div></div>
              <span>85%</span>
            </div>
            <div class="skill-item">
              <label>Spring Boot</label>
              <div class="skill-bar"><div class="skill-progress spring"></div></div>
              <span>80%</span>
            </div>
            <div class="skill-item">
              <label>MySQL</label>
              <div class="skill-bar"><div class="skill-progress mysql"></div></div>
              <span>75%</span>
            </div>
            <div class="skill-item">
              <label>Maven</label>
              <div class="skill-bar"><div class="skill-progress maven"></div></div>
              <span>70%</span>
            </div>
            <div class="skill-item">
              <label>Git & GitHub</label>
              <div class="skill-bar"><div class="skill-progress git"></div></div>
              <span>80%</span>
            </div>
          </div>
        </section>

        <!-- 右侧：时间轴 -->
        <section class="timeline-section">
          <h3>📅 成长时间轴</h3>
          <div class="timeline-horizontal">
            <div v-for="(item, index) in timeline" :key="index" class="timeline-item" :class="{ left: item.align === 'left', right: item.align === 'right' }">
              <div class="timeline-content">
                <div class="timeline-date">{{ item.date }}</div>
                <div class="timeline-card">
                  <strong>{{ item.title }}</strong>
                  <p>{{ item.desc }}</p>
                </div>
              </div>
              <div class="timeline-dot"></div>
            </div>
          </div>
        </section>
      </div>

      <!-- 联系我 -->
      <div class="contact-section">
        <h3>📬 联系我</h3>
        <p>
          欢迎通过邮箱
          <a href="mailto:1541317260@qq.com">1541317260@qq.com</a>
          或在
          <a href="https://github.com/corezhy" target="_blank">GitHub</a>
          找到我。
        </p>
      </div>

      <!-- 底部 -->
      <footer class="footer">
        &copy; 2025 咸鱼梦想家. 保持好奇，持续前行。
      </footer>
    </div>
  </div>
</template>

<script setup>
const timeline = [
  { date: '2024年9月', title: '大学入学，接触编程', desc: '第一门课是java语言，从此开启了我的计算机之旅', align: 'left' },
  { date: '2024年11月', title: '学习 Java 基础', desc: '掌握面向对象、集合框架、异常处理等核心知识', align: 'right' },
  { date: '2025年3月', title: '学习JavaWeb', desc: '掌握Spring,MyBatis,前端,vue', align: 'left' },
  { date: '2025年8月', title: '掌握若依框架', desc: '基于若依完成帝可得管理系统', align: 'right' },
  { date: '2025年10月', title: '学完SpringCloud微服务， 至此JavaWeb全套路线学完', desc: '目标：打造高性能、可维护的后端系统，向全栈进发', align: 'left' }
]
</script>

<style scoped>
.about-me-page {
  position: relative;
  width: 100%;
  min-height: 100vh;
  overflow: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 视频背景 */
.video-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -2;
  overflow: hidden;
}

.bg-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 60, 90, 0.5);
  z-index: -1;
}

/* 内容主体 */
.about-content {
  position: relative;
  z-index: 1;
  color: #e6f7ff;
  padding: 40px 20px;
  max-width: 900px;
  margin: 0 auto;
  animation: fadeInUp 1s ease forwards;
  opacity: 0;
  transform: translateY(20px);
}

@keyframes fadeInUp {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 头像区域 */
.profile-header {
  text-align: center;
  margin-bottom: 2rem;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #00aaa0;
  box-shadow: 0 0 10px rgba(0, 170, 160, 0.4);
  margin-bottom: 0.8rem;
}

.profile-header h2 {
  margin: 0.5rem 0;
  color: #00d072;
  text-shadow: 0 0 5px rgba(0, 208, 114, 0.5);
}

.profile-header p {
  color: #ccc;
  font-size: 1rem;
}

/* 布局 */
.content-wrapper {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin: 1.5rem 0;
}

.about-section,
.timeline-section {
  flex: 1;
  min-width: 300px;
}

.about-section h3,
.timeline-section h3 {
  color: #00aaa0;
  border-left: 4px solid #00d072;
  padding-left: 0.8rem;
  margin-top: 1.2rem;
  font-weight: 600;
}

.about-section ul {
  line-height: 1.8;
  color: #f0faff;
}

/* 技能条 */
.skills {
  margin-top: 1rem;
}

.skill-item {
  display: flex;
  align-items: center;
  margin-bottom: 0.8rem;
  color: #fff;
}

.skill-item label {
  width: 100px;
  font-weight: bold;
}

.skill-bar {
  flex-grow: 1;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  overflow: hidden;
}

.skill-progress {
  height: 100%;
  background: #00aaa0;
  border-radius: 4px;
  transition: width 1s ease-in-out;
}

.skill-progress.java { width: 85%; }
.skill-progress.spring { width: 80%; }
.skill-progress.mysql { width: 75%; }
.skill-progress.maven { width: 70%; }
.skill-progress.git { width: 80%; }

/* 时间轴 */
.timeline-horizontal {
  position: relative;
  margin-left: 20px;
}

.timeline-horizontal::before {
  content: '';
  position: absolute;
  left: 0;
  top: 20px;
  height: 2px;
  width: 100%;
  background: #00aaa0;
}

.timeline-item {
  position: relative;
  margin-bottom: 2.5rem;
  display: flex;
  align-items: center;
}

.timeline-item.left {
  justify-content: flex-start;
}

.timeline-item.right {
  justify-content: flex-end;
}

.timeline-dot {
  width: 12px;
  height: 12px;
  background: #00d072;
  border: 2px solid white;
  border-radius: 50%;
  box-shadow: 0 0 0 2px #00aaa0;
  position: absolute;
  left: -6px;
  z-index: 1;
}

.timeline-content {
  max-width: 380px;
  background: rgba(0, 100, 130, 0.4);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  padding: 0.8rem;
  border-radius: 10px;
  border: 1px solid rgba(0, 170, 160, 0.5);
}

.timeline-date {
  font-size: 0.85rem;
  color: #00d072;
  font-weight: bold;
}

.timeline-card strong {
  color: #a8eb12;
}

.timeline-card p {
  color: #e6f7ff;
  margin: 0.4rem 0 0;
  font-size: 0.9rem;
}

/* 联系我 */
.contact-section {
  text-align: center;
  margin: 2.5rem 0 1.5rem;
  padding: 1rem;
  background: rgba(0, 170, 160, 0.2);
  border-radius: 10px;
  backdrop-filter: blur(6px);
  border: 1px solid rgba(0, 208, 114, 0.3);
}

.contact-section a {
  color: #a8eb12;
  text-decoration: none;
  font-weight: bold;
}

.contact-section a:hover {
  color: #00d072;
  text-decoration: underline;
}

/* 底部 */
.footer {
  text-align: center;
  padding: 1rem 0;
  color: #99ddbb;
  font-size: 0.9rem;
  margin-top: 2rem;
  border-top: 1px dashed #00aaa0;
  padding-top: 0.8rem;
}

/* 响应式 */
@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }
  .about-section, .timeline-section {
    min-width: 100%;
  }
  .timeline-horizontal {
    margin-left: 10px;
  }
  .profile-header h2 {
    font-size: 1.5rem;
  }
}
</style>