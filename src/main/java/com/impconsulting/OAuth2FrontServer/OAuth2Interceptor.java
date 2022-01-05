 package com.impconsulting.OAuth2FrontServer;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Interceptor implements HandlerInterceptor {
	
	private static final Log LOG = LogFactory.getLog(OAuth2Interceptor.class);
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);

		String accessToken = (String)session.getAttribute("accessToken");
		String scope = (String)session.getAttribute("scope");
		
		// LOG.info("interceptor accessToken: " +  accessToken);
		
		if (accessToken != null) {
			return true;
		} else {
			LOG.info("로그인 인터셉터 작동");
			response.sendRedirect(request.getContextPath() + "/front/login/");
			return false;
		}
	}
}