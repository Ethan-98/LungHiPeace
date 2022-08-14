package com.eeit45team2.lungspringbootversion.backend.member.controller;

import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/outputmembertojson")
    public String outputJson(Model model) {
        List<MemberBean> memberBeans = memberService.findAll();

        List<Map<String, String>> listAllMember = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 內層的Map -> {欄位名: 欄位值}
        for (MemberBean member : memberBeans) {
            Map<String, String> aRow = new HashMap<>();
            aRow.put("會員編號", member.getMiNo().toString());
            aRow.put("會員姓名", member.getMiName());
            aRow.put("會員帳號", member.getMiAccount());
            aRow.put("會員密碼", member.getMiPassword());
            aRow.put("會員權限", member.getMiRole());
            aRow.put("會員身分證", member.getMiId());
            aRow.put("會員生日", dateFormat.format(member.getMiBirth()));
            aRow.put("會員電話", member.getMiPhone());
            aRow.put("會員Email", member.getMiEmail());
            aRow.put("會員地址", member.getMiAddress());
            listAllMember.add(aRow);
        }

        JSONObject outerjson = new JSONObject();  //json的key值
        int counter = 1;
        for (Map<String, String> aRow : listAllMember) {
            //new JSONObject(mapMember),把map放進去產生json物件,json型態很像map
            JSONObject rowjson = new JSONObject(aRow);  //把一列資料變成json object
            counter += 1;  //放完一筆(row)就記一次，代表json資料的序號
            outerjson.put(counter, rowjson);  //outerjson內放的是(序號, 一列會員資料)
        }

        try {
            Files.write(Paths.get(System.currentTimeMillis() + "_" + "OutputMemberJson"), outerjson.toJSONString().getBytes());
            System.out.println("output Json success");   //toJSONString()會自動補反斜線(跳脫字元)所以把反斜線替換成空字串
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "Backendmember/outputSuccess";
    }


    public void outputCsv() {

    }
}



