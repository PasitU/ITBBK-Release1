<template>
        <div>
          <p><strong>Title:</strong> {{ task.title }}</p>
          <p><strong>Description:</strong> {{ task.description }}</p>
          <p><strong>Assignees:</strong> {{ task.assignees }}</p>
          <p><strong>Status:</strong> {{ task.status }}</p>
          <p><strong>Created On:</strong> {{ task.createdOn }}</p>
          <p><strong>Updated On:</strong> {{ task.updatedOn }}</p>
          <p><strong>Timezone:</strong> {{ task.timezone }}</p>
        </div>
        <Button @click="closePage">Close</Button>
  </template>
  
  <script setup>
  import Button from '@/components/ui/button/Button.vue';
  import { onMounted, ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { getTaskById } from '@/api/taskService'

  const props = defineProps({ 
    id: Number 
  })
  
  onMounted(async () => {
    const taskId = router.currentRoute.value.params.id
    task.value = await getTaskById(taskId)
    console.log(task.value);

    console.log(taskId);
  })

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
  
  const closePage = () => {
    router.back()
  }
  </script>
  
  <style lang="scss" scoped></style>
  