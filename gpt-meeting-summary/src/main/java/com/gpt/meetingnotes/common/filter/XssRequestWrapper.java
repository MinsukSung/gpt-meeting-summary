package com.gpt.meetingnotes.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.text.StringEscapeUtils;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        return escape(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) return null;
        for (int i = 0; i < values.length; i++) {
            values[i] = escape(values[i]);
        }
        return values;
    }

    @Override
    public String getHeader(String name) {
        return escape(super.getHeader(name));
    }

    private String escape(String input) {
        return input != null ? StringEscapeUtils.escapeHtml4(input) : null;
    }
}