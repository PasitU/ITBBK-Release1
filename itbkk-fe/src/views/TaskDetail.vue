<template>
  <div class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800">
    <div class="w-1/2">
      <Card class="items-center self-center w-full h-full">
        <CardHeader>
          Title
          <div>
            <div class="space-x-5 rounded-md border p-4">
              {{ task.title }}
            </div>
          </div>
        </CardHeader>
        <CardContent class="flex">
          <div class="w-1/2">
            <p>Description:</p>
            <div class="space-x-5 border p-4 rounded-md">
              <h1 class="break-words">
                <p>{{ task.description }}</p>
              </h1>
            </div>
            <p>Assignees:</p>
            <div class="space-x-5 border p-4 rounded-md">
              <h1 class="break-words">
                <p>{{ task.assignees }}</p>
              </h1>
            </div>
            <p>Status:</p>
            <select
              class="select select-bordered w-full"
              v-model="task.status"
              :selected="task.status"
            >
              <option disabled hidden>{{ task.status }}</option>
              <option :value="'NO_STATUS'">NO_STATUS</option>
              <option :value="'TO_DO'">TO_DO</option>
              <option :value="'DOING'">DOING</option>
              <option :value="'DONE'">DONE</option>
            </select>
          </div>
          <div class="stats stats-vertical shadow w-1/2 gap-5 ml-10">
            <div class="stat">
              <div class="stat-title">CreatedOn</div>
              <div class="stat-value">{{ task.createdOn }}</div>
              <div class="stat-desc">Jan 1st - Feb 1st</div>
            </div>

            <div class="stat">
              <div class="stat-title">UpdatedOn</div>
              <div class="stat-value">{{ task.updatedOn }}</div>
              <div class="stat-desc">↗︎ 400 (22%)</div>
            </div>

            <div class="stat">
              <div class="stat-title">TimeZone</div>
              <div class="stat-value">{{ task.timezone }}</div>
              <div class="stat-desc">↘︎ 90 (14%)</div>
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
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle
} from '@/components/ui/card'

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
  task.value.description = task.value.description ? task.value.description : 'No description'
})

const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
