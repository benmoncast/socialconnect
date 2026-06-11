import { Link } from 'react-router-dom'
import Button from '../components/common/Button.jsx'

export default function NotFoundPage() {
  return (
    <main className="grid min-h-screen place-items-center bg-slate-50 px-4">
      <div className="text-center">
        <p className="text-5xl font-black text-blue-700">404</p>
        <h1 className="mt-3 text-2xl font-bold text-slate-950">Page not found</h1>
        <p className="mt-2 text-slate-600">That SocialConnect page does not exist.</p>
        <Link to="/"><Button className="mt-5">Go Home</Button></Link>
      </div>
    </main>
  )
}
