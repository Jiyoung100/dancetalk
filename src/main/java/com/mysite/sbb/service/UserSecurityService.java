package com.mysite.sbb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.repository.UserRepository;
import com.mysite.sbb.role.UserRole;

import lombok.RequiredArgsConstructor;

// 로그인을 할 때 사용하는 서비스 클래스
// 스프링부트 시큐리티가 관리하는 서비스 클래스가 되려면, UserDetailsService 인터페이스를 구현해야 함
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<SiteUser> _siteUser = userRepository.findByUsername(username);
		
		// 사용자 ID가 없다면
		if(_siteUser.isEmpty()) {
			throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
		}
		
		// 사용자 ID가 있다면
		SiteUser siteUser = _siteUser.get();
		
		// 사용자를 확인하여 사용자에 맞는 권한을 부여
		List<GrantedAuthority> authorites = new ArrayList<>();
		if("admin".equals(username)) {
			authorites.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
		} else {
			authorites.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
		}
		
		return new User(siteUser.getUsername(), siteUser.getPassword(), authorites);
	}
	
	
}
