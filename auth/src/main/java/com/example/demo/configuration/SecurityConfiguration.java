package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	

	private static final String[] AUTH_WHITELIST = {

            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Value("${spring.queries.users-query}")
    private String usersQuery;
    
    @Value("${spring.queries.admin-query}")
    private String adminQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
       http.csrf().disable().authorizeRequests()
        .antMatchers(HttpMethod.POST, "/customer/register").permitAll()
        .antMatchers(HttpMethod.POST, "/admin/register/{storeNumber}").permitAll()
        .antMatchers(HttpMethod.POST, "/decrypt/{storeNumber}").permitAll()
        .antMatchers(HttpMethod.POST, "/customer/login").permitAll()
        .antMatchers(HttpMethod.POST, "/admin/login").permitAll()
        .antMatchers(HttpMethod.POST, "/token").permitAll()
        .antMatchers(HttpMethod.POST, "/decrypted").permitAll()
        .antMatchers(AUTH_WHITELIST).permitAll()
        .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/swagger.json")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }
    
    
    
    

}