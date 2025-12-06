import { createRouter, createWebHistory } from 'vue-router';

import IndexView from '@/views/index/index.vue';
import StuView from '@/views/stu/index.vue';
import EmpView from '@/views/emp/index.vue';
import dormReportView from '@/views/dormReport/index.vue';
import StuReportView from '@/views/stuReport/index.vue';
import LogView from '@/views/timeReport/index.vue';
import LoginView from '@/views/login/index.vue';
import DormView from '@/views/dorm/index.vue';
import FloorView from '@/views/floor/index.vue';
import LayoutView from '@/views/layout/index.vue';
import DeptView from '@/views/dept/index.vue';
import BuildingView from '@/views/building/index.vue';
import MajorView from '@/views/major/index.vue';
import ClassView from '@/views/class/index.vue';
import ManagementView from '@/views/managementReport/index.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '',
      component: LayoutView, //布局容器
      redirect: '/index', //重定向
      children: [
        { path: 'index', name: 'index', component: IndexView },
        { path: 'stu', name: 'stu', component: StuView },
        { path: 'emp', name: 'emp', component: EmpView },
        { path: 'dormReport', name: 'dormReport', component: dormReportView },
        { path: 'stuReport', name: 'stuReport', component: StuReportView },
        { path: 'timeReport', name: 'timeReport', component: LogView },
        { path: 'dorm', name: 'dorm', component: DormView },
        { path: 'floor', name: 'floor', component: FloorView },
        { path: 'dept', name: 'dept', component: DeptView},
        { path: 'building', name: 'building', component: BuildingView},
        { path: 'major', name: 'major', component: MajorView},
        { path: 'class', name: 'class', component: ClassView},
        { path: 'managementReport', name: 'managementReport', component: ManagementView}
      ]
    },
    { path: '/login', name: 'login', component: LoginView }
  ]
})

export default router;

/* const routes = [
  { path: '/index', component: IndexView },
  { path: '/stu', component: StuView },
  { path: '/emp', component: EmpView },
  { path: '/empReport', component: EmpReportView },
  { path: '/stuReport', component: StuReportView },
  { path: '/log', component: LogView },
  { path: '/login', component: LoginView },
  { path: '/dorm', component: DormView},
  { path: '/floor', component: FloorView}, 
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router; */