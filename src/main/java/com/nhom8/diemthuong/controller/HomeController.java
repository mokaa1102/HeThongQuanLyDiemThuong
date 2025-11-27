package com.nhom8.diemthuong.controller;

import com.nhom8.diemthuong.entity.*;
import com.nhom8.diemthuong.repository.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
public class HomeController {

    @Autowired private NguoiDungRepo ndRepo;
    @Autowired private ViDiemRepo viRepo;
    @Autowired private GiaoDichRepo gdRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostConstruct
    public void initAdmin() {
        if (ndRepo.findByTenDangNhap("admin").isEmpty()) {
            NguoiDung admin = new NguoiDung();
            admin.setTenDangNhap("admin");
            admin.setMatKhau(encoder.encode("123456"));
            admin.setHoTen("Quản Trị Viên Nhóm 8");
            admin.setVaiTro("ADMIN");
            ndRepo.save(admin);

            ViDiem vi = new ViDiem();
            vi.setNguoiDung(admin);
            vi.setSoDu(0.0);
            viRepo.save(vi);

            System.out.println("=== ADMIN ĐÃ ĐƯỢC TẠO: admin / 123456 ===");
        }
    }

    @GetMapping({"/", "/dang-nhap"})
    public String dangNhap() { return "dang-nhap"; }

    @PostMapping("/dang-nhap")
    public String xuLyDangNhap(@RequestParam String tenDangNhap, @RequestParam String matKhau, HttpSession session, RedirectAttributes ra) {
        var userOpt = ndRepo.findByTenDangNhap(tenDangNhap);
        if (userOpt.isPresent() && encoder.matches(matKhau, userOpt.get().getMatKhau())) {
            session.setAttribute("user", userOpt.get());
            return "redirect:/trang-chu";
        }
        ra.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
        return "redirect:/dang-nhap";
    }

