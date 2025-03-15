alter table phieu_giam_gia_khach_hang change column  IS_DELETED IS_USED bit default false;

alter table phieu_giam_gia drop column DOI_TUONG_GIAM;
alter table san_pham_chi_tiet drop foreign key san_pham_chi_tiet_dot_giam_gia_fk;
alter table san_pham_chi_tiet drop column DOT_GIAM_GIA_ID;
CREATE TABLE IF NOT EXISTS san_pham_chi_tiet_dot_giam_gia (
                                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                                              dot_giam_gia_id INT NOT NULL,
                                                              san_pham_chi_tiet_id INT NOT NULL,
                                                              so_luong INT NOT NULL,
                                                              is_active TINYINT(1) DEFAULT 0,
                                                              CONSTRAINT fk_dot_giam_gia FOREIGN KEY (dot_giam_gia_id) REFERENCES dot_giam_gia(id) ON DELETE CASCADE,
                                                              CONSTRAINT fk_san_pham_chi_tiet FOREIGN KEY (san_pham_chi_tiet_id) REFERENCES san_pham_chi_tiet(id) ON DELETE CASCADE
);
