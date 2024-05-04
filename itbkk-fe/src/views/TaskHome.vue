<template>
  <div data-theme="light">
    <!-- :default-size="displaySidebar ? 0 : 20" -->
    <ResizablePanelGroup direction="horizontal" class="w-24 h-screen rounded-lg">
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
              <span class="font-bold text-3xl"
                >INTEGRATED PROJECT ITBKK-SY-1</span
              >
            </h1>
          </div>

          <div
            class="flex justify-between alert ml-6 w-auto mr-6 -mb-3"
            :class="saveResult.result ? 'alert-success' : 'alert-error'"
            v-if="saveResult.displayResult"
          >
            <div>
              <span class="font-bold text-xl text-slate-900">{{
                saveResult.result ? 'Success' : 'Error'
              }}</span
              ><br />
              <span class="text-slate-800">{{ saveResult.message }}</span>
            </div>
            <span class="pb-5 cursor-pointer" @click="saveResult.displayResult = false">x</span>
          </div>

          <div class="h-full w-full p-6 overflow-auto">
            <table class="table">
              <thead class="text-slate-700">
                <tr>
                  <th class="font-bold text-[1.5rem]"></th>
                  <th class="font-bold text-[1.5rem]">Title</th>
                  <th class="font-bold text-[1.5rem]">Assignees</th>
                  <th class="font-bold text-[1.5rem]">Status</th>
                  <th class="font-bold text-[1.5rem]">Action</th>
                </tr>
              </thead>

              <tbody>
                <tr class="itbkk-item hover" v-for="(task, key) in tasks" :key="key">
                  <td class="p-5">
                    <div class="flex">
                      <p class="itbkk-title font-bold">{{ key + 1 }}</p>
                    </div>
                  </td>

                  <td @click="openTaskDetail(task.id)" class="">
                    <p class="itbkk-title">
                      {{ task.title }}
                    </p>
                  </td>
                  <td @click="openTaskDetail(task.id)" class="">
                    <p class="itbkk-assignees" :class="{ italic: !task.assignees }">
                      {{ task.assignees || 'Unassigned' }}
                    </p>
                  </td>
                  <td @click="openTaskDetail(task.id)" class="">
                    <button
                      :class="getStatusClass(task.status)"
                      class="btn btn-active h-[1rem] min-h-[1.8rem] text-black"
                      @click="toggleSidebar"
                    >
                      <p class="itbkk-status">
                        {{ changeStatusName(task.status) }}
                      </p>
                    </button>
                  </td>
                  <td>
                    <div class="dropdown">
                        <div tabindex="0" role="button" class="btn m-1">
                          <v-icon name="co-settings" tabindex="0" role="button"> </v-icon>
                        </div>
                      <ul
                        tabindex="0"
                        class="dropdown-content z-[1] menu p-2 shadow bg-base-100 rounded-box w-36"
                      >
                        <li class="felx flex-row">
                          <button class="text-warning w-full" @click="editTask(task.id)">
                            <v-icon name="fa-edit"></v-icon>Edit
                          </button>
                        </li>
                        <li class="flex flex-row" @click="openDeleteDialog(task.title, task.id)">
                          <button class="text-error w-full" onclick="my_modal_1.showModal()">
                            <v-icon name="md-deleteforever"></v-icon>Delete
                          </button>
                        </li>
                      </ul>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
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
    <Teleport to="#modal" v-if="$route.params.id > 0 && !$route.path.includes('edit')">
      <TaskDetail></TaskDetail>
    </Teleport>
    <Teleport to="#modal" v-if="$route.params.id > 0 && $route.path.includes('edit')">
      <TaskEdit @update-tasks="taskEditEmit"></TaskEdit>
    </Teleport>

    <Teleport to="#addmodal" v-if="$route.path === '/task/add'">
      <TaskAdd @return-status="checkReceivedStatus"></TaskAdd>
    </Teleport>

    <!-- Open the modal using ID.showModal() method -->
    <dialog id="my_modal_1" class="modal">
      <div class="modal-box">
        <h3 class="font-bold text-lg">Delete a Task</h3>
        <p class="py-4 break-words">Do you want to delete the task "{{ taskTitle }}"?</p>
        <div class="modal-action">
          <form method="dialog">
            <!-- if there is a button in form, it will close the modal -->
            <button class="btn bg-error text-white">Close</button>
            <button
              class="itbkk-button-confirm btn bg-success text-white ml-2"
              @click="deleteTaskConfirm()"
            >
              <p class="itbkk-button">Confirm</p>
            </button>
          </form>
        </div>
      </div>
    </dialog>
  </div>
</template>

<script setup>
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'
import TaskDetail from './TaskDetail.vue'
import TaskAdd from './TaskAdd.vue'
import TaskEdit from './TaskEdit.vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { onMounted } from 'vue'
import { Button } from '@/components/ui/button'
import { getAllTasks, deleteTask } from '@/api/taskService'

const tasks = ref([])
const router = useRouter()
const isNull = ref(false)
const saveResult = ref({ displayResult: false, result: true, message: '' })

const taskTitle = ref(null)
const taskId = ref(null)

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

const checkReceivedStatus = async (response) => {
  saveResult.value.displayResult = true
  saveResult.value.result = response.status
  saveResult.value.message = response.message
  if (saveResult.value.result) {
    try {
      tasks.value = await getAllTasks()
    } catch (error) {
      console.log(error)
    }
  }
}

const taskEditEmit = async () => {
  tasks.value = await getAllTasks()
}

const deleteTaskConfirm = async () => {
  if (taskId.value !== null) {
    try {
      await deleteTask(taskId.value)
      console.log('Task deleted successfully')
      tasks.value = tasks.value.filter((task) => task.id !== taskId.value)
      my_modal_1.closeModal()
    } catch (error) {
      console.error('Error deleting task:', error)
    }
  }
}

const openTaskDetail = async (id) => {
  await router.push(`/task/${id}`)
}
const editTask = async (id) => {
  await router.push(`/task/${id}/edit`)
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

const openDeleteDialog = (title, id) => {
  taskTitle.value = title
  taskId.value = id
}

const displaySidebar = ref(false)

const toggleSidebar = () => {
  displaySidebar.value = !displaySidebar.value
}
</script>

<style></style>
