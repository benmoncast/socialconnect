import { MessageCircle, Trash2 } from 'lucide-react'
import Button from '../common/Button.jsx'
import ReactionPicker from './ReactionPicker.jsx'

export default function PostActions({ post, isOwner, onRefresh, onDelete, onToggleComments }) {
  return (
    <div className="mt-4 border-t border-slate-100 pt-4">
      <ReactionPicker post={post} onChange={onRefresh} />
      <div className="mt-3 flex flex-wrap items-center gap-2">
        <Button type="button" variant="secondary" onClick={onToggleComments}>
          <MessageCircle size={17} />
          {post.commentsCount} Comments
        </Button>
        {isOwner && (
          <Button type="button" variant="danger" onClick={onDelete}>
            <Trash2 size={17} />
            Delete
          </Button>
        )}
      </div>
    </div>
  )
}
