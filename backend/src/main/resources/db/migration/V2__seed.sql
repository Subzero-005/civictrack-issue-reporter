-- Demo seed data. Password for every account is: Password123!
-- Hash below is a BCrypt hash of that string, generated once and stored fixed for reproducible demo data.
INSERT INTO users (name, email, password_hash, role) VALUES
    ('Admin User', 'admin@civictrack.local', '$2y$10$WWWpGlghZRuWwZgE5UPvIepvow2L/I5nhUEMQK.zLG01Uo1wGmc6a', 'ADMIN'),
    ('Priya Sharma', 'priya@civictrack.local', '$2y$10$WWWpGlghZRuWwZgE5UPvIepvow2L/I5nhUEMQK.zLG01Uo1wGmc6a', 'CITIZEN'),
    ('Rahul Verma', 'rahul@civictrack.local', '$2y$10$WWWpGlghZRuWwZgE5UPvIepvow2L/I5nhUEMQK.zLG01Uo1wGmc6a', 'CITIZEN');

INSERT INTO issues (reporter_id, title, description, category, status, priority, latitude, longitude, address, created_at, updated_at) VALUES
    (2, 'Large pothole near main gate', 'A deep pothole has formed near the market main gate, causing two-wheelers to swerve dangerously.', 'POTHOLE', 'IN_PROGRESS', 'HIGH', 28.6139, 77.2090, 'Main Market Road', now() - interval '4 days', now() - interval '1 day'),
    (2, 'Streetlight not working', 'The streetlight outside block C has been off for over a week, making the lane unsafe at night.', 'STREETLIGHT', 'VERIFIED', 'MEDIUM', 28.6145, 77.2100, 'Block C Lane 2', now() - interval '2 days', now() - interval '2 days'),
    (3, 'Garbage not collected for 5 days', 'Waste bins near the community park are overflowing and attracting stray animals.', 'WASTE', 'REPORTED', 'HIGH', 28.6120, 77.2075, 'Community Park Entrance', now() - interval '1 day', now() - interval '1 day'),
    (3, 'Water pipe leakage', 'Continuous water leakage from an underground pipe is flooding the footpath.', 'WATER_LEAKAGE', 'RESOLVED', 'URGENT', 28.6150, 77.2060, 'Sector 5 Footpath', now() - interval '10 days', now() - interval '6 days');

UPDATE issues SET resolved_at = updated_at WHERE status = 'RESOLVED';
