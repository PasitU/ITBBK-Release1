import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import TaskHome from '@/views/TaskHome.vue'
// import TaskAdd from '@/views/TaskAdd.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/task'
  },
  {
    path: '/task/add', // Moved up to ensure it matches before /task/:id?
    name: 'Add',
    component: TaskHome
  },
  {
    path: '/task/:id?',
    name: 'Home',
    component: TaskHome
  },
  // {
  //   path: '/task/:id?',
  //   name: 'Home',
  //   component: TaskHome
  // }
]


const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
