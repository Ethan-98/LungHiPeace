package com.eeit45team2.lungspringbootversion.backend.activity.repository;

import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityApply;
import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityApply;
import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityApplyRepository extends JpaRepository<ActivityApply, Long> {
//    Optional<ActivityApply> findByOrderNo(String orderNo);

    List<ActivityApply> findAllByMemberBean(MemberBean memberBean);

//    Page<ActivityApply> findAllByOrderStatusAndMemberBean(ApplyStatus status, MemberBean memberBean, Pageable pageable);
}
