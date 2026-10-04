package com.mysite.sbb.validation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnswerForm {
	
	@NotBlank(message = "답변 내용은 필수 입력 항목입니다.")
	private String content;

}
