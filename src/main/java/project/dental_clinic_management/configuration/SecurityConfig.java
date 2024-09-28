package project.dental_clinic_management.configuration;


import project.dental_clinic_management.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //Custom endpoints that can access without log in
    private final String[] PUBLIC_ENDPOINTS = {"/login", "/register", "/css/**", "/img/**", "/js/**", "/homePage", "/nextRegister", "/nextRegisterDoctor", "/registerDoctor", "/verifyEmail/**", "/forgotPassword/**"};

    @Autowired
    private CustomUserDetailService customUserDetailService;


    //Authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/admin/**").hasAuthority("Admin")
                        .requestMatchers("/manager/**").hasAuthority("Manager")
                        .requestMatchers("/doctor/**").hasAnyAuthority("Doctor", "Nurse")
                        .requestMatchers("/receptionist/**").hasAuthority("Receptionist")
                        .anyRequest().authenticated()
                ).formLogin(login -> login
                        //Custom login page and retreat data from form login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/homePage", true)
                ).logout(logout -> logout
                        .logoutUrl("/logout") // URL cho logout
                        .logoutSuccessUrl("/login?logout") // URL chuyển hướng sau khi logout
                        .permitAll() // Cho phép tất cả người dùng đăng xuất
                );

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}