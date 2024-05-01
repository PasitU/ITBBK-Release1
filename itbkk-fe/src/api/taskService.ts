
/* eslint-disable no-useless-catch */
const BASE_URL = 'http://ip23sy1.sit.kmutt.ac.th:8080/v1/tasks'
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

export const createTask = async (task:Task) => {
  try {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        title: task.title,
        description: task.description,
        assignees: task.assignees,
        status: task.status
      })
    })
    if (!response.ok) {
      throw new Error('Unable to create task.')
    }
    return response.json()
  } catch (error) {
    throw error
  }
}

export const deleteTask = async (id:number) => {
// url = http://ip23sy1.sit.kmutt.ac.th:8080/v1/tasks/{id}
// method = DELETE
}

export const updateTask = async (id:number, task:Task) => {
// url = http://ip23sy1.sit.kmutt.ac.th:8080/v1/tasks/{id}
// method = PUT
}