package com.poliqlo.controllers.admin.dashboard.service;

import com.poliqlo.controllers.admin.dashboard.model.response.OrderRateResponse;

import java.util.Map;

public interface IStatisticService {
    public Map<Integer,Integer> getTop10ProductBestSeller();
    public OrderRateResponse getOrderRate();

}
