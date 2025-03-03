package com.poliqlo.controllers.admin.san_pham.controller;


import com.poliqlo.controllers.admin.san_pham.model.reponse.DataList;
import com.poliqlo.controllers.admin.san_pham.model.reponse.ResponseDataList;
import com.poliqlo.controllers.admin.san_pham.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.service.ChatLieuService;
import com.poliqlo.models.DanhMuc;
import com.poliqlo.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/data-list-add-san-pham")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietDataListController {


    SanPhamController sanPhamController;
    ModelMapper modelMapper;
    private final ChatLieuService chatLieuService;
    private final ChatLieuRepository chatLieuRepository;
    private final DanhMucRepository danhMucRepository;
    private final KichThuocRepository kichThuocRepository;
    private final KieuDangRepository kieuDangRepository;
    private final MauSacRepository mauSacRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final SanPhamRepository sanPhamRepository;


    @GetMapping("/{id}")
    public ResponseEntity<?> getSanPhamTemplate(@PathVariable(name = "id") Integer id) {
        var sp = sanPhamRepository.findById(id);
        var sanPhamTemplate = sp
                .map((element) -> modelMapper.map(element, AddRequest.class)).orElse(null);
        assert sanPhamTemplate != null;
        sanPhamTemplate.setDanhMucIds(sp.get().getDanhMucs().stream().map(DanhMuc::getId).collect(Collectors.toSet()));
        return ResponseEntity.ok(sanPhamTemplate);
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        var sanPhams=sanPhamRepository.findAllDataList();
        var chatLieus=chatLieuRepository.findAll().stream().map(e->new DataList(e.getId(),e.getTen())).toList();
        var danhMucs=danhMucRepository.findAll().stream().map(e->new DataList(e.getId(),e.getTen())).toList();
        var kichThuocs=kichThuocRepository.findAll().stream().map(e->new DataList(e.getId(),e.getTen())).toList();
        var kieuDangs=kieuDangRepository.findAll().stream().map(e->new DataList(e.getId(),e.getTen())).toList();
        var mauSacs=mauSacRepository.findAll().stream().map(e->new DataList(e.getId(),e.getTen()+":"+e.getCode())).toList();
        var thuongHieus=thuongHieuRepository.findAll().stream().map(e->new DataList(e.getId(),e.getTen())).toList();
        var code = sanPhamRepository.findAllCode();
        var resp=ResponseDataList.builder()
                .sanPham(sanPhams)
                .chatLieu(chatLieus)
                .danhMuc(danhMucs)
                .kichThuoc(kichThuocs)
                .kieuDang(kieuDangs)
                .mauSac(mauSacs)
                .thuongHieu(thuongHieus)
                .code(code)
                .build();

        return ResponseEntity.ok(resp);
    }




}

