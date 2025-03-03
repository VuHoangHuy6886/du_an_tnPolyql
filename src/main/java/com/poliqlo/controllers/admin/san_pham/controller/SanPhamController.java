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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SanPhamController {
    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
//    private static final Logger logger = LoggerFactory.getLogger(SanPhamController.class);
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
//    @ResponseBody
//    @PostMapping("/san-pham/update")
//    public ResponseEntity<?> updateSanPham(@RequestBody EditReq dto) {
//        SanPham updated = sanPhamService.updateSanPham(dto);
//        if (updated != null) {
//            return ResponseEntity.ok(updated);
//        } else {
//            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
//        }
//    }
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
    public List<Response> findAllSanPhamResponse() {
        List<Object[]> results = sanPhamRepository.findAllSanPham(); // Gọi phương thức findAllSanPham()
        List<Response> responses = new ArrayList<>();

        for (Object[] row : results) {
            Response respone = new Response();
            respone.setId((Integer) row[0]);
            respone.setMaSanPham((String) row[1]);
            respone.setTenSanPham((String) row[2]);
            respone.setThuongHieu((String) row[3]);
            respone.setChatLieu((String) row[4]);
            respone.setKieuDang((String) row[5]);
            respone.setDanhMuc((String) row[6]);
            respone.setAnhUrl((String) row[7]);
            respone.setSoLuong((Long) row[8]);
            respone.setTrangThai((String) row[9]);
//            Response response = new Response(
//                    ((Long) row[0]).intValue(),         // sp.id (Long to Integer)
//                    (String) row[1],          // sp.maSanPham
//                    (String) row[2],          // sp.ten
//                    (String) row[3],          // th.ten
//                    (String) row[4],          // cl.ten
//                    (String) row[5],          // kd.ten
//                    (String) row[6],          // dm.ten (GROUP_CONCAT)
//                    (String) row[7],          // sp.anhUrl
//                    ((Long) row[8]).intValue(),         // soLuong (Long to Integer)
//                    (String) row[9]          // sp.trangThai
//            );
            responses.add(respone);
        }

        return responses;
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

    @ResponseBody
    @PostMapping("/api/v1/san-pham/update")
    public ResponseEntity<?> updateSanPham(@Valid @RequestBody EditReq request) {
        try {
            boolean isUpdated = sanPhamService.updateSanPham(request);
            if (isUpdated) {
                return ResponseEntity.ok("Cập nhật sản phẩm thành công!");
            }
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại hoặc cập nhật thất bại!");
        } catch (Exception ex) {
            log.error("Lỗi cập nhật sản phẩm với id {}: {}", request.getId(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi cập nhật sản phẩm!");
        }
    }


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
