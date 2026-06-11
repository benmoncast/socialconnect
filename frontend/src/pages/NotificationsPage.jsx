import { useEffect, useState } from 'react'
import api from '../api/client.js'
import Button from '../components/common/Button.jsx'
import EmptyState from '../components/common/EmptyState.jsx'
import Loader from '../components/common/Loader.jsx'
import NotificationItem from '../components/notifications/NotificationItem.jsx'

export default function NotificationsPage() {
  const [notifications, setNotifications] = useState([])
  const [loading, setLoading] = useState(true)

  const load = async () => {
    const { data } = await api.get('/notifications')
    setNotifications(data)
    setLoading(false)
  }

  useEffect(() => {
    load()
  }, [])

  const markAll = async () => {
    await api.put('/notifications/read-all')
    load()
  }

  if (loading) {
    return <Loader label="Loading notifications..." />
  }

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-950">Notifications</h1>
        <Button variant="secondary" onClick={markAll}>Mark all as read</Button>
      </div>
      {notifications.length === 0 ? <EmptyState title="No notifications" description="Friend and post activity appears here." /> : notifications.map((notification) => <NotificationItem key={notification.id} notification={notification} onRead={load} />)}
    </div>
  )
}
