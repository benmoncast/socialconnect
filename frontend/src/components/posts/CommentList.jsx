import { useEffect, useState } from 'react'
import api from '../../api/client.js'
import Avatar from '../common/Avatar.jsx'
import EmptyState from '../common/EmptyState.jsx'

export default function CommentList({ postId, refreshKey }) {
  const [comments, setComments] = useState([])

  useEffect(() => {
    api.get(`/posts/${postId}/comments`).then((response) => setComments(response.data)).catch(() => setComments([]))
  }, [postId, refreshKey])

  if (comments.length === 0) {
    return <EmptyState title="No comments yet" description="Start the conversation." />
  }

  return (
    <div className="mt-3 space-y-3">
      {comments.map((comment) => (
        <div key={comment.id} className="flex gap-3 rounded-md bg-slate-50 p-3">
          <Avatar src={comment.author.profilePictureUrl} name={comment.author.fullName} size="sm" />
          <div>
            <p className="text-sm font-semibold text-slate-950">{comment.author.fullName}</p>
            <p className="text-sm text-slate-700">{comment.content}</p>
          </div>
        </div>
      ))}
    </div>
  )
}
