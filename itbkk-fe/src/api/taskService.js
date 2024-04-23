// const BASE_URL = 'http://ip23us2.sit.kmutt.ac.th:8080/itb-kk/v1/tasks'
const BASE_URL = 'http://localhost:3000/tasks'

export const getAllTasks = async () => {
  try {
    const response = await fetch(BASE_URL)
    if (!response.ok) {
      throw new Error('Network response was not ok') 
    }
    return response.json()
  } catch (error) {
    console.error('Error:', error)
  }
}
