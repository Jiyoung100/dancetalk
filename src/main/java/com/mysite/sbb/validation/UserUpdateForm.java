package com.mysite.sbb.validation;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateForm {
	
	@NotBlank(message = "사용자 ID는 필수 항목입니다.")
	@Length(min=3, max=25)
	private String username;
	
	@NotBlank(message = "비밀번호는 필수 항목입니다.")
	private String password1;
	
	@NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
	private String password2;
	
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email
	private String email;
	
	@NotBlank(message = "닉네임은 필수 항목입니다.")
	private String nickname;
}
