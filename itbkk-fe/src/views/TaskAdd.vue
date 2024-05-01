<template>
  <div class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800">
    <div class="w-3/5">
      <Card class="items-center self-center min-w-full h-full">
        <CardHeader>
          <p class="pb-2">Title</p>
          <input
            type="text"
            placeholder="Type here"
            class="input input-bordered w-full  bg-white"
          />
        </CardHeader>
        <CardContent class="flex">
          <div class="w-1/2">
            <p class="pb-2">Assignees:</p>
            <input
              type="text"
              placeholder="Type here"
              class="input input-bordered w-full  bg-white "
            />
            <p>Status:</p>
            <select class="select select-bordered w-full bg-white">
              <option disabled hidden>
                <p class="itbkk-status"></p>
              </option>
              <option :value="'NO_STATUS'">No Status</option>
              <option :value="'TO_DO'">To Do</option>
              <option :value="'DOING'">Doing</option>
              <option :value="'DONE'">Done</option>
            </select>
          </div>
          <div class="w-1/2 gap-5 ml-10">
            <p class="pb-2">Description:</p>
            <textarea class="textarea textarea-bordered min-h-[8rem] w-full bg-white" placeholder="Bio"></textarea>
          </div>
        </CardContent>
        <CardFooter>
          <Button class="justify-between content-between" @click="closePage">Close</Button>
        </CardFooter>
      </Card>
    </div>
  </div>
</template>

<script setup>
import Button from '@/components/ui/button/Button.vue'
// import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
const router = useRouter()
const closePage = () => {
  router.back()
}
</script>

<style lang="scss" scoped></style>
