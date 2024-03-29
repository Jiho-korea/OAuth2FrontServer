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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/front")
public class MemberController {
	
	private static final Log LOG = LogFactory.getLog(MemberController.class);
	
	@GetMapping(value="/login/")
    public String loginGet(Model model, HttpServletRequest request) throws Exception {
    	Map<String, Object> params = new HashMap<String, Object>();
//    	LOG.info("Login Get: " + request.getMethod());
        return "loginForm";
    }   
	
	@ResponseBody
	@PostMapping(value="/login/")
    public ResponseEntity<String> loginPost(Model model, HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, Object> params = new HashMap<String, Object>();
    	//LOG.info("Login Post: " + request.getMethod());
    	String tokenType = request.getHeader("tokenType");
    	String accessToken = request.getHeader("accessToken");
    	String refreshToken = request.getHeader("refreshToken");
    	String scope = request.getHeader("scope");
    	
    	//LOG.info("member controller accessToken: " +  accessToken);
//    	LOG.info("POST");
//    	LOG.info("auth = " + auth);
//    	LOG.info("refreshToken = " + refreshToken);
//    	LOG.info("scope = " + scope);
    	session.setAttribute("tokenType", tokenType);
    	session.setAttribute("accessToken", accessToken);
    	session.setAttribute("refreshToken", refreshToken);
    	session.setAttribute("scope", scope);
    	
        return ResponseEntity.ok("login success");
    }  
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
    public ResponseEntity<String> logoutGet(Model model, HttpServletRequest request, HttpSession session) throws Exception {
    	Map<String, Object> params = new HashMap<String, Object>();
    	
    	Object obj = session.getAttribute("accessToken");
		// 세션 제거
		if (obj != null) {
			session.removeAttribute("accessToken");
			session.invalidate();
		}
		
        return ResponseEntity.ok("logout success");
    }   
}
