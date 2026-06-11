import { useState } from 'react'
import api from '../../api/client.js'
import Button from '../common/Button.jsx'

export default function CommentInput({ postId, onCreated }) {
  const [content, setContent] = useState('')

  const submit = async (event) => {
    event.preventDefault()
    if (!content.trim()) {
      return
    }
    await api.post(`/posts/${postId}/comments`, { content: content.trim() })
    setContent('')
    onCreated?.()
  }

  return (
    <form onSubmit={submit} className="mt-3 flex gap-2">
      <input value={content} onChange={(event) => setContent(event.target.value)} placeholder="Write a comment" className="min-w-0 flex-1 rounded-md border border-slate-300 px-3 py-2 text-sm" />
      <Button>Post</Button>
    </form>
  )
}
