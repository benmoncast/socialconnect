import { Link } from 'react-router-dom'
import Avatar from '../common/Avatar.jsx'
import Button from '../common/Button.jsx'
import FriendButton from '../friends/FriendButton.jsx'
import { getMediaUrl } from '../../api/client.js'
import CoverPhotoUploader from './CoverPhotoUploader.jsx'
import ProfilePhotoUploader from './ProfilePhotoUploader.jsx'

export default function ProfileHeader({ user, isMe, onRefresh }) {
  return (
    <section className="overflow-hidden rounded-lg border border-slate-200 bg-white shadow-sm">
      {user.coverPhotoUrl ? (
        <img src={getMediaUrl(user.coverPhotoUrl)} alt="" className="h-56 w-full object-cover" />
      ) : (
        <div className="h-56 bg-gradient-to-r from-blue-700 via-sky-500 to-slate-300" />
      )}
      <div className="px-5 pb-5">
        <div className="-mt-12 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-end">
            <div className="rounded-full border-4 border-white bg-white shadow-sm">
              <Avatar src={user.profilePictureUrl} name={user.fullName} size="lg" />
            </div>
            <div className="pb-1">
              <h1 className="text-3xl font-bold text-slate-950">{user.fullName}</h1>
              <p className="text-slate-500">@{user.username}</p>
            </div>
          </div>
          <div className="flex flex-wrap gap-2">
            {isMe ? (
              <>
                <ProfilePhotoUploader endpoint="/users/me/profile-picture" label="Profile Photo" onUploaded={onRefresh} />
                <CoverPhotoUploader onUploaded={onRefresh} />
                <Link to="/edit-profile"><Button variant="secondary">Edit Profile</Button></Link>
              </>
            ) : (
              <FriendButton user={user} onChange={onRefresh} />
            )}
          </div>
        </div>
      </div>
    </section>
  )
}
