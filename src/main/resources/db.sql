-- ==============================
-- 0. ENUMs & Extension
-- ==============================
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TYPE role_name_enum             AS ENUM ('admin','manager','employee');
CREATE TYPE employee_status_enum       AS ENUM ('active','inactive','on_leave');
CREATE TYPE course_level_enum          AS ENUM ('beginner','intermediate','advanced');
CREATE TYPE course_type_enum           AS ENUM ('self_paced','instructor_led','blended');
CREATE TYPE enrollment_status_enum     AS ENUM ('registered','in_progress','completed','dropped');
CREATE TYPE progress_status_enum       AS ENUM ('not_started','in_progress','completed');
CREATE TYPE assignment_type_enum       AS ENUM ('file','text','link','mixed');
CREATE TYPE assignment_status_enum     AS ENUM ('draft','published','archived');
CREATE TYPE submission_status_enum     AS ENUM ('submitted','graded','late','resubmitted');
CREATE TYPE reaction_type_enum         AS ENUM ('like','dislike','helpful','funny','insightful');
CREATE TYPE roadmap_status_enum        AS ENUM ('draft','active','completed','archived','cancelled');
CREATE TYPE roadmap_area_status_enum   AS ENUM ('pending','in_progress','completed','skipped');


-- ==============================
-- 1. Phân Quyền & Người Dùng
-- ==============================
CREATE TABLE roles (
                       role_id     BIGINT        PRIMARY KEY,
                       role_name   role_name_enum NOT NULL UNIQUE,
                       description TEXT,
                       created_at  TIMESTAMPTZ   NOT NULL DEFAULT now(),
                       updated_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE users (
                       user_id      BIGINT        PRIMARY KEY,
                       username     VARCHAR(50)   NOT NULL UNIQUE,
                       password     VARCHAR(255)  NOT NULL,
                       email        VARCHAR(100)  NOT NULL UNIQUE,
                       role_id      BIGINT        NOT NULL,
                       is_active    BOOLEAN       NOT NULL DEFAULT TRUE,
                       is_deleted   BOOLEAN       NOT NULL DEFAULT FALSE,
                       last_login   TIMESTAMPTZ,
                       created_at   TIMESTAMPTZ   NOT NULL DEFAULT now(),
                       updated_at   TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE departments (
                             department_id   BIGINT      PRIMARY KEY,
                             department_name VARCHAR(100) NOT NULL,
                             manager_id      BIGINT,
                             description     TEXT,
                             created_by      BIGINT,
                             updated_by      BIGINT,
                             created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                             is_deleted      BOOLEAN     NOT NULL DEFAULT FALSE
);

CREATE TABLE employees (
                           employee_id   BIGINT        PRIMARY KEY,
                           user_id       BIGINT        NOT NULL,
                           first_name    VARCHAR(50),
                           last_name     VARCHAR(50),
                           department_id BIGINT        NOT NULL,
                           position      VARCHAR(100),
                           hire_date     DATE,
                           status        employee_status_enum NOT NULL DEFAULT 'active',
                           created_by    BIGINT,
                           updated_by    BIGINT,
                           created_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
                           updated_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
                           is_deleted    BOOLEAN       NOT NULL DEFAULT FALSE
);

CREATE TABLE employee_hierarchy (
                                    hierarchy_id BIGINT   PRIMARY KEY,
                                    manager_id   BIGINT   NOT NULL,
                                    employee_id  BIGINT   NOT NULL,
                                    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                    updated_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                    CONSTRAINT chk_no_self_manager CHECK (manager_id <> employee_id),
                                    UNIQUE(manager_id, employee_id)
);


-- ==============================
-- 2. Nhật Ký Hoạt Động
-- ==============================
CREATE TABLE activity_logs (
                               log_id        BIGINT      PRIMARY KEY,
                               user_id       BIGINT      NOT NULL,
                               activity_type VARCHAR(50),
                               ip_address    VARCHAR(45),
                               session_id    VARCHAR(100),
                               description   TEXT,
                               created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
                               updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);


-- ==============================
-- 3. Đề Mục & Đào Tạo
-- ==============================
CREATE TABLE training_areas (
                                area_id     BIGINT        PRIMARY KEY,
                                area_code   VARCHAR(20)   NOT NULL UNIQUE,
                                area_name   VARCHAR(255)  NOT NULL,
                                description TEXT,
                                created_by  BIGINT,
                                created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE training_area_hierarchy (
                                         parent_area_id BIGINT    NOT NULL,
                                         child_area_id  BIGINT    NOT NULL,
                                         sequence       INTEGER,
                                         description    TEXT,
                                         created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                         PRIMARY KEY (parent_area_id, child_area_id)
);

CREATE TABLE training_area_contents (
                                        content_id          BIGINT    PRIMARY KEY,
                                        area_id             BIGINT    NOT NULL,
                                        content_title       VARCHAR(255) NOT NULL,
                                        content_description TEXT,
                                        created_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE course_statuses (
                                 status_id   BIGINT      PRIMARY KEY,
                                 status_name VARCHAR(50) NOT NULL UNIQUE,
                                 description TEXT,
                                 created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE courses (
                         course_id             BIGINT      PRIMARY KEY,
                         course_code           VARCHAR(50) NOT NULL UNIQUE,
                         title                 VARCHAR(255) NOT NULL,
                         description           TEXT,
                         content_description   TEXT,
                         level                 course_level_enum NOT NULL DEFAULT 'beginner',
                         language              VARCHAR(10) NOT NULL DEFAULT 'en',
                         course_type           course_type_enum NOT NULL DEFAULT 'self_paced',
                         start_date            DATE,
                         end_date              DATE,
                         enrollment_start      TIMESTAMPTZ,
                         enrollment_end        TIMESTAMPTZ,
                         max_enrollment        INTEGER     NOT NULL DEFAULT 0,
                         price                 NUMERIC(12,2) NOT NULL DEFAULT 0.00,
                         currency              VARCHAR(3)  NOT NULL DEFAULT 'USD',
                         duration_weeks        INTEGER,
                         duration_hours        INTEGER,
                         expected_time_per_week INTEGER,
                         status_id             BIGINT      NOT NULL,
                         instructor_id         BIGINT      NOT NULL,
                         thumbnail_url         VARCHAR(500),
                         syllabus_url          VARCHAR(500),
                         rating_avg            NUMERIC(3,2),
                         rating_count          INTEGER     NOT NULL DEFAULT 0,
                         total_enrolled        INTEGER     NOT NULL DEFAULT 0,
                         created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE course_outcomes_training (
                                          outcome_id BIGINT   PRIMARY KEY,
                                          course_id  BIGINT   NOT NULL,
                                          area_id    BIGINT   NOT NULL,
                                          description TEXT,
                                          created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE course_prerequisite_training (
                                              course_id  BIGINT NOT NULL,
                                              area_id    BIGINT NOT NULL,
                                              created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                                              PRIMARY KEY(course_id, area_id)
);

CREATE TABLE instructors (
                             instructor_id   BIGINT      PRIMARY KEY,
                             user_id         BIGINT      NOT NULL,
                             first_name      VARCHAR(50),
                             last_name       VARCHAR(50),
                             bio             TEXT,
                             email           VARCHAR(100) UNIQUE,
                             phone           VARCHAR(20),
                             profile_pic_url VARCHAR(500),
                             created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);


-- ==============================
-- 4. Nội Dung Khóa Học
-- ==============================
CREATE TABLE modules (
                         module_id   BIGINT      PRIMARY KEY,
                         course_id   BIGINT      NOT NULL,
                         title       VARCHAR(255) NOT NULL,
                         description TEXT,
                         order_index INTEGER,
                         created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE content (
                         content_id   BIGINT      PRIMARY KEY,
                         title        VARCHAR(255) NOT NULL,
                         type         VARCHAR(50),
                         content_url  VARCHAR(500),
                         content_text TEXT,
                         created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE resources (
                           resource_id BIGINT      PRIMARY KEY,
                           content_id  BIGINT      NOT NULL,
                           title       VARCHAR(255),
                           url         VARCHAR(500),
                           type        VARCHAR(50),
                           created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                           updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE quizzes (
                         quiz_id     BIGINT      PRIMARY KEY,
                         title       VARCHAR(255) NOT NULL,
                         description TEXT,
                         questions   JSONB,
                         time_limit  INTEGER,
                         created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE quiz_questions (
                                question_id   BIGINT      PRIMARY KEY,
                                quiz_id       BIGINT      NOT NULL,
                                question_text TEXT        NOT NULL,
                                question_type VARCHAR(50),
                                points        INTEGER,
                                order_index   INTEGER,
                                json_data     JSONB,
                                created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
                                updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE question_options (
                                  option_id   BIGINT      PRIMARY KEY,
                                  question_id BIGINT      NOT NULL,
                                  option_text TEXT,
                                  is_correct  BOOLEAN     NOT NULL DEFAULT FALSE,
                                  order_index INTEGER,
                                  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE assignments (
                             assignment_id         BIGINT      PRIMARY KEY,
                             course_id             BIGINT      NOT NULL,
                             module_id             BIGINT,
                             title                 VARCHAR(255) NOT NULL,
                             description           TEXT,
                             assigned_date         TIMESTAMPTZ NOT NULL DEFAULT now(),
                             available_from        TIMESTAMPTZ,
                             deadline              TIMESTAMPTZ NOT NULL,
                             late_deadline         TIMESTAMPTZ,
                             grace_period_minutes  INTEGER     NOT NULL DEFAULT 0,
                             time_limit_seconds    INTEGER,
                             max_attempts          INTEGER     NOT NULL DEFAULT 1,
                             submission_type       assignment_type_enum NOT NULL DEFAULT 'file',
                             allow_late_submission BOOLEAN     NOT NULL DEFAULT FALSE,
                             status                assignment_status_enum NOT NULL DEFAULT 'draft',
                             created_by            BIGINT      NOT NULL,
                             updated_by            BIGINT,
                             created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE module_items (
                              item_id        BIGINT      PRIMARY KEY,
                              module_id      BIGINT      NOT NULL,
                              content_id     BIGINT,
                              quiz_id        BIGINT,
                              assignment_id  BIGINT,
                              order_index    INTEGER,
                              created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
                              updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);


-- ==============================
-- 5. Đăng Ký & Tiến Độ
-- ==============================
CREATE TABLE enrollments (
                             enrollment_id      BIGINT      PRIMARY KEY,
                             employee_id        BIGINT      NOT NULL,
                             course_id          BIGINT      NOT NULL,
                             enrollment_date    TIMESTAMPTZ NOT NULL DEFAULT now(),
                             status             enrollment_status_enum NOT NULL DEFAULT 'registered',
                             progress_percent   SMALLINT    NOT NULL DEFAULT 0,
                             time_spent_seconds INTEGER     NOT NULL DEFAULT 0,
                             last_accessed_at   TIMESTAMPTZ,
                             completion_date    TIMESTAMPTZ,
                             created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE module_progress (
                                 progress_id        BIGINT      PRIMARY KEY,
                                 enrollment_id      BIGINT      NOT NULL,
                                 module_id          BIGINT      NOT NULL,
                                 status             progress_status_enum NOT NULL DEFAULT 'not_started',
                                 started_at         TIMESTAMPTZ,
                                 completed_at       TIMESTAMPTZ,
                                 time_spent_seconds INTEGER     NOT NULL DEFAULT 0,
                                 attempt_count      INTEGER     NOT NULL DEFAULT 0,
                                 last_activity_at   TIMESTAMPTZ,
                                 updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
                                 UNIQUE(enrollment_id, module_id)
);

CREATE TABLE content_progress (
                                  progress_id        BIGINT      PRIMARY KEY,
                                  enrollment_id      BIGINT      NOT NULL,
                                  content_id         BIGINT      NOT NULL,
                                  status             progress_status_enum NOT NULL DEFAULT 'not_started',
                                  percent_complete   SMALLINT    NOT NULL DEFAULT 0,
                                  started_at         TIMESTAMPTZ,
                                  completed_at       TIMESTAMPTZ,
                                  time_spent_seconds INTEGER     NOT NULL DEFAULT 0,
                                  last_accessed_at   TIMESTAMPTZ,
                                  last_activity_at   TIMESTAMPTZ,
                                  updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  UNIQUE(enrollment_id, content_id)
);


-- ==============================
-- 6. Đánh Giá & Submissions
-- ==============================
CREATE TABLE quiz_attempts (
                               attempt_id         BIGINT      PRIMARY KEY,
                               quiz_id            BIGINT      NOT NULL,
                               employee_id        BIGINT      NOT NULL,
                               attempt_number     INTEGER     NOT NULL DEFAULT 1,
                               start_time         TIMESTAMPTZ NOT NULL,
                               end_time           TIMESTAMPTZ,
                               time_spent_seconds INTEGER     NOT NULL DEFAULT 0,
                               score              NUMERIC,
                               max_score          NUMERIC,
                               passed             BOOLEAN,
                               created_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE quiz_answers (
                              answer_id       BIGINT      PRIMARY KEY,
                              attempt_id      BIGINT      NOT NULL,
                              question_id     BIGINT      NOT NULL,
                              selected_option BIGINT,
                              answer_text     TEXT,
                              is_correct      BOOLEAN,
                              created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE assignment_submissions (
                                        submission_id      BIGINT      PRIMARY KEY,
                                        assignment_id      BIGINT      NOT NULL,
                                        employee_id        BIGINT      NOT NULL,
                                        submission_version INTEGER     NOT NULL DEFAULT 1,
                                        submitted_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
                                        file_url           VARCHAR(500),
                                        text_submission    TEXT,
                                        time_spent_seconds INTEGER     NOT NULL DEFAULT 0,
                                        grade              NUMERIC,
                                        feedback           TEXT,
                                        status             submission_status_enum NOT NULL DEFAULT 'submitted',
                                        updated_at         TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE certificates (
                              certificate_id  BIGINT      PRIMARY KEY,
                              enrollment_id   BIGINT      NOT NULL,
                              issued_date     DATE        NOT NULL DEFAULT CURRENT_DATE,
                              certificate_url VARCHAR(500),
                              remarks         TEXT,
                              created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE user_course_statistics (
                                        stat_id               BIGINT      PRIMARY KEY,
                                        employee_id           BIGINT      NOT NULL,
                                        course_id             BIGINT      NOT NULL,
                                        enrollment_id         BIGINT      NOT NULL,
                                        modules_total         INTEGER     NOT NULL DEFAULT 0,
                                        modules_completed     INTEGER     NOT NULL DEFAULT 0,
                                        contents_total        INTEGER     NOT NULL DEFAULT 0,
                                        contents_completed    INTEGER     NOT NULL DEFAULT 0,
                                        avg_quiz_score        NUMERIC,
                                        quiz_attempts_count   INTEGER     NOT NULL DEFAULT 0,
                                        avg_assignment_score  NUMERIC,
                                        assignments_submitted INTEGER     NOT NULL DEFAULT 0,
                                        overall_progress      SMALLINT    NOT NULL DEFAULT 0,
                                        last_activity_at      TIMESTAMPTZ,
                                        updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE grade_history (
                               history_id    BIGINT PRIMARY KEY,
                               related_type  VARCHAR(20) NOT NULL CHECK(related_type IN ('assignment_submission','quiz_attempt')),
                               related_id    BIGINT NOT NULL,
                               previous_grade NUMERIC,
                               new_grade     NUMERIC,
                               changed_by    BIGINT,
                               changed_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
                               CONSTRAINT fk_grade_history_user FOREIGN KEY(changed_by) REFERENCES users(user_id)
);
CREATE INDEX idx_grade_history_related ON grade_history(related_type, related_id);


-- ==============================
-- 7. Thảo Luận & Tag
-- ==============================
CREATE TABLE course_forums (
                               forum_id     BIGINT      PRIMARY KEY,
                               course_id    BIGINT      NOT NULL,
                               title        VARCHAR(255) NOT NULL,
                               description  TEXT,
                               is_active    BOOLEAN     NOT NULL DEFAULT TRUE,
                               created_by   BIGINT      NOT NULL,
                               created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                               updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE forum_threads (
                               thread_id    BIGINT      PRIMARY KEY,
                               forum_id     BIGINT      NOT NULL,
                               employee_id  BIGINT      NOT NULL,
                               title        VARCHAR(255) NOT NULL,
                               content      TEXT        NOT NULL,
                               rating       SMALLINT NOT NULL CHECK(rating BETWEEN 1 AND 5),
                               is_sticky    BOOLEAN     NOT NULL DEFAULT FALSE,
                               views_count  INTEGER     NOT NULL DEFAULT 0,
                               created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                               updated_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE forum_posts (
                             post_id        BIGINT      PRIMARY KEY,
                             thread_id      BIGINT      NOT NULL,
                             employee_id    BIGINT      NOT NULL,
                             parent_post_id BIGINT,
                             content        TEXT        NOT NULL,
                             created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE post_reactions (
                                reaction_id   BIGINT                 PRIMARY KEY,
                                post_id       BIGINT                 NOT NULL,
                                employee_id   BIGINT                 NOT NULL,
                                reaction_type reaction_type_enum     NOT NULL,
                                created_at    TIMESTAMPTZ            NOT NULL DEFAULT now()
);

CREATE TABLE forum_tags (
                            tag_id BIGINT PRIMARY KEY,
                            name   VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE thread_tags (
                             thread_id BIGINT NOT NULL,
                             tag_id    BIGINT NOT NULL,
                             PRIMARY KEY(thread_id, tag_id)
);


-- ==============================
-- 8. Roadmap Cá Nhân
-- ==============================
CREATE TABLE user_roadmaps (
                               roadmap_id        BIGINT      PRIMARY KEY,
                               employee_id       BIGINT      NOT NULL,
                               name              VARCHAR(255) NOT NULL,
                               description       TEXT,
                               priority          VARCHAR(10) NOT NULL DEFAULT 'medium',
                               created_by        BIGINT      NOT NULL,
                               approved_by       BIGINT,
                               start_date        DATE,
                               actual_start_date DATE,
                               target_end_date   DATE,
                               actual_end_date   DATE,
                               status            roadmap_status_enum NOT NULL DEFAULT 'draft',
                               is_current        BOOLEAN       NOT NULL DEFAULT TRUE,
                               created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
                               updated_by        BIGINT,
                               updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE roadmap_training_areas (
                                        roadmap_id         BIGINT      NOT NULL,
                                        area_id            BIGINT      NOT NULL,
                                        sequence           INTEGER     NOT NULL,
                                        target_start_date  DATE,
                                        actual_start_date  DATE,
                                        target_end_date    DATE,
                                        actual_end_date    DATE,
                                        allocated_hours    INTEGER     NOT NULL DEFAULT 0,
                                        weight             SMALLINT    NOT NULL DEFAULT 0,
                                        status             roadmap_area_status_enum NOT NULL DEFAULT 'pending',
                                        notes              TEXT,
                                        created_by         BIGINT      NOT NULL,
                                        created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
                                        updated_by         BIGINT,
                                        updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
                                        PRIMARY KEY (roadmap_id, area_id)
);

CREATE TABLE roadmap_area_courses (
                                      roadmap_id        BIGINT      NOT NULL,
                                      area_id           BIGINT      NOT NULL,
                                      course_id         BIGINT      NOT NULL,
                                      selected_date     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                      allocated_hours   INTEGER     NOT NULL DEFAULT 0,
                                      recommended_order INTEGER,
                                      prerequisites     TEXT,
                                      status            enrollment_status_enum NOT NULL DEFAULT 'registered',
                                      progress_percent  SMALLINT    NOT NULL DEFAULT 0,
                                      actual_start_date DATE,
                                      actual_end_date   DATE,
                                      feedback          TEXT,
                                      created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
                                      updated_by        BIGINT,
                                      updated_at        TIMESTAMPTZ NOT NULL DEFAULT now(),
                                      PRIMARY KEY (roadmap_id, area_id, course_id)
);

CREATE TABLE roadmap_statistics (
                                    stat_id             BIGINT      PRIMARY KEY,
                                    roadmap_id          BIGINT      NOT NULL,
                                    employee_id         BIGINT      NOT NULL,
                                    areas_total         INTEGER     NOT NULL DEFAULT 0,
                                    areas_completed     INTEGER     NOT NULL DEFAULT 0,
                                    courses_total       INTEGER     NOT NULL DEFAULT 0,
                                    courses_completed   INTEGER     NOT NULL DEFAULT 0,
                                    average_progress    SMALLINT    NOT NULL DEFAULT 0,
                                    average_score       NUMERIC,
                                    pass_rate           NUMERIC(5,2),
                                    total_time_spent    INTEGER     NOT NULL DEFAULT 0,
                                    courses_dropped     INTEGER     NOT NULL DEFAULT 0,
                                    last_calculated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Roadmap versions
CREATE TABLE roadmap_versions (
                                  version_id     BIGINT PRIMARY KEY,
                                  roadmap_id     BIGINT NOT NULL,
                                  version_number INTEGER NOT NULL,
                                  snapshot       JSONB NOT NULL,
                                  created_by     BIGINT NOT NULL,
                                  created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  CONSTRAINT fk_roadmap_versions_roadmap FOREIGN KEY(roadmap_id) REFERENCES user_roadmaps(roadmap_id) ON DELETE CASCADE,
                                  CONSTRAINT fk_roadmap_versions_user FOREIGN KEY(created_by) REFERENCES users(user_id)
);
CREATE UNIQUE INDEX idx_roadmap_versions_roadmap_version ON roadmap_versions(roadmap_id, version_number);

-- Roadmap shares
CREATE TABLE roadmap_shares (
                                share_id    BIGINT PRIMARY KEY,
                                roadmap_id  BIGINT NOT NULL,
                                user_id     BIGINT NOT NULL,
                                permission  VARCHAR(20) NOT NULL DEFAULT 'view',
                                shared_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                CONSTRAINT fk_roadmap_shares_roadmap FOREIGN KEY(roadmap_id) REFERENCES user_roadmaps(roadmap_id) ON DELETE CASCADE,
                                CONSTRAINT fk_roadmap_shares_user FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX idx_roadmap_shares_roadmap_user ON roadmap_shares(roadmap_id, user_id);

-- Roadmap feedback & rating
CREATE TABLE roadmap_feedback (
                                  feedback_id  BIGINT PRIMARY KEY,
                                  roadmap_id   BIGINT NOT NULL,
                                  employee_id  BIGINT NOT NULL,
                                  rating       SMALLINT NOT NULL CHECK(rating BETWEEN 1 AND 5),
                                  comment      TEXT,
                                  created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
                                  CONSTRAINT fk_roadmap_feedback_roadmap FOREIGN KEY(roadmap_id) REFERENCES user_roadmaps(roadmap_id) ON DELETE CASCADE,
                                  CONSTRAINT fk_roadmap_feedback_employee FOREIGN KEY(employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);
CREATE INDEX idx_roadmap_feedback_roadmap ON roadmap_feedback(roadmap_id);

-- ==============================
-- 9. Thêm FK constraints và Indexes
-- ==============================

-- users.role_id → roles.role_id
ALTER TABLE users
    ADD CONSTRAINT fk_users_role_id_roles
        FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT;
CREATE INDEX idx_users_role_id ON users(role_id);

-- departments.manager_id → employees.employee_id
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_manager_id_employees
        FOREIGN KEY (manager_id) REFERENCES employees(employee_id) ON DELETE SET NULL;
CREATE INDEX idx_departments_manager_id ON departments(manager_id);

-- departments.created_by → users.user_id
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_created_by_users
        FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL;
CREATE INDEX idx_departments_created_by ON departments(created_by);

-- departments.updated_by → users.user_id
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_updated_by_users
        FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL;
CREATE INDEX idx_departments_updated_by ON departments(updated_by);

-- employees.user_id → users.user_id
ALTER TABLE employees
    ADD CONSTRAINT fk_employees_user_id_users
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE INDEX idx_employees_user_id ON employees(user_id);

-- employees.department_id → departments.department_id
ALTER TABLE employees
    ADD CONSTRAINT fk_employees_department_id_departments
        FOREIGN KEY (department_id) REFERENCES departments(department_id);
CREATE INDEX idx_employees_department_id ON employees(department_id);

-- employees.created_by → users.user_id
ALTER TABLE employees
    ADD CONSTRAINT fk_employees_created_by_users
        FOREIGN KEY (created_by) REFERENCES users(user_id);
CREATE INDEX idx_employees_created_by ON employees(created_by);

-- employees.updated_by → users.user_id
ALTER TABLE employees
    ADD CONSTRAINT fk_employees_updated_by_users
        FOREIGN KEY (updated_by) REFERENCES users(user_id);
CREATE INDEX idx_employees_updated_by ON employees(updated_by);

-- employee_hierarchy.manager_id → employees.employee_id
ALTER TABLE employee_hierarchy
    ADD CONSTRAINT fk_employee_hierarchy_manager
        FOREIGN KEY (manager_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_employee_hierarchy_manager_id ON employee_hierarchy(manager_id);

-- employee_hierarchy.employee_id → employees.employee_id
ALTER TABLE employee_hierarchy
    ADD CONSTRAINT fk_employee_hierarchy_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_employee_hierarchy_employee_id ON employee_hierarchy(employee_id);

-- activity_logs.user_id → users.user_id
ALTER TABLE activity_logs
    ADD CONSTRAINT fk_activity_logs_user_id_users
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE INDEX idx_activity_logs_user_id ON activity_logs(user_id);

-- training_area_hierarchy.parent_area_id → training_areas.area_id
ALTER TABLE training_area_hierarchy
    ADD CONSTRAINT fk_training_area_hierarchy_parent
        FOREIGN KEY (parent_area_id) REFERENCES training_areas(area_id) ON DELETE CASCADE;
CREATE INDEX idx_training_area_hierarchy_parent_id ON training_area_hierarchy(parent_area_id);

-- training_area_hierarchy.child_area_id → training_areas.area_id
ALTER TABLE training_area_hierarchy
    ADD CONSTRAINT fk_training_area_hierarchy_child
        FOREIGN KEY (child_area_id) REFERENCES training_areas(area_id) ON DELETE CASCADE;
CREATE INDEX idx_training_area_hierarchy_child_id ON training_area_hierarchy(child_area_id);

-- training_area_contents.area_id → training_areas.area_id
ALTER TABLE training_area_contents
    ADD CONSTRAINT fk_training_area_contents_area
        FOREIGN KEY (area_id) REFERENCES training_areas(area_id) ON DELETE CASCADE;
CREATE INDEX idx_training_area_contents_area_id ON training_area_contents(area_id);

-- courses.status_id → course_statuses.status_id
ALTER TABLE courses
    ADD CONSTRAINT fk_courses_status
        FOREIGN KEY (status_id) REFERENCES course_statuses(status_id);
CREATE INDEX idx_courses_status_id ON courses(status_id);

-- courses.instructor_id → instructors.instructor_id
ALTER TABLE courses
    ADD CONSTRAINT fk_courses_instructor
        FOREIGN KEY (instructor_id) REFERENCES instructors(instructor_id);
CREATE INDEX idx_courses_instructor_id ON courses(instructor_id);

-- course_outcomes_training.course_id → courses.course_id
ALTER TABLE course_outcomes_training
    ADD CONSTRAINT fk_course_outcomes_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_course_outcomes_course_id ON course_outcomes_training(course_id);

-- course_outcomes_training.area_id → training_areas.area_id
ALTER TABLE course_outcomes_training
    ADD CONSTRAINT fk_course_outcomes_area
        FOREIGN KEY (area_id) REFERENCES training_areas(area_id);
CREATE INDEX idx_course_outcomes_area_id ON course_outcomes_training(area_id);

-- course_prerequisite_training.course_id → courses.course_id
ALTER TABLE course_prerequisite_training
    ADD CONSTRAINT fk_course_prerequisites_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_course_prerequisite_course_id ON course_prerequisite_training(course_id);

-- course_prerequisite_training.area_id → training_areas.area_id
ALTER TABLE course_prerequisite_training
    ADD CONSTRAINT fk_course_prerequisites_area
        FOREIGN KEY (area_id) REFERENCES training_areas(area_id);
CREATE INDEX idx_course_prerequisite_area_id ON course_prerequisite_training(area_id);

-- instructors.user_id → users.user_id
ALTER TABLE instructors
    ADD CONSTRAINT fk_instructors_user
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
CREATE INDEX idx_instructors_user_id ON instructors(user_id);

-- modules.course_id → courses.course_id
ALTER TABLE modules
    ADD CONSTRAINT fk_modules_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_modules_course_id ON modules(course_id);

-- resources.content_id → content.content_id
ALTER TABLE resources
    ADD CONSTRAINT fk_resources_content
        FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;
CREATE INDEX idx_resources_content_id ON resources(content_id);

-- quiz_questions.quiz_id → quizzes.quiz_id
ALTER TABLE quiz_questions
    ADD CONSTRAINT fk_quiz_questions_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE;
CREATE INDEX idx_quiz_questions_quiz_id ON quiz_questions(quiz_id);

-- question_options.question_id → quiz_questions.question_id
ALTER TABLE question_options
    ADD CONSTRAINT fk_question_options_question
        FOREIGN KEY (question_id) REFERENCES quiz_questions(question_id) ON DELETE CASCADE;
CREATE INDEX idx_question_options_question_id ON question_options(question_id);

-- assignments.course_id → courses.course_id
ALTER TABLE assignments
    ADD CONSTRAINT fk_assignments_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_assignments_course_id ON assignments(course_id);

-- assignments.module_id → modules.module_id
ALTER TABLE assignments
    ADD CONSTRAINT fk_assignments_module
        FOREIGN KEY (module_id) REFERENCES modules(module_id) ON DELETE SET NULL;
CREATE INDEX idx_assignments_module_id ON assignments(module_id);

-- assignments.created_by → users.user_id
ALTER TABLE assignments
    ADD CONSTRAINT fk_assignments_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id);
CREATE INDEX idx_assignments_created_by ON assignments(created_by);

-- assignments.updated_by → users.user_id
ALTER TABLE assignments
    ADD CONSTRAINT fk_assignments_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(user_id);
CREATE INDEX idx_assignments_updated_by ON assignments(updated_by);

-- module_items.module_id → modules.module_id
ALTER TABLE module_items
    ADD CONSTRAINT fk_module_items_module
        FOREIGN KEY (module_id) REFERENCES modules(module_id) ON DELETE CASCADE;
CREATE INDEX idx_module_items_module_id ON module_items(module_id);

-- module_items.content_id → content.content_id
ALTER TABLE module_items
    ADD CONSTRAINT fk_module_items_content
        FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE SET NULL;
CREATE INDEX idx_module_items_content_id ON module_items(content_id);

-- module_items.quiz_id → quizzes.quiz_id
ALTER TABLE module_items
    ADD CONSTRAINT fk_module_items_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE SET NULL;
CREATE INDEX idx_module_items_quiz_id ON module_items(quiz_id);

-- module_items.assignment_id → assignments.assignment_id
ALTER TABLE module_items
    ADD CONSTRAINT fk_module_items_assignment
        FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id) ON DELETE SET NULL;
CREATE INDEX idx_module_items_assignment_id ON module_items(assignment_id);

-- enrollments.employee_id → employees.employee_id
ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollments_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_enrollments_employee_id ON enrollments(employee_id);

-- enrollments.course_id → courses.course_id
ALTER TABLE enrollments
    ADD CONSTRAINT fk_enrollments_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_enrollments_course_id ON enrollments(course_id);

-- module_progress.enrollment_id → enrollments.enrollment_id
ALTER TABLE module_progress
    ADD CONSTRAINT fk_module_progress_enrollment
        FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE;
CREATE INDEX idx_module_progress_enrollment_id ON module_progress(enrollment_id);

-- module_progress.module_id → modules.module_id
ALTER TABLE module_progress
    ADD CONSTRAINT fk_module_progress_module
        FOREIGN KEY (module_id) REFERENCES modules(module_id) ON DELETE CASCADE;
CREATE INDEX idx_module_progress_module_id ON module_progress(module_id);

-- content_progress.enrollment_id → enrollments.enrollment_id
ALTER TABLE content_progress
    ADD CONSTRAINT fk_content_progress_enrollment
        FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE;
CREATE INDEX idx_content_progress_enrollment_id ON content_progress(enrollment_id);

-- content_progress.content_id → content.content_id
ALTER TABLE content_progress
    ADD CONSTRAINT fk_content_progress_content
        FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE;
CREATE INDEX idx_content_progress_content_id ON content_progress(content_id);

-- quiz_attempts.quiz_id → quizzes.quiz_id
ALTER TABLE quiz_attempts
    ADD CONSTRAINT fk_quiz_attempts_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE;
CREATE INDEX idx_quiz_attempts_quiz_id ON quiz_attempts(quiz_id);

-- quiz_attempts.employee_id → employees.employee_id
ALTER TABLE quiz_attempts
    ADD CONSTRAINT fk_quiz_attempts_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_quiz_attempts_employee_id ON quiz_attempts(employee_id);

-- quiz_answers.attempt_id → quiz_attempts.attempt_id
ALTER TABLE quiz_answers
    ADD CONSTRAINT fk_quiz_answers_attempt
        FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(attempt_id) ON DELETE CASCADE;
CREATE INDEX idx_quiz_answers_attempt_id ON quiz_answers(attempt_id);

-- quiz_answers.question_id → quiz_questions.question_id
ALTER TABLE quiz_answers
    ADD CONSTRAINT fk_quiz_answers_question
        FOREIGN KEY (question_id) REFERENCES quiz_questions(question_id) ON DELETE CASCADE;
CREATE INDEX idx_quiz_answers_question_id ON quiz_answers(question_id);

-- quiz_answers.selected_option → question_options.option_id
ALTER TABLE quiz_answers
    ADD CONSTRAINT fk_quiz_answers_selected_option
        FOREIGN KEY (selected_option) REFERENCES question_options(option_id);
CREATE INDEX idx_quiz_answers_selected_option ON quiz_answers(selected_option);

-- assignment_submissions.assignment_id → assignments.assignment_id
ALTER TABLE assignment_submissions
    ADD CONSTRAINT fk_assignment_submissions_assignment
        FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE;
CREATE INDEX idx_assignment_submissions_assignment_id ON assignment_submissions(assignment_id);

-- assignment_submissions.employee_id → employees.employee_id
ALTER TABLE assignment_submissions
    ADD CONSTRAINT fk_assignment_submissions_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_assignment_submissions_employee_id ON assignment_submissions(employee_id);

-- certificates.enrollment_id → enrollments.enrollment_id
ALTER TABLE certificates
    ADD CONSTRAINT fk_certificates_enrollment
        FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE;
CREATE INDEX idx_certificates_enrollment_id ON certificates(enrollment_id);

-- user_course_statistics.employee_id → employees.employee_id
ALTER TABLE user_course_statistics
    ADD CONSTRAINT fk_user_course_stats_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_user_course_stats_employee_id ON user_course_statistics(employee_id);

-- user_course_statistics.course_id → courses.course_id
ALTER TABLE user_course_statistics
    ADD CONSTRAINT fk_user_course_stats_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_user_course_stats_course_id ON user_course_statistics(course_id);

-- user_course_statistics.enrollment_id → enrollments.enrollment_id
ALTER TABLE user_course_statistics
    ADD CONSTRAINT fk_user_course_stats_enrollment
        FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE;
CREATE INDEX idx_user_course_stats_enrollment_id ON user_course_statistics(enrollment_id);

-- course_forums.course_id → courses.course_id
ALTER TABLE course_forums
    ADD CONSTRAINT fk_course_forums_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_course_forums_course_id ON course_forums(course_id);

-- course_forums.created_by → users.user_id
ALTER TABLE course_forums
    ADD CONSTRAINT fk_course_forums_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT;
CREATE INDEX idx_course_forums_created_by ON course_forums(created_by);

-- forum_threads.forum_id → course_forums.forum_id
ALTER TABLE forum_threads
    ADD CONSTRAINT fk_forum_threads_forum
        FOREIGN KEY (forum_id) REFERENCES course_forums(forum_id) ON DELETE CASCADE;
CREATE INDEX idx_forum_threads_forum_id ON forum_threads(forum_id);

-- forum_threads.employee_id → employees.employee_id
ALTER TABLE forum_threads
    ADD CONSTRAINT fk_forum_threads_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_forum_threads_employee_id ON forum_threads(employee_id);

-- forum_posts.thread_id → forum_threads.thread_id
ALTER TABLE forum_posts
    ADD CONSTRAINT fk_forum_posts_thread
        FOREIGN KEY (thread_id) REFERENCES forum_threads(thread_id) ON DELETE CASCADE;
CREATE INDEX idx_forum_posts_thread_id ON forum_posts(thread_id);

-- forum_posts.employee_id → employees.employee_id
ALTER TABLE forum_posts
    ADD CONSTRAINT fk_forum_posts_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_forum_posts_employee_id ON forum_posts(employee_id);

-- forum_posts.parent_post_id → forum_posts.post_id
ALTER TABLE forum_posts
    ADD CONSTRAINT fk_forum_posts_parent
        FOREIGN KEY (parent_post_id) REFERENCES forum_posts(post_id) ON DELETE CASCADE;
CREATE INDEX idx_forum_posts_parent_id ON forum_posts(parent_post_id);

-- post_reactions.post_id → forum_posts.post_id
ALTER TABLE post_reactions
    ADD CONSTRAINT fk_post_reactions_post
        FOREIGN KEY (post_id) REFERENCES forum_posts(post_id) ON DELETE CASCADE;
CREATE INDEX idx_post_reactions_post_id ON post_reactions(post_id);

-- post_reactions.employee_id → employees.employee_id
ALTER TABLE post_reactions
    ADD CONSTRAINT fk_post_reactions_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_post_reactions_employee_id ON post_reactions(employee_id);

-- thread_tags.thread_id → forum_threads.thread_id
ALTER TABLE thread_tags
    ADD CONSTRAINT fk_thread_tags_thread
        FOREIGN KEY (thread_id) REFERENCES forum_threads(thread_id) ON DELETE CASCADE;
CREATE INDEX idx_thread_tags_thread_id ON thread_tags(thread_id);

-- thread_tags.tag_id → forum_tags.tag_id
ALTER TABLE thread_tags
    ADD CONSTRAINT fk_thread_tags_tag
        FOREIGN KEY (tag_id) REFERENCES forum_tags(tag_id) ON DELETE CASCADE;
CREATE INDEX idx_thread_tags_tag_id ON thread_tags(tag_id);

-- user_roadmaps.employee_id → employees.employee_id
ALTER TABLE user_roadmaps
    ADD CONSTRAINT fk_user_roadmaps_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE;
CREATE INDEX idx_user_roadmaps_employee_id ON user_roadmaps(employee_id);

-- user_roadmaps.created_by → users.user_id
ALTER TABLE user_roadmaps
    ADD CONSTRAINT fk_user_roadmaps_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT;
CREATE INDEX idx_user_roadmaps_created_by ON user_roadmaps(created_by);

-- user_roadmaps.approved_by → users.user_id
ALTER TABLE user_roadmaps
    ADD CONSTRAINT fk_user_roadmaps_approved_by
        FOREIGN KEY (approved_by) REFERENCES users(user_id) ON DELETE SET NULL;
CREATE INDEX idx_user_roadmaps_approved_by ON user_roadmaps(approved_by);

-- user_roadmaps.updated_by → users.user_id
ALTER TABLE user_roadmaps
    ADD CONSTRAINT fk_user_roadmaps_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL;
CREATE INDEX idx_user_roadmaps_updated_by ON user_roadmaps(updated_by);

-- roadmap_training_areas.roadmap_id → user_roadmaps.roadmap_id
ALTER TABLE roadmap_training_areas
    ADD CONSTRAINT fk_roadmap_training_areas_roadmap
        FOREIGN KEY (roadmap_id) REFERENCES user_roadmaps(roadmap_id) ON DELETE CASCADE;
CREATE INDEX idx_roadmap_training_areas_roadmap_id ON roadmap_training_areas(roadmap_id);

-- roadmap_training_areas.area_id → training_areas.area_id
ALTER TABLE roadmap_training_areas
    ADD CONSTRAINT fk_roadmap_training_areas_area
        FOREIGN KEY (area_id) REFERENCES training_areas(area_id) ON DELETE CASCADE;
CREATE INDEX idx_roadmap_training_areas_area_id ON roadmap_training_areas(area_id);

-- roadmap_training_areas.created_by → users.user_id
ALTER TABLE roadmap_training_areas
    ADD CONSTRAINT fk_roadmap_training_areas_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT;
CREATE INDEX idx_roadmap_training_areas_created_by ON roadmap_training_areas(created_by);

-- roadmap_training_areas.updated_by → users.user_id
ALTER TABLE roadmap_training_areas
    ADD CONSTRAINT fk_roadmap_training_areas_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL;
CREATE INDEX idx_roadmap_training_areas_updated_by ON roadmap_training_areas(updated_by);

-- roadmap_area_courses.roadmap_id → user_roadmaps.roadmap_id
ALTER TABLE roadmap_area_courses
    ADD CONSTRAINT fk_roadmap_area_courses_roadmap
        FOREIGN KEY (roadmap_id) REFERENCES user_roadmaps(roadmap_id) ON DELETE CASCADE;
CREATE INDEX idx_roadmap_area_courses_roadmap_id ON roadmap_area_courses(roadmap_id);

-- roadmap_area_courses.area_id → training_areas.area_id
ALTER TABLE roadmap_area_courses
    ADD CONSTRAINT fk_roadmap_area_courses_area
        FOREIGN KEY (area_id) REFERENCES training_areas(area_id) ON DELETE CASCADE;
CREATE INDEX idx_roadmap_area_courses_area_id ON roadmap_area_courses(area_id);

-- roadmap_area_courses.course_id → courses.course_id
ALTER TABLE roadmap_area_courses
    ADD CONSTRAINT fk_roadmap_area_courses_course
        FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE;
CREATE INDEX idx_roadmap_area_courses_course_id ON roadmap_area_courses(course_id);

-- roadmap_area_courses.updated_by → users.user_id
ALTER TABLE roadmap_area_courses
    ADD CONSTRAINT fk_roadmap_area_courses_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL;
CREATE INDEX idx_roadmap_area_courses_updated_by ON roadmap_area_courses(updated_by);

-- roadmap_statistics.roadmap_id → user_roadmaps.roadmap_id
ALTER TABLE roadmap_statistics
    ADD CONSTRAINT fk_roadmap_statistics_roadmap
        FOREIGN KEY (roadmap_id) REFERENCES user_roadmaps(roadmap_id) ON DELETE CASCADE;
CREATE INDEX idx_roadmap_statistics_roadmap_id ON roadmap_statistics(roadmap_id);

-- roadmap_statistics.employee_id → employees.employee_id
ALTER TABLE roadmap_statistics
    ADD CONSTRAINT fk_roadmap_statistics_employee
        FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE RESTRICT;
CREATE INDEX idx_roadmap_statistics_employee_id ON roadmap_statistics(employee_id);

