import { Bell, Settings, UserRound, UsersRound } from 'lucide-react'
import { Link } from 'react-router-dom'
import Avatar from '../common/Avatar.jsx'
import { useAuth } from '../../auth/AuthContext.jsx'

export default function Sidebar() {
  const { user } = useAuth()
  const links = [
    { to: `/profile/${user?.id}`, label: 'My Profile', icon: UserRound },
    { to: '/friends', label: 'Friends', icon: UsersRound },
    { to: '/notifications', label: 'Notifications', icon: Bell },
    { to: '/edit-profile', label: 'Settings', icon: Settings },
  ]

  return (
    <aside className="sticky top-24 hidden h-fit rounded-lg border border-slate-200 bg-white p-4 shadow-sm lg:block">
      <div className="mb-4 flex items-center gap-3">
        <Avatar src={user?.profilePictureUrl} name={user?.fullName} />
        <div className="min-w-0">
          <p className="truncate text-sm font-semibold text-slate-950">{user?.fullName}</p>
          <p className="truncate text-xs text-slate-500">@{user?.username}</p>
        </div>
      </div>
      <div className="space-y-1">
        {links.map(({ to, label, icon: Icon }) => (
          <Link key={label} to={to} className="flex items-center gap-3 rounded-md px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100">
            <Icon size={18} className="text-blue-600" aria-hidden="true" />
            {label}
          </Link>
        ))}
      </div>
    </aside>
  )
}
