<template>
  <div
    data-theme="light"
    class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
  >
    <div class="w-3/5">
      <Card class="items-center self-center w-auto" v-if="isLoading">
        <CardHeader class="flex justify-center items-center">
          <span class="loading loading-spinner loading-lg"></span>
        </CardHeader>
      </Card>

      <Card
        class="items-center self-center min-w-full h-full"
        v-if="!fetchError.hasError && !isLoading"
      >
        <CardHeader>
          Title
          <input
            type="text"
            class="input input-bordered w-full space-x-5 border p-4 mt-2"
            v-model="task.title"
            placeholder="title"
          />
        </CardHeader>
        <CardContent class="flex">
          <div class="w-1/2">
            <p>Description:</p>
            <textarea
              class="textarea textarea-bordered h-1/2 w-full"
              v-model="task.description"
              placeholder="description"
            >
            </textarea>
            <p>Assignees:</p>
            <input
              type="text"
              class="input input-bordered w-full space-x-5 border p-4"
              v-model="task.assignees"
              placeholder="assignees"
            />
            <p>Status:</p>
            <select
              class="select select-bordered w-full"
              v-model="task.status"
              :selected="task.status"
            >
              <option :value="'NO_STATUS'">No Status</option>
              <option :value="'TO_DO'">To Do</option>
              <option :value="'DOING'">Doing</option>
              <option :value="'DONE'">Done</option>
            </select>
          </div>
          <div class="stats stats-vertical shadow w-1/2 gap-5 ml-10 text-slate-700">
            <div class="stat">
              <div class="stat-title">CreatedOn</div>
              <div class="stat-value">
                <p class="itbkk-created-on">{{ task.createdOn }}</p>
              </div>
            </div>

            <div class="stat">
              <div class="stat-title">UpdatedOn</div>
              <div class="stat-value">
                <p class="itbkk-updated-on">{{ task.updatedOn }}</p>
              </div>
            </div>

            <div class="stat">
              <div class="stat-title">TimeZone</div>
              <div class="stat-value">
                <p class="itbkk-timezone">{{ task.timezone }}</p>
              </div>
            </div>
          </div>
        </CardContent>
        <CardFooter>
          <button class="btn btn-error mr-3 w-20" @click="closePage">Close</button>
          <button
            class="btn btn-success w-20"
            :class="{ 'btn-disabled': isTaskSame }"
            @click="saveTask"
          >
            Save
          </button>
          
        </CardFooter>
      </Card>

      <Card
        class="items-center self-center min-w-full h-full"
        v-else-if="fetchError.hasError && !isLoading"
      >
        <CardHeader class="flex justify-center items-center bg-rose-500">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="shrink-0 h-28 w-28 stroke-white"
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
        </CardHeader>
        <CardContent class="mt-2 flex flex-col items-center">
          <div class="text-xl">An Error Occurred</div>
          <div class="mt-2">{{ fetchError.message }}</div>
          <div>Please try again later</div>
        </CardContent>
        <CardFooter class="flex justify-center">
          <Button class="justify-between content-between bg-rose-500 text-white" @click="closePage"
            >Close</Button
          >
        </CardFooter>
      </Card>
    </div>
    <div v-if="saveResult.success" role="alert" class="alert alert-success absolute bottom-0 right-0 w-1/3">
      <svg
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
      <span>Task ID: "{{ taskId }}" has been update!</span>
      <button class="btn btn-xs btn-outline btn-circle" @click="closeAlert">X</button>
    </div>
    <div v-if="saveResult.unsuccessful" role="alert" class="alert alert-error absolute bottom-0 right-0 w-1/3">
      <svg
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
      <span>Unable to update Task ID: "{{ taskId }}", {{ saveResult.message }}</span>
      <button class="btn btn-xs btn-outline btn-circle" @click="closeAlert">X</button>
    </div>
  </div>
</template>

<script setup>
import Button from '@/components/ui/button/Button.vue'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getTaskById, updateTask } from '@/api/taskService'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { getUserTimeZoneId, UTCtoLocalFormat } from '@/utils/timeConverter'
const router = useRouter()
const fetchError = ref({ hasError: false, message: '' })
const isLoading = ref(false)
const saveResult = ref({ success : false, unsuccessful: false, message: ''})
const task = ref({
  title: '',
  description: '',
  assignees: '',
  status: '',
  createdOn: '',
  updatedOn: '',
  timezone: ''
})
const taskUpdate = ref({
  title: '',
  description: '',
  assignees: '',
  status: ''
})

const taskId = router.currentRoute.value.params.id
const mount = onMounted(async () => {
  isLoading.value = true
  try {
    task.value = await getTaskById(taskId)
    taskUpdate.value = { ...task.value }
  } catch (error) {
    fetchError.value = { hasError: true, message: error.message }
    isLoading.value = false
    closePage() //TestCase need to change path to /tasks immediately when error occurs
    return
  }
  isLoading.value = false
  task.value.createdOn = task.value.createdOn ? UTCtoLocalFormat(task.value.createdOn) : 'No Data'
  task.value.updatedOn = task.value.updatedOn ? UTCtoLocalFormat(task.value.updatedOn) : 'No Data'
  task.value.timezone = getUserTimeZoneId()
})

const isTaskSame = computed(() => {
  return (
    task.value.title === taskUpdate.value.title &&
    task.value.description === taskUpdate.value.description &&
    task.value.assignees === taskUpdate.value.assignees &&
    task.value.status === taskUpdate.value.status
  )
})

const emit = defineEmits(['UpdateTasks'])

const saveTask = async () => {
  try {
    taskUpdate.value = { ...task.value }
    await updateTask(taskId, taskUpdate.value)
    saveResult.value = { success: true, unsuccessful: false, message: 'Task updated successfully' }
    mount()
    emit('UpdateTasks')
  } catch (error) {
    saveResult.value = { success: false, unsuccessful: true, message: error.message }
  }
}

const closeAlert = () => {
  saveResult.value = { success: false, unsuccessful: false, message: ''}
}

const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
