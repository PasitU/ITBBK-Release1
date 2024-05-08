/* eslint-disable no-useless-catch */
// const BASE_URL = 'http://ip23sy1.sit.kmutt.ac.th:8080/v1/tasks'
// const BASE_URL = 'http://localhost:8080/v1/tasks'
// const BASE_URL = 'http://localhost:3000/tasks'
const BASE_URL = 'http://localhost:8080/v2/tasks'

export const getAllTasks = async (): Promise<any> => {
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

export const getTaskById = async (id: number): Promise<any> => {
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

    if (response.status === 404) {
      throw new Error(`Unable to delete, Requested task ID: ${id} not exist.`)
    }

    if (!response.ok) {
      throw new Error(`Unable to delete task ID: ${id}`)
    }

    console.log('Task deleted successfully')
  } catch (error) {
    throw error
  }
}

export const updateTask = async (id: number, updatedTask: any) => {
  try {
    const response = await fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({
        title: updatedTask.title,
        description: updatedTask.description,
        assignees: updatedTask.assignees,
        status: updatedTask.status
      })
    })
    if (response.status === 404) {
      throw new Error(`Unable to update task with ID: ${updatedTask.id}. Task not found.`)
    }
    if (!response.ok) {
      throw new Error(`Failed to update task with ID: ${updatedTask.id}.`)
    }
  } catch (error) {
    throw error
  }
}