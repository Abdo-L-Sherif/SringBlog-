package org.studyeasy.sptingBlog.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.studyeasy.sptingBlog.util.constants.Privilages;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig{
    private static final String[] WHITELIST = {
        "/",
        "/login",
        "/register",
        "/db-console/**",
        "/css/**",
        "/fonts/**",
        "/images/**",
        "/js/**",
        "/post/**"
    }; 

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } 

    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITELIST)
            .permitAll()
            .requestMatchers("/profile/**")
            .authenticated()
            .requestMatchers("/admin/**")
            .hasRole("ADMIN")
            .requestMatchers("/editor/**")
            .hasAnyRole("ADMIN","EDITOR")
            .requestMatchers("/admin")
            .hasAuthority(Privilages.ACCESS_ADMIN_PANEL.getPrivilageString()))
            .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/")
            .failureUrl("/login?error")
            .permitAll()
        )
        .logout(logout -> logout 
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
        )
        .httpBasic(Customizer.withDefaults());
        
        http.csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }


}