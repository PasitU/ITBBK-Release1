<template>
  <!-- :default-size="displaySidebar ? 0 : 20" -->
  <ResizablePanelGroup direction="horizontal" class="w-24 h-screen rounded-lg border">
    <ResizablePanel
      v-show="displaySidebar"
      id="handle-demo-panel-3"
      :default-size="displaySidebar ? 10 : 0"
    >
      <div class="flex h-full items-center justify-center p-6">
        <span class="font-semibold">Sidebar {{ $route.params.id }} </span>
      </div>
    </ResizablePanel>
    <ResizableHandle id="handle-demo-handle-1" with-handle />
    <ResizablePanel
      id="handle-demo-panel-1"
      :default-size="displaySidebar ? 50 : 20"
      class="h-screen"
    >
      <div>
        <div class="flex justify-center items-center p-6">
          <h1>
            <span class="font-bold text-3xl">INTEGRATED PROJECT ITBKK-SY-1 </span>
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
                ></TableHead>
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
                  >Status
                </TableHead>
              </TableRow>
            </TableHeader>

            <TableBody>
              <TableRow class="itbkk-item" v-for="task in tasks" :key="task.id">
                <TableCell class="border border-solid border-black p-0">
                  <div class="flex items-center justify-center gap-3">
                    <p class="itbkk-title">{{ task.id }}</p>
                    <details class="dropdown">
                      <summary class="block">
                        <v-icon name="co-settings" tabindex="0" role="button"> </v-icon>
                      </summary>
                      <ul
                        tabindex="0"
                        class="shadow menu dropdown-content z-[1] bg-white rounded-box w-auto"
                      >
                        <li class="text-yellow-500">
                          <a @click="editTask(task.id)"><v-icon name="fa-edit"></v-icon>Edit</a>
                        </li>
                        <li
                          class="text-red-700"
                          onclick="my_modal_1.showModal()"
                          @click="openDeleteDialog(task.title)"
                        >
                          <a><v-icon name="md-deleteforever"></v-icon>Delete</a>
                        </li>
                      </ul>
                    </details>
                  </div>
                  <dialog id="my_modal_1" class="modal">
                    <div class="modal-box bg-white">
                      <h3 class="font-bold text-lg">Delete a Task</h3>
                      <p class="py-4 break-words">
                        Do you want to delete the task "{{ currentTaskId }}"?
                      </p>
                      <div class="modal-action">
                        <form method="dialog">
                          <button class="btn bg-white">Close</button>
                        </form>
                      </div>
                    </div>
                  </dialog>
                </TableCell>

                <TableCell
                  @click="openTaskDetail(task.id)"
                  class="border border-solid border-black"
                >
                  <p class="itbkk-title">
                    {{ task.title }}
                  </p>
                </TableCell>
                <TableCell
                  @click="openTaskDetail(task.id)"
                  class="border border-solid border-black"
                >
                  <p class="itbkk-assignees" :class="{ italic: !task.assignees }">
                    {{ task.assignees || 'Unassigned' }}
                  </p>
                </TableCell>
                <TableCell
                  @click="openTaskDetail(task.id)"
                  class="border border-solid border-black"
                >
                  <button
                    :class="getStatusClass(task.status)"
                    class="btn btn-active h-[1rem] min-h-[1.8rem] text-black"
                    @click="toggleSidebar"
                  >
                    <p class="itbkk-status">
                      {{ changeStatusName(task.status) }}
                    </p>
                  </button></TableCell
                >
              </TableRow>
            </TableBody>
          </Table>
          <p v-if="isNull" class="w-full p-6 text-center text-red-500">No tasks found</p>
          <button class="btn h-[1rem] min-h-[1.8rem]" @click="toggleSidebar">
            {{ displaySidebar ? 'Close sidebar' : 'Open sidebar' }}
          </button>
          {{ displaySidebar ? 'Sidebar is on' : 'Sidebar is off' }}
        </div>
      </div>
    </ResizablePanel>
    <Button
      class="bg-blue-700 absolute bottom-8 right-16 text-18 text-red-50 hover:bg-blue-800"
      @click="navigateToAddTask"
      >Add Task</Button
    >
  </ResizablePanelGroup>
  <Teleport to="#modal" v-if="$route.params.id && $route.params.id.length > 0">
    <TaskDetail></TaskDetail>
  </Teleport>
  <!-- <Teleport to="#modal" v-if="$route.params.id && $route.params.id.length > 0">
    <TaskDetail></TaskDetail>
  </Teleport> -->

  <Teleport to="#addmodal" v-if="$route.path === '/task/add'">
    <TaskAdd></TaskAdd>
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
import TaskAdd from './TaskAdd.vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { onMounted } from 'vue'
import { Button } from '@/components/ui/button'
import { getAllTasks } from '@/api/taskService'

const tasks = ref([])
const router = useRouter()
const isNull = ref(false)

const currentTaskId = ref(null)

onMounted(async () => {
  try {
    tasks.value = await getAllTasks()
  } catch (error) {
    isNull.value = true
  }
})
const navigateToAddTask = () => {
  router.push('/task/add')
}

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

const changeStatusName = (status) => {
  switch (status) {
    case 'NO_STATUS':
      return 'No Status'
    case 'TO_DO':
      return 'To Do'
    case 'DOING':
      return 'Doing'
    case 'DONE':
      return 'Done'
    default:
      return ''
  }
}

const openDeleteDialog = (taskId) => {
  currentTaskId.value = taskId
}



const displaySidebar = ref(false)

const toggleSidebar = () => {
  displaySidebar.value = !displaySidebar.value
}
</script>

<style></style>
