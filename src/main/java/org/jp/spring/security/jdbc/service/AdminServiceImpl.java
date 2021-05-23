package org.jp.spring.security.jdbc.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jp.spring.security.jdbc.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private JdbcUserDetailsManager jdbcUserDetailsManager;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public List<UserDto> findAllUserDto() {
		log.info("enter into findAllUserDto method.");

		final JdbcTemplate jdbcTemplate = jdbcUserDetailsManager.getJdbcTemplate();

		final RowMapper<UserDto> rowMapper = (rs, index) -> {
			return UserDto.builder().userName(rs.getString("username"))
					.authorities(Arrays.asList(rs.getString("authority"))).build();
		};

		final List<UserDto> userDtoList = jdbcTemplate.query(
				"select u.username, a.authority from users u inner join authorities a where u.username = a.username",
				rowMapper);

		log.info("user details fetched from db are  : {}.", userDtoList);

		final Map<String, List<String>> userDetailMap = new HashMap<>();

		// iterate through user dto list, create a mapping of username,roles
		userDtoList.stream().forEach(userDetails -> {
			final String username = userDetails.getUserName();
			final String authority = !CollectionUtils.isEmpty(userDetails.getAuthorities())
					? userDetails.getAuthorities().get(0)
					: null;
			if (userDetailMap.containsKey(username)) {
				final List<String> tempList = userDetailMap.get(username);
				if (authority != null)
					tempList.add(authority);
				userDetailMap.put(username, tempList);
			} else {
				if (StringUtils.hasLength(authority))
					userDetailMap.put(username, new ArrayList<>(Arrays.asList(authority)));
			}
		});

		final List<UserDto> result = userDetailMap.keySet().parallelStream().map(key -> {
			return UserDto.builder().userName(key).authorities(userDetailMap.get(key)).build();
		}).collect(Collectors.toList());

		log.info("exit from findAllUserDto method with output : {}.", result);
		return result;
	}

	@Override
	public String saveUser(final UserDto userDto) {
		log.info("enter into saveUser method with parameters : {}.", userDto);
		final String password = UUID.randomUUID().toString();
		jdbcUserDetailsManager.createUser(
				User.builder().username(userDto.getUserName()).password(bCryptPasswordEncoder.encode(password))
						.authorities(userDto.getAuthorities().toArray(new String[0])).build());
		log.info("exit from saveUser method with output : {}.", password);
		return password;
	}

	@Override
	public void deleteUser(final String userName) {
		log.info("enter into deleteUser method with parameters : {}.", userName);
		jdbcUserDetailsManager.deleteUser(userName);
		log.info("exit from deleteUser method.");
	}

	@Override
	public void updateUser(final UserDto userDto) {
		log.info("enter into updateUser method with parameters : {}.", userDto);
		UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(userDto.getUserName());
		userDetails = User.withUserDetails(userDetails).authorities(userDto.getAuthorities().toArray(new String[0]))
				.build();
		jdbcUserDetailsManager.updateUser(userDetails);
		log.info("exit from updateUser method.");
	}

}
