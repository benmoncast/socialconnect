import api from '../../api/client.js'
import Avatar from '../common/Avatar.jsx'

export default function NotificationItem({ notification, onRead }) {
  const markRead = async () => {
    if (!notification.read) {
      await api.put(`/notifications/${notification.id}/read`)
      onRead?.()
    }
  }

  return (
    <button
      type="button"
      onClick={markRead}
      className={`flex w-full items-start gap-3 rounded-lg border p-4 text-left shadow-sm ${
        notification.read ? 'border-slate-200 bg-white' : 'border-blue-200 bg-blue-50'
      }`}
    >
      <Avatar src={notification.actor?.profilePictureUrl} name={notification.actor?.fullName} />
      <div>
        <p className="text-sm font-medium text-slate-900">{notification.message}</p>
        <p className="mt-1 text-xs text-slate-500">{new Date(notification.createdAt).toLocaleString()}</p>
      </div>
    </button>
  )
}
