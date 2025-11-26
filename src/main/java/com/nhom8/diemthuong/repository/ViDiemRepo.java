package com.nhom8.diemthuong.repository;

import com.nhom8.diemthuong.entity.ViDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ViDiemRepo extends JpaRepository<ViDiem, Long> {
    Optional<ViDiem> findByNguoiDungId(Long nguoiDungId);
}