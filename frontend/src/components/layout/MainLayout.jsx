import { Outlet } from 'react-router-dom'
import Navbar from './Navbar.jsx'
import Sidebar from './Sidebar.jsx'
import RightSidebar from './RightSidebar.jsx'

export default function MainLayout() {
  return (
    <div className="min-h-screen bg-slate-50">
      <Navbar />
      <div className="mx-auto grid max-w-7xl gap-6 px-4 py-6 lg:grid-cols-[240px_minmax(0,1fr)] lg:px-8 xl:grid-cols-[240px_minmax(0,1fr)_280px]">
        <Sidebar />
        <main className="min-w-0">
          <Outlet />
        </main>
        <RightSidebar />
      </div>
    </div>
  )
}
