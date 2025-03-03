package com.poliqlo.controllers.admin.san_pham.model.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class DataList implements Serializable {
    private Integer id;
    private String text;
}