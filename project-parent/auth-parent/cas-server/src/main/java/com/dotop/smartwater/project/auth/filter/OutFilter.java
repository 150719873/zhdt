package com.dotop.smartwater.project.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;

//@Component
public class OutFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println(filterConfig);
	}

	@Override
	public void destroy() {
		System.out.println("destroy");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String curr = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json;charset=UTF-8");
		response.addHeader("Code", BaseExceptionConstants.SUCCESS);
		response.addHeader("Resp-Time", curr);
		chain.doFilter(request, response);
		String header = response.getHeader("Code");
		if (StringUtils.isEmpty(header)) {
			response.setContentType("application/json;charset=UTF-8");
			response.addHeader("Code", BaseExceptionConstants.SUCCESS);
			response.addHeader("Resp-Time", curr);
		}

	}
}
