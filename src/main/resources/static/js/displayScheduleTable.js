// Hàm lấy dữ liệu từ API và render vào bảng
function fetchAndRenderSchedule(startDate) {
    const monday = getMonday(startDate); // Ngày đầu tuần (thứ Hai)
    const sunday = new Date(monday);
    sunday.setDate(monday.getDate() + 6); // Ngày cuối tuần (Chủ nhật)

    // Gửi yêu cầu API với khoảng thời gian đã chọn
    $.ajax({
        url: `/manager/scheduleData`,
        type: 'GET',
        data: {
            startDate: monday.toISOString().split('T')[0], // YYYY-MM-DD
            endDate: sunday.toISOString().split('T')[0]
        },
        success: function (scheduleData) {
            console.log('Dữ liệu từ API:', scheduleData); // Kiểm tra dữ liệu từ API
            clearTable(); // Xóa dữ liệu cũ
            renderSchedule(scheduleData); // Hiển thị dữ liệu mới
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi lấy dữ liệu:', error); // Xử lý lỗi
        }
    });
}


// Hàm để xóa nội dung cũ trong bảng (tránh bị lặp dữ liệu)
function clearTable() {
    const cells = document.querySelectorAll('td[id*="-morning"], td[id*="-afternoon"]');
    cells.forEach(cell => cell.innerHTML = '');  // Xóa nội dung của các ô
}

// Hàm để điền dữ liệu vào bảng
function renderSchedule(data) {
    const userRole = data.length > 0 ? data[0].currentUserRole : null;

    data.forEach(item => {
        const itemDate = new Date(item.date); // Ngày của lịch làm việc
        const dateStr = item.date; // Chuỗi ngày từ dữ liệu (YYYY-MM-DD)
        const shift = item.shift ? "afternoon" : "morning"; // Xác định ca làm việc
        console.log(item);

        // Tạo ID động dựa trên ngày và ca làm việc
        const cellId = `${dateStr}-${shift}`;

        console.log(`Đang điền dữ liệu vào ô: ${cellId} cho ngày ${item.date}`); // Debug thông tin

        const cell = document.getElementById(cellId);
        if (cell) {
            // Tạo nút hiển thị thông tin nhân viên
            const button = document.createElement('button');
            button.className = 'btn btn-secondary btn-small w-100';
            button.textContent = `${item.empployeeRole}: ${item.employeeName}`; // Hiển thị tên nhân viên
            button.setAttribute('data-emp-id', item.employeeId);

            // Thêm sự kiện click vào nút để gọi hàm showEmployeeInfo
            button.addEventListener('click', function () {
                showEmployeeInfo(this); // Truyền chính button được click vào hàm
            });

            // Gắn nút vào ô
            cell.appendChild(button);

        } else {
            console.warn(`Không tìm thấy ô với id: ${cellId}`); // Cảnh báo nếu không tìm thấy ô
        }
    });
}

function parseLocalDate(dateStr) {
    const [year, month, day] = dateStr.split('-').map(Number);
    return new Date(year, month - 1, day); // Không có múi giờ lệch
}

// Khởi tạo ngày hiện tại
let currentDate = new Date();

