CREATE TABLE nguoidung (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) UNIQUE NOT NULL,
    mat_khau VARCHAR(255) NOT NULL,
    ho_ten VARCHAR(100),
    email VARCHAR(100),
    vai_tro ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

CREATE TABLE vidiem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nguoi_dung_id BIGINT UNIQUE NOT NULL,
    so_du DECIMAL(15,2) DEFAULT 0.00,
    ngay_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nguoi_dung_id) REFERENCES nguoidung(id) ON DELETE CASCADE
);

CREATE TABLE giaodich (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vi_nguon_id BIGINT,
    vi_dich_id BIGINT,
    loai VARCHAR(20) NOT NULL, -- 'NAP', 'CHUYEN', 'NHAN'
    so_diem DECIMAL(15,2) NOT NULL,
    mo_ta VARCHAR(255),
    ngay_giao_dich DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vi_nguon_id) REFERENCES vidiem(id),
    FOREIGN KEY (vi_dich_id) REFERENCES vidiem(id)
);

INSERT INTO nguoidung (ten_dang_nhap, mat_khau, ho_ten, email, vai_tro) VALUES
('admin',       '$2a$10$Q8z2p0fKTKa8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z8z', 'Quan tri vien', 'admin@nhom8.com', 'ADMIN');

