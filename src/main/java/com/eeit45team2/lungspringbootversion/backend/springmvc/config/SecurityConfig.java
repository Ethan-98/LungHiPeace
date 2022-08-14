package com.eeit45team2.lungspringbootversion.backend.springmvc.config;

import com.eeit45team2.lungspringbootversion.backend.member.service.impl.CustomOAuth2UserService;
import com.eeit45team2.lungspringbootversion.backend.oauth.OAuthLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private CustomOAuth2UserService oauth2UserService;

    @Autowired
    private OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()  //開始自訂授權規則
//                .anyRequest().permitAll()  // TODO 第一次登入前都要先打開這個!! 然後建一個會員，才會拿到帳號和加密後的密碼!!
                // TODO 開啟anyRequest()後，註解下面所有的antMatchers()
                .antMatchers(HttpMethod.GET,"/loginPage**","/BackEnd/css/**","/BackEnd/images/**","/BackEnd/js/**").permitAll()  //不限授權皆可進到登入
                .antMatchers(HttpMethod.GET,"/FrontEnd/assets/**").permitAll()
                .antMatchers(HttpMethod.GET,"/image/**","/image3/**","/image4/**").permitAll()
                .antMatchers(HttpMethod.GET,"/Front/**","/FrontEndAnimalF/animals","Frontendactivity/activities","/Front/products","/announcelist2","/viewForm/**").permitAll() // 前台誰都可以進
                .antMatchers(HttpMethod.GET, "/Back").hasAuthority("EMPLOYEE") //有EMPLOYEE權限以上的角色才能進到後台頁面
                .antMatchers(HttpMethod.GET, "/Backendmember/CheckMemberAccount","/Backendmember/picture/**").permitAll()
                .antMatchers(HttpMethod.GET, "/Backendmember/delete/**","/Backendmember/updateForm/**").hasAuthority("ADMIN") //有ADMIN權限才能修改、刪除會員
                .antMatchers(HttpMethod.GET, "/Backendmember/**","/Backendactivity/**","/Backendanimal/**").hasAuthority("EMPLOYEE") //有EMPLOYEE權限以上的角色才能進到會員頁面
                .antMatchers(HttpMethod.GET,"/memberInfo/checkUserLogin").permitAll()
                .antMatchers(HttpMethod.GET,"/memberInfo/getCurrentUserImage").permitAll()
                .antMatchers(HttpMethod.GET,"/memberInfo/getCurrentUserMiNameString").permitAll()
                .antMatchers(HttpMethod.GET,"/FrontMember/**").permitAll()
                .antMatchers(HttpMethod.GET,"/Frontendactivity/CheckOutApply/**").hasAuthority("ACTIVE") // 要認證才能報名志工活動
                .antMatchers(HttpMethod.GET,"/Frontendactivity/**").permitAll()
                .antMatchers(HttpMethod.GET,"/FrontEndAnimalF/animalForm/**").hasAuthority("ACTIVE") // 要認證才能填寫認養表單
                .antMatchers(HttpMethod.POST,"/Front/**").permitAll()
                .antMatchers(HttpMethod.POST,"/FrontMember/register").permitAll()
                .antMatchers(HttpMethod.POST,"/FrontMember/CheckMemberEmail").permitAll()
                .antMatchers(HttpMethod.POST,"/FrontMember/forgetPassword").permitAll()
                .antMatchers(HttpMethod.POST,"/FrontMember/**").permitAll()
                .anyRequest().authenticated()  //其他請求，都要經過驗證
                // TODO 以上都註解掉
                .and()
                //.oauth2Login().loginPage("/loginPage").defaultSuccessUrl("/default", true).failureUrl("/loginPage-error")
                    .oauth2Login().loginPage("/loginPage")
                    .userInfoEndpoint()
                    .userService(oauth2UserService)
                .and()
                    .successHandler(oauthLoginSuccessHandler)
                .and()
                    .formLogin().loginPage("/loginPage").failureUrl("/loginPage-error").defaultSuccessUrl("/default", true)
                .and()
                    .exceptionHandling().accessDeniedPage("/Front/errorpage403") //如果有權限錯誤時，重導「/403頁面」
                .and()
                    .logout().logoutSuccessUrl("/loginPage")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable(); //關閉對 CSRF（跨站請求偽造）攻擊的防護。這樣 Security 機制才不會拒絕外部直接對 API 發出的請求
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder()); //登入時輸入的密碼便會被加密，與資料庫中使用者的已加密密碼進行比對
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("admin").password("password").roles("ADMIN");
    }




}
