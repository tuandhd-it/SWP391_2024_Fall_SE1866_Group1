package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

//Custom user details to check authority
public class CustomEmployeeDetails implements UserDetails {

    private Employee employee;
    private Account account;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomEmployeeDetails() {
    }

    public CustomEmployeeDetails(Employee employee, Account account, Collection<? extends GrantedAuthority> authorities) {
        super();
        this.employee = employee;
        this.account = account;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
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
        if(account.getIsActivated().equalsIgnoreCase("Activated")) {
            enabled = true;
        } else {
            enabled = false;
        }
        return enabled;
    }
}
