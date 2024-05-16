const BASE_URL = import.meta.env.VITE_CONSTANT_URL

export const getConstants = async (): Promise<any> => {
  try {
    const response = await fetch(`${BASE_URL}/GStatLim`)
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const data = await response.json()
    return data
  } catch (error) {
    console.error('Failed to fetch constants:', error)
    throw error // Rethrowing the error to be handled by the caller
  }
}
