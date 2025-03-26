document.getElementById("ClearForm").addEventListener("click", function () {
    document.getElementById("name").value = "";
    document.getElementById("status").value = "";
    document.getElementById("startTime").value = "";
    document.getElementById("endTime").value = "";
});
document.addEventListener("DOMContentLoaded", function () {
    let rows = document.querySelectorAll("tr"); // Lấy tất cả các dòng trong bảng

    rows.forEach(function (row) {
        let statusElement = row.querySelector("#DGGstatus"); // Tìm trạng thái trong từng dòng
        let toggleBtn = row.querySelector("#toggleBTN"); // Tìm nút toggle trong cùng dòng

        if (statusElement && toggleBtn) {
            let statusText = statusElement.textContent.trim();

            if (statusText === "DA_KET_THUC") {
                statusElement.style.color = "black";
                statusElement.style.fontWeight = "bold";
                statusElement.innerText = "Đã kết thúc";

                // Cập nhật nút
                toggleBtn.innerHTML = "<i class=\"fa-solid fa-toggle-off\"></i>";
                toggleBtn.classList.remove("btn-success");
                toggleBtn.classList.add("btn-secondary");


            } else if (statusText === "DANG_DIEN_RA") {
                statusElement.style.color = "red";
                statusElement.style.fontWeight = "bold";
                statusElement.innerText = "Đang Diễn Ra";

                // Cập nhật nút
                toggleBtn.innerHTML = "<i class=\"fa-solid fa-toggle-on\"></i>";
                toggleBtn.classList.remove("btn-secondary");
                toggleBtn.classList.add("btn-success");


            } else { // Trạng thái sắp diễn ra
                statusElement.style.color = "orange";
                statusElement.style.fontWeight = "bold";
                statusElement.innerText = "Sắp Diễn Ra";

                // Cập nhật nút
                toggleBtn.innerHTML = "<i class=\"fa-solid fa-toggle-on\"></i>";
                toggleBtn.classList.remove("btn-secondary");
                toggleBtn.classList.add("btn-success");

            }
        }
    });
});
