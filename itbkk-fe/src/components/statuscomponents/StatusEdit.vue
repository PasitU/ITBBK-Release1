<template>
  <div>
    <div
      data-theme="light"
      class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
    >
      <div class="itbkk-modal-status w-3/5">
        <Card class="items-center light self-center min-w-full h-full">
          <CardHeader>
            <div class="flex gap-1">
              <p class="pb-2">Status name :</p>
              <p class="text-gray-500">({{ statusLength.nameLength }}/50)</p>
            </div>
            <input
              type="text"
              class="itbkk-status-name input input-bordered w-full space-x-5 border p-4 mt-2"
              v-model="statuses.name"
            />
          </CardHeader>
          <CardContent class="flex-row">
            <div class="flex gap-1">
              <p class="pb-2">Description :</p>
              <p class="text-gray-500">({{ statusLength.descriptionLength }}/200)</p>
            </div>
            <textarea
              class="itbkk-status-description textarea textarea-bordered h-44 w-full"
              v-model="statuses.description"
            >
            </textarea>
            <p class="pb-2">Enable Limit:</p>
            <input type="checkbox" class="toggle" v-model="statuses.limitEnabled" />
          </CardContent>

          <CardFooter>
            <button class="itbkk-button-cancel btn btn-error mr-3 w-20" @click="closePage">
              Cancel
            </button>
            <button
              class="itbkk-button-confirm btn btn-success w-20"
              :class="{ 'btn-disabled': !isDirty }"
              @click="saveStatus"
              :disabled="!isDirty"
            >
              Save
            </button>
          </CardFooter>
        </Card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card/index.ts'
import { useRouter } from 'vue-router'
import { getStatusById, updateStatus } from '@/api/statusService.ts'
const emits = defineEmits(['status-updated'])
const router = useRouter()
const statuses = ref({
  id: Number,
  name: String,
  description: String,
  limitEnabled: Boolean,
  customizable: Boolean
})

const statusOrg = ref({
  id: Number,
  name: String,
  description: String,
  limitEnabled: Boolean,
  customizable: Boolean
})

const statusId = router.currentRoute.value.params.id

onMounted(async () => {
  try {
    // if (statusId === 1) {
    //   router.push({ name: 'status' })
    //   return
    // }
    const fetchedStatus = await getStatusById(statusId)
    statusOrg.value = { ...fetchedStatus }
    statuses.value = { ...fetchedStatus }
    console.log(statuses.value);
  } catch (error) {
    emits('status-updated', {
      displayResult: true,
      result: false,
      message: `An error has occurred, the status does not exist`
    })
    router.push({ name: 'status' })
  }
})

const isDirty = computed(() => {
  return JSON.stringify(statusOrg.value) !== JSON.stringify(statuses.value)
})

const statusLength = computed(() => ({
  nameLength: statuses.value.name.length,
  descriptionLength: statuses.value.description.length
}))

const saveStatus = async () => {
  if (isDirty.value) {
    try {
      await updateStatus(statuses.value.id, statuses.value)
      emits('status-updated', {
        newStatus: statuses.value,
        displayResult: true,
        result: true,
        message: `Status "${statuses.value.name}" updated successfully`
      })
      // alert('Status updated successfully!')
      router.push({ name: 'status' })
    } catch (error) {
      emits('status-updated', {
        displayResult: true,
        result: false,
        message: `An error occurred: ${error.message}`
      })
      // alert('Failed to update status.')
      router.push({ name: 'status' })
    }
  }
}

const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
