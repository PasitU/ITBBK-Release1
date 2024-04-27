/* eslint-disable no-useless-catch */
const BASE_URL = 'http://ip23us2.sit.kmutt.ac.th:8080/v1/tasks'
// const BASE_URL = 'http://localhost:8080/v1/tasks'
// const BASE_URL = 'http://localhost:3000/tasks'

export const getAllTasks = async () => {
  try {
    const response = await fetch(BASE_URL)
    if (!response.ok) {
      throw new Error('Unable to fetch tasks.') 
    }
    return response.json()
  } catch (error) {
    throw error
  }
}

export const getTaskById = async (id) => {
  try {
    const response = await fetch(`${BASE_URL}/${id}`)
    if(response.status === 404){
      throw new Error("The requested task does not exist")
    }
    if (!response.ok) {
      throw new Error(`Unable to fetch task Id: ${id}.`) 
    }
    return response.json()
  } catch (error) {
    throw error
  }
}