// Hàm định dạng ngày theo định dạng 'dd/mm/yyyy'
function formatDate(date) {
    let day = date.getDate().toString().padStart(2, '0');
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

// Hàm lấy ngày đầu tuần (Thứ Hai)
function getMonday(date) {
    const d = new Date(date);
    const day = d.getDay(); // Lấy thứ của ngày: 0 (Chủ nhật), 1 (Thứ Hai), ...
    const diff = (day === 0 ? -6 : 1) - day; // Nếu là Chủ nhật, lùi về thứ Hai
    d.setDate(d.getDate() + diff); // Điều chỉnh về đúng thứ Hai
    d.setHours(0, 0, 0, 0); // Đặt giờ về đầu ngày để tránh lỗi múi giờ
    return d;
}


// Tạo danh sách các tuần trong năm và thêm vào <select>
function populateWeeks() {
    const weekSelect = document.getElementById('week-select');
    let startDate = new Date(currentDate.getFullYear(), 0, 1); // Ngày đầu năm

    while (startDate.getFullYear() === currentDate.getFullYear()) {
        let monday = getMonday(new Date(startDate));
        let sunday = new Date(monday);
        sunday.setDate(monday.getDate() + 6);

        let option = document.createElement('option');
        // Lưu ngày đầu tuần
        option.value = `${monday.getFullYear()}-${(monday.getMonth() + 1).toString().padStart(2, '0')}-${monday.getDate().toString().padStart(2, '0')}`;
        option.textContent = `${formatDate(monday)} - ${formatDate(sunday)}`;
        weekSelect.appendChild(option);

        startDate.setDate(startDate.getDate() + 7);
    }
}

// Cập nhật bảng với ngày của tuần đã chọn
function updateSchedule(startDate) {
    let currentDay = getMonday(new Date(startDate)); // Ngày đầu tuần (Thứ Hai)

    // Duyệt qua 7 ngày trong tuần (Thứ Hai đến Chủ Nhật)
    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];

    for (let i = 0; i < 7; i++) {
        // Lấy ngày dưới dạng YYYY-MM-DD mà không bị lệch múi giờ
        const dateStr = `${currentDay.getFullYear()}-${(currentDay.getMonth() + 1).toString().padStart(2, '0')}-${currentDay.getDate().toString().padStart(2, '0')}`;

        // Cập nhật ngày vào tiêu đề cột (span)
        const dayHeader = document.getElementById(`${days[i]}-date`);
        if (dayHeader) {
            dayHeader.innerText = formatDate(currentDay); // Cập nhật ngày cụ thể
        }

        // Lấy các ô ca sáng và chiều theo thứ tự
        const morningCell = document.querySelector(`#row-morning td:nth-child(${i + 2})`);
        const afternoonCell = document.querySelector(`#row-afternoon td:nth-child(${i + 2})`);

        // Thêm ID động cho các ô nếu chúng tồn tại
        if (morningCell) {
            morningCell.id = `${dateStr}-morning`;
        }
        if (afternoonCell) {
            afternoonCell.id = `${dateStr}-afternoon`;
        }

        // Chuyển sang ngày tiếp theo
        currentDay.setDate(currentDay.getDate() + 1);
    }

    // Gọi API để lấy dữ liệu và hiển thị lịch
    fetchAndRenderSchedule(startDate);
}


// Khi người dùng bấm nút "Previous Week"
document.getElementById('prev-week').addEventListener('click', () => {
    currentDate.setDate(currentDate.getDate() - 7); // Lùi lại 7 ngày
    updateSchedule(currentDate);
});

// Khi người dùng bấm nút "Next Week"
document.getElementById('next-week').addEventListener('click', () => {
    currentDate.setDate(currentDate.getDate() + 7); // Tiến tới 7 ngày
    updateSchedule(currentDate);
});
// Khi người dùng bấm nút "Tìm kiếm"
document.getElementById('search-week').addEventListener('click', () => {
    const weekSelect = document.getElementById('week-select');
    const selectedDateStr = weekSelect.value; // Chuỗi YYYY-MM-DD

    if (selectedDateStr !== "0") {
        const mondayDate = parseLocalDate(selectedDateStr); // Sử dụng parseLocalDate
        currentDate = mondayDate; // Cập nhật currentDate với ngày mới
        updateSchedule(mondayDate); // Cập nhật lịch
    } else {
        updateSchedule(currentDate); // Hiển thị tuần hiện tại
    }
});


// Khởi tạo danh sách tuần khi trang được tải
populateWeeks();

// Hiển thị tuần hiện tại mặc định khi trang được tải
updateSchedule(currentDate);

function formatDateToDDMMYYYY(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');   // Đảm bảo ngày có 2 chữ số
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0 nên cần +1
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;  // Định dạng dd/mm/yyyy
}

