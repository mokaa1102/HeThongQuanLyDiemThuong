package com.nhom8.diemthuong.repository;

import com.nhom8.diemthuong.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NguoiDungRepo extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);
    Optional<NguoiDung> findByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);
    List<NguoiDung> findAllByOrderByIdAsc();
    List<NguoiDung> findByVaiTroOrderByIdAsc(String vaiTro);
    long countByVaiTro(String vaiTro);
    @Query("SELECT n FROM NguoiDung n LEFT JOIN FETCH n.viDiem WHERE n.id = :id")
    Optional<NguoiDung> findByIdWithViDiem(@Param("id") Long id);
    @Query("SELECT n FROM NguoiDung n LEFT JOIN FETCH n.viDiem ORDER BY n.id")
    List<NguoiDung> findAllWithViDiem();
}