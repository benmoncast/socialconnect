import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { UserPlus } from 'lucide-react'
import { getApiError } from '../../api/client.js'
import { useAuth } from '../../auth/AuthContext.jsx'
import Button from '../common/Button.jsx'

const initialForm = {
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  password: '',
  gender: '',
  birthdate: '',
}

export default function RegisterForm() {
  const navigate = useNavigate()
  const { register } = useAuth()
  const [form, setForm] = useState(initialForm)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const update = (field, value) => setForm((current) => ({ ...current, [field]: value }))

  const submit = async (event) => {
    event.preventDefault()
    setLoading(true)
    setError('')
    try {
      await register({ ...form, birthdate: form.birthdate || null })
      navigate('/')
    } catch (err) {
      setError(getApiError(err))
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={submit} className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-bold text-slate-950">Join SocialConnect</h1>
      <p className="mt-2 text-sm text-slate-600">Create your original social profile.</p>
      {error && <div className="mt-4 rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</div>}
      <div className="mt-5 grid gap-4 sm:grid-cols-2">
        <input className="rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="First name" value={form.firstName} onChange={(event) => update('firstName', event.target.value)} required />
        <input className="rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Last name" value={form.lastName} onChange={(event) => update('lastName', event.target.value)} required />
        <input className="rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Username" value={form.username} onChange={(event) => update('username', event.target.value)} required />
        <input className="rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Email" type="email" value={form.email} onChange={(event) => update('email', event.target.value)} required />
        <input className="rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Password" type="password" value={form.password} onChange={(event) => update('password', event.target.value)} required minLength={6} />
        <input className="rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Gender" value={form.gender} onChange={(event) => update('gender', event.target.value)} />
        <label className="sm:col-span-2">
          <span className="mb-1 block text-xs font-semibold text-slate-500">Birthdate</span>
          <input className="w-full rounded-md border border-slate-300 px-3 py-2.5 text-sm" type="date" value={form.birthdate} onChange={(event) => update('birthdate', event.target.value)} />
        </label>
      </div>
      <Button className="mt-5 w-full" disabled={loading}><UserPlus size={18} />{loading ? 'Creating account...' : 'Register'}</Button>
      <p className="mt-4 text-center text-sm text-slate-600">Already registered? <Link className="font-semibold text-blue-700" to="/login">Log in</Link></p>
    </form>
  )
}
