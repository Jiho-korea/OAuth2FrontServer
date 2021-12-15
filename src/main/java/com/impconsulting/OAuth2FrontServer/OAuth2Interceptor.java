 package com.impconsulting.OAuth2FrontServer;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import com.impconsulting.OAuth2FrontServer.controller.MemberController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Interceptor implements HandlerInterceptor {
	
	private static final Log LOG = LogFactory.getLog(OAuth2Interceptor.class);
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);

		String auth = (String)session.getAttribute("auth");
		String scope = (String)session.getAttribute("scope");
		
		//LOG.info("interceptor auth = " + auth);
		if (auth != null) {
			return true;
		} else {
			LOG.info("로그인 인터셉터 작동");
			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
	}
}