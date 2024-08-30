const BASE_URL = import.meta.env.VITE_CONSTANT_URL

export const getConstants = async (name: string): Promise<any> => {
  try {
    const response = await fetch(`${BASE_URL}/${name}`)
    if (!response.ok) {
      const errorDetails = await response.json()
      throw new Error(`${errorDetails.message || 'Unknown error'}`)
    }
    const data = await response.json()
    return data
  } catch (error) {
    console.error('Failed to fetch constants:', error)
    throw error // Rethrowing the error to be handled by the caller
  }
}
