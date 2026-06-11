import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import api, { ensureCsrfToken } from '../api/client.js'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const loadUser = async () => {
      try {
        localStorage.removeItem('socialconnect_token')
        await ensureCsrfToken()
        const { data } = await api.get('/auth/me')
        setUser(data.user)
      } catch {
        setUser(null)
      } finally {
        setLoading(false)
      }
    }

    loadUser()
  }, [])

  const login = async (payload) => {
    const { data } = await api.post('/auth/login', payload)
    setUser(data.user)
    return data.user
  }

  const register = async (payload) => {
    const { data } = await api.post('/auth/register', payload)
    setUser(data.user)
    return data.user
  }

  const logout = async () => {
    try {
      await api.post('/auth/logout')
    } catch {
      // Clear local UI state even if the server session has already expired.
    } finally {
      localStorage.removeItem('socialconnect_token')
      setUser(null)
    }
  }

  const clearSession = () => {
    localStorage.removeItem('socialconnect_token')
    setUser(null)
  }

  const refreshUser = async () => {
    const { data } = await api.get('/auth/me')
    setUser(data.user)
    return data.user
  }

  const value = useMemo(
    () => ({ user, loading, login, register, logout, clearSession, refreshUser, setUser }),
    [user, loading],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const value = useContext(AuthContext)
  if (!value) {
    throw new Error('useAuth must be used inside AuthProvider')
  }
  return value
}
