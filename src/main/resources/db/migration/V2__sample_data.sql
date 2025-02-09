-- Dữ liệu mẫu cho bảng TAI_KHOAN
INSERT INTO TAI_KHOAN (USER_NAME, EMAIL, SO_DIEN_THOAI, ANH_URL, ROLE, PASSWORD, IS_ENABLE, IS_DELETED) VALUES
                                                                                                            ('john_doe', 'john.doe@example.com', '1234567890', 'john.jpg', 'customer', 'hashed_password_1', b'1', b'0'),
                                                                                                            ('jane_smith', 'jane.smith@example.com', '0987654321', 'jane.jpg', 'admin', 'hashed_password_2', b'1', b'0'),
                                                                                                            ('peter_jones', 'peter.jones@example.com', '1122334455', 'peter.jpg', 'customer', 'hashed_password_3', b'1', b'0'),
                                                                                                            ('alice_brown', 'alice.brown@example.com', '5551234567', 'alice.jpg', 'customer', 'hashed_password_4', b'1', b'0'),
                                                                                                            ('bob_wilson', 'bob.wilson@example.com', '7779876543', 'bob.jpg', 'employee', 'hashed_password_5', b'1', b'0');

-- Dữ liệu mẫu cho bảng KHACH_HANG
INSERT INTO KHACH_HANG (TAI_KHOAN_ID, TEN, NGAY_SINH, GIOI_TINH, IS_DELETED) VALUES
                                                                                            ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'john_doe'), 'John Doe', '1990-05-15', 'Nam', b'0'),
                                                                                            ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'peter_jones'), 'Peter Jones', '1985-12-20', 'Nam', b'0'),
                                                                                            ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'alice_brown'), 'Alice Brown', '1992-08-10', 'Nữ', b'0');

-- Dữ liệu mẫu cho bảng NHAN_VIEN
INSERT INTO NHAN_VIEN (TAI_KHOAN_ID, TEN, IS_DELETED) VALUES
                                                                    ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'jane_smith'), 'Jane Smith', b'0'),
                                                                    ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'bob_wilson'), 'Bob Wilson', b'0');

-- Dữ liệu mẫu cho bảng NHAN_VIEN_LOG
INSERT INTO NHAN_VIEN_LOG (NHAN_VIEN_ID, NOI_DUNG, THOI_GIAN, IS_DELETED) VALUES
                                                                              ((SELECT ID FROM NHAN_VIEN WHERE TEN= 'Jane Smith'), 'Đăng nhập hệ thống', '2024-01-04 08:00:00', b'0'),
                                                                              ((SELECT ID FROM NHAN_VIEN WHERE TEN= 'Bob Wilson'), 'Cập nhật thông tin sản phẩm', '2024-01-04 10:30:00', b'0');

-- Dữ liệu mẫu cho bảng THUONG_HIEU
INSERT INTO THUONG_HIEU (TEN, TRANG_THAI, IS_DELETED, thumbnail) VALUES
                                                                                 ('Adidas', 'Còn hàng', b'0', 'adidas.jpg'),
                                                                                 ('Nike', 'Còn hàng', b'0', 'nike.jpg'),
                                                                                 ('Gucci', 'Còn hàng', b'0', 'gucci.jpg');

-- Dữ liệu mẫu cho bảng CHAT_LIEU
INSERT INTO CHAT_LIEU (TEN, TRANG_THAI, IS_DELETED) VALUES
                                                                  ('Cotton', 'Còn hàng', b'0'),
                                                                  ('Jean', 'Còn hàng', b'0'),
                                                                  ('Linen', 'Còn hàng', b'0'),
                                                                  ('Voan', 'Còn hàng', b'0');

-- Dữ liệu mẫu cho bảng KIEU_DANG
INSERT INTO KIEU_DANG (TEN, TRANG_THAI, IS_DELETED) VALUES
                                                                  ('Áo thun', 'Còn hàng', b'0'),
                                                                  ('Quần jean', 'Còn hàng', b'0'),
                                                                  ('Áo sơ mi', 'Còn hàng', b'0'),
                                                                  ('Váy hoa', 'Còn hàng', b'0');

-- Dữ liệu mẫu cho bảng KICH_THUOC
INSERT INTO KICH_THUOC (TEN, TRANG_THAI, IS_DELETED) VALUES
                                                                    ('S', 'Còn hàng', b'0'),
                                                                    ('M', 'Còn hàng', b'0'),
                                                                    ('L', 'Còn hàng', b'0'),
                                                                    ('XL', 'Còn hàng', b'0');

