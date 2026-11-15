# Lab Report — Campus Placement Management System

## Abstract

The Campus Placement Management System is a small web application that helps students browse placement drives and track applications, while placement administrators manage employers, drives, applicants, and application progress.

## Objectives

- Maintain student placement profiles.
- Publish company placement drives with eligibility criteria.
- Let eligible students submit applications.
- Allow administrators to update application status and stage.
- Keep relational records in MySQL and access them from Java using JDBC.

## Architecture

| Layer | Implementation |
|---|---|
| User interface | HTML, CSS, Bootstrap 5, JavaScript |
| Application server | Java 17+ HTTP server |
| Data access | JDBC and MySQL Connector/J |
| Database | MySQL 8 relational schema |
| Build | Apache Maven |

## Database design

The database contains `students`, `companies`, `drives`, `applications`, and `application_history`. The ER diagram is in [DATABASE_ERD.md](DATABASE_ERD.md). A student can submit multiple applications; each drive belongs to one company; each application belongs to one student and drive; and each application can have multiple history events.

## Module summary

### Student portal

Students register and sign in, maintain branch/year/CGPA/skills/resume details, browse drives, apply when profile eligibility matches, and view application status history.

### Admin portal

Administrators review dashboard counts, add/edit/delete companies, create/edit/delete drives, search/filter applicants by status and stage, update progress, and change the admin password.

## Demonstration sequence

1. Register a student, complete the profile, and sign out.
2. Sign in as admin; create a company and a placement drive with branch/CGPA criteria.
3. Sign in as the student and submit an eligible application.
4. Sign in as admin; open the drive applicants, search for the student, and update status/stage.
5. Return to the student portal and show the saved history timeline.

## Screenshots

Save the screenshots in `docs/screenshots/` using these filenames. Capture each page with **Win+Shift+S** after following the demonstration steps:

| Filename | Screen |
|---|---|
| `student-drives.png` | Student portal with placement drive cards |
| `student-applications.png` | Student applications and status history |
| `admin-drives.png` | Admin drive table and create/edit form |
| `admin-companies.png` | Admin company cards and company form |
| `admin-applicants.png` | Applicant search/filter table with stage/status controls |

## Limitations

- Student academic profile values are self-reported in this lab version.
- Password recovery prints a short-lived reset code in the local server terminal; email delivery is not configured.
- This application is intended for local coursework demonstrations, not public deployment.

## Run instructions

Install JDK 17+, Maven, and MySQL 8. Configure the `placement_db` database and app user using the README; set `DB_PASSWORD` and `ADMIN_INITIAL_PASSWORD`, then run `.\run.bat` in the project folder and open <http://localhost:8080>.
