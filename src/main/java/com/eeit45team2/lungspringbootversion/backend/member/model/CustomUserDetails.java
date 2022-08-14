package com.eeit45team2.lungspringbootversion.backend.member.model;


import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;

public class CustomUserDetails implements UserDetails{

//    在 UserDetailsServiceImpl 注入memberbean的值

    private static final long serialVersionUID = 1L;

    private MemberBean member;

    public CustomUserDetails(MemberBean member) {
        this.member = member;
    }

    public CustomUserDetails() {
    }

    @Override
    public String getPassword() {
        return this.member.getMiPassword();
    }

    @Override
    public String getUsername() {
        return this.member.getMiAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
//		return this.user.isAccountNonExpired();
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
//		return this.user.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//		return this.user.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//		return this.user.isEnabled();
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//		return AuthorityUtils.createAuthorityList(user.getRole());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String[] var2 = member.getMiRole().split(";");
        for (String authority : var2) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
        }
        return grantedAuthorities;
    }

    public MemberBean getUser() {
        return member;
    }

    public void setUser(MemberBean user) {
        this.member = user;
    }

    public String getMiname() {
        return this.member.getMiName();
    }

    public Long getMino() {
        return this.member.getMiNo();
    }

    public String getMigender() {
        return this.member.getMiGender();
    }

    public String getMiemail() {
        return this.member.getMiEmail();
    }

}

