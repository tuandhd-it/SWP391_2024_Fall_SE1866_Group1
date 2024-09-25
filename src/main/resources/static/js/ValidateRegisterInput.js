document.addEventListener('DOMContentLoaded', function () {
    const phoneInput = document.getElementById('phone');
    const passwordInput = document.getElementById('password');
    const rePasswordInput = document.getElementById('re_password');

    const phonePattern = /^(0[1-9][0-9]{8})$/; // Số điện thoại Việt Nam
    let isValid = true;

    document.getElementById('validationForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Ngăn chặn gửi form

        // Kiểm tra số điện thoại
        if (!phonePattern.test(phoneInput.value)) {
            phoneInput.setCustomValidity('Please enter a valid Vietnamese phone number (0XXXXXXXXX).');
            isValid = false;
        } else {
            phoneInput.setCustomValidity(''); // Xóa thông báo lỗi
            isValid = true;
        }

        // Kiểm tra mật khẩu không để trống
        if (passwordInput.value.trim() === '') {
            passwordInput.setCustomValidity('Mật khẩu không được để trống.');
            isValid = false;
        } else {
            passwordInput.setCustomValidity(''); // Xóa thông báo lỗi
        }

        // Kiểm tra xác nhận mật khẩu không để trống
        if (rePasswordInput.value.trim() === '') {
            rePasswordInput.setCustomValidity('Xác nhận mật khẩu không được để trống.');
            isValid = false;
        } else {
            rePasswordInput.setCustomValidity(''); // Xóa thông báo lỗi
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp nhau không
        if (passwordInput.value !== rePasswordInput.value) {
            rePasswordInput.setCustomValidity('Mật khẩu và xác nhận mật khẩu không khớp.');
            isValid = false;
        } else {
            rePasswordInput.setCustomValidity(''); // Xóa thông báo lỗi
        }

        // Nếu tất cả đều hợp lệ, thực hiện hành động
        if (isValid) {
            this.submit(); // Gửi form nếu cần
        } else {
            // Nếu không hợp lệ, hiển thị thông báo lỗi
            phoneInput.reportValidity();
            passwordInput.reportValidity();
            rePasswordInput.reportValidity();
        }
    });

    // Xóa thông báo lỗi khi người dùng nhập lại
    phoneInput.addEventListener('input', function() {
        if (phonePattern.test(phoneInput.value)) {
            phoneInput.setCustomValidity(''); // Xóa thông báo lỗi nếu hợp lệ
            isValid = true;
        } else {
            phoneInput.setCustomValidity('Please enter a valid Vietnamese phone number (0XXXXXXXXX).');
        }
    });

    passwordInput.addEventListener('input', function() {
        passwordInput.setCustomValidity(''); // Xóa thông báo lỗi
    });

    rePasswordInput.addEventListener('input', function() {
        if (rePasswordInput.value !== passwordInput.value) {
            rePasswordInput.setCustomValidity('Mật khẩu và xác nhận mật khẩu không khớp.');
            isValid = false;
        } else {
            rePasswordInput.setCustomValidity(''); // Xóa thông báo lỗi
            isValid = true;
        }
    });
});