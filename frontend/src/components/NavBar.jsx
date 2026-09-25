import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function NavBar() {
  const { user, isAdmin, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-white border-b border-slate-200 px-4 sm:px-6 py-3 flex items-center justify-between">
      <Link to="/" className="text-lg font-semibold text-slate-900">
        CivicTrack
      </Link>

      <div className="flex items-center gap-3 sm:gap-5 text-sm">
        {user && (
          <>
            <Link to="/" className="text-slate-600 hover:text-slate-900">Feed</Link>
            <Link to="/report" className="text-slate-600 hover:text-slate-900">Report Issue</Link>
            <Link to="/my-reports" className="text-slate-600 hover:text-slate-900">My Reports</Link>
            {isAdmin && (
              <>
                <Link to="/admin/triage" className="text-slate-600 hover:text-slate-900">Triage</Link>
                <Link to="/admin/dashboard" className="text-slate-600 hover:text-slate-900">Dashboard</Link>
              </>
            )}
            <span className="hidden sm:inline text-slate-400">|</span>
            <span className="hidden sm:inline text-slate-500">{user.name}</span>
            <button
              onClick={handleLogout}
              className="px-3 py-1.5 rounded-md bg-slate-900 text-white hover:bg-slate-700"
            >
              Log out
            </button>
          </>
        )}
      </div>
    </nav>
  )
}
