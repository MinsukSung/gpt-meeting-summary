package com.gpt.meetingnotes.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class XssEscapeFilter implements Filter {
	
	 @Override
	public void init(FilterConfig filterConfig) throws ServletException {
    	 log.info("[XssEscapeFilter] 초기화 완료");
	}
	    
	
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        XssRequestWrapper wrappedRequest = new XssRequestWrapper(req);
        
        chain.doFilter(wrappedRequest, response);
    }
    
    @Override
	public void destroy() {
		log.info("[XssEscapeFilter] 종료 및 자원 정리");
	}
    
}