package com.poliqlo.controllers.admin.ban_hang.controller;


import com.poliqlo.controllers.admin.ban_hang.repository.impl.SanPhamRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BanHangController {
    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;
//    private final MaGiamGiaRepository maGiamGiaRepository;
//    private final ImeiRepository imeiRepository;
//    private final SanPhamChiTietRepository sanPhamChiTietRepository;
//    private final KhachHangRepository khachHangRepository;
//    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;
//    private final IBanHangService banHangService;
//
//    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
//    private final ModelMapper modelMapper;
//    private final SanPhamRepository sanPhamRepository;
//    private final ImageService imageService;
//    private final AnhRepository anhRepository;

    @GetMapping("/admin/sale")
    public String sale(Model model) {
        model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "admin/ban-hang/ban-hang";
    }

//    @PutMapping("/admin/sale")
//    @ResponseBody
//    @Transactional
//    public ResponseEntity<?> newInvoice(@RequestBody HoaDonTaiQuayAddRequest req) {
////        model.addAttribute("username",SecurityContextHolder.getContext().getAuthentication().getName());
//
//
//        return banHangService.save(req);
//    }
//
//    @GetMapping("/api/v1/sale/product")
//    public ResponseEntity<?> getAll(
//            @RequestParam(required = false, defaultValue = "") String searchKey,
//            @RequestParam(required = false, defaultValue = "") Integer[] seriesFilter,
//            @RequestParam(required = false, defaultValue = "id:desc") String orderBy,
//            @RequestParam(required = false, defaultValue = "0") Integer pageNo
//    ) {
//        return ResponseEntity.ok(sanPhamRepositoryImpl.findAllSanPhamDataTableBanHang(searchKey, seriesFilter, orderBy, PageRequest.of(pageNo, 12)));
//    }
//
//    @GetMapping("/api/v1/sale/product/{id}")
//    public ResponseEntity<?> getSanPhamById(
//            @PathVariable(name = "id") SanPham sanPham
//    ) {
//        var spResponse = modelMapper.map(sanPham, SanPhamProductResponse.class);
//        return ResponseEntity.ok(spResponse);
//    }
//
//    @GetMapping("/api/v1/sale/promotion/{id}")
//    @ResponseBody
//    public ResponseEntity<?> getPromotion(
//            @PathVariable(name = "id", required = false) MaGiamGia promotion
//    ) {
//        if (promotion == null) {
//            return ResponseEntity.ok(null);
//        }
//        var spResponse = modelMapper.map(promotion, MaGiamGiaDto.class);
//        return ResponseEntity.ok(spResponse);
//    }
//
//    @GetMapping("/api/v1/sale/customer/{id}")
//    @ResponseBody
//    public ResponseEntity<?> getCustomer(
//            @PathVariable(name = "id") KhachHang customer
//    ) {
//        var spResponse = modelMapper.map(customer, CustomerDto.class);
//        return ResponseEntity.ok(spResponse);
//    }
//
//
//    @GetMapping("/api/v1/sale/promotion")
//    @ResponseBody
//    public ResponseEntity<?> getAllPromotion(
//            @RequestParam(name = "page", required = false) Integer page,
//            @RequestParam(name = "pageSize", required = false) Integer pageSize,
//            @RequestParam(name = "code", required = false) Optional<String> code
//    ) {
//        Pageable pageAble = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
//        Page<MaGiamGia> promotions;
//        if (code.isPresent()) {
//            promotions = maGiamGiaRepository.findByCodeLike("%" + code.get() + "%", pageAble);
//        } else {
//            promotions = maGiamGiaRepository.findAll(pageAble);
//        }
//        PromotionResponseSelect2 response = new PromotionResponseSelect2();
//        response.setPagination(new PromotionResponseSelect2.Pagination(!promotions.isLast()));
//        var result = promotions.getContent().stream().map(voucher -> new PromotionResponseSelect2.PromotionResponse(voucher.getId(), voucher.getCode())).collect(Collectors.toList());
//        response.setResults(result);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/api/v1/sale/customer")
//    @ResponseBody
//    public ResponseEntity<?> getAllKhachHang(
//            @RequestParam(name = "page", required = false) Integer page,
//            @RequestParam(name = "pageSize", required = false) Integer pageSize,
//            @RequestParam(name = "code", required = false) Optional<String> code
//    ) {
//        Pageable pageAble = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "ten"));
//        Page<KhachHang> customers;
//        if (code.isPresent()) {
//            customers = khachHangRepository.findByEmailLikeOrSoDienThoaiLike("%" + code.get() + "%", code.get(), pageAble);
//        } else {
//            customers = khachHangRepository.findAll(pageAble);
//        }
//        CustomerResponseSelect2 response = new CustomerResponseSelect2();
//        response.setPagination(new CustomerResponseSelect2.Pagination(!customers.isLast()));
//        var result = customers.getContent().stream().map(customer -> new CustomerResponseSelect2.CustomerResponse(customer.getId(), customer.getTen())).collect(Collectors.toList());
//        response.setResults(result);
//        return ResponseEntity.ok(response);
//    }
//
//
//    @ResponseBody
//    @GetMapping("/api/v1/sale/check-imei")
//    public ResponseEntity<?> checkImei(
//            @RequestParam(name = "imei") Optional<Imei> imei,
//            @RequestParam(name = "imei") Optional<String> ime,
//            @RequestParam(name = "spctid") Integer spctId
//    ) {
//        if (!ime.isPresent() || ime.get().equals("")) {
//            return ResponseEntity.status(202).body("Vui lòng không để trống IMEI");
//        }
//        if (imei.isPresent()) {
//            if (imei.get().getTrangThai() == Imei.TrangThai.CHO_BAN) {
//                if (imeiRepository.findFirstByTrangThaiLike(Imei.TrangThai.TRONG_KHO) == null) {
//                    return ResponseEntity.status(202).body("IMEI Sản phẩm đã bán");
//                } else {
//                    var newImei = imeiRepository.findFirstByTrangThaiLike(Imei.TrangThai.TRONG_KHO);
//                    newImei.setTrangThai(Imei.TrangThai.CHO_BAN);
//                    imeiRepository.save(newImei);
//                    var imei2 = imei.get();
//                    imei2.setTrangThai(Imei.TrangThai.TRONG_KHO);
//                    imeiRepository.save(imei2);
//                    return ResponseEntity.ok("Valid");
//                }
//            }
//            if (Objects.equals(imei.get().getSanPhamChiTiet().getId(), spctId) && imei.get().getTrangThai() == Imei.TrangThai.TRONG_KHO) {
//                return ResponseEntity.ok("Valid");
//            }
//            if (imei.get().getTrangThai() != Imei.TrangThai.TRONG_KHO) {
//                return ResponseEntity.status(202).body("IMEI Sản phẩm đã bán");
//            }
//            if (!Objects.equals(imei.get().getSanPhamChiTiet().getId(), spctId)) {
//                return ResponseEntity.status(202).body("IMEI Không thuộc sản phẩm này");
//            }
//        }
//        return ResponseEntity.status(201).body("IMEI Không trùng khớp với sản phẩm nào");
//    }
//
//    @ResponseBody
//    @GetMapping("/api/v1/sale/new-imei")
//    public ResponseEntity<?> checkImei(
//            @RequestParam(name = "imei") String newImei,
//            @RequestParam(name = "spctid") Integer spctId) {
//        Imei imei = new Imei();
//        imei.setImei(newImei);
//
//        imei.setTrangThai(Imei.TrangThai.TRONG_KHO);
//        var oldSPCT = sanPhamChiTietRepository.findById(spctId).get();
//        if (oldSPCT.getTrangThai() == SanPhamRepository.TrangThai.IN_STOCK) {
//            oldSPCT.setSoLuong(oldSPCT.getSoLuong() + 1);
//            SanPhamChiTiet spct = sanPhamChiTietRepository.save(oldSPCT);
//            imei.setSanPhamChiTiet(spct);
//            var nImei = imeiRepository.save(imei);
//            return ResponseEntity.ok(nImei);
//        }
//        return ResponseEntity.badRequest().build();
//
//
//    }

}