-- Dữ liệu mẫu cho bảng MAU_SAC
INSERT INTO MAU_SAC (TEN, TRANG_THAI, IS_DELETED, code) VALUES
                                                                    ('Đỏ', 'Còn hàng', b'0', '#FF0000'),
                                                                    ('Xanh', 'Còn hàng', b'0', '#00FF00'),
                                                                    ('Trắng', 'Còn hàng', b'0', '#FFFFFF'),
                                                                    ('Đen', 'Còn hàng', b'0', '#000000');

-- Dữ liệu mẫu cho bảng DOT_GIAM_GIA
INSERT INTO DOT_GIAM_GIA (MA, TEN, MO_TA, THOI_GIAN_BAT_DAU, THOI_GIAN_KET_THUC, LOAI_CHIET_KHAU, GIA_TRI_GIAM, GIAM_TOI_DA, TRANG_THAI, IS_DELETED) VALUES
                                                                                                                                                         ('GG001', 'Khuyến mãi tháng 1', 'Giảm giá 10% cho tất cả sản phẩm', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 'Phần trăm', 10, 50000, 'Còn hiệu lực', b'0'),
                                                                                                                                                         ('GG002', 'Tết Nguyên Đán', 'Giảm giá 20% cho sản phẩm chọn lọc', '2024-02-08 00:00:00', '2024-02-14 23:59:59', 'Phần trăm', 20, 100000, 'Sắp diễn ra', b'0');

-- Dữ liệu mẫu cho bảng PHIEU_GIAM_GIA
INSERT INTO PHIEU_GIAM_GIA (MA, TEN, SO_LUONG, HOA_DON_TOI_THIEU, LOAI_HINH_GIAM, GIA_TRI_GIAM, GIAM_TOI_DA, NGAY_BAT_DAU, NGAY_KET_THUC, DOI_TUONG_GIAM, TRANG_THAI, IS_DELETED) VALUES
                                                                                                                                                                                      ('PGG001', 'Phiếu giảm giá 50k', 100, 500000, 'Tiền mặt', 50000, 50000, '2024-01-04 00:00:00', '2024-01-31 23:59:59', 'Tất cả khách hàng', 'Còn hiệu lực', b'0'),
                                                                                                                                                                                      ('PGG002', 'Phiếu giảm giá 10%', 50, 1000000, 'Phần trăm', 10, 100000, '2024-02-01 00:00:00', '2024-02-29 23:59:59', 'Khách hàng thân thiết', 'Sắp diễn ra', b'0');

-- Dữ liệu mẫu cho bảng PHIEU_GIAM_GIA_KHACH_HANG
INSERT INTO PHIEU_GIAM_GIA_KHACH_HANG (ID_KHACH_HANG, ID_PHIEU_GIAM_GIA, IS_DELETED) VALUES
                                                                                         ((SELECT ID FROM KHACH_HANG WHERE TEN= 'John Doe'), (SELECT ID FROM PHIEU_GIAM_GIA WHERE MA = 'PGG001'), b'0'),
                                                                                         ((SELECT ID FROM KHACH_HANG WHERE TEN= 'Alice Brown'), (SELECT ID FROM PHIEU_GIAM_GIA WHERE MA = 'PGG002'), b'0');

-- Dữ liệu mẫu cho bảng SAN_PHAM
INSERT INTO SAN_PHAM (ID_THUONG_HIEU, ID_CHAT_LIEU, ID_KIEU_DANG, MA_SAN_PHAM, TEN, TRANG_THAI, MO_TA, ANH_URL, IS_DELETED) VALUES
                                                                                                                                         (1, 1, 1, 'SP001', 'Áo thun nam', 'Còn hàng', 'Áo thun cotton 100%', 'ao_thun.jpg', b'0'),
                                                                                                                                         (2, 2, 2, 'SP002', 'Quần jean nữ', 'Hết hàng', 'Quần jean ống đứng', 'quan_jean.jpg', b'0'),
                                                                                                                                         (1, 1, 3, 'SP003', 'Áo sơ mi nam', 'Còn hàng', 'Áo sơ mi linen', 'ao_somi.jpg', b'0'),
                                                                                                                                         (2, 3, 4, 'SP004', 'Váy hoa nữ', 'Còn hàng', 'Váy hoa voan', 'vay_hoa.jpg', b'0');

-- Dữ liệu mẫu cho bảng SAN_PHAM_CHI_TIET
INSERT INTO SAN_PHAM_CHI_TIET (ID_SAN_PHAM, ID_KICH_THUOC, ID_MAU_SAC, ID_DOT_GIAM_GIA, GIA_BAN, SO_LUONG, BARCODE, IS_DELETED) VALUES
                                                                                                                                    ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP001'), 1, 1, NULL, 150000, 50, 'barcode_1', b'0'),
                                                                                                                                    ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP002'), 2, 2, NULL, 250000, 0, 'barcode_2', b'0'),
                                                                                                                                    ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP003'), 1, 3, NULL, 300000, 25, 'barcode_3', b'0'),
                                                                                                                                    ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP004'), 2, 4, NULL, 350000, 30, 'barcode_4', b'0');

