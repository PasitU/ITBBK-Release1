<template>
  <div data-theme="light">
    <div class="overflow-x-scroll w-screen h-screen">
      <div class="flex justify-center items-center p-6">
        <h1>
          <span class="font-bold text-3xl items-center gap-2 flex"
            >INTEGRATED PROJECT ITBKK-SY-1 Status Page
          </span>
        </h1>
      </div>
      <div class="flex justify-between text-lg px-11 mb-4">
        <div class="breadcrumbs">
          <ul>
            <li><a @click="BackToHome" class="itbkk-button-home text-blue-600">Home</a></li>
            <li class="">Task Status</li>
          </ul>
        </div>
        <div>
          <Button
            class="itbkk-button-add bg-blue-700 text-18 text-red-50 hover:bg-blue-800"
            @click="navigateToAddStatus"
            >Add Status</Button
          >
        </div>
      </div>
      <div
        v-if="crudAlert.displayResult"
        role="alert"
        class="alert absolute bottom-20 right-3 w-1/3 z-10"
        :class="crudAlert.result ? `bg-success` : `bg-error`"
      >
        <svg
          v-if="crudAlert.result"
          xmlns="http://www.w3.org/2000/svg"
          class="stroke-current shrink-0 h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <svg
          v-else
          xmlns="http://www.w3.org/2000/svg"
          class="stroke-current shrink-0 h-6 w-6"
          fill="none"
          viewBox="0 0 24 24"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
        <span class="itbkk-message">{{ crudAlert.message }}</span>
        <button class="btn btn-xs btn-outline btn-circle" @click="crudAlert.displayResult = false">
          X
        </button>
      </div>

      <div class="px-[3rem]">
        <table class="table mb-30">
          <thead class="text-slate-700">
            <tr>
              <th class="font-bold text-[1.5rem]"></th>
              <th class="font-bold text-[1.5rem]">Name</th>
              <th class="font-bold w-1/2 text-[1.5rem]">Description</th>
              <th class="font-bold w-1/5 text-[1.5rem]">Action</th>
            </tr>
          </thead>

          <tbody>
            <tr class="itbkk-item" v-for="(status, key) in statuses" :key="key">
              <td class="p-5">
                <div class="flex">
                  <p class="itbkk-id font-bold">{{ key + 1 }}</p>
                </div>
              </td>

              <td class="">
                <p
                  class="itbkk-status-name rounded-sm break-before-all"
                  :class="getStatusClass(status)"
                >
                  {{ status.name }}
                </p>
              </td>
              <td class="">
                <p
                  class="itbkk-status-description break-all"
                  :class="status.description ? '' : 'italic text-gray-400'"
                >
                  {{ status.description || 'No description is provided' }}
                </p>
              </td>

              <!-- Check the correct status property for condition -->
              <td class="" v-if="status.customizable">
                <button
                  @click="editStatus(status.id)"
                  class="itbkk-button-edit text-warning mr-4 btn"
                >
                  <v-icon name="fa-edit"></v-icon>Edit
                </button>
                <button @click="statusRemove(status)" class="itbkk-button-delete text-error btn">
                  <v-icon name="md-deleteforever"></v-icon>Delete
                </button>
              </td>
              <td class="" v-else></td>
            </tr>
          </tbody>
        </table>

        <Teleport to="#modal" v-if="deleteability.showModal">
          <div
            data-theme="light"
            class="flex justify-center items-center h-screen w-screen bg-opacity-80 bg-zinc-800"
          >
            <div class="modal-box absolute" v-if="deleteability.canDelete">
              <h3 class="font-bold text-lg">Delete a Task</h3>
              <p class="itbkk-message py-4 break-words">
                Do you want to delete the status "{{ deleteability.statusName }}"?
              </p>
              <div class="modal-action">
                <form method="dialog">
                  <button
                    class="itbkk-button-cancel btn bg-error text-white"
                    @click="deleteability.showModal = false"
                  >
                    Cancel
                  </button>
                  <button
                    class="itbkk-button-confirm btn bg-success text-white ml-2"
                    @click="confirmDelete(deleteability.statusId)"
                  >
                    <p class="itbkk-button">Confirm</p>
                  </button>
                </form>
              </div>
            </div>
            <div class="modal-box" v-else-if="!deleteability.canDelete">
              <h3 class="font-bold text-lg">Delete a Task</h3>
              <p class="itbkk-message py-4 break-words">
                There is some task associated with the "{{ deleteability.statusName }}" status.
              </p>
              <p>
                Transfer to
                <select
                  class="itbkk-status select select-bordered w-full max-w-xs"
                  v-model="transferStatus"
                >
                  <option
                    v-for="(selectStatus, key) in statuses"
                    :key="key"
                    :hidden="deleteability.statusName === selectStatus.name"
                    :value="selectStatus"
                  >
                    {{ selectStatus.name }}
                  </option>
                </select>
              </p>
              <p class="text-red-600" v-show="showTransferError">
                Please select status to transfer to before proceeding
              </p>
              <div class="modal-action">
                <form method="dialog">
                  <!-- if there is a button in form, it will close the modal -->
                  <button
                    class="itbkk-button-cancel btn bg-error text-white"
                    @click="deleteability.showModal = false"
                  >
                    Cancel
                  </button>
                  <button
                    class="itbkk-button-confirm btn bg-success text-white ml-2"
                    @click="
                      () => {
                        if (transferStatus === '') {
                          showTransferError = true
                          return
                        }
                        confirmDelete(deleteability.statusId, transferStatus)
                      }
                    "
                  >
                    <p class="itbkk-button">Confirm</p>
                  </button>
                </form>
              </div>
            </div>
          </div>
        </Teleport>
      </div>
    </div>

    <Teleport to="#modal" v-if="$route.params.id > 1 && $route.path.includes('edit')">
      <StatusEdit @status-updated="handleStatusUpdate"></StatusEdit>
    </Teleport>
    <Teleport to="#addmodal" v-if="$route.path === '/status/add'">
      <StatusAdd @return-status="checkReceivedStatus"></StatusAdd>
    </Teleport>
  </div>
