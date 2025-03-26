package com.poliqlo.controllers.admin.dashboard.controller;


import com.poliqlo.models.HoaDon;
import com.poliqlo.models.SanPhamChiTiet;
import com.poliqlo.repositories.HoaDonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ThongKeController {
    private final HoaDonRepository hoaDonRepository;

    @GetMapping("/admin")
    public String index(
            Model model,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false, defaultValue = "30") String findBy
            ) {
        switch (findBy) {
            case "30":
                fromDate = LocalDate.now().minusDays(30);
                toDate=LocalDate.now();
                break;
            case "60":
                fromDate = LocalDate.now().minusDays(60);
                toDate=LocalDate.now();
                break;
            case "90":
                fromDate = LocalDate.now().minusDays(90);
                toDate=LocalDate.now();
                break;
            case "TUY_CHINH":
                fromDate=fromDate==null?LocalDate.now().minusDays(30):fromDate;
                toDate=toDate==null?LocalDate.now():toDate;
                break;
        }

        List<HoaDon> data=hoaDonRepository.findAllByNgayTaoBetween(fromDate.atStartOfDay(),toDate.plusDays(1).atStartOfDay());

        var countDate=ChronoUnit.DAYS.between(fromDate, toDate);
        long[]chartAreaData;
        long[]pieChartData=new long[2];
        String[] chartArealabel;

        Map<LocalDate, Long> mapData = data.stream()
                .filter(hd->
                        hd.getTrangThai()!=null&&
                            !((hd.getIsDeleted()!=null&&hd.getIsDeleted())
                            ||hd.getTrangThai().equalsIgnoreCase("DA_HUY")
                            ||hd.getTrangThai().equalsIgnoreCase("GIAO_HANG_THAT_BAI")
                            ||hd.getTrangThai().equalsIgnoreCase("CHO_CHUYEN_HOAN")))
                .collect(Collectors.groupingBy(
                        hd->hd.getNgayTao().toLocalDate(),
                        Collectors.summingLong(hd -> hd.getTongTien().longValue())
                ));
        Map<String, Long> finalMap = new LinkedHashMap<>();
        for (long i = 0; i <= countDate; i++) {
            LocalDate currentDate = fromDate.plusDays(i);
            finalMap.put(currentDate.toString(), mapData.getOrDefault(currentDate, 0L));
        }

        var temp=data.stream().collect(Collectors.groupingBy(
                HoaDon::getLoaiHoaDon,
                Collectors.summingLong(
                        hd -> hd.getTongTien().longValue()
                )
        ));
        pieChartData[0]=temp.get("TAI_QUAY")==null?0L:temp.get("TAI_QUAY");
        pieChartData[1]=temp.get("ONLINE")==null?0L:temp.get("ONLINE");

        chartArealabel = finalMap.keySet().toArray(new String[0]);
        chartAreaData = finalMap.values().stream().mapToLong(Long::longValue).toArray();



        double thanhCong,dangXuLy,dangGiaoHang,huy,giaoHangThatBai;


        long dailyRevenue = 100000L;
        long monthlyRevenue= 100000L;
        long revenueLastMonth=10000L;
        int totalPendingBills=0;

        List<Object[]> top10SanPhamBanChayNhat =hoaDonRepository.getTop10SanPhamBanChayNhat(fromDate.atStartOfDay(),toDate.plusDays(1).atStartOfDay());
        List<SanPhamChiTiet> top10SanPhamDoanhThuCaoNhat =new ArrayList<>();



        dailyRevenue=hoaDonRepository.getDaylyRevenue().orElse(0L);
        monthlyRevenue=hoaDonRepository.getMonthlyRevenue().orElse(0L);
        revenueLastMonth=hoaDonRepository.getSamePeriodLastMonthRevenue().orElse(0L);
        totalPendingBills= (int) hoaDonRepository.countAllByTrangThaiIn(List.of(HoaDonRepository.CHO_XAC_NHAN,HoaDonRepository.DANG_CHUAN_BI_HANG,HoaDonRepository.XAC_NHAN));


        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate",toDate);
        model.addAttribute("findBy",findBy);
        model.addAttribute("dailyRevenue",formatVND(dailyRevenue));
        model.addAttribute("monthlyRevenue",formatVND(monthlyRevenue));
        model.addAttribute("revenueLastMonth",formatVND(revenueLastMonth));
        model.addAttribute("totalPendingBills",totalPendingBills);

        model.addAttribute("chartAreaData", chartAreaData);
        model.addAttribute("chartArealabel",chartArealabel);
        model.addAttribute("pieChartData",pieChartData);

        model.addAttribute("thanhCong", formatDoublePercent(data.stream().filter(hoaDon -> hoaDon.getTrangThai().equals(HoaDonRepository.THANH_CONG)).count() * 100 / data.size()));
        model.addAttribute("dangXuLy", formatDoublePercent(data.stream().filter(hoaDon -> hoaDon.getTrangThai().equals(HoaDonRepository.DANG_CHUAN_BI_HANG)||hoaDon.getTrangThai().equals(HoaDonRepository.CHO_XAC_NHAN)||hoaDon.getTrangThai().equals(HoaDonRepository.XAC_NHAN)).count() * 100 / data.size()));
        model.addAttribute("dangGiaoHang", formatDoublePercent(data.stream().filter(hoaDon -> hoaDon.getTrangThai().equals(HoaDonRepository.CHO_LAY_HANG)||hoaDon.getTrangThai().equals(HoaDonRepository.LAY_HANG_THANH_CONG)||hoaDon.getTrangThai().equals(HoaDonRepository.DANG_VAN_CHUYEN)||hoaDon.getTrangThai().equals(HoaDonRepository.DANG_GIAO)).count() * 100 / data.size()));
        model.addAttribute("huy", formatDoublePercent(data.stream().filter(hoaDon -> hoaDon.getTrangThai().equals(HoaDonRepository.DA_HUY)).count() * 100 / data.size()));
        model.addAttribute("giaoHangThatBai", formatDoublePercent(data.stream().filter(hoaDon -> hoaDon.getTrangThai().equals(HoaDonRepository.GIAO_HANG_THAT_BAI)||hoaDon.getTrangThai().equals(HoaDonRepository.THAT_LAC)||hoaDon.getTrangThai().equals(HoaDonRepository.CHO_CHUYEN_HOAN)).count() * 100 / data.size()));
       
        return "admin/dashboard/dashboard";
    }






    public static String formatVND(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " VND";
    }
    public static String formatDoublePercent(Number num) {
        return (num == null ? 0L : num.longValue())+"%";

    }

}
