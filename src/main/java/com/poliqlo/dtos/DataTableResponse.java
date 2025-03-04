package com.poliqlo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataTableResponse<T> {
    private Integer draw;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List<T> data;

}
