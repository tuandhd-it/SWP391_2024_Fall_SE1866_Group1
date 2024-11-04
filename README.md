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
INSERT INTO dental_clinic_management.medicine (reg_number, medicine_name, unit, quantity, price, ingredients) VALUES (4, 'Paracetamol', 'hộp', '1000', 5000, 'Acetaminophen'), (2, 'Thuoc te', 'hộp', '1000', 1000, 'abc'), (3, 'Thuoc ho', 'chai', '0', 5000, 'xyz');


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

