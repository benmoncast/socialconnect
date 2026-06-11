import RegisterForm from '../components/auth/RegisterForm.jsx'

export default function RegisterPage() {
  return (
    <main className="grid min-h-screen place-items-center bg-slate-50 px-4 py-10">
      <div className="w-full max-w-2xl">
        <div className="mb-6 text-center">
          <p className="text-3xl font-black text-blue-700">SocialConnect</p>
          <p className="mt-2 text-sm text-slate-600">Create your account and start building your circle.</p>
        </div>
        <RegisterForm />
      </div>
    </main>
  )
}
