/* eslint-disable no-useless-catch */

const BASE_URL = 'http://localhost:8080/v2/statuses'

export const getAllStatuses = async () => {
    try {
      const response = await fetch(BASE_URL)
      if (!response.ok) {
        throw new Error('Unable to fetch Status.')
      }
      return response.json()
    } catch (error) {
      throw error
    }
  }