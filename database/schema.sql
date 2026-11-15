CREATE DATABASE IF NOT EXISTS placement_db;
USE placement_db;

CREATE TABLE IF NOT EXISTS students (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  department VARCHAR(100) NOT NULL,
  graduation_year INT NOT NULL,
  gpa DECIMAL(3,2) NOT NULL,
  skills VARCHAR(500) DEFAULT '',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS companies (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(140) NOT NULL UNIQUE,
  industry VARCHAR(100) NOT NULL,
  location VARCHAR(140) NOT NULL,
  website VARCHAR(220) DEFAULT '',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS applications (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  student_id BIGINT NOT NULL,
  company_id BIGINT NOT NULL,
  role VARCHAR(140) NOT NULL,
  package_lpa DECIMAL(7,2) NOT NULL,
  status VARCHAR(30) NOT NULL DEFAULT 'Applied',
  applied_on DATE NOT NULL DEFAULT (CURRENT_DATE),
  CONSTRAINT uq_student_company_role UNIQUE(student_id, company_id, role),
  CONSTRAINT chk_application_status CHECK(status IN ('Applied','Interview','Offer','Rejected')),
  CONSTRAINT fk_app_student FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,
  CONSTRAINT fk_app_company FOREIGN KEY(company_id) REFERENCES companies(id) ON DELETE CASCADE
);

INSERT IGNORE INTO students(full_name,email,department,graduation_year,gpa,skills) VALUES
('Aarav Sharma','aarav.sharma@campus.edu','Computer Science',2026,3.82,'Java, SQL, Spring'),
('Maya Patel','maya.patel@campus.edu','Information Technology',2026,3.74,'Python, Data Analysis, SQL'),
('Ishaan Verma','ishaan.verma@campus.edu','Electronics',2026,3.56,'Embedded Systems, C++, IoT'),
('Ananya Rao','ananya.rao@campus.edu','Computer Science',2026,3.93,'React, JavaScript, UI Design'),
('Rohan Das','rohan.das@campus.edu','Mechanical',2026,3.48,'CAD, Manufacturing, Python'),
('Zoya Khan','zoya.khan@campus.edu','Information Technology',2026,3.88,'Cloud, AWS, Java');

INSERT IGNORE INTO companies(name,industry,location,website) VALUES
('Northstar Labs','Technology','Bengaluru, India','northstarlabs.example'),
('Vertex Analytics','Data & Analytics','Hyderabad, India','vertexanalytics.example'),
('Pine & Peak','Fintech','Mumbai, India','pinepeak.example'),
('Cirrus Systems','Cloud Computing','Pune, India','cirrussystems.example');
