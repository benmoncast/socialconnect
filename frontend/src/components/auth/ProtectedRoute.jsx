import { Navigate, Outlet } from 'react-router-dom'
import Loader from '../common/Loader.jsx'
import { useAuth } from '../../auth/AuthContext.jsx'

export default function ProtectedRoute() {
  const { token, loading } = useAuth()

  if (loading) {
    return <Loader label="Loading SocialConnect..." />
  }

  return token ? <Outlet /> : <Navigate to="/login" replace />
}
