package com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.controller;



import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.request.AddRequest;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.request.EditReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.request.ImportReq;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.model.response.Response;
import com.poliqlo.controllers.admin.san_pham_chi_tiet.chat_lieu.service.ChatLieuService;
import com.poliqlo.models.ChatLieu;
import com.poliqlo.repositories.ChatLieuRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatLieuController {
    private static final Logger log = LoggerFactory.getLogger(ChatLieuController.class);
    
    private final ModelMapper modelMapper;
    
    private final ChatLieuRepository chatLieuRepository;
    private final ChatLieuService chatLieuService;

    @GetMapping("/admin/san-pham-chi-tiet/chat-lieu")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/chat-lieu/chat-lieu";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/chat-lieu")
    public List<Response> getAll() {
        return modelMapper.map(chatLieuRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/chat-lieu")
    public ResponseEntity<Response> add(@Valid @RequestBody AddRequest req) {
        return chatLieuService.save(req);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/chat-lieu")
    public ResponseEntity<Response> update(@Valid @RequestBody EditReq editReq) {
        return chatLieuService.update(editReq);
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/chat-lieu")
    public ResponseEntity<ChatLieu> delete(@RequestParam(name = "id") Integer id) {
        return chatLieuService.delete(id);
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/chat-lieu/revert")
    public ResponseEntity<?> revert(@RequestParam(name = "id") Integer id) {
        return chatLieuService.revert(id);
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/chat-lieu/import-excel")
    @Transactional
    public ResponseEntity<?> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstChatLieu) {
        return chatLieuService.importExcel(lstChatLieu);
    }

    @GetMapping("/admin/san-pham-chi-tiet/chat-lieu/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        return chatLieuService.exportToExcel();
    }
}
