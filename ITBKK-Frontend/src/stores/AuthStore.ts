import { defineStore } from 'pinia'
import { jwtDecode } from 'jwt-decode'

interface JwtPayload {
  iss: string
  iat: number
  exp: number
  name: string
  oid: string
  email: string
  role: string
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: '',
    decodedToken: {} as JwtPayload
  }),
  actions: {
    setToken(token: string) {
      this.token = token
      this.decodedToken = jwtDecode<JwtPayload>(token)
    },
    clearToken() {
      this.token = ''
      this.decodedToken = {} as JwtPayload
    }
  },
  getters: {
    isAuthenticated: (state) => !!state.token
  }
})
