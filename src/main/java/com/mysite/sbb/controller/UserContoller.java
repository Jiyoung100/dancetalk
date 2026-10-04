package com.mysite.sbb.controller;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.service.UserService;
import com.mysite.sbb.validation.UserCreateForm;
import com.mysite.sbb.validation.UserUpdateForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserContoller {
	
	private final UserService userService;
	
	// [ 로그인 - 페이지 이동 ]
	// - 로그인 처리는 스프링부트 시큐리티가 처리하도록 함
	@GetMapping("/login")
	public String login() {
		return "user/login_form";
	}
		
	// [ 회원 가입 - 페이지 이동 ]
	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "user/signup_form";
	}
	
	// [ 회원 가입 - 처리 ]
	@PostMapping("/signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		
		// 입력한 데이터에 에러가 있다면,
		if(bindingResult.hasErrors()) {
			return "user/signup_form";
		}
		// 입력한 2개의 비밀번호가 일치하지 않는다면,
		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			// rejectValue(필드명, 에러코드, 에러메시지)
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			return "user/signup_form";
		}
		
		// 입력한 데이터에 에러가 없다면 처리
		// 중복 아이디에 대한 예외 처리
		try {
			userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail(), userCreateForm.getNickname());
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			// reject(에러코드, 에러메시지)
			bindingResult.reject("signUpFailed", "이미 등록된 사용자입니다.");
			return "user/signup_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signUpFailed", e.getMessage());
			return "user/signup_form";
		}
		return "redirect:/";
	}
	
	// [ 회원 정보 조회 ]
	@GetMapping("/profile")
	public String profile(Model model, Principal principal) {
		SiteUser siteUser = userService.getUser(principal.getName());
		model.addAttribute("siteUser", siteUser);
		return "user/user_profile";
	}
	
	// [ 회원 정보 수정 - 페이지 이동 ]
	@GetMapping("/update")
	public String update(UserUpdateForm userUpdateForm, Principal principal) {
		SiteUser user = userService.getUser(principal.getName());
		userUpdateForm.setUsername(user.getUsername());
		userUpdateForm.setEmail(user.getEmail());
		userUpdateForm.setNickname(user.getNickname());
		return "user/user_update_form";
	}
	
	// [ 회원 정보 수정 - 처리 ]
	@PostMapping("/update")
	public String update(@Valid UserUpdateForm userUpdateForm, BindingResult bindingResult, Principal principal) {
		
		// 입력한 데이터에 에러가 있는 경우
		if(bindingResult.hasErrors()) {
			return "user/user_update_form";
		}
		// 입력한 2개의 비밀번호가 일치하지 않는 경우
		if(!userUpdateForm.getPassword1().equals(userUpdateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
			return "user/user_update_form";
		}
		
		try {
			userService.update(userUpdateForm.getUsername(), userUpdateForm.getPassword1(), userUpdateForm.getEmail(), userUpdateForm.getNickname());
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signUpFailed", "이미 등록된 사용자입니다.");
			return "user/user_update_form";
		} catch(Exception e) {
			e.printStackTrace();
			bindingResult.reject("signUpFailed", e.getMessage());
			return "user/user_update_form";
		}
		
		return "redirect:/user/profile";
	}
	
	// 회원 삭제
	@PostMapping("/delete")
	public String delete(Principal principal, HttpServletRequest request) {
		userService.delete(principal.getName());
		
		SecurityContextHolder.clearContext();
	    request.getSession().invalidate();
		
		return "redirect:/";
	}

}
