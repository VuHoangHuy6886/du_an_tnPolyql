package com.poliqlo.controllers.admin.san_pham.controller;

import com.poliqlo.controllers.admin.san_pham.model.reponse.AddProductDetailRequest;
import com.poliqlo.controllers.admin.san_pham.model.reponse.UpdateProductDetailDTO;
import com.poliqlo.controllers.admin.san_pham.model.request.AddRequestNBC;
import com.poliqlo.controllers.admin.san_pham.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham.model.request.EditRequestNBC;
import com.poliqlo.controllers.admin.san_pham.model.response.Response;
import com.poliqlo.controllers.admin.san_pham.service.SanPhamService;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response.ProductDetailDTO;

import com.poliqlo.models.*;
import com.poliqlo.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class SanPhamController {
    private final ModelMapper modelMapper;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamService sanPhamService;
    private final ThuongHieuRepository thuongHieuRepository;

    private final ChatLieuRepository chatLieuRepository;

    private final KieuDangRepository kieuDangRepository;
    private final KichThuocRepository kichThuocRepository;
    private final MauSacRepository mauSacRepository;

    private final DanhMucRepository danhMucRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @PersistenceContext
    private EntityManager entityManager;



    @GetMapping("/admin/san-pham/add")
    public String add(Model model) {
        return "/admin/san-pham/add";
    }
    @GetMapping("/admin/order_detail")
    public String adda(Model model) {
        return "/admin/don_hang/order_detail";
    }

    @GetMapping("/admin/san-pham/{id}")
    public String edit(Model model, @PathVariable(name = "id") SanPham sp) {
        model.addAttribute("moTa", sp.getMoTa());

        return "/admin/san-pham/edit-san-pham";
    }
    @ResponseBody
    @PutMapping("/api/v1/san-pham")
    public ResponseEntity<?> addNew(@Validated @RequestBody AddRequestNBC addRequestNBC, BindingResult bindResult) {
        if(bindResult.hasErrors()){
            String errorMessages = bindResult.getAllErrors().stream()
                    .map(er-> ((DefaultMessageSourceResolvable) er.getArguments()[0]).getDefaultMessage()+ " " +er.getDefaultMessage())
                    .reduce("", (e1, e2) -> e1 + "\n" + e2);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessages);

        }
        return sanPhamService.persist(addRequestNBC);
    }
    @ResponseBody
    @PostMapping("/api/v1/san-pham/{id}")
    public ResponseEntity<?> update(@Validated @RequestBody EditRequestNBC editReq, @PathVariable(name = "id") Integer  id, BindingResult bindResult) {
        if(bindResult.hasErrors()){
            String errorMessages = bindResult.getAllErrors().stream()
                    .map(er-> ((DefaultMessageSourceResolvable) er.getArguments()[0]).getDefaultMessage()+ " " +er.getDefaultMessage())
                    .reduce("", (e1, e2) -> e1 + "\n" + e2);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessages);

        }
        return sanPhamService.update(editReq,id);
    }
    // Các phương thức khác...
    // API trả về danh sách sản phẩm chi tiết cho 1 sản phẩm
    @GetMapping("/api/v1/san-pham/{id}/details")
    public ResponseEntity<List<ProductDetailDTO>> getProductDetails(@PathVariable("id") Integer id) {
        List<ProductDetailDTO> details = sanPhamChiTietRepository.findProductDetailsBySanPhamId(id);
        return ResponseEntity.ok(details);
    }
    // Hiển thị giao diện quản lý sản phẩm
    @GetMapping("/admin/san-pham")
    public String ui(Model model) {
        List<ThuongHieu> listThuongHieu = thuongHieuRepository.findAll();
        List<ChatLieu> listChatLieu = chatLieuRepository.findAll();
        List<DanhMuc> listDanhMuc = danhMucRepository.findAll();
        List<KieuDang> listKieuDang = kieuDangRepository.findAll();
        List<MauSac> listMauSac = mauSacRepository.findAll();
        List<KichThuoc> listKichThuoc = kichThuocRepository.findAll();
        model.addAttribute("listThuongHieu", listThuongHieu);
        model.addAttribute("listChatLieu", listChatLieu);
        model.addAttribute("listDanhMuc", listDanhMuc);
        model.addAttribute("listKieuDang", listKieuDang);
        model.addAttribute("listMauSac", listMauSac);
        model.addAttribute("listKichThuoc", listKichThuoc);
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

    @ResponseBody
    @PostMapping("/api/v1/san-pham-detail")
    public ResponseEntity<?> addProductDetail(@Valid @RequestBody AddProductDetailRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .reduce("", (a, b) -> a + "\n" + b);
            return ResponseEntity.badRequest().body(errorMessages);
        }

        SanPham sanPham = sanPhamRepository.findById(request.getSanPhamId())
                .orElse(null);
        if (sanPham == null) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại");
        }

        boolean exists = sanPhamChiTietRepository.existsBySanPhamIdAndKichThuocIdAndMauSacId(
                request.getSanPhamId(), request.getKichThuocId(), request.getMauSacId());
        if (exists) {
            return ResponseEntity.badRequest().body("Sản phẩm chi tiết đã tồn tại, vui lòng kiểm tra lại");
        }

        SanPhamChiTiet newDetail = new SanPhamChiTiet();
        newDetail.setSanPham(sanPham);

        KichThuoc kichThuoc = kichThuocRepository.findById(request.getKichThuocId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kích thước không hợp lệ"));
        MauSac mauSac = mauSacRepository.findById(request.getMauSacId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Màu sắc không hợp lệ"));

        newDetail.setKichThuoc(kichThuoc);
        newDetail.setMauSac(mauSac);
        newDetail.setSoLuong(request.getSoLuong());
        newDetail.setGiaBan(request.getGiaBan()); // Gán giá bán từ payload
        newDetail.setBarcode(request.getBarcode()); // Gán giá bán từ payload

        // Nếu có các thuộc tính khác như barcode, có thể thêm tương ứng

        sanPhamChiTietRepository.save(newDetail);

        return ResponseEntity.ok("Thêm sản phẩm chi tiết thành công!");
    }


    @ResponseBody
    @PutMapping("/api/san-pham/update")
    public ResponseEntity<?> updateSanPham(@Valid @RequestBody EditReq request) {
        try {
            boolean isUpdated = sanPhamService.updateSanPham(request);
            if (isUpdated) {
                return ResponseEntity.ok("Cập nhật sản phẩm thành công!");
            }
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại hoặc cập nhật thất bại!");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi cập nhật sản phẩm!");
        }
    }
    @ResponseBody
    @PostMapping("/api/san-pham-detail/update")
    public ResponseEntity<?> updateProductDetail(@RequestBody UpdateProductDetailDTO dto) {
        // Validate các trường
        if (dto.getGiaBan() == null || dto.getGiaBan().compareTo(BigDecimal.valueOf(1000)) <= 0) {
            return ResponseEntity.badRequest().body("Giá bán phải lớn hơn 1000");
        }
        if (dto.getSoLuong() == null || dto.getSoLuong() < 0) {
            return ResponseEntity.badRequest().body("Số lượng phải lớn hơn hoặc bằng 0");
        }

        // Tìm sản phẩm chi tiết theo id
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với id: " + dto.getId()));

        // Cập nhật các trường
        spct.setGiaBan(dto.getGiaBan());
        spct.setSoLuong(dto.getSoLuong());

        // Lưu entity
        sanPhamChiTietRepository.save(spct);

        return ResponseEntity.ok(spct);
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



    // Export Excel
    @GetMapping("/admin/san-pham/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return sanPhamService.exportToExcel();
    }
    @ResponseBody
    @DeleteMapping("/api/san-pham/delete")
    public ResponseEntity<String> stopSelling(@RequestParam Integer id) {
        boolean updated = sanPhamService.updateIsDelete(id, true);
        if (updated) {
            return ResponseEntity.ok("Sản phẩm đã ngừng bán");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm");
    }

    // Bán lại sản phẩm (Cập nhật isDelete = false)
    @PostMapping("/api/san-pham/revert")
    public ResponseEntity<String> resumeSelling(@RequestParam Integer id) {
        boolean updated = sanPhamService.updateIsDelete(id, false);
        if (updated) {
            return ResponseEntity.ok("Sản phẩm đã được bán lại");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sản phẩm");
    }


}
