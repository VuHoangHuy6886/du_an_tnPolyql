package com.poliqlo.controllers.admin.san_pham.controller;

import com.poliqlo.controllers.admin.san_pham.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham.model.response.Response;
import com.poliqlo.controllers.admin.san_pham.service.SanPhamService;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response.ProductDetailDTO;
import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SanPhamController {
    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);

    private final ModelMapper modelMapper;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamService sanPhamService;
    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private KieuDangRepository kieuDangRepository;
    @Autowired
    private DanhMucRepository danhMucRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    // Các phương thức khác...
    @ResponseBody
    @PostMapping("/api/v1/san-pham")
    public ResponseEntity<?> updateSanPham(@RequestBody EditReq dto) {
        SanPham updated = sanPhamService.updateSanPham(dto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }
    }
    // API trả về danh sách sản phẩm chi tiết cho 1 sản phẩm
    @GetMapping("/api/v1/san-pham/{id}/details")
    public ResponseEntity<List<ProductDetailDTO>> getProductDetails(@PathVariable("id") Integer id) {
        List<ProductDetailDTO> details = sanPhamChiTietRepository.findProductDetailsBySanPhamId(id);
        return ResponseEntity.ok(details);
    }
    // GET: Hiển thị trang thêm mới sản phẩm
    @GetMapping("admin/san-pham/add")
    public String showAddForm(Model model) {
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.findAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.findAll();
        List<KieuDang> listKieuDang = kieuDangRepository.findAll();

        model.addAttribute("listThuongHieu", listThuongHieu);
        model.addAttribute("listChatLieu", listChatLieu);
        model.addAttribute("listKieuDang", listKieuDang);
        return "admin/san-pham/add-san-pham";
    }

    // POST: Xử lý thêm mới sản phẩm

    @PostMapping("admin/san-pham/add")
    public String addSanPham(String maSanPham, String tenSanPham,
                             String trangThai, String moTa,
                             String anhUrl, // sau khi upload file, tên ảnh được lưu qua hidden input
                             List<Integer> thuongHieu, List<Integer> mauSac, List<Integer> kieuDang,
                             RedirectAttributes redirectAttributes) {
        SanPham sp = new SanPham();
        sp.setMaSanPham(maSanPham);
        sp.setTen(tenSanPham);
        sp.setTrangThai(trangThai);
        sp.setMoTa(moTa);
        sp.setAnhUrl(anhUrl);
        sp.setIsDeleted(false);

        // Vì entity SanPham định nghĩa quan hệ ManyToOne nên chỉ lưu giá trị đầu tiên từ danh sách select
        if (thuongHieu != null && !thuongHieu.isEmpty()) {
            ThuongHieu th = thuongHieuRepository.findById(thuongHieu.get(0)).orElse(null);
            sp.setIdThuongHieu(th);
        }
        if (mauSac != null && !mauSac.isEmpty()) {
            ChatLieu ms = chatLieuRepository.findById(mauSac.get(0)).orElse(null);
            sp.setIdChatLieu(ms);
        }
        if (kieuDang != null && !kieuDang.isEmpty()) {
            KieuDang kd = kieuDangRepository.findById(kieuDang.get(0)).orElse(null);
            sp.setIdKieuDang(kd);
        }

        sanPhamRepository.save(sp);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công!");
        return "redirect:/admin/san-pham/add";
    }
    // Hiển thị giao diện quản lý sản phẩm
    @GetMapping("/admin/san-pham")
    public String ui(Model model) {
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.findAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.findAll();
        List<DanhMuc> listDanhMuc = danhMucRepository.findAll();
        List<KieuDang> listKieuDang = kieuDangRepository.findAll();
        model.addAttribute("listThuongHieu", listThuongHieu);
        model.addAttribute("listChatLieu", listChatLieu);
        model.addAttribute("listDanhMuc", listDanhMuc);
        model.addAttribute("listKieuDang", listKieuDang);
        return "/admin/san-pham/san-pham";
    }

    // API trả về dữ liệu cho DataTables
    @ResponseBody
    @GetMapping("/api/v1/san-pham-table")
    public List<Response> getDataTable() {
        return sanPhamRepository.findAllSanPham();
    }

    // API lấy danh sách sản phẩm (nếu cần)
    @ResponseBody
    @GetMapping("/api/v1/san-pham")
    public List<Response> getAll() {
        return modelMapper.map(sanPhamRepository.findAll(), new TypeToken<List<Response>>() {}.getType());
    }

    // Thêm mới sản phẩm (PUT)
    @ResponseBody
    @PutMapping("/api/v1/san-pham")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return sanPhamService.save(req);
    }

    // Cập nhật sản phẩm (POST)
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham")
//    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
//        return sanPhamService.update(editReq);
//    }

    // Xóa sản phẩm
    @ResponseBody
    @DeleteMapping("/api/v1/san-pham")
    public ResponseEntity<SanPham> delete(@RequestParam(name = "id") Integer id) {
        return sanPhamService.delete(id);
    }

    // Khôi phục sản phẩm
    @ResponseBody
    @PostMapping("/api/v1/san-pham/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return sanPhamService.revert(id);
    }

    // Import file Excel
    @ResponseBody
    @PostMapping("/api/v1/san-pham/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstChatLieu) {
        return sanPhamService.importExcel(lstChatLieu);
    }

    // Export Excel
    @GetMapping("/admin/san-pham/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return sanPhamService.exportToExcel();
    }
}
