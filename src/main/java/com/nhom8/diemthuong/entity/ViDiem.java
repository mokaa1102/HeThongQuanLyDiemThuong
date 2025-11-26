package com.nhom8.diemthuong.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vi_diem")
public class ViDiem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "nguoi_dung_id")
    private NguoiDung nguoiDung;
    private double soDu = 0.0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public NguoiDung getNguoiDung() { return nguoiDung; }
    public void setNguoiDung(NguoiDung nguoiDung) { this.nguoiDung = nguoiDung; }
    public double getSoDu() { return soDu; }
    public void setSoDu(double soDu) { this.soDu = soDu; }
}