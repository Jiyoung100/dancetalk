package com.mysite.sbb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Question;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
	// 페이징 처리
	Page<Answer> findByQuestion(Question question, Pageable pageable);

}
