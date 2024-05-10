<template>
  <div
    theme-data="light"
    class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
  >
    <div class="w-3/5">
      <Card class="items-center self-center min-w-full h-full">
        <CardHeader>
          <p class="pb-2">Name</p>
          <input
            type="text"
            placeholder="name is required"
            class="itbkk-title-add input input-bordered w-full bg-white"
            v-model="newStatus.name"
          />
        </CardHeader>
        <CardContent class="flex flex-col">
          <p class="pb-2">Description:</p>
          <textarea
            class="itbkk-description-add textarea textarea-bordered min-h-[8rem] w-full bg-white"
            placeholder="enter description here"
            v-model="newStatus.description"
          ></textarea>
        </CardContent>
        <CardFooter class="gap-3">
          <button
            class="itbkk-button-add-confirm btn btn-success text-white"
            :class="{ 'btn-disabled': newStatus.name.length === 0 }"
            :disabled="newStatus.name.length === 0"
            @click="createNewStatus"
          >
            Save
          </button>

          <button class="itbkk-button-add-cancel btn btn-error text-white" @click="closePage">
            Cancel
          </button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card/index.ts'
import { useRouter } from 'vue-router'
import { createStatus } from '@/api/statusService.ts'

const emit = defineEmits(['returnStatus'])
const newStatus = ref({ name: '', description: '' })
const router = useRouter()

const createNewStatus = async () => {
  try {
    await createStatus(newStatus.value)
  } catch (error) {
    emit('returnStatus', {
      displayResult: true,
      result: false,
      message: `An error occurred: ${error.message}`
    })
    router.back()
    return
  }
  emit('returnStatus', {
    displayResult: true,
    result: true,
    message: `Status "${newStatus.value.name}" created successfully`
  })
  router.back()
}

const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
