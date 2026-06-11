import api from '../../api/client.js'

const reactions = ['LIKE', 'LOVE', 'CARE', 'HAHA', 'WOW', 'SAD', 'ANGRY']

export default function ReactionPicker({ post, onChange }) {
  const react = async (type) => {
    if (post.currentUserReaction === type) {
      await api.delete(`/posts/${post.id}/reactions`)
    } else {
      await api.post(`/posts/${post.id}/reactions`, { type })
    }
    onChange?.()
  }

  return (
    <div className="flex flex-wrap gap-2">
      {reactions.map((type) => (
        <button
          key={type}
          type="button"
          onClick={() => react(type)}
          className={`rounded-md px-2 py-1 text-xs font-semibold ${
            post.currentUserReaction === type ? 'bg-blue-600 text-white' : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
          }`}
        >
          {type}
        </button>
      ))}
    </div>
  )
}
