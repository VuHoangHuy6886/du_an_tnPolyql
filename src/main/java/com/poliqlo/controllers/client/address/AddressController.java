package com.poliqlo.controllers.client.address;

import com.poliqlo.controllers.client.address.dto.AddressRequestDTO;
import com.poliqlo.controllers.client.address.dto.AddressResponseDTO;
import com.poliqlo.controllers.client.address.dto.UpdateAddressRequestDTO;
import com.poliqlo.controllers.client.address.mapper.AddressMapper;
import com.poliqlo.controllers.client.address.service.AddressService;
import com.poliqlo.controllers.common.auth.service.AuthService;
import com.poliqlo.models.DiaChi;
import com.poliqlo.models.KhachHang;
import com.poliqlo.repositories.DiaChiRepository;
import com.poliqlo.repositories.KhachHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final KhachHangRepository khachHangRepository;
    private final DiaChiRepository diaChiRepository;
    private final AuthService authService;

    @PostMapping("/api/find-address-by-id-customer")
    public ResponseEntity<?> getDiaChiById(@RequestBody Integer id) {
        System.out.println("id dia chi : " + id);
        AddressResponseDTO addressDTO = addressService.findDiaChiByIdCustomer(id);
        return ResponseEntity.ok(addressDTO);
    }

    @PostMapping("/api/findAll-address-by-id-customer")
    public ResponseEntity<?> geListDiaChiByIdCustomer(@RequestBody Integer id) {
        System.out.println("id dia chi : " + id);
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

    @GetMapping("/address")
    public String showAddress(Model model) {
        model.addAttribute("customerId", authService.getCurrentUserDetails().get().getKhachHang().getId());
        return "client/address";
    }

    @PostMapping("/api/update-address")
    public ResponseEntity<?> updateAddress(@RequestBody UpdateAddressRequestDTO requestDTO) {
        KhachHang khachHang = khachHangRepository.findById(requestDTO.getCustomerID()).get();
        Boolean isDefault = diaChiRepository.findById(requestDTO.getId()).get().getIsDefault();
        DiaChi diaChi = AddressMapper.requestUpdateToDiaChi(requestDTO, khachHang,isDefault);
        diaChiRepository.save(diaChi);
        System.out.println("dia chi muon sua : " + diaChi.toString());
        return ResponseEntity.ok("Sửa thành công");
    }

    @PostMapping("/api/delete-address")
    public ResponseEntity<?> deleteAddress(@RequestBody Integer id) {
        DiaChi diaChi = diaChiRepository.findById(id).get();
        diaChi.setIsDeleted(true);
        diaChiRepository.save(diaChi);
        return ResponseEntity.ok("delete thành công");
    }

    @PostMapping("/api/set-default-address")
    public ResponseEntity<?> setDefault(@RequestBody Integer id) {
        DiaChi diaChi = diaChiRepository.findById(id).get();
        Integer customerId = khachHangRepository.findById(diaChi.getKhachHang().getId()).get().getId();
        if (customerId != null) {
            List<DiaChi> diaChis = addressService.getAllDiaChiByCustomerId(customerId);
            for (DiaChi diaChi1 : diaChis) {
                diaChi1.setIsDefault(false);
                diaChiRepository.save(diaChi);
            }
        }
        diaChi.setIsDefault(true);
        diaChiRepository.save(diaChi);
        return ResponseEntity.ok("set default thành công");
    }
}
