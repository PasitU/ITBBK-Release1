<template>
  <div>
    <div class="flex justify-center items-center flex-row gap-3">
      <div
        v-for="(count, status) in statusCounts"
        :key="status"
        :class="[
          'bg-gray-100 text-black text-center rounded-lg p-2 border-2 ',
          getBorderColor(status)
        ]"
      >
        <p>{{ status }} : {{ count }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  statusCounts: {
    type: Object,
    required: true
  }
})

function getBorderColor(status) {
  switch (status) {
    case 'No Status':
      return 'border border-gray-500 bg-grey-200'
    case 'To Do':
      return 'border border-blue-500 bg-blue-100'
    case 'Doing':
      return 'border border-yellow-500 bg-yellow-100'
    case 'Done':
      return 'border border-green-500 bg-green-100' // Green border
    default:
      return 'border' // Default border if no status matches
  }
}
</script>

<style lang="scss" scoped></style>
