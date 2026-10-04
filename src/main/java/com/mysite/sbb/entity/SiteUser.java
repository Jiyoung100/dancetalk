package com.mysite.sbb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder @Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
public class SiteUser {

	// primary key, auto_increment 속성
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// 스프링 시큐리티를 통하여 암호화할 것이므로 길이를 제한하지 않도록 할 것
	private String password;
	
	// unique, not null 제약조건 설정
	@Column(unique = true, nullable = false, columnDefinition = "varchar(25)")
	private String username;
	
	@Column(unique = true, nullable = false, columnDefinition = "varchar(20)")
	private String nickname;
	
	@Column(columnDefinition = "datetime default now()")
	private LocalDate regDate;
	
	// unique 제약조건 설정
	@Column(unique = true, columnDefinition = "varchar(100)")
	private String email;
	
	// 회원이 작성한 게시글
    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Question> questionList = new ArrayList<>();

    // 회원이 작성한 댓글
    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Answer> answerList = new ArrayList<>();

}
