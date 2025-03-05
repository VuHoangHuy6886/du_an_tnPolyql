document.getElementById("ClearForm").addEventListener("click", function(event) {
    event.preventDefault(); // Ngăn form submit lại trang
    document.getElementById("name").value = "";
    document.getElementById("startTime").value = "";
    document.getElementById("endTime").value = "";
    document.getElementById("status").value = "";
});