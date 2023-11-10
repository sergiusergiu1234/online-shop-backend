package com.StefanSergiu.Licenta.config;

import com.StefanSergiu.Licenta.filter.JwtAuthFilter;
import com.StefanSergiu.Licenta.service.UserInfoUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthFilter authFilter;

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;
    //authentication
    @Bean
    public UserDetailsService userDetailsService(){

        return new UserInfoUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/users/signup","/users/signin","/users/signout",
                        "/brands/all","/types/all",
                        "/categories/all","/brands/**","/products/**","/genders/**","/types/**","/attributes/**","/productAttributes/**","/size/getSizes/**","products/sizes/**","/productSizes/**")
                .permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/users/**","/users/me/**","/favorites/**",
                        "/brands/admin/add/**","/brands/admin/delete/**",
                        "/types/admin/**","/type/admin/delete/**",
                        "/categories/admin/**","/products/admin/**","/genders/admin/**","/types/admin/**","/attributes/admin/**","/productAttributes/admin/**","/favorites/**",
                        "/favorites/delete/**","/shoppingCart/**","/order/**","/order/create/**","/order/update/**","/size/admin/add/**",
                        "/me/edit/password","/size/admin/delete/**")
                .authenticated()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

