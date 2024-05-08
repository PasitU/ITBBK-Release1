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
      return 'border border-gray-400 bg-grey-300'
    case 'To Do':
      return 'border border-blue-400 bg-blue-300'
    case 'Doing':
      return 'border border-yellow-400 bg-yellow-300'
    case 'Done':
      return ' border-green-400 bg-green-300' // Green border
    default:
      return 'border-sky-400 bg-sky-300' // Default border if no status matches
  }
}
</script>

<style lang="scss" scoped></style>
