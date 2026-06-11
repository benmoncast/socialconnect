import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import api, { getApiError } from '../api/client.js'
import { useAuth } from '../auth/AuthContext.jsx'
import EmptyState from '../components/common/EmptyState.jsx'
import Loader from '../components/common/Loader.jsx'
import PostCard from '../components/posts/PostCard.jsx'
import ProfileHeader from '../components/profile/ProfileHeader.jsx'
import ProfileInfo from '../components/profile/ProfileInfo.jsx'

export default function ProfilePage() {
  const { id } = useParams()
  const { user: currentUser, setUser } = useAuth()
  const [profile, setProfile] = useState(null)
  const [posts, setPosts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const loadProfile = async () => {
    setLoading(true)
    try {
      const [profileResponse, postsResponse] = await Promise.all([
        api.get(`/users/${id}`),
        api.get(`/posts/user/${id}`),
      ])
      setProfile(profileResponse.data)
      setPosts(postsResponse.data)
      if (Number(id) === currentUser?.id) {
        setUser(profileResponse.data)
      }
    } catch (err) {
      setError(getApiError(err))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    const load = async () => {
      setLoading(true)
      try {
        const [profileResponse, postsResponse] = await Promise.all([
          api.get(`/users/${id}`),
          api.get(`/posts/user/${id}`),
        ])
        setProfile(profileResponse.data)
        setPosts(postsResponse.data)
        if (Number(id) === currentUser?.id) {
          setUser(profileResponse.data)
        }
      } catch (err) {
        setError(getApiError(err))
      } finally {
        setLoading(false)
      }
    }

    load()
  }, [id, currentUser?.id, setUser])

  if (loading) {
    return <Loader label="Loading profile..." />
  }

  if (error || !profile) {
    return <EmptyState title="Profile unavailable" description={error || 'This profile could not be found.'} />
  }

  const isMe = profile.id === currentUser?.id

  return (
    <div className="space-y-5">
      <ProfileHeader user={profile} isMe={isMe} onRefresh={loadProfile} />
      <div className="grid gap-5 xl:grid-cols-[280px_1fr]">
        <ProfileInfo user={profile} />
        <div className="space-y-5">
          {posts.length === 0 ? <EmptyState title="No posts yet" description="Posts from this profile will appear here." /> : posts.map((post) => <PostCard key={post.id} post={post} onRefresh={loadProfile} />)}
        </div>
      </div>
    </div>
  )
}
