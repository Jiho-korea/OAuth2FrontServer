package com.impconsulting.OAuth2FrontServer.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/front")
public class MainController {

	private static final Log LOG = LogFactory.getLog(MainController.class);

	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public String mainGet(Model model, HttpServletResponse response, HttpSession session) throws Exception {
		return "main";
	}
}
