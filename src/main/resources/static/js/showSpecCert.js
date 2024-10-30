function showSpecCert(button) {
    // Lấy empId từ thuộc tính data-emp-id của button
    var empId = $(button).attr('data-emp-id');
    console.log('Employee ID:', empId);

    // Gửi yêu cầu AJAX tới server để lấy thông tin chi tiết
    $.ajax({
        url: '/manager/getDoctorDetails',
        type: 'GET',
        data: { empId: empId },
        success: function (response) {
            console.log('Response:', response); // Kiểm tra dữ liệu nhận về

            if (response) {
                // Gán thông tin vào các phần tử trong modal
                $('#spec').text(response.spec || 'Chưa có');
                $('#cert').text(response.cert || 'Chưa có');

                const imgElement = document.querySelector('.user-icon');
                if (response.img) {
                    imgElement.src = response.img;
                    imgElement.alt = `Ảnh của ${response.doctorName || 'Bác sĩ'}`;
                } else {
                    imgElement.src = 'default-avatar.png';
                    imgElement.alt = 'Ảnh mặc định';
                }

                // Mở modal
                $('#infoModal').modal('show');
            } else {
                console.error('Dữ liệu không hợp lệ hoặc không tồn tại.');
                alert('Không tìm thấy thông tin bác sĩ.');
            }
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi gửi yêu cầu: ' + error);
            alert('Đã xảy ra lỗi khi lấy thông tin. Vui lòng thử lại.');
        }
    });
}