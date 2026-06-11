import { Camera } from 'lucide-react'
import api, { getApiError } from '../../api/client.js'

export default function ProfilePhotoUploader({ endpoint, label, onUploaded }) {
  const upload = async (event) => {
    const file = event.target.files?.[0]
    if (!file) {
      return
    }

    const data = new FormData()
    data.append('file', file)

    try {
      const response = await api.post(endpoint, data)
      onUploaded?.(response.data)
    } catch (error) {
      alert(getApiError(error))
    } finally {
      event.target.value = ''
    }
  }

  return (
    <label className="inline-flex cursor-pointer items-center justify-center gap-2 rounded-md border border-slate-300 bg-white px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50">
      <input type="file" accept="image/jpeg,image/png,image/webp" className="hidden" onChange={upload} />
      <Camera size={17} />
      {label}
    </label>
  )
}
