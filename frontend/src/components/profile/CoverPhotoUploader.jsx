import ProfilePhotoUploader from './ProfilePhotoUploader.jsx'

export default function CoverPhotoUploader({ onUploaded }) {
  return <ProfilePhotoUploader endpoint="/users/me/cover-photo" label="Change Cover" onUploaded={onUploaded} />
}
