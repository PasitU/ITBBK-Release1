import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import TaskHome from '@/views/TaskHome.vue'
import TaskEdit from '@/components/taskcomponents/TaskEdit.vue'
// import TaskAdd from '@/views/TaskAdd.vue'
import StatusTable from '@/views/StatusTable.vue'
import StatusAdd from '@/components/statuscomponents/StatusAdd.vue'
import StatusEdit from '@/components/statuscomponents/StatusEdit.vue'
// import TaskAdd from '@/views/TaskAdd.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/task'
  },
  {
    path: '/task/add', // Moved up to ensure it matches before /task/:id?
    name: 'add',
    component: TaskHome
  },
  {
    path: '/task/:id?',
    name: 'home',
    component: TaskHome
  },
  {
    path: '/task/:id?/edit',
    name: 'edit',
    component: TaskHome
  },
  {
    path: '/status',
    name: 'status',
    component: StatusTable
  },
  {
    path: '/status/add',
    name: 'statusAdd',
    component: StatusTable
  },
  {
    path: '/status/:id/edit',
    name: 'statusEdit',
    component: StatusTable
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
