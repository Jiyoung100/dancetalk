package com.mysite.sbb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Question;

// Answer 엔터티를 사용하여 CRUD를 하기 위한 인터페이스
//1. JpaRepository 인터페이스를 상속한 인터페이스 생성
//2. 제너릭 타입에서 사용할 2개의 속성
//- Answer: Answer 엔터티에 대해서 CRUD를 함
//- Integer: Answer 엔터티의 @Id(primary key)로 설정된 필드의 타입

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
	
	// 페이징 처리
	Page<Answer> findByQuestion(Question question, Pageable pageable);

}
