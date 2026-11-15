# Database relationship diagram

```mermaid
erDiagram
    STUDENTS ||--o{ APPLICATIONS : submits
    COMPANIES ||--o{ DRIVES : posts
    DRIVES ||--o{ APPLICATIONS : receives
    APPLICATIONS ||--o{ APPLICATION_HISTORY : records
    STUDENTS o|--o{ APPLICATION_HISTORY : changes

    STUDENTS {
        bigint id PK
        varchar full_name
        varchar email UK
        varchar department
        int graduation_year
        decimal gpa
        varchar skills
        varchar resume_link
        varchar password_hash
    }
    COMPANIES {
        bigint id PK
        varchar name UK
        varchar industry
        varchar location
    }
    DRIVES {
        bigint id PK
        bigint company_id FK
        varchar title
        varchar location
        decimal package_lpa
        decimal eligibility_gpa
        varchar eligibility_branch
        date apply_by
        varchar status
    }
    APPLICATIONS {
        bigint id PK
        bigint student_id FK
        bigint drive_id FK
        bigint company_id FK
        varchar status
        varchar stage
        date applied_on
    }
    APPLICATION_HISTORY {
        bigint id PK
        bigint application_id FK
        varchar status
        varchar stage
        timestamp changed_at
        bigint changed_by FK
    }
```

`APPLICATIONS` connects a student to a placement drive. Each drive belongs to a company. `APPLICATION_HISTORY` stores each status/stage change so students can see the timeline.
