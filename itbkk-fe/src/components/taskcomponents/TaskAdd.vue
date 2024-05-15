<template>
  <div
    theme-data="light"
    class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
  >
    <div class="w-3/5">
      <Card class="itbkk-modal-task light items-center self-center min-w-full h-full light">
        <CardHeader
          ><div class="flex gap-1">
            <p class="pb-2">Title</p>
            <p class="text-gray-500">({{ taskLengths.titleLength }}/100)</p>
          </div>

          <input
            type="text"
            placeholder="title is required"
            class="itbkk-title input input-bordered w-full bg-white"
            :class="titleError ? `input-error` : ``"
            v-model="newTask.title"
            maxlength="100"
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
            <select
              class="itbkk-status select select-bordered w-full bg-white"
              v-model="newTask.status"
            >
              <option v-for="(selectStatus, key) in statusesList" :key="key" :value="selectStatus">
                {{ selectStatus.name }}
              </option>
            </select>
          </div>
          <div class="w-1/2 gap-5 ml-10">
            <div class="flex gap-1">
              <p class="pb-2">Description:</p>
              <p class="text-gray-500">({{ taskLengths.descriptionLength }}/500)</p>
            </div>

            <textarea
              class="itbkk-description textarea textarea-bordered min-h-[8rem] w-full bg-white"
              placeholder="enter description here"
              v-model="newTask.description"
              maxlength="500"
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
            :class="titleError ? 'disabled btn-disabled' : ''"
            @click="saveNewTask"
          >
            save
          </button>
          <button class="itbkk-button-cancel btn btn-error text-white" @click="closePage">
            cancel
          </button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card/index.ts'
import { createTask } from '@/api/taskService.ts'
import { getAllStatuses } from '@/api/statusService.ts'
import { ref, defineEmits, computed, onMounted } from 'vue'
import { shortenTitle } from '@/lib/utils.ts'

const warning = ref('')
const emit = defineEmits(['returnStatus'])
const newTask = ref({ title: '', description: '', assignees: '', status: {} })
const statusesList = ref()
const router = useRouter()
const closePage = () => {
  router.back()
}

const taskLengths = computed(() => ({
  titleLength: newTask.value.title.length,
  descriptionLength: newTask.value.description.length
}))

const titleError = computed(() => {
  return newTask.value.title.length === 0
})

onMounted(async () => {
  statusesList.value = await getAllStatuses()
  newTask.value.status = statusesList.value[0]
})

const saveNewTask = async () => {
  if (newTask.value.title.length === 0) {
    warning.value = "Title can't be empty!"
    return
  }
  try {
    await createTask(newTask.value)
    emit('returnStatus', {
      status: true,
      message: `The task "${shortenTitle(newTask.value.title)}" has been saved! ...`
    })
    router.back()
  } catch (error) {
    emit('returnStatus', {
      status: false,
      message: `An error occured: task "${shortenTitle(newTask.value.title)}" couldn't be saved, Please try again later`
    })
    router.back()
  }
}
</script>

<style lang="scss" scoped></style>
