package com.mysite.sbb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String password;

	@Column(unique = true, nullable = false, columnDefinition = "varchar(25)")
	private String username;
	
	@Column(unique = true, nullable = false, columnDefinition = "varchar(20)")
	private String nickname;
	
	@Column(columnDefinition = "datetime default now()")
	private LocalDate regDate;

	@Column(unique = true, columnDefinition = "varchar(100)")
	private String email;

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Question> questionList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Answer> answerList = new ArrayList<>();

}
