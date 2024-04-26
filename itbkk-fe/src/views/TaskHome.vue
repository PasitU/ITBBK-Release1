<template>
  <!-- :default-size="displaySidebar ? 0 : 20" -->
  <ResizablePanelGroup direction="horizontal" class="w-24 h-screen rounded-lg border">
    <ResizablePanel id="handle-demo-panel-0">
      <div class="flex h-full items-center justify-center p-6" :default-size="sidebarSize">
        <span class="font-semibold">Sidebar {{ $route.params.id }} </span>
      </div>
    </ResizablePanel>
    <ResizableHandle id="handle-demo-handle-1" with-handle />
    <ResizablePanel
      id="handle-demo-panel-2"
      :default-size="contentSize"
      class="h-screen"
    >
      <div>
        <div class="flex justify-center items-center p-6">
          <h1>
            <span class="font-bold text-3xl"
              >INTEGRATED PROJECT ITBKK-SY-1 {{ displaySidebar }}
            </span>
          </h1>
        </div>

        <div class="h-full w-full p-6">
          <Table class="text-black border border-solid border-black">
            <TableCaption v-if="!isNull" class="pb-4 text-red-800 font-bold text-[1.5rem]"
              >Total {{ tasks.length }} Tasks</TableCaption
            >
            <TableHeader>
              <TableRow class="border border-solid border-black">
                <TableHead
                  class="text-red-800 font-bold text-[1.5rem] border border-solid border-black"
                  >Title</TableHead
                >
                <TableHead
                  class="text-red-800 font-bold text-[1.5rem] border border-solid border-black"
                  >Assignees</TableHead
                >
                <TableHead
                  class="text-red-800 font-bold text-[1.5rem] border border-solid border-black"
                  >Status</TableHead
                >
              </TableRow>
            </TableHeader>

            <TableBody>
              <TableRow
                class="itbkk-item"
                v-for="task in tasks"
                :key="task.id"
                @click="openTaskDetail(task.id)"
              >
                <TableCell class="itbkk-title border border-solid border-black">{{
                  task.title
                }}</TableCell>
                <TableCell class="itbkk-assignees border border-solid border-black">
                  {{ task.assignees }}
                </TableCell>
                <TableCell class="itbkk-status border border-solid border-black">
                  <button
                    :class="getStatusClass(task.status)"
                    class="btn btn-active h-[1rem] min-h-[1.8rem] text-black"
                    @click="toggleSidebar"
                  >
                    {{ task.status }}
                  </button></TableCell
                >
              </TableRow>
            </TableBody>
          </Table>
          <p v-if="isNull" class="w-full p-6 text-center text-red-500">No tasks found</p>
          <button class="btn h-[1rem] min-h-[1.8rem]" @click="toggleSidebar">Button</button>
        </div>
      </div>
    </ResizablePanel>
  </ResizablePanelGroup>
  <Teleport to="#modal" v-if="route.params.id.length > 0">
    <TaskDetail></TaskDetail>
  </Teleport>
</template>

<script setup>
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table'

import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'
import TaskDetail from './TaskDetail.vue'
import { useRouter, useRoute } from 'vue-router'
import { ref } from 'vue'
import { onMounted } from 'vue'
import { getAllTasks } from '@/api/taskService'

const tasks = ref([])
const router = useRouter()
const isNull = ref(false)

const route = useRoute()
onMounted(async () => {
  try {
    tasks.value = await getAllTasks()
  } catch (error) {
    isNull.value = true
  }
})

const openTaskDetail = async (id) => {
  await router.push(`/task/${id}`)
}

const getStatusClass = (status) => {
  switch (status) {
    case 'NO_STATUS':
      return 'bg-gray-500'
    case 'TO_DO':
      return 'bg-blue-500'
    case 'DOING':
      return 'bg-yellow-500'
    case 'DONE':
      return 'bg-green-500'
    default:
      return ''
  }
}

const sidebarSize = ref(25)
const contentSize = ref(75)
const displaySidebar = ref(true)

const toggleSidebar = () => {
  displaySidebar.value = !displaySidebar.value
  sidebarSize.value = displaySidebar.value ? 25 : 0
  contentSize.value = displaySidebar.value ? 75 : 100 
}
</script>

<style lang="scss" scoped></style>
