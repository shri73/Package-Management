package com.eureka.auth.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.eureka.common.security.JwtConfig;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
	"/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs", "/webjars/**" };

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.manager-query}")
	private String adminQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtConfig jwtConfig;

	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
				.authorizeRequests().antMatchers(HttpMethod.POST, "/customer/register").permitAll()
				.antMatchers(HttpMethod.POST, "/admin/register/{storeNumber}").permitAll()
				.antMatchers(HttpMethod.POST, "/decrypt/{storeNumber}").permitAll()
				.antMatchers(HttpMethod.POST, "/customer/login").permitAll()
				.antMatchers(HttpMethod.POST, "/admin/login").permitAll().antMatchers(HttpMethod.POST, "/token")
				.permitAll().antMatchers(HttpMethod.POST, "/decrypted").permitAll()
				.antMatchers(HttpMethod.POST, "/admin/logout").permitAll()
				.antMatchers(HttpMethod.POST, "/admin/deviceToken").permitAll()
				.antMatchers(HttpMethod.POST, "/customer/deviceToken").permitAll()
				.antMatchers(HttpMethod.POST, "/customer/logout").permitAll().antMatchers(AUTH_WHITELIST).permitAll()
				.anyRequest().authenticated();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs/**").antMatchers("/swagger.json").antMatchers("/swagger-ui.html")
				.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
	
	@Bean
	public JwtConfig jwtConfig() {
        	return new JwtConfig();
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	

}