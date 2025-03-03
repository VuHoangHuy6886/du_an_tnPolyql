package com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.service;

import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response.Response;
import com.poliqlo.models.ChatLieu;
import com.poliqlo.repositories.ChatLieuRepository;
import com.poliqlo.utils.ExportExcel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatLieuService {
    private final ModelMapper modelMapper;
    private final ChatLieuRepository chatLieuRepository;


    public ResponseEntity<Response> save(@Valid AddRequest req) {
        ChatLieu chatLieu = modelMapper.map(req, ChatLieu.class);
        Response chatLieuResponse = modelMapper.map(chatLieuRepository.save(chatLieu), Response.class);
        return new ResponseEntity<>(chatLieuResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<Response> update(@Valid EditReq editReq) {
        chatLieuRepository.save(modelMapper.map(editReq, ChatLieu.class));
        var resp= chatLieuRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<ChatLieu> delete(Integer id) {
        if (id == null || !chatLieuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id này");
        }
        var chatLieuEditRequest = chatLieuRepository.findById(id).get();
        chatLieuEditRequest.setIsDeleted(true);
        var chatLieu = modelMapper.map(chatLieuEditRequest, Response.class);
        var resp=chatLieuRepository.save(chatLieuEditRequest);

        return ResponseEntity.accepted().body(resp);
    }

    public ResponseEntity<?> revert(Integer id) {
        if (id == null || !chatLieuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var chatLieuEditRequest = chatLieuRepository.findById(id).get();
        chatLieuEditRequest.setIsDeleted(false);
        var chatLieu = modelMapper.map(chatLieuEditRequest, Response.class);
        chatLieuRepository.save(chatLieuEditRequest);
        return ResponseEntity.accepted().body(chatLieu);
    }

    @Transactional
    public ResponseEntity<?> importExcel(@Valid List<@Valid ImportReq> lstChatLieu) {

        try{
            List<ChatLieu> lstMS = lstChatLieu.stream()
                    .map((element) -> modelMapper.map(element, ChatLieu.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = chatLieuRepository.saveAll(lstMS).stream()
                    .map((element) -> modelMapper.map(element, Response.class))
                    .collect(Collectors.toList());
            return ResponseEntity.accepted().body(lstRP);
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
    }

    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<ChatLieu> mauSacList = chatLieuRepository.findAll();
        ExportExcel<ChatLieu> exportExcel = new ExportExcel<ChatLieu> ();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=chat_lieu.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
