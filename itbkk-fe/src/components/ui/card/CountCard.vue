<template>
  <div>
    <div class="flex justify-center items-center flex-row gap-1">
      <div
        v-for="(count, status) in statusCounts"
        :key="status"
        :class="[
          'bg-gray-100 text-black text-center rounded-lg p-2 border-2 ',
          getBorderColor(status)
        ]"
      >
        <p>{{ changeStatusName(status) }}: {{ count }}</p>
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
    case 'NO_STATUS':
      return 'border border-gray-500 bg-grey-200'
    case 'TO_DO':
      return 'border border-blue-500 bg-blue-100'
    case 'DOING':
      return 'border border-yellow-500 bg-yellow-100'
    case 'DONE':
      return 'border border-green-500 bg-green-100' // Green border
    default:
      return 'border' // Default border if no status matches
  }
}

const changeStatusName = (status) => {
  switch (status) {
    case 'NO_STATUS':
      return 'No Status'
    case 'TO_DO':
      return 'To Do'
    case 'DOING':
      return 'Doing'
    case 'DONE':
      return 'Done'
    default:
      return ''
  }
}
</script>

<style lang="scss" scoped></style>
