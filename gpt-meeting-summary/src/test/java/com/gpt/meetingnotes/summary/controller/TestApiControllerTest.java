package com.gpt.meetingnotes.summary.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.gpt.meetingnotes.common.enums.ServiceErrorCode;
import com.gpt.meetingnotes.summary.config.WebMvcTestConfig;
import com.gpt.meetingnotes.utils.TestUtils;

@SpringJUnitWebConfig
@ContextConfiguration(classes = {WebMvcTestConfig.class}) // MessageSource 포함된 Java Config
public class TestApiControllerTest {
	
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
	@DisplayName("API 정상응답 테스트")
	void testMessageResponse() throws Exception {
		MvcResult result = mockMvc.perform(get("/mock/summary/api/success"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value("정상 응답입니다"))
			.andReturn();
	    TestUtils.printPrettyJson(result);
	}
	
	@Test
	@DisplayName("API Valid 정상응답 테스트")
	void testMessageResponse2() throws Exception {
		MvcResult result = mockMvc.perform(post("/mock/summary/api/analyze")
				.param("meetingDate", "2025-07-18"))
			.andExpect(status().isOk())
			.andReturn();
	    TestUtils.printPrettyJson(result);
	}
	
	
	@Test
	@DisplayName("API Valid 에러응답 테스트")
	void testErrorValidResponse() throws Exception {
		MvcResult result = mockMvc.perform(post("/mock/summary/api/analyze"))
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message").value("입력값이 유효하지 않습니다. 입력 내용을 확인해주세요."))
			.andExpect(jsonPath("$.data").isNotEmpty())
			.andReturn();
	    TestUtils.printPrettyJson(result);
	}
	
	@Test
	@DisplayName("API Valid 에러응답 테스트(다국어)")
	void testErrorValidResponseEn() throws Exception {
		MvcResult result = mockMvc.perform(post("/mock/summary/api/analyze")
				.header("Accept-Language", "en"))
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("$.message").value("One or more fields have invalid values. Please check your input."))
			.andExpect(jsonPath("$.data").isNotEmpty())
			.andReturn();
	    TestUtils.printPrettyJson(result);
	}
	
	 @Test
	    @DisplayName("커스텀 예외 발생 테스트")
	    void testApplicationException() throws Exception {
	        MvcResult result = mockMvc.perform(get("/mock/summary/api/exception"))
	                .andExpect(status().isInternalServerError())
	                .andExpect(jsonPath("$.code").value(ServiceErrorCode.SUMMARY_GENERATION_FAILED.getCode()))
	                .andExpect(jsonPath("$.message").exists())
	                .andReturn();

	        TestUtils.printPrettyJson(result);
	    }

	    @Test
	    @DisplayName("NPE 강제 발생 테스트")
	    void testNullPointerException() throws Exception {
	        MvcResult result = mockMvc.perform(get("/mock/summary/api/null"))
	                .andExpect(status().isInternalServerError())
	                .andExpect(jsonPath("$.code").value(ServiceErrorCode.NULL_POINTER.getCode()))
	                .andExpect(jsonPath("$.message").exists())
	                .andReturn();

	        TestUtils.printPrettyJson(result);
	    }

	    @Test
	    @DisplayName("다국어 메시지 반환 테스트 - ko")
	    void testI18nKorean() throws Exception {
	        MvcResult result = mockMvc.perform(get("/mock/summary/api/i18n")
	                        .header("Accept-Language", "ko"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.message").value("성공"))  // messages_ko.properties 기준
	                .andReturn();

	        TestUtils.printPrettyJson(result);
	    }

	    @Test
	    @DisplayName("다국어 메시지 반환 테스트 - en")
	    void testI18nEnglish() throws Exception {
	        MvcResult result = mockMvc.perform(get("/mock/summary/api/i18n")
	                        .header("Accept-Language", "en"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.message").value("Success"))  // messages_en.properties 기준
	                .andReturn();

	        TestUtils.printPrettyJson(result);
	    }
}
