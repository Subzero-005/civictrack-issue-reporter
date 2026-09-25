import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import { Link } from 'react-router-dom'
import './leafletIconFix'
import { formatLabel } from '../constants'

const DEFAULT_CENTER = [28.6139, 77.2090]

export default function MapView({ issues }) {
  const center =
    issues.length > 0 ? [issues[0].latitude, issues[0].longitude] : DEFAULT_CENTER

  return (
    <div className="rounded-lg overflow-hidden border border-slate-300">
      <MapContainer center={center} zoom={13} style={{ height: '420px', width: '100%' }}>
        <TileLayer
          attribution='&copy; OpenStreetMap contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {issues.map((issue) => (
          <Marker key={issue.id} position={[issue.latitude, issue.longitude]}>
            <Popup>
              <div className="text-sm">
                <p className="font-semibold">{issue.title}</p>
                <p className="text-slate-500">{formatLabel(issue.category)} &middot; {formatLabel(issue.status)}</p>
                <Link to={`/issues/${issue.id}`} className="text-blue-600 underline">
                  View details
                </Link>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  )
}
