package com.eeit45team2.lungspringbootversion.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LogoutController {
	
	@RequestMapping(path = "/logoutmain" , method = RequestMethod.GET)
	public String processMainAction() {
		return "login";  // 傳回登入畫面
	}

}
