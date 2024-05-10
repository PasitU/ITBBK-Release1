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

// TypeScript function to fetch a Status by its ID
export const getStatusById = async (id: number): Promise<any> => {
  try {
    const response = await fetch(`${BASE_URL}/${id}`)
    if (!response.ok) {
      throw new Error(`Unable to fetch Status with ID ${id}.`)
    }
    return response.json()
  } catch (error) {
    throw error
  }
}

export const createStatus = async (newStatus: any): Promise<any> => {
  try {
    const response = await fetch(BASE_URL, {
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify({ ...newStatus })
    })

    if (response.status !== 201) {
      throw new Error(`Unable to save the status "${newStatus.name}"`)
    }

    return await response.json()
  } catch (error) {
    throw error
  }
}

export const deleteStatus = async (statusId: number, newStatusId: number = null): Promise<any> => {
  try {
    let response
    if (newStatusId != null) {
      response = await fetch(`${BASE_URL}/${statusId}/${newStatusId}`, {
        method: 'DELETE'
      })
    } else {
      response = await fetch(`${BASE_URL}/${statusId}`, {
        method: 'DELETE'
      })
    }
    if (!response.ok) {
      throw new Error(`Unable to delete the status with id "${statusId}"`)
    }
  } catch (error) {
    throw error
  }
}

export const checkCanBeDeleted = async (statusId) => {
  try {
    const response = await fetch(`${BASE_URL}/check-usage/${statusId}`)
    if (!response.ok) {
      throw new Error(`Unable to get usage of status "${statusId}"`)
    }
    return response.json()
  } catch (error) {
    throw error
  }
}

export const updateStatus = async (
  statusId: number,
  statusData: { name: string; description: string }
): Promise<any> => {
  try {
    const response = await fetch(`${BASE_URL}/${statusId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(statusData)
    })
    if (!response.ok) {
      throw new Error(`Unable to update the status with id "${statusId}".`)
    }
    return response.json() // Assuming the server responds with the updated status object
  } catch (error) {
    throw error
  }
}
