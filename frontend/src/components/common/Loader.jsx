import { LoaderCircle } from 'lucide-react'

export default function Loader({ label = 'Loading...' }) {
  return (
    <div className="flex min-h-[240px] items-center justify-center text-slate-600">
      <LoaderCircle className="mr-2 animate-spin" size={20} aria-hidden="true" />
      {label}
    </div>
  )
}
