package com.mysite.sbb.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	//  1. 회원가입(추가) - 각 필드 사용
	public SiteUser create(String username, String password, String email, String nickname) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		// 비밀번호를 암호화하여 저장
		user.setPassword(passwordEncoder.encode(password));
		user.setEmail(email);
		user.setNickname(nickname);
		user.setRegDate(LocalDate.now());
		userRepository.save(user);
		return user;
	}
	
	//  2. 회원가입 - 객체 사용
	public SiteUser create(SiteUser user) {
		userRepository.save(user);
		return user;
	}

	// 사용자 조회
	public SiteUser getUser(String username) throws DataNotFoundException {
		Optional<SiteUser> siteUser = userRepository.findByUsername(username);
		
		if(siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("사용자가 존재하지 않습니다.");
		}
	}
	
	// 회원 수정
	public SiteUser update(String username, String password, String email, String nickname) {
		SiteUser user = this.getUser(username);
		user.setPassword(passwordEncoder.encode(password));
		user.setEmail(email);
		user.setNickname(nickname);
		userRepository.save(user);
		return user;
	}
	
	// 회원 수정
	public SiteUser update(SiteUser user) {
		userRepository.save(user);
		return user;
	}
	
	// 회원 삭제
	public void delete(String username) {
		SiteUser user = this.getUser(username);
		userRepository.delete(user);
	}
}
