-- Add sample departments for the employee management demo.
INSERT INTO departments (name) VALUES
    ('Engineering'),
    ('Human Resources'),
    ('Finance'),
    ('Marketing'),
    ('Operations')
ON CONFLICT (name) DO NOTHING;

-- Add sample employees and resolve department IDs by name instead of relying on sequence values.
INSERT INTO employees (first_name, last_name, email, phone, department_id, joining_date)
SELECT sample.first_name,
       sample.last_name,
       sample.email,
       sample.phone,
       departments.id,
       sample.joining_date
FROM (VALUES
    ('Ava', 'Patel', 'ava.patel@example.com', '+1-555-0101', 'Engineering', DATE '2022-03-14'),
    ('Noah', 'Sharma', 'noah.sharma@example.com', '+1-555-0102', 'Engineering', DATE '2021-08-02'),
    ('Mia', 'Chen', 'mia.chen@example.com', '+1-555-0103', 'Human Resources', DATE '2023-01-09'),
    ('Liam', 'Singh', 'liam.singh@example.com', '+1-555-0104', 'Human Resources', DATE '2020-11-16'),
    ('Isla', 'Garcia', 'isla.garcia@example.com', '+1-555-0105', 'Finance', DATE '2022-06-20'),
    ('Ethan', 'Kim', 'ethan.kim@example.com', '+1-555-0106', 'Finance', DATE '2024-02-12'),
    ('Zoe', 'Brown', 'zoe.brown@example.com', '+1-555-0107', 'Marketing', DATE '2021-04-05'),
    ('Lucas', 'Wilson', 'lucas.wilson@example.com', '+1-555-0108', 'Marketing', DATE '2023-09-18'),
    ('Aria', 'Davis', 'aria.davis@example.com', '+1-555-0109', 'Operations', DATE '2020-07-27'),
    ('Oliver', 'Martin', 'oliver.martin@example.com', '+1-555-0110', 'Operations', DATE '2022-12-01')
) AS sample(first_name, last_name, email, phone, department_name, joining_date)
JOIN departments ON departments.name = sample.department_name;
