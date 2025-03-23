package com.poliqlo.controllers.client.address.service;

import com.poliqlo.controllers.client.address.dto.AddressResponseDTO;
import com.poliqlo.controllers.client.address.mapper.AddressMapper;
import com.poliqlo.models.DiaChi;
import com.poliqlo.repositories.DiaChiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {    // get dia chi
    private final DiaChiRepository diaChiRepository;

    public AddressResponseDTO findDiaChiByIdCustomer(Integer id) {
        DiaChi diaChi = diaChiRepository.findAddressDefault(id);
        return AddressMapper.diaChiToAddress(diaChi);
    }

    public AddressResponseDTO findDiaChiById(Integer id) {
        DiaChi diaChi = diaChiRepository.findDiaChiById(id);
        return AddressMapper.diaChiToAddress(diaChi);
    }

    public List<AddressResponseDTO> findAllDiaChi(Integer id) {
        List<DiaChi> list = diaChiRepository.timKiemDiaChiTheoIdKhachHang(id);
        List<AddressResponseDTO> addressResponseDTOList = new ArrayList<>();
        for (DiaChi dc : list) {
            AddressResponseDTO addressDTO = AddressMapper.diaChiToAddress(dc);
            addressResponseDTOList.add(addressDTO);
        }
        return addressResponseDTOList;
    }

    public List<DiaChi> getAllDiaChiByCustomerId(Integer id) {
        List<DiaChi> list = diaChiRepository.timKiemDiaChiTheoIdKhachHang(id);
        return list;
    }
}
