package com.mysite.sbb.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 질문 클래스
@Entity
@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200)
	private String subject;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@CreatedDate
	@Column(columnDefinition = "datetime default now()")
	private LocalDateTime createDate;
	
	// 질문 1개에 답변이 여러 개가 될 수 있음
	// mappedBy는 Answer 엔터티에 있는 필드명
	// cascade의 CascadeType.REMOVE는 질문을 삭제했을 때, 질문에 대한 답변도 함께 삭제함 
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	// 작성자
	// 1명의 사용자는 여러 개의 질문을 남길 수 있음
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	// 추천
	// 모든 질문에 대해서 추천은 여러 명이 할 수 있음
	// Set의 장점: 중복을 허용하지 않음
	@ManyToMany
	private Set<SiteUser> voter;
	
	// 조회수
	@Column(columnDefinition = "int default 0")
	private Integer viewCount = 0;
	
	// 케타고리
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;
}
