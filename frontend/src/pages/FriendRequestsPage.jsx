import { useEffect, useState } from 'react'
import api from '../api/client.js'
import EmptyState from '../components/common/EmptyState.jsx'
import Loader from '../components/common/Loader.jsx'
import FriendRequestCard from '../components/friends/FriendRequestCard.jsx'

export default function FriendRequestsPage() {
  const [received, setReceived] = useState([])
  const [sent, setSent] = useState([])
  const [loading, setLoading] = useState(true)

  const load = async () => {
    setLoading(true)
    const [receivedResponse, sentResponse] = await Promise.all([
      api.get('/friends/requests/received'),
      api.get('/friends/requests/sent'),
    ])
    setReceived(receivedResponse.data)
    setSent(sentResponse.data)
    setLoading(false)
  }

  useEffect(() => {
    load()
  }, [])

  if (loading) {
    return <Loader label="Loading requests..." />
  }

  return (
    <div className="space-y-6">
      <section>
        <h1 className="mb-4 text-2xl font-bold text-slate-950">Friend Requests</h1>
        {received.length === 0 ? <EmptyState title="No received requests" /> : <div className="grid gap-4 sm:grid-cols-2">{received.map((request) => <FriendRequestCard key={request.id} request={request} onChange={load} />)}</div>}
      </section>
      <section>
        <h2 className="mb-4 text-xl font-bold text-slate-950">Sent Requests</h2>
        {sent.length === 0 ? <EmptyState title="No sent requests" /> : <div className="grid gap-4 sm:grid-cols-2">{sent.map((request) => <FriendRequestCard key={request.id} request={request} type="sent" onChange={load} />)}</div>}
      </section>
    </div>
  )
}
