package com.campus.placement;

import java.sql.*;
import java.security.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

final class Database {
  private Database() {}
  static Connection connect() throws SQLException {
    String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/placement_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    String password=System.getenv("DB_PASSWORD");
    if(password==null||password.isBlank())throw new SQLException("Set DB_PASSWORD before starting the app.");
    return DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","placement_user"),password);
  }
  static void initialize() throws Exception {
    try(Connection c=connect();Statement s=c.createStatement()) {
      s.execute("CREATE TABLE IF NOT EXISTS students (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, full_name VARCHAR(120) NOT NULL, email VARCHAR(180) NOT NULL UNIQUE, department VARCHAR(100) NOT NULL DEFAULT '', graduation_year INT NOT NULL DEFAULT 2026, gpa DECIMAL(3,2) NOT NULL DEFAULT 0, skills VARCHAR(500) DEFAULT '', resume_link VARCHAR(500) DEFAULT '', student_code VARCHAR(40) DEFAULT NULL UNIQUE, password_hash VARCHAR(300) DEFAULT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
      addColumn(c,"students","resume_link","VARCHAR(500) DEFAULT ''"); addColumn(c,"students","password_hash","VARCHAR(300) DEFAULT NULL"); addColumn(c,"students","student_code","VARCHAR(40) DEFAULT NULL UNIQUE");
      s.execute("CREATE TABLE IF NOT EXISTS companies (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,name VARCHAR(140) NOT NULL UNIQUE,industry VARCHAR(100) NOT NULL,location VARCHAR(140) NOT NULL,website VARCHAR(220) DEFAULT '',created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
      s.execute("CREATE TABLE IF NOT EXISTS drives (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,company_id BIGINT NOT NULL,title VARCHAR(140) NOT NULL,location VARCHAR(140) NOT NULL,package_lpa DECIMAL(7,2) NOT NULL,eligibility_gpa DECIMAL(3,2) NOT NULL DEFAULT 0,eligibility_branch VARCHAR(300) NOT NULL DEFAULT 'All branches',description VARCHAR(800) DEFAULT '',apply_by DATE NOT NULL,status VARCHAR(20) NOT NULL DEFAULT 'Open',created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY(company_id) REFERENCES companies(id) ON DELETE CASCADE)");
      s.execute("CREATE TABLE IF NOT EXISTS applications (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,student_id BIGINT NOT NULL,drive_id BIGINT NULL,company_id BIGINT NOT NULL,role VARCHAR(140) NOT NULL,package_lpa DECIMAL(7,2) NOT NULL,status VARCHAR(30) NOT NULL DEFAULT 'Applied',stage VARCHAR(40) NOT NULL DEFAULT 'Applied',applied_on DATE NOT NULL DEFAULT (CURRENT_DATE),UNIQUE KEY uq_student_drive(student_id,drive_id),FOREIGN KEY(student_id) REFERENCES students(id) ON DELETE CASCADE,FOREIGN KEY(company_id) REFERENCES companies(id) ON DELETE CASCADE,FOREIGN KEY(drive_id) REFERENCES drives(id) ON DELETE SET NULL)");
      addColumn(c,"applications","drive_id","BIGINT NULL");addColumn(c,"applications","stage","VARCHAR(40) NOT NULL DEFAULT 'Applied'");
      s.execute("CREATE TABLE IF NOT EXISTS application_history (id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,application_id BIGINT NOT NULL,status VARCHAR(30) NOT NULL,stage VARCHAR(40) NOT NULL,changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,changed_by BIGINT NULL,FOREIGN KEY(application_id) REFERENCES applications(id) ON DELETE CASCADE,FOREIGN KEY(changed_by) REFERENCES students(id) ON DELETE SET NULL)");
      s.execute("INSERT INTO application_history(application_id,status,stage,changed_at,changed_by) SELECT a.id,a.status,a.stage,a.applied_on,a.student_id FROM applications a WHERE NOT EXISTS(SELECT 1 FROM application_history h WHERE h.application_id=a.id)");
      s.execute("UPDATE students SET department=CASE department WHEN 'Computer Science' THEN 'CSE' WHEN 'Information Technology' THEN 'IT' WHEN 'Electronics' THEN 'ECE' WHEN 'Mechanical' THEN 'ME' WHEN 'Computer Engineering' THEN 'CE' WHEN 'Civil Engineering' THEN 'CIVIL' ELSE department END");
      s.execute("UPDATE drives SET eligibility_branch=REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(eligibility_branch,'Computer Science','CSE'),'Information Technology','IT'),'Electronics','ECE'),'Mechanical','ME'),'Computer Engineering','CE')");
      seed(c);
      s.execute("UPDATE students SET department=CASE department WHEN 'Computer Science' THEN 'CSE' WHEN 'Information Technology' THEN 'IT' WHEN 'Electronics' THEN 'ECE' WHEN 'Mechanical' THEN 'ME' WHEN 'Computer Engineering' THEN 'CE' WHEN 'Civil Engineering' THEN 'CIVIL' ELSE department END");
      s.execute("UPDATE drives SET eligibility_branch=REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(eligibility_branch,'Computer Science','CSE'),'Information Technology','IT'),'Electronics','ECE'),'Mechanical','ME'),'Computer Engineering','CE')");
    }
  }
  private static void addColumn(Connection c,String table,String col,String type)throws SQLException{try(Statement s=c.createStatement()){try{s.execute("ALTER TABLE "+table+" ADD COLUMN "+col+" "+type);}catch(SQLException e){if(e.getErrorCode()!=1060)throw e;}}}
  static String hash(String raw)throws Exception{byte[] salt=new byte[16];SecureRandom.getInstanceStrong().nextBytes(salt);PBEKeySpec spec=new PBEKeySpec(raw.toCharArray(),salt,120000,256);byte[] key=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();spec.clearPassword();return Base64.getEncoder().encodeToString(salt)+":"+Base64.getEncoder().encodeToString(key);}
  static boolean verify(String raw,String encoded)throws Exception{if(encoded==null||!encoded.contains(":"))return false;String[] p=encoded.split(":");byte[] salt=Base64.getDecoder().decode(p[0]);PBEKeySpec spec=new PBEKeySpec(raw.toCharArray(),salt,120000,256);byte[] key=SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();spec.clearPassword();return MessageDigest.isEqual(key,Base64.getDecoder().decode(p[1]));}
  private static void seed(Connection c)throws Exception {
    try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM students")){r.next();if(r.getInt(1)==0)try(Statement q=c.createStatement()){q.execute("INSERT INTO students(full_name,email,department,graduation_year,gpa,skills) VALUES('Aarav Sharma','aarav.sharma@campus.edu','Computer Science',2026,3.82,'Java, SQL'),('Maya Patel','maya.patel@campus.edu','Information Technology',2026,3.74,'Python, SQL'),('Ananya Rao','ananya.rao@campus.edu','Computer Science',2026,3.93,'React, JavaScript')");}}
    try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM companies")){r.next();if(r.getInt(1)==0)try(Statement q=c.createStatement()){q.execute("INSERT INTO companies(name,industry,location) VALUES('Northstar Labs','Technology','Bengaluru'),('Vertex Analytics','Data & Analytics','Hyderabad'),('Cirrus Systems','Cloud Computing','Pune')");}}
    try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM students WHERE email='admin@placement.local'")){r.next();if(r.getInt(1)==0){String initial=System.getenv("ADMIN_INITIAL_PASSWORD");if(initial==null||initial.length()<8)throw new IllegalStateException("Set ADMIN_INITIAL_PASSWORD (at least 8 characters) before first startup.");try(PreparedStatement p=c.prepareStatement("INSERT INTO students(full_name,email,department,graduation_year,gpa,skills,password_hash) VALUES('Placement Admin','admin@placement.local','Administration',2026,0,'',?)")){p.setString(1,hash(initial));p.executeUpdate();}}}
    try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT COUNT(*) FROM drives")){r.next();if(r.getInt(1)==0)try(Statement q=c.createStatement()){q.execute("INSERT INTO drives(company_id,title,location,package_lpa,eligibility_gpa,eligibility_branch,description,apply_by) VALUES(1,'Software Engineer','Bengaluru',12.50,3.00,'Computer Science, Information Technology','Build reliable web services with the engineering team.',DATE_ADD(CURRENT_DATE,INTERVAL 30 DAY)),(2,'Data Analyst','Hyderabad',9.00,3.20,'All branches','Analyze product and business data.',DATE_ADD(CURRENT_DATE,INTERVAL 21 DAY)),(3,'Graduate Engineer','Pune',8.50,2.80,'All branches','Join the graduate engineering program.',DATE_ADD(CURRENT_DATE,INTERVAL 45 DAY))");}}
  }
}
