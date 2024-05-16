/* eslint-disable no-useless-catch */
// const BASE_URL = 'http://ip23sy1.sit.kmutt.ac.th:8080/v1/tasks'
// const BASE_URL = 'http://localhost:8080/v1/tasks'
// const BASE_URL = 'http://localhost:3000/tasks'
const BASE_URL = import.meta.env.VITE_TASK_URL


export const getAllTasks = async (): Promise<any> => {
  try {
    const response = await fetch(BASE_URL)
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const data = await response.json()
    return data
  } catch (error) {
    console.error('Failed to fetch tasks:', error)
    throw error // Rethrowing the error to be handled by the caller
  }
}

// you can combine getAllTasksInStatus to getAllTasks but am not cuz i'll go to sleep
export const getAllTasksInStatus = async (filterStatuses: any): Promise<any> => {
  try {
    const response = await fetch(`${BASE_URL}?filterStatuses=${filterStatuses}`)
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const data = await response.json()
    return data
  } catch (error) {
    console.error('Failed to fetch tasks:', error)
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
