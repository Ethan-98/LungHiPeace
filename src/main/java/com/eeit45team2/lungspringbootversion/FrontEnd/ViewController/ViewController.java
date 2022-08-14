package com.eeit45team2.lungspringbootversion.FrontEnd.ViewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/Front")
public class ViewController {

    @GetMapping
    public String index(HttpSession session) {
        session.setAttribute("test", "1234");
        return "FrontEnd/FrontIndex";
    }

    //測試用: 拿來看session id
    @RequestMapping("/security")
    public String security() {
        return "security";
    }

    @GetMapping("/animals")
    public String animals() {
        return "FrontEnd/Animal/animal";
    }

    @GetMapping("/errorpage403")
    public String errorpage403() {
        return "FrontEnd/common/403page";
    }

}


