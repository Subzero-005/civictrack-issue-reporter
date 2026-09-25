import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import client from '../api/client'
import Badge from '../components/Badge'
import { useAuth } from '../context/AuthContext'
import { STATUS_STYLES, PRIORITY_STYLES, STATUSES, PRIORITIES, formatLabel } from '../constants'

const API_BASE = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export default function IssueDetailPage() {
  const { id } = useParams()
  const { isAdmin } = useAuth()
  const [issue, setIssue] = useState(null)
  const [loading, setLoading] = useState(true)
  const [note, setNote] = useState('')
  const [savingNote, setSavingNote] = useState(false)

  const load = () => {
    setLoading(true)
    client.get(`/api/issues/${id}`).then((res) => setIssue(res.data)).finally(() => setLoading(false))
  }

  useEffect(load, [id])

  const handleUpvote = async () => {
    const { data } = await client.post(`/api/issues/${id}/upvote`)
    setIssue(data)
  }

  const handleStatusChange = async (field, value) => {
    const { data } = await client.patch(`/api/issues/${id}/status`, { [field]: value })
    setIssue(data)
  }

  const handleAddNote = async (e) => {
    e.preventDefault()
    if (!note.trim()) return
    setSavingNote(true)
    try {
      const { data } = await client.post(`/api/issues/${id}/remarks`, { note })
      setIssue(data)
      setNote('')
    } finally {
      setSavingNote(false)
    }
  }

  if (loading) return <p className="text-center text-slate-500 py-10">Loading…</p>
  if (!issue) return <p className="text-center text-slate-500 py-10">Issue not found.</p>

  return (
    <div className="max-w-3xl mx-auto px-4 sm:px-6 py-6">
      <div className="bg-white rounded-xl border border-slate-200 overflow-hidden">
        {issue.photoUrl && (
          <img src={`${API_BASE}${issue.photoUrl}`} alt={issue.title} className="w-full max-h-80 object-cover" />
        )}
        <div className="p-6">
          <div className="flex items-center gap-2 mb-3">
            <Badge value={issue.status} styles={STATUS_STYLES} />
            <Badge value={issue.priority} styles={PRIORITY_STYLES} />
          </div>
          <h1 className="text-2xl font-semibold text-slate-900 mb-2">{issue.title}</h1>
          <p className="text-slate-600 mb-4">{issue.description}</p>

          <div className="text-sm text-slate-500 space-y-1 mb-4">
            <p>Category: {formatLabel(issue.category)}</p>
            {issue.address && <p>Location: {issue.address}</p>}
            <p>Reported by {issue.reporterName}</p>
            <p>Reported on {new Date(issue.createdAt).toLocaleString()}</p>
            {issue.resolvedAt && <p>Resolved on {new Date(issue.resolvedAt).toLocaleString()}</p>}
          </div>

          <button
            onClick={handleUpvote}
            className={`px-4 py-2 rounded-md text-sm font-medium border ${
              issue.upvotedByMe
                ? 'bg-slate-900 text-white border-slate-900'
                : 'bg-white text-slate-700 border-slate-300 hover:bg-slate-50'
            }`}
          >
            &#9650; {issue.upvotedByMe ? 'Upvoted' : 'Upvote'} ({issue.upvoteCount})
          </button>

          {isAdmin && (
            <div className="mt-6 pt-6 border-t border-slate-200 space-y-4">
              <h2 className="font-medium text-slate-900">Admin Controls</h2>
              <div className="flex flex-wrap gap-4">
                <div>
                  <label className="block text-xs text-slate-500 mb-1">Status</label>
                  <select
                    value={issue.status}
                    onChange={(e) => handleStatusChange('status', e.target.value)}
                    className="border border-slate-300 rounded-md px-3 py-2 text-sm"
                  >
                    {STATUSES.map((s) => (
                      <option key={s} value={s}>{formatLabel(s)}</option>
                    ))}
                  </select>
                </div>
                <div>
                  <label className="block text-xs text-slate-500 mb-1">Priority</label>
                  <select
                    value={issue.priority}
                    onChange={(e) => handleStatusChange('priority', e.target.value)}
                    className="border border-slate-300 rounded-md px-3 py-2 text-sm"
                  >
                    {PRIORITIES.map((p) => (
                      <option key={p} value={p}>{formatLabel(p)}</option>
                    ))}
                  </select>
                </div>
              </div>

              <form onSubmit={handleAddNote} className="flex gap-2">
                <input
                  value={note}
                  onChange={(e) => setNote(e.target.value)}
                  placeholder="Add a remark for the reporter…"
                  className="flex-1 border border-slate-300 rounded-md px-3 py-2 text-sm"
                />
                <button
                  type="submit"
                  disabled={savingNote}
                  className="px-4 py-2 bg-slate-900 text-white rounded-md text-sm disabled:opacity-60"
                >
                  Add
                </button>
              </form>
            </div>
          )}

          <div className="mt-6 pt-6 border-t border-slate-200">
            <h2 className="font-medium text-slate-900 mb-3">Status Updates</h2>
            {issue.remarks.length === 0 ? (
              <p className="text-sm text-slate-400">No updates yet.</p>
            ) : (
              <ul className="space-y-3">
                {issue.remarks.map((r) => (
                  <li key={r.id} className="text-sm">
                    <p className="text-slate-700">{r.note}</p>
                    <p className="text-xs text-slate-400">
                      {r.adminName} &middot; {new Date(r.createdAt).toLocaleString()}
                    </p>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
