-- =========================
-- Users
-- =========================
INSERT INTO users (email, password, username, role) VALUES
                                      ('daniel@example.com', 'test', 'Daniel', 'ROLE_USER'),
                                      ('alex@example.com', 'test2', 'Alex', 'ROLE_USER');


-- =========================
-- STUDENTS
-- =========================
INSERT INTO student (email, name) VALUES
                                      ('daniel@example.com', 'Daniel'),
                                      ('alex@example.com', 'Alex');




-- =========================
-- MODULES
-- =========================
INSERT INTO academic_module (name, module_code, credits, user_id) VALUES
                                                             ('Computer Systems', 'CS101', 20, 1),
                                                             ('Databases', 'CS102', 20, 1),
                                                             ('Software Engineering', 'CS103', 20, 1),
                                                             ('Artificial Intelligence', 'CS104', 20, 1);



-- =========================
-- COURSEWORK
-- =========================
INSERT INTO coursework
(title, deadline, weighting, difficulty, estimated_hours, user_id, module_id)
VALUES
    ('Database Design CW', '2026-06-25', 40, 4, 20, 1, 2),

    ('SQL Optimisation Report', '2026-06-30', 60, 5, 25, 1, 2),

    ('AI Research Assignment', '2026-07-05', 50, 4, 18, 1, 4);

--     ('Software Engineering Group Report', '2026-07-10', 30, 3, 15, 'daniel@example.com', 3),
--
--     ('Computer Systems Coursework', '2026-06-22', 35, 3, 12, 'alex@example.com', 1),
--
--     ('Operating Systems Essay', '2026-06-28', 45, 4, 16, 'alex@example.com', 1),
--
--     ('Software Engineering Presentation', '2026-07-03', 25, 2, 10, 'alex@example.com', 3),
--
--     ('Machine Learning Mini Project', '2026-07-08', 70, 5, 30, 'alex@example.com', 4);