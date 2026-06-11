import { LoaderCircle, Save } from 'lucide-react'
import { useEffect, useState } from 'react'

const emptyValues = {
  firstName: '',
  middleName: '',
  lastName: '',
  username: '',
  bio: '',
  profilePictureUrl: '',
  coverPhotoUrl: '',
  gender: '',
  birthdate: '',
  phoneNumber: '',
  address: '',
  city: '',
  province: '',
  country: '',
}

const normalizeValues = (values = {}) =>
  Object.keys(emptyValues).reduce(
    (result, key) => ({
      ...result,
      [key]: values[key] ?? '',
    }),
    {},
  )

const textFields = [
  { name: 'firstName', label: 'First Name', required: true },
  { name: 'middleName', label: 'Middle Name' },
  { name: 'lastName', label: 'Last Name', required: true },
  { name: 'username', label: 'Username', required: true },
  { name: 'profilePictureUrl', label: 'Profile Picture URL', type: 'url', wide: true },
  { name: 'coverPhotoUrl', label: 'Cover Photo URL', type: 'url', wide: true },
  { name: 'gender', label: 'Gender' },
  { name: 'birthdate', label: 'Birthdate', type: 'date' },
  { name: 'phoneNumber', label: 'Phone Number' },
  { name: 'address', label: 'Address', wide: true },
  { name: 'city', label: 'City' },
  { name: 'province', label: 'Province' },
  { name: 'country', label: 'Country' },
]

const buildPayload = (values) =>
  Object.entries(values).reduce((result, [key, value]) => {
    const normalizedValue = typeof value === 'string' ? value.trim() : value
    return {
      ...result,
      [key]: normalizedValue === '' ? null : normalizedValue,
    }
  }, {})

export default function UserProfileForm({
  initialValues,
  isSubmitting,
  onSubmit,
  submitLabel = 'Save Profile',
  serverError,
  fieldErrors = {},
}) {
  const [values, setValues] = useState(() => normalizeValues(initialValues))
  const [localErrors, setLocalErrors] = useState({})

  useEffect(() => {
    setValues(normalizeValues(initialValues))
  }, [initialValues])

  const errors = { ...fieldErrors, ...localErrors }

  const validate = () => {
    const nextErrors = {}

    if (!values.firstName.trim()) {
      nextErrors.firstName = 'First name is required'
    }

    if (!values.lastName.trim()) {
      nextErrors.lastName = 'Last name is required'
    }

    if (!values.username.trim()) {
      nextErrors.username = 'Username is required'
    }

    setLocalErrors(nextErrors)
    return Object.keys(nextErrors).length === 0
  }

  const handleChange = (event) => {
    const { name, value } = event.target
    setValues((currentValues) => ({ ...currentValues, [name]: value }))
    setLocalErrors((currentErrors) => ({ ...currentErrors, [name]: undefined }))
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    if (!validate()) {
      return
    }

    onSubmit(buildPayload(values))
  }

  return (
    <form onSubmit={handleSubmit} className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm sm:p-6">
      {serverError && (
        <div className="mb-5 rounded-md border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {serverError}
        </div>
      )}

      <div className="grid gap-4 md:grid-cols-2">
        {textFields.map((field) => (
          <label key={field.name} className={field.wide ? 'md:col-span-2' : ''}>
            <span className="mb-1.5 block text-sm font-semibold text-slate-700">
              {field.label}
              {field.required && <span className="text-red-600"> *</span>}
            </span>
            <input
              type={field.type || 'text'}
              name={field.name}
              value={values[field.name]}
              onChange={handleChange}
              className="w-full rounded-md border border-slate-300 bg-white px-3 py-2.5 text-sm text-slate-950 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-4 focus:ring-blue-100"
              aria-invalid={Boolean(errors[field.name])}
            />
            {errors[field.name] && (
              <span className="mt-1 block text-sm text-red-600">{errors[field.name]}</span>
            )}
          </label>
        ))}

        <label className="md:col-span-2">
          <span className="mb-1.5 block text-sm font-semibold text-slate-700">Bio</span>
          <textarea
            name="bio"
            value={values.bio}
            onChange={handleChange}
            rows={4}
            className="w-full resize-y rounded-md border border-slate-300 bg-white px-3 py-2.5 text-sm text-slate-950 outline-none transition placeholder:text-slate-400 focus:border-blue-500 focus:ring-4 focus:ring-blue-100"
            aria-invalid={Boolean(errors.bio)}
          />
          {errors.bio && <span className="mt-1 block text-sm text-red-600">{errors.bio}</span>}
        </label>
      </div>

      <div className="mt-6 flex justify-end">
        <button
          type="submit"
          disabled={isSubmitting}
          className="inline-flex items-center justify-center gap-2 rounded-md bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white shadow-sm transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:bg-blue-300"
        >
          {isSubmitting ? (
            <LoaderCircle className="animate-spin" size={18} aria-hidden="true" />
          ) : (
            <Save size={18} aria-hidden="true" />
          )}
          {submitLabel}
        </button>
      </div>
    </form>
  )
}
