# Campus Placement Management System — Lab Guide

## Objective

Build a web-based placement portal with separate student and admin workflows. Students maintain profiles, browse eligible placement drives, apply, and follow their application stage. Admins manage companies and drives and review/update applicants.

## Technology architecture

| Layer | Technology | Purpose |
|---|---|---|
| Frontend | HTML, CSS, Bootstrap 5, JavaScript | Responsive pages, forms, tables, and browser/server requests |
| Backend | Java 17+, JDBC, Java built-in HTTP server | Authentication, validation, API endpoints, and SQL operations |
| Database | MySQL 8 | Persistent students, companies, drives, and applications |
| Build | Apache Maven | Compile Java and download MySQL Connector/J |

## Setup and run (Windows)

Install JDK 17+, Apache Maven, MySQL Server 8, and Git (for cloning). In MySQL, create the local database and app user:

```sql
CREATE DATABASE IF NOT EXISTS placement_db;
CREATE USER IF NOT EXISTS 'placement_user'@'localhost' IDENTIFIED BY 'YOUR_CHOSEN_DB_PASSWORD';
GRANT ALL PRIVILEGES ON placement_db.* TO 'placement_user'@'localhost';
```

In PowerShell, set the password you used when creating the MySQL app user and choose an initial admin password (8 or more characters), then run:

```powershell
$env:DB_PASSWORD = 'your chosen app database password'
$env:ADMIN_INITIAL_PASSWORD = 'choose an admin password with at least 8 characters'
.\run.bat
```

The first launch creates/migrates the tables and sample company/drive data. Open <http://localhost:8080>. Use Ctrl+C to stop the server. Set `DB_PASSWORD` again in each new PowerShell window. `ADMIN_INITIAL_PASSWORD` is needed only on the first launch that creates the admin account. For custom DB credentials, set `DB_URL` and `DB_USER` before running.

## Main features

### Student

- Register and sign in with a password.
- Update name, branch, graduation year, CGPA, skills, and resume URL.
- Browse open drives and apply when branch and CGPA eligibility checks pass.
- View application status and current stage.
- Recover a forgotten password using a 10-minute code shown in the reset form. This lab setup does not send email.

### Admin

- Review dashboard totals for students, open drives, applications, and selections.
- Add, edit, and delete companies with a Bootstrap form.
- Create, edit, and delete drives with role, location, package, eligibility, and deadline.
- Choose a drive, search applicants, filter by status/stage, and update both.
- Change the admin password after signing in.

The first launch seeds an administrator account using your `ADMIN_INITIAL_PASSWORD`. For a real deployment, configure a private administrator password and add email delivery for password recovery.

## Relational database tables

- `students`: account email, password hash, name, branch, graduation year, CGPA, skills, and resume URL.
- `companies`: employer name, industry, location, and website.
- `drives`: company, role, location, package, branch/CGPA eligibility, description, deadline, and open/closed status.
- `applications`: student and drive references, application date, status, and stage. Foreign keys keep application records linked to existing students and drives.

## Screenshots for lab report

Capture these screens after running the app and save them under `docs/screenshots/`:

1. `student-drives.png` — student account showing the available drive cards.
2. `student-applications.png` — student application list with status and stage.
3. `admin-drives.png` — admin drive table with create/edit actions.
4. `admin-companies.png` — admin company list with add/edit/delete actions.
5. `admin-applicants.png` — applicants view showing filters and status/stage selectors.

On Windows, open each screen and press **Win+Shift+S**, then save the captured image using the filename above. Keep sample student information fictitious in screenshots.

## Suggested lab demonstration

1. Register a student account and complete the profile.
2. Sign in as admin, add a company, and create a drive for that company.
3. Sign in as student and apply to the eligible drive.
4. Return to admin, find the applicant, and change status/stage.
5. Sign in as the student again and show the updated application tracking.
