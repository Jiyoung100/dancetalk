package com.mysite.sbb.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	// 댓글 등록 1 - 각 필드 사용
	public Answer create(Question quesiton, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(quesiton);
		answer.setAuthor(author);
		answerRepository.save(answer);
		return answer;
	}
	
	// 댓글 등록 2 - 객체 사용
	public void create(Answer answer) {
		answerRepository.save(answer);
	}
	
	// 댓글 조회 - id로 조회
	public Answer getAnswer(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		if(answer.isPresent()) {
			return answer.get();
		} else {
			throw new DataNotFoundException("Answer Not Found!");
		}
	}
	
	// 댓글 수정
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		answerRepository.save(answer);
	}
	
	// 댓글 삭제
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
	
	// 댓글 추천
	public void vote(Answer answer, SiteUser siteUser, int size) {
		if(answer.getVoter().contains(siteUser)) {
			answer.getVoter().remove(siteUser);
		} else {
			answer.getVoter().add(siteUser);
		}
		answerRepository.save(answer);
	}
	
	// 댓글 페이징 - 작성일을 기준으로 내림차순 정렬
	public Page<Answer> getAnswerListPaging(Question question, int page){
		Pageable pageable = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.DESC, "createDate"));
		return answerRepository.findByQuestion(question, pageable);
	}
}
