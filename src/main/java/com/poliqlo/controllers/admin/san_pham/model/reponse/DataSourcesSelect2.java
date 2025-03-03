package com.poliqlo.controllers.admin.san_pham.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DataSourcesSelect2 implements Serializable {
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Pagination{
        private Boolean more=false;
    }
    private List<DataList> results;
    private Pagination pagination;
}
