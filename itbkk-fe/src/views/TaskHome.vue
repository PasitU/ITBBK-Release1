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
                <TableHead>Create At</TableHead>
                <TableHead>Update At</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody v-if="tasks.length !== 0">
              <TableRow v-for="task in tasks" :key="task.id">
                <TableCell class="max-w-96 overflow-hidden">{{ task.taskTitle }}</TableCell>
                <TableCell>
                  <template
                    v-for="taskAssignee in task.taskAssignees"
                    :key="taskAssignee.assigneeId"
                  >
                    <span>{{ taskAssignee.assigneeName }}</span>
                    <span v-if="task.taskAssignees.length > 1 && taskAssignee !== task.taskAssignees[task.taskAssignees.length - 1]">, </span>
                  </template>
                </TableCell>
                <TableCell>{{ task.taskStatus }}</TableCell>
                <TableCell>{{ task.createdOn }}</TableCell>
                <TableCell>{{ task.updatedOn }}</TableCell>
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
import Button from '@/components/ui/button/Button.vue'
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from '@/components/ui/table'
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select'
import { Separator } from '@/components/ui/separator'
import { ResizableHandle, ResizablePanel, ResizablePanelGroup } from '@/components/ui/resizable'
import { ref } from 'vue'
const tasks = ref([])
import { onMounted } from 'vue'
import { getAllTasks } from '@/api/taskService'
onMounted(async () => {
  tasks.value = await getAllTasks()
})
</script>

<style lang="scss" scoped></style>
