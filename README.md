//Create data for Role table
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('1', 'Admin');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('2', 'Manager');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('3', 'Doctor');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('4', 'Nurse');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('5', 'Receptionist');

---------------------------------------------------------------------------------------------------------
//Create data for Branch table
INSERT INTO `dental_clinic_management`.`branch` (`branch_name`, `branch_address`, `branch_description`, `active`, `branch_img`, `branch_phone`) VALUES ('Hai Phong\'s Dental', 'Hai Phong', 'The dental clinic top 1 Hai Phong', 1,'/img/banner.png', '0123456789');

-----------------------------------------------------------------------------------------------------------
//Create data for Employee table (Create admin infor) (password equals "123456")
INSERT INTO `dental_clinic_management`.`employee`
(`address`, `dob`, `email`, `first_name`, `gender`,`is_active`, `last_name`, `password`, `phone`, `salary`, `bran_id`, `role_id`, `img`, `description`)
VALUES
('Ha Noi', '2004-12-09', 'tuan6a1thcstv@gmail.com', 'Đỗ', 'Nam', 1, 'Tuấn', '$2a$10$ou0A97XvoK6mzsi32vSwi.MurUhVug6VS7qBsmwlNAFTnEqQFaCO6', '0123456789', 4000,  1, 1, '/imgAmin', 'FirstAdmin');

//Create Manager Account
INSERT INTO `dental_clinic_management`.`employee` (`address`, `description`, `dob`, `email`, `first_name`, `gender`, `is_active`, `last_name`, `password`, `phone`, `salary`, `bran_id`, `branch_managed_bran_id`, `role_id`) VALUES ('Hà Nội', 'Manager', '2004-12-08', 'khoa@gmail.com', 'Lê', 'Nam', 1, 'Khoa', '$2b$12$EIHLAyrnP6CBjR9cjGaZW.6.NKdkZrKjEIVdgHShTPMGfT48Q3Z8m', '0912345651', '5000', 1, 1, 2);


INSERT INTO waiting_room (waiting_roomid, is_available, capacity, branch_id)
VALUES (1, true, 10, 1);

//Create data for Medicine table
INSERT INTO dental_clinic_management.medicine ( medicine_name, unit, quantity, price, ingredients) VALUES
( 'Paracetamol', 'hộp', 1000, 5000, 'Acetaminophen'),
('Ibuprofen', 'vỉ', 500, 7000, 'Ibuprofen'),
( 'Amoxicillin', 'chai', 300, 10000, 'Amoxicillin'),
( 'Metronidazole', 'viên', 1500, 8000, 'Metronidazole'),
('Cephalexin', 'hộp', 200, 12000, 'Cephalexin'),
( 'Aspirin', 'chai', 600, 6000, 'Aspirin'),
( 'Clarithromycin', 'vỉ', 400, 15000, 'Clarithromycin'),
( 'Azithromycin', 'viên', 1000, 14000, 'Azithromycin'),
( 'Doxycycline', 'hộp', 350, 9000, 'Doxycycline'),
( 'Ciprofloxacin', 'chai', 250, 13000, 'Ciprofloxacin');


//Create data for Equipment table
INSERT INTO dental_clinic_management.equipment (equipment_id, equipment_name, unit, quantity, price, note) VALUES 
(1, 'Ghế nha khoa', 'cái', 10, 15000000, 'Ghế điều trị nha khoa'),
(2, 'Máy X-quang', 'cái', 5, 50000000, 'Máy chụp X-quang nha khoa'),
(3, 'Máy siêu âm', 'cái', 3, 30000000, 'Máy siêu âm cầm tay'),
(4, 'Đèn trám răng', 'cái', 20, 700000, 'Đèn dùng trong trám răng'),
(5, 'Tủ tiệt trùng', 'cái', 4, 10000000, 'Tủ khử trùng thiết bị'),
(6, 'Máy nén khí', 'cái', 6, 12000000, 'Máy nén khí nha khoa'),
(7, 'Khay dụng cụ', 'cái', 50, 100000, 'Khay đựng dụng cụ nha khoa'),
(8, 'Bộ lấy cao răng', 'bộ', 30, 500000, 'Bộ dụng cụ lấy cao răng'),
(9, 'Bàn khám', 'cái', 8, 2000000, 'Bàn dùng trong phòng khám'),
(10, 'Gương khám răng', 'cái', 100, 20000, 'Gương soi miệng nha khoa'),
(11, 'Khẩu trang y tế', 'hộp', 200, 50000, 'Khẩu trang phòng dịch'),
(12, 'Găng tay y tế', 'hộp', 150, 70000, 'Găng tay sử dụng 1 lần'),
(13, 'Kìm nhổ răng', 'cái', 40, 150000, 'Kìm nhổ răng nha khoa'),
(14, 'Bộ dụng cụ trám', 'bộ', 25, 400000, 'Bộ dụng cụ trám răng'),
(15, 'Đèn chiếu sáng', 'cái', 12, 1500000, 'Đèn chiếu sáng phòng khám');