-- Dữ liệu mẫu cho bảng DANH_MUC
INSERT INTO DANH_MUC (TEN, TRANG_THAI, IS_DELETED) VALUES
                                                                ('Áo', 'Còn hàng', b'0'),
                                                                ('Quần', 'Còn hàng', b'0'),
                                                                ('Váy', 'Còn hàng', b'0');


-- Dữ liệu mẫu cho bảng GIO_HANG_CHI_TIET
INSERT INTO GIO_HANG_CHI_TIET (ID_KHACH_HANG, ID_SAN_PHAM_CHI_TIET, SO_LUONG, NGAY_THEM_VAO, IS_DELETED) VALUES
                                                                                                             ((SELECT ID FROM KHACH_HANG WHERE TEN= 'John Doe'), (SELECT ID FROM SAN_PHAM_CHI_TIET WHERE BARCODE = 'barcode_1'), 2, '2024-01-03 00:00:00', b'0'),
                                                                                                             ((SELECT ID FROM KHACH_HANG WHERE TEN= 'Alice Brown'), (SELECT ID FROM SAN_PHAM_CHI_TIET WHERE BARCODE = 'barcode_4'), 1, '2024-01-03 00:00:00', b'0');

-- Dữ liệu mẫu cho bảng THONG_BAO
INSERT INTO THONG_BAO (ID_TAI_KHOAN, URL, NOI_DUNG, THOI_GIAN, TRANG_THAI, IS_DELETED) VALUES
                                                                                           ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'john_doe'), '/thong-bao/1', 'Bạn có một thông báo mới', '2024-01-03 00:00:00', 'Chưa đọc', b'0'),
                                                                                           ((SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'alice_brown'), '/thong-bao/2', 'Có sản phẩm mới', '2024-01-03 00:00:00', 'Đã đọc', b'0');

-- Dữ liệu mẫu cho bảng DIA_CHI
INSERT INTO DIA_CHI (ID_KHACH_HANG, HO_TEN_NGUOI_NHAN, SO_DIEN_THOAI, PROVINCE_ID, DISTRICT_ID, WARD_CODE, ADDRESS, IS_DEFAULT, IS_DELETED) VALUES
                                                                                                                                                ((SELECT ID FROM KHACH_HANG WHERE TEN= 'John Doe'), 'John Doe', '1234567890', 1, 1, '00001', '123 Main Street', b'1', b'0'),
                                                                                                                                                ((SELECT ID FROM KHACH_HANG WHERE TEN= 'Alice Brown'), 'Alice Brown', '5551234567', 2, 2, '00002', '456 Oak Avenue', b'1', b'0');

-- Dữ liệu mẫu cho bảng REVIEWS
INSERT INTO REVIEWS (ID_SAN_PHAM, ID_KHACH_HANG, THOI_GIAN, NOI_DUNG, ANH, STATUS, IS_DELETED, UP_VOTE, DOWN_VOTE) VALUES
                                                                                                                       ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP001'), (SELECT ID FROM KHACH_HANG WHERE TEN= 'John Doe'), '2024-01-03 00:00:00', 'Sản phẩm tốt', 'review.jpg', 'Đã duyệt', b'0', 5, 1),
                                                                                                                       ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP004'), (SELECT ID FROM KHACH_HANG WHERE TEN= 'Alice Brown'), '2024-01-03 00:00:00', 'Váy rất đẹp', 'review2.jpg', 'Chờ duyệt', b'0', 2, 0);

-- Dữ liệu mẫu cho bảng REPLIES
INSERT INTO REPLIES (REVIEW_ID, NHAN_VIEN_ID, NOI_DUNG, THOI_GIAN, IS_DELETED) VALUES
                                                                                   ((SELECT ID FROM REVIEWS WHERE NOI_DUNG = 'Sản phẩm tốt'), (SELECT ID FROM NHAN_VIEN WHERE TEN= 'Jane Smith'), 'Cảm ơn bạn đã đánh giá', '2024-01-03 00:00:00', b'0'),
                                                                                   ((SELECT ID FROM REVIEWS WHERE NOI_DUNG = 'Váy rất đẹp'), (SELECT ID FROM NHAN_VIEN WHERE TEN= 'Bob Wilson'), 'Cảm ơn bạn đã ủng hộ', '2024-01-03 00:00:00', b'0');

