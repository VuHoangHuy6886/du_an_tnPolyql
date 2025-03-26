// Hàm chuyển đổi giá trị từ không dấu sang tiếng Việt có dấu
function convertToVietnamese(text) {
    const mapping = {
        "CHO_XAC_NHAN": "Chờ xác nhận",
        "XAC_NHAN": "Xác nhận",
        "DANG_CHUAN_BI_HANG": "Đang chuẩn bị hàng",
        "CHO_LAY_HANG": "Chờ lấy hàng",
        "LAY_HANG_THANH_CONG": "Lấy hàng thành công",
        "DANG_VAN_CHUYEN": "Đang vận chuyển",
        "DANG_GIAO": "Đang giao",
        "GIAO_HANG_THANH_CONG": "Giao hàng thành công",
        "GIAO_HANG_THAT_BAI": "Giao hàng thất bại",
        "CHO_CHUYEN_HOAN": "Chờ chuyển hoàn",
        "THANH_CONG": "Thành công",
        "DA_HUY": "Đã hủy",
        "THAT_LAC": "Thất lạc",
        "COD": "Thanh toán khi nhận hàng",
        "BANK_TRANSFER": "Chuyển khoản ngân hàng",
        "XUAT_KHO": "Xuất kho",
        "TAI_QUAY": "Tại quầy",
        "TIEN_MAT": "Tiền mặt",
    };
    return mapping[text] || text;
}

let currentOrder = null;
let orderId = new URLSearchParams(window.location.search).get('id') || 1;
let selectedNewStatus = null;

// Danh sách trạng thái
const statuses = [
    "CHO_XAC_NHAN", "XAC_NHAN", "DANG_CHUAN_BI_HANG",
    "CHO_LAY_HANG", "LAY_HANG_THANH_CONG", "DANG_VAN_CHUYEN",
    "DANG_GIAO", "GIAO_HANG_THANH_CONG", "GIAO_HANG_THAT_BAI",
    "CHO_CHUYEN_HOAN", "THANH_CONG", "DA_HUY", "THAT_LAC"
];

// Map icon cho trạng thái
const statusIconMap = {
    "CHO_XAC_NHAN": "fa-hourglass-start",
    "XAC_NHAN": "fa-check-circle",
    "DANG_CHUAN_BI_HANG": "fa-box-open",
    "CHO_LAY_HANG": "fa-truck-loading",
    "LAY_HANG_THANH_CONG": "fa-box",
    "DANG_VAN_CHUYEN": "fa-truck",
    "DANG_GIAO": "fa-shipping-fast",
    "GIAO_HANG_THANH_CONG": "fa-check",
    "GIAO_HANG_THAT_BAI": "fa-times-circle",
    "CHO_CHUYEN_HOAN": "fa-rotate-left",
    "THANH_CONG": "fa-trophy",
    "DA_HUY": "fa-ban",
    "THAT_LAC": "fa-exclamation-triangle"
};

