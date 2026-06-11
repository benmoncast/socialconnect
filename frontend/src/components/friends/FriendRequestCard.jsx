import api, { getApiError } from '../../api/client.js'
import Avatar from '../common/Avatar.jsx'
import Button from '../common/Button.jsx'

export default function FriendRequestCard({ request, type = 'received', onChange }) {
  const person = type === 'received' ? request.sender : request.receiver

  const run = async (action) => {
    try {
      await action()
      onChange?.()
    } catch (error) {
      alert(getApiError(error))
    }
  }

  return (
    <article className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
      <div className="flex items-center gap-3">
        <Avatar src={person.profilePictureUrl} name={person.fullName} />
        <div className="min-w-0">
          <p className="truncate font-semibold text-slate-950">{person.fullName}</p>
          <p className="truncate text-sm text-slate-500">@{person.username}</p>
        </div>
      </div>
      {type === 'received' ? (
        <div className="mt-4 flex gap-2">
          <Button onClick={() => run(() => api.post(`/friends/request/${request.id}/accept`))}>Accept</Button>
          <Button variant="secondary" onClick={() => run(() => api.post(`/friends/request/${request.id}/reject`))}>Reject</Button>
        </div>
      ) : (
        <p className="mt-4 text-sm text-slate-600">Request sent</p>
      )}
    </article>
  )
}
