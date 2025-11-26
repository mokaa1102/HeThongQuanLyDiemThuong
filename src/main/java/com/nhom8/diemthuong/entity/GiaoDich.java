package com.nhom8.diemthuong.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "giao_dich")
public class GiaoDich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dung_id")
    private NguoiDung nguoiDung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_gui_id")
    private NguoiDung nguoiGui;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_nhan_id")
    private NguoiDung nguoiNhan;

    @Column(name = "thoi_gian", nullable = false, updatable = false)
    private LocalDateTime thoiGian = LocalDateTime.now();

    @Column(nullable = false)
    private String loai;

    @Column(nullable = false)
    private double soTien;

    private String moTa;
    private String nguoiLienQuan;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }

    public NguoiDung getNguoiGui() { return nguoiGui; }
    public void setNguoiGui(NguoiDung nguoiGui) { this.nguoiGui = nguoiGui; }

    public NguoiDung getNguoiNhan() { return nguoiNhan; }
    public void setNguoiNhan(NguoiDung nguoiNhan) { this.nguoiNhan = nguoiNhan; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public double getSoTien() { return soTien; }
    public void setSoTien(double soTien) { this.soTien = soTien; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getNguoiLienQuan() { return nguoiLienQuan; }
    public void setNguoiLienQuan(String nguoiLienQuan) { this.nguoiLienQuan = nguoiLienQuan; }

    public LocalDateTime getThoiGian() { return thoiGian; }
    public void setThoiGian(LocalDateTime thoiGian) {this.thoiGian = thoiGian != null ? thoiGian : LocalDateTime.now();}
}