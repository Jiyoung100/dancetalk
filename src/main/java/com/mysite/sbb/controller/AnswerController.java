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
	
	// [ 댓글 등록 ] - 등록 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String answerCreate(Model model, @PathVariable("id") Integer id, 
			@Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {

		Question question = questionService.getQuestion(id);

		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question/question_detail";
		}

		SiteUser siteUser = userService.getUser(principal.getName());

		Answer answer = answerService.create(question, answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	// [ 댓글 수정 ] - 수정 페이지로 이동
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);

		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 수정 권한이 없습니다.");
		}
		
		answerForm.setContent(answer.getContent());
		return "question/answer_form";
	}
	
	// [ 댓글 수정 ] - 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {

		if(bindingResult.hasErrors()) {
			return "question/answer_form";
		}

		Answer answer = answerService.getAnswer(id);

		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 수정 권한이 없습니다.");
		}
		
		answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	// [ 댓글 삭제 ] - 처리
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(@PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);

		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "답변 삭제 권한이 없습니다.");
		}
		
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	// [ 댓글 추천 ]
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}/{size}")
	public String answerVote(@PathVariable("id") Integer id, @PathVariable("size") int size, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		SiteUser siteUser = userService.getUser(principal.getName());
		answerService.vote(answer, siteUser, size);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
}
