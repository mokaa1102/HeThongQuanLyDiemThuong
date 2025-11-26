package com.nhom8.diemthuong.repository;

import com.nhom8.diemthuong.entity.GiaoDich;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GiaoDichRepo extends JpaRepository<GiaoDich, Long> {
    List<GiaoDich> findByNguoiDungIdOrderByThoiGianDesc(Long nguoiDungId);
    List<GiaoDich> findAllByOrderByThoiGianDesc();
    void deleteAllByNguoiDungId(Long nguoiDungId);
}