package Project.SWP391_2024_Fall_SE1866_Group1.Configuration;


import Project.SWP391_2024_Fall_SE1866_Group1.Service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //Custom endpoints that can access without log in
    private final String[] PUBLIC_ENDPOINTS = {"/login", "/register", "/css/**", "/img/**", "/test"};

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
        );

        return http.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
