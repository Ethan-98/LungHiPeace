package com.eeit45team2.lungspringbootversion.login.service.impl;

import com.eeit45team2.lungspringbootversion.backend.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.login.repository.LoginRepository;
import com.eeit45team2.lungspringbootversion.login.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginRepository loginRepository;

	@Override
	public MemberBean findByMiAccountAndMiPassword(String miAccount, String miPassword){
		return loginRepository.findByMiAccountAndMiPassword(miAccount, miPassword);
	}

}
