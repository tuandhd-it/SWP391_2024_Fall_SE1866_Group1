document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const firstName = document.getElementById("firstName");
    const lastName = document.getElementById("lastName");
    const email = document.getElementById("email");
    const phone = document.getElementById("phone");
    const reason = document.getElementById("reason");
    const note = document.getElementById("note");
    const dob = document.getElementById("dob");
    const examRegisterDate = document.getElementById("date");

    // Trim spaces and validate empty names
    function trimFields() {
        firstName.value = firstName.value.trim();
        lastName.value = lastName.value.trim();
        email.value = email.value.trim();
        reason.value = reason.value.trim();
        note.value = note.value.trim();
    }

    // Check if a field is not empty after trimming
    function isFieldEmpty(value) {
        return value === "";
    }

    // Validate phone number format for Vietnam (+84 or starting with 0)
    function validatePhoneNumber(phoneNumber) {
        const regex = /^(0|\+84)[3|5|7|8|9][0-9]{8}$/;
        return regex.test(phoneNumber);
    }

    // Check if a date is not in the future
    function validateDateNotInFuture(dateField) {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const selectedDate = new Date(dateField.value);
        selectedDate.setHours(0, 0, 0, 0);

        return selectedDate <= today;
    }

    // Validate that names do not contain numbers
    function validateName(name) {
        const regex = /^[^\d]*$/; // Chuỗi không chứa chữ số
        return regex.test(name);
    }

    // Add event listener to form submission
    form.addEventListener("submit", function (e) {
        // Trim fields
        trimFields();

        // Reset custom validity messages
        firstName.setCustomValidity("");
        lastName.setCustomValidity("");
        phone.setCustomValidity("");
        dob.setCustomValidity("");
        examRegisterDate.setCustomValidity("");

        // Check for empty first name or only spaces
        if (isFieldEmpty(firstName.value)) {
            firstName.setCustomValidity("Họ không được để trống hoặc chỉ chứa dấu cách.");
            firstName.reportValidity();
            e.preventDefault();
            return;
        }

        // Check for empty last name or only spaces
        if (isFieldEmpty(lastName.value)) {
            lastName.setCustomValidity("Tên không được để trống hoặc chỉ chứa dấu cách.");
            lastName.reportValidity();
            e.preventDefault();
            return;
        }

        // Validate first name and last name
        if (!validateName(firstName.value)) {
            firstName.setCustomValidity("Họ không được chứa số. Vui lòng nhập lại.");
            firstName.reportValidity();
            e.preventDefault();
            return;
        }

        if (!validateName(lastName.value)) {
            lastName.setCustomValidity("Tên không được chứa số. Vui lòng nhập lại.");
            lastName.reportValidity();
            e.preventDefault();
            return;
        }

        // Validate phone number
        if (!validatePhoneNumber(phone.value)) {
            phone.setCustomValidity("Số điện thoại không đúng định dạng Việt Nam. Vui lòng nhập số bắt đầu bằng +84 hoặc 0 và có 9 chữ số sau.");
            phone.reportValidity();
            e.preventDefault();
            return;
        }

        // Validate DOB not in the future
        if (!validateDateNotInFuture(dob)) {
            dob.setCustomValidity("Ngày sinh không được vượt quá ngày hôm nay. Vui lòng kiểm tra lại.");
            dob.reportValidity();
            e.preventDefault();
            return;
        }

        // Validate exam registration date not in the future
        if (validateDateNotInFuture(examRegisterDate)) {
            examRegisterDate.setCustomValidity("Ngày đăng ký khám không được trước ngày hôm nay. Vui lòng chọn ngày hợp lệ.");
            examRegisterDate.reportValidity();
            e.preventDefault();
            return;
        }
    });

    // Add event listener for name input fields to clear error when retyping
    firstName.addEventListener("input", function () {
        if (isFieldEmpty(firstName.value) || !validateName(firstName.value)) {
            firstName.setCustomValidity("Họ không hợp lệ. Vui lòng nhập lại.");
        } else {
            firstName.setCustomValidity("");
        }
    });

    lastName.addEventListener("input", function () {
        if (isFieldEmpty(lastName.value) || !validateName(lastName.value)) {
            lastName.setCustomValidity("Tên không hợp lệ. Vui lòng nhập lại.");
        } else {
            lastName.setCustomValidity("");
        }
    });

    // Add event listener for phone input field to clear error when retyping
    phone.addEventListener("input", function () {
        if (validatePhoneNumber(phone.value)) {
            phone.setCustomValidity("");
        } else {
            phone.setCustomValidity("Số điện thoại không đúng định dạng Việt Nam. Vui lòng nhập số bắt đầu bằng +84 hoặc 0 và có 9 chữ số sau.");
        }
    });

    // Add event listener for DOB input field to clear error when retyping
    dob.addEventListener("input", function () {
        if (validateDateNotInFuture(dob)) {
            dob.setCustomValidity("");
        } else {
            dob.setCustomValidity("Ngày sinh không được vượt quá ngày hôm nay. Vui lòng kiểm tra lại.");
        }
    });

    // Add event listener for examRegisterDate input field to clear error when retyping
    examRegisterDate.addEventListener("input", function () {
        if (!validateDateNotInFuture(examRegisterDate)) {
            examRegisterDate.setCustomValidity("");
        } else {
            examRegisterDate.setCustomValidity("Ngày đăng ký khám không được trước ngày hôm nay. Vui lòng chọn ngày hợp lệ.");
        }
    });
});
