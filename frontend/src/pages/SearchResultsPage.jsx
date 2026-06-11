import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import api from '../api/client.js'
import EmptyState from '../components/common/EmptyState.jsx'
import FriendListCard from '../components/friends/FriendListCard.jsx'

export default function SearchResultsPage() {
  const [params] = useSearchParams()
  const query = params.get('query') || ''
  const [results, setResults] = useState([])

  useEffect(() => {
    const load = async () => {
      const { data } = await api.get('/users/search', { params: { query } })
      setResults(data)
    }

    load()
  }, [query])

  return (
    <div className="space-y-5">
      <h1 className="text-2xl font-bold text-slate-950">Search results for "{query}"</h1>
      {results.length === 0 ? <EmptyState title="No people found" description="Try a name, username, or email." /> : <div className="grid gap-4 sm:grid-cols-2">{results.map((user) => <FriendListCard key={user.id} user={user} />)}</div>}
    </div>
  )
}
