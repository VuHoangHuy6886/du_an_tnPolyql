package com.poliqlo.controllers.admin.ban_hang.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class CustomerResponseSelect2 implements Serializable {
    private List<CustomerResponse> results;
    private Pagination pagination;
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Pagination {
        Boolean more;
    }
    /**
     * DTO for {@link com.poly.polystore.entity.KhachHang}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class CustomerResponse implements Serializable {
        Integer id;
        String text;
    }

}

