package org.jp.spring.security.jdbc.service;

import java.util.List;

import org.jp.spring.security.jdbc.dto.UserDto;

public interface AdminService {

	List<UserDto> findAllUserDto();

	String saveUser(UserDto userDto);

	void deleteUser(String userName);

	void updateUser(UserDto userDto);

}