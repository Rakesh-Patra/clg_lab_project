# Campus Placement Management System

A lab project for campus placement tracking using a responsive Bootstrap frontend, Java/JDBC backend, and MySQL relational database.

## Requirements

- JDK 17 or newer
- Apache Maven 3.9 or newer
- MySQL Community Server 8
- Git (when cloning)

Maven is the build tool; MySQL is the database. Maven downloads the JDBC connector during the first build.

## Quick setup (Windows)

1. Install the requirements above and start the MySQL80 service.
2. In MySQL as root, create the database and project user. Replace the placeholder with a password you choose:

```sql
CREATE DATABASE IF NOT EXISTS placement_db;
CREATE USER IF NOT EXISTS 'placement_user'@'localhost' IDENTIFIED BY 'YOUR_CHOSEN_DB_PASSWORD';
GRANT ALL PRIVILEGES ON placement_db.* TO 'placement_user'@'localhost';
```

3. Open PowerShell in the project folder. Set your app database password, choose an initial admin password, and start the app:

```powershell
$env:DB_PASSWORD = 'your chosen app database password'
$env:ADMIN_INITIAL_PASSWORD = 'choose an admin password with at least 8 characters'
.\run.bat
```

Open <http://localhost:8080>. The app creates/migrates its schema and sample data on startup. Stop it with Ctrl+C. On later starts, set `DB_PASSWORD` again. `ADMIN_INITIAL_PASSWORD` is needed only on the first startup that creates the admin account.

## Roles and password recovery

Students can register from the sign-in page. The admin email is `admin@placement.local`; its initial password is the value you set in `ADMIN_INITIAL_PASSWORD`. After signing in, change it from **Admin > Companies > Change admin password**.

Student **Forgot password?** issues a 6-digit code valid for 10 minutes. The code appears in the reset form because this lab app has no email provider configured.

## Features

- Student registration/sign-in and editable placement profile
- Open-drive browsing, eligibility checks, online application, status/stage tracking
- Admin company create/edit/delete and drive create/edit/delete
- Applicant search and status/stage filters
- Responsive Bootstrap 5 UI

See [docs/PROJECT_GUIDE.md](docs/PROJECT_GUIDE.md) for architecture, database tables, setup walkthrough, and lab-report screenshot checklist.

## Structure

```text
src/main/java/com/campus/placement/   Java HTTP API, authentication, and JDBC
src/main/resources/static/            Bootstrap page, styles, and browser JavaScript
database/schema.sql                   MySQL schema reference
pom.xml                               Maven build and Connector/J dependency
run.bat                               Windows build and run
```

For the lab deliverables, see [Lab Report](docs/LAB_REPORT.md), [Project Guide](docs/PROJECT_GUIDE.md), and [Database ER Diagram](docs/DATABASE_ERD.md).
