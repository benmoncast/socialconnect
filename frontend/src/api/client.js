import axios from 'axios'

const DEFAULT_API_BASE_URL = 'https://socialconnect-production-4cf0.up.railway.app/api'
const normalizeApiBaseUrl = (value) => {
  const baseUrl = (value || DEFAULT_API_BASE_URL).replace(/\/+$/, '')
  return /\/api(?:\/.*)?$/.test(baseUrl) ? baseUrl : `${baseUrl}/api`
}

export const API_BASE_URL = normalizeApiBaseUrl(import.meta.env.VITE_API_BASE_URL)
export const API_ORIGIN = API_BASE_URL.replace(/\/api(?:\/.*)?\/?$/, '')
const CSRF_COOKIE_NAME = 'XSRF-TOKEN'
const CSRF_HEADER_NAME = 'X-XSRF-TOKEN'
const SAFE_METHODS = new Set(['get', 'head', 'options', 'trace'])

let csrfTokenRequest = null
let csrfToken = ''

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
})

const readCookie = (name) => {
  const cookie = document.cookie
    .split('; ')
    .find((part) => part.startsWith(`${name}=`))

  return cookie ? decodeURIComponent(cookie.substring(name.length + 1)) : ''
}

export const ensureCsrfToken = async () => {
  if (!csrfTokenRequest) {
    csrfTokenRequest = api
      .get('/auth/csrf', { skipCsrf: true })
      .then((response) => {
        csrfToken = response.data?.token || readCookie(CSRF_COOKIE_NAME)
        return response
      })
      .finally(() => {
        csrfTokenRequest = null
      })
  }
  return csrfTokenRequest
}

api.interceptors.request.use(async (config) => {
  const method = (config.method || 'get').toLowerCase()
  if (!config.skipCsrf && !SAFE_METHODS.has(method)) {
    await ensureCsrfToken()
    const token = csrfToken || readCookie(CSRF_COOKIE_NAME)
    if (token) {
      config.headers = config.headers || {}
      config.headers[CSRF_HEADER_NAME] = token
    }
  }

  return config
})

export const getApiError = (error) => {
  if (!error.response) {
    return `Network error. Make sure the backend is reachable at ${API_BASE_URL}.`
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