-- Dữ liệu mẫu cho bảng SAN_PHAM_UA_THICH
INSERT INTO SAN_PHAM_UA_THICH (ID_KHACH_HANG, SAN_PHAM_ID, IS_DELETED) VALUES
                                                                           ((SELECT ID FROM KHACH_HANG WHERE TEN= 'John Doe'), (SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP001'), b'0'),
                                                                           ((SELECT ID FROM KHACH_HANG WHERE TEN= 'Alice Brown'), (SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP004'), b'0');

-- Dữ liệu mẫu cho bảng HOA_DON
INSERT INTO HOA_DON (ID_KHACH_HANG, ID_PHIEU_GIAM_GIA, PHUONG_THUC_THANH_TOAN, PHI_VAN_CHUYEN, TONG_TIEN, SO_DIEN_THOAI, DIA_CHI, TEN_NGUOI_NHAN, NGAY_NHAN_HANG, TRANG_THAI, IS_DELETED) VALUES
                                                                                                                                                                                              ((SELECT ID FROM KHACH_HANG WHERE TEN= 'John Doe'), NULL, 'COD', 30000, 180000, '1234567890', '123 Main Street', 'John Doe', '2024-01-06', 'Đang giao hàng', b'0'),
                                                                                                                                                                                              ((SELECT ID FROM KHACH_HANG WHERE TEN= 'Alice Brown'), NULL, 'Credit Card', 0, 350000, '5551234567', '456 Oak Avenue', 'Alice Brown', '2024-01-05', 'Đã giao hàng', b'0');

-- Dữ liệu mẫu cho bảng HOA_DON_CHI_TIET
INSERT INTO HOA_DON_CHI_TIET (ID_HOA_DON, ID_SAN_PHAM_CHI_TIET, ID_DOT_GIAM_GIA, GIA_GOC, GIA_KHUYEN_MAI, SO_LUONG, IS_DELETED) VALUES
                                                                                                                                    ((SELECT ID FROM HOA_DON WHERE TEN_NGUOI_NHAN = 'John Doe'), (SELECT ID FROM SAN_PHAM_CHI_TIET WHERE BARCODE = 'barcode_1'), NULL, 150000, 150000, 1, b'0'),
                                                                                                                                    ((SELECT ID FROM HOA_DON WHERE TEN_NGUOI_NHAN = 'Alice Brown'), (SELECT ID FROM SAN_PHAM_CHI_TIET WHERE BARCODE = 'barcode_4'), NULL, 350000, 350000, 1, b'0');

-- Dữ liệu mẫu cho bảng LICH_SU_HOA_DON
INSERT INTO LICH_SU_HOA_DON (ID_HOA_DON, ID_TAI_KHOAN, TIEU_DE, MO_TA, THOI_GIAN, IS_DELETED) VALUES
                                                                                                  ((SELECT ID FROM HOA_DON WHERE TEN_NGUOI_NHAN = 'John Doe'), (SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'jane_smith'), 'Đơn hàng mới', 'Đơn hàng đã được tạo', '2024-01-03 00:00:00', b'0'),
                                                                                                  ((SELECT ID FROM HOA_DON WHERE TEN_NGUOI_NHAN = 'Alice Brown'), (SELECT ID FROM TAI_KHOAN WHERE USER_NAME = 'jane_smith'), 'Đơn hàng đã giao', 'Đơn hàng đã được giao thành công', '2024-01-03 00:00:00', b'0');

-- Dữ liệu mẫu cho bảng ANH
INSERT INTO ANH (ID_SAN_PHAM, ID_MAU_SAC, URL, IS_DEFAULT, IS_DELETED) VALUES
                                                                           ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP001'), (SELECT ID FROM MAU_SAC WHERE TEN= 'Đỏ'), 'anh_ao_do.jpg', b'1', b'0'),
                                                                           ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP001'), (SELECT ID FROM MAU_SAC WHERE TEN= 'Xanh'), 'anh_ao_xanh.jpg', b'0', b'0'),
                                                                           ((SELECT ID FROM SAN_PHAM WHERE MA_SAN_PHAM = 'SP004'), (SELECT ID FROM MAU_SAC WHERE TEN= 'Trắng'), 'anh_vay_trang.jpg', b'1', b'0');

-- Dữ liệu mẫu cho bảng BANNER
INSERT INTO BANNER (ANH_URL, LINK_URL, THOI_GIAN_BAT_DAU, THOI_GIAN_KET_THUC, PRIORITY, IS_DELETED) VALUES
                                                                                                        ('banner1.jpg', '/product/123', '2024-01-01 00:00:00', '2024-01-31 23:59:59', 1, b'0'),
                                                                                                        ('banner2.jpg', '/category/456', '2024-02-01 00:00:00', '2024-02-29 23:59:59', 2, b'0'),
                                                                                                        ('banner3.jpg', '/sale', '2024-03-01 00:00:00', '2024-03-31 23:59:59', 3, b'0');