import { useEffect, useState } from 'react'
import client from '../api/client'
import IssueCard from '../components/IssueCard'
import MapView from '../components/MapView'
import { CATEGORIES, STATUSES, CATEGORY_LABELS, formatLabel } from '../constants'

export default function FeedPage() {
  const [issues, setIssues] = useState([])
  const [loading, setLoading] = useState(true)
  const [view, setView] = useState('list')
  const [category, setCategory] = useState('')
  const [status, setStatus] = useState('')
  const [keyword, setKeyword] = useState('')

  useEffect(() => {
    const params = {}
    if (category) params.category = category
    if (status) params.status = status
    if (keyword) params.keyword = keyword

    setLoading(true)
    client
      .get('/api/issues', { params })
      .then((res) => setIssues(res.data.content))
      .finally(() => setLoading(false))
  }, [category, status, keyword])

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 py-6">
      <div className="flex flex-wrap items-center justify-between gap-3 mb-6">
        <h1 className="text-xl font-semibold text-slate-900">Community Issue Feed</h1>
        <div className="flex bg-slate-200 rounded-md p-1 text-sm">
          <button
            onClick={() => setView('list')}
            className={`px-3 py-1 rounded ${view === 'list' ? 'bg-white shadow-sm' : 'text-slate-600'}`}
          >
            List
          </button>
          <button
            onClick={() => setView('map')}
            className={`px-3 py-1 rounded ${view === 'map' ? 'bg-white shadow-sm' : 'text-slate-600'}`}
          >
            Map
          </button>
        </div>
      </div>

      <div className="flex flex-wrap gap-3 mb-6">
        <input
          placeholder="Search issues…"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          className="border border-slate-300 rounded-md px-3 py-2 text-sm flex-1 min-w-[180px]"
        />
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="border border-slate-300 rounded-md px-3 py-2 text-sm"
        >
          <option value="">All categories</option>
          {CATEGORIES.map((c) => (
            <option key={c} value={c}>{CATEGORY_LABELS[c]}</option>
          ))}
        </select>
        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          className="border border-slate-300 rounded-md px-3 py-2 text-sm"
        >
          <option value="">All statuses</option>
          {STATUSES.map((s) => (
            <option key={s} value={s}>{formatLabel(s)}</option>
          ))}
        </select>
      </div>

      {loading ? (
        <p className="text-slate-500 text-sm">Loading issues…</p>
      ) : issues.length === 0 ? (
        <p className="text-slate-500 text-sm">No issues match these filters yet.</p>
      ) : view === 'list' ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {issues.map((issue) => (
            <IssueCard key={issue.id} issue={issue} />
          ))}
        </div>
      ) : (
        <MapView issues={issues} />
      )}
    </div>
  )
}
