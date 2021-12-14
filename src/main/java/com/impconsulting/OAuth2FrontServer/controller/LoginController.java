package com.impconsulting.OAuth2FrontServer.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
	
	private static final Log LOG = LogFactory.getLog(LoginController.class);
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
    public String loginGet(Model model) throws Exception {
    	Map<String, Object> params = new HashMap<String, Object>();
    	LOG.info("GET");
        return "loginForm";
    }   
	
	@ResponseBody
	@RequestMapping(value="/login", method=RequestMethod.POST)
    public ResponseEntity<String> loginPost(Model model, HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	String auth = request.getHeader("Authorization");
    	String scope = request.getHeader("scope");
    	
    	LOG.info("POST");
    	LOG.info("auth = " + auth);
    	LOG.info("scope = " + scope);
    	session.setAttribute("auth", auth);
    	session.setAttribute("scope", scope);
    	
        return ResponseEntity.ok("login success");
    }   
}
