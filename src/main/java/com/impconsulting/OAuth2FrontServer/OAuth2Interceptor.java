 package com.impconsulting.OAuth2FrontServer;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2Interceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(true);

		Object vo = session.getAttribute("USER");

		if (vo != null) {
			return true;
		} else {
//			ModelAndView modelAndView = new ModelAndView("/login");
//			throw new ModelAndViewDefiningException(modelAndView);

			response.sendRedirect(request.getContextPath() + "/login");
			return false;
		}
	}
}