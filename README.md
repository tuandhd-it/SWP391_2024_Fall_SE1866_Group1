//Create data for Role table
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('1', 'Admin');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('2', 'Manager');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('3', 'Doctor');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('4', 'Nurse');
INSERT INTO `dental_clinic_management`.`role` (`role_id`, `role_name`) VALUES ('5', 'Receptionist');

---------------------------------------------------------------------------------------------------------
//Create data for Branch table
INSERT INTO `dental_clinic_management`.`branch` (`bran_id`, `branch_name`, `branch_address`, `branch_description`, `branch_phone`) VALUES ('1', 'Hai Phong\'s Dental', 'Hai Phong', 'The dental clinic top 1 Hai Phong', '0123456789');

----------------------------------------------------------------------------------------------------------
//Create data for Account table (create admin account)
INSERT INTO `dental_clinic_management`.`account` (`acc_id`, `is_activated`, `password`, `username`) VALUES ('1', 'Yes', '$2a$10$Y9N0oLAIELmoe7wEgbbPu.Bp4lOZfg01MEOsUQmtCseVpTStQyO/2', 'admin');

-----------------------------------------------------------------------------------------------------------
//Create data for Employee table (Create admin infor)
INSERT INTO `dental_clinic_management`.`employee` (`emp_id`, `address`, `email`, `first_name`, `last_name`, `phone`, `salary`, `status`, `account_acc_id`, `bran_id`, `role_id`) VALUES ('1', 'Van Khe - Me Linh - Ha Noi', 'admin@gmail.com', 'Đỗ', 'Tuấn', '0337851355', '4000', 'On', '1', '1', '1');
