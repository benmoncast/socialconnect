import { Navigate, Outlet } from 'react-router-dom'
import Loader from '../common/Loader.jsx'
import { useAuth } from '../../auth/AuthContext.jsx'

export default function PublicOnlyRoute() {
  const { user, loading } = useAuth()

  if (loading) {
    return <Loader label="Loading SocialConnect..." />
  }

  return user ? <Navigate to="/" replace /> : <Outlet />
}