    @GetMapping("/trang-chu")
    public String trangChu(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) return "redirect:/dang-nhap";
        double soDu = viRepo.findByNguoiDungId(user.getId()).map(ViDiem::getSoDu).orElse(0.0);
        model.addAttribute("user", user);
        model.addAttribute("soDu", soDu);
        return "trang-chu";
    }

    @GetMapping("/nap-diem")
    public String napDiem(HttpSession session, Model model) {NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) return "redirect:/dang-nhap";
        if ("ADMIN".equals(user.getVaiTro())) {
            List<NguoiDung> users = ndRepo.findAll();
            Map<Long, Long> soDuMap = new HashMap<>();
            for (NguoiDung u : users) {double soDuDouble = viRepo.findByNguoiDungId(u.getId()).map(ViDiem::getSoDu).orElse(0.0);
                soDuMap.put(u.getId(), Math.round(soDuDouble));
            }
            model.addAttribute("users", users);
            model.addAttribute("soDuMap", soDuMap);
        }
        return "nap-diem";
    }

    @PostMapping("/nap-diem")
    public String xuLyNapDiemChoUser(@RequestParam String tenDangNhap, @RequestParam double soDiem, @RequestParam(required = false) String moTa, HttpSession session, RedirectAttributes ra) {
        NguoiDung admin = (NguoiDung) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getVaiTro())) {
            ra.addFlashAttribute("error", "Bạn không có quyền thực hiện hành động này");
            return "redirect:/trang-chu";
        }
        if (soDiem <= 0) {
            ra.addFlashAttribute("error", "Số điểm phải lớn hơn 0");
            return "redirect:/nap-diem";
        }
        NguoiDung user = ndRepo.findByTenDangNhap(tenDangNhap).orElse(null);
        if (user == null) {
            ra.addFlashAttribute("error", "Không tìm thấy người dùng: " + tenDangNhap);
            return "redirect:/nap-diem";
        }
        ViDiem vi = viRepo.findByNguoiDungId(user.getId()).orElseGet(() -> {
            ViDiem v = new ViDiem();
            v.setNguoiDung(user);
            v.setSoDu(0.0);
            viRepo.save(v);
            return v;
        });

        vi.setSoDu(vi.getSoDu() + soDiem);
        viRepo.save(vi);

        GiaoDich gd = new GiaoDich();
        gd.setNguoiDung(admin);
        gd.setNguoiGui(admin);
        gd.setNguoiNhan(user);
        gd.setLoai("ADMIN_NAP");
        gd.setSoTien(soDiem);
        gd.setMoTa(moTa != null && !moTa.isBlank() ? moTa : "Admin nạp điểm");
        gd.setNguoiLienQuan(user.getTenDangNhap());
        gdRepo.save(gd);

        ra.addFlashAttribute("success","Nạp thành công " + soDiem + " điểm cho tài khoản: " + tenDangNhap);
        return "redirect:/nap-diem";
    }

    @GetMapping("/chuyen-diem")
    public String chuyenDiemForm(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) return "redirect:/dang-nhap";
        double soDu = viRepo.findByNguoiDungId(user.getId()).map(ViDiem::getSoDu).orElse(0.0);
        model.addAttribute("soDu", soDu);
        return "chuyen-diem";
    }

    @PostMapping("/chuyen-diem")
    public String xuLyChuyen(@RequestParam String nguoiNhan, @RequestParam double soTien, @RequestParam String moTa, HttpSession session, RedirectAttributes ra) {
        NguoiDung gui = (NguoiDung) session.getAttribute("user");
        NguoiDung nhan = ndRepo.findByTenDangNhap(nguoiNhan).orElse(null);

        if (nhan == null) {
            ra.addFlashAttribute("error", "Không tìm thấy người nhận");
            return "redirect:/chuyen-diem";
        }
        if (nhan.getId().equals(gui.getId())) {
            ra.addFlashAttribute("error", "Không thể chuyển cho chính mình");
            return "redirect:/chuyen-diem";
        }

        ViDiem viGui = viRepo.findByNguoiDungId(gui.getId()).orElse(null);
        if (viGui == null || viGui.getSoDu() < soTien) {
            ra.addFlashAttribute("error", "Số dư không đủ");
            return "redirect:/chuyen-diem";
        }

        viGui.setSoDu(viGui.getSoDu() - soTien);
        viRepo.save(viGui);

        ViDiem viNhan = viRepo.findByNguoiDungId(nhan.getId()).orElseGet(() -> {
            ViDiem v = new ViDiem(); v.setNguoiDung(nhan); viRepo.save(v); return v;
        });
        viNhan.setSoDu(viNhan.getSoDu() + soTien);
        viRepo.save(viNhan);

        GiaoDich gd1 = new GiaoDich();
        gd1.setNguoiDung(gui);
        gd1.setNguoiGui(gui);
        gd1.setNguoiNhan(nhan);
        gd1.setLoai("CHUYEN");
        gd1.setSoTien(soTien);
        gd1.setMoTa(moTa);
        gd1.setNguoiLienQuan(nhan.getTenDangNhap());
        gdRepo.save(gd1);

        GiaoDich gd2 = new GiaoDich();
        gd2.setNguoiDung(nhan);
        gd2.setNguoiGui(gui);
        gd2.setNguoiNhan(nhan);
        gd2.setLoai("NHAN");
        gd2.setSoTien(soTien);
        gd2.setMoTa("Nhận từ " + gui.getTenDangNhap());
        gd2.setNguoiLienQuan(gui.getTenDangNhap());
        gdRepo.save(gd2);

        ra.addFlashAttribute("success", "Chuyển thành công " + soTien + " điểm cho " + nguoiNhan + "!");
        return "redirect:/trang-chu";
    }

    @GetMapping("/lich-su")
    public String lichSu(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null) {
            return "redirect:/dang-nhap";
        }

        List<GiaoDich> ds;

        if ("ADMIN".equals(user.getVaiTro())) {
            ds = gdRepo.findAllByOrderByThoiGianDesc();
            model.addAttribute("isAdminView", true);
            model.addAttribute("title", "LỊCH SỬ GIAO DỊCH - TOÀN HỆ THỐNG");
        } else {
            ds = gdRepo.findByNguoiDungIdOrderByThoiGianDesc(user.getId());
            model.addAttribute("isAdminView", false);
            model.addAttribute("title", "LỊCH SỬ GIAO DỊCH CỦA BẠN");
        }

        model.addAttribute("ds", ds);
        return "lich-su";
    }

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getVaiTro())) {
            return "redirect:/trang-chu";
        }
        List<NguoiDung> users = ndRepo.findAll();
        double tongDiemDouble = viRepo.findAll().stream().mapToDouble(v -> v.getSoDu()).sum();
        long tongDiem = Math.round(tongDiemDouble);
        long tongGD = gdRepo.count();

        Map<Long, Long> soDuMap = new HashMap<>();
        for (NguoiDung u : users) {
            double soDuDouble = viRepo.findByNguoiDungId(u.getId()).map(ViDiem::getSoDu).orElse(0.0);
            soDuMap.put(u.getId(), Math.round(soDuDouble));
        }

        model.addAttribute("users", users);
        model.addAttribute("tongDiem", tongDiem);
        model.addAttribute("tongGD", tongGD);
        model.addAttribute("soDuMap", soDuMap);
        return "admin/dashboard";
    }

    @GetMapping("/admin/tao-nguoi-dung")
    public String formTaoNguoiDung(HttpSession session, RedirectAttributes ra) {
        NguoiDung user = (NguoiDung) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getVaiTro())) return "redirect:/trang-chu";
        return "admin/tao-nguoi-dung";
    }

    @PostMapping("/admin/tao-nguoi-dung")
    public String xuLyTaoNguoiDung(@RequestParam String tenDangNhap, @RequestParam String matKhau, @RequestParam String hoTen, @RequestParam(defaultValue = "USER") String vaiTro, HttpSession session, RedirectAttributes ra) {
        NguoiDung admin = (NguoiDung) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getVaiTro())) return "redirect:/trang-chu";

        if (ndRepo.findByTenDangNhap(tenDangNhap).isPresent()) {
            ra.addFlashAttribute("error", "Người dùng đã tồn tại");
            return "redirect:/admin/tao-nguoi-dung";
        }

        NguoiDung newUser = new NguoiDung();
        newUser.setTenDangNhap(tenDangNhap);
        newUser.setMatKhau(encoder.encode(matKhau));
        newUser.setHoTen(hoTen);
        newUser.setVaiTro(vaiTro);
        ndRepo.save(newUser);

        ViDiem vi = new ViDiem();
        vi.setNguoiDung(newUser);
        vi.setSoDu(0.0);
        viRepo.save(vi);

        GiaoDich log = new GiaoDich();
        log.setNguoiDung(newUser);
        log.setNguoiGui(admin);
        log.setLoai("TAO_TAI_KHOAN");
        log.setSoTien(0);
        log.setMoTa("Tài khoản được tạo bởi Admin");
        log.setNguoiLienQuan("Admin: " + admin.getTenDangNhap());
        gdRepo.save(log);

        ra.addFlashAttribute("success", "Tạo thành công: " + tenDangNhap);
        return "redirect:/admin";
    }

    @GetMapping("/admin/tru-diem/{userId}")
    public String formTruDiem(@PathVariable Long userId, HttpSession session, Model model, RedirectAttributes ra) {
        NguoiDung admin = (NguoiDung) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getVaiTro())) return "redirect:/trang-chu";

        NguoiDung nguoiDung = ndRepo.findById(userId).orElse(null);
        if (nguoiDung == null) return "redirect:/admin";

        double soDu = viRepo.findByNguoiDungId(userId).map(ViDiem::getSoDu).orElse(0.0);
        model.addAttribute("nguoiDung", nguoiDung);
        model.addAttribute("soDuHienTai", Math.round(soDu));
        return "admin/tru-diem";
    }

    @PostMapping("/admin/tru-diem/{userId}")
    public String xuLyTruDiem(@PathVariable Long userId, @RequestParam double soTien, @RequestParam String moTa, HttpSession session, RedirectAttributes ra) {
        NguoiDung admin = (NguoiDung) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getVaiTro())) return "redirect:/trang-chu";
        if (soTien <= 0) {
            ra.addFlashAttribute("error", "Số điểm phải lớn hơn 0");
            return "redirect:/admin/tru-diem/" + userId;
        }

        NguoiDung nguoiBiTru = ndRepo.findById(userId).orElse(null);
        if (nguoiBiTru == null) {
            ra.addFlashAttribute("error", "Người dùng không tồn tại");
            return "redirect:/admin";
        }

        ViDiem vi = viRepo.findByNguoiDungId(userId).orElse(null);
        if (vi == null || vi.getSoDu() < soTien) {
            ra.addFlashAttribute("error", "Số dư không đủ để trừ");
            return "redirect:/admin/tru-diem/" + userId;
        }

        vi.setSoDu(vi.getSoDu() - soTien);
        viRepo.save(vi);

        GiaoDich gd = new GiaoDich();
        gd.setNguoiDung(nguoiBiTru);
        gd.setNguoiGui(admin);
        gd.setNguoiNhan(nguoiBiTru);
        gd.setLoai("TRU_DIEM");
        gd.setSoTien(soTien);
        gd.setMoTa(moTa.isEmpty() ? "Bị trừ điểm bởi Admin" : moTa);
        gd.setNguoiLienQuan("Admin: " + admin.getTenDangNhap());
        gdRepo.save(gd);

        ra.addFlashAttribute("success", "Đã trừ " + soTien + " điểm khỏi " + nguoiBiTru.getTenDangNhap());
        return "redirect:/admin";
    }

    @GetMapping("/admin/danh-sach-nguoi-dung")
    public String danhSachNguoiDung(HttpSession session, Model model) {
        NguoiDung currentUser = (NguoiDung) session.getAttribute("user");
        if (currentUser == null || !"ADMIN".equals(currentUser.getVaiTro())) {
            return "redirect:/dang-nhap";
        }
        List<NguoiDung> users = ndRepo.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        return "admin/danh-sach-nguoi-dung";
    }

    @GetMapping("/admin/xoa-nguoi-dung/{id}")
    @Transactional
    public String xoaNguoiDung(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        NguoiDung admin = (NguoiDung) session.getAttribute("user");
        if (admin == null || !"ADMIN".equals(admin.getVaiTro())) {
            ra.addFlashAttribute("error", "Không có quyền");
            return "redirect:/dang-nhap";
        }

        NguoiDung user = ndRepo.findById(id).orElse(null);
        if (user == null) {
            ra.addFlashAttribute("error", "Không tìm thấy người dùng");
            return "redirect:/admin/danh-sach-nguoi-dung";
        }
        if (user.getId().equals(admin.getId())) {
            ra.addFlashAttribute("error", "Không thể tự xóa chính mình");
            return "redirect:/admin/danh-sach-nguoi-dung";
        }

        GiaoDich log = new GiaoDich();
        log.setNguoiDung(user);
        log.setNguoiGui(admin);
        log.setLoai("XOA_TAI_KHOAN");
        log.setSoTien(0);
        log.setMoTa("Tài khoản bị xóa bởi Admin");
        log.setNguoiLienQuan(admin.getTenDangNhap());
        gdRepo.save(log);

        gdRepo.deleteAllByNguoiDungId(user.getId());
        viRepo.findByNguoiDungId(user.getId()).ifPresent(viRepo::delete);
        ndRepo.delete(user);
        
        ra.addFlashAttribute("success", "Đã xóa thành công người dùng: " + user.getTenDangNhap());
        return "redirect:/admin/danh-sach-nguoi-dung";
    }

    @GetMapping("/dang-xuat")
    public String dangXuat(HttpSession session) {
        session.invalidate();
        return "redirect:/dang-nhap";
    }
}
