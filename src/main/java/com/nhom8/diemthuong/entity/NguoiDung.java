package com.nhom8.diemthuong.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nguoi_dung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String tenDangNhap;
    @Column(nullable = false)
    private String matKhau;
    private String hoTen;
    @Column(nullable = false)
    private String vaiTro = "USER";
    @OneToOne(mappedBy = "nguoiDung", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ViDiem viDiem;
    @OneToMany(mappedBy = "nguoiGui", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GiaoDich> giaoDichGui = new ArrayList<>();
    @OneToMany(mappedBy = "nguoiNhan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GiaoDich> giaoDichNhan = new ArrayList<>();
    @Column(name = "trang_thai", nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'HOAT_DONG'")
    private String trangThai = "HOAT_DONG";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public ViDiem getViDiem() { return viDiem; }
    public void setViDiem(ViDiem viDiem) { this.viDiem = viDiem; }
    public List<GiaoDich> getGiaoDichGui() { return giaoDichGui; }
    public void setGiaoDichGui(List<GiaoDich> giaoDichGui) { this.giaoDichGui = giaoDichGui; }
    public List<GiaoDich> getGiaoDichNhan() { return giaoDichNhan; }
    public void setGiaoDichNhan(List<GiaoDich> giaoDichNhan) { this.giaoDichNhan = giaoDichNhan; }
}