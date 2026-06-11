import { useState } from 'react'
import api, { getApiError } from '../../api/client.js'
import { useAuth } from '../../auth/AuthContext.jsx'
import Button from '../common/Button.jsx'

export default function EditProfileForm() {
  const { user, setUser } = useAuth()
  const [form, setForm] = useState({
    firstName: user?.firstName || '',
    middleName: user?.middleName || '',
    lastName: user?.lastName || '',
    username: user?.username || '',
    email: user?.email || '',
    bio: user?.bio || '',
    gender: user?.gender || '',
    birthdate: user?.birthdate || '',
    phoneNumber: user?.phoneNumber || '',
    address: user?.address || '',
    city: user?.city || '',
    province: user?.province || '',
    country: user?.country || '',
  })
  const [message, setMessage] = useState('')
  const [error, setError] = useState('')

  const update = (field, value) => setForm((current) => ({ ...current, [field]: value }))

  const submit = async (event) => {
    event.preventDefault()
    setMessage('')
    setError('')
    try {
      const { data } = await api.put('/users/me', { ...form, birthdate: form.birthdate || null })
      setUser(data)
      setMessage('Profile updated successfully.')
    } catch (err) {
      setError(getApiError(err))
    }
  }

  return (
    <form onSubmit={submit} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <h1 className="text-2xl font-bold text-slate-950">Edit Profile</h1>
      {message && <div className="mt-4 rounded-md bg-green-50 p-3 text-sm text-green-700">{message}</div>}
      {error && <div className="mt-4 rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</div>}
      <div className="mt-5 grid gap-4 sm:grid-cols-2">
        {[
          ['firstName', 'First name'],
          ['middleName', 'Middle name'],
          ['lastName', 'Last name'],
          ['username', 'Username'],
          ['email', 'Email'],
          ['gender', 'Gender'],
          ['birthdate', 'Birthdate'],
          ['phoneNumber', 'Phone'],
          ['address', 'Address'],
          ['city', 'City'],
          ['province', 'Province'],
          ['country', 'Country'],
        ].map(([field, label]) => (
          <label key={field}>
            <span className="mb-1 block text-xs font-semibold text-slate-500">{label}</span>
            <input
              type={field === 'birthdate' ? 'date' : field === 'email' ? 'email' : 'text'}
              value={form[field]}
              onChange={(event) => update(field, event.target.value)}
              className="w-full rounded-md border border-slate-300 px-3 py-2.5 text-sm"
              required={['firstName', 'lastName', 'username', 'email'].includes(field)}
            />
          </label>
        ))}
        <label className="sm:col-span-2">
          <span className="mb-1 block text-xs font-semibold text-slate-500">Bio</span>
          <textarea value={form.bio} onChange={(event) => update('bio', event.target.value)} rows={4} className="w-full rounded-md border border-slate-300 px-3 py-2.5 text-sm" />
        </label>
      </div>
      <Button className="mt-5">Save Changes</Button>
    </form>
  )
}
