import { Check, UserPlus, UsersRound, X } from 'lucide-react'
import api, { getApiError } from '../../api/client.js'
import Button from '../common/Button.jsx'

export default function FriendButton({ user, onChange }) {
  if (!user || user.friendshipStatus === 'SELF') {
    return null
  }

  const run = async (action) => {
    try {
      await action()
      onChange?.()
    } catch (error) {
      alert(getApiError(error))
    }
  }

  if (user.friendshipStatus === 'FRIENDS') {
    return <Button variant="secondary" onClick={() => run(() => api.delete(`/friends/${user.id}`))}><UsersRound size={17} />Unfriend</Button>
  }

  if (user.friendshipStatus === 'REQUEST_SENT') {
    return <Button variant="secondary" onClick={() => run(() => api.delete(`/friends/request/${user.id}/cancel`))}><X size={17} />Cancel Request</Button>
  }

  if (user.friendshipStatus === 'RESPOND_TO_REQUEST') {
    return <Button variant="secondary" disabled><Check size={17} />Respond in Requests</Button>
  }

  return <Button onClick={() => run(() => api.post(`/friends/request/${user.id}`))}><UserPlus size={17} />Add Friend</Button>
}
