package com.gpt.meetingnotes.summary.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gpt.meetingnotes.summary.config.WebMvcTestConfig;


@SpringJUnitWebConfig
@ContextConfiguration(classes = {WebMvcTestConfig.class})
public class TestWebControllerTest {
	
	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
	            .addFilters(new CharacterEncodingFilter("UTF-8", true))
	            .build();
	}
	
	@Test
	@DisplayName("정상 JSP 페이지 렌더링 테스트")
	void testHomePage() throws Exception {
	    mockMvc.perform(get("/mock/summary/"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("home"));
	}
	
	@Test
	@DisplayName("정상 분석 JSP 페이지 렌더링 테스트")
	void testAnalyzepage() throws Exception {
	    mockMvc.perform(get("/mock/summary/analyze")
	    			.param("meetingDate", "2025-07-18"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("summary/analyze"));
	}
	
	@Test
	@DisplayName("에러 분석 JSP 페이지 렌더링 테스트")
	void testErrorAnalyzepage() throws Exception {
	    mockMvc.perform(get("/mock/summary/analyze"))
	            .andExpect(status().isOk())
	            .andExpect(view().name("error/validationError"));
	}
	
	
	@Test
	@DisplayName("커스텀 예외 발생 시 에러 뷰 렌더링 테스트")
	void testApplicationExceptionView() throws Exception {
	    mockMvc.perform(get("/mock/summary/exception"))
	            .andExpect(view().name("error/serviceError"))
	            .andExpect(request().attribute("errorCode", "SUMMARY_FAILED"))
	            .andExpect(request().attribute("errorMessage", notNullValue()));
	}
	
	@Test
	@DisplayName("NPE 발생 시 에러 뷰 렌더링 테스트")
	void testNullPointerView() throws Exception {
	    mockMvc.perform(get("/mock/summary/null"))
	            .andExpect(view().name("error/serviceError"))
	            .andExpect(request().attribute("errorCode", "NULL_POINTER"))
	            .andExpect(request().attribute("errorMessage", notNullValue()));
	}
	
}
