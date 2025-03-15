package com.poliqlo.controllers.admin.ban_hang.model.response;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.List;


public class ResponseSelect2<T> implements Serializable {
    private List<? extends ResponseSelect2Item> results;
    private Pagination pagination;

    public ResponseSelect2(List<? extends ResponseSelect2Item> results, Boolean pagination) {
        this.results = results;
        this.pagination = new Pagination(pagination);;
    }
    public ResponseSelect2(List<? extends ResponseSelect2Item> results) {
        this.results = results;
        this.pagination = new Pagination(false);
    }

    @AllArgsConstructor
    public static class Pagination {
        Boolean more;
    }
    public interface ResponseSelect2Item {
        Number getId();
        String getText();

    }


}


