package com.gpt.meetingnotes.summary.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.gpt.meetingnotes.common.validation.ValidationGroups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeRequest {
	
	@NotBlank(groups = {ValidationGroups.Analyze.class})
	private String meetingDate;
	
	@NotEmpty(groups= {ValidationGroups.Analyze.class})
	private List<@NotBlank(groups= {ValidationGroups.Analyze.class}) String> attendees;
	
	@NotBlank(groups = {ValidationGroups.Analyze.class})
	private String summary;
}
