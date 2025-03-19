document.getElementById("ClearForm").addEventListener("click", function () {
    document.getElementById("name").value = "";
    document.getElementById("status").value = "";
    document.getElementById("startTime").value = "";
    document.getElementById("endTime").value = "";
});
document.addEventListener("DOMContentLoaded", function () {
    let statusElements = document.querySelectorAll("#DGGstatus"); // Lấy tất cả các phần tử có id là DGGstatus
    statusElements.forEach(function (statusElement) {
        let statusText = statusElement.textContent.trim(); // Lấy nội dung văn bản và loại bỏ khoảng trắng thừa
        if (statusText === "DA_KET_THUC") {
            statusElement.style.color = "black";
            statusElement.style.fontWeight = "bold";
        } else if (statusText === "DANG_DIEN_RA") {
            statusElement.style.color = "red";
            statusElement.style.fontWeight = "bold";
        } else {
            statusElement.style.color = "orange";
            statusElement.style.fontWeight = "bold";
        }
    });
});