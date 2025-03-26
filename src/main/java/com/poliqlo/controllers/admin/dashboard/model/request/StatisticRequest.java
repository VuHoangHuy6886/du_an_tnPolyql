package com.poliqlo.controllers.admin.dashboard.model.request;

import java.time.LocalDate;

public class StatisticRequest {
    LocalDate fromDate;
    LocalDate toDate;
//    public static Map<LocalDate,String> getMonthLabels(LocalDate fromDate, LocalDate toDate) {
//        Map<LocalDate,String> months=new HashMap<>();
//        LocalDate date = fromDate.withDayOfMonth(1);
//
//        while (!date.isAfter(toDate)) {
//            String monthName = "Tháng " + date.getMonth().getValue();
//            months.add(monthName);
//            date = date.plusMonths(1);
//        }
//
//        return months;
//    }

}
