import axios from 'axios'

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
export const API_ORIGIN = API_BASE_URL.replace(/\/api(?:\/.*)?\/?$/, '')

const api = axios.create({
  baseURL: API_BASE_URL,
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('socialconnect_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export const getApiError = (error) => {
  if (!error.response) {
    return 'Network error. Make sure the backend is running on http://localhost:8080.'
  }
  return error.response.data?.message || 'Something went wrong. Please try again.'
}

export const getMediaUrl = (url) => {
  if (!url) {
    return ''
  }

  if (/^https?:\/\//i.test(url)) {
    return url
  }

  return `${API_ORIGIN}${url.startsWith('/') ? url : `/${url}`}`
}

export default api
