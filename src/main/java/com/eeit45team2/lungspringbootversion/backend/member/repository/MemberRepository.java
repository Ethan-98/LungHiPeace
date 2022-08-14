package com.eeit45team2.lungspringbootversion.backend.member.repository;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<MemberBean, Long> {

    public Boolean existsByMiAccount(String miAccount);

    public MemberBean findByMiAccount(String miAccount);





}
