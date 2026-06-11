import { Sparkles, UsersRound } from 'lucide-react'

export default function RightSidebar() {
  return (
    <aside className="sticky top-24 hidden h-fit space-y-4 xl:block">
      <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
        <div className="mb-3 flex items-center gap-2 text-sm font-semibold text-slate-950">
          <Sparkles size={18} className="text-blue-600" />
          Friend Suggestions
        </div>
        <p className="text-sm text-slate-600">Search for people and send friend requests to grow your network.</p>
      </section>
      <section className="rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
        <div className="mb-3 flex items-center gap-2 text-sm font-semibold text-slate-950">
          <UsersRound size={18} className="text-green-600" />
          Online Friends
        </div>
        <p className="text-sm text-slate-600">Online presence is ready for a future real-time upgrade.</p>
      </section>
    </aside>
  )
}
