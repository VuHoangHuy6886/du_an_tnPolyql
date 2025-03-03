package com.poliqlo.controllers.admin.ban_hang.repository.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SanPhamRepositoryImpl {
//    @PersistenceContext
//    private final EntityManager em;

//    public PageImpl<SanPhamDataTableBanHang> findAllSanPhamDataTableBanHang(String searchKey, Integer[] seriesFilter, String orderBy, Pageable pageAble) {
//       CriteriaBuilder cb = em.getCriteriaBuilder();
////       Tạo một đối tượng CriteriaQuery để chỉ định kiểu dữ liệu trả về là SanPhamDataTableBanHang.
//       CriteriaQuery<SanPhamDataTableBanHang> query = cb.createQuery(SanPhamDataTableBanHang.class);
//        // Tạo  thực thể chính đại diện cho sản phẩm.
//        Root<SanPham> sp = query.from(SanPham.class);
//
//        // Join với thực thể SanPhamChiTiet
//        Join<SanPham, SanPhamChiTiet> spct = sp.join("sanPhamChiTiet", JoinType.LEFT);
//
//
//
//        //Tạo danh sách điều kiện lọc
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Lọc theo searchKey
//        if (searchKey != null && !searchKey.isEmpty()) {
//            String searchPattern = "%" + searchKey.toLowerCase() + "%";
//            Predicate tenSanPhamPredicate = cb.like(cb.lower(sp.get("tenSanPham")), searchPattern);
//            Predicate idPredicate = null;
//            try {
//                Integer idValue = Integer.parseInt(searchKey);
//                idPredicate = cb.equal(sp.get("id"), idValue); // Điều kiện cho id
//            } catch (NumberFormatException e) {
//                idPredicate = cb.equal(cb.literal(true), false);
//            }
//            Predicate tagPredicate = cb.like(cb.lower(sp.get("tag")), searchPattern);
//
//            // Kết hợp các điều kiện bằng toán tử OR
//            Predicate searchPredicate = cb.or(tenSanPhamPredicate, idPredicate, tagPredicate);
//            predicates.add(searchPredicate);
//        }
//
//        // Lọc theo seriesFilter
//        if (seriesFilter != null && seriesFilter.length > 0) {
//            Path<Integer> idSeriesPath = sp.get("series").get("id");
//            Predicate seriesPredicate = idSeriesPath.in(seriesFilter);
//            predicates.add(seriesPredicate);
//        }
//
//        //      Tạo một đối tượng mới của SanPhamDataTableBanHang từ các trường của thực thể SanPham và tổng số lượng từ SanPhamChiTiet.
//        query.select(cb.construct(
//                SanPhamDataTableBanHang.class,
//                sp.get("id"),
//                sp.get("anh").get("url"),
//                sp.get("tenSanPham"),
//                sp.get("trangThai"),
//                cb.sum(spct.get("soLuong"))
//        ));
//        //Thêm các nhóm lọc
//        query.where(predicates.toArray(new Predicate[predicates.size()]));
//
//        // GROUP BY các trường
//        query.groupBy(sp.get("id"), sp.get("tenSanPham"), sp.get("anh").get("url"), sp.get("trangThai"));
//
//        // Sắp xếp
//        if (orderBy != null && !orderBy.isEmpty()) {
//            Pattern pattern= Pattern.compile("(\\w+?)(:)(\\w+?)");
//            Matcher matcher = pattern.matcher(orderBy);
//            if(matcher.find()){
//                Order order = "asc".equalsIgnoreCase(matcher.group(3))
//                        ? cb.asc(sp.get(matcher.group(1)))
//                        : cb.desc(sp.get(matcher.group(1)));
//                query.orderBy(order);
//            }
//        }
//
//
//
//        // Tạo và thực thi truy vấn
//        TypedQuery<SanPhamDataTableBanHang> typedQuery = em.createQuery(query);
//        // Phân trang
//        typedQuery.setFirstResult(pageAble.getPageNumber()*pageAble.getPageSize());
//        typedQuery.setMaxResults(pageAble.getPageSize());
//        var lstSPData = typedQuery.getResultList();
//
//
//        //Tạo đối tượng đếm
//        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
//        //Tạo root đếm
//        Root<SanPham> countRoot = countQuery.from(SanPham.class);
//
//        List<Predicate> predicates2 = new ArrayList<>();
//
//        // Lọc theo searchKey
//        if (searchKey != null && !searchKey.isEmpty()) {
//            String searchPattern = "%" + searchKey.toLowerCase() + "%";
//            Predicate tenSanPhamPredicate = cb.like(cb.lower(countRoot.get("tenSanPham")), searchPattern);
//            Predicate idPredicate = null;
//            try {
//                Integer idValue = Integer.parseInt(searchKey);
//                idPredicate = cb.equal(countRoot.get("id"), idValue); // Điều kiện cho id
//            } catch (NumberFormatException e) {
//                idPredicate = cb.equal(cb.literal(true), false);
//            }
//            Predicate tagPredicate = cb.like(cb.lower(countRoot.get("tag")), searchPattern);
//
//            // Kết hợp các điều kiện bằng toán tử OR
//            Predicate searchPredicate = cb.or(tenSanPhamPredicate, idPredicate, tagPredicate);
//            predicates2.add(searchPredicate);
//        }
//
//        // Lọc theo seriesFilter
//        if (seriesFilter != null && seriesFilter.length > 0) {
//            Path<Integer> idSeriesPath = countRoot.get("series").get("id");
//            Predicate seriesPredicate = idSeriesPath.in(seriesFilter);
//            predicates2.add(seriesPredicate);
//        }
//
//        // Tính tổng số bản ghi
//
//        countQuery.select(cb.count(countRoot))
//              .where(predicates2.toArray(new Predicate[0]));
//        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);
//        Long totalRecords = countTypedQuery.getSingleResult();
//
//        var resp= new PageImpl<>(lstSPData, pageAble, totalRecords);
//
//
//        return resp;
//
//    }
//
//    public List<Object[]> findAllByIds(Set<Integer> ids) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
//        Root<SanPhamChiTiet> spct = query.from(SanPhamChiTiet.class);
//        query.multiselect(spct.get("id"),
//            cb.concat(
//                cb.concat(cb.concat(
//                        cb.concat(spct.get("sanPham").get("tenSanPham")," "),
//                        spct.get("rom"))," "),
//            spct.get("mauSac").get("ten"))
//        ).where(spct.get("id").in(ids));
//        TypedQuery<Object[]> typedQuery = em.createQuery(query);
//        return typedQuery.getResultList();
//    }

//    public Page<Select2Response.Result> findTaiKhoanNhanVienByCodeLike(String searchKey, Pageable pageAble) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Select2Response.Result> query = cb.createQuery(Select2Response.Result.class);
//        Root<SanPhamChiTiet> root = query.from(SanPhamChiTiet.class);
//        Join<SanPhamChiTiet,SanPham> sp= root.join("sanPham",JoinType.LEFT);
//        Join<SanPhamChiTiet, MauSac> ms=root.join("mauSac",JoinType.LEFT);
//
//        query.select()
//
//
//
//        return null;
//    }
}
