package com.poliqlo.controllers.admin.hoa_don.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DiaLyService {
    private static final String GHN_TOKEN = "568f0428-ff0b-11ef-968a-1659e6af139f";
    private static final String API_BASE = "https://online-gateway.ghn.vn/shiip/public-api/master-data";
    private final RestTemplate restTemplate;

    public DiaLyService() {
        this.restTemplate = new RestTemplate(); // Tạo instance RestTemplate trực tiếp
    }

    public String layTenTinhThanhPho(Integer provinceId) {
        if (provinceId == null) {
            return "";
        }

        try {
            String url = API_BASE + "/province";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", GHN_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.get("code").equals(200)) {
                    Object data = body.get("data");
                    if (data instanceof Iterable) {
                        for (Object item : (Iterable<?>) data) {
                            Map<String, Object> province = (Map<String, Object>) item;
                            if (province.get("ProvinceID").equals(provinceId)) {
                                String provinceName = (String) province.get("ProvinceName");
                                return provinceName != null ? provinceName : "";
                            }
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String layTenQuanHuyen(Integer districtId) {
        if (districtId == null) {
            return "";
        }

        try {
            String url = API_BASE + "/district";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", GHN_TOKEN);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Integer> requestBody = new HashMap<>();
            // Note: API GHN dùng province_id để lấy district, nhưng ở đây districtId là ID của district
            // Giả sử districtId có thể được dùng để tìm trực tiếp, nếu không cần sửa logic
            HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.get("code").equals(200)) {
                    Object data = body.get("data");
                    if (data instanceof Iterable) {
                        for (Object item : (Iterable<?>) data) {
                            Map<String, Object> district = (Map<String, Object>) item;
                            if (district.get("DistrictID").equals(districtId)) {
                                String districtName = (String) district.get("DistrictName");
                                return districtName != null ? districtName : "";
                            }
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String layTenPhuongXa(String wardCode, Integer districtId) {
        if (wardCode == null || wardCode.isEmpty() || districtId == null) {
            return "";
        }

        try {
            String url = API_BASE + "/ward";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Token", GHN_TOKEN);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Integer> requestBody = new HashMap<>();
            requestBody.put("district_id", districtId);
            HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                if (body.get("code").equals(200)) {
                    Object data = body.get("data");
                    if (data instanceof Iterable) {
                        for (Object item : (Iterable<?>) data) {
                            Map<String, Object> ward = (Map<String, Object>) item;
                            if (ward.get("WardCode").equals(wardCode)) {
                                String wardName = (String) ward.get("WardName");
                                return wardName != null ? wardName : "";
                            }
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public String ghepDiaChiDayDu(String diaChiChiTiet, String wardCode, Integer districtId, Integer provinceId) {
        String phuongXa = layTenPhuongXa(wardCode, districtId);
        String quanHuyen = layTenQuanHuyen(districtId);
        String tinhThanhPho = layTenTinhThanhPho(provinceId);

        StringBuilder diaChiDayDu = new StringBuilder(diaChiChiTiet != null ? diaChiChiTiet : "");
        if (phuongXa != null && !phuongXa.isEmpty()) {
            diaChiDayDu.append(", ").append(phuongXa);
        }
        if (quanHuyen != null && !quanHuyen.isEmpty()) {
            diaChiDayDu.append(", ").append(quanHuyen);
        }
        if (tinhThanhPho != null && !tinhThanhPho.isEmpty()) {
            diaChiDayDu.append(", ").append(tinhThanhPho);
        }

        return diaChiDayDu.toString();
    }
}
