<template>
  <div data-theme="light">
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
              <span class="font-bold text-3xl items-center gap-2 flex"
                >INTEGRATED PROJECT ITBKK-SY-1 Status Page
              </span>
            </h1>
          </div>
          <div class="flex justify-between text-[1.5rem] px-9">
            <div class="flex gap-4">
              <p @click="BackToHome" class="text-blue-600 cursor-pointer">Home</p>
              <span> > </span>
              <p class="font-bold">Task Status</p>
            </div>
            <div>
              <Button
                class="itbkk-button-add bg-blue-700 text-18 text-red-50 hover:bg-blue-800"
                @click="navigateToAddTask"
                >Add Status</Button
              >
            </div>
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
            <span class="itbkk-message">{{ crudResult.message }}</span>
            <button
              class="btn btn-xs btn-outline btn-circle"
              @click="crudResult.displayResult = false"
            >
              X
            </button>
          </div>

          <div class="h-full w-full px-[3rem] overflow-auto">
            <table class="table mb-30">
              <thead class="text-slate-700">
                <tr>
                  <th class="font-bold text-[1.5rem]"></th>
                  <th class="font-bold text-[1.5rem]">Name</th>
                  <th class="font-bold text-[1.5rem]">Description</th>
                  <th class="font-bold text-[1.5rem]">Action</th>
                </tr>
              </thead>

              <tbody>
                <tr class="itbkk-item hover" v-for="(status, key) in statuses" :key="key">
                  <td class="p-5">
                    <div class="flex">
                      <p class="itbkk-title font-bold">{{ key + 1 }}</p>
                    </div>
                  </td>

                  <td class="">
                    <p class="itbkk-title">
                      {{ status.name }}
                    </p>
                  </td>
                  <td class="">
                    <p class="itbkk-assignees">
                      {{ status.description }}
                    </p>
                  </td>

                  <!-- Check the correct status property for condition -->
                  <td class="" v-if="!cantEdit.includes(status.name)">
                    <button class="itbkk-button-edit text-warning mr-4 btn">
                      <v-icon name="fa-edit"></v-icon>Edit
                    </button>
                    <button class="itbkk-button-delete text-error btn">
                      <v-icon name="md-deleteforever"></v-icon>Delete
                    </button>
                  </td>
                  <td class="" v-else></td>
                </tr>
              </tbody>
            </table>

            <!-- <button class="btn h-[1rem] min-h-[1.8rem]" @click="toggleSidebar">
              {{ displaySidebar ? 'Close sidebar' : 'Open sidebar' }}
            </button>
            {{ displaySidebar ? 'Sidebar is on' : 'Sidebar is off' }} -->
          </div>
        </div>
      </ResizablePanel>
    </ResizablePanelGroup>
    <Teleport to="#addmodal" v-if="$route.path === '/status/add'">
      <StatusAdd></StatusAdd
    ></Teleport>
  </div>
</template>

<script setup>
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'
// import statusesData from '../../data/db.json'
import { getAllStatuses } from '@/api/taskService'
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { onMounted } from 'vue'
import { Button } from '@/components/ui/button'
import StatusAdd from './StatusAdd.vue'

const statuses = ref([])
const router = useRouter()
const cantEdit = ['No Status']

const crudResult = ref({ displayResult: false, result: true, message: '' })

onMounted(async () => {
  try {
    statuses.value = await getAllStatuses()
  } catch (error) {
    return
  }
})

const BackToHome = () => {
  router.push('/task')
}

const navigateToAddTask = () => {
  router.push('/status/add')
}

const displaySidebar = ref(false)
</script>

<style></style>
