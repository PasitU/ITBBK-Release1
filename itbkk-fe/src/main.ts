import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { CoSettings} from 'oh-vue-icons/icons'
import { OhVueIcon, addIcons } from 'oh-vue-icons'
addIcons(CoSettings)

const app = createApp(App)
app.use(router)
app.component('v-icon', OhVueIcon)
app.mount('#app')