//Create data for Service
INSERT INTO `dental_clinic_management`.`service` (`service_id`, `is_active`, `price`, `service_name`, `detail`, `img`, `quantity`, `unit`, `guarantee`, `material`) VALUES
(1, 0, 150000000, 'Tẩy trắng răng tại phòng khám bằng hệ thống chiếu đèn a', 'Dịch vụ tẩy trắng răng giúp loại bỏ vết ố vàng do thực phẩm, thuốc lá, và lão hóa, mang lại hàm răng sáng bóng, đều màu. Quá trình thực hiện nhanh chóng, an toàn và hiệu quả, giúp cải thiện thẩm mỹ nụ cười và tăng sự tự tin.', 'b16f36c8-828f-41b6-bfd6-76ef48d9e222.jpg', NULL, NULL, '2 tháng', 'Sử dụng gel peroxide an toàn, giúp loại bỏ vết ố và làm răng sáng bóng tự nhiên.'),
(2, 1, 2500000, 'Tẩy trắng răng tại phòng khám bằng hệ thống chiếu đèn', 'Dịch vụ tẩy trắng răng tại phòng khám sử dụng hệ thống chiếu đèn công nghệ cao, giúp tăng cường hiệu quả tẩy trắng nhanh chóng. Quy trình an toàn, nhẹ nhàng, mang lại hàm răng sáng bóng và tự nhiên, đồng thời không gây hại cho men răng.', 'f5826538-454f-417d-9220-756df34e3a52.jpg', NULL, NULL, '6 tháng', 'Sử dụng gel tẩy trắng an toàn, kết hợp với hệ thống chiếu đèn LED hiện đại, giúp tăng cường hiệu quả và bảo vệ men răng trong suốt quá trình điều trị.'),
(3, 0, 25000000, 'Cấy ghép Implant', 'là phương pháp thay thế răng đã mất bằng trụ titanium cấy vào xương hàm, giúp khôi phục chức năng và thẩm mỹ như răng thật.', '090c2b4c-abd4-4564-a07e-744a2774c4ea.jpg', NULL, NULL, '10 năm', 'Trụ Implant làm từ titanium cao cấp, an toàn và bền chắc. Mão răng sứ tự nhiên và thẩm mỹ.'),
(4, 0, 5000000, 'Inlay/Onlay toàn sứ', 'Inlay/Onlay toàn sứ là giải pháp phục hồi răng miệng hiệu quả cho các răng bị sâu hoặc tổn thương. Quy trình này sử dụng các miếng inlay hoặc onlay làm từ sứ cao cấp, đảm bảo tính thẩm mỹ và độ bền cao.', 'b11f3c7f-33eb-40da-9684-1a373943b397.jpg', NULL, NULL, '3 năm', 'Inlay/Onlay toàn sứ được làm từ sứ nguyên khối, có độ bền và khả năng chịu lực tốt, đồng thời màu sắc tự nhiên giúp hài hòa với răng thật.'),
(5, 1, 2500000, 'Chụp răng kim loại', 'Chụp răng kim loại là một phương pháp phục hồi răng giúp bảo vệ và phục hồi hình dạng của răng bị hư hại hoặc sâu. Quy trình này sử dụng chụp răng làm từ hợp kim, mang lại sự chắc chắn và độ bền cao cho răng.', 'fc8f365c-d773-4813-8b57-d5066161b717.png', NULL, NULL, '5 năm', 'Chụp răng kim loại thường được làm từ hợp kim quý như vàng hoặc hợp kim niken-chrom, giúp đảm bảo độ bền và khả năng chịu lực tốt.'),
(6, 1, 50000000, 'Niềng răng trong suốt Invisalign', 'Niềng răng trong suốt Invisalign là một phương pháp chỉnh nha hiện đại, sử dụng khay niềng trong suốt và tháo lắp dễ dàng để điều chỉnh vị trí răng. Phương pháp này giúp cải thiện nụ cười một cách tinh tế mà không gây khó chịu.', 'c2923d36-d4ac-40ce-88ce-b67a446f7a23.jpg', NULL, NULL, 'trong suốt quá trình điều trị', 'Khay niềng Invisalign được làm từ nhựa trong suốt, an toàn cho sức khỏe, không gây kích ứng và gần như vô hình khi đeo, mang lại sự tự tin cho người sử dụng.'),
(7, 1, 30000000, 'Chỉnh nha mắc cài kim loại', 'là phương pháp chỉnh nha truyền thống, sử dụng mắc cài kim loại gắn trên bề mặt răng và dây cung để điều chỉnh vị trí răng. Phương pháp này rất hiệu quả trong việc điều chỉnh các vấn đề về răng như lệch lạc, chen chúc, và khớp cắn.', 'c1457691-ba77-43ab-add4-9abe08c771bf.jpg', NULL, NULL, 'trong suốt quá trình điều trị', 'Mắc cài kim loại được làm từ hợp kim chắc chắn, bền bỉ, giúp đảm bảo hiệu quả trong suốt quá trình điều trị.'),
(8, 0, 35000000, 'Chỉnh nha mắc cài sứ', 'là phương pháp chỉnh nha hiện đại, sử dụng mắc cài được làm từ sứ cao cấp, giúp cải thiện tình trạng răng lệch lạc mà không gây ảnh hưởng đến thẩm mỹ nụ cười. Mắc cài sứ có màu sắc gần giống với màu răng thật.', '5bb49944-7844-42b1-a6c1-dc958d054b60.jpg', NULL, NULL, 'trong suốt quá trình điều trị', 'Mắc cài sứ được làm từ vật liệu sứ bền và chịu lực tốt, giúp hạn chế tình trạng dễ vỡ, đồng thời có tính thẩm mỹ cao hơn so với mắc cài kim loại.'),
(9, 0, 6000000, 'Dán sứ Veneers', 'Dán sứ Veneers là một giải pháp thẩm mỹ hiệu quả giúp cải thiện hình dáng, màu sắc và kích thước của răng. Quy trình này bao gồm việc gắn mặt dán sứ lên bề mặt răng, mang lại nụ cười tự nhiên, rạng rỡ.', '94bf0279-440e-48bb-89c0-300f6aed39d7.jpg', NULL, NULL, '2 năm', 'Dịch vụ dán sứ Veneers sử dụng mặt dán sứ cao cấp, mỏng nhẹ, được chế tác chính xác để tạo hình dáng và màu sắc tự nhiên, giúp cải thiện vẻ đẹp của răng.'),
(10, 1, 1500000, 'Điều trị tủy răng cửa', 'Điều trị tủy răng cửa là quá trình loại bỏ tủy răng bị viêm nhiễm hoặc tổn thương, sau đó làm sạch, khử trùng và trám bít ống tủy để bảo vệ răng. Phương pháp này giúp bảo tồn răng thật, ngăn ngừa biến chứng và giảm đau nhức.', '1907c857-79c1-448a-b35d-03f8529c23d5.jpg', NULL, NULL, '1 năm', 'Sử dụng các vật liệu trám bít ống tủy chuyên dụng và an toàn, giúp bảo vệ răng sau khi điều trị, đồng thời duy trì chức năng và thẩm mỹ.');


