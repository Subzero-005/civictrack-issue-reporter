export const CATEGORIES = ['POTHOLE', 'STREETLIGHT', 'WASTE', 'WATER_LEAKAGE', 'OTHER']

export const STATUSES = ['REPORTED', 'VERIFIED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED']

export const PRIORITIES = ['LOW', 'MEDIUM', 'HIGH', 'URGENT']

export const CATEGORY_LABELS = {
  POTHOLE: 'Pothole',
  STREETLIGHT: 'Streetlight',
  WASTE: 'Waste / Garbage',
  WATER_LEAKAGE: 'Water Leakage',
  OTHER: 'Other',
}

export const STATUS_STYLES = {
  REPORTED: 'bg-slate-200 text-slate-800',
  VERIFIED: 'bg-blue-100 text-blue-800',
  IN_PROGRESS: 'bg-amber-100 text-amber-800',
  RESOLVED: 'bg-emerald-100 text-emerald-800',
  CLOSED: 'bg-gray-300 text-gray-700',
}

export const PRIORITY_STYLES = {
  LOW: 'bg-slate-100 text-slate-600',
  MEDIUM: 'bg-blue-50 text-blue-700',
  HIGH: 'bg-orange-100 text-orange-800',
  URGENT: 'bg-red-100 text-red-800',
}

export function formatLabel(value) {
  if (!value) return ''
  return value
    .toLowerCase()
    .split('_')
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ')
}
