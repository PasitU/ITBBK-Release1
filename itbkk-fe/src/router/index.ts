import { createRouter, createWebHistory } from 'vue-router'
import taskHome from '@/views/TaskHome.vue'
import taskDetail from '@/views/TaskDetail.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: taskHome
  },
  {
    path: '/task/:id',
    name: 'TaskDetail',
    component: taskDetail
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
