package com.eeit45team2.lungspringbootversion.login.service;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;

public interface LoginService {

	public MemberBean findByMiAccountAndMiPassword(String miAccount, String miPassword);
}
