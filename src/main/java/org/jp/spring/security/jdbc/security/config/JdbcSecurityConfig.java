package org.jp.spring.security.jdbc.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Order(0)
@Configuration
public class JdbcSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jdbcUserDetailsManager()).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.antMatcher("/jdbc-auth/**").formLogin().and().httpBasic().and().authorizeRequests()
				.antMatchers("/jdbc-auth/user").hasRole("user").antMatchers("/jdbc-auth/health").permitAll()
				.antMatchers("/jdbc-auth/**").hasRole("admin");
		http.csrf().disable();
	}

	@Bean
	JdbcUserDetailsManager jdbcUserDetailsManager() {
		return new JdbcUserDetailsManager(dataSource);
	}

	/*
	 * @Bean JdbcUserDetailsManager jdbcUserDetailsManager() { final
	 * JdbcUserDetailsManager jdbcUserDetailsManager = new
	 * JdbcUserDetailsManager(dataSource); jdbcUserDetailsManager.
	 * setUsersByUsernameQuery("select email,password,enabled from users where email = ?"
	 * ); jdbcUserDetailsManager.
	 * setAuthoritiesByUsernameQuery("select email,authority from authorities where email = ?"
	 * ); return jdbcUserDetailsManager; }
	 */
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
