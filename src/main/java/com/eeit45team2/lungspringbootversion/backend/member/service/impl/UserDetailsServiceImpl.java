package com.eeit45team2.lungspringbootversion.backend.member.service.impl;

import com.eeit45team2.lungspringbootversion.backend.member.model.CustomUserDetails;
import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private MemberService memberService;

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String miAccount)
            throws UsernameNotFoundException {
        MemberBean member = memberService.findByMiAccount(miAccount); //去DB查詢匹配的帳號，回傳一個User物件
//        System.out.println("用帳號查詢member資訊 : "+member);
        // 判斷是否有從DB找到匹配的用戶
        // 沒有匹配帳號
        if(Objects.isNull(member)){
            System.out.println("查無此用戶，驗證失敗");
            throw new UsernameNotFoundException("查無此用戶，驗證失敗");
        }
        // 有匹配帳號
        // 把從DB查到的用戶數據封裝成UserDetails(含用戶訊息與授權訊息)，然後往前端傳
        // ，會在DaoAuthenticationProvider這個實作類別，通過PasswordEncoder，把UserDetails中的密碼跟前端傳來的密碼比對
        // 密碼驗證成功的話 -> 就會把原本UserDetails中的全縣設置到Authentication物件中，Authentication被繼續往前端傳(含用戶訊息與授權訊息)
        return new CustomUserDetails(member);
                //member.getMiAccount(), member.getMiPassword(), getGrantedAuthorities(member));
    }


    private List<GrantedAuthority> getGrantedAuthorities(MemberBean member){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : member.getMiRole().split(";")) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        System.out.print("權限角色 :"+authorities.toString());
        return authorities;
    }

}