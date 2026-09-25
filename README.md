# CivicTrack — Neighborhood Issue Reporter

A civic-tech platform that closes the loop between citizens and local authorities on everyday neighborhood problems: potholes, broken streetlights, garbage buildup, and water leakage.

**The problem:** citizens have no easy way to report local issues with evidence, and no way to see what happens after they report — so most people stop bothering. Authorities have no structured, prioritized view of what's actually broken across a neighborhood.

**What this does:** citizens report an issue with a photo and a map pin → an admin verifies, prioritizes, and updates its status → the citizen sees the progress → an analytics dashboard shows the authority (and this demo's judges) where the real problems are.

## Features

- Citizen signup/login (JWT-based auth)
- Report an issue: title, description, category, photo, map-pinned location
- Public issue feed with category/status search and filtering, list or map view
- Upvote/confirm existing issues instead of duplicating reports
- Admin triage board: filter, update status (`Reported → Verified → In Progress → Resolved/Closed`) and priority
- Admin remarks visible to the reporting citizen
- Admin analytics dashboard: totals, open/resolved counts, average resolution time, breakdowns by category/status/priority

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.3, Spring Security (JWT), Spring Data JPA, Flyway, PostgreSQL
- **Frontend:** React 18, Vite, Tailwind CSS 4, React Router, Axios, Leaflet (OpenStreetMap), Recharts
- **Infra:** Docker Compose (Postgres + backend + frontend), Nginx for the built frontend

## Running It

### One command (Docker Compose)

```bash
docker compose up -d --build
```

- Frontend: http://localhost:8081
- Backend API: http://localhost:8080
- Postgres: localhost:55432 (mapped to avoid clashing with a local Postgres install)

The backend automatically runs Flyway migrations and seeds demo data on first start.

### Demo accounts

| Role    | Email                       | Password       |
|---------|------------------------------|----------------|
| Admin   | admin@civictrack.local       | Password123!   |
| Citizen | priya@civictrack.local       | Password123!   |
| Citizen | rahul@civictrack.local       | Password123!   |

### Running locally without Docker

**Backend** (requires a local PostgreSQL instance, database `issuetracker`):
```bash
cd backend
mvn spring-boot:run
```

**Frontend**:
```bash
cd frontend
npm install
npm run dev
```

## API Overview

| Method | Endpoint                       | Access        | Purpose                              |
|--------|---------------------------------|---------------|---------------------------------------|
| POST   | `/api/auth/register`           | Public        | Citizen signup                        |
| POST   | `/api/auth/login`               | Public        | Login, returns JWT                    |
| POST   | `/api/issues`                   | Authenticated | Report a new issue (multipart)        |
| GET    | `/api/issues`                   | Authenticated | List/filter/search issues             |
| GET    | `/api/issues/{id}`              | Authenticated | Issue detail                          |
| POST   | `/api/issues/{id}/upvote`       | Authenticated | Toggle upvote/confirmation            |
| PATCH  | `/api/issues/{id}/status`       | Admin only    | Update status/priority                |
| POST   | `/api/issues/{id}/remarks`      | Admin only    | Add a remark visible to the reporter  |
| GET    | `/api/dashboard/stats`          | Admin only    | Aggregated analytics                  |

## Notes

- Security: passwords are bcrypt-hashed, all state-changing routes require a valid JWT, role checks are enforced server-side (not just hidden in the UI), file uploads are restricted to JPEG/PNG/WEBP.
- The core application works fully offline/self-hosted — no external AI or third-party API dependency.
