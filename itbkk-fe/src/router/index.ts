import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import TaskHome from '@/views/TaskHome.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/task' 
  },
  {
    path: '/task/:id?',
    name: 'Home',
    component: TaskHome
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
