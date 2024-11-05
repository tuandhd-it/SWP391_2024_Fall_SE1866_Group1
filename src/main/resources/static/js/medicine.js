// Thêm thuốc tạm thời vào danh sách và hiển thị trong bảng
function addTemporaryMedicine() {
    const regNumber = document.getElementById("regNumber").value;
    const medicineName = document.getElementById("medicineName").value;
    const medicineQuantity = document.getElementById("medicineQuantity").value;
    const medicinePrice = document.getElementById("medicinePrice").value;

    // Kiểm tra nếu dữ liệu đầu vào hợp lệ
    if (regNumber && medicineName && medicineQuantity && medicinePrice) {
        // Gọi API để thêm thuốc tạm thời vào backend
        fetch('/api/manager/addTemporaryMedicine', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                medicineName,
                quantity: medicineQuantity,
                price: medicinePrice
            })
        })
            .then(response => response.json())
            .then(updateTemporaryMedicinesTable)
            .catch(error => console.error('Error:', error));
    } else {
        alert("Vui lòng điền đầy đủ thông tin thuốc.");
    }
}

// Cập nhật bảng hiển thị danh sách thuốc tạm thời
function updateTemporaryMedicinesTable(medicines) {
    const tableBody = document.getElementById("temporaryMedicinesTable").querySelector("tbody");
    tableBody.innerHTML = "";

    medicines.forEach((medicine, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${medicine.regNumber}</td>
            <td>${medicine.medicineName}</td>
            <td>${medicine.quantity}</td>
            <td>${medicine.price}</td>
            <td><button onclick="removeTemporaryMedicine(${index})">Xóa</button></td>
        `;

        tableBody.appendChild(row);
    });
}

// Xóa một thuốc từ danh sách tạm thời
function removeTemporaryMedicine(index) {
    fetch(`/api/manager/removeTemporaryMedicine/${index}`, { method: 'DELETE' })
        .then(response => response.json())
        .then(updateTemporaryMedicinesTable)
        .catch(error => console.error('Error:', error));
}

// Lưu đơn thuốc (gọi API để lưu vào cơ sở dữ liệu và xóa danh sách tạm thời)
function savePrescription() {
    fetch('/api/manager/savePrescription', { method: 'POST' })
        .then(response => {
            if (response.ok) {
                alert("Đơn thuốc đã được lưu.");
                updateTemporaryMedicinesTable([]); // Xóa bảng sau khi lưu thành công
            } else {
                alert("Đã xảy ra lỗi khi lưu đơn thuốc.");
            }
        })
        .catch(error => console.error('Error:', error));
}