function showEmployeeInfo(button) {
    // Lấy examId từ thuộc tính data-exam-id của button
    var empId = $(button).attr('data-emp-id');
    console.log(empId);

    // Gửi yêu cầu AJAX tới server để lấy thông tin chi tiết
    $.ajax({
        url: '/manager/getDetails',
        type: 'GET',
        data: {empId: empId},
        success: function (response) {
            console.log(response);  // Kiểm tra xem response có giá trị không và có cấu trúc mong đợi không

            // Sau khi xác định rằng response không bị undefined, mới xử lý hiển thị
            if (response) {
                $('#fullName').text(response.doctorName);
                $('#email').text(response.email);
                $('#phone').text(response.phone);
                $('#dob').text(formatDateToDDMMYYYY(response.dob));
                $('#gender').text(response.gender);
                $('#role').text(response.roleName);
                const imgElement = document.querySelector('.user-icon');
                if (response.img) {
                    imgElement.src = response.img; // Gán URL ảnh
                    imgElement.alt = `Ảnh của ${response.fullname}`; // Gán thẻ alt
                } else {
                    imgElement.src = 'default-avatar.png'; // Ảnh mặc định nếu không có ảnh
                    imgElement.alt = 'Ảnh mặc định';
                }
                // Mở modal
                $('#infoModal').modal('show');
            } else {
                console.error("Dữ liệu response không hợp lệ");
                alert("Không tìm thấy thông tin đăng ký.");
            }
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi lấy dữ liệu: ' + error);
            alert('Không tìm thấy thông tin đăng ký.');
        }
    });
}

// Hàm tạo ra danh sách tất cả ngày trong năm đã cho
function generateDatesForYear(year) {
    const dates = [];
    for (let month = 0; month < 12; month++) { // Lặp qua các tháng
        const daysInMonth = new Date(year, month + 1, 0).getDate(); // Số ngày trong tháng
        for (let day = 1; day <= daysInMonth; day++) { // Lặp qua các ngày
            const date = new Date(year, month, day);
            dates.push(date);
        }
    }
    return dates;
}

$('#addScheduleModal').one('show.bs.modal', function () {
    const selectedDate = $('#flashScheduleDate').val();
    const selectedEmployee = $('#flashEmployee').val();

    // Gọi hàm load với giá trị đã chọn (nếu có)
    loadDates(selectedDate);
    loadEmployeeList(selectedEmployee);
});

function loadDates(selectedDate) {
    const dateSelect = $('#scheduleDate');
    dateSelect.empty(); // Xóa các option cũ

    const currentYear = new Date().getFullYear();
    const dates = generateDatesForYear(currentYear);

    let hasSelected = false; // Để kiểm tra xem có ngày nào đã được chọn không

    dates.forEach(date => {
        const formattedDate = formatDate(date);
        const valueDate = `${date.getFullYear()}-${(date.getMonth() + 1)
            .toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;

        const isSelected = valueDate === selectedDate ? 'selected' : '';
        if (isSelected !== '') hasSelected = true;

        dateSelect.append(`<option value="${valueDate}" ${isSelected}>${formattedDate}</option>`);
    });

    // Nếu không có ngày nào được chọn, thêm và chọn option "Chọn ngày"
    if (!hasSelected) {
        dateSelect.prepend('<option value="" selected>Chọn ngày</option>');
    }
}


function loadEmployeeList(selectedEmployee) {
    $.ajax({
        url: '/manager/employees',
        method: 'GET',
        success: function (employees) {
            const employeeSelect = $('#employee');
            employeeSelect.empty();
            let hasSelected = false;

            employees.forEach(employee => {
                const isSelected = employee.employeeId.toString() === selectedEmployee ? 'selected' : '';
                if (isSelected !== '') hasSelected = true;
                employeeSelect.append(`<option value="${employee.employeeId}" ${isSelected}>${employee.employeeFullName}</option>`);
            });
            if (!hasSelected) {
                employeeSelect.prepend('<option value="" selected>Chọn nhân viên</option>');
            }
        },
        error: function (err) {
            console.error('Lỗi khi lấy danh sách nhân viên:', err);
        }
    });
}



