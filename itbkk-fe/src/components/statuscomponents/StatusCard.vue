<script setup>
import { shortenTitle, getStatusClass } from '../../lib/utils.ts'
import { defineEmits } from 'vue'

let props = defineProps({
  status: {
    type: String,
    require: true
  }
})
const emit = defineEmits(['removeStatusFilter'])
</script>

<template>
  <div
    class="flex items-center border rounded-sm border-indigo-400 gap-2 pl-1 pr-2 h-7 leading-8 bg-indigo-300 leading-8 text-sm justify-between"
    :class="getStatusClass(status)"
  >
    <span>{{ shortenTitle(status, 10) }}</span>
    <span><v-icon name="md-cancel"  @click="$emit('removeStatusFilter', status)"></v-icon></span>
  </div>
</template>

<style scoped></style>
