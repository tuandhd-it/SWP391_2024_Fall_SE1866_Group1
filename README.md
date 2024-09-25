//Create data for Role table
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('1', 'Admin');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('2', 'Manager');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('3', 'Doctor');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('4', 'Nurse');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('5', 'Receptionist');

---------------------------------------------------------------------------------------------------------
//Create data for Branch table
INSERT INTO `dental_clinic_management`.`branch` (`bran_id`, `branch_name`, `branch_address`, `branch_description`, `branch_img`, `branch_phone`) VALUES ('1', 'Hai Phong\'s Dental', 'Hai Phong', 'The dental clinic top 1 Hai Phong', '/img/banner.png', '0123456789');

-----------------------------------------------------------------------------------------------------------
//Create data for Employee table (Create admin infor) (password equals "123456")
INSERT INTO `dental_clinic_management`.`employee` 
(`address`, `dob`, `email`, `first_name`, `gender`, `is_accept`, `is_active`, `last_name`, `password`, `phone`, `salary`, `status`, `bran_id`, `role_id`) 
VALUES 
('Ha Noi', '2004-12-09', 'tuan6a1thcstv@gmail.com', 'Đỗ', 'Nam', 1, 1, 'Tuấn', '$2a$10$ou0A97XvoK6mzsi32vSwi.MurUhVug6VS7qBsmwlNAFTnEqQFaCO6', '0123456789', 4000, 'Check-in', 1, 1);