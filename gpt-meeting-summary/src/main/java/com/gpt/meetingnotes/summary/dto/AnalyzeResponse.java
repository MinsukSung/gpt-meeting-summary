package com.gpt.meetingnotes.summary.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeResponse {
	private String meetingDate;
	private List<String> attendees;
	private String summary;
}
