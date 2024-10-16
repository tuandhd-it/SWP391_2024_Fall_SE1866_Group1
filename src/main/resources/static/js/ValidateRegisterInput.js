document.addEventListener('DOMContentLoaded', function () {
    const phoneInput = document.getElementById('phone');
    const passwordInput = document.getElementById('password');
    const rePasswordInput = document.getElementById('re_password');
    const firstNameInput = document.getElementById('firstName');
    const lastNameInput = document.getElementById('lastName');

    // Chọn tất cả các input liên quan, bao gồm cả password
    const allInputs = document.querySelectorAll('input, textarea');

    const phonePattern = /^(0[1-9][0-9]{8})$/; // Số điện thoại Việt Nam
    let isValid = true;

    // Hàm trim tất cả các input
    function trimInputs() {
        allInputs.forEach(input => {
            input.value = input.value.trim();
        });
    }

    // Hàm kiểm tra tên không chứa số
    function validateName(name) {
        const namePattern = /^[^\d]*$/; // Không chứa số
        return namePattern.test(name);
    }

    document.getElementById('validationForm').addEventListener('submit', function (event) {
        event.preventDefault(); // Ngăn chặn gửi form

        // Trim tất cả các input trước khi kiểm tra
        trimInputs();

        // Kiểm tra họ và tên không chứa số
        if (!validateName(firstNameInput.value)) {
            firstNameInput.setCustomValidity('Họ không được chứa số.');
            isValid = false;
        } else {
            firstNameInput.setCustomValidity('');
        }

        if (!validateName(lastNameInput.value)) {
            lastNameInput.setCustomValidity('Tên không được chứa số.');
            isValid = false;
        } else {
            lastNameInput.setCustomValidity('');
        }

        // Kiểm tra số điện thoại
        if (!phonePattern.test(phoneInput.value)) {
            phoneInput.setCustomValidity('Please enter a valid Vietnamese phone number (0XXXXXXXXX).');
            isValid = false;
        } else {
            phoneInput.setCustomValidity('');
        }

        // Kiểm tra mật khẩu không để trống
        if (passwordInput.value === '') {
            passwordInput.setCustomValidity('Mật khẩu không được để trống.');
            isValid = false;
        } else {
            passwordInput.setCustomValidity('');
        }

        // Kiểm tra xác nhận mật khẩu không để trống
        if (rePasswordInput.value === '') {
            rePasswordInput.setCustomValidity('Xác nhận mật khẩu không được để trống.');
            isValid = false;
        } else {
            rePasswordInput.setCustomValidity('');
        }

        // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp nhau không
        if (passwordInput.value !== rePasswordInput.value) {
            rePasswordInput.setCustomValidity('Mật khẩu và xác nhận mật khẩu không khớp.');
            isValid = false;
        } else {
            rePasswordInput.setCustomValidity('');
        }

        // Nếu tất cả đều hợp lệ, thực hiện hành động
        if (isValid) {
            this.submit(); // Gửi form nếu hợp lệ
        } else {
            // Nếu không hợp lệ, hiển thị thông báo lỗi
            firstNameInput.reportValidity();
            lastNameInput.reportValidity();
            phoneInput.reportValidity();
            passwordInput.reportValidity();
            rePasswordInput.reportValidity();
        }
    });

    // Xóa thông báo lỗi khi người dùng nhập lại
    firstNameInput.addEventListener('input', function () {
        if (validateName(firstNameInput.value)) {
            firstNameInput.setCustomValidity('');
        } else {
            firstNameInput.setCustomValidity('Họ không được chứa số.');
        }
    });

    lastNameInput.addEventListener('input', function () {
        if (validateName(lastNameInput.value)) {
            lastNameInput.setCustomValidity('');
        } else {
            lastNameInput.setCustomValidity('Tên không được chứa số.');
        }
    });

    phoneInput.addEventListener('input', function () {
        if (phonePattern.test(phoneInput.value)) {
            phoneInput.setCustomValidity('');
        } else {
            phoneInput.setCustomValidity('Please enter a valid Vietnamese phone number (0XXXXXXXXX).');
        }
    });

    passwordInput.addEventListener('input', function () {
        passwordInput.setCustomValidity('');
    });

    rePasswordInput.addEventListener('input', function () {
        if (rePasswordInput.value !== passwordInput.value) {
            rePasswordInput.setCustomValidity('Mật khẩu và xác nhận mật khẩu không khớp.');
        } else {
            rePasswordInput.setCustomValidity('');
        }
    });
});
