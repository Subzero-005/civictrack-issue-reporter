import { Link } from 'react-router-dom'
import Badge from './Badge'
import { STATUS_STYLES, PRIORITY_STYLES, formatLabel } from '../constants'

export default function IssueCard({ issue }) {
  return (
    <Link
      to={`/issues/${issue.id}`}
      className="block bg-white rounded-lg border border-slate-200 hover:shadow-md transition-shadow overflow-hidden"
    >
      {issue.photoUrl && (
        <img
          src={`${import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'}${issue.photoUrl}`}
          alt={issue.title}
          className="w-full h-40 object-cover"
        />
      )}
      <div className="p-4">
        <div className="flex items-center gap-2 mb-2">
          <Badge value={issue.status} styles={STATUS_STYLES} />
          <Badge value={issue.priority} styles={PRIORITY_STYLES} />
        </div>
        <h3 className="font-semibold text-slate-900 mb-1">{issue.title}</h3>
        <p className="text-sm text-slate-500 mb-2 line-clamp-2">{issue.description}</p>
        <div className="flex items-center justify-between text-xs text-slate-400">
          <span>{formatLabel(issue.category)}</span>
          <span>&#9650; {issue.upvoteCount}</span>
        </div>
      </div>
    </Link>
  )
}
