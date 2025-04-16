-- --------------------------------------------------------
-- Tập lệnh khởi tạo dữ liệu cho cơ sở dữ liệu StaffEdu
-- --------------------------------------------------------

-- Đặt SERI của các bảng để bắt đầu từ 1000
ALTER SEQUENCE roles_role_id_seq RESTART WITH 1000;
ALTER SEQUENCE users_user_id_seq RESTART WITH 1000;
ALTER SEQUENCE departments_department_id_seq RESTART WITH 1000;
ALTER SEQUENCE employees_employee_id_seq RESTART WITH 1000;
ALTER SEQUENCE employee_hierarchy_hierarchy_id_seq RESTART WITH 1000;
ALTER SEQUENCE course_domains_domain_id_seq RESTART WITH 1000;
ALTER SEQUENCE course_statuses_status_id_seq RESTART WITH 1000;
ALTER SEQUENCE instructors_instructor_id_seq RESTART WITH 1000;
ALTER SEQUENCE courses_course_id_seq RESTART WITH 1000;
ALTER SEQUENCE training_levels_level_id_seq RESTART WITH 1000;
ALTER SEQUENCE skills_skill_id_seq RESTART WITH 1000;

-- --------------------------------------------------------
-- 1. Khởi tạo dữ liệu cho Người dùng và Phân quyền
-- --------------------------------------------------------

-- Roles
INSERT INTO roles (role_name, description) VALUES
('ADMIN', 'Quản trị viên hệ thống với toàn quyền truy cập'),
('MANAGER', 'Quản lý phòng ban, có quyền quản lý nhân viên và khóa học'),
('EMPLOYEE', 'Nhân viên thông thường, có thể tham gia khóa học'),
('INSTRUCTOR', 'Giảng viên, có thể tạo và quản lý các khóa học');

-- Departments (chưa có manager_id, sẽ cập nhật sau)
INSERT INTO departments (department_name, description) VALUES
('Ban Giám đốc', 'Ban lãnh đạo công ty'),
('Phòng Nhân sự', 'Quản lý nhân sự và đào tạo'),
('Phòng Công nghệ', 'Phát triển công nghệ và sản phẩm'),
('Phòng Marketing', 'Quản lý marketing và truyền thông'),
('Phòng Kế toán', 'Quản lý tài chính và kế toán');

-- Users (Mật khẩu mã hóa cần được thay đổi trong môi trường thực tế)
INSERT INTO users (username, password, email, role_id, is_active) VALUES
('admin', '$2a$10$dDA4Hw.1UaAzQSH4KR6Z..vxNyGj8WHFRaJ5QuRvYMRUjHvZyT.Iy', 'admin@staffedu.com', 1000, TRUE),
('nguyenvana', '$2a$10$dDA4Hw.1UaAzQSH4KR6Z..vxNyGj8WHFRaJ5QuRvYMRUjHvZyT.Iy', 'nguyenvana@staffedu.com', 1001, TRUE),
('tranthib', '$2a$10$dDA4Hw.1UaAzQSH4KR6Z..vxNyGj8WHFRaJ5QuRvYMRUjHvZyT.Iy', 'tranthib@staffedu.com', 1001, TRUE),
('levanc', '$2a$10$dDA4Hw.1UaAzQSH4KR6Z..vxNyGj8WHFRaJ5QuRvYMRUjHvZyT.Iy', 'levanc@staffedu.com', 1002, TRUE),
('phamthid', '$2a$10$dDA4Hw.1UaAzQSH4KR6Z..vxNyGj8WHFRaJ5QuRvYMRUjHvZyT.Iy', 'phamthid@staffedu.com', 1002, TRUE),
('hoangvane', '$2a$10$dDA4Hw.1UaAzQSH4KR6Z..vxNyGj8WHFRaJ5QuRvYMRUjHvZyT.Iy', 'hoangvane@staffedu.com', 1003, TRUE);

-- Employees
INSERT INTO employees (user_id, first_name, last_name, department_id, position, hire_date, status) VALUES
(1000, 'Admin', 'System', 1000, 'System Administrator', '2023-01-01', 'active'),
(1001, 'Nguyễn Văn', 'A', 1000, 'Giám đốc', '2023-01-01', 'active'),
(1002, 'Trần Thị', 'B', 1001, 'Trưởng phòng Nhân sự', '2023-01-15', 'active'),
(1003, 'Lê Văn', 'C', 1002, 'Trưởng phòng Công nghệ', '2023-02-01', 'active'),
(1004, 'Phạm Thị', 'D', 1002, 'Nhân viên IT', '2023-03-01', 'active'),
(1005, 'Hoàng Văn', 'E', 1003, 'Giảng viên', '2023-03-15', 'active');

-- Cập nhật manager_id cho departments
UPDATE departments SET manager_id = 1001 WHERE department_id = 1000;
UPDATE departments SET manager_id = 1002 WHERE department_id = 1001;
UPDATE departments SET manager_id = 1003 WHERE department_id = 1002;
UPDATE departments SET manager_id = 1001 WHERE department_id = 1003;
UPDATE departments SET manager_id = 1001 WHERE department_id = 1004;

-- Employee Hierarchy
INSERT INTO employee_hierarchy (manager_id, employee_id) VALUES
(1001, 1002),
(1001, 1003),
(1003, 1004),
(1002, 1005);

