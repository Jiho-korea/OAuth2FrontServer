package com.impconsulting.OAuth2FrontServer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	private static final Log LOG = LogFactory.getLog(MainController.class);
	
    @RequestMapping("/main")
    public String main(Model model) throws Exception {
    	Map<String, Object> params = new HashMap<String, Object>();

        return "main";
    }
}