document.addEventListener("DOMContentLoaded", function() {
// Render timeline theo loại đơn hàng
    // Vẽ timeline
    function renderTimeline(order) {
        const timeline = document.getElementById('timeline');
        if (!timeline) return;
        timeline.innerHTML = '';

        // Tùy theo trạng thái đơn hàng, xác định các bước hiển thị
        let timelineData = [];
        if (order.trangThai === "DA_HUY") {
            // Đơn hàng hủy: hiển thị đến CHO_LAY_HANG, rồi DA_HUY
            let idx = statuses.indexOf("CHO_LAY_HANG");
            timelineData = statuses.slice(0, idx + 1);
            timelineData.push("DA_HUY");
        } else if (
            order.trangThai === "GIAO_HANG_THAT_BAI" ||
            order.trangThai === "CHO_CHUYEN_HOAN" ||
            order.trangThai === "THAT_LAC"
        ) {
            // Giao thất bại / Chờ hoàn / Thất lạc => hiển thị đến GIAO_HANG_THAT_BAI, rồi CHO_CHUYEN_HOAN
            let idx = statuses.indexOf("DANG_GIAO");
            timelineData = statuses.slice(0, idx + 1);
            timelineData.push("GIAO_HANG_THAT_BAI");
            timelineData.push("CHO_CHUYEN_HOAN");
        } else {
            // Đơn hàng bình thường => hiển thị đến GIAO_HANG_THANH_CONG
            let idx = statuses.indexOf("GIAO_HANG_THANH_CONG");
            timelineData = statuses.slice(0, idx + 1);

            // Nếu đơn hàng thực tế đã sang GIAO_HANG_THAT_BAI hay THANH_CONG, thêm chúng
            if (order.trangThai === "GIAO_HANG_THAT_BAI") {
                timelineData.push("GIAO_HANG_THAT_BAI");
            }
            if (order.trangThai === "THANH_CONG") {
                timelineData.push("THANH_CONG");
            }
        }

        // Xác định chỉ số hiện tại
        let currentTimelineIndex = timelineData.indexOf(order.trangThai);
        if (currentTimelineIndex === -1) currentTimelineIndex = 0;

        // Vẽ
        timelineData.forEach((status, index) => {
            const stepDiv = document.createElement('div');
            stepDiv.classList.add('step');
            if (index < currentTimelineIndex) stepDiv.classList.add('completed');
            else if (index === currentTimelineIndex) stepDiv.classList.add('active');

            const indicator = document.createElement('span');
            indicator.classList.add('step-indicator');
            const icon = document.createElement('i');
            icon.classList.add('fa-solid', statusIconMap[status] || 'fa-circle');
            indicator.appendChild(icon);

            const nameDiv = document.createElement('div');
            nameDiv.classList.add('step-name');
            nameDiv.textContent = convertToVietnamese(status);

            stepDiv.appendChild(indicator);
            stepDiv.appendChild(nameDiv);
            timeline.appendChild(stepDiv);
        });
    }

// Vô hiệu hóa các thao tác chỉnh sửa
    function disableOrderActions(status) {
        // Nút "Thêm sản phẩm"
        const addBtn = document.getElementById('addProductBtn');
        if (addBtn) addBtn.disabled = status;
        // Các ô input cập nhật số lượng
        // Các ô input cập nhật số lượng
        document.querySelectorAll('.updateQtyInput').forEach(input => input.disabled = status);

        // Nút xóa chi tiết
        document.querySelectorAll('.deleteDetailBtn').forEach(btn => btn.disabled = status);

        // Nút hủy đơn
        const cancelBtn = document.getElementById('cancelOrderBtn');
        if (cancelBtn) cancelBtn.disabled = status;
        const btnUpdate = document.getElementById('btn-info-khach-hang');
        if (btnUpdate) btnUpdate.disabled = status;

        // Vô hiệu hóa luôn các thành phần trong bảng #productListBody
        const productListBody = document.getElementById('productListBody');
        if (productListBody) {
            productListBody.querySelectorAll('input, button, select, textarea')
                .forEach(el => el.disabled = status);
        }
    }

// Nút hành động đặc biệt (nếu cần)
    function setupSpecialActions(order) {
        const container = document.getElementById('specialActionContainer');
        if (!container) return;

        container.innerHTML = '';
        if (order.trangThai === "GIAO_HANG_THAT_BAI" || order.trangThai === "THAT_LAC") {
            // Chuyển sang CHO_CHUYEN_HOAN
            const btn = document.createElement('button');
            btn.textContent = (order.trangThai === "GIAO_HANG_THAT_BAI") ? "Hoàn hàng" : "Xử lý khiếu nại";
            btn.classList.add('btn', (order.trangThai === "GIAO_HANG_THAT_BAI") ? 'btn-warning' : 'btn-info');
            btn.addEventListener('click', function() {
                selectedNewStatus = "CHO_CHUYEN_HOAN";
                document.getElementById('statusUpdateMessage').textContent =
                    `Chuyển sang "${convertToVietnamese(selectedNewStatus)}"? (Sẽ cập nhật số lượng sản phẩm về kho)`;
                $('#updateStatusModal').modal('show');
            });
            container.appendChild(btn);
        }
    }

// Cập nhật UI đơn hàng
    function updateUI(order) {
        // Vẽ timeline
        renderTimeline(order);
        // Nút đặc biệt
        setupSpecialActions(order);

        // Từ "LAY_HANG_THANH_CONG" trở đi, hoặc DA_HUY, CHO_CHUYEN_HOAN, THANH_CONG, GIAO_HANG_THAT_BAI => disable
        // Từ "LAY_HANG_THANH_CONG" trở đi, hoặc DA_HUY, CHO_CHUYEN_HOAN, THANH_CONG, GIAO_HANG_THAT_BAI => disable
        const disableIndex = statuses.indexOf("LAY_HANG_THANH_CONG");
        const currentIndex = statuses.indexOf(order.trangThai);

        // Chia làm 2 khối để rõ ràng:
        // 1) Nếu đơn hàng vẫn là DA_HUY => disable
        if (order.trangThai === "DA_HUY") {
            disableOrderActions(true);
        }
        // 2) Hoặc nếu >= LAY_HANG_THANH_CONG, HOẶC CHO_CHUYEN_HOAN, THÀNH_CÔNG, GIAO_HANG_THẤT_BẠI => disable
        else if (
            currentIndex >= disableIndex ||
            order.trangThai === "CHO_CHUYEN_HOAN" ||
            order.trangThai === "THANH_CONG" ||
            order.trangThai === "GIAO_HANG_THAT_BAI"
        ) {
            disableOrderActions(true);
        }
    }

// ---------------- Xử lý cập nhật trạng thái ---------------- //

// Tính trạng thái kế tiếp (ngoại trừ "DANG_GIAO" => hiển thị popup)
    function computeNextStatus(order) {
        const current = order.trangThai;

        // Đơn hàng bị hủy => khôi phục => DANG_CHUAN_BI_HANG
        if (current === "DA_HUY") return "DANG_CHUAN_BI_HANG";

        // Giao hàng thất bại => sang CHO_CHUYEN_HOAN
        if (current === "GIAO_HANG_THAT_BAI") return "CHO_CHUYEN_HOAN";

        // CHO_CHUYEN_HOAN hoặc THANH_CONG => null
        if (current === "CHO_CHUYEN_HOAN" || current === "THANH_CONG") return null;

        // Nếu đang DANG_GIAO => null => code chặn => hiển popup chọn
        if (current === "DANG_GIAO") return null;

        // Bình thường => tìm trạng thái kế tiếp
        let idx = statuses.indexOf(current);
        if (idx === -1) return null;

        if (current !== "GIAO_HANG_THANH_CONG") {
            if (idx + 1 < statuses.length) {
                return statuses[idx + 1];
            } else {
                return null;
            }
        }
        // Nếu đang GIAO_HANG_THANH_CONG => sang GIAO_HANG_THAT_BAI
        return "GIAO_HANG_THAT_BAI";
    }

// Mở modal cập nhật trạng thái
    function setupUpdateStatusModal() {
        const openBtn = document.getElementById('openUpdateStatusModalBtn');
        if (!openBtn) return;

        openBtn.addEventListener('click', function() {
            // Nếu đang DANG_GIAO => cho user chọn
            if (currentOrder && currentOrder.trangThai === "DANG_GIAO") {
                Swal.fire({
                    title: 'Trạng thái kế tiếp?',
                    text: 'Vui lòng chọn kết quả giao hàng',
                    icon: 'question',
                    showCancelButton: true,
                    confirmButtonText: 'Giao hàng thành công',
                    cancelButtonText: 'Giao hàng thất bại',
                    reverseButtons: true
                }).then((result) => {
                    if (result.isConfirmed) {
                        selectedNewStatus = "GIAO_HANG_THANH_CONG";
                        showConfirmModal(selectedNewStatus);
                    } else if (result.dismiss === Swal.DismissReason.cancel) {
                        selectedNewStatus = "GIAO_HANG_THAT_BAI";
                        showConfirmModal(selectedNewStatus);
                    }
                });
                return;
            }

            // Nếu không phải DANG_GIAO => logic cũ
            if (!currentOrder) return;
            const nextStatus = computeNextStatus(currentOrder);
            if (!nextStatus) {
                Swal.fire('Thông báo', 'Không thể chuyển trạng thái từ trạng thái hiện tại', 'info');
                return;
            }
            selectedNewStatus = nextStatus;
            let message = `Bạn có chắc muốn chuyển sang "${convertToVietnamese(nextStatus)}"?`;

            if (currentOrder.trangThai === "DA_HUY") {
                message += " (Khôi phục đơn hàng - trừ số lượng sản phẩm từ kho)";
            } else if (currentOrder.trangThai === "GIAO_HANG_THAT_BAI" || currentOrder.trangThai === "THAT_LAC") {
                message += " (Trả hàng về kho - cộng số lượng sản phẩm)";
            }
            document.getElementById('statusUpdateMessage').textContent = message;

            $('#updateStatusModal').modal('show');
        });
    }

// Hàm hiển thị xác nhận (sau khi user chọn GIAO_HANG_THANH_CONG hoặc GIAO_HANG_THAT_BAI)
    function showConfirmModal(nextStatus) {
        selectedNewStatus = nextStatus;
        let message = `Bạn có chắc muốn chuyển sang "${convertToVietnamese(nextStatus)}"?`;

        if (nextStatus === "GIAO_HANG_THAT_BAI") {
            message += " (Trả hàng về kho)";
        }
        document.getElementById('statusUpdateMessage').textContent = message;

        $('#updateStatusModal').modal('show');
    }

// Submit form cập nhật trạng thái
    function setupUpdateStatusForm() {
        const form = document.getElementById('updateStatusForm');
        if (!form) return;

        form.addEventListener('submit', function(e) {
            e.preventDefault();
            const statusNote = document.getElementById('statusNote').value;
            const payload = {
                trangThai: selectedNewStatus,
                ghiChu: statusNote
            };

            // Nếu đang "DA_HUY" => gọi /restore, nếu không => /updateStatus
            let endpoint = '/api/orders/' + orderId + '/updateStatus';
            if (currentOrder && currentOrder.trangThai === "DA_HUY") {
                endpoint = '/api/orders/' + orderId + '/restore';
                disableOrderActions(true);
            }
            fetch(endpoint, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
                .then(response => {
                    if (!response.ok) throw new Error('Lỗi cập nhật trạng thái');
                    return response.json();
                })
                .then(() => {
                    Swal.fire('Thành công', 'Cập nhật trạng thái thành công!', 'success');

                    $('#updateStatusModal').modal('hide');
                    fetchOrderDetails();
                    if (currentOrder && currentOrder.trangThai === "DA_HUY") {
                        disableOrderActions(false);
                    }
                })
                .catch(error => {
                    console.error(error);
                    Swal.fire('Lỗi', error.message, 'error');
                });
        });
    }

// Xử lý nút hủy đơn hàng
    function setupCancelOrderButton() {
        const cancelBtn = document.getElementById('cancelOrderBtn');
        if (!cancelBtn) return;

        cancelBtn.addEventListener('click', function() {
            Swal.fire({
                title: 'Xác nhận hủy đơn hàng?',
                text: 'Bạn có chắc chắn muốn hủy đơn hàng này?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Đồng ý',
                cancelButtonText: 'Hủy'
            }).then(result => {
                if (result.isConfirmed) {
                    fetch('/api/orders/' + orderId + '/cancel', { method: 'POST' })
                        .then(response => {
                            if (!response.ok) throw new Error('Lỗi hủy đơn hàng: ' + response.statusText);
                            return response.json();
                        })
                        .then(() => {
                            Swal.fire('Thành công', 'Đơn hàng đã được hủy', 'success');
                            fetchOrderDetails();
                        })
                        .catch(error => {
                            console.error("Error canceling order:", error);
                            Swal.fire('Lỗi', error.message, 'error');
                        });
                }
            });
        });
    }

// ========== Lấy thông tin đơn hàng & cập nhật UI ========== //
    function fetchOrderDetails() {
        fetch('/api/orders/' + orderId)
            .then(response => {
                if (!response.ok) throw new Error('Không tìm thấy đơn hàng');
                return response.json();
            })
            .then(order => {
                currentOrder = order;
                       // Giả sử bạn có hàm này để hiển thị danh sách sản phẩm
                fetchAndPopulateProductList(orderId);
                // updateUI
                updateUI(order);
                // Hiển thị thông tin đơn hàng
                populateOrderDetails(order);


                // Nếu đơn hàng đang ở "LAY_HANG_THANH_CONG", disable thêm/sửa
                if (order.trangThai === 'LAY_HANG_THANH_CONG') {
                    disableOrderActions(true)
                }
            })
            .catch(error => {
                console.error(error);
                Swal.fire('Lỗi', error.message, 'error');
            });
    }

// Hiển thị thông tin đơn hàng, timeline, người nhận
    function populateOrderDetails(order) {
        // Chỉ hiển thị data, KHÔNG setup event listener ở đây (tránh bind nhiều lần)
        const orderInfo = document.getElementById('orderInfo');
        if (orderInfo) {
            orderInfo.innerHTML = `
            <div class="col-md-4 mb-2">
                <label><strong>Mã đơn hàng:</strong></label>
                <div>#HD${order.id}</div>
            </div>
            <div class="col-md-4 mb-2">
                <label><strong>Trạng thái:</strong></label>
                <div>${convertToVietnamese(order.trangThai || "")}</div>
            </div>
            <div class="col-md-4 mb-2">
                <label><strong>Tổng tiền:</strong></label>
                <div>${order.tongTien || 0}</div>
            </div>
            <div class="col-md-4 mb-2">
                <label><strong>Phí giảm giá:</strong></label>
                <div>${order.phiGiamGia || 0}</div>
            </div>
            <div class="col-md-4 mb-2">
                <label><strong>Phương thức thanh toán:</strong></label>
                <div>${convertToVietnamese(order.phuongThucThanhToan || "")}</div>
            </div>
            <div class="col-md-4 mb-2">
                <label><strong>Phí vận chuyển:</strong></label>
                <div>${order.phiVanChuyen || 0}</div>
            </div>
            <div class="col-md-4 mb-2">
                <label><strong>Loại hóa đơn:</strong></label>
                <div>${convertToVietnamese(order.loaiHoaDon || "")}</div>
            </div>
            <div class="col-md-12 mb-2">
                <label><strong>Ghi chú:</strong></label>
                <div>${order.ghiChu || ''}</div>
            </div>
        `;
        }

        const customerInfo = document.getElementById('customerInfo');
        if (customerInfo) {
            if (order) {
                customerInfo.innerHTML = `
                <div class="col-md-4 mb-2">
                    <label><strong>Tên người nhận:</strong></label>
                    <div>${order.tenNguoiNhan || 'Chưa cập nhật'}</div>
                </div>
                <div class="col-md-4 mb-2">
                    <label><strong>Địa chỉ giao hàng:</strong></label>
                    <div>${order.diaChi || ''}</div>
                </div>
                <div class="col-md-4 mb-2">
                    <label><strong>Số điện thoại:</strong></label>
                    <div>${order.soDienThoai || ''}</div>
                </div>
            `;
            } else {
                customerInfo.innerHTML = `
                <div class="col-md-12 mb-2">
                    <label><strong>Thông tin người nhận:</strong></label>
                    <div>Chưa cập nhật</div>
                </div>
            `;
            }
        }

        // Nếu có nút "Thêm" để mở modal add-to-cart
        $(document).on("click", ".addProductToOrderBtn", function () {
            const productData = $(this).data("product");
            openAddToCartModal(productData);
        });
    }

// Khi DOM load
    let pendingDeletion = null;
    let deletionTimer = null;
    const undoDuration = 120; // thời gian hoàn tác (giây)

    document.getElementById('productListBody').addEventListener('click', function(e) {
        if (e.target.closest('.deleteDetailBtn')) {
            const btn = e.target.closest('.deleteDetailBtn');
            const detailId = btn.getAttribute('data-detail-id');
            Swal.fire({
                title: 'Xóa sản phẩm?',
                text: 'Bạn có chắc muốn xóa sản phẩm này khỏi đơn hàng?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Đồng ý',
                cancelButtonText: 'Hủy'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Lưu lại detailId để có thể hoàn tác
                    pendingDeletion = detailId;
                    fetch('/api/orders/' + orderId + '/details/' + detailId + '/delete', {
                        method: 'POST'
                    })
                        .then(response => {
                            if (!response.ok) throw new Error('Lỗi xóa sản phẩm');
                            return response.json();
                        })
                        .then(() => {
                            Swal.fire('Thành công', 'Xóa sản phẩm thành công!', 'success');
                            // Nếu muốn cập nhật lại giao diện ngay, gọi fetchOrderDetails();
                            fetchAndPopulateProductList(orderId)
                            // Hiển thị thông báo hoàn tác
                            fetchOrderDetails();

                            showUndoNotification();

                        })
                        .catch(error => {
                            console.error("Error deleting product detail:", error);
                            Swal.fire('Lỗi', error.message, 'error');
                        });
                }
            });
        }
    });

    function fetchAndPopulateProductList(hoaDonId) {
        fetch('/api/orders/' + hoaDonId + '/chi-tiet')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Không tìm thấy dữ liệu chi tiết đơn hàng');
                }
                return response.json();
            })
            .then(data => {
                // Giả sử API trả về một mảng các HoaDonChiTietDTO
                populateProductList(data);
            })
            .catch(error => {
                console.error(error);
                Swal.fire('Lỗi', error.message, 'error');
            });
    }
    // 1. Setup event listener CHỈ 1 lần
        setupUpdateStatusModal();
        setupUpdateStatusForm();
        setupCancelOrderButton();

        // 2. Gọi hàm fetchOrderDetails() để lấy và hiển thị đơn hàng
        fetchOrderDetails();


    // Hàm hiển thị danh sách sản phẩm


