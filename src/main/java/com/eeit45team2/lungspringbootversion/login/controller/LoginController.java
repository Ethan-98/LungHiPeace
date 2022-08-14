package com.eeit45team2.lungspringbootversion.login.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.login.service.LoginService;

@Controller
public class LoginController {

	
	@Autowired
	private LoginService loginService;

	
	@RequestMapping(path = "/loginPage" , method = RequestMethod.GET)
	public String processMainAction() {
		return "login";
	}


	@RequestMapping(path = "/loginPage-error" , method=RequestMethod.GET)
	public String loginError(Model model) {
		model.addAttribute("errormsg", "請輸入正確的帳號和密碼");
		return "login";
	}

	
	@RequestMapping(path = "/indexPage" , method=RequestMethod.POST)
	public String checkLogin(@RequestParam("loginAccount") String username , @RequestParam("loginPassword") String pwd , Model m) {
		Map<String, String> errors = new HashMap<String, String>();
		m.addAttribute("errors", errors);
		
		if(username==null || username.length()==0) {
			errors.put("name", "帳號不能為空");
		}
		
		if(pwd==null ||pwd.length()==0) {
			errors.put("pwd", "密碼不能為空");
		}
		
		if(errors!=null && !errors.isEmpty()) {
			return "login";
		}
		
		MemberBean memberexisted = loginService.findByMiAccountAndMiPassword(username, pwd);
		
		if(memberexisted !=null) {
			m.addAttribute("user", username);
			m.addAttribute("pwd", pwd);
			return "index";  // 導到首頁
		}
		
		errors.put("msg", "請輸入正確的帳號和密碼");
		return "login";  //導回login頁面
		
		
	}
	
	
	

}
