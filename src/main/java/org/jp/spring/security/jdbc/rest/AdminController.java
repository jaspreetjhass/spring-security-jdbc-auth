package org.jp.spring.security.jdbc.rest;

import java.util.List;

import org.jp.spring.security.jdbc.dto.UserDto;
import org.jp.spring.security.jdbc.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("jdbc-auth/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/users")
	public List<UserDto> getAllUser() {
		log.info("enter into getAllUser method.");
		final List<UserDto> userDtoList = adminService.findAllUserDto();
		log.info("exit from getAllUser method with output : {}.", userDtoList);
		return userDtoList;
	}

	@PostMapping("/users")
	public String createUser(@RequestBody final UserDto userDto) {
		log.info("enter into createUser mwthod with parameters : {}.", userDto);
		final String password = adminService.saveUser(userDto);
		log.info("exit from createUser method with output : {}.");
		return password;
	}

	@DeleteMapping("/users/{username}")
	public void deleteUser(final String username) {
		log.info("enter into deleteUser mwthod with parameters : {}.", username);
		if (!StringUtils.pathEquals("admin", username))
			adminService.deleteUser(username);
		log.info("exit from deleteUser method.");
	}

	@PutMapping("/users/{username}")
	public void updateUser(@RequestBody final UserDto userDto, @PathVariable final String username) {
		log.info("enter into updateUser mwthod with parameters : {}.", userDto);
		adminService.updateUser(userDto);
		log.info("exit from updateUser method.");
	}
}