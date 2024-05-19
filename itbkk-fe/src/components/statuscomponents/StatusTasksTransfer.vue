<template>
  <div
    data-theme="light"
    class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
  >
    <div class="itbkk-modal-status w-1/2">
      <Card class="itbkk-modal-task light items-center self-center min-w-full h-full light">
        <CardHeader>
          <h2 class="font-bold text-lg">
            Tasks in {{ statusToTransfer?.name }} is exceed the limit
          </h2>
          <p class="text-gray-400">please transfer the status below</p>
          <div
            class="badge rounded-md mt-2 h-5"
            :class="TaskCount > 10 ? 'badge-error' : 'badge-success'"
          >
            {{ TaskCount }} /10
          </div>
        </CardHeader>
        <CardContent class="max-h-96 overflow-auto">
          <table class="table">
            <thead>
              <tr>
                <th></th>
                <th>Title:</th>
                <th>Status:</th>
                <th></th>
                <th></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(task, key) in tasks" :key="task.id">
                <td>{{ key + 1 }}</td>
                <td>{{ task.title }}</td>
                <td>
                  <select
                    class="select select-bordered"
                    v-model="task.status"
                    :selected="task.status"
                  >
                    <option
                      v-for="(selectStatus, key) in statusesList"
                      :key="key"
                      :value="selectStatus"
                    >
                      <p>{{ shortenTitle(selectStatus.name) }}</p>
                    </option>
                  </select>
                </td>
                <td>{{ TaskCount }}/10</td>
                <td><v-icon name="co-arrow-right" /></td>
                <td>{{ getStatusValue(task.status.id)  }}/10</td>
              </tr>
            </tbody>
          </table>
        </CardContent>
        <CardFooter class="w-1/3 gap-2 pt-5">
          <button @click="$emit('cancelLimit', statusToTransfer.id)" class="btn w-1/2">
            Cancel
          </button>
          <button
            @click="saveAll"
            class="btn w-1/2"
            :class="TaskCount > 10 ? 'btn-disabled' : 'btn-success'"
          >
            Save
          </button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import { getAllTasksInStatus, updateTask } from '@/api/taskService'
import { getAllStatuses, checkTaskDepend } from '@/api/statusService'
import { shortenTitle } from '@/lib/utils.ts'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card/index.ts'
import { ref, onMounted, computed } from 'vue'

const props = defineProps({
  statusToTransfer: {
    type: Object,
    require: true
  }
})

const emits = defineEmits(['cancelLimit', 'crudAlert'])

onMounted(async () => {
  try {
    statusesList.value = await getAllStatuses()
    tasks.value = await getAllTasksInStatus(props.statusToTransfer.name)
    currentUsage.value = await checkTaskDepend()
  } catch (error) {
    emits('crudAlert', {
      displayResult: true,
      result: false,
      message: error.message
    })
  }
})

const tasks = ref([])
const statusesList = ref([])
const currentUsage = ref([])


const TaskCount = computed(() => {
  let count = 0
  tasks.value.forEach((task) => {
    if (task.status.name === props.statusToTransfer.name) {
      count++
    }
  })
  return count
})

const getStatusValue = (statusId) => {
  return currentUsage.value[statusId] || 'Unknown'
}



const saveAll = async () => {
  try {
    for (const task of tasks.value) {
      if (task.status.name !== props.statusToTransfer.name) {
        await updateTask(task.id, { ...task })
      }
    }
    emits('crudAlert', {
      displayResult: true,
      result: true,
      message: 'All tasks have been transferred successfully'
    })
  } catch (error) {
    emits('cancelLimit', props.statusToTransfer.id)
    emits('crudAlert', {
      displayResult: true,
      result: false,
      message: error.message
    })
  }
}
</script>

<style scoped></style>
