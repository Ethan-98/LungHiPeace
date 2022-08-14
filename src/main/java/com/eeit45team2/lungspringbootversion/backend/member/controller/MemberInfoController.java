package com.eeit45team2.lungspringbootversion.backend.member.controller;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.service.impl.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/memberInfo")
public class MemberInfoController {

    @Autowired
    CommonService commonService;

    @GetMapping("/checkUserLogin")
    public @ResponseBody String checkUserLogin(){
        String username = commonService.getCurrentUserMiNameString();
        if(username.equals("anonymousUser")){
            return "N"; //未登入
        }else{
            return "Y";
        }
//        return !username.equals("anonymousUser");
        //是anonymousUser -> 沒有登入 -> 顯示登入btn
        //有登入 -> 顯示會員中心btn & 登出btn
//        return "navbar::refreshNavbarUserButton"; /* html檔名::局部刷新的容器名字 */
    }


    /*取得當前登入的會員姓名*/
    @GetMapping("/getCurrentUserMiNameString")
    public @ResponseBody String getCurrentUserMiNameString(){
//        return commonService.getCurrentUserMiName(); // 呼叫CustomUserDetails.java取得那裡變數member的name
        if(commonService.getCurrentUserMiNameString().equals("anonymousUser")){
            return "";
        }
        return commonService.getCurrentUserMiNameString(); // 呼叫CustomUserDetails.java取得那裡變數member的name
    }

    /*取得當前登入的會員編號*/
    @GetMapping("/getCurrentUserMiGender")
    public @ResponseBody String getCurrentUserMiGender(){
        return commonService.getCurrentUserMiGender();
    }

    /*取得當前登入的會員編號*/
    @GetMapping("/getCurrentUserMiNo")
    public @ResponseBody Long getCurrentUserMiNo(){
        return commonService.getCurrentUserMiNo();
    }

    /*取得當前登入會員所有資訊*/
    @GetMapping("/getCurrentMemerBean")
    public @ResponseBody MemberBean getCurrentMemerBean(){
        return commonService.getCurrentMemerBean();
    }

    /*取得當前登入的會員大頭貼*/
    @GetMapping("/getCurrentUserImage")
    public ResponseEntity<byte[]> getCurrentUserImage(){
        return commonService.getCurrentUserImage();
    }





}