// Hàm render danh sách sản phẩm (không cần kiểm tra isDeleted vì API đã lọc)
    function populateProductList(productList) {
        const productListBody = document.getElementById('productListBody');
        productListBody.innerHTML = '';
        if (productList && productList.length > 0) {
            productList.forEach((detail, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                <td>${index + 1}</td>
                <td>${detail.sanPhamChiTietId}</td>
                <td>${detail.tenSanPham || ''}</td>
                <td>${detail.mauSac || ''}</td>
                <td>${detail.kichThuoc || ''}</td>
                <td>${detail.giaGoc}</td>
                <td>${detail.giaKhuyenMai || 0}</td>
                <td>
                    <input type="number" value="${detail.soLuong}" min="1" data-detail-id="${detail.id}"
                           class="form-control form-control-sm updateQtyInput" style="width:80px;">
                </td>
                <td>${(detail.giaKhuyenMai || detail.giaGoc) * detail.soLuong}</td>
                <td>
                    <button class="btn btn-sm btn-danger deleteDetailBtn" data-detail-id="${detail.id}">
                        <i class="fa-solid fa-trash"></i> Xóa
                    </button>
                </td>
            `;
                productListBody.appendChild(row);
            });
        } else {
            productListBody.innerHTML = `<tr><td colspan="8" class="text-center">Không có sản phẩm nào</td></tr>`;
        }
    }

    // Khi modal cập nhật thông tin đơn hàng mở, load dữ liệu cũ
    $('#updateOrderModal').on('show.bs.modal', function () {
        if (currentOrder) {
            document.getElementById('tenNguoiNhan').value = currentOrder.tenNguoiNhan || '';
            document.getElementById('diaChi').value = currentOrder.diaChi || '';
            document.getElementById('soDienThoai').value = currentOrder.soDienThoai || '';
            document.getElementById('phiVanChuyen').value = currentOrder.phiVanChuyen || '';
            document.getElementById('ghiChu').value = currentOrder.ghiChu || '';
        }
    });

    document.getElementById('addProductBtn').addEventListener('click', function() {
        var addProductModal = new bootstrap.Modal(document.getElementById('addProductModal'));
        addProductModal.show();
        if ($.fn.DataTable.isDataTable("#dataTable")) {
            $("#dataTable").DataTable().ajax.reload();
        }
    });

    // DataTable: hiển thị danh sách sản phẩm (từ API /api/san-pham)
    function loadData() {
        return $('#dataTable').DataTable({
            destroy: true,
            processing: true,
            language: {
                sProcessing: "Đang tải dữ liệu...",
                sLengthMenu: "Hiển thị _MENU_ bản ghi",
                sZeroRecords: "Không tìm thấy dữ liệu",
                sInfo: "Hiển thị _START_ đến _END_ của _TOTAL_ bản ghi",
                sInfoEmpty: "Hiển thị 0 đến 0 của 0 bản ghi",
                sInfoFiltered: "(được lọc từ _MAX_ bản ghi)",
                sSearch: "Tìm kiếm:",
                oPaginate: {
                    sFirst: "Đầu",
                    sPrevious: "Trước",
                    sNext: "Tiếp",
                    sLast: "Cuối"
                }
            },
            ajax: {
                url: "/api/san-pham",
                type: "GET",
                cache: true,
                dataSrc: function (json) {
                    console.log("Dữ liệu trả về:", json);
                    return json.content || [];
                }
            },
            columns: [
                { data: "id", name: "id" },
                { data: "maSanPham", name: "maSanPham" },
                {
                    data: "ten",
                    name: "ten",
                    render: data => data && data.trim() !== "" ? data : "Không có dữ liệu"
                },
                {
                    data: "thuongHieu",
                    name: "thuongHieu",
                    render: data => data && data.ten ? data.ten : "Không có dữ liệu"
                },
                {
                    data: "soLuong",
                    name: "soLuong",
                    render: data => data !== null && data !== "" ? data : "Không có dữ liệu"
                },
                {
                    data: "anhUrl",
                    name: "anhUrl",
                    render: data => data ? `<img src="${data}" style="width:70px; height:100px;" alt="Ảnh">` : "Không có ảnh"
                },
                {
                    data: null,
                    render: function (data, type, row) {
                        return `<button type="button" class="btn btn-sm btn-success addProductToOrderBtn" data-product='${JSON.stringify(row)}'>
                        <i class="fas fa-shopping-cart"></i> Thêm
                      </button>`;
                    }
                }
            ],
            createdRow: function (row, data, dataIndex) {
                $(row).find('.addProductToOrderBtn').data('product', data);
            }
        });
    }

    // Khởi tạo DataTable khi trang load
    $(document).ready(function () {
        loadData();
    });

    // Sự kiện khi nhấn nút "Thêm" trong bảng sẽ mở modal add-to-cart
    $(document).on("click", ".addProductToOrderBtn", function () {
        var productData = $(this).data("product");
        openAddToCartModal(productData);
    });

    /***************** ADD-TO-CART FUNCTIONS *****************/

    /**
     * Mở modal add-to-cart và hiển thị thông tin sản phẩm, các tùy chọn biến thể…
     * @param {Object} product - Đối tượng sản phẩm.
     */
    function openAddToCartModal(product) {
        currentProduct = product;
        $("#modal-product-name").text(product.ten);
        $("#modal-quantity").val(1);
        // Reset biến thể
        selectedColor = null;
        selectedSize = null;

        // Build carousel ảnh dựa trên product.anhs, nếu có.
        var carouselInner = $("#modal-carousel-inner");
        carouselInner.empty();
        if (product.anhs && product.anhs.length > 0) {
            product.anhs.forEach(function (imgObj, index) {
                var activeClass = index === 0 ? " active" : "";
                var slide = `<div class="carousel-item${activeClass}" data-color="${imgObj.mauSacId}">
                        <img src="${imgObj.url}" class="d-block w-100" alt="${product.ten}" style="max-height:300px; object-fit:contain;">
                      </div>`;
                carouselInner.append(slide);
            });
        } else {
            carouselInner.append(`<div class="carousel-item active">
                                  <img src="${product.anhUrl}" class="d-block w-100" alt="${product.ten}" style="max-height:300px; object-fit:contain;">
                                </div>`);
        }
        $("#modal-carousel").carousel(0);

        // Render các tùy chọn biến thể (màu sắc, kích thước)
        renderVariantOptions(product);

        // Reset thông tin giá và tổng tiền
        $("#modal-price").text("N/A");
        $("#modal-total").text("Tổng tiền: N/A");
        $("#modal-total-warning").text("");

        // Mở modal add-to-cart
        $("#addCartModal").modal("show");
    }

    /**
     * Render các lựa chọn biến thể (màu sắc & kích thước) dựa trên thuộc tính sanPhamChiTiets của sản phẩm.
     * @param {Object} prod - Đối tượng sản phẩm.
     */
    function renderVariantOptions(prod) {
        var container = document.getElementById("modal-variant-container");
        container.innerHTML = ""; // Xóa nội dung cũ

        // Render phần màu sắc
        var colorLabel = document.createElement("div");
        colorLabel.className = "variant-label mb-1";
        colorLabel.textContent = "Màu sắc:";
        container.appendChild(colorLabel);

        var colorOptions = document.createElement("div");
        colorOptions.className = "color-options d-flex flex-wrap";
        var uniqueColors = [...new Set(prod.sanPhamChiTiets.map(ct => ct.mauSac.ten))];
        uniqueColors.forEach((colorName) => {
            var swatch = document.createElement("div");
            swatch.className = "color-swatch badge badge-pill badge-light mr-2 mb-2 text-dark";
            swatch.style.cursor = "pointer";
            swatch.style.border = "1px solid #ccc";
            swatch.style.padding = "5px 10px";
            swatch.textContent = colorName;
            swatch.addEventListener("click", function () {
                if (swatch.classList.contains("active")) {
                    swatch.classList.remove("active");
                    selectedColor = null;
                } else {
                    document.querySelectorAll(".color-swatch").forEach(el => el.classList.remove("active"));
                    swatch.classList.add("active");
                    selectedColor = colorName;
                }
                updateDependentOptions(prod);
                updateImages(prod);
                updateMainPrice(prod);
                updateTotalAmount(prod);
            });
            colorOptions.appendChild(swatch);
        });
        container.appendChild(colorOptions);

        // Render phần kích thước
        var sizeLabel = document.createElement("div");
        sizeLabel.className = "variant-label mt-3 mb-1";
        sizeLabel.textContent = "Kích thước:";
        container.appendChild(sizeLabel);

        var sizeOptions = document.createElement("div");
        sizeOptions.className = "size-options d-flex flex-wrap";
        var uniqueSizes = [...new Set(prod.sanPhamChiTiets.map(ct => ct.kichThuoc.ten))];
        uniqueSizes.forEach((sizeName) => {
            var swatch = document.createElement("div");
            swatch.className = "size-swatch badge badge-pill badge-light mr-2 mb-2 text-dark";
            swatch.style.cursor = "pointer";
            swatch.style.border = "1px solid #ccc";
            swatch.style.padding = "5px 10px";
            swatch.textContent = sizeName;
            swatch.addEventListener("click", function () {
                if (swatch.classList.contains("active")) {
                    swatch.classList.remove("active");
                    selectedSize = null;
                } else {
                    document.querySelectorAll(".size-swatch").forEach(el => el.classList.remove("active"));
                    swatch.classList.add("active");
                    selectedSize = sizeName;
                }
                updateDependentOptions(prod);
                updateMainPrice(prod);
                updateTotalAmount(prod);
            });
            sizeOptions.appendChild(swatch);
        });
        container.appendChild(sizeOptions);
    }

    /**
     * Cập nhật trạng thái các lựa chọn biến thể dựa trên sự phụ thuộc giữa màu sắc và kích thước.
     * Nếu một màu không có kích thước tương ứng hoặc ngược lại thì các swatch đó sẽ bị vô hiệu hóa.
     * @param {Object} prod - Đối tượng sản phẩm.
     */
    function updateDependentOptions(prod) {
        var colorToSizes = {};
        var sizeToColors = {};
        prod.sanPhamChiTiets.forEach(function(ct) {
            var color = ct.mauSac.ten;
            var size = ct.kichThuoc.ten;
            if (!colorToSizes[color]) { colorToSizes[color] = new Set(); }
            colorToSizes[color].add(size);
            if (!sizeToColors[size]) { sizeToColors[size] = new Set(); }
            sizeToColors[size].add(color);
        });
        document.querySelectorAll(".size-swatch").forEach(function(swatch) {
            var sizeName = swatch.textContent;
            if (selectedColor) {
                if (!colorToSizes[selectedColor] || !colorToSizes[selectedColor].has(sizeName)) {
                    swatch.style.opacity = "0.5";
                    swatch.style.pointerEvents = "none";
                    swatch.classList.remove("active");
                    if (selectedSize === sizeName) selectedSize = null;
                } else {
                    swatch.style.opacity = "1";
                    swatch.style.pointerEvents = "auto";
                }
            } else {
                swatch.style.opacity = "1";
                swatch.style.pointerEvents = "auto";
            }
        });
        document.querySelectorAll(".color-swatch").forEach(function(swatch) {
            var colorName = swatch.textContent;
            if (selectedSize) {
                if (!sizeToColors[selectedSize] || !sizeToColors[selectedSize].has(colorName)) {
                    swatch.style.opacity = "0.5";
                    swatch.style.pointerEvents = "none";
                    swatch.classList.remove("active");
                    if (selectedColor === colorName) selectedColor = null;
                } else {
                    swatch.style.opacity = "1";
                    swatch.style.pointerEvents = "auto";
                }
            } else {
                swatch.style.opacity = "1";
                swatch.style.pointerEvents = "auto";
            }
        });
    }

    /**
     * Cập nhật carousel hiển thị ảnh theo màu được chọn.
     * @param {Object} prod - Đối tượng sản phẩm.
     */
    function updateImages(prod) {
        if (selectedColor) {
            var targetSlide = document.querySelector(`#modal-carousel-inner .carousel-item[data-color='${selectedColor}']`);
            if (targetSlide) {
                var index = Array.from(document.querySelectorAll("#modal-carousel-inner .carousel-item")).indexOf(targetSlide);
                $("#modal-carousel").carousel(index);
            }
        }
    }

    /**
     * Cập nhật hiển thị giá sản phẩm theo biến thể được chọn.
     * @param {Object} prod - Đối tượng sản phẩm.
     */
    function updateMainPrice(prod) {
        function formatCurrency(value) {
            return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
        }
        if (selectedColor && selectedSize) {
            var matchingDetail = prod.sanPhamChiTiets.find(function(ct) {
                return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
            });
            if (matchingDetail) {
                var giaSanPhamHtml = '';
                if (matchingDetail.isPromotionProduct) {
                    if (matchingDetail.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
                        giaSanPhamHtml = `
                <div class="d-flex align-items-end">
                  <h5 class="me-3">Giá gốc:</h5>
                  <del class="h5">${formatCurrency(matchingDetail.giaBan)}</del>
                  <span class="ml-1" style="color: red;"> - ${matchingDetail.dotGiamGia.giaTriGiam}%</span>
                </div>
                <div class="d-flex align-items-end">
                  <h5 class="me-3">Giá KM:</h5>
                  <span class="h5 font-weight-bold">${formatCurrency(matchingDetail.giaChietKhau)}</span>
                </div>
                <div class="d-flex align-items-end">
                  <span>Số lượng tồn: ${matchingDetail.soLuong}</span>
                </div>
              `;
                    } else {
                        giaSanPhamHtml = `
                <div class="d-flex align-items-end">
                  <h5 class="me-3">Giá gốc:</h5>
                  <del class="h5">${formatCurrency(matchingDetail.giaBan)}</del>
                  <span class="ml-1"> - ${formatCurrency(matchingDetail.dotGiamGia.giaTriGiam)}</span>
                </div>
                <div class="d-flex align-items-end">
                  <h5>Giá KM:</h5>
                  <span class="h5 font-weight-bold">${formatCurrency(matchingDetail.giaBan - matchingDetail.dotGiamGia.giaTriGiam)}</span>
                </div>
                <div class="d-flex align-items-end">
                  <span>Số lượng tồn: ${matchingDetail.soLuong}</span>
                </div>
              `;
                    }
                } else {
                    giaSanPhamHtml = `
              <div class="d-flex align-items-end">
                <h5 class="me-3">Giá:</h5>
                <span class="h5 font-weight-bold">${formatCurrency(matchingDetail.giaBan)}</span>
              </div>
              <div class="d-flex align-items-end">
                  <span>Số lượng tồn: ${matchingDetail.soLuong}</span>
                </div>
            `;
                }
                $("#modal-price").html(giaSanPhamHtml);
                return;
            }
        }
        $("#modal-price").text("N/A");
    }

    /**
     * Cập nhật tổng tiền (số lượng x giá) và hiển thị cảnh báo nếu vượt giới hạn.
     * @param {Object} prod - Đối tượng sản phẩm.
     */
    function updateTotalAmount(prod) {
        if (selectedColor && selectedSize) {
            var matchingDetail = prod.sanPhamChiTiets.find(function(ct) {
                return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
            });
            if (matchingDetail) {
                var quantity = parseInt($("#modal-quantity").val(), 10) || 0;
                var effectivePrice = matchingDetail.giaBan;
                if (matchingDetail.isPromotionProduct) {
                    if (matchingDetail.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
                        effectivePrice = matchingDetail.giaChietKhau;
                    } else {
                        effectivePrice = matchingDetail.giaBan - matchingDetail.dotGiamGia.giaTriGiam;
                    }
                }
                var totalAmount = quantity * effectivePrice;
                $("#modal-total").text("Tổng tiền: " + totalAmount.toLocaleString() + " VNĐ");

                if (totalAmount > 5000000) {
                    $("#modal-total-warning").text("Tổng tiền vượt quá 5 triệu VNĐ!").css("color", "red");
                } else {
                    $("#modal-total-warning").text("");
                }
                return;
            }
        }
        $("#modal-total").text("Tổng tiền: N/A");
        $("#modal-total-warning").text("");
    }

    // Bắt sự kiện thay đổi số lượng để cập nhật tổng tiền
    $(document).on("input", "#modal-quantity", function() {
        if (currentProduct) updateTotalAmount(currentProduct);
    });

    // Xử lý submit form add-to-cart
    $(document).on("submit", "#modal-form", function(e) {
        e.preventDefault();
        var quantity = parseInt($("#modal-quantity").val(), 10);
        if (isNaN(quantity) || quantity < 1) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Số lượng phải là số nguyên dương, ít nhất 1.'
            });
            return;
        }
        if (!selectedColor || !selectedSize) {
            Swal.fire({
                icon: 'error',
                title: 'Chưa chọn đủ!',
                text: 'Vui lòng chọn cả màu sắc và kích thước.'
            });
            return;
        }
        var matchingDetail = currentProduct.sanPhamChiTiets.find(function(ct) {
            return ct.mauSac.ten === selectedColor && ct.kichThuoc.ten === selectedSize;
        });
        if (!matchingDetail) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Không tìm thấy chi tiết sản phẩm phù hợp!'
            });
            return;
        }
        if (matchingDetail.soLuong < quantity) {
            Swal.fire({
                icon: 'error',
                title: 'Hết hàng!',
                text: 'Số lượng tồn kho không đủ.'
            });
            return;
        }
        var effectivePrice = matchingDetail.giaBan;
        if (matchingDetail.isPromotionProduct && matchingDetail.dotGiamGia) {
            if (matchingDetail.dotGiamGia.loaiChietKhau === 'PHAN_TRAM') {
                effectivePrice = matchingDetail.giaChietKhau;
            } else {
                effectivePrice = matchingDetail.giaBan - matchingDetail.dotGiamGia.giaTriGiam;
            }
        }
        var totalAmount = quantity * effectivePrice;
        var info = {
            idSanPhamChiTiet: parseInt(matchingDetail.id),
            soLuong: quantity,
            giaGoc: matchingDetail.giaBan,
            giaKhuyenMai: effectivePrice
        };

        fetch('/api/orders/' + orderId + '/details/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(info)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Lỗi thêm sản phẩm: ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                Swal.fire('Thành công', 'Thêm sản phẩm thành công!', 'success');
                // Cập nhật lại thông tin đơn hàng (ví dụ: gọi hàm fetchOrderDetails)
                fetchOrderDetails();
            })
            .catch(error => {
                console.error("Error adding product detail:", error);
                Swal.fire('Lỗi', error.message, 'error');
            });


    });


    // Cập nhật số lượng sản phẩm (validate > 0, gọi API và ghi log)
    document.getElementById('productListBody').addEventListener('change', function(e) {
        if (e.target.classList.contains('updateQtyInput')) {
            const newQty = parseInt(e.target.value, 10);
            if (isNaN(newQty) || newQty <= 0) {
                Swal.fire('Lỗi', 'Số lượng phải lớn hơn 0', 'error');
                return;
            }
            const detailId = e.target.getAttribute('data-detail-id');
            const details = currentOrder.hoaDonChiTiets || currentOrder.chiTietList || [];
            const currentDetail = details.find(d => d.id == detailId);
            if (!currentDetail) {
                console.error("Không tìm thấy chi tiết đơn hàng với id:", detailId);
                return;
            }
            const oldQty = currentDetail.soLuong;
            fetch('/api/orders/' + orderId + '/details/' + detailId + '/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ soLuong: newQty })
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Lỗi cập nhật số lượng: ' + response.statusText);
                    }
                    return response.json();
                })
                .then(() => {
                    Swal.fire('Thành công', 'Cập nhật số lượng thành công!', 'success');
                    fetchOrderDetails();
                })
                .catch(error => {
                    console.error("Error updating product quantity:", error);
                    Swal.fire('Lỗi', error.message, 'error');
                });
        }
    });


    // Xóa sản phẩm (soft delete)
    // Biến toàn cục để lưu detailId của sản phẩm vừa xóa và timer hoàn tác

