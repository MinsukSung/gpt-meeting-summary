package com.gpt.meetingnotes.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommonResponse<T> {
	private String code;
	private String message;
	private T data;
}
