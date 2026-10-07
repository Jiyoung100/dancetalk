package com.mysite.sbb.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Category;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.service.AnswerService;
import com.mysite.sbb.service.CategoryService;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.service.UserService;
import com.mysite.sbb.validation.AnswerForm;
import com.mysite.sbb.validation.QuestionForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	private final CategoryService categoryService;

	// [ 전체 게시글 목록 ] - 카테고리 + 키워드 검색 + 페이징 처리
	@GetMapping("/list/{categoryId}")
	public String list(Model model, @RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="kw", defaultValue="")String kw,
			@PathVariable("categoryId") Long categoryId) {
		
		Category category = categoryService.getCategory(categoryId);

		Page<Question> paging = questionService.getList(page, kw, categoryId);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("category", category);
		return "question/question_list";
	}
	
	// [ 게시글 상세보기 ] - 댓글 페이징 처리
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, @RequestParam(value="page", defaultValue="1") int page) {
		Question question = questionService.getQuestion(id);

		Page<Answer> paging = answerService.getAnswerListPaging(question, page);
		
		int startPage = page - (page - 1) % 5;
        int endPage = startPage - 1 + 5;
		
		model.addAttribute("question", question);
		model.addAttribute("paging", paging);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "question/question_detail";
	}
	
	// [ 게시글 등록 ] - 등록 페이지 이동
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm, Model model) {
		List<Category> categoryList = categoryService.getList();
		model.addAttribute("categoryList",categoryList);
		return "question/question_form";
	}
	
	// [ 게시글 등록 ] - 등록 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, Model model) {

		if(bindingResult.hasErrors()) {
			List<Category> categoryList = categoryService.getList();
			model.addAttribute("categoryList",categoryList);
			return "question/question_form";
		}

		SiteUser siteUser = userService.getUser(principal.getName());
		Category category = categoryService.getCategory(questionForm.getCategoryId());
		questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser, category);		

		return "redirect:/question/list/" + category.getId();
	}
	
	// [ 게시글 수정 ] - 화면에 보여줌
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal, Model model)
		throws ResponseStatusException {
		Question question = questionService.getQuestion(id);

		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
		}

		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		questionForm.setCategoryId(question.getCategory().getId());
		model.addAttribute("categoryList", categoryService.getList());
		return "question/question_form";
	}
	
	// [ 게시글 수정 ] - 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
			@PathVariable("id") Integer id, Principal principal, Model model) throws ResponseStatusException {

		if(bindingResult.hasErrors()) {
			model.addAttribute("categoryList", categoryService.getList());
			return "question/question_form";
		}
		
		Question question = questionService.getQuestion(id);

		if(question.getAuthor() == null || !question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "질문 수정 권한이 없습니다.");
		}

		Category category = categoryService.getCategory(questionForm.getCategoryId());
		questionService.modify(question, questionForm.getSubject(), questionForm.getContent(), category);
		return "redirect:/question/detail/" + id;
	}
	
	// [ 게시글 삭제 ] - 처리
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(@PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);

		Long categoryId = question.getCategory().getId();
		question = questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "질문 삭제 권한이 없습니다.");
		}
		
		questionService.delete(question);
		return "redirect:/question/list/" + categoryId;
	}
	
	// [ 게시글 추천 ]
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}/{size}")
	public String questionVote(@PathVariable("id") Integer id, @PathVariable("size") Integer size, Principal principal) {
		Question question = questionService.getQuestion(id);
		SiteUser siteUser = userService.getUser(principal.getName());
		questionService.vote(question, siteUser, size);
		return "redirect:/question/detail/" + id;
	}
}
