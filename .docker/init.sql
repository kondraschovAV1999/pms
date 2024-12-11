CREATE DATABASE IF NOT EXISTS prison;

USE prison;

CREATE TABLE IF NOT EXISTS prisoner (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(255) NOT NULL,
    lname VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    id_number VARCHAR(255) NOT NULL,
    id_type VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    marriage ENUM('SINGLE', 'DIVORCED', 'MARRIED', 'WIDOWED'),
    d_level ENUM('A', 'B', 'C', 'D', 'E') NOT NULL,
    crime_type VARCHAR(255) NOT NULL,
    status ENUM('IMPRISONED', 'RELEASED'),
    ed_level VARCHAR(255),
    term VARCHAR(255) NOT NULL,
    staff_id INT,
    work_id INT,
    rel_date DATE NOT NULL,
    dept_id INT
);

CREATE TABLE IF NOT EXISTS staff (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fname VARCHAR(255) NOT NULL,
    lname VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    id_number VARCHAR(255) NOT NULL,
    id_type VARCHAR(255) NOT NULL,
    gender ENUM('M', 'F'),
    level ENUM('NORMAL_STAFF', 'MASTER', 'VICE_MASTER'),
    master_id INT,
    dept_id INT
);

CREATE TABLE IF NOT EXISTS department (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS duty (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    is_day_time BOOL NOT NULL,
    date DATE NOT NULL,
    dept_id INT
);

CREATE TABLE IF NOT EXISTS assessment (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    score INT NOT NULL,
    staff_id INT,
    date DATE NOT NULL,
    CHECK (score > 0 AND score <= 100)
);

CREATE TABLE IF NOT EXISTS assignment (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    dept_id INT,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS work (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    dept_id INT
);

CREATE TABLE IF NOT EXISTS permission (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    permission VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS degree (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    field VARCHAR(255),
    level VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS course (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    teacher VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS communication (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    prisoner_id INT,
    timestamp TIMESTAMP(6) NOT NULL,
    transcript TEXT NOT NULL,
    relation VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS prisoner_degree (
    prisoner_id INT,
    degree_id INT,
    status ENUM('IN_PROGRESS', 'FINISHED', 'SUSPENDED'),
    date DATE,
    PRIMARY KEY(prisoner_id, degree_id)
);

CREATE TABLE IF NOT EXISTS enrollment (
    prisoner_id INT,
    course_id INT,
    grade FLOAT(2),
    status ENUM('IN_PROGRESS', 'FINISHED', 'SUSPENDED'),
    date DATE,
    PRIMARY KEY(prisoner_id, course_id),
    CHECK (grade > 0 AND grade <= 4)
);

CREATE TABLE IF NOT EXISTS degree_course (
    degree_id INT,
    course_id INT,
    PRIMARY KEY(degree_id, course_id)
);

CREATE TABLE IF NOT EXISTS staff_duties (
    staff_id INT,
    duty_id INT,
    PRIMARY KEY(staff_id, duty_id) 
);

CREATE TABLE IF NOT EXISTS staff_assignments (
    staff_id INT,
    assignment_id INT, 
    PRIMARY KEY(staff_id, assignment_id)
);

CREATE TABLE IF NOT EXISTS assignment_permissions (
    assignment_id INT,
    permission_id INT,
    PRIMARY KEY(assignment_id, permission_id)
);

CREATE TABLE IF NOT EXISTS dept_permissions (
    dept_id INT,
    permission_id INT,
    PRIMARY KEY(dept_id, permission_id)
);

/* Add FK constraints to the tables */

ALTER TABLE staff 
ADD CONSTRAINT fk_master 
FOREIGN KEY (master_id) REFERENCES staff(id);

ALTER TABLE staff
ADD CONSTRAINT fk_staff_dept
FOREIGN KEY (dept_id) REFERENCES department(id) ON DELETE CASCADE;

ALTER TABLE assignment 
ADD CONSTRAINT fk_assignment_dept 
FOREIGN KEY (dept_id) REFERENCES department(id);

ALTER TABLE work
ADD CONSTRAINT fk_work_dept
FOREIGN KEY (dept_id) REFERENCES department(id);

ALTER TABLE communication
ADD CONSTRAINT fk_communication_prisoner
FOREIGN KEY (prisoner_id) REFERENCES prisoner(id);

ALTER TABLE staff_duties 
ADD CONSTRAINT fk_staff_duties_staff
FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE;

ALTER TABLE staff_duties 
ADD CONSTRAINT fk_staff_duties_duty
FOREIGN KEY (duty_id) REFERENCES duty(id);

ALTER TABLE duty 
ADD CONSTRAINT fk_duty_dept
FOREIGN KEY (dept_id) REFERENCES department(id) ON DELETE CASCADE;

ALTER TABLE assessment 
ADD CONSTRAINT fk_assessment_staff
FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE;

ALTER TABLE staff_assignments 
ADD CONSTRAINT fk_staff_assignments_staff
FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE;

ALTER TABLE staff_assignments 
ADD CONSTRAINT fk_staff_assignments_assignment
FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE CASCADE;

ALTER TABLE assignment_permissions 
ADD CONSTRAINT fk_assignment_permissions_assignment
FOREIGN KEY (assignment_id) REFERENCES assignment(id) ON DELETE CASCADE;

ALTER TABLE assignment_permissions 
ADD CONSTRAINT fk_assignment_permissions_permission
FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE;

ALTER TABLE dept_permissions 
ADD CONSTRAINT fk_dept_permissions_dept
FOREIGN KEY (dept_id) REFERENCES department(id) ON DELETE CASCADE;

ALTER TABLE dept_permissions
ADD CONSTRAINT fk_dept_permissions_permission
FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE;

ALTER TABLE degree_course
ADD CONSTRAINT fk_degree_course_degree
FOREIGN KEY (degree_id) REFERENCES degree(id) ON DELETE CASCADE;

ALTER TABLE degree_course
ADD CONSTRAINT fk_degree_course_course
FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE;

ALTER TABLE prisoner_degree
ADD CONSTRAINT fk_prisoner_degree_degree
FOREIGN KEY (degree_id) REFERENCES degree(id) ON DELETE CASCADE;

ALTER TABLE prisoner_degree
ADD CONSTRAINT fk_prisoner_degree_prisoner
FOREIGN KEY (prisoner_id) REFERENCES prisoner(id) ON DELETE CASCADE;

ALTER TABLE enrollment
ADD CONSTRAINT fk_enrollment_course
FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE;

ALTER TABLE enrollment
ADD CONSTRAINT fk_enrollment_prisoner
FOREIGN KEY (prisoner_id) REFERENCES prisoner(id) ON DELETE CASCADE;

ALTER TABLE prisoner
ADD CONSTRAINT fk_prisoner_work
FOREIGN KEY (work_id) REFERENCES work(id);

ALTER TABLE prisoner
ADD CONSTRAINT fk_prisoner_staff
FOREIGN KEY (staff_id) REFERENCES staff(id);

ALTER TABLE prisoner
ADD CONSTRAINT fk_prisoner_dep
FOREIGN KEY (dept_id) REFERENCES department(id) ON DELETE CASCADE;

CREATE USER 'app_dev'@'%' IDENTIFIED BY 'dev';
GRANT SELECT, INSERT, UPDATE, DELETE ON prison.* TO 'app_dev'@'%';

FLUSH PRIVILEGES;

USE prison;
-- Insert sample data into department
INSERT INTO department (id, name)
VALUES (1, 'Human Resources'),
       (2, 'Management and Surveillance'),
       (3, 'Education and Rehabilitation'),
       (4, 'Production'),
       (5, 'Central Management'),
       (6, 'Prison District 1'),
       (7, 'Prison District 2'),
       (8, 'Prison District 3'),
       (9, 'Prison District 4'),
       (10, 'Prison District 5'),
       (11, 'Prison District 6'),
       (12, 'Prison District 7'),
       (13, 'Prison District 8'),
       (14, 'Prison District 9'),
       (15, 'Prison District 10'),
       (16, 'Prison District 11'),
       (17, 'Prison District 12'),
       (18, 'Prison District 13'),
       (19, 'Prison District 14'),
       (20, 'Prison District 15'),
       (21, 'Prison District 16'),
       (22, 'Prison District 17'),
       (23, 'Prison District 18'),
       (24, 'Prison District 19'),
       (25, 'Prison District 20');

-- Insert sample data into staff
INSERT INTO staff (id, fname, lname, dob, id_number, id_type, gender, level, master_id, dept_id)
VALUES
-- Human Resources Staff
(1, 'Alice', 'Smith', '1985-04-15', 'HR001', 'Passport', 'F', 'MASTER', NULL, 1),
(2, 'Bob', 'Johnson', '1988-08-20', 'HR002', 'ID Card', 'M', 'NORMAL_STAFF', 1, 1),
(3, 'Charlie', 'Brown', '1990-05-25', 'HR003', 'ID Card', 'M', 'NORMAL_STAFF', 1, 1),

-- Management and Surveillance Staff
(4, 'Diana', 'Prince', '1982-03-12', 'MS001', 'Passport', 'F', 'MASTER', NULL, 2),
(5, 'Ethan', 'Hunt', '1987-07-18', 'MS002', 'ID Card', 'M', 'VICE_MASTER', 4, 2),
(6, 'Fiona', 'Mills', '1992-11-30', 'MS003', 'ID Card', 'F', 'NORMAL_STAFF', 5, 2),

-- Education and Rehabilitation Staff
(7, 'Grace', 'Harper', '1984-09-10', 'ER001', 'Passport', 'F', 'MASTER', NULL, 3),
(8, 'Harry', 'Potter', '1995-06-22', 'ER002', 'ID Card', 'M', 'NORMAL_STAFF', 7, 3),
(9, 'Irene', 'Adler', '1991-02-14', 'ER003', 'ID Card', 'F', 'NORMAL_STAFF', 7, 3),

-- Production Staff
(10, 'Jack', 'Bauer', '1986-10-05', 'PR001', 'Passport', 'M', 'MASTER', NULL, 4),
(11, 'Karen', 'Walker', '1993-12-08', 'PR002', 'ID Card', 'F', 'NORMAL_STAFF', 10, 4),

-- Central Management Staff
(12, 'Leo', 'King', '1980-01-28', 'CM001', 'Passport', 'M', 'MASTER', NULL, 5),
(13, 'Mona', 'Lisa', '1989-09-15', 'CM002', 'ID Card', 'F', 'NORMAL_STAFF', 12, 5),

-- Prison District Staff
(14, 'Nancy', 'Drew', '1990-04-21', 'PD001', 'Passport', 'F', 'MASTER', NULL, 6),
(15, 'Oscar', 'Wilde', '1988-05-17', 'PD002', 'ID Card', 'M', 'VICE_MASTER', 14, 6),
(16, 'Paul', 'Revere', '1992-03-31', 'PD003', 'ID Card', 'M', 'NORMAL_STAFF', 14, 6);

INSERT INTO prisoner (fname, lname, dob, id_number, id_type, address, marriage, d_level, crime_type, status,
                      ed_level, term, staff_id, work_id, rel_date, dept_id)
VALUES ('Alex', 'Brown', '1995-08-12', 'P101', 'ID Card', '12 Elm Street', 'MARRIED', 'B', 'Theft', 'IMPRISONED',
        'High School', '3 years', 1, NULL, '2027-08-12', 6),
       ('Emma', 'Davis', '1987-02-20', 'P102', 'Passport', '34 Maple Road', 'DIVORCED', 'C', 'Fraud', 'IMPRISONED',
        'College', '5 years', 2, NULL, '2029-02-20', 6),
       ('Michael', 'Garcia', '1990-04-15', 'P103', 'ID Card', '56 Oak Drive', 'SINGLE', 'A', 'Assault', 'IMPRISONED',
        'High School', '7 years', 3, NULL, '2031-04-15', 6),
       ('Sophia', 'Martinez', '1993-07-30', 'P104', 'ID Card', '78 Pine Avenue', 'WIDOWED', 'B', 'Drug Possession',
        'IMPRISONED', 'Bachelor', '4 years', 4, NULL, '2028-07-30', 6),
       ('James', 'Wilson', '1985-11-10', 'P105', 'Passport', '90 Birch Lane', 'MARRIED', 'C', 'Robbery', 'IMPRISONED',
        'High School', '6 years', 5, NULL, '2030-11-10', 7),
       ('Isabella', 'Clark', '1997-03-22', 'P106', 'ID Card', '11 Walnut Drive', 'SINGLE', 'B', 'Arson', 'IMPRISONED',
        'High School', '5 years', 6, NULL, '2029-03-22', 7),
       ('Daniel', 'Lee', '1992-01-18', 'P107', 'ID Card', '22 Cedar Street', 'MARRIED', 'A', 'Burglary', 'IMPRISONED',
        'College', '10 years', 7, NULL, '2034-01-18', 8),
       ('Mia', 'Perez', '1991-12-05', 'P108', 'ID Card', '33 Willow Road', 'DIVORCED', 'D', 'Kidnapping', 'IMPRISONED',
        'Bachelor', '8 years', 8, NULL, '2032-12-05', 8),
       ('Ethan', 'Young', '1986-09-14', 'P109', 'ID Card', '44 Aspen Lane', 'WIDOWED', 'E', 'Murder', 'IMPRISONED',
        'None', '15 years', 9, NULL, '2039-09-14', 9),
       ('Olivia', 'King', '1989-05-25', 'P110', 'Passport', '55 Spruce Avenue', 'SINGLE', 'C', 'Fraud', 'IMPRISONED',
        'High School', '4 years', 10, NULL, '2028-05-25', 9),
       ('Liam', 'Scott', '1994-06-08', 'P111', 'ID Card', '66 Cypress Road', 'MARRIED', 'A', 'Theft', 'IMPRISONED',
        'High School', '3 years', 11, NULL, '2027-06-08', 10),
       ('Emily', 'Adams', '1996-08-17', 'P112', 'ID Card', '77 Palm Street', 'DIVORCED', 'B', 'Fraud', 'IMPRISONED',
        'College', '5 years', 12, NULL, '2031-08-17', 10),
       ('Noah', 'Baker', '1988-10-25', 'P113', 'ID Card', '88 Ash Avenue', 'SINGLE', 'C', 'Drug Trafficking',
        'IMPRISONED', 'High School', '10 years', 13, NULL, '2036-10-25', 11),
       ('Chloe', 'Evans', '1991-11-03', 'P114', 'Passport', '99 Chestnut Lane', 'MARRIED', 'D', 'Manslaughter',
        'IMPRISONED', 'Bachelor', '8 years', 14, NULL, '2034-11-03', 11),
       ('Ryan', 'Mitchell', '1990-02-12', 'P115', 'ID Card', '10 Hawthorn Street', 'SINGLE', 'E', 'Terrorism',
        'IMPRISONED', 'None', '25 years', 15, NULL, '2049-02-12', 12),
       ('Grace', 'Reed', '1987-07-23', 'P116', 'ID Card', '20 Sycamore Drive', 'DIVORCED', 'B', 'Robbery', 'IMPRISONED',
        'High School', '6 years', 16, NULL, '2033-07-23', 12);


-- Insert sample data into work
INSERT INTO work (type, dept_id)
VALUES ('Kitchen Duty', 3),
       ('Library Assistant', 2),
       ('Janitorial Services', 3),
       ('Health Aid', 5),
       ('Groundskeeping', 1),
       ('Laundering', 3),
       ('Food Preparation', 1),
       ('IT Support', 2),
       ('Library Management', 4),
       ('Outdoor Landscaping', 5),
       ('Warehouse Logistics', 2),
       ('Health Monitoring', 5),
       ('Rehabilitation Counseling', 4),
       ('Gym Maintenance', 3),
       ('Vehicle Maintenance', 1);

-- Insert sample data into assessment
INSERT INTO assessment (score, staff_id, date)
VALUES (95, 1, '2024-11-15'),
       (87, 2, '2024-11-16'),
       (92, 3, '2024-11-17'),
       (88, 4, '2024-11-18'),
       (90, 5, '2024-11-19'),
       (98, 6, '2024-11-20'),
       (75, 7, '2024-11-21'),
       (88, 8, '2024-11-22'),
       (91, 9, '2024-11-23'),
       (84, 10, '2024-11-24'),
       (92, 11, '2024-11-25'),
       (89, 12, '2024-11-26'),
       (94, 13, '2024-11-27'),
       (96, 14, '2024-11-28'),
       (78, 15, '2024-11-29');

-- Insert sample data into duty
INSERT INTO duty (id, is_day_time, date, dept_id)
VALUES
-- Human Resources Duties
(1, TRUE, '2024-12-01', 1),
(2, FALSE, '2024-12-02', 1),

-- Management and Surveillance Duties
(3, TRUE, '2024-12-01', 2),
(4, FALSE, '2024-12-02', 2),

-- Education and Rehabilitation Duties
(5, TRUE, '2024-12-01', 3),
(6, FALSE, '2024-12-02', 3),

-- Production Duties
(7, TRUE, '2024-12-01', 4),
(8, FALSE, '2024-12-02', 4),

-- Central Management Duties
(9, TRUE, '2024-12-01', 5),
(10, FALSE, '2024-12-02', 5),

-- Prison District 1 Duties
(11, TRUE, '2024-12-01', 6),
(12, FALSE, '2024-12-02', 6);


INSERT INTO staff_duties (staff_id, duty_id)
VALUES
-- Assigning Human Resources staff to duties
(1, 1),
(2, 2),
(3, 1),

-- Assigning Management and Surveillance staff to duties
(4, 3),
(5, 4),
(6, 3),

-- Assigning Education and Rehabilitation staff to duties
(7, 5),
(8, 6),
(9, 5),

-- Assigning Production staff to duties
(10, 7),
(11, 8),

-- Assigning Central Management staff to duties
(12, 9),
(13, 10),

-- Assigning Prison District staff to duties
(14, 11),
(15, 12),
(16, 11);

-- Insert sample data into assignment
INSERT INTO assignment (dept_id, description)
VALUES (1, 'Security Monitoring'),
       (2, 'Teaching Prisoner Literacy'),
       (3, 'Facility Maintenance'),
       (4, 'Administrative Record Keeping'),
       (5, 'Medical Assistance');

-- Insert sample data into degree
INSERT INTO degree (name, field, level)
VALUES ('Engineering', 'Electrical', 'Bachelors'),
       ('Medicine', 'Health Sciences', 'Masters'),
       ('Computer Science', 'Technology', 'Bachelors'),
       ('Law', 'Legal Studies', 'Doctorate'),
       ('Business Administration', 'Management', 'MBA');

-- Insert sample data into course
INSERT INTO course (name, teacher)
VALUES ('Introduction to Law', 'Prof. Alan White'),
       ('Data Structures', 'Dr. Sarah King'),
       ('Anatomy 101', 'Dr. Rebecca Green'),
       ('Project Management', 'Mr. Michael Johnson'),
       ('Electrical Circuits', 'Dr. Laura Adams');

-- Insert sample data into communication
INSERT INTO communication (prisoner_id, timestamp, transcript, relation, name)
VALUES (1, '2024-12-01 10:30:00', 'Hey Alex, how are you?', 'Brother', 'Daniel Johnson'),
       (2, '2024-12-02 12:00:00', 'Stay strong Sophia!', 'Mother', 'Maria Martinez'),
       (3, '2024-12-03 15:45:00', 'I miss you Liam.', 'Daughter', 'Olivia Williams'),
       (4, '2024-12-04 09:20:00', 'Keep your head up Emma.', 'Father', 'Robert Davis'),
       (5, '2024-12-05 13:10:00', 'Noah, we believe in you.', 'Friend', 'Lucas Garcia');


INSERT INTO permission (permission)
VALUES ('VIEW_PRISONER_RECORDS'),
       ('EDIT_PRISONER_RECORDS'),
       ('DELETE_PRISONER_RECORDS'),
       ('VIEW_STAFF_RECORDS'),
       ('EDIT_STAFF_RECORDS'),
       ('ASSIGN_DUTIES'),
       ('MANAGE_DEPARTMENTS'),
       ('ACCESS_CONFIDENTIAL'),
       ('SCHEDULE_ASSESSMENTS'),
       ('VIEW_REPORTS');

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (1, 1), -- VIEW_PRISONER_RECORDS
       (1, 4), -- VIEW_STAFF_RECORDS
       (1, 9), -- VIEW_DUTY_LIST
       (1, 6); -- MANAGE_JOBS

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (2, 1), -- VIEW_PRISONER_RECORDS
       (2, 9), -- VIEW_DUTY_LIST
       (2, 3), -- EDIT_PRISONER_RECORDS
       (2, 8), -- ACCESS_COMMUNICATIONS
       (2, 6); -- MANAGE_LABOR

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (3, 1), -- VIEW_PRISONER_RECORDS
       (3, 6), -- MANAGE_COURSE_REGISTRATION
       (3, 7); -- EDIT_COURSE_INFORMATION

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (4, 6), -- MANAGE_LABOR
       (4, 9); -- VIEW_DUTY_LIST

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (6, 1), -- VIEW_PRISONER_RECORDS
       (6, 9), -- VIEW_DUTY_LIST
       (6, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (7, 1), -- VIEW_PRISONER_RECORDS
       (7, 9), -- VIEW_DUTY_LIST
       (7, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (8, 1), -- VIEW_PRISONER_RECORDS
       (8, 9), -- VIEW_DUTY_LIST
       (8, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (9, 1), -- VIEW_PRISONER_RECORDS
       (9, 9), -- VIEW_DUTY_LIST
       (9, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (10, 1), -- VIEW_PRISONER_RECORDS
       (10, 9), -- VIEW_DUTY_LIST
       (10, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (11, 1), -- VIEW_PRISONER_RECORDS
       (11, 9), -- VIEW_DUTY_LIST
       (11, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (12, 1), -- VIEW_PRISONER_RECORDS
       (12, 9), -- VIEW_DUTY_LIST
       (12, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (13, 1), -- VIEW_PRISONER_RECORDS
       (13, 9), -- VIEW_DUTY_LIST
       (13, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (14, 1), -- VIEW_PRISONER_RECORDS
       (14, 9), -- VIEW_DUTY_LIST
       (14, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (15, 1), -- VIEW_PRISONER_RECORDS
       (15, 9), -- VIEW_DUTY_LIST
       (15, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (16, 1), -- VIEW_PRISONER_RECORDS
       (16, 9), -- VIEW_DUTY_LIST
       (16, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (17, 1), -- VIEW_PRISONER_RECORDS
       (17, 9), -- VIEW_DUTY_LIST
       (17, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (18, 1), -- VIEW_PRISONER_RECORDS
       (18, 9), -- VIEW_DUTY_LIST
       (18, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (19, 1), -- VIEW_PRISONER_RECORDS
       (19, 9), -- VIEW_DUTY_LIST
       (19, 2); -- UPDATE_RESPONSIBLE_STAFF

INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (20, 1), -- VIEW_PRISONER_RECORDS
       (20, 9), -- VIEW_DUTY_LIST
       (20, 2); -- UPDATE_RESPONSIBLE_STAFF


INSERT INTO dept_permissions (dept_id, permission_id)
VALUES (5, 1), -- VIEW_PRISONER_RECORDS
       (5, 2), -- EDIT_PRISONER_RECORDS
       (5, 4), -- MANAGE_STAFF
       (5, 5), -- CREATE_ASSIGNMENTS
       (5, 6), -- DELETE_ASSIGNMENTS
       (5, 7), -- EDIT_ASSIGNMENTS
       (5, 8); -- ACCESS_CONFIDENTIAL
