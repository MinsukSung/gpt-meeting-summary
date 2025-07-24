package com.gpt.meetingnotes.summary.dto;

import javax.validation.constraints.NotBlank;

import com.gpt.meetingnotes.common.annotation.Masking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaskingResponse {

	@Masking
	@NotBlank
	private String rrn;
	
	@Masking
	@NotBlank
	private String email;
	
	@Masking
	private String phone;
	
	@Masking
	private String maskingText;
	
	private String text;
	
}
