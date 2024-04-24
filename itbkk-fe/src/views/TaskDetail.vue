<template>
  <div class="w-1/2 m-auto">
    <Card class="items-center self-center w-full h-full">
      <CardHeader>
        <div class="space-x-5 rounded-md border p-4">
          <CardTitle>{{ task.title }}</CardTitle>
        </div>
      </CardHeader>
      <CardContent>
        <div class="space-x-5 border p-4 rounded-md">
          <p>Description: {{ task.description }}</p>
        </div>
        <div class="space-x-5 border p-4 rounded-md">
          <p>Assignees: {{ task.assignees }}</p>
        </div>
        <div class="space-x-5 border p-4 rounded-md">
          <p>Status: {{ task.status }}</p>
        </div>
        <div class="space-x-5 border p-4 rounded-md">
          <p>CreatedOn: {{ task.createdOn }}</p>
        </div>
        <div class="space-x-5 border p-4 rounded-md">
          <p>UpdatedOn: {{ task.updatedOn }}</p>
        </div>
        <div class="space-x-5 border p-4 rounded-md">
          <p>Timezone: {{ task.timezone }}</p>
        </div>
      </CardContent>
      <CardFooter>
        <Button class="justify-between content-between" @click="closePage">Close</Button>
      </CardFooter>
    </Card>
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
