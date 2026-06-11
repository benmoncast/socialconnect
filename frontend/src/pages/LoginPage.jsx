import LoginForm from '../components/auth/LoginForm.jsx'

export default function LoginPage() {
  return (
    <main className="grid min-h-screen place-items-center bg-slate-50 px-4 py-10">
      <div className="w-full max-w-md">
        <div className="mb-6 text-center">
          <p className="text-3xl font-black text-blue-700">SocialConnect</p>
          <p className="mt-2 text-sm text-slate-600">An original place to share, react, and connect.</p>
        </div>
        <LoginForm />
      </div>
    </main>
  )
}
