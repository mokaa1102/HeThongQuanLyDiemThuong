### 1. Thông tin chung dự án
- Tên dự án: Hệ thống quản lý điểm thưởng
- Phân công thành viên:
| STT | Thành viên      | Mã sinh viên | Vai trò            | Công việc được giao                                                     |
|-----------------------|--------------|--------------------|-------------------------------------------------------------------------|
| 1   | Nguyễn Năng Huy | K24DTCN201   | Backend Lead +     | 1. Hoàn thiện backend (HomeController, Repository, Entity)              |
|     |                 |              | Database + DevOps  | 2. Xử lý lỗi transaction, xóa user thật + giữ lịch sử                   |
|     |                 |              | Frontend Lead +    | 1. Thiết kế lại giao diện toàn bộ (trang chủ, lịch sử, admin dashboard) |
|     |                 |              | UI/UX + Presenter  | 2. Responsive 100% (mobile + tablet + PC)                               |
|     |                 |              |                    | 3. Thêm phân trang (pagination) cho lịch sử giao dịch                   |
|     |                 |              | Business Analyst + | 1. Tối ưu database                                                      |
|     |                 |              | Document + Tester  | 2. Viết test case + kịch bản test                                       |
|     |                 |              |                    | 3. Viết báo cáo phần Kiến trúc hệ thống + Cơ sở dữ liệu + Triển khai    |
  
- Công nghệ & Framework sử dụng:

| Thành phần                      | Công nghệ sử dụng                          | Phiên bản |
|---------------------------------|--------------------------------------------|-----------|
| Back-end                        | Spring Boot 3.x                            | 3.2.x+    |
| Web MVC & Template Engine       | Spring MVC + Thymeleaf                     | -         |
| Cơ sở dữ liệu                   | MariaDB                                    | 10.11.13  |
| Object-Relational Mapping (ORM) | Spring Data JPA (Hibernate)                | -         |
| Front-end                       | HTML5, CSS3, Bootstrap 5                   | 5.3+      |
| Build & Dependencies            | Maven                                      | 3.8.7     |
| Embedded                        | Spring Boot Embedded Tomcat                | -         |
| Quản lý session                 | HttpSession (Session-based authentication) | -         |

### 2. Mô tả tính năng:
- Đối với người dùng (USER):
    - Đăng nhập / Đăng xuất an toàn (mã hóa mật khẩu bằng BCrypt)
    - Xem số dư điểm hiện tại
    - Nạp điểm (giả lập thanh toán)
    - Chuyển điểm cho người dùng khác bằng tên đăng nhập
    - Xem lịch sử giao dịch chi tiết (nạp, chuyển, nhận, trừ)
- Đối với quản trị viên (ADMIN):
    - Dashboard tổng quan: tổng điểm, tổng giao dịch, danh sách người dùng
    - Tạo tài khoản người dùng mới
    - Nạp điểm trực tiếp cho bất kỳ người dùng nào (trang /nap-diem)
    - Trừ điểm người dùng (có xác nhận và ghi log)
    - Xóa tài khoản người dùng (không cho tự xóa chính mình)
    - Xem số dư và thông tin tất cả người dùng dưới dạng bảng, có định dạng tiền tệ (1,000 điểm)

### Công cụ phát triển & hỗ trợ:
- IDE: IntelliJ IDEA Community, VSCode
- Database client: MySQL Workbench, MySQL Shell for VSCode
- Quản lý source code: Git & GitHub
- Build tool: Apache Maven
- Server: Spring Boot Embedded Tomcat (không cần cài Tomcat riêng)
- OS: Ubuntu Server 24.04

### Cấu trúc thư mục:
````
HeThongQuanLyDiemThuong/
├── README.md
├── Tạo database MariaDB.md
├── database_full_data.sql
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── nhom8
        │           └── diemthuong
        │               ├── DiemThuongApplication.java
        │               ├── entity
        │               │   ├── GiaoDich.java
        │               │   ├── NguoiDung.java
        │               │   └── ViDiem.java
        │               └── repository
        │                   ├── GiaoDichRepo.java
        │                   ├── NguoiDungRepo.java
        │                   └── ViDiemRepo.java
        └── resources
            ├── application.properties
            ├── application.yml
            ├── static
            │   ├── css
            │   │   └── style.css
            │   └── img
            │       └── bg.jpg
            └── templates
                ├── admin
                │   ├── danh-sach-nguoi-dung.html
                │   ├── dashboard.html
                │   ├── tao-nguoi-dung.html
                │   └── tru-diem.html
                ├── chuyen-diem.html
                ├── dang-nhap.html
                ├── lich-su.html
                ├── nap-diem.html
                └── trang-chu.html
````
## Cài đặt MariaDB:
```bash
sudo apt install mariadb-server mariadb-client -y
sudo systemctl enable mariadb
sudo systemctl start mariadb
sudo systemctl status mariadb
```

## Bảo mật máy chủ MariaDB bằng mật khẩu gốc, xóa người dùng ẩn danh và vô hiệu hóa quyền truy cập từ xa cho người dùng gốc
```bash
sudo mysql_secure_installation
```

## Tạo Database:
```bash
sudo mysql -u root -e "CREATE DATABASE diemthuong CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

### Tạo bảng và admin hệ thống:
```bash
sudo mysql -u root -p diemthuong < database_full_data.sql
```
- User/Passwd mặc định: admin / 123456

## Cài đặt JDK 17
```bash
sudo apt install openjdk-17-jdk -y
java --version
```

## Cài đặt Apache Maven:
```bash
sudo apt install maven -y
mvn -version
```

## Chạy chương trình:
```bash
cd HeThongQuanLyDiemThuong
mvn clean spring-boot:run
```

## (Optional) Cài đặt thêm `tree` để kiểm tra cấu trúc thư mục chương trình
```bash
sudo apt install tree
```

## Truy cập từ web:
- http://ip_addr:8080
## Lưu ý:
- Chương trình chưa được tối ưu nên thường gặp tình trạng thoát chương trình (Ctrl + C) nhưng không giải phóng bộ nhớ RAM, xử lý tạm thời bằng câu lệnh:
```bash
pkill -f java
```
