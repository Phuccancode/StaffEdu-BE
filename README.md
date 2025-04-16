# StaffEdu - Web Adventure

Hệ thống quản lý nhân viên và khóa học đào tạo.

## Tóm tắt các thành phần đã tạo

### 1. Entity Classes
- `Role`: Lưu thông tin về vai trò người dùng
- `User`: Lưu thông tin đăng nhập và xác thực
- `Department`: Lưu thông tin về phòng ban
- `Employee`: Lưu thông tin về nhân viên
- `Instructor`: Lưu thông tin về giảng viên
- `CourseDomain`: Lưu thông tin về lĩnh vực khóa học
- `CourseStatus`: Lưu thông tin về trạng thái khóa học
- `Course`: Lưu thông tin về khóa học

### 2. Repository Interfaces
- `UserRepository`: Repository cho User entity
- `DepartmentRepository`: Repository cho Department entity
- `EmployeeRepository`: Repository cho Employee entity
- `InstructorRepository`: Repository cho Instructor entity
- `CourseDomainRepository`: Repository cho CourseDomain entity
- `CourseStatusRepository`: Repository cho CourseStatus entity
- `CourseRepository`: Repository cho Course entity

### 3. DTO (Data Transfer Objects)
- `EmployeeDTO`: DTO cho Employee
- `CourseDTO`: DTO cho Course

### 4. Service Interfaces và Implementations
- `EmployeeService` và `EmployeeServiceImpl`: Service cho Employee
- `CourseService` và `CourseServiceImpl`: Service cho Course

### 5. Controllers
- `EmployeeController`: REST Controller cho Employee
- `CourseController`: REST Controller cho Course

### 6. Exception Handling
- `GlobalExceptionHandler`: Xử lý ngoại lệ toàn cục
- `ResourceNotFoundException`: Ngoại lệ khi không tìm thấy tài nguyên
- `ResourceAlreadyExistsException`: Ngoại lệ khi tài nguyên đã tồn tại

### 7. Security Configuration
- `SecurityConfig`: Cấu hình Spring Security để cho phép truy cập API endpoints

## Các API Endpoints đã tạo

### Employee API Endpoints
- `GET /api/employees`: Lấy tất cả nhân viên
- `GET /api/employees/{id}`: Lấy nhân viên theo ID
- `POST /api/employees`: Tạo nhân viên mới
- `PUT /api/employees/{id}`: Cập nhật thông tin nhân viên
- `DELETE /api/employees/{id}`: Xóa nhân viên
- `GET /api/employees/search?keyword=xxx`: Tìm kiếm nhân viên theo từ khóa
- `GET /api/employees/department/{departmentId}`: Lấy nhân viên theo phòng ban

### Course API Endpoints
- `GET /api/courses`: Lấy tất cả khóa học
- `GET /api/courses/{id}`: Lấy khóa học theo ID
- `POST /api/courses`: Tạo khóa học mới
- `PUT /api/courses/{id}`: Cập nhật thông tin khóa học
- `DELETE /api/courses/{id}`: Xóa khóa học
- `GET /api/courses/search?keyword=xxx`: Tìm kiếm khóa học theo từ khóa
- `GET /api/courses/domain/{domainId}`: Lấy khóa học theo lĩnh vực
- `GET /api/courses/instructor/{instructorId}`: Lấy khóa học theo giảng viên
- `GET /api/courses/status/{statusId}`: Lấy khóa học theo trạng thái
- `POST /api/courses/{courseId}/prerequisites/{prerequisiteId}`: Thêm điều kiện tiên quyết
- `DELETE /api/courses/{courseId}/prerequisites/{prerequisiteId}`: Xóa điều kiện tiên quyết

## Cấu hình
Ứng dụng sử dụng PostgreSQL làm cơ sở dữ liệu.

## Cách chạy ứng dụng
1. Cài đặt PostgreSQL và tạo database `web_adventure`
2. Chạy script SQL trong file `db.sql` để tạo schema và dữ liệu ban đầu
3. Cập nhật thông tin kết nối database trong `application.properties`
