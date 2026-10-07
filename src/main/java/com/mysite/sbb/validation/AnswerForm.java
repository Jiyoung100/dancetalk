package com.mysite.sbb.validation;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnswerForm {
	
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	private String content;

}
