import { ImagePlus, Send } from 'lucide-react'
import { useState } from 'react'
import api, { getApiError } from '../../api/client.js'
import { useAuth } from '../../auth/AuthContext.jsx'
import Avatar from '../common/Avatar.jsx'
import Button from '../common/Button.jsx'

export default function CreatePostBox({ onCreated }) {
  const { user } = useAuth()
  const [content, setContent] = useState('')
  const [privacy, setPrivacy] = useState('PUBLIC')
  const [imageUrl, setImageUrl] = useState('')
  const [preview, setPreview] = useState('')
  const [error, setError] = useState('')

  const uploadImage = async (event) => {
    const file = event.target.files?.[0]
    if (!file) {
      return
    }
    setPreview(URL.createObjectURL(file))
    const data = new FormData()
    data.append('file', file)
    try {
      const response = await api.post('/posts/images', data)
      setImageUrl(response.data.url)
    } catch (err) {
      setError(getApiError(err))
    }
  }

  const submit = async (event) => {
    event.preventDefault()
    setError('')
    try {
      await api.post('/posts', { content, imageUrl, privacy })
      setContent('')
      setImageUrl('')
      setPreview('')
      onCreated?.()
    } catch (err) {
      setError(getApiError(err))
    }
  }

  return (
    <form onSubmit={submit} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <div className="flex gap-3">
        <Avatar src={user?.profilePictureUrl} name={user?.fullName} />
        <textarea value={content} onChange={(event) => setContent(event.target.value)} rows={3} placeholder={`What's happening, ${user?.firstName}?`} className="min-w-0 flex-1 resize-y rounded-md border border-slate-300 px-3 py-2.5 text-sm" />
      </div>
      {preview && <img src={preview} alt="" className="mt-4 max-h-72 w-full rounded-lg object-cover" />}
      {error && <div className="mt-3 rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</div>}
      <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
        <div className="flex gap-2">
          <select value={privacy} onChange={(event) => setPrivacy(event.target.value)} className="rounded-md border border-slate-300 px-3 py-2 text-sm">
            <option>PUBLIC</option>
            <option>FRIENDS</option>
            <option>PRIVATE</option>
          </select>
          <label className="inline-flex cursor-pointer items-center gap-2 rounded-md border border-slate-300 bg-white px-3 py-2 text-sm font-semibold text-slate-700 hover:bg-slate-50">
            <ImagePlus size={17} />
            Image
            <input type="file" accept="image/jpeg,image/png,image/webp" className="hidden" onChange={uploadImage} />
          </label>
        </div>
        <Button disabled={!content.trim() && !imageUrl}><Send size={17} />Post</Button>
      </div>
    </form>
  )
}