//Create data for time tracking
INSERT INTO `time_tracking` (`time_tracking_id`, `check_in`, `check_out`, `emp_id`, `note`)
VALUES
(40, '2024-10-15 09:00:00.000000', NULL, 3, NULL),
(41, '2024-10-01 08:00:00.000000', '2024-10-01 20:00:00.000000', 2, NULL),
(42, '2024-10-02 08:00:00.000000', '2024-10-02 20:00:00.000000', 2, NULL),
(43, '2024-10-03 08:00:00.000000', '2024-10-03 20:00:00.000000', 2, NULL),
(44, '2024-10-04 08:00:00.000000', '2024-10-04 20:00:00.000000', 2, NULL),
(45, '2024-10-05 08:00:00.000000', '2024-10-05 20:00:00.000000', 2, NULL),
(46, '2024-10-06 08:00:00.000000', '2024-10-06 20:00:00.000000', 2, NULL),
(47, '2024-10-07 08:00:00.000000', '2024-10-07 20:00:00.000000', 2, NULL),
(48, '2024-10-08 08:00:00.000000', '2024-10-08 20:00:00.000000', 2, NULL),
(49, '2024-10-09 08:00:00.000000', '2024-10-09 20:00:00.000000', 2, NULL),
(50, '2024-10-10 08:00:00.000000', '2024-10-10 20:00:00.000000', 2, NULL),
(51, '2024-10-11 08:00:00.000000', '2024-10-11 20:00:00.000000', 2, NULL),
(52, '2024-10-12 08:00:00.000000', '2024-10-12 20:00:00.000000', 2, NULL),
(53, '2024-10-13 08:00:00.000000', '2024-10-13 20:00:00.000000', 2, NULL),
(54, '2024-10-14 08:00:00.000000', '2024-10-14 20:00:00.000000', 2, NULL),
(55, '2024-10-15 08:00:00.000000', '2024-10-15 20:00:00.000000', 2, NULL),
(60, '2024-10-16 03:00:30.539575', '2024-10-16 03:01:01.923330', 3, NULL),
(72, '2024-11-03 17:18:24.146733', '2024-11-03 19:01:08.531385', 4, 'Check in: huhu\nCheck out: null'),
(77, '2024-11-04 08:29:03.900843', '2024-11-04 11:40:04.032312', 6, 'Check in: null\nCheck out: không');



