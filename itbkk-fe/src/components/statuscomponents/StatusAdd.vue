<template>
  <div
    theme-data="light"
    class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
  >
    <div class="itbkk-modal-status w-3/5">
      <Card class="items-center self-center min-w-full h-full">
        <CardHeader>
          <div class="flex gap-1">
            <p class="pb-2">Status name :</p>
            <p class="text-gray-500">({{ statusLength.nameLength }}/50)</p>
          </div>
          <input
            type="text"
            placeholder="name is required"
            class="itbkk-status-name input input-bordered w-full bg-white"
            v-model="newStatus.name"
            maxlength="50"
          />
        </CardHeader>
        <CardContent class="flex flex-col">
          <div class="flex gap-1">
            <p class="pb-2">Description :</p>
            <p class="text-gray-500">({{ statusLength.descriptionLength }}/200)</p>
          </div>
          <textarea
            class="itbkk-status-description textarea textarea-bordered min-h-[8rem] w-full bg-white"
            placeholder="enter description here"
            v-model="newStatus.description"
            maxlength="200"
          ></textarea>
          <p class="pb-2">Enable Limit:</p>
          <input type="checkbox" class="toggle toggle-warning" v-model="newStatus.limitEnabled" />
        </CardContent>
        <CardFooter class="gap-3">
          <button
            class="itbkk-button-confirm btn btn-success text-white"
            :class="{ 'btn-disabled': newStatus.name.length === 0 }"
            :disabled="newStatus.name.length === 0"
            @click="createNewStatus"
          >
            Save
          </button>

          <button class="itbkk-button-cancel btn btn-error text-white" @click="closePage">
            Cancel
          </button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, computed } from 'vue'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card/index.ts'
import { useRouter } from 'vue-router'
import { createStatus } from '@/api/statusService.ts'

const emit = defineEmits(['returnStatus'])
const newStatus = ref({ name: '', description: '', limitEnabled: false })
const router = useRouter()

const statusLength = computed(() => ({
  nameLength: newStatus.value.name.length,
  descriptionLength: newStatus.value.description.length
}))

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