</template>

<script setup>
import { getAllStatuses, deleteStatus, checkTaskDepend, getStatusById } from '@/api/statusService'
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'
import { Button } from '@/components/ui/button'
import StatusAdd from '../components/statuscomponents/StatusAdd.vue'
import StatusEdit from '../components/statuscomponents/StatusEdit.vue'

const statuses = ref([])
const router = useRouter()
const deleteability = ref({ statusId: '', statusName: '', showModal: false, canDelete: false })
const transferStatus = ref('')
const crudAlert = ref({ displayResult: false, result: false, message: '' })
const showTransferError = ref(false)

onMounted(async () => {
  try {
    statuses.value = await getAllStatuses()
    console.log(statuses.value)
  } catch (error) {
    crudAlert.value = { displayResult: true, result: false, message: error.message }
  }
})

const statusRemove = async (status) => {
  try {
    await getStatusById(status.id)
    let canBeDeleted = (await checkTaskDepend(status.id)) === 0
    deleteability.value.statusId = status.id
    deleteability.value.statusName = status.name
    if (canBeDeleted) {
      deleteability.value.canDelete = true
    } else {
      deleteability.value.canDelete = false
    }
    deleteability.value.showModal = true
  } catch (error) {
    crudAlert.value = { displayResult: true, result: false, message: error.message }
    statuses.value = await getAllStatuses()
  }
}

const confirmDelete = async (statusId, newStatus = null) => {
  try {
    if (newStatus !== null) {
      await deleteStatus(statusId, newStatus.id)
    } else {
      await deleteStatus(statusId)
    }
    statuses.value = statuses.value.filter((status) => status.id !== statusId)
    crudAlert.value = { displayResult: true, result: true, message: 'Status deleted successfully' }
  } catch (error) {
    crudAlert.value = { displayResult: true, result: false, message: error.message }
  }
  deleteability.value.showModal = false
}

const checkReceivedStatus = async (response) => {
  crudAlert.value = { ...response }
  if (crudAlert.value.result) {
    try {
      statuses.value = await getAllStatuses()
    } catch (error) {
      crudAlert.value = { displayResult: true, result: false, message: error.message }
    }
  }
}
const BackToHome = () => {
  router.push({ name: 'home' })
}

const handleStatusUpdate = (updatedStatus) => {
  if (updatedStatus.result) {
    const index = statuses.value.findIndex((s) => s.id === updatedStatus.newStatus.id)
    statuses.value[index] = updatedStatus.newStatus
    crudAlert.value.displayResult = true
    crudAlert.value.result = updatedStatus.result
    crudAlert.value.message = updatedStatus.message
  } else {
    crudAlert.value.displayResult = true
    crudAlert.value.result = updatedStatus.result
    crudAlert.value.message = updatedStatus.message
  }
}
const navigateToAddStatus = () => {
  router.push({ name: 'statusAdd' })
}

const getStatusClass = (status) => {
  switch (status.name) {
    case 'No Status':
      return 'badge bg-gray-400'
    case 'To Do':
      return 'badge bg-blue-400'
    case 'Doing':
      return 'badge bg-yellow-400'
    case 'Done':
      return 'badge bg-green-400'
    default:
      return 'badge bg-indigo-400'
  }
}

const editStatus = async (id) => {
  await router.push({ name: 'statusEdit', params: { id: id } })
}
</script>

<style></style>
