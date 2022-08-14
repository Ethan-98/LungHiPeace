package com.eeit45team2.lungspringbootversion.login.repository;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<MemberBean, Long> {

	public MemberBean findByMiAccountAndMiPassword(String miAccount, String miPassword);

}
