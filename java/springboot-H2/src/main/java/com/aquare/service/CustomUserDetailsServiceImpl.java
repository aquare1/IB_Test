package com.aquare.service;


import com.aquare.domain.UserDetail;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author dengtao aquare@163.com  
 * @createAt 2022/2/23
 */
@Log4j2
@Component(value="CustomUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetail userDetail = new UserDetail();
		userDetail.setUsername(username);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userDetail.setPassword(encoder.encode("admin123"));

		return userDetail;
	}

}
