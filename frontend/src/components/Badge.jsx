import { formatLabel } from '../constants'

export default function Badge({ value, styles }) {
  const className = styles[value] || 'bg-slate-100 text-slate-700'
  return (
    <span className={`inline-block px-2.5 py-1 rounded-full text-xs font-medium ${className}`}>
      {formatLabel(value)}
    </span>
  )
}
