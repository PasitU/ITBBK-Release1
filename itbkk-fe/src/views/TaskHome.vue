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
              <span class="font-bold text-3xl">INTEGRATED PROJECT ITBKK-SY-1</span>
            </h1>
          </div>

          <div
            v-if="crudResult.displayResult"
            role="alert"
            class="alert absolute bottom-20 right-3 w-1/3 z-10"
            :class="crudResult.result ? `alert-success` : `alert-error`"
          >
            <svg
              v-if="crudResult.result"
              xmlns="http://www.w3.org/2000/svg"
              class="stroke-current shrink-0 h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
            <svg
              v-if="!crudResult.result"
              xmlns="http://www.w3.org/2000/svg"
              class="stroke-current shrink-0 h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
            <span>{{ crudResult.message }}</span>
            <button
              class="btn btn-xs btn-outline btn-circle"
              @click="crudResult.displayResult = false"
            >
              X
            </button>
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
      <TaskEdit @return-status="checkReceivedStatus"></TaskEdit>
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
const crudResult = ref({ displayResult: false, result: true, message: '' })

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
  crudResult.value.displayResult = true
  crudResult.value.result = response.status
  crudResult.value.message = response.message
  if (crudResult.value.result) {
    try {
      tasks.value = await getAllTasks()
    } catch (error) {
      console.log(error)
    }
  }
}

const deleteTaskConfirm = async () => {
  if (taskId.value !== null) {
    try {
      await deleteTask(taskId.value)
      await checkReceivedStatus({
        status: true,
        message: `The task "${taskTitle.value}" has been deleted successfully`
      })
      // my_modal_1.closeModal()
      tasks.value = tasks.value.filter((task) => task.id !== taskId.value)
    } catch (error) {
      await checkReceivedStatus({
        status: false,
        message: error.message
      })
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
