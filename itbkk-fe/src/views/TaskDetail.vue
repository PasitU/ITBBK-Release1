<template>
  <div class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800">
    <div class="w-3/5">
      <Card class="items-center self-center min-w-full h-full">
        <CardHeader>
          Title
          <div>
            <div class="space-x-5 rounded-md border p-4">
              <p class="itbkk-title">
                {{ task.title }}
              </p>
            </div>
          </div>
        </CardHeader>
        <CardContent class="flex">
          <div class="w-1/2">
            <p>Description:</p>
            <div class="space-x-5 border p-4 rounded-md">
              <h1 class="break-words">
                <p class="itbkk-description" :class="{ italic: !task.assignees }">
                  {{ task.description }}
                </p>
              </h1>
            </div>
            <p>Assignees:</p>
            <div class="space-x-5 border p-4 rounded-md">
              <h1 class="break-words">
                <p class="itbkk-assignees" :class="{ italic: !task.assignees }">
                  {{ task.assignees }}
                </p>
              </h1>
            </div>
            <p>Status:</p>
            <select
              class="select select-bordered w-full"
              v-model="task.status"
              :selected="task.status"
            >
              <option disabled hidden>
                <p class="itbkk-status">{{ task.status }}</p>
              </option>
              <option :value="'NO_STATUS'">No Status</option>
              <option :value="'TO_DO'">To Do</option>
              <option :value="'DOING'">Doing</option>
              <option :value="'DONE'">Done</option>
            </select>
          </div>
          <div class="stats stats-vertical shadow w-1/2 gap-5 ml-10">
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
          <Button class="justify-between content-between" @click="closePage">Close</Button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import Button from '@/components/ui/button/Button.vue'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getTaskById } from '@/api/taskService'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { getUserTimeZoneId, UTCtoLocalFormat } from '@/utils/timeConverter'
const router = useRouter()
const task = ref({
  title: '',
  description: '',
  assignees: '',
  status: '',
  createdOn: '',
  updatedOn: '',
  timezone: ''
})
const taskId = router.currentRoute.value.params.id

onMounted(async () => {
  try {
    task.value = await getTaskById(taskId)
  } catch (error) {
    router.back()
  }
  task.value.title = task.value.title ? task.value.title : 'No title'
  task.value.status = task.value.status ? task.value.status : 'No_status'
  task.value.assignees = task.value.assignees ? task.value.assignees : ''
  task.value.description = task.value.description
    ? task.value.description
    : 'No Description Provided'
  task.value.createdOn = task.value.createdOn ? UTCtoLocalFormat(task.value.createdOn) : 'No Data'
  task.value.updatedOn = task.value.updatedOn ? UTCtoLocalFormat(task.value.updatedOn) : 'No Data'
  task.value.timezone = getUserTimeZoneId()
})

const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
