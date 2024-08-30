/* eslint-disable no-useless-catch */
const BASE_URL = import.meta.env.VITE_AUTH_URL

export const loginService = async (username: string, password: string): Promise<{ token: string }> => {
    try {
      const response = await fetch(`${BASE_URL}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
      });
  
      if (!response.ok) {
        const errorDetails = await response.json();
        throw new Error(`${errorDetails.message || 'Unknown error'}`);
      }
  
      // The token is now just a string, so no need to parse JSON again
      const token = await response.text();
      return { token };
    } catch (error) {
      throw error;
    }
  };
  
