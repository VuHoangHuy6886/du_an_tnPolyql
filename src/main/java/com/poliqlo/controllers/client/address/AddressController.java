package com.poliqlo.controllers.client.address;

import com.poliqlo.controllers.client.address.dto.AddressRequestDTO;
import com.poliqlo.controllers.client.address.dto.AddressResponseDTO;
import com.poliqlo.controllers.client.address.mapper.AddressMapper;
import com.poliqlo.controllers.client.address.service.AddressService;
import com.poliqlo.models.DiaChi;
import com.poliqlo.models.KhachHang;
import com.poliqlo.repositories.DiaChiRepository;
import com.poliqlo.repositories.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final KhachHangRepository khachHangRepository;
    private final DiaChiRepository diaChiRepository;
    @PostMapping("/api/find-address-by-id-customer")
    public ResponseEntity<?> getDiaChiById(@RequestBody Integer id) {
        System.out.println("id dia chi : "+id);
        AddressResponseDTO addressDTO = addressService.findDiaChiByIdCustomer(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/api/findAll-address-by-id-customer")
    public ResponseEntity<?> geListDiaChiByIdCustomer(@RequestBody Integer id) {
        System.out.println("id dia chi : "+id);
        List<AddressResponseDTO> addressDTO = addressService.findAllDiaChi(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/api/find-address-by-id-address")
    public ResponseEntity<?> geListDiaChiByIdAddress(@RequestBody Integer id) {
        AddressResponseDTO addressDTO = addressService.findDiaChiById(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/api/create-address")
    public ResponseEntity<?> createAddress(@RequestBody AddressRequestDTO requestDTO) {
        KhachHang khachHang = khachHangRepository.findById(requestDTO.getCustomerID()).get();
        DiaChi diaChi = AddressMapper.requestToDiaChi(requestDTO, khachHang);
        diaChiRepository.save(diaChi);
        return ResponseEntity.ok("thêm thành công");
    }
}
