import { useEffect, useState } from 'react'
import api, { getApiError } from '../api/client.js'
import EmptyState from '../components/common/EmptyState.jsx'
import Loader from '../components/common/Loader.jsx'
import CreatePostBox from '../components/posts/CreatePostBox.jsx'
import PostCard from '../components/posts/PostCard.jsx'

export default function HomePage() {
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const loadFeed = async () => {
    setLoading(true)
    setError('')
    try {
      const { data } = await api.get('/posts/feed')
      setPosts(data)
    } catch (err) {
      setError(getApiError(err))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadFeed()
  }, [])

  return (
    <div className="space-y-5">
      <CreatePostBox onCreated={loadFeed} />
      {error && <div className="rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</div>}
      {loading ? <Loader label="Loading feed..." /> : posts.length === 0 ? (
        <EmptyState title="Your feed is quiet" description="Create a post or add friends to start seeing updates." />
      ) : (
        <div className="space-y-5">{posts.map((post) => <PostCard key={post.id} post={post} onRefresh={loadFeed} />)}</div>
      )}
    </div>
  )
}
