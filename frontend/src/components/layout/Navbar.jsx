import { Bell, Home, LogOut, Search, UsersRound } from 'lucide-react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import Avatar from '../common/Avatar.jsx'
import Button from '../common/Button.jsx'
import { useAuth } from '../../auth/AuthContext.jsx'

const navClass = ({ isActive }) =>
  `inline-flex items-center gap-2 rounded-md px-3 py-2 text-sm font-semibold transition ${
    isActive ? 'bg-blue-50 text-blue-700' : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
  }`

export default function Navbar() {
  const navigate = useNavigate()
  const { user, logout } = useAuth()

  const handleSearch = (event) => {
    event.preventDefault()
    const query = new FormData(event.currentTarget).get('query')?.toString().trim()
    if (query) {
      navigate(`/search?query=${encodeURIComponent(query)}`)
    }
  }

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="sticky top-0 z-40 border-b border-slate-200 bg-white/95 backdrop-blur">
      <nav className="mx-auto flex max-w-7xl flex-col gap-3 px-4 py-3 lg:flex-row lg:items-center lg:justify-between lg:px-8">
        <div className="flex items-center justify-between gap-3">
          <Link to="/" className="text-xl font-black text-blue-700">SocialConnect</Link>
          <Button type="button" variant="ghost" className="lg:hidden" onClick={handleLogout}>
            <LogOut size={17} aria-hidden="true" />
          </Button>
        </div>

        <form onSubmit={handleSearch} className="relative w-full lg:max-w-md">
          <Search className="absolute left-3 top-2.5 text-slate-400" size={18} aria-hidden="true" />
          <input
            name="query"
            placeholder="Search people"
            className="w-full rounded-md border border-slate-300 bg-slate-50 py-2 pl-10 pr-3 text-sm outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-100"
          />
        </form>

        <div className="flex flex-wrap items-center gap-2">
          <NavLink to="/" className={navClass}><Home size={17} />Home</NavLink>
          <NavLink to="/friends" className={navClass}><UsersRound size={17} />Friends</NavLink>
          <NavLink to="/notifications" className={navClass}><Bell size={17} />Notifications</NavLink>
          <NavLink to={`/profile/${user?.id}`} className={navClass}>
            <Avatar src={user?.profilePictureUrl} name={user?.fullName} size="sm" />
            Profile
          </NavLink>
          <Button type="button" variant="ghost" className="hidden lg:inline-flex" onClick={handleLogout}>
            <LogOut size={17} aria-hidden="true" />
            Logout
          </Button>
        </div>
      </nav>
    </header>
  )
}
