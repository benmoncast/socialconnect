import { AlertTriangle, LoaderCircle, X } from 'lucide-react'

export default function ConfirmDeleteModal({ profile, isDeleting, onCancel, onConfirm }) {
  if (!profile) {
    return null
  }

  const fullName = [profile.firstName, profile.middleName, profile.lastName].filter(Boolean).join(' ')

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-950/50 px-4">
      <section className="w-full max-w-md rounded-lg bg-white p-6 shadow-xl">
        <div className="flex items-start justify-between gap-4">
          <div className="flex items-center gap-3">
            <span className="flex h-11 w-11 items-center justify-center rounded-md bg-red-100 text-red-600">
              <AlertTriangle size={22} aria-hidden="true" />
            </span>
            <div>
              <h2 className="text-lg font-semibold text-slate-950">Delete profile</h2>
              <p className="mt-1 text-sm text-slate-600">{fullName || profile.username}</p>
            </div>
          </div>
          <button
            type="button"
            onClick={onCancel}
            className="rounded-md p-2 text-slate-500 transition hover:bg-slate-100 hover:text-slate-950"
            aria-label="Close delete confirmation"
            disabled={isDeleting}
          >
            <X size={18} aria-hidden="true" />
          </button>
        </div>

        <p className="mt-5 text-sm leading-6 text-slate-700">
          This action permanently removes the selected user profile from the database.
        </p>

        <div className="mt-6 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <button
            type="button"
            onClick={onCancel}
            className="inline-flex items-center justify-center rounded-md border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
            disabled={isDeleting}
          >
            Cancel
          </button>
          <button
            type="button"
            onClick={onConfirm}
            className="inline-flex items-center justify-center gap-2 rounded-md bg-red-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-red-700 disabled:cursor-not-allowed disabled:bg-red-300"
            disabled={isDeleting}
          >
            {isDeleting && <LoaderCircle className="animate-spin" size={17} aria-hidden="true" />}
            Delete
          </button>
        </div>
      </section>
    </div>
  )
}
