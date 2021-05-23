package org.jp.spring.security.jdbc.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

	private String userName;
	private List<String> authorities;

}
