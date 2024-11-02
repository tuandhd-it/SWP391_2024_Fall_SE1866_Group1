const toggler = document.querySelector(".toggler-btn");
const mainContent = document.querySelector('.main');
toggler.addEventListener("click", function () {
    document.querySelector("#sidebar").classList.toggle("collapsed");
    mainContent.classList.toggle('no-margin');
});


function toggleEditMode() {
    // Ẩn hiện các phần tử theo chế độ chỉnh sửa hoặc xem
    const editButton = document.getElementById("edit-button");
    const saveButton = document.getElementById("save-button");
    const cancelButton = document.getElementById("cancel-button");


    // Ẩn/Hiện input
    const inputs = document.querySelectorAll('input, select, textarea');
    inputs.forEach(input => {
        input.style.display = (input.style.display === 'none' || input.style.display === '') ? 'inline-block' : 'none';
    });

    // Ẩn/Hiện span có class là label-span
    const spans = document.querySelectorAll('span.label-span');
    spans.forEach(span => {
        span.style.display = (span.style.display === 'none' || span.style.display === '') ? 'inline-block' : 'none';
    });

    // Ẩn/Hiện nút
    if (editButton.style.display === 'none') {
        editButton.style.display = 'inline-block';
        saveButton.style.display = 'none';
        cancelButton.style.display = 'none';
    } else {
        editButton.style.display = 'none';
        saveButton.style.display = 'inline-block';
        cancelButton.style.display = 'inline-block';
    }
}

