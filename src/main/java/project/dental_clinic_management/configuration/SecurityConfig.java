package project.dental_clinic_management.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import project.dental_clinic_management.service.CustomUserDetailService;

@Configuration
public class SecurityConfig {

    //Custom endpoints that can access without log in
    private final String[] PUBLIC_ENDPOINTS = {"/login","/loginFail" ,"/register", "/css/**", "/img/**", "/js/**", "/homePage", "/nextRegister",
            "/nextRegisterDoctor", "/registerDoctor", "/verifyEmail/**", "/forgotPassword/**", "/guestExamRegistration", "/guestExamRegistration", "/chooseDoctor"};
    public SecurityConfig(PasswordEncoder passwordEncoder, CustomUserDetailService customUserDetailService) {
        this.customUserDetailService=customUserDetailService;
        this.passwordEncoder = passwordEncoder;
    }
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder);
    }
    //Authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/admin/manageRegisterAccount").hasAnyAuthority("Admin", "Manager")
                        .requestMatchers("/admin/acceptAccount").hasAnyAuthority("Admin", "Manager")
                        .requestMatchers("/admin/manageService").hasAnyAuthority("Receptionist", "Admin")
                        .requestMatchers("/admin/detail/**").hasAnyAuthority("Receptionist", "Admin")
                        .requestMatchers("/recep/viewRegistration").hasAnyAuthority("Receptionist", "Doctor", "Manager", "Nurse")
                        .requestMatchers("/recep/myScheduleList").hasAnyAuthority("Receptionist", "Doctor", "Nurse")
                        .requestMatchers("/manager/viewRegistration").hasAnyAuthority("Receptionist", "Doctor", "Manager", "Nurse")
                        .requestMatchers("/manager/getExamDetails").hasAnyAuthority("Receptionist", "Manager")
                        .requestMatchers("/recep/getDetails").hasAnyAuthority("Receptionist", "Doctor", "Nurse")
                        .requestMatchers("/recep/search").hasAnyAuthority("Receptionist", "Doctor", "Nurse")
                        .requestMatchers("/manager/scheduleList").hasAnyAuthority("Doctor", "Nurse", "Manager", "Receptionist")
                        .requestMatchers("/manager/getDetails").hasAnyAuthority("Doctor", "Nurse", "Manager", "Receptionist")
                        .requestMatchers("/manager/scheduleData").hasAnyAuthority("Doctor", "Nurse", "Manager", "Receptionist")
                        .requestMatchers("/doctor/**").hasAnyAuthority("Doctor", "Nurse")
                        .requestMatchers("/tracking/**").hasAnyAuthority("Doctor", "Nurse", "Receptionist", "Admin")
                        .requestMatchers("/admin/**").hasAuthority("Admin")
                        .requestMatchers("/manager/**").hasAuthority("Manager")
                        .requestMatchers("/recep/**").hasAuthority("Receptionist")
                        .requestMatchers("/profile/**").permitAll()
                        .requestMatchers("changePass").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(login -> login
                        //Custom login page and retreat data from form login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .failureHandler(new CustomAuthenticationFailureHandler())
                        .defaultSuccessUrl("/homePage", true)
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout") // URL cho logout
                        .logoutSuccessUrl("/login?logout") // URL chuyển hướng sau khi logout
                        .permitAll() // Cho phép tất cả người dùng đăng xuất
                );

        return http.build();
    }


}
