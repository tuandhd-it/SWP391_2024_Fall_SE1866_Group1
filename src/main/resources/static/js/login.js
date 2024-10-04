const passwordInput = document.getElementById('password-input');
const passwordIcon = document.getElementById('password-icon');

passwordInput.addEventListener('input', function () {
    if (this.value) {
        passwordIcon.classList.add('hidden'); // Ẩn biểu tượng khi có dữ liệu
    } else {
        passwordIcon.classList.remove('hidden'); // Hiện lại biểu tượng khi không có dữ liệu
    }
});