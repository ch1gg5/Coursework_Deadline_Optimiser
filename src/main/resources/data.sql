-- =========================
-- MODULES
-- =========================
INSERT INTO academic_module (name, module_code, credits) VALUES
                                                             ('Computer Systems', 'CS101', 20),
                                                             ('Databases', 'CS102', 20),
                                                             ('Software Engineering', 'CS103', 20),
                                                             ('Artificial Intelligence', 'CS104', 20);

-- =========================
-- STUDENTS
-- =========================
INSERT INTO student (email, name) VALUES
                                   ('daniel@example.com', 'Daniel'),
                                   ('alex@example.com', 'Alex');

-- =========================
-- COURSEWORK
-- =========================
INSERT INTO coursework
(title, deadline, weighting, difficulty, estimated_hours, student_id, module_id)
VALUES
    ('Database Design CW', '2026-06-25', 40, 4, 20, 'daniel@example.com', 2),

    ('SQL Optimisation Report', '2026-06-30', 60, 5, 25, 'daniel@example.com', 2),

    ('AI Research Assignment', '2026-07-05', 50, 4, 18, 'daniel@example.com', 4),

    ('Software Engineering Group Report', '2026-07-10', 30, 3, 15, 'daniel@example.com', 3),

    ('Computer Systems Coursework', '2026-06-22', 35, 3, 12, 'alex@example.com', 1),

    ('Operating Systems Essay', '2026-06-28', 45, 4, 16, 'alex@example.com', 1),

    ('Software Engineering Presentation', '2026-07-03', 25, 2, 10, 'alex@example.com', 3),

    ('Machine Learning Mini Project', '2026-07-08', 70, 5, 30, 'alex@example.com', 4);