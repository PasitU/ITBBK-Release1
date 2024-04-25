import { createRouter, createWebHistory } from 'vue-router'
import taskHome from '@/views/TaskHome.vue'
import taskDetail from '@/views/TaskDetail.vue'

const routes = [
  {
    path: '/task/:id?',
    name: 'Home',
    component: taskHome
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
