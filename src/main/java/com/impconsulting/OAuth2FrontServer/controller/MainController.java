package com.impconsulting.OAuth2FrontServer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Controller
public class MainController {

	private static final Log LOG = LogFactory.getLog(MainController.class);
	@Autowired
	private WebClient webClient;

	@RequestMapping(value="/main", method=RequestMethod.GET)
	public String mainGet(Model model, HttpServletResponse response, HttpSession session) throws Exception {
		//Map<String, Object> params = new HashMap<String, Object>();
    	response.addHeader("auth", (String) session.getAttribute("auth"));
    	response.addHeader("refreshToken", (String) session.getAttribute("refreshToken"));
    	response.addHeader("scope", (String) session.getAttribute("scope"));
		return "main";
	}
	
	@RequestMapping(value="/main", method=RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> mainPost(Model model, HttpServletResponse response, HttpSession session) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
	
		Map<String, Object> result = webClient.get().uri("/user/data").headers(headers -> {
			headers.add("Authorization", (String) session.getAttribute("auth"));
			headers.add("scope", (String) session.getAttribute("scope"));
		}).retrieve().bodyToMono(HashMap.class).block();
		
		data.put("result", result);
		
		LOG.info(data);
		return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);   
	}
}
