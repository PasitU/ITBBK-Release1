/* eslint-disable no-useless-catch */
// const BASE_URL = 'http://ip23sy1.sit.kmutt.ac.th:8080/v1/tasks'
const BASE_URL = 'http://localhost:8080/v1/tasks'
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

export const getTaskById = async (id: number) => {
  try {
    const response = await fetch(`${BASE_URL}/${id}`)
    if (response.status === 404) {
      throw new Error('The requested task does not exist')
    }
    if (!response.ok) {
      throw new Error(`Unable to fetch task Id: ${id}.`)
    }
    return response.json()
  } catch (error) {
    throw error
  }
}

export const createTask = async (newTask: any): Promise<any> => {
  try {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({ ...newTask })
    })

    if (response.status !== 201) {
      throw new Error(`Unable to save the task`)
    }

    return await response.json()
  } catch (error) {
    throw error
  }
}

export const deleteTask = async (id: number): Promise<void> => {
  try {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'DELETE'
    })

    if (!response.ok) {
      throw new Error(`Failed to delete task with ID: ${id}. Status: ${response.status}`)
    }

    console.log('Task deleted successfully')
  } catch (error) {
    console.error('Error deleting task:', error)
  }
}

