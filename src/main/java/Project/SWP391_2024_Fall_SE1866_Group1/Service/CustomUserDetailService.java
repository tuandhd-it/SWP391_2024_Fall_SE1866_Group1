package Project.SWP391_2024_Fall_SE1866_Group1.Service;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.CustomEmployeeDetails;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private ReceptionistService employeeService;

    //Custom login retreat information from database and to assign authority
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Not found Employee with username: " + username);
        }
        Collection<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(employee.getRole().getRole_name()));

        return new CustomEmployeeDetails(employee, authorities);
    }
}
