package com.aiur.rest.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

/**
 * Custom dispatcher servlet
 *
 */
@SuppressWarnings("serial")
public class BaseServlet extends DispatcherServlet {
	/**
	 * Do service
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.doService(request, response);
	}

	/**
	 * Do dispatch
	 */
	@Override
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.doDispatch(request, response);
	}
}