INSERT INTO `employee` (`address`, `description`, `dob`, `email`, `first_name`, `gender`, `img`, `is_active`, `last_name`, `password`, `phone`, `salary`, `bran_id`, `branch_managed_emp_id`, `role_id`, `branch_managed_bran_id`, `certification`, `specification`)
VALUES
('Hà Nội', 'Nurse', NULL, 'nurse@gmail.com', 'Y', NULL, '', b'1', 'Tá', '$2a$10$6RUK7w18LMwXFPYKY0RPPuXeE.iJhZz5L2qdd22gJOuD7FB5LsmuC', '0987654321', 4500, 3, NULL, 4, NULL, NULL, NULL),
('Ha Nội', 'Bác sĩ tại bệnh viện Xanhpon. 6 năm kinh nghiệm tại các phòng khám lớn.', '1989-07-12', 'doctor@gmail.com', 'Lưu', 'Nữ', 'd1c21f5a-9209-459e-a21a-fbcc8406f943_luu-hong-hanh-300x300.png', b'1', 'Hồng Hạnh', '$2a$10$SGzFYESbkB1z4FAfksrFtO9R8jjLULM20KYFUqG6tfi1thJ2OgQPK', '0984763836', 123, 2, NULL, 3, NULL, 'Tốt nghiệp Đại học Y', 'Nắn chỉnh răng'),
('hà nội', 'receptionist', '2003-09-09', 'receptionist@gmail.com', 'Lễ', 'Nam', '308e38d1-ed95-48d8-aaa5-d2362c0dc16c_dong-phuc-le-tan-nha-khoa.jpeg', b'1', 'Tân', '$2b$12$EIHLAyrnP6CBjR9cjGaZW.6.NKdkZrKjEIVdgHShTPMGfT48Q3Z8m', '0836880711', 1, 2, NULL, 5, NULL, NULL, NULL),
('hà nội', 'Bác sĩ chuyên khoa Răng Hàm Mặt, 4 năm kinh nghiệm tại các phòng khám lớn', NULL, 'doctor1@gmail.com', 'Phạm', 'Nam', 'a1acca01-772c-43de-a6d7-47db68ba178f_pham-dinh-duc-2.png', b'1', 'Đình Đức', '$2a$10$gozMI9OS6kMLukBxf/EixeBREdA73tQ8CBvy4lwtAfwTT4GRYkRQi', '0987654123', 3, 2, NULL, 3, NULL, 'Tốt nghiệp Đại học Y', 'Nha khoa thẩm mỹ'),
('test manager', 'manager', '2023-12-12', 'manage@gmail.com', 'Quản', NULL, 'manage', b'1', 'Lý', '$2a$10$ZUnBf0zqtg9sjeKC/fG4juaNEjQpcGipE2r1J2UMRuCORTxUZN6iy', '987654356', 1234543, 2, NULL, 2, 2, NULL, NULL),
('hà nội', '4 năm kinh nghiệm tại các phòng khám lớn', '2024-10-30', 'doctor2@gmail.com', 'Lã', 'Nữ', '71d05b64-1e5d-4c91-8be9-3e6198d49b01_la-trinh-dieu-lan-300x300.png', b'1', 'Trịnh Diệu Lan', '$2a$10$gozMI9OS6kMLukBxf/EixeBREdA73tQ8CBvy4lwtAfwTT4GRYkRQi', '0987654123', 111111111, 2, NULL, 3, NULL, 'Tốt nghiệp nha khoa tại Trung Quốc', 'Nha khoa tổng quát');

