package project.dental_clinic_management.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

//Custom user details to check authority
public class CustomEmployeeDetails implements UserDetails {

    @Getter
    private Employee employee;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomEmployeeDetails() {
    }

    public CustomEmployeeDetails(Employee employee, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.employee = employee;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        boolean enabled;
        enabled = employee.is_active();
        return enabled;
    }
}
