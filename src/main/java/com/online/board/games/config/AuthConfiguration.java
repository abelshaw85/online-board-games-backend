package com.online.board.games.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.online.board.games.filter.JwtRequestFilter;
import com.online.board.games.service.UserService;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserService userService;
	
	@Value("${origins.url}")
	private String originsUrl;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		// SessionCreationPolicy must be set to STATELESS as we are using JWT to manually manage authorisation
        http.csrf()
        	.disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .permitAll()
            .antMatchers("/auth/**")
            .permitAll()
            .antMatchers("/ws/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors();
        // Add our custom filter for checking JWT token in the header
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
	
	// AuthenticationManager no longer has a default implementation, so needs to be overridden
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth.authenticationProvider(authenticationProvider()); // authenticationProvider() is our bean defined below.
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		// Service to get the User's details, in this case UserService. 
		// This will retrieve the user with the same name as the user attempting to log in, and check their password against the one provided.
		auth.setUserDetailsService(userService); 
		// Our encoder is the BCrypt encoder defined below. Passwords sent will be encoded, and checked against the encoded password stored.
		auth.setPasswordEncoder(passwordEncoder()); 
		return auth;
	}
	
	/* Defines a bean that can be used to encode a plaintext password to BCrypt. Used in the Auth Provider above to encode incoming passwords
	   and check them against the encoded password stored for a user */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(originsUrl));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "Access-Control-Allow-Headers",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers",
                        "Origin", "Cache-Control",
                        "Content-Type",
                        "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
