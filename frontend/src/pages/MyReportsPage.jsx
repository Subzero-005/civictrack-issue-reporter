import { useEffect, useState } from 'react'
import client from '../api/client'
import IssueCard from '../components/IssueCard'

export default function MyReportsPage() {
  const [issues, setIssues] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(false)

  useEffect(() => {
    client
      .get('/api/issues', { params: { mine: true } })
      .then((res) => setIssues(res.data.content))
      .catch(() => setError(true))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 py-6">
      <h1 className="text-xl font-semibold text-slate-900 mb-6">My Reports</h1>

      {loading ? (
        <p className="text-slate-500 text-sm">Loading…</p>
      ) : error ? (
        <p className="text-red-600 text-sm">Could not load your reports. Check your connection and try again.</p>
      ) : issues.length === 0 ? (
        <p className="text-slate-500 text-sm">You haven't reported any issues yet.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {issues.map((issue) => (
            <IssueCard key={issue.id} issue={issue} />
          ))}
        </div>
      )}
    </div>
  )
}
