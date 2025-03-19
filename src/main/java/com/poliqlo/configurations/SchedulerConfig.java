package com.poliqlo.configurations;

import com.poliqlo.controllers.admin.dot_giam_gia.service.DotGiamGiaService;
import com.poliqlo.models.DotGiamGia;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerConfig {
    private final DotGiamGiaService service;

//    @Scheduled(fixedDelay = 10000) // Chạy mỗi 10    giây
//    public void fixedRateTask() {
//        List<DotGiamGia> dotGiamGiaList = service.listDiscountsUpdateStatus();
//        service.updateStatusDiscount(dotGiamGiaList);
//    }
}
