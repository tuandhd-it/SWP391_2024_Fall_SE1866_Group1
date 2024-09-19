package Project.SWP391_2024_Fall_SE1866_Group1.Service;

import Project.SWP391_2024_Fall_SE1866_Group1.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
}
