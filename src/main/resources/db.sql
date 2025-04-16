-- -------------------------------
-- 1. Tạo Bảng Quản Lý Người Dùng và Phân Quyền
-- -------------------------------

-- Tạo bảng: roles
CREATE TABLE roles (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng: users
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- Tạo bảng: departments
CREATE TABLE departments (
    department_id BIGSERIAL PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    manager_id BIGINT,  -- Sẽ thêm ràng buộc sau
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    -- Ràng buộc khóa ngoại tới employees sẽ được thêm sau
);

-- Tạo bảng: employees
CREATE TABLE employees (
    employee_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    department_id BIGINT NOT NULL,
    position VARCHAR(100),
    hire_date DATE,
    status VARCHAR CHECK (status IN ('active', 'inactive', 'on_leave')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- Thêm ràng buộc khóa ngoại cho departments.manager_id sau khi employees đã được tạo
ALTER TABLE departments
ADD CONSTRAINT fk_departments_manager
FOREIGN KEY (manager_id) REFERENCES employees(employee_id);

-- Tạo bảng: employee_hierarchy
CREATE TABLE employee_hierarchy (
    hierarchy_id BIGSERIAL PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Tạo bảng: activity_logs
CREATE TABLE activity_logs (
    log_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR CHECK (activity_type IN ('login', 'logout', 'view_profile', 'update_profile', 'other')),
    ip_address VARCHAR(45),  -- Hỗ trợ IPv4 và IPv6
    session_id VARCHAR(100),
    description TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- -------------------------------
-- 2. Tạo Bảng Quản Lý Khóa Học và Liên Kết
-- -------------------------------

-- Tạo bảng: course_domains
CREATE TABLE course_domains (
    domain_id BIGSERIAL PRIMARY KEY,
    domain_name VARCHAR NOT NULL,
    description TEXT,
    parent_domain_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_domain_id) REFERENCES course_domains(domain_id)
);

-- Tạo bảng: course_statuses
CREATE TABLE course_statuses (
    status_id BIGSERIAL PRIMARY KEY,
    status_name VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng: instructors
CREATE TABLE instructors (
    instructor_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    bio TEXT,
    email VARCHAR NOT NULL UNIQUE,
    phone VARCHAR,
    profile_pic_url VARCHAR,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng: courses
CREATE TABLE courses (
    course_id BIGSERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description TEXT,  -- Mô tả tóm tắt hoặc giới thiệu khoá học
    duration INTEGER,  -- Thời lượng khoá học (theo giờ, phút,…)
    content_description TEXT,  -- Mô tả chi tiết nội dung đào tạo của khoá học
    learning_outcomes TEXT,  -- Kết quả học tập/mục tiêu đạt được sau khi hoàn thành
    course_domain_id BIGINT,
    status_id BIGINT,
    thumbnail_url VARCHAR,
    instructor_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_domain_id) REFERENCES course_domains(domain_id),
    FOREIGN KEY (status_id) REFERENCES course_statuses(status_id),
    FOREIGN KEY (instructor_id) REFERENCES instructors(instructor_id)
);

-- Tạo bảng: course_prerequisites
CREATE TABLE course_prerequisites (
    course_id BIGINT NOT NULL,
    prerequisite_course_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (course_id, prerequisite_course_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (prerequisite_course_id) REFERENCES courses(course_id)
);

-- Tạo bảng: training_levels
CREATE TABLE training_levels (
    level_id BIGSERIAL PRIMARY KEY,
    level_name VARCHAR NOT NULL,  -- Ví dụ: 'Beginner', 'Intermediate', 'Advanced', 'Expert'
    description TEXT,  -- Mô tả chung về mức đào tạo
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng: skills
CREATE TABLE skills (
    skill_id BIGSERIAL PRIMARY KEY,
    skill_name VARCHAR NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng: course_skills
CREATE TABLE course_skills (
    course_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    training_level_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (course_id, skill_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id),
    FOREIGN KEY (training_level_id) REFERENCES training_levels(level_id)
);

-- Tạo bảng: domain_skills
CREATE TABLE domain_skills (
    domain_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (domain_id, skill_id),
    FOREIGN KEY (domain_id) REFERENCES course_domains(domain_id),
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);

-- -------------------------------
-- 3. Cập Nhật Các Bảng Với Các Ràng Buộc Bổ Sung (Nếu Cần)
-- -------------------------------
-- Ở đây, chúng ta đã thêm tất cả các ràng buộc khóa ngoại khi tạo bảng. Nếu có thêm ràng buộc nào khác, bạn có thể thêm dưới dạng ALTER TABLE.

-- -------------------------------
-- 4. Tạo Các Trigger Để Cập Nhật Trường updated_at
-- -------------------------------

-- PostgreSQL không hỗ trợ 'ON UPDATE CURRENT_TIMESTAMP' trực tiếp, nên chúng ta sẽ sử dụng trigger để tự động cập nhật trường 'updated_at'.

-- Tạo hàm trigger
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Tạo các trigger cho từng bảng có trường updated_at
DO $$
DECLARE
    tbl text;
BEGIN
    -- Loop qua danh sách các bảng cần trigger
    FOREACH tbl IN ARRAY ARRAY[
        'roles', 
        'users', 
        'departments', 
        'employees', 
        'employee_hierarchy', 
        'activity_logs', 
        'instructors', 
        'courses', 
        'course_prerequisites', 
        'course_skills', 
        'domain_skills'
    ] LOOP
        -- Kiểm tra nếu trigger chưa tồn tại để tránh lỗi khi chạy lại script
        IF NOT EXISTS (
            SELECT 1 
            FROM pg_trigger 
            WHERE tgname = 'trigger_' || tbl || '_updated_at'
        ) THEN
            EXECUTE format(
                'CREATE TRIGGER trigger_%I_updated_at
                 BEFORE UPDATE ON %I
                 FOR EACH ROW
                 EXECUTE FUNCTION update_updated_at_column();',
                tbl,
                tbl
            );
        END IF;
    END LOOP;
END;
$$;

