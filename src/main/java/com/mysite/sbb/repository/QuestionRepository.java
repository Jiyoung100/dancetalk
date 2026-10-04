package com.mysite.sbb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mysite.sbb.entity.Question;

// Question 엔터티를 사용하여 CRUD를 하기 위한 인터페이스
// 1. JpaRepository 인터페이스를 상속한 인터페이스 생성
// 2. 제너릭 타입에서 사용할 2개의 속성
// - Question: Question 엔터티에 대해서 CRUD를 함
// - Integer: Question 엔터티의 @Id(primary key)로 설정된 필드의 타입
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	// 페이징 처리
	Page<Question> findAll(Pageable pageable);
	
	// 키워드 검색 + 페이징 처리 -> 1번 방법
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	// 키워드 검색 + 페이징 처리 -> 2번 방법
	public final String SEARCH_KEYWORD =
	        "select distinct q from Question q "
	        + " left outer join SiteUser u1 on q.author = u1 "
	        + " left outer join Answer a on a.question = q "
	        + " left outer join SiteUser u2 on a.author = u2 "
	        + " where q.category.id = :categoryId "
	        + " and ("
	        + " q.subject like %:kw% "
	        + " or q.content like %:kw% "
	        + " or u1.username like %:kw% "
	        + " or a.content like %:kw% "
	        + " or u2.username like %:kw% "
	        + " )";
	@Query(value = SEARCH_KEYWORD, nativeQuery = false)
	Page<Question> queryAllByKeyword(@Param("kw") String kw,@Param("categoryId") Long categoryId,
	        Pageable pageable
	);
	
	Page<Question> findByCategoryId(Long categoryId, Pageable pageable);
}
