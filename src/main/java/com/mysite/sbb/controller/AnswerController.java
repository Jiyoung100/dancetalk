package com.mysite.sbb.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;
import com.mysite.sbb.validation.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {
	
	private final UserService userService;
	private final QuestionService questionService;
	private final AnswerService answerService;
	
	// [ 답변 등록 - 등록 처리 ]
	// - 답변폼 입력 검증 기능 구현
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String answerCreate(Model model, @PathVariable("id") Integer id, 
			@Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		// 몇번 질문에 대한 답변인지 확인
		Question question = questionService.getQuestion(id);
				
		// 유효성 검증에 문제가 있다면 처리
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question/question_detail";
		}
		
		// 유효성 검증에 문제가 없다면 처리
		// 답변을 남기는 사용자 정보 확인
		SiteUser siteUser = userService.getUser(principal.getName());
		
		// 답변 저장 (질문과 함께 답변 저장)
		Answer answer = answerService.create(question, answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	// [ 답변 수정 - 수정 페이지로 이동 ]
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		
		// 작성자와 로그인한 사용자가 같음을 확인
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 수정 권한이 없습니다.");
		}
		
		answerForm.setContent(answer.getContent());
		return "question/answer_form";
	}
	
	// [ 답변 수정 - 처리 ]
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {
		// 유효성 검증에 실패했다면
		if(bindingResult.hasErrors()) {
			return "question/answer_form";
		}
		
		// 유효성 검증에 성공했다면
		Answer answer = answerService.getAnswer(id);
		
		// 작성자와 로그인한 사용자가 같음을 확인
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 수정 권한이 없습니다.");
		}
		
		answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	// [ 답변 삭제 - 처리 ]
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		
		// 작성자와 로그인한 사용자가 같음을 확인
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 삭제 권한이 없습니다.");
		}
		
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	// [ 답변 추천 ]
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}/{size}")
	public String answerVote(@PathVariable("id") Integer id, @PathVariable("size") int size, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		SiteUser siteUser = userService.getUser(principal.getName());
		answerService.vote(answer, siteUser, size);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
}
