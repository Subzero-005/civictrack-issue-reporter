import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import client from '../api/client'
import Badge from '../components/Badge'
import { STATUS_STYLES, PRIORITY_STYLES, STATUSES, PRIORITIES, CATEGORIES, CATEGORY_LABELS, formatLabel } from '../constants'

export default function AdminTriagePage() {
  const [issues, setIssues] = useState([])
  const [loading, setLoading] = useState(true)
  const [status, setStatus] = useState('')
  const [priority, setPriority] = useState('')
  const [category, setCategory] = useState('')

  const load = () => {
    const params = {}
    if (status) params.status = status
    if (priority) params.priority = priority
    if (category) params.category = category

    setLoading(true)
    client
      .get('/api/issues', { params: { ...params, size: 50 } })
      .then((res) => setIssues(res.data.content))
      .finally(() => setLoading(false))
  }

  useEffect(load, [status, priority, category])

  const updateField = async (id, field, value) => {
    await client.patch(`/api/issues/${id}/status`, { [field]: value })
    load()
  }

  return (
    <div className="max-w-6xl mx-auto px-4 sm:px-6 py-6">
      <h1 className="text-xl font-semibold text-slate-900 mb-6">Admin Triage Board</h1>

      <div className="flex flex-wrap gap-3 mb-6">
        <select value={status} onChange={(e) => setStatus(e.target.value)} className="border border-slate-300 rounded-md px-3 py-2 text-sm">
          <option value="">All statuses</option>
          {STATUSES.map((s) => <option key={s} value={s}>{formatLabel(s)}</option>)}
        </select>
        <select value={priority} onChange={(e) => setPriority(e.target.value)} className="border border-slate-300 rounded-md px-3 py-2 text-sm">
          <option value="">All priorities</option>
          {PRIORITIES.map((p) => <option key={p} value={p}>{formatLabel(p)}</option>)}
        </select>
        <select value={category} onChange={(e) => setCategory(e.target.value)} className="border border-slate-300 rounded-md px-3 py-2 text-sm">
          <option value="">All categories</option>
          {CATEGORIES.map((c) => <option key={c} value={c}>{CATEGORY_LABELS[c]}</option>)}
        </select>
      </div>

      {loading ? (
        <p className="text-slate-500 text-sm">Loading…</p>
      ) : (
        <div className="bg-white border border-slate-200 rounded-xl overflow-hidden overflow-x-auto">
          <table className="w-full text-sm min-w-[720px]">
            <thead className="bg-slate-50 text-slate-500 text-left">
              <tr>
                <th className="px-4 py-3">Issue</th>
                <th className="px-4 py-3">Reporter</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Priority</th>
                <th className="px-4 py-3">Reported</th>
              </tr>
            </thead>
            <tbody>
              {issues.map((issue) => (
                <tr key={issue.id} className="border-t border-slate-100">
                  <td className="px-4 py-3">
                    <Link to={`/issues/${issue.id}`} className="font-medium text-slate-900 hover:underline">
                      {issue.title}
                    </Link>
                    <div className="mt-1"><Badge value={issue.category} styles={{}} /></div>
                  </td>
                  <td className="px-4 py-3 text-slate-600">{issue.reporterName}</td>
                  <td className="px-4 py-3">
                    <select
                      value={issue.status}
                      onChange={(e) => updateField(issue.id, 'status', e.target.value)}
                      className="border border-slate-300 rounded-md px-2 py-1 text-xs"
                    >
                      {STATUSES.map((s) => <option key={s} value={s}>{formatLabel(s)}</option>)}
                    </select>
                  </td>
                  <td className="px-4 py-3">
                    <select
                      value={issue.priority}
                      onChange={(e) => updateField(issue.id, 'priority', e.target.value)}
                      className="border border-slate-300 rounded-md px-2 py-1 text-xs"
                    >
                      {PRIORITIES.map((p) => <option key={p} value={p}>{formatLabel(p)}</option>)}
                    </select>
                  </td>
                  <td className="px-4 py-3 text-slate-500">{new Date(issue.createdAt).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
