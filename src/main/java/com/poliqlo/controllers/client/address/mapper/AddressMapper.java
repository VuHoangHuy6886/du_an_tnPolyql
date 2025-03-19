package com.poliqlo.controllers.client.address.mapper;

import com.poliqlo.controllers.client.address.dto.AddressRequestDTO;
import com.poliqlo.controllers.client.address.dto.UpdateAddressRequestDTO;
import com.poliqlo.controllers.client.address.dto.AddressResponseDTO;
import com.poliqlo.models.DiaChi;
import com.poliqlo.models.KhachHang;

public class AddressMapper {
    public static AddressResponseDTO diaChiToAddress(DiaChi diaChi) {
        AddressResponseDTO addressDTO = AddressResponseDTO.builder()
                .id(diaChi.getId())
                .ten(diaChi.getHoTenNguoiNhan())
                .provinceId(diaChi.getProvinceId())
                .districtId(diaChi.getDistrictId())
                .wardId(diaChi.getWardCode())
                .soDienThoai(diaChi.getSoDienThoai())
                .defaultValue(diaChi.getIsDefault())
                .build();
        return addressDTO;
    }

    public static DiaChi requestToDiaChi(AddressRequestDTO requestDTO, KhachHang khachHang) {
        DiaChi diaChi = DiaChi.builder()
                .provinceId(Integer.parseInt(requestDTO.getProvinceID()))
                .districtId(Integer.parseInt(requestDTO.getDistrictID()))
                .wardCode(requestDTO.getWardID())
                .address(requestDTO.getAddressStr())
                .khachHang(khachHang)
                .hoTenNguoiNhan(requestDTO.getTenNguoiNhan())
                .soDienThoai(requestDTO.getPhone())
                .isDeleted(false)
                .isDefault(false)
                .build();
        return diaChi;
    }
    public static DiaChi requestUpdateToDiaChi(UpdateAddressRequestDTO requestDTO, KhachHang khachHang) {
        DiaChi diaChi = DiaChi.builder()
                .id(requestDTO.getId())
                .provinceId(Integer.parseInt(requestDTO.getProvinceID()))
                .districtId(Integer.parseInt(requestDTO.getDistrictID()))
                .wardCode(requestDTO.getWardID())
                .address(requestDTO.getAddressStr())
                .khachHang(khachHang)
                .hoTenNguoiNhan(requestDTO.getTenNguoiNhan())
                .soDienThoai(requestDTO.getPhone())
                .isDeleted(false)
                .isDefault(false)
                .build();
        return diaChi;
    }
}
