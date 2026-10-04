package com.mysite.sbb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Category;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.repository.QuestionRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class QuestionService {
	
	// < 레파지토리 객체를 사용하는 방법 >
	// final을 사용하고, 클래스 위에는 @RequiredArgsConstructor를 붙여서 사용
	private final QuestionRepository questionRepository;
	
	// 전체 질문 목록 리턴
	public List<Question> getList() {
		return questionRepository.findAll();
	}
	
	// 키워드 검색에서 사용할 메소드
	private Specification<Question> search(String kw) {
		return new Specification<>() {
			private static final long serialVersionUID = 1L;
			@Override
			public @Nullable Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query,CriteriaBuilder cb) {
				query.distinct(true);
				Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
				Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
				Join<Answer, SiteUser> u2 = q.join("author", JoinType.LEFT);
				return cb.or(cb.like(q.get("subject"), "%" + kw + "%"),
						cb.like(q.get("content"), "%" + kw + "%"),
						cb.like(u1.get("username"), "%" + kw + "%"),
						cb.like(a.get("content"), "%" + kw + "%"),
						cb.like(u2.get("username"), "%" + kw + "%")
				);
			}
		};
	}
	
	// 카테고리 + 키워드 검색 + 페이징 처리
	public Page<Question> getList(int page, String kw, Long categoryId) {
		// 작성날짜(제목)의 내림차순으로 저장
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate")); // 작성날짜의 내림차순
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		
		// 검색어가 없는 경우
	    if (kw == null || kw.trim().isEmpty()) {
	        return questionRepository.findByCategoryId(categoryId,pageable);
	    }

	    // 검색어가 있는 경우
	    return questionRepository.queryAllByKeyword(kw,categoryId,pageable);
	}
	
	// 페이징 처리를 한 질문 목록 리턴
	// page: 조회할 페이지번호
	// 10: 한 페이지에 보여줄 질문 개수
	public Page<Question> getList(int page) {
		// 작성날짜(제목)의 내림차순으로 저장
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate")); // 작성날짜의 내림차순
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return questionRepository.findAll(pageable);
	}
	
	// 질문 1개 리턴
	@Transactional
	public Question getQuestion(Integer id) {
		Optional<Question> question = questionRepository.findById(id);
		if(question.isPresent()) {
			// 조회수 처리
			Question q = question.get();
			q.setViewCount(q.getViewCount()+1);
			return q;
		} else {
			throw new DataNotFoundException("Question Not Found!");
		}
	}
	
	// 질문 등록 1 - 각 필드 사용
	public void create(String subject, String content, SiteUser author, Category category) {
		Question q = new Question();
		q.setSubject(subject);
		q.setContent(content);
		q.setCreateDate(LocalDateTime.now());
		q.setAuthor(author);
		q.setCategory(category);
		questionRepository.save(q);
	}
	
	// 질문 등록 2 - 객체 사용
	public void create(Question question) {
		questionRepository.save(question);
	}
	
	// 질문 수정
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		questionRepository.save(question);
	}
	
	// 질문 삭제
	public void delete(Question question) {
		questionRepository.delete(question);
	}
	
	// 질문 추천
	public void vote(Question question, SiteUser siteUser, int size) {
		if(question.getVoter().contains(siteUser)) {
		    question.getVoter().remove(siteUser);
		} else {
		    question.getVoter().add(siteUser);
		}
		questionRepository.save(question);
	}
	
}
