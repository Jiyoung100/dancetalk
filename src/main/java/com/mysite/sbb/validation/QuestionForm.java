package com.mysite.sbb.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QuestionForm {

	@NotBlank(message="제목은 필수입니다.")
	@Size(max=200)
	private String subject;
	
	@NotBlank(message="내용은 필수입니다.")
	private String content;
	
	@NotNull(message="카테고리를 선택해주세요.")
	private Long categoryId;
	
}
