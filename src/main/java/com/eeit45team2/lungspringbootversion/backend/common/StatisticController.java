package com.eeit45team2.lungspringbootversion.backend.common;

import com.eeit45team2.lungspringbootversion.backend.animal.model.AbDogBean;
import com.eeit45team2.lungspringbootversion.backend.animal.service.AbDogService;
import com.eeit45team2.lungspringbootversion.backend.announce.model.AnnounceBean;
import com.eeit45team2.lungspringbootversion.backend.announce.service.AnnounceService;
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
@RequestMapping("/BackendStatistic")
public class StatisticController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AnnounceService announceService;

    private AbDogService abDogService;

    @GetMapping("/SummaryAll")
    public @ResponseBody Map<String, Integer> SummaryAll() {
        //第一步
        List<MemberBean> memberBeans = memberService.findAll();
        List<AnnounceBean> announceBeans = announceService.findAll();
//        List<AbDogBean> abDogBeans = abDogService.abdoglistAll("keyword");

        //第二步
        int sumMember =0;
        int sumAnnounce =0;
//        int sumAnimal =0;

        //第三步
        for (MemberBean memberBean : memberBeans) {
            sumMember++;
        }
        for (AnnounceBean announceBean : announceBeans) {
            sumAnnounce++;
        }
//        for (AbDogBean abDogBean : abDogBeans) {
//            sumAnimal++;
//        }
        // 第四步
        Map<String, Integer> conuntAll = new HashMap<String, Integer>();
        conuntAll.put("sumMember",sumMember);
        conuntAll.put("sumAnnounce",sumAnnounce);
//        conuntAll.put("sumAnimal",sumAnimal);


        return conuntAll;
    }


}
