package com.mysite.sbb.role;

import lombok.Getter;

@Getter
public enum UserRole {
	// ADMIN: 관리자(DBA), USER: 일반 사용자
	ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
	UserRole(String value) {
		this.value = value;
	}
	private String value;
}
