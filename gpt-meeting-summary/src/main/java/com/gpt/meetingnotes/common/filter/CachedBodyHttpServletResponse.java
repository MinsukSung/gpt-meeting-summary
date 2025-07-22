package com.gpt.meetingnotes.common.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {
	
	private final ByteArrayOutputStream cacheBody = new ByteArrayOutputStream();
	private final ServletOutputStream outputStream;
	
	public CachedBodyHttpServletResponse(HttpServletResponse response) {
		super(response);
		outputStream = new ServletOutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				cacheBody.write(b);
			}
			
			@Override
			public void setWriteListener(WriteListener writeListener) {}
			
			@Override
			public boolean isReady() {
				return true;
			}
		};
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return outputStream;
	}
	
	public byte[] getCacheBody() {
		return cacheBody.toByteArray();
	}
}
