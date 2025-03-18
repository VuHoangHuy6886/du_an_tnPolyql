package com.poliqlo.controllers.client.DiaChiKhachHang.serviece;

import com.poliqlo.models.DiaChi;
import com.poliqlo.repositories.DiaChiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaChiKhachHangService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    public List<DiaChi> getAllDiaChi() {
        return diaChiRepository.findAll();
    }
}
