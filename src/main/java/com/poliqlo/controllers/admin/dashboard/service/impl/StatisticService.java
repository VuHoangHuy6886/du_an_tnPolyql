package com.poliqlo.controllers.admin.dashboard.service.impl;

import com.poliqlo.controllers.admin.dashboard.model.response.OrderRateResponse;
import com.poliqlo.controllers.admin.dashboard.service.IStatisticService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StatisticService implements IStatisticService {

    @Override
    public Map<Integer, Integer> getTop10ProductBestSeller() {
        return Map.of();
    }

    @Override
    public OrderRateResponse getOrderRate() {
        return null;
    }
}
