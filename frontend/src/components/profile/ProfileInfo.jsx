export default function ProfileInfo({ user }) {
  const rows = [
    ['Bio', user.bio || 'No bio yet.'],
    ['Location', [user.city, user.province, user.country].filter(Boolean).join(', ') || 'Not set'],
    ['Phone', user.phoneNumber || 'Not set'],
    ['Birthdate', user.birthdate || 'Not set'],
    ['Gender', user.gender || 'Not set'],
  ]

  return (
    <section className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <h2 className="font-semibold text-slate-950">Profile Info</h2>
      <dl className="mt-4 space-y-3">
        {rows.map(([label, value]) => (
          <div key={label}>
            <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">{label}</dt>
            <dd className="mt-1 text-sm text-slate-700">{value}</dd>
          </div>
        ))}
      </dl>
    </section>
  )
}
