package com.poliqlo.controllers.admin.san_pham.controller;


import com.poliqlo.controllers.admin.san_pham.model.request.AddRequestNBC;
import com.poliqlo.controllers.admin.san_pham.service.SanPhamService;
import com.poliqlo.models.*;
import com.poliqlo.repositories.SanPhamRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Controller
public class SanPhamController {
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamService sanPhamService;
    @PersistenceContext
    private EntityManager entityManager;
    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
    private final ModelMapper modelMapper;


    @GetMapping("/admin/san-pham")
    public String ui(Model model) {
        return "/admin/san-pham/san-pham";
    }

    @GetMapping("/admin/san-pham/add")
    public String add(Model model) {
        return "/admin/san-pham/add";
    }

    @GetMapping("/admin/san-pham/{id}")
    public String edit(Model model, @PathVariable(name = "id") SanPham sp) {
        model.addAttribute("moTa", sp.getMoTa());

        return "/admin/san-pham/edit-san-pham";
    }

//    @ResponseBody
//    @GetMapping("/api/v1/admin/san-pham/{id}")
//    public ResponseEntity<?> getSanPhamEdit(@PathVariable(name = "id") SanPham sp) {
//        var spEditResp = modelMapper.map(sp, SanPhamEditResponse.class);
//        spEditResp.setCameraSauTinhNangCameraIds(sp.getCameraTruoc()
//                .getTinhNangCameras()
//                .stream()
//                .map(TinhNangCamera::getId)
//                .collect(Collectors.toSet()));
//        spEditResp.setCameraTruocTinhNangCameraIds(sp.getCameraSau()
//                .getTinhNangCameras()
//                .stream()
//                .map(TinhNangCamera::getId)
//                .collect(Collectors.toSet()));
//        spEditResp.setKetNoiWifiIds(sp
//                .getKetNoi()
//                .getWifi()
//                .stream()
//                .map(Wifi::getId)
//                .collect(Collectors.toSet()));
//        spEditResp.setKetNoiGpsIds(sp
//                .getKetNoi()
//                .getGps()
//                .stream()
//                .map(Gps::getId)
//                .collect(Collectors.toSet()));
//        spEditResp.setKetNoiBluetoothIds(sp
//                .getKetNoi()
//                .getBluetooth()
//                .stream()
//                .map(Bluetooth::getId)
//                .collect(Collectors.toSet()));
//        spEditResp.setPinVaSacCongNghePinIds(sp
//                .getPinVaSac()
//                .getCongNghePin()
//                .stream()
//                .map(CongNghePin::getId)
//                .collect(Collectors.toSet()));
//        spEditResp.setKhuyenMaiIds(sp
//                .getKhuyenMai()
//                .stream()
//                .map(KhuyenMai::getId)
//                .collect(Collectors.toList()));
//        spEditResp.setSanPhamChiTietRoms(sp
//                .getSanPhamChiTiet()
//                .stream()
//                .map(SanPhamChiTiet::getRom)
//                .collect(Collectors.toSet())
//        );
//        spEditResp.setSanPhamChiTietMauSacIds(sp
//                .getSanPhamChiTiet()
//                .stream()
//                .map(SanPhamChiTiet::getMauSac)
//                .map(MauSac::getId)
//                .collect(Collectors.toSet())
//        );
//        spEditResp.setThongTinChungTinhNangDacBietIds(sp
//                .getThongTinChung()
//                .getTinhNangDacBiet()
//                .stream()
//                .map(tndb -> tndb.getId())
//                .collect(Collectors.toSet())
//        );
//        spEditResp.setRam(sp.getRam());
//        spEditResp.setThoiGianBaoHanh(sp.getThoiGianBaoHanh());
//
//
//        return ResponseEntity.ok(spEditResp);
//    }
//
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

//    @Transactional
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/{id}")
//    public ResponseEntity<?> edit(@RequestBody SanPhamEditRequest editRequest, @PathVariable(name = "id") SanPham sp) {
//        var spEdit = modelMapper.map(editRequest, SanPham.class);
//        //Đưa ảnh về container chính
//        if (spEdit.getAnh().getId() == null) {
//            var anhSanPhamAddRquest = new Anh();
//            anhSanPhamAddRquest.setName(editRequest.getAnh().getName());
//            anhSanPhamAddRquest.setUrl(imageService.moveImageToPermanent(editRequest.getAnh().getName()));
//            var anhSanPham = anhRepository.save(anhSanPhamAddRquest);
//            spEdit.setAnh(anhSanPham);
//        }
//
//        //Lưu ảnh theo màu sắc sản phẩm chi tiết.
//        Map<Integer, List<Anh>> mapImgByMsId = new HashMap<>();
//        spEdit.getSanPhamChiTiet().forEach(spct -> {
//            if (!mapImgByMsId.containsKey(spct.getMauSac().getId())) {
//                //Chuyển về cùng 1 thực thể
//                List<Anh> lstNewImg=new ArrayList<>();
//                if (spct.getAnh().get(0).getId() == null) {
//                    spct.getAnh().forEach(newImg -> {
//                        newImg.setUrl(imageService.moveImageToPermanent(newImg.getName()));
//                        var anhSanPham = anhRepository.save(newImg);
//                        lstNewImg.add(anhSanPham);
//                    });
//                }else{
//                    lstNewImg.addAll(spct.getAnh());
//                }
//
//                mapImgByMsId.put(spct.getMauSac().getId(), lstNewImg);
//            }
//        });
//        spEdit.getSanPhamChiTiet().forEach(spct -> {
//            var oldSpct=sanPhamChiTietRepository.findById(spct.getId()).orElse(spct);
//            spct.setAnh(mapImgByMsId.get(spct.getMauSac().getId()));
//            spct.setSanPham(sp);
//            spct.setDotGiamGia(oldSpct.getDotGiamGia());
//            spct.setSoLuong(oldSpct.getSoLuong());
//        });
//
//        sanPhamRepository.save(spEdit);
//
//        return ResponseEntity.ok("Save success");
//
//
//    }
//
//    private void optimizeField(Object source, Object target) {
//        if (target == null) {
//        }
//    }
//
//
//    @ResponseBody
//    @GetMapping("/api/v1/san-pham-data-table")
//    public List<SanPhamDataTable> getAll(
//    ) {
//        var spdt = sanPhamRepository.findAllSanPhamDataTable();
//        return spdt;
//    }
//
//    public Collection<SanPham> findAllByDeletedIsFalseOrderByIdDesc() {
//        return null;
//    }
//
//    @ResponseBody
//    @PutMapping("/api/v1/san-pham")
//    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        SanPham cnmh = modelMapper.map(req, SanPham.class);
//        Response cnmhRP = modelMapper.map(sanPhamRepository.save(cnmh), Response.class);
//        response.setStatus(HttpStatus.CREATED.value());
//        return cnmhRP;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham")
//    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        if (editReq.getId() == null || !sanPhamRepository.existsById(editReq.getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        sanPhamRepository.save(modelMapper.map(editReq, SanPham.class));
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return sanPhamRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
//    }
//
//
//    @ResponseBody
//    @DeleteMapping("/api/v1/san-pham")
//    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !sanPhamRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = sanPhamRepository.findById(id).get();
//        cnmhE.setDeleted(true);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        sanPhamRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/revert")
//    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !sanPhamRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = sanPhamRepository.findById(id).get();
//        cnmhE.setDeleted(false);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        sanPhamRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/import-excel")
//    @Transactional
//    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstCNMH, Errors errors) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//        try{
//            List<SanPham> lstMS = lstCNMH.stream()
//                    .map((element) -> modelMapper.map(element, SanPham.class))
//                    .collect(Collectors.toList());
//            List<Response> lstRP = sanPhamRepository.saveAll(lstMS).stream()
//                    .map((element) -> modelMapper.map(element, Response.class))
//                    .collect(Collectors.toList());
//            return lstRP;
//        }
//        catch (Exception e) {
//            log.error("Lỗi import file");
//            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//
//    }
//
//    @GetMapping("/admin/san-pham/export-excel")
//    public ResponseEntity<byte[]> exportToExcel() throws IOException {
//        List<SanPham> mauSacList = sanPhamRepository.findAll();
//        ExportExcel<SanPham> exportExcel = new ExportExcel();
//        ByteArrayInputStream in = exportExcel.export(mauSacList);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=cong_nghe_man_hinh.xlsx");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(in.readAllBytes());
//    }
}
