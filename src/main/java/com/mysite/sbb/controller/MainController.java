package com.mysite.sbb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	// [ 루트 ]
	// redirect:경로명 -> 클라이언트의 요청시에 해당 경로로 바로 이동하는 것
	@GetMapping("/")
	public String root() {
		return "index";
	}

}