// Hàm hiển thị thông báo hoàn tác với countdown
    function showUndoNotification() {
        const undoDiv = document.getElementById('undoNotification');
        const timerSpan = document.getElementById('undoTimer');
        let remaining = undoDuration;
        // Hiển thị thông báo (xóa class hidden)
        undoDiv.classList.remove('hidden');
        timerSpan.textContent = `(${remaining} giây)`;

        // Bắt đầu đồng hồ đếm ngược
        deletionTimer = setInterval(() => {
            remaining--;
            timerSpan.textContent = `(${remaining} giây)`;
            if (remaining <= 0) {
                clearInterval(deletionTimer);
                // Ẩn thông báo hoàn tác khi hết thời gian
                undoDiv.classList.add('hidden');
                // Xử lý logic hoàn tất nếu cần (ví dụ: xóa log hoàn tác)
                pendingDeletion = null;
            }
        }, 1000);
    }

// Sự kiện nút hoàn tác
    document.getElementById('undoBtn').addEventListener('click', function() {
        if (pendingDeletion) {
            clearInterval(deletionTimer);
            fetch('/api/orders/' + orderId + '/details/' + pendingDeletion + '/undo', {
                method: 'POST'
            })
                .then(response => {
                    if (!response.ok) throw new Error('Lỗi hoàn tác xóa sản phẩm');
                    return response.json();
                })
                .then(() => {
                    Swal.fire('Thành công', 'Hoàn tác xóa sản phẩm thành công!', 'success');
                    pendingDeletion = null;
                    fetchAndPopulateProductList(orderId)
                    // Ẩn thông báo hoàn tác
                    document.getElementById('undoNotification').classList.add('hidden');
                    // Cập nhật lại thông tin đơn hàng
                    fetchOrderDetails();

                })
                .catch(error => {
                    console.error("Error undoing deletion:", error);
                    Swal.fire('Lỗi', error.message, 'error');
                });
        }
    });

    // Hàm ghi log lịch sử qua API
    function addHistoryLog(tieuDe, moTa) {
        fetch('/api/orders/' + orderId + '/history', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ tieuDe: tieuDe, moTa: moTa })
        }).catch(err => console.error("Lỗi ghi log lịch sử:", err));
    }

    // Thêm sản phẩm: kiểm tra tồn kho trước khi gọi API thêm (sử dụng API /api/products/{id} để lấy tồn kho)
    function addProduct(productId,soLuongCanThem) {
        const qty = parseInt(soLuongCanThem, 10);
        if (isNaN(qty) || qty <= 0) {
            Swal.fire('Lỗi', 'Số lượng cần thêm phải lớn hơn 0', 'error');
            return;
        }
        // Gọi API lấy thông tin sản phẩm để biết tồn kho
        fetch('/api/products/' + productId)
            .then(response => {
                if (!response.ok) throw new Error('Lỗi tải thông tin sản phẩm');
                return response.json();
            })
            .then(product => {
                if (product.soLuongTon < qty) {
                    throw new Error("Không đủ tồn kho. Tồn kho hiện tại: " + product.soLuongTon);
                }
                const details = currentOrder.hoaDonChiTiets || currentOrder.chiTietList || [];
                let existingDetail = details.find(d => d.sanPhamChiTietId == productId);
                if (existingDetail) {
                    const newQty = existingDetail.soLuong + qty;
                    // Kiểm tra tồn kho khi cộng dồn
                    if (product.soLuongTon < qty) {
                        throw new Error("Không đủ tồn kho để tăng số lượng. Tồn kho hiện tại: " + product.soLuongTon);
                    }
                    fetch('/api/orders/' + orderId + '/details/' + existingDetail.id + '/update', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ soLuong: newQty })
                    })
                        .then(response => {
                            if (!response.ok) throw new Error('Lỗi cập nhật số lượng');
                            return response.json();
                        })
                        .then(() => {
                            Swal.fire('Thành công', 'Cập nhật số lượng thành công!', 'success');
                            $('#addCartModal').modal('hide');
                            fetchOrderDetails();
                        })
                        .catch(error => {
                            console.error(error);
                            Swal.fire('Lỗi', error.message, 'error');
                        });
                } else {
                    fetch('/api/orders/' + orderId + '/details/add', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ productId: productId, soLuong: qty })
                    })
                        .then(response => {
                            if (!response.ok) throw new Error('Lỗi thêm sản phẩm');
                            return response.json();
                        })
                        .then(result => {
                            Swal.fire('Thành công', 'Thêm sản phẩm thành công!', 'success');
                            $('#addCartModal').modal('hide');
                            fetchOrderDetails();
                        })
                        .catch(error => {
                            console.error(error);
                            Swal.fire('Lỗi', error.message, 'error');
                        });
                }
            })
            .catch(error => {
                console.error(error);
                Swal.fire('Lỗi', error.message, 'error');
            });
    }

    // Sự kiện khi nhấn nút thêm sản phẩm từ DataTable modal
    $('#productTable').on('click', '.addProductToOrderBtn', function() {
        const productId = $(this).data('product-id');
        addProduct(productId, 1);
    });



    // Cập nhật thông tin đơn hàng (người nhận, phí vận chuyển, ghi chú)
    document.getElementById('updateOrderInfoForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = {
            tenNguoiNhan: formData.get('tenNguoiNhan'),
            diaChi: formData.get('diaChi'),
            soDienThoai: formData.get('soDienThoai'),
            phiVanChuyen: formData.get('phiVanChuyen'),
            ghiChu: formData.get('ghiChu')
        };
        fetch('/api/orders/' + orderId + '/updateInfo', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (!res.ok) throw new Error('Lỗi cập nhật thông tin đơn hàng');
                return res.json();
            })
            .then(updatedOrder => {
                Swal.fire('Thành công', 'Cập nhật thông tin đơn hàng thành công!', 'success');
                $('#updateOrderModal').modal('hide');
                fetchOrderDetails();
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Lỗi', err.message, 'error');
            });
    });

    // Nút xem lịch sử đơn hàng
    document.getElementById('openOrderHistoryBtn').addEventListener('click', function() {
        fetchOrderHistory();
    });
    function fetchOrderHistory() {
        fetch('/api/orders/' + orderId + '/history')
            .then(res => {
                if (!res.ok) throw new Error('Lỗi lấy lịch sử đơn hàng');
                return res.json();
            })
            .then(historyList => {
                populateOrderHistory(historyList);
                $('#orderHistoryModal').modal('show');
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Lỗi', err.message, 'error');
            });
    }
    function populateOrderHistory(historyList) {
        const historyBody = document.getElementById('orderHistoryBody');
        historyBody.innerHTML = '';
        if (historyList && historyList.length > 0) {
            historyList.forEach((log, index) => {
                const row = document.createElement('tr');
                row.innerHTML = `
                            <td>${index + 1}</td>
                            <td>${log.tieuDe || ''}</td>
                            <td>${log.moTa || ''}</td>
                            <td>${new Date(log.thoiGian).toLocaleString()}</td>
                            <td>${log.tenTaiKhoan || ''}</td>
                        `;
                historyBody.appendChild(row);
            });
        } else {
            historyBody.innerHTML = `<tr><td colspan="5" class="text-center">Chưa có lịch sử</td></tr>`;
        }
    }


});
