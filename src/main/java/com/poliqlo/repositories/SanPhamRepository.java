package com.poliqlo.repositories;

import com.poliqlo.controllers.admin.san_pham.model.reponse.DataList;
import com.poliqlo.models.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> , JpaSpecificationExecutor<SanPham> {
  @Query(value = "SELECT new com.poliqlo.controllers.admin.san_pham.model.reponse.DataList(sp.id,sp.ten) FROM SanPham sp")
  List<DataList> findAllDataList();
  @Query(value = "SELECT DISTINCT sp.maSanPham FROM SanPham sp")
  List<String> findAllCode();
}