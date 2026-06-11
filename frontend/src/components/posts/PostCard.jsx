import { useState } from 'react'
import { Link } from 'react-router-dom'
import Avatar from '../common/Avatar.jsx'
import ConfirmDialog from '../common/ConfirmDialog.jsx'
import api, { getMediaUrl } from '../../api/client.js'
import { useAuth } from '../../auth/AuthContext.jsx'
import CommentInput from './CommentInput.jsx'
import CommentList from './CommentList.jsx'
import PostActions from './PostActions.jsx'

export default function PostCard({ post, onRefresh }) {
  const { user } = useAuth()
  const [showComments, setShowComments] = useState(false)
  const [commentRefresh, setCommentRefresh] = useState(0)
  const [confirmDelete, setConfirmDelete] = useState(false)
  const isOwner = post.author.id === user?.id

  const deletePost = async () => {
    await api.delete(`/posts/${post.id}`)
    setConfirmDelete(false)
    onRefresh?.()
  }

  return (
    <article className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <div className="flex items-start gap-3">
        <Link to={`/profile/${post.author.id}`}><Avatar src={post.author.profilePictureUrl} name={post.author.fullName} /></Link>
        <div>
          <Link to={`/profile/${post.author.id}`} className="font-semibold text-slate-950 hover:text-blue-700">{post.author.fullName}</Link>
          <p className="text-xs text-slate-500">@{post.author.username} · {new Date(post.createdAt).toLocaleString()} · {post.privacy}</p>
        </div>
      </div>
      {post.content && <p className="mt-4 whitespace-pre-line text-slate-800">{post.content}</p>}
      {post.imageUrl && <img src={getMediaUrl(post.imageUrl)} alt="" className="mt-4 max-h-[520px] w-full rounded-lg object-cover" />}
      <div className="mt-4 text-sm text-slate-500">{post.reactionSummary.total} reactions</div>
      <PostActions post={post} isOwner={isOwner} onRefresh={onRefresh} onDelete={() => setConfirmDelete(true)} onToggleComments={() => setShowComments((value) => !value)} />
      {showComments && (
        <div className="mt-4">
          <CommentList postId={post.id} refreshKey={commentRefresh} />
          <CommentInput postId={post.id} onCreated={() => {
            setCommentRefresh((value) => value + 1)
            onRefresh?.()
          }} />
        </div>
      )}
      <ConfirmDialog open={confirmDelete} title="Delete post" message="This post and its comments will be removed." confirmLabel="Delete" onCancel={() => setConfirmDelete(false)} onConfirm={deletePost} />
    </article>
  )
}