INSERT INTO `dental_clinic_management`.`patient`
(`address`, `dob`, `email`, `first_name`, `gender`, `last_name`, `medical_history`, `phone`)
VALUES
('918A Đường Láng', '2024-10-29', 'nguyentuankhanh1310@gmail.com', 'Nguyễn', 'Nam', 'Khanh', 'Bệnh nhân có tiền sử hút thuốc lá, không đánh răng thường xuyên', '0357284999'),
('25 Hai Bà Trưng', '1990-05-14', 'tranthithao90@gmail.com', 'Trần', 'Nữ', 'Thảo', 'Bệnh nhân có tiền sử viêm lợi, thường xuyên uống nước ngọt', '0987654321'),
('56B Nguyễn Huệ', '1985-08-20', 'lehoanganh1985@gmail.com', 'Lê', 'Nam', 'Hoàng Anh', 'Tiền sử cao huyết áp, ăn uống nhiều đồ ngọt', '0901234567'),
('123 Cầu Giấy', '2000-12-25', 'phamkimthanh2000@gmail.com', 'Phạm', 'Nữ', 'Kim Thanh', 'Bệnh nhân chăm chỉ đánh răng nhưng thường xuyên ăn đồ ngọt', '0912345678'),
('345 Lý Thường Kiệt', '1978-07-15', 'dangquanghung78@gmail.com', 'Đặng', 'Nam', 'Quang Hưng', 'Tiền sử viêm nha chu, không súc miệng nước muối', '0923456789'),
('678A Phan Đình Phùng', '1995-03-30', 'truongminhhoa95@gmail.com', 'Trương', 'Nữ', 'Minh Hòa', 'Bệnh nhân bị sâu răng thường xuyên, ăn nhiều đồ ngọt', '0934567890'),
('89 Đinh Tiên Hoàng', '1988-09-10', 'nguyenhongan88@gmail.com', 'Nguyễn', 'Nam', 'Hồng An', 'Bệnh nhân không đánh răng trước khi ngủ, có tiền sử hút thuốc', '0945678901'),
('101 Bà Triệu', '2002-04-22', 'phanthilinh02@gmail.com', 'Phan', 'Nữ', 'Thị Linh', 'Thích uống nước có ga, ít uống nước lọc', '0956789012'),
('223 Nguyễn Trãi', '1993-01-05', 'vudinhcuong93@gmail.com', 'Vũ', 'Nam', 'Đình Cường', 'Bệnh nhân có tiền sử bệnh tiểu đường, thường xuyên ăn bánh kẹo', '0967890123'),
('12B Trần Hưng Đạo', '1997-11-11', 'hoangthuytrang97@gmail.com', 'Hoàng', 'Nữ', 'Thùy Trang', 'Chăm chỉ đánh răng nhưng có tiền sử sâu răng', '0978901234');


