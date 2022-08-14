package com.eeit45team2.lungspringbootversion.backend.member.repository;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<MemberBean, String> {
    MemberBean findByMiEmailIgnoreCase(String miEmail);
}
