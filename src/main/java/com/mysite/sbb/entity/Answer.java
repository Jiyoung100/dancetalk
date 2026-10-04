package com.mysite.sbb.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 답변 클래스 - 질문에 대한 답변
@Entity
@Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@CreatedDate
	@Column(columnDefinition = "datetime default now()")
	private LocalDateTime createDate;
	
	// 질문1개에 대해서 답변을 여러 개가 될 수 있음
	@ManyToOne
	private Question question;
	
	// 작성자
	// 1명의 사용자는 여러 개의 답변을 남길 수 있음
	@ManyToOne
	private SiteUser author;
	
	private LocalDateTime modifyDate;
	
	// 추천
	// 모든 답변은 여러 명이 추천할 수 있음
	@ManyToMany
	private Set<SiteUser> voter;
}