-- --------------------------------------------------------
-- 2. Khởi tạo dữ liệu cho Quản lý Khóa học
-- --------------------------------------------------------

-- Course Domains
INSERT INTO course_domains (domain_name, description, parent_domain_id) VALUES
('Công nghệ thông tin', 'Các khóa học về CNTT', NULL),
('Phát triển phần mềm', 'Khóa học về lập trình và phát triển phần mềm', 1000),
('Quản trị mạng', 'Khóa học về quản trị mạng và hệ thống', 1000),
('Kỹ năng mềm', 'Các khóa học về kỹ năng mềm', NULL),
('Lãnh đạo', 'Khóa học về kỹ năng lãnh đạo', 1003),
('Giao tiếp', 'Khóa học về kỹ năng giao tiếp', 1003);

-- Course Statuses
INSERT INTO course_statuses (status_name, description) VALUES
('Active', 'Khóa học đang hoạt động'),
('Draft', 'Khóa học đang soạn thảo'),
('Archived', 'Khóa học đã lưu trữ'),
('Upcoming', 'Khóa học sắp mở');

-- Instructors
INSERT INTO instructors (first_name, last_name, bio, email, phone, profile_pic_url) VALUES
('Hoàng Văn', 'E', 'Chuyên gia về công nghệ thông tin với hơn 10 năm kinh nghiệm', 'hoangvane@staffedu.com', '0123456789', '/images/instructors/hoangvane.jpg'),
('Đỗ Thị', 'F', 'Chuyên gia về kỹ năng lãnh đạo và quản lý', 'dothif@staffedu.com', '0987654321', '/images/instructors/dothif.jpg'),
('Lý Văn', 'G', 'Chuyên gia về phát triển phần mềm', 'lyvang@staffedu.com', '0123123123', '/images/instructors/lyvang.jpg');

-- Training Levels
INSERT INTO training_levels (level_name, description) VALUES
('Beginner', 'Dành cho người mới bắt đầu'),
('Intermediate', 'Dành cho người đã có kiến thức cơ bản'),
('Advanced', 'Dành cho người đã có kinh nghiệm'),
('Expert', 'Dành cho chuyên gia');

-- Skills
INSERT INTO skills (skill_name, description) VALUES
('Java', 'Lập trình Java'),
('SQL', 'Quản trị cơ sở dữ liệu SQL'),
('Spring Boot', 'Framework Spring Boot'),
('Leadership', 'Kỹ năng lãnh đạo'),
('Communication', 'Kỹ năng giao tiếp'),
('Problem Solving', 'Kỹ năng giải quyết vấn đề'),
('Project Management', 'Quản lý dự án');

-- Domain Skills
INSERT INTO domain_skills (domain_id, skill_id) VALUES
(1001, 1000), -- Phát triển phần mềm - Java
(1001, 1001), -- Phát triển phần mềm - SQL
(1001, 1002), -- Phát triển phần mềm - Spring Boot
(1004, 1003), -- Lãnh đạo - Leadership
(1005, 1004); -- Giao tiếp - Communication

-- Courses
INSERT INTO courses (title, description, duration, content_description, learning_outcomes, course_domain_id, status_id, thumbnail_url, instructor_id) VALUES
('Spring Boot cơ bản', 'Khóa học về Spring Boot cho người mới bắt đầu', 40, 'Nội dung chi tiết về Spring Boot bao gồm các module cơ bản', 'Hiểu và áp dụng Spring Boot để xây dựng ứng dụng web', 1001, 1000, '/images/courses/spring-boot.jpg', 1000),
('Java nâng cao', 'Khóa học Java chuyên sâu', 60, 'Nội dung chi tiết về Java nâng cao và các design pattern', 'Thành thạo Java và có thể xây dựng ứng dụng phức tạp', 1001, 1000, '/images/courses/java-advanced.jpg', 1002),
('Kỹ năng lãnh đạo', 'Khóa học về kỹ năng lãnh đạo và quản lý nhóm', 30, 'Nội dung chi tiết về các kỹ năng lãnh đạo hiệu quả', 'Có khả năng quản lý nhóm và lãnh đạo hiệu quả', 1004, 1000, '/images/courses/leadership.jpg', 1001),
('SQL cho người mới bắt đầu', 'Khóa học cơ bản về SQL', 20, 'Nội dung chi tiết về SQL và quản trị cơ sở dữ liệu', 'Hiểu và viết được các câu lệnh SQL cơ bản', 1001, 1003, '/images/courses/sql-basics.jpg', 1002);

-- Course Skills
INSERT INTO course_skills (course_id, skill_id, training_level_id) VALUES
(1000, 1002, 1000), -- Spring Boot cơ bản - Spring Boot - Beginner
(1000, 1000, 1000), -- Spring Boot cơ bản - Java - Beginner
(1001, 1000, 1002), -- Java nâng cao - Java - Advanced
(1002, 1003, 1001), -- Kỹ năng lãnh đạo - Leadership - Intermediate
(1002, 1004, 1001), -- Kỹ năng lãnh đạo - Communication - Intermediate
(1003, 1001, 1000); -- SQL cho người mới bắt đầu - SQL - Beginner

-- Course Prerequisites
INSERT INTO course_prerequisites (course_id, prerequisite_course_id) VALUES
(1000, 1001); -- Spring Boot cơ bản yêu cầu Java nâng cao