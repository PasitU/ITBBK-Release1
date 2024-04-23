import { createRouter, createWebHistory } from 'vue-router'
import taskHome from '@/views/TaskHome.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: taskHome,
    }
  ]
})

export default router
