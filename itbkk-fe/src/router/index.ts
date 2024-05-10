import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import TaskHome from '@/views/TaskHome.vue'
import TaskEdit from '@/views/TaskEdit.vue'
// import TaskAdd from '@/views/TaskAdd.vue'
import StatusTable from '@/views/StatusTable.vue'
import StatusAdd from '@/views/StatusAdd.vue'
import StatusEdit from '@/views/StatusEdit.vue'
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
    name: 'home',
    component: TaskHome
  },
  {
    path: '/task/:id?/edit',
    name: 'edit',
    component: TaskEdit
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
  history: createWebHistory(),
  routes
})

export default router
