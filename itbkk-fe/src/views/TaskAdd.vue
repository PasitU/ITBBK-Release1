<template>
  <div theme-data="light" class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800">
    <div class="w-3/5">
      <Card class="items-center self-center min-w-full h-full">
        <CardHeader>
          <p class="pb-2">Title</p>
          <input
            type="text"
            placeholder="title is require"
            class="itbkk-title input input-bordered w-full bg-white"
            :class="titleError ? `input-error` : ``"
            v-model="newTask.title"
          />
        </CardHeader>
        <CardContent class="flex">
          <div class="w-1/2">
            <p class="pb-2">Assignees:</p>
            <input
              type="text"
              placeholder="enter assignees here"
              class="itbkk-assignees input input-bordered w-full bg-white"
              v-model="newTask.assignees"
            />
            <p>Status:</p>
            <select class="itbkk-status select select-bordered w-full bg-white" v-model="newTask.status">
              <option :value="'NO_STATUS'">No Status</option>
              <option :value="'TO_DO'">To Do</option>
              <option :value="'DOING'">Doing</option>
              <option :value="'DONE'">Done</option>
            </select>
          </div>
          <div class="w-1/2 gap-5 ml-10">
            <p class="pb-2">Description:</p>
            <textarea
              class="itbkk-description textarea textarea-bordered min-h-[8rem] w-full bg-white"
              placeholder="enter description here"
              v-model="newTask.description"
            ></textarea>
          </div>
        </CardContent>
        <CardContent class="-mt-6 -mb-4">
          <div v-if="warning.length > 0" class="gap-3 text-red-600">
            {{ warning }}
          </div>
        </CardContent>
        <CardFooter class="gap-3">
          <button
            class="itbkk-button-confirm btn btn-success text-white"
            :class="titleError ? 'disabled btn-disabled': ''"
            @click="saveNewTask"
            >save</button
          >
          <button
            class="itbkk-button-cancel btn btn-error text-white"
            @click="closePage"
            >cancel</button
          >
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { createTask } from '@/api/taskService.ts'
import { ref, defineEmits, computed } from 'vue'
import { shortenTitle } from '@/lib/utils'

const warning = ref('')
const emit = defineEmits(['returnStatus'])
const newTask = ref({ title: '', description: '', assignees: '', status: 'NO_STATUS' })
const router = useRouter()
const closePage = () => {
  router.back()
}

const titleError = computed(() => {
  return newTask.value.title.length === 0
})

const saveNewTask = async () => {
  if (newTask.value.title.length === 0) {
    warning.value = "Title can't be empty!"
    return
  }
  try {
    await createTask(newTask.value)
  } catch (error) {
    console.log(error)
    emit('returnStatus', {
      status: false,
      message: `An error occured: task "${shortenTitle(newTask.value.title)}" couldn't be saved, Please try again later`
    })
    router.back()
    return
  }

  emit('returnStatus', {
    status: true,
    message: `The task "${shortenTitle(newTask.value.title)}" has been saved! ...`
  })
  router.back()
}
</script>

<style lang="scss" scoped></style>
