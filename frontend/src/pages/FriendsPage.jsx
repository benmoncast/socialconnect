import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import api from '../api/client.js'
import Button from '../components/common/Button.jsx'
import EmptyState from '../components/common/EmptyState.jsx'
import Loader from '../components/common/Loader.jsx'
import FriendListCard from '../components/friends/FriendListCard.jsx'

export default function FriendsPage() {
  const [friends, setFriends] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get('/friends').then((response) => setFriends(response.data)).finally(() => setLoading(false))
  }, [])

  if (loading) {
    return <Loader label="Loading friends..." />
  }

  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-slate-950">Friends</h1>
        <Link to="/friend-requests"><Button variant="secondary">Requests</Button></Link>
      </div>
      {friends.length === 0 ? <EmptyState title="No friends yet" description="Search for people and send your first request." /> : (
        <div className="grid gap-4 sm:grid-cols-2">{friends.map((friend) => <FriendListCard key={friend.id} user={friend} />)}</div>
      )}
    </div>
  )
}
