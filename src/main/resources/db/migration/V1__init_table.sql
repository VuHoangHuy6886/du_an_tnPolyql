-- -----------------------------------------------------
-- Table TAI_KHOAN
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS TAI_KHOAN
(
    ID            INT                 NOT NULL AUTO_INCREMENT,
    USER_NAME     VARCHAR(255) UNIQUE NULL,
    EMAIL         VARCHAR(255) UNIQUE NULL,
    SO_DIEN_THOAI VARCHAR(20) UNIQUE  NULL,
    ANH_URL       VARCHAR(255)        NULL,
    ROLE          VARCHAR(50)         NULL,
    PASSWORD      VARCHAR(255)        NULL,
    GOOGLE_ID     VARCHAR(255)        NULL,
    FACEBOOK_ID   VARCHAR(255)        NULL,
    IS_ENABLE     BIT                 NOT NULL DEFAULT b'1',
    IS_DELETED    BIT                 NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table KHACH_HANG
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS KHACH_HANG
(
    ID                       INT          NOT NULL AUTO_INCREMENT,
    TAI_KHOAN_ID             INT          NOT NULL,
    TEN          VARCHAR(255) NOT NULL,
    NGAY_SINH                DATE         NULL,
    GIOI_TINH                VARCHAR(10)  NULL,
    SO_LAN_TU_CHOI_NHAN_HANG INT          NULL     DEFAULT 0,
    IS_DELETED               BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (TAI_KHOAN_ID) REFERENCES TAI_KHOAN (ID)
);

-- -----------------------------------------------------
-- Table NHAN_VIEN
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS NHAN_VIEN
(
    ID            INT          NOT NULL AUTO_INCREMENT,
    TAI_KHOAN_ID  INT          NOT NULL,
    TEN VARCHAR(255) NOT NULL,
    IS_DELETED    BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (TAI_KHOAN_ID) REFERENCES TAI_KHOAN (ID)
);

-- -----------------------------------------------------
-- Table NHAN_VIEN_LOG
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS NHAN_VIEN_LOG
(
    ID           INT      NOT NULL AUTO_INCREMENT,
    NHAN_VIEN_ID INT      NOT NULL,
    NOI_DUNG     TEXT     NULL,
    THOI_GIAN    DATETIME NOT NULL,
    IS_DELETED   BIT      NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (NHAN_VIEN_ID) REFERENCES NHAN_VIEN (ID)
);

-- -----------------------------------------------------
-- Table SAN_PHAM
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SAN_PHAM
(
    ID             INT          NOT NULL AUTO_INCREMENT,
    THUONG_HIEU_ID INT          NULL,
    CHAT_LIEU_ID   INT          NULL,
    KIEU_DANG_ID   INT          NULL,
    MA_SAN_PHAM    VARCHAR(50)  NOT NULL,
    TEN   VARCHAR(255) NOT NULL,
    TRANG_THAI     VARCHAR(50)  NULL,
    MO_TA          TEXT         NULL,
    ANH_URL        VARCHAR(255) NULL,
    IS_DELETED     BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table SAN_PHAM_CHI_TIET
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SAN_PHAM_CHI_TIET
(
    ID              INT            NOT NULL AUTO_INCREMENT,
    SAN_PHAM_ID     INT            NOT NULL,
    KICH_THUOC_ID   INT            NULL,
    MAU_SAC_ID      INT            NULL,
    DOT_GIAM_GIA_ID INT            NULL,
    GIA_BAN         DECIMAL(15, 2) NOT NULL,
    SO_LUONG        INT            NOT NULL,
    BARCODE         VARCHAR(100)   NULL,
    IS_DELETED      BIT            NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (SAN_PHAM_ID) REFERENCES SAN_PHAM (ID)
);

-- -----------------------------------------------------
-- Table DANH_MUC
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DANH_MUC
(
    ID           INT          NOT NULL AUTO_INCREMENT,
    TEN VARCHAR(255) NOT NULL,
    TRANG_THAI   VARCHAR(50)  NULL,
    IS_DELETED   BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table SAN_PHAM_DANH_MUC
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SAN_PHAM_DANH_MUC
(
    SAN_PHAM_ID INT NOT NULL,
    DANH_MUC_ID INT NOT NULL,
    PRIMARY KEY (SAN_PHAM_ID, DANH_MUC_ID),
    FOREIGN KEY (SAN_PHAM_ID) REFERENCES SAN_PHAM (ID),
    FOREIGN KEY (DANH_MUC_ID) REFERENCES DANH_MUC (ID)
);

-- -----------------------------------------------------
-- Table THUONG_HIEU
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS THUONG_HIEU
(
    ID              INT          NOT NULL AUTO_INCREMENT,
    TEN VARCHAR(255) NOT NULL,
    TRANG_THAI      VARCHAR(50)  NULL,
    IS_DELETED      BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table CHAT_LIEU
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS CHAT_LIEU
(
    ID            INT          NOT NULL AUTO_INCREMENT,
    TEN VARCHAR(255) NOT NULL,
    TRANG_THAI    VARCHAR(50)  NULL,
    IS_DELETED    BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table KIEU_DANG
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS KIEU_DANG
(
    ID            INT          NOT NULL AUTO_INCREMENT,
    TEN VARCHAR(255) NOT NULL,
    TRANG_THAI    VARCHAR(50)  NULL,
    IS_DELETED    BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table KICH_THUOC
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS KICH_THUOC
(
    ID             INT         NOT NULL AUTO_INCREMENT,
    TEN VARCHAR(50) NOT NULL,
    TRANG_THAI     VARCHAR(50) NULL,
    IS_DELETED     BIT         NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table MAU_SAC
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS MAU_SAC
(
    ID          INT         NOT NULL AUTO_INCREMENT,
    TEN VARCHAR(50) NOT NULL,
    TRANG_THAI  VARCHAR(50) NULL,
    IS_DELETED  BIT         NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table DOT_GIAM_GIA
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DOT_GIAM_GIA
(
    ID                 INT            NOT NULL AUTO_INCREMENT,
    MA                 VARCHAR(50)    NOT NULL,
    TEN                VARCHAR(255)   NOT NULL,
    MO_TA              TEXT           NULL,
    THOI_GIAN_BAT_DAU  DATETIME       NOT NULL,
    THOI_GIAN_KET_THUC DATETIME       NOT NULL,
    LOAI_CHIET_KHAU    VARCHAR(50)    NULL,
    GIA_TRI_GIAM       DECIMAL(15, 2) NULL,
    GIAM_TOI_DA        DECIMAL(15, 2) NULL,
    TRANG_THAI         VARCHAR(50)    NULL,
    IS_DELETED         BIT            NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table PHIEU_GIAM_GIA
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS PHIEU_GIAM_GIA
(
    ID                INT            NOT NULL AUTO_INCREMENT,
    MA                VARCHAR(50)    NOT NULL,
    TEN               VARCHAR(255)   NOT NULL,
    SO_LUONG          INT            NOT NULL,
    HOA_DON_TOI_THIEU DECIMAL(15, 2) NULL,
    LOAI_HINH_GIAM    VARCHAR(50)    NULL,
    GIA_TRI_GIAM      DECIMAL(15, 2) NULL,
    GIAM_TOI_DA       DECIMAL(15, 2) NULL,
    NGAY_BAT_DAU      DATETIME       NOT NULL,
    NGAY_KET_THUC     DATETIME       NOT NULL,
    DOI_TUONG_GIAM    VARCHAR(255)   NULL,
    TRANG_THAI        VARCHAR(50)    NULL,
    IS_DELETED        BIT            NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);

-- -----------------------------------------------------
-- Table PHIEU_GIAM_GIA_KHACH_HANG
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS PHIEU_GIAM_GIA_KHACH_HANG
(
    ID                INT NOT NULL AUTO_INCREMENT,
    KHACH_HANG_ID     INT NOT NULL,
    PHIEU_GIAM_GIA_ID INT NOT NULL,
    IS_DELETED        BIT NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (KHACH_HANG_ID) REFERENCES KHACH_HANG (ID),
    FOREIGN KEY (PHIEU_GIAM_GIA_ID) REFERENCES PHIEU_GIAM_GIA (ID)
);

-- -----------------------------------------------------
-- Table GIO_HANG_CHI_TIET
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS GIO_HANG_CHI_TIET
(
    ID                   INT      NOT NULL AUTO_INCREMENT,
    KHACH_HANG_ID        INT      NOT NULL,
    SAN_PHAM_CHI_TIET_ID INT      NOT NULL,
    SO_LUONG             INT      NOT NULL,
    NGAY_THEM_VAO        DATETIME NOT NULL,
    IS_DELETED           BIT      NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (KHACH_HANG_ID) REFERENCES KHACH_HANG (ID),
    FOREIGN KEY (SAN_PHAM_CHI_TIET_ID) REFERENCES SAN_PHAM_CHI_TIET (ID)
);

-- -----------------------------------------------------
-- Table THONG_BAO
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS THONG_BAO
(
    ID           INT          NOT NULL AUTO_INCREMENT,
    TAI_KHOAN_ID INT          NOT NULL,
    URL          VARCHAR(255) NULL,
    NOI_DUNG     TEXT         NULL,
    THOI_GIAN    DATETIME     NOT NULL,
    TRANG_THAI   VARCHAR(50)  NULL,
    IS_DELETED   BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (TAI_KHOAN_ID) REFERENCES TAI_KHOAN (ID)
);

-- -----------------------------------------------------
-- Table DIA_CHI
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS DIA_CHI
(
    ID                INT          NOT NULL AUTO_INCREMENT,
    KHACH_HANG_ID     INT          NOT NULL,
    HO_TEN_NGUOI_NHAN VARCHAR(255) NOT NULL,
    SO_DIEN_THOAI     VARCHAR(20)  NOT NULL,
    PROVINCE_ID       INT          NULL,                  -- or VARCHAR depending on your province ID type
    DISTRICT_ID       INT          NULL,                  -- or VARCHAR depending on your district ID type
    WARD_CODE         VARCHAR(20)  NULL,
    ADDRESS           VARCHAR(255) NOT NULL,
    IS_DEFAULT        BIT          NOT NULL DEFAULT b'0',
    IS_DELETED        BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (KHACH_HANG_ID) REFERENCES KHACH_HANG (ID)
);

-- -----------------------------------------------------
-- Table REVIEWS
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS REVIEWS
(
    ID            INT          NOT NULL AUTO_INCREMENT,
    SAN_PHAM_ID   INT          NOT NULL,
    KHACH_HANG_ID INT          NOT NULL,
    THOI_GIAN     DATETIME     NOT NULL,
    NOI_DUNG      TEXT         NULL,
    ANH           VARCHAR(255) NULL,
    STATUS        VARCHAR(50)  NULL,
    UP_VOTE       INT          NULL     DEFAULT 0,
    DOWN_VOTE     INT          NULL     DEFAULT 0,
    IS_DELETED    BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (SAN_PHAM_ID) REFERENCES SAN_PHAM (ID),
    FOREIGN KEY (KHACH_HANG_ID) REFERENCES KHACH_HANG (ID)
);

-- -----------------------------------------------------
-- Table REPLIES
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS REPLIES
(
    ID           INT      NOT NULL AUTO_INCREMENT,
    REVIEW_ID    INT      NOT NULL,
    NHAN_VIEN_ID INT      NULL,                  -- Allow NULL for replies from non-employees.  Consider a USER_ID.
    NOI_DUNG     TEXT     NOT NULL,
    THOI_GIAN    DATETIME NOT NULL,
    IS_DELETED   BIT      NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (REVIEW_ID) REFERENCES REVIEWS (ID),
    FOREIGN KEY (NHAN_VIEN_ID) REFERENCES NHAN_VIEN (ID)
);

-- -----------------------------------------------------
-- Table SAN_PHAM_UA_THICH
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS SAN_PHAM_UA_THICH
(
    ID            INT NOT NULL AUTO_INCREMENT,
    KHACH_HANG_ID INT NOT NULL,
    SAN_PHAM_ID   INT NOT NULL,
    IS_DELETED    BIT NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (KHACH_HANG_ID) REFERENCES KHACH_HANG (ID),
    FOREIGN KEY (SAN_PHAM_ID) REFERENCES SAN_PHAM (ID)
);

-- -----------------------------------------------------
-- Table HOA_DON
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS HOA_DON
(
    ID                     INT            NOT NULL AUTO_INCREMENT,
    KHACH_HANG_ID          INT            NOT NULL,
    PHIEU_GIAM_GIA_ID      INT            NULL,
    PHUONG_THUC_THANH_TOAN VARCHAR(255)   NOT NULL,
    PHI_VAN_CHUYEN         DECIMAL(15, 2) NULL,
    TONG_TIEN              DECIMAL(15, 2) NOT NULL,
    GIAM_MA_GIAM_GIA       DECIMAL(15, 2) NULL,
    SO_DIEN_THOAI          VARCHAR(20)    NOT NULL,
    DIA_CHI                VARCHAR(255)   NOT NULL,
    TEN_NGUOI_NHAN         VARCHAR(255)   NOT NULL,
    NGAY_NHAN_HANG         DATE           NULL,
    GHI_CHU                TEXT           NULL,
    LOAI_HOA_DON           VARCHAR(255)   NULL,
    TRANG_THAI             VARCHAR(255)   NULL,
    IS_DELETED             BIT            NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (KHACH_HANG_ID) REFERENCES KHACH_HANG (ID),
    FOREIGN KEY (PHIEU_GIAM_GIA_ID) REFERENCES PHIEU_GIAM_GIA (ID)
);

-- -----------------------------------------------------
-- Table HOA_DON_CHI_TIET
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS HOA_DON_CHI_TIET
(
    ID                   INT            NOT NULL AUTO_INCREMENT,
    HOA_DON_ID           INT            NOT NULL,
    SAN_PHAM_CHI_TIET_ID INT            NOT NULL,
    DOT_GIAM_GIA_ID      INT            NULL,
    GIA_GOC              DECIMAL(15, 2) NOT NULL,
    GIA_KHUYEN_MAI       DECIMAL(15, 2) NULL,
    SO_LUONG             INT            NOT NULL,
    IS_DELETED           BIT            NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (HOA_DON_ID) REFERENCES HOA_DON (ID),
    FOREIGN KEY (SAN_PHAM_CHI_TIET_ID) REFERENCES SAN_PHAM_CHI_TIET (ID),
    FOREIGN KEY (DOT_GIAM_GIA_ID) REFERENCES DOT_GIAM_GIA (ID)
);

-- -----------------------------------------------------
-- Table LICH_SU_HOA_DON
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS LICH_SU_HOA_DON
(
    ID           INT          NOT NULL AUTO_INCREMENT,
    HOA_DON_ID   INT          NOT NULL,
    TAI_KHOAN_ID INT          NULL,
    TIEU_DE      VARCHAR(255) NOT NULL,
    MO_TA        TEXT         NULL,
    THOI_GIAN    DATETIME     NOT NULL,
    IS_DELETED   BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (HOA_DON_ID) REFERENCES HOA_DON (ID),
    FOREIGN KEY (TAI_KHOAN_ID) REFERENCES TAI_KHOAN (ID)
);

-- -----------------------------------------------------
-- Table ANH
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ANH
(
    ID          INT          NOT NULL AUTO_INCREMENT,
    SAN_PHAM_ID INT          NULL,
    MAU_SAC_ID  INT          NULL,
    IS_DEFAULT  BIT          NOT NULL DEFAULT b'0',
    URL         VARCHAR(255) NOT NULL,
    IS_DELETED  BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID),
    FOREIGN KEY (SAN_PHAM_ID) REFERENCES SAN_PHAM (ID),
    FOREIGN KEY (MAU_SAC_ID) REFERENCES MAU_SAC (ID)
);

-- -----------------------------------------------------
-- Table BANNER
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS BANNER
(
    ID                 INT          NOT NULL AUTO_INCREMENT,
    ANH_URL            VARCHAR(255) NOT NULL,
    LINK_URL           VARCHAR(255) NOT NULL,
    THOI_GIAN_BAT_DAU  DATETIME     NOT NULL,
    THOI_GIAN_KET_THUC DATETIME     NOT NULL,
    PRIORITY           INT          NULL     DEFAULT 0,
    IS_DELETED         BIT          NOT NULL DEFAULT b'0',
    PRIMARY KEY (ID)
);
ALTER TABLE DATN.san_pham_chi_tiet ADD CONSTRAINT san_pham_chi_tiet_kich_thuoc_FK FOREIGN KEY (KICH_THUOC_ID) REFERENCES DATN.kich_thuoc(ID);
ALTER TABLE DATN.san_pham_chi_tiet ADD CONSTRAINT san_pham_chi_tiet_mau_sac_FK FOREIGN KEY (MAU_SAC_ID) REFERENCES DATN.mau_sac(ID);
ALTER TABLE DATN.san_pham_chi_tiet ADD CONSTRAINT san_pham_chi_tiet_dot_giam_gia_FK FOREIGN KEY (DOT_GIAM_GIA_ID) REFERENCES DATN.dot_giam_gia(ID);
ALTER TABLE DATN.san_pham ADD CONSTRAINT san_pham_chat_lieu_FK FOREIGN KEY (CHAT_LIEU_ID) REFERENCES DATN.chat_lieu(ID);
ALTER TABLE DATN.san_pham ADD CONSTRAINT san_pham_kieu_dang_FK FOREIGN KEY (KIEU_DANG_ID) REFERENCES DATN.kieu_dang(ID);
ALTER TABLE DATN.san_pham ADD CONSTRAINT san_pham_thuong_hieu_FK FOREIGN KEY (THUONG_HIEU_ID) REFERENCES DATN.thuong_hieu(ID);
ALTER TABLE DATN.mau_sac ADD code varchar(10) NULL;
ALTER TABLE DATN.thuong_hieu ADD thumbnail varchar(255) NULL;
ALTER TABLE DATN.san_pham ADD CONSTRAINT san_pham_chat_lieu_FK_1 FOREIGN KEY (CHAT_LIEU_ID) REFERENCES DATN.chat_lieu(ID);
ALTER TABLE DATN.san_pham ADD CONSTRAINT san_pham_kieu_dang_FK_1 FOREIGN KEY (KIEU_DANG_ID) REFERENCES DATN.kieu_dang(ID);
