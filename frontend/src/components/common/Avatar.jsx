import { UserRound } from 'lucide-react'
import { getMediaUrl } from '../../api/client.js'

export default function Avatar({ src, name = 'User', size = 'md' }) {
  const sizes = {
    sm: 'h-9 w-9',
    md: 'h-11 w-11',
    lg: 'h-24 w-24',
  }

  if (src) {
    return <img src={getMediaUrl(src)} alt={name} className={`${sizes[size]} rounded-full object-cover`} />
  }

  return (
    <span className={`${sizes[size]} flex items-center justify-center rounded-full bg-slate-200 text-slate-500`}>
      <UserRound size={size === 'lg' ? 38 : 20} aria-hidden="true" />
    </span>
  )
}
