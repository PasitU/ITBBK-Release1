import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { CoSettings, FaEdit, MdDeleteforever } from 'oh-vue-icons/icons'
import { OhVueIcon, addIcons } from 'oh-vue-icons'
addIcons(CoSettings, FaEdit, MdDeleteforever)

const app = createApp(App)
app.use(router)
app.component('v-icon', OhVueIcon)
app.mount('#app')