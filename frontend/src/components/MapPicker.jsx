import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet'
import './leafletIconFix'

const DEFAULT_CENTER = [28.6139, 77.2090] // fallback: New Delhi

function ClickHandler({ onPick }) {
  useMapEvents({
    click(e) {
      onPick(e.latlng.lat, e.latlng.lng)
    },
  })
  return null
}

export default function MapPicker({ lat, lng, onPick }) {
  const center = lat && lng ? [lat, lng] : DEFAULT_CENTER

  return (
    <div className="rounded-lg overflow-hidden border border-slate-300">
      <MapContainer center={center} zoom={15} style={{ height: '280px', width: '100%' }}>
        <TileLayer
          attribution='&copy; OpenStreetMap contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <ClickHandler onPick={onPick} />
        {lat && lng && <Marker position={[lat, lng]} />}
      </MapContainer>
      <p className="text-xs text-slate-500 px-2 py-1.5 bg-slate-50">
        Click on the map to drop a pin at the issue location.
      </p>
    </div>
  )
}
