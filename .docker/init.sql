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
