import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { LogIn } from 'lucide-react'
import { getApiError } from '../../api/client.js'
import { useAuth } from '../../auth/AuthContext.jsx'
import Button from '../common/Button.jsx'

export default function LoginForm() {
  const navigate = useNavigate()
  const { login } = useAuth()
  const [form, setForm] = useState({ identifier: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const submit = async (event) => {
    event.preventDefault()
    setLoading(true)
    setError('')
    try {
      await login(form)
      navigate('/')
    } catch (err) {
      setError(getApiError(err))
    } finally {
      setLoading(false)
    }
  }

  return (
    <form onSubmit={submit} className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
      <h1 className="text-2xl font-bold text-slate-950">Welcome back</h1>
      <p className="mt-2 text-sm text-slate-600">Log in with your email or username.</p>
      {error && <div className="mt-4 rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</div>}
      <div className="mt-5 space-y-4">
        <input className="w-full rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Email or username" value={form.identifier} onChange={(event) => setForm({ ...form, identifier: event.target.value })} required />
        <input className="w-full rounded-md border border-slate-300 px-3 py-2.5 text-sm" placeholder="Password" type="password" value={form.password} onChange={(event) => setForm({ ...form, password: event.target.value })} required />
      </div>
      <Button className="mt-5 w-full" disabled={loading}><LogIn size={18} />{loading ? 'Logging in...' : 'Log In'}</Button>
      <p className="mt-4 text-center text-sm text-slate-600">New here? <Link className="font-semibold text-blue-700" to="/register">Create an account</Link></p>
    </form>
  )
}
