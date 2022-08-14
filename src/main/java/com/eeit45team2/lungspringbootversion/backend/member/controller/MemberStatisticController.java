package com.eeit45team2.lungspringbootversion.backend.member.controller;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/StatisticMember")
public class MemberStatisticController {

    @Autowired
    private MemberService memberService;


    @GetMapping("/CountAllGender")
    public @ResponseBody Map<String, Integer> CountAllGender() {
        List<MemberBean> memberBeans = memberService.findAll();
        int countFemale =0;
        int countMale =0;
        int countSecret =0;
        for (MemberBean memberBean : memberBeans) {
//            System.out.println(memberBean.getMiGender());
            if (memberBean.getMiGender().equals("女")) {
                countFemale++;
            } else if (memberBean.getMiGender().equals("男")) {
                countMale++;
            } else {
                countSecret++;
            }
//            System.out.println("countFemale: " + countFemale);
//            System.out.println("countMale: " + countMale);
//            System.out.println("countSecret: " + countSecret);
        }
        Map<String, Integer> conuntMemberGender = new HashMap<String, Integer>();
        conuntMemberGender.put("countFemale",countFemale);
        conuntMemberGender.put("countMale",countMale);
        conuntMemberGender.put("countSecret",countSecret);

        return conuntMemberGender;
    }




}
