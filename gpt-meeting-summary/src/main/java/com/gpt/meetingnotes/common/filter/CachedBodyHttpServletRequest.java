package com.gpt.meetingnotes.common.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper{
	
	private final byte[] cachedBody; 
	
	public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(cachedBody);
		
		return new ServletInputStream() {
			
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return bis.read();
			}
			
			@Override
			public void setReadListener(ReadListener readListener) { }
			
			@Override
			public boolean isReady() {
				return true;
			}
			
			@Override
			public boolean isFinished() {
				return bis.available() == 0;
			}
		};
		
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
}
