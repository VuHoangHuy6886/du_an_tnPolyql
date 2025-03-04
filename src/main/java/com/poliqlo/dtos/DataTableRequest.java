package com.poliqlo.dtos;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DataTableRequest {
    int draw;
    int start;
    int length;
    String search;
    int orderColumn;
    String orderDir;
    Map<String,Object> params;
    public String getOrderColumnName(){
        var columnName= params.get(String.format("columns[%d][data]",orderColumn));
        return columnName.toString();
    }

}
