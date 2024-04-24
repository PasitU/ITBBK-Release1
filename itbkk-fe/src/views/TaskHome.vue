<template>
  <ResizablePanelGroup direction="horizontal" class="w-24 h-screen rounded-lg border">
    <ResizablePanel id="handle-demo-panel-1" :default-size="20">
      <div class="flex h-full items-center justify-center p-6">
        <span class="font-semibold">Sidebar</span>
      </div>
    </ResizablePanel>
    <ResizableHandle id="handle-demo-handle-1" with-handle />
    <ResizablePanel id="handle-demo-panel-2" :default-size="80" class="h-screen">
      <div>
        <div class="flex justify-between items-center p-6">
          <h1>
            <span class="font-semibold">Tasks</span>
          </h1>
        </div>
        <div class="h-full w-full p-6">
          <Table class="border">
            <TableCaption class="pb-4">Total {{ tasks.length }} Tasks</TableCaption>
            <TableHeader>
              <TableRow>
                <TableHead>Title</TableHead>
                <TableHead>Assignees</TableHead>
                <TableHead>Status</TableHead>
              </TableRow>
            </TableHeader>

            <TableBody v-if="tasks.length !== 0">
              <TableRow
                class="itbkk-item"
                v-for="task in tasks"
                :key="task.id"
                @click="openTaskDetail(task.id)"
              >
                <TableCell class="itbkk-title">{{ task.title }}</TableCell>
                <TableCell class="itbkk-assignees">
                  {{ task.assignees }}
                </TableCell>
                <TableCell class="itbkk-status">{{ task.status }}</TableCell>
              </TableRow>
            </TableBody>
            <p v-else class="p-6 text-center text-red-500">No tasks found</p>
          </Table>
        </div>
      </div>
    </ResizablePanel>
  </ResizablePanelGroup>
</template>

<script setup>
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table'

import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'

import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { onMounted } from 'vue'
import { getAllTasks } from '@/api/taskService'

const tasks = ref([])
const router = useRouter()

onMounted(async () => {
  tasks.value = await getAllTasks()
})

const openTaskDetail = async (id) => {
  await router.push(`/task/${id}`)
}
</script>

<style lang="scss" scoped></style>
