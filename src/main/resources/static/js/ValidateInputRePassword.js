document.addEventListener('DOMContentLoaded', function () {
    const passwordInput = document.getElementById('password');
    const rePasswordInput = document.getElementById('re_password');

    const phonePattern = /^(0[1-9][0-9]{8})$/; // Số điện thoại Việt Nam
    let isValid = true;

    document.getElementById('validationForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Ngăn chặn gửi form

        // Kiểm tra mật khẩu không để trống và độ dài từ 8 đến 45 ký tự
        if (passwordInput.value.trim() === '') {
            passwordInput.setCustomValidity('Mật khẩu không được để trống.');
            isValid = false;
        } else if (passwordInput.value.length < 8 || passwordInput.value.length > 45) {
            passwordInput.setCustomValidity('Mật khẩu phải từ 8 đến 45 ký tự.');
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
            passwordInput.reportValidity();
            rePasswordInput.reportValidity();
        }
    });

    passwordInput.addEventListener('input', function() {
        // Kiểm tra độ dài mật khẩu khi người dùng nhập lại
        if (passwordInput.value.length >= 8 && passwordInput.value.length <= 45) {
            passwordInput.setCustomValidity(''); // Xóa thông báo lỗi
        } else {
            passwordInput.setCustomValidity('Mật khẩu phải từ 8 đến 45 ký tự.');
        }
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
