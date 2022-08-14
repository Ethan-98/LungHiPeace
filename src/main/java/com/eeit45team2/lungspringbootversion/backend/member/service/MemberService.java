package com.eeit45team2.lungspringbootversion.backend.member.service;

import java.util.List;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;

public interface MemberService {
	
	//show all
	public List<MemberBean> findAll();
	
	//save
	public void save(MemberBean theMemberBean);

	//save no pwd encoding
	public void updateNoPwdEncoding(MemberBean theMemberBean);

	/* 專門for前台重設密碼使用 */
	public void save(MemberBean theMemberBean, String newPassword);
	
	//findById
	public MemberBean findById(Long miNo);
	
	//delete
	public void delete(Long miNo);

	public void testDelete(String miNo);

	public Boolean existsByMiAccount(String miAccount);

	public MemberBean findByMiAccount(String miAccount);



	public MemberBean saveHeadshotInDB(MemberBean memberBean, Boolean isInsert);
	
}
