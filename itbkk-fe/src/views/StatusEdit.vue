<template>
  <div>
    <div
      data-theme="light"
      class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
    >
      <div class="w-3/5">
        <!-- <Card class="items-center self-center w-auto" v-if="isLoading">
               <CardHeader class="flex justify-center items-center">
               <span class="loading loading-spinner loading-lg"></span>
               </CardHeader>
          </Card> -->

        <!-- v-if="!fetchError.hasError && !isLoading" -->

        <Card class="items-center self-center min-w-full h-full">
          <CardHeader>
            Status name :
            <input
              type="text"
              class="itbkk-title input input-bordered w-full space-x-5 border p-4 mt-2"
              v-model="statuses.name"
            />
          </CardHeader>
          <CardContent class="flex-row">
            <p>Description:</p>
            <textarea
              class="itbkk-description textarea textarea-bordered h-44 w-full"
              v-model="statuses.description"
            >
            </textarea>
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
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { useRouter } from 'vue-router'
import { getStatusById, updateStatus } from '@/api/statusService'
const router = useRouter()
const statuses = ref({
  id: Number,
  name: '',
  description: ''
})

const statusUpdate = ref({
  id: Number,
  name: '',
  description: ''
})

const statusId = router.currentRoute.value.params.id

onMounted(async () => {
  try {
    const fetchedStatus = await getStatusById(statusId)
    if (!fetchedStatus) {
      router.push('/status')
      return
    }
    statusUpdate.value = { ...fetchedStatus }
    statuses.value = { ...fetchedStatus }
  } catch (error) {
    router.push('/status')
    return
  }
})

const saveStatus = async () => {
  if (isDirty.value) {
    try {
      await updateStatus(statusId, statuses.value)
      alert('Status updated successfully!')
      router.push('/status') // Redirect after successful update
    } catch (error) {
      alert('Failed to update status.')
    }
  }
}

const isDirty = computed(() => {
  return JSON.stringify(statusUpdate.value) !== JSON.stringify(statuses.value)
})

const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
