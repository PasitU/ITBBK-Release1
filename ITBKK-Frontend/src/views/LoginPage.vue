<template>
  <div data-theme="light" class="flex h-screen">
    <CrudResponseAlert
      v-show="crudAlert.displayResult"
      class="z-100"
      :crudAlert="crudAlert"
      @update-displayResult="handleDisplayResult"
    />
    <!-- Welcome Section -->
    <!-- class="bg-cover bg-center bg-no-repeat min-h-screen" -->
    <!-- :style="{ backgroundImage: `url('@/assets/picture/image.png')` }" -->
    <div
      class="flex-1 flex items-center justify-center bg-gradient-to-r from-pink-200 via-purple-400 to-blue-500 text-white p-6"
    >
      <h1 class="text-5xl font-bold text-white focus-in-expand-fwd">
        INTEGRATED PROJECT ITBKK-SY-1 !
      </h1>
    </div>

    <!-- Login Section -->
    <div class="flex-1 flex flex-col justify-center p-10 bg-white">
      <h2 class="text-3xl font-bold mb-4 text-black">Login</h2>
      <p class="text-gray-500 mb-6">Welcome back! Please login to your account.</p>
      <form @submit.prevent="login">
        <div class="mb-4">
          <label for="username" class="block text-gray-700 font-bold mb-2"
            >User Name
            <span class="text-gray-400">({{ username.length }}/50)</span>
          </label>

          <input
            type="text"
            id="username"
            v-model="username"
            maxlength="50"
            placeholder="username@gmail.com"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 itbkk-username"
            required
          />
        </div>
        <div class="mb-6">
          <label for="password" class="block text-gray-700 font-bold mb-2"
            >Password <span class="text-gray-400">({{ password.length }}/14)</span></label
          >
          <input
            type="password"
            id="password"
            v-model="password"
            placeholder="********"
            class="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-500 itbkk-password"
            maxlength="14"
            required
          />
        </div>

        <div class="flex items-center mb-6">
          <input type="checkbox" id="rememberMe" v-model="rememberMe" class="mr-2" />
          <label for="rememberMe" class="text-gray-700">Remember Me</label>
        </div>
        <button
          @click="login"
          :disabled="!isFormValid"
          type="submit"
          :class="[
            'w-full py-2 rounded-lg focus:outline-none focus:ring-2 itbkk-button-signin',
            isFormValid
              ? 'bg-purple-600 text-white hover:bg-purple-700'
              : 'bg-gray-400 cursor-not-allowed disabled'
          ]"
        >
          Login
        </button>
      </form>
      <div class="flex justify-between mt-6">
        <a href="#" class="text-black hover:underline">Forget Password?</a>
        <!-- <div>
          <a href="#" class="text-black font-bold hover:underline">New User? </a>
          <a href="#" class="text-black font-bold hover:underline">Signup</a>
        </div> -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { loginService } from '@/api/authService'
import CrudResponseAlert from '@/components/ui/CrudResponseAlert.vue'
import { useAuthStore } from '@/stores/AuthStore'

const username = ref('')
const password = ref('')
const rememberMe = ref(false)
const router = useRouter()
const crudAlert = ref({ displayResult: false, result: false, message: '' })
const authStore = useAuthStore()

const isFormValid = computed(() => {
  return username.value.length > 0 && password.value.length > 0
})

const handleDisplayResult = (displayResult) => {
  crudAlert.value.displayResult = displayResult
}

const login = async () => {
  try {
    const response = await loginService(username.value, password.value)
    authStore.setToken(response.token)
    console.log(response)
    // const decoded = jwtDecode(response)
    // // console.log(decoded);

    router.push({ name: 'home' })
  } catch (error) {
    crudAlert.value = {
      displayResult: true,
      result: false,
      message: error.message
    }
  }
}
</script>

<style scoped>
.focus-in-expand-fwd {
  -webkit-animation: focus-in-expand-fwd 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
  animation: focus-in-expand-fwd 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) both;
}

@-webkit-keyframes focus-in-expand-fwd {
  0% {
    letter-spacing: -0.5em;
    -webkit-transform: translateZ(-800px);
    transform: translateZ(-800px);
    -webkit-filter: blur(12px);
    filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateZ(0);
    transform: translateZ(0);
    -webkit-filter: blur(0);
    filter: blur(0);
    opacity: 1;
  }
}
@keyframes focus-in-expand-fwd {
  0% {
    letter-spacing: -0.5em;
    -webkit-transform: translateZ(-800px);
    transform: translateZ(-800px);
    -webkit-filter: blur(12px);
    filter: blur(12px);
    opacity: 0;
  }
  100% {
    -webkit-transform: translateZ(0);
    transform: translateZ(0);
    -webkit-filter: blur(0);
    filter: blur(0);
    opacity: 1;
  }
}
</style>
