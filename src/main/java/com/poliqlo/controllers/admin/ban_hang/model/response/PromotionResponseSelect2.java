package com.poliqlo.controllers.admin.ban_hang.model.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PromotionResponseSelect2 implements Serializable {
    private List<PromotionResponse> results;
    private Pagination pagination;
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Pagination {
        Boolean more;
    }
    /**
     * DTO for {@link com.poly.polystore.entity.MaGiamGia}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    @Builder
    public static class PromotionResponse implements Serializable {
        Integer id;
        String text;
    }

}

