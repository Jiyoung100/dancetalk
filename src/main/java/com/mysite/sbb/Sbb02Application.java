package com.mysite.sbb;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;

// 프로젝트가 시작되는 클래스를 알려주는 애너테이션
@SpringBootApplication
public class Sbb02Application {

	public static void main(String[] args) {
		SpringApplication.run(Sbb02Application.class, args);
	}
	
	// 사용자, 질문, 답변 더미 데이터 추가 -> 엔터티를 활용
	//@Bean // -> 사용후에 주석처리 할 것
	public CommandLineRunner run(UserService userService, QuestionService questionService, AnswerService answerService) {
		return (String[] args) -> {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			// 1. 사용자 추가 - USER
			SiteUser user1 = SiteUser.builder().username("aaaa").password(passwordEncoder.encode("1234"))
			        .email("aaaa@naver.com").nickname("리듬토끼").regDate(LocalDate.now()).build();
			SiteUser user2 = SiteUser.builder().username("bbbb").password(passwordEncoder.encode("1234"))
			        .email("bbbb@naver.com").nickname("스텝바이").regDate(LocalDate.now()).build();
			SiteUser user3 = SiteUser.builder().username("cccc").password(passwordEncoder.encode("1234"))
			        .email("cccc@naver.com").nickname("그루브온").regDate(LocalDate.now()).build();
			SiteUser user4 = SiteUser.builder().username("dddd").password(passwordEncoder.encode("1234"))
			        .email("dddd@naver.com").nickname("왁킹스타").regDate(LocalDate.now()).build();
			SiteUser user5 = SiteUser.builder().username("eeee").password(passwordEncoder.encode("1234"))
			        .email("eeee@naver.com").nickname("팝핀구름").regDate(LocalDate.now()).build();
			SiteUser user6 = SiteUser.builder().username("ffff").password(passwordEncoder.encode("1234"))
			        .email("ffff@naver.com").nickname("코레오픽").regDate(LocalDate.now()).build();
			SiteUser user7 = SiteUser.builder().username("gggg").password(passwordEncoder.encode("1234"))
			        .email("gggg@naver.com").nickname("무브메이트").regDate(LocalDate.now()).build();
			SiteUser user8 = SiteUser.builder().username("hhhh").password(passwordEncoder.encode("1234"))
			        .email("hhhh@naver.com").nickname("비트웨이브").regDate(LocalDate.now()).build();
			SiteUser user9 = SiteUser.builder().username("iiii").password(passwordEncoder.encode("1234"))
			        .email("iiii@naver.com").nickname("댄스로그").regDate(LocalDate.now()).build();
			SiteUser user10 = SiteUser.builder().username("jjjj").password(passwordEncoder.encode("1234"))
			        .email("jjjj@naver.com").nickname("플로우업").regDate(LocalDate.now()).build();
			userService.create(user1);
			userService.create(user2);
			userService.create(user3);
			userService.create(user4);
			userService.create(user5);
			userService.create(user6);
			userService.create(user7);
			userService.create(user8);
			userService.create(user9);
			userService.create(user10);
			
		};
	}

}
