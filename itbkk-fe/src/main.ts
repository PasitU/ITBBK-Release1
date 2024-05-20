import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import {
  MdCancel,
  CoSettings,
  FaEdit,
  MdDeleteforever,
  CoSortAlphaDown,
  CoSortAlphaUp,
  OiCheck,
  FaCheck,
  BiX,
  CoArrowRight,
  BiInfinity
} from 'oh-vue-icons/icons'
import { OhVueIcon, addIcons } from 'oh-vue-icons'
addIcons(CoSettings, FaEdit, MdDeleteforever, CoSortAlphaDown, CoSortAlphaUp, MdCancel, FaCheck, OiCheck, BiX, CoArrowRight, BiInfinity)

const app = createApp(App)
app.use(router)
app.component('v-icon', OhVueIcon)
app.mount('#app')