INSERT INTO `record` (`diagnose`, `note`, `reason`, `record_date`, `bran_id`, `emp_id`, `patient_id`)
VALUES
('Sâu răng mãn tính', 'Uống thuốc đầy đủ, Không uống khi đói, Ăn ít bánh kẹo', 'Đến Khám vì đau răng', '2024-11-06', '1', '3', '1'),
('Viêm lợi', 'Súc miệng nước muối mỗi ngày, Tránh đồ ăn cay nóng', 'Đến khám vì lợi sưng đau', '2024-11-07', '1', '3', '1'),
('Chỉnh nha', 'Đeo niềng răng, Tuân thủ lịch tái khám hàng tháng', 'Khám chỉnh nha', '2024-11-08', '1', '3', '1'),
('Sâu răng hàm dưới', 'Tránh ăn uống đồ ngọt, Đánh răng thường xuyên', 'Đau nhức răng hàm', '2024-11-09', '1', '3', '1'),
('Viêm tủy răng', 'Tránh ăn uống đồ quá nóng hoặc lạnh, Uống thuốc theo chỉ định', 'Đau buốt khi ăn uống', '2024-11-10', '1', '3', '1'),
('Nhổ răng khôn', 'Không ăn uống trong vòng 30 phút sau khi nhổ, Tái khám sau 1 tuần', 'Đến khám để nhổ răng khôn', '2024-11-11', '1', '3', '1');


INSERT INTO `dental_clinic_management`.`record_medicine`
(`note`, `price`, `quantity`, `recordid`, `medicine_id`)
VALUES
-- Record 1
('Uống 2 viên/ lần, chia làm 2 lần trong ngày', '1000000', '3', '1', '1'),
('Uống 1 viên sau khi ăn', '150000', '2', '1', '2'),
('Dùng trước khi đi ngủ', '200000', '1', '1', '3'),
('Sáng và tối mỗi lần 1 viên', '120000', '2', '1', '4'),

-- Record 2
('Uống sau khi ăn sáng', '80000', '5', '2', '5'),
('Uống trước bữa trưa', '250000', '2', '2', '6'),
('Uống vào buổi tối', '50000', '3', '2', '7'),

-- Record 3
('Uống cùng bữa sáng', '90000', '2', '3', '8'),
('Uống 1 viên trước khi đi ngủ', '300000', '1', '3', '9'),
('Uống 2 viên sáng, 2 viên tối', '400000', '4', '3', '10'),

-- Record 4
('Uống 1 viên sáng, 1 viên tối', '200000', '3', '4', '1'),
('Uống sau ăn trưa', '100000', '2', '4', '2'),
('Dùng mỗi tối sau bữa ăn', '150000', '3', '4', '3'),

-- Record 5
('Dùng trước bữa sáng', '120000', '2', '5', '4'),
('Uống 3 viên vào buổi tối', '200000', '3', '5', '5'),
('Uống 1 viên mỗi sáng', '180000', '1', '5', '6'),
('Dùng mỗi tối', '300000', '2', '5', '7');

INSERT INTO `dental_clinic_management`.`record_service`
(`note`, `recordid`, `service_id`)
VALUES
-- Record 1
('Kiểm tra hàm trên', '1', '1'),
('Vệ sinh răng miệng', '1', '2'),
('Chụp X-quang', '1', '3'),
('Tư vấn niềng răng', '1', '4'),

-- Record 2
('Kiểm tra hàm dưới', '2', '5'),
('Làm sạch cao răng', '2', '6'),
('Lấy mẫu xét nghiệm', '2', '7'),
('Tẩy trắng răng', '2', '8'),

-- Record 3
('Kiểm tra sâu răng', '3', '9'),
('Trám răng', '3', '1'),
('Đánh bóng răng', '3', '2'),
('Phục hồi răng sứ', '3', '3'),

-- Record 4
('Kiểm tra nướu răng', '4', '4'),
('Chỉnh nha', '4', '5'),
('Nhổ răng khôn', '4', '6'),
('Lấy tủy răng', '4', '7'),

-- Record 5
('Khám răng định kỳ', '5', '8'),
('Lấy dấu hàm', '5', '9'),
('Kiểm tra răng giả', '5', '10'),
('Vệ sinh răng miệng', '5', '1');

