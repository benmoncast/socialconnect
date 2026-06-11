import { Eye, Pencil, Trash2, UserRound } from 'lucide-react'
import { Link } from 'react-router-dom'
import UserProfileCard from './UserProfileCard.jsx'

const getFullName = (profile) =>
  [profile.firstName, profile.middleName, profile.lastName].filter(Boolean).join(' ')

export default function UserProfileTable({ profiles, onDelete }) {
  return (
    <>
      <div className="grid gap-4 md:hidden">
        {profiles.map((profile) => (
          <UserProfileCard key={profile.id} profile={profile} onDelete={onDelete} />
        ))}
      </div>

      <div className="hidden overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm md:block">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-slate-200">
            <thead className="bg-slate-50">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Profile
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Username
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Gender
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                  City
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Province
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Country
                </th>
                <th className="px-4 py-3 text-right text-xs font-semibold uppercase tracking-wide text-slate-500">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 bg-white">
              {profiles.map((profile) => (
                <tr key={profile.id} className="hover:bg-slate-50">
                  <td className="px-4 py-4">
                    <div className="flex items-center gap-3">
                      {profile.profilePictureUrl ? (
                        <img
                          src={profile.profilePictureUrl}
                          alt={`${getFullName(profile)} profile`}
                          className="h-11 w-11 rounded-md object-cover"
                        />
                      ) : (
                        <span className="flex h-11 w-11 items-center justify-center rounded-md bg-slate-100 text-slate-500">
                          <UserRound size={21} aria-hidden="true" />
                        </span>
                      )}
                      <span className="font-medium text-slate-950">{getFullName(profile)}</span>
                    </div>
                  </td>
                  <td className="px-4 py-4 text-sm text-slate-600">@{profile.username}</td>
                  <td className="px-4 py-4 text-sm text-slate-600">{profile.gender || '-'}</td>
                  <td className="px-4 py-4 text-sm text-slate-600">{profile.city || '-'}</td>
                  <td className="px-4 py-4 text-sm text-slate-600">{profile.province || '-'}</td>
                  <td className="px-4 py-4 text-sm text-slate-600">{profile.country || '-'}</td>
                  <td className="px-4 py-4">
                    <div className="flex justify-end gap-2">
                      <Link
                        to={`/user-profiles/${profile.id}`}
                        className="rounded-md p-2 text-slate-600 transition hover:bg-slate-100 hover:text-slate-950"
                        aria-label={`View ${getFullName(profile)}`}
                      >
                        <Eye size={17} aria-hidden="true" />
                      </Link>
                      <Link
                        to={`/user-profiles/edit/${profile.id}`}
                        className="rounded-md p-2 text-blue-600 transition hover:bg-blue-50 hover:text-blue-700"
                        aria-label={`Edit ${getFullName(profile)}`}
                      >
                        <Pencil size={17} aria-hidden="true" />
                      </Link>
                      <button
                        type="button"
                        onClick={() => onDelete(profile)}
                        className="rounded-md p-2 text-red-600 transition hover:bg-red-50 hover:text-red-700"
                        aria-label={`Delete ${getFullName(profile)}`}
                      >
                        <Trash2 size={17} aria-hidden="true" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  )
}
