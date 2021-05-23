package org.jp.spring.security.jdbc.db.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "jdbc.auth.datasource")
public class DbConfig {

	private String driverClassName;
	private String url;
	private String userName;
	private String password;

	@Bean
	DataSource dataSource() {
		return DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(userName)
				.password(password).build();
	}

}
