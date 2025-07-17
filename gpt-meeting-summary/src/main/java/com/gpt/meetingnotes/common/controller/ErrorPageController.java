package com.gpt.meetingnotes.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {
	@GetMapping("/error/404")
	public String error404() {
		return "error/404";
	}
	
	@GetMapping("/error/500")
	public String error500() {
	    return "error/500";
	}
	
	@GetMapping("/error/serviceError")
	public String errorServiceError() {
	    return "error/serviceError";
	}
	
	
	@GetMapping("/error/default")
	public String errorDefault() {
	    return "error/default";
	}
}
