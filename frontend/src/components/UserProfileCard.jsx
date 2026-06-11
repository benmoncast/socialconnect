import { Eye, MapPin, Pencil, Trash2, UserRound } from 'lucide-react'
import { Link } from 'react-router-dom'

const getFullName = (profile) =>
  [profile.firstName, profile.middleName, profile.lastName].filter(Boolean).join(' ')

export default function UserProfileCard({ profile, onDelete }) {
  return (
    <article className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
      <div className="flex items-start gap-4">
        {profile.profilePictureUrl ? (
          <img
            src={profile.profilePictureUrl}
            alt={`${getFullName(profile)} profile`}
            className="h-14 w-14 rounded-md object-cover"
          />
        ) : (
          <span className="flex h-14 w-14 items-center justify-center rounded-md bg-slate-100 text-slate-500">
            <UserRound size={24} aria-hidden="true" />
          </span>
        )}

        <div className="min-w-0 flex-1">
          <h2 className="truncate text-base font-semibold text-slate-950">{getFullName(profile)}</h2>
          <p className="truncate text-sm text-slate-500">@{profile.username}</p>
          <p className="mt-2 flex items-center gap-1.5 text-sm text-slate-600">
            <MapPin size={15} aria-hidden="true" />
            {[profile.city, profile.province, profile.country].filter(Boolean).join(', ') || 'No location'}
          </p>
        </div>
      </div>

      <div className="mt-4 grid grid-cols-3 gap-2">
        <Link
          to={`/user-profiles/${profile.id}`}
          className="inline-flex items-center justify-center gap-2 rounded-md border border-slate-300 px-3 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
        >
          <Eye size={16} aria-hidden="true" />
          View
        </Link>
        <Link
          to={`/user-profiles/edit/${profile.id}`}
          className="inline-flex items-center justify-center gap-2 rounded-md border border-blue-200 bg-blue-50 px-3 py-2 text-sm font-semibold text-blue-700 transition hover:bg-blue-100"
        >
          <Pencil size={16} aria-hidden="true" />
          Edit
        </Link>
        <button
          type="button"
          onClick={() => onDelete(profile)}
          className="inline-flex items-center justify-center gap-2 rounded-md border border-red-200 bg-red-50 px-3 py-2 text-sm font-semibold text-red-700 transition hover:bg-red-100"
        >
          <Trash2 size={16} aria-hidden="true" />
          Delete
        </button>
      </div>
    </article>
  )
}
