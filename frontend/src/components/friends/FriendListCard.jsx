import { Link } from 'react-router-dom'
import Avatar from '../common/Avatar.jsx'

export default function FriendListCard({ user }) {
  return (
    <Link to={`/profile/${user.id}`} className="flex items-center gap-3 rounded-lg border border-slate-200 bg-white p-4 shadow-sm hover:border-blue-200">
      <Avatar src={user.profilePictureUrl} name={user.fullName} />
      <div className="min-w-0">
        <p className="truncate font-semibold text-slate-950">{user.fullName}</p>
        <p className="truncate text-sm text-slate-500">@{user.username}</p>
        <p className="truncate text-xs text-slate-500">{[user.city, user.country].filter(Boolean).join(', ')}</p>
      </div>
    </Link>
  )
}
