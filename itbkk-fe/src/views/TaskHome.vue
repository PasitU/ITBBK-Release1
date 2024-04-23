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
            <TableBody>
              <TableRow v-for="task in tasks" :key="task.id">
                <TableCell class="max-w-96 overflow-hidden">{{ task.title }}</TableCell>
                <TableCell>{{ task.assignees }}</TableCell>
                <TableCell>{{ task.status }}</TableCell>
                <TableCell>{{ task.createdAt }}</TableCell>
                <TableCell>{{ task.updatedAt }}</TableCell>
              </TableRow>
            </TableBody>
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
import { Fullscreen } from 'lucide-vue-next'
const tasks = ref([
  {
    id: 1,
    title:
      'TaskTitle1TaskTitle2TaskTitle3TaskTitle4TaskTitle5TaskTitle6TaskTitle7TaskTitle8TaskTitle9TaskTitle0',
    description:
      'Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti1Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti2Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti3Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti4Descripti1Descripti2Descripti3Descripti4Descripti5Descripti6Descripti7Descripti8Descripti9Descripti5',
    assignees: 'Assignee 1, Assignee 2',
    status: 'No Status',
    createdAt: '2022-01-01 09:00:00 UTC',
    updatedAt: '2022-01-01 09:00:00 UTC'
  },
  {
    id: 2,
    title: 'ดาต้าเบส',
    description: 'คำอธิบาย2',
    assignees: 'あなた、彼、彼女 (私ではありません',
    status: 'Doing',
    createdAt: '2022-01-01 09:00:00 UTC',
    updatedAt: '2022-01-01 09:00:00 UTC'
  },
  {
    id: 3,
    title: 'TaskTitle3',
    description: 'Description3',
    assignees: 'Assignee 1, Assignee 2',
    status: 'No Status',
    createdAt: '2022-01-01 09:00:00 UTC',
    updatedAt: '2022-01-01 09:00:00 UTC'
  },
  {
    id: 4,
    title: 'TaskTitle4',
    description: 'Description4',
    assignees: 'Assignee 1, Assignee 2',
    status: 'No Status',
    createdAt: '2022-01-01 09:00:00 UTC',
    updatedAt: '2022-01-01 09:00:00 UTC'
  },
  {
    id: 5,
    title: 'TaskTitle5',
    description: 'Description5',
    assignees: 'Assignee 1, Assignee 2',
    status: 'No Status',
    createdAt: '2022-01-01 09:00:00 UTC',
    updatedAt: '2022-01-01 09:00:00 UTC'
  }
])
</script>

<style lang="scss" scoped></style>
