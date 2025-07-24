package com.gpt.meetingnotes.summary.dto;

import javax.validation.constraints.NotBlank;

import com.gpt.meetingnotes.common.annotation.Masking;
import com.gpt.meetingnotes.common.annotation.ValidJumin;
import com.gpt.meetingnotes.common.enums.MaskingType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaskingRequest {

	@Masking(type = MaskingType.JUMIN)
	@ValidJumin
	private String rrn;
	
	@Masking(type = MaskingType.EMAIL)
	@NotBlank
	private String email;
	
	@Masking(type = MaskingType.PHONE)
	private String phone;
	
	@Masking
	private String maskingText;
	
	private String text;
}
