package com.eeit45team2.lungspringbootversion.FrontEnd.member;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityApply;
import com.eeit45team2.lungspringbootversion.backend.activity.model.ActivityBean;
import com.eeit45team2.lungspringbootversion.backend.activity.service.ActivityApplyService;
import com.eeit45team2.lungspringbootversion.backend.member.model.ConfirmationToken;
import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.repository.ConfirmationTokenRepository;
import com.eeit45team2.lungspringbootversion.backend.member.repository.MemberRepository;
import com.eeit45team2.lungspringbootversion.backend.member.repository.UserRepository;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import com.eeit45team2.lungspringbootversion.backend.member.service.impl.CommonService;
import com.eeit45team2.lungspringbootversion.backend.member.service.impl.EmailSenderService;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.sql.rowset.serial.SerialBlob;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/FrontMember")
public class MemberControllerF {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CommonService commonService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ActivityApplyService activityApplyService;

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public ModelAndView displayRegistration(ModelAndView modelAndView, MemberBean member) {
        modelAndView.addObject("member", member);
        modelAndView.setViewName("/FrontEnd/register");
        return modelAndView;
    }


    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ModelAndView registerUser(ModelAndView modelAndView, MemberBean member) {
        Boolean isInsert = (member.getMiNo() ==null); // ???????????????insert -> ????????????????????????????????????????????????????????????
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        member.setMiPassword(passwordEncoder.encode(member.getMiPassword()));
        MemberBean memberBean1 = memberService.saveHeadshotInDB(member,isInsert);
        memberService.save(memberBean1);
//        userRepository.save(member);
        sendEmailToUser(memberBean1,memberBean1.getMiEmail());

        modelAndView.addObject("miEmail", memberBean1.getMiEmail());
        modelAndView.setViewName("/FrontEnd/registerconfirm");
        return modelAndView;
    }


    public void sendEmailToUser(MemberBean member, String email){
        ConfirmationToken confirmationToken = new ConfirmationToken(member);
        confirmationTokenRepository.save(confirmationToken);
        /* START??????????????? */
        Template template = Engine.use("memberMail").getTemplate("sendMailTemplate.html");
        Map<String, Object> data = new HashMap<>();
        data.put("title", "???????????? ??????Email?????????");
        data.put("toWho",member.getMiName());
        data.put("contentOne","????????????????????????????????????!");
        data.put("contentBefore", "?????????<a id=\"astyle\" href=\"" );
        data.put("link","http://localhost:8080/Lung/FrontMember/confirm-account?token=" + confirmationToken.getConfirmationToken());
        data.put("contentAfter", "\">??????</a>???????????????" );
        String html = template.renderToString(data);
        /* END??????????????? */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("LungHiPeace0302@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject("LungHi Peace???????????? Email?????????");
            messageHelper.setText(html, true);
            emailSenderService.htmlMail(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendEmailForResetPassword(MemberBean member, String email){
        ConfirmationToken confirmationToken = new ConfirmationToken(member);
        confirmationTokenRepository.save(confirmationToken); //?
        /* START??????????????? */
        Template template = Engine.use("memberMail").getTemplate("sendMailTemplate.html");
        Map<String, Object> data = new HashMap<>();
        data.put("title", "???????????? ?????????????????????");
        data.put("toWho",member.getMiName());
        data.put("contentOne","??????????????????????????????????????????!");
        data.put("contentBefore", "?????????<a id=\"astyle\" href=\"" );
        data.put("link","http://localhost:8080/Lung/FrontMember/resetPassword?email="
                + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&token="+ confirmationToken.getConfirmationToken());
        data.put("contentAfter", "\">??????</a>???????????????????????????" );
        String html = template.renderToString(data);
        /* END??????????????? */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("LungHiPeace0302@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject("LungHi Peace???????????? ???????????????");
            messageHelper.setText(html, true);
            emailSenderService.htmlMail(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if(token != null)
        {
            MemberBean member = userRepository.findByMiEmailIgnoreCase(token.getMember().getMiEmail());
            member.setMiActive("Y");
            member.setMiRole("USER;ACTIVE");
            userRepository.save(member);
            modelAndView.setViewName("/FrontEnd/registerVerifySuccess");
        }
        else
        {
            modelAndView.addObject("message","????????????????????????????????????");
            modelAndView.setViewName("/FrontEnd/registerVerifyFailed");
        }

        return modelAndView;
    }



    @PostMapping(value = "/CheckMemberEmail", produces = { "application/json" })
    public @ResponseBody Map<String, Boolean> existsByMiEmail(@RequestBody String res) {
        JSONObject object= JSON.parseObject(res);
        String emailToCheck = (String) object.get("emailToCheck");
        Long miNo;
        if(object.get("miNo") == null){
            miNo = null;
        } else {
            miNo = Long.parseLong((String) object.get("miNo"));
        }

        Map<String, Boolean> map = new HashMap<>();  // ????????????map
        MemberBean memberExisted = userRepository.findByMiEmailIgnoreCase(emailToCheck);
        boolean emailExisted;
        emailExisted = memberExisted != null;

        if(miNo ==null){  // ??????
            // email??????(true)????????? -> false , email?????????(false)????????? -> true
            map.put("emailCanUse", !emailExisted);
        }else{  // ??????
            MemberBean member = memberService.findById(miNo); // ???db?????????????????????email
            // user????????????email ->???user???????????????email ?????????db????????????email??????
            if(member.getMiEmail().equals(emailToCheck)){
                //??????????????????(?????????????????????)
                map.put("emailCanUse", true);
            }else {
                // user???????????????
                map.put("emailCanUse", !emailExisted);
            }
        }
        return map;
    }


    @RequestMapping(value="/forgetPassword", method = RequestMethod.GET)
    public String forgetPassword() {
        return "/FrontEnd/forgetPassword";
    }

    /* ????????????????????? */
    @RequestMapping(value="/forgetPassword", method = RequestMethod.POST)
    public ModelAndView forgetPassword(ModelAndView modelAndView, @RequestParam("inputEmail") String email){
        MemberBean member = userRepository.findByMiEmailIgnoreCase(email);
        sendEmailForResetPassword(member,email);
        modelAndView.addObject("miEmail", email);
        modelAndView.setViewName("/FrontEnd/registerconfirm");
        return modelAndView;
    }

    @RequestMapping(value="/resetPassword", method = RequestMethod.GET)
    public ModelAndView resetPassword(ModelAndView modelAndView, @RequestParam("email")String email, @RequestParam("token")String confirmationToken) {
        modelAndView.addObject("miEmail", email);
        modelAndView.setViewName("/FrontEnd/resetPassword");
        return modelAndView;
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST, produces = { "application/json" })
    public @ResponseBody Map<String, String> updatePassword(@RequestBody String req){
        JSONObject object= JSON.parseObject(req);
        String miEmail = (String) object.get("miEmail");
        String miPassword = (String) object.get("miPassword");
        HashMap<String, String> msg = new HashMap<>();

        try{
            MemberBean member = userRepository.findByMiEmailIgnoreCase(miEmail);
            //member.setMiPassword(miPassword);
            //??????service??????????????????
            memberService.save(member, miPassword); /* ????????????for???????????????????????? */
            msg.put("msg","success");
        }catch(Exception e){
            msg.put("msg","fail");
        }
        return msg;
    }

//    @RequestMapping(value="/resendEmailPage", method = RequestMethod.GET)
//    public String resendEmailPage() {
//        return "/FrontEnd/emailTokenExpired";
//    }

    /* ??????????????? ->  email token invalid*/
//    @RequestMapping(value="/resendEmail", method = RequestMethod.POST)
//    public ModelAndView resendEmail(ModelAndView modelAndView, @RequestBody String req){
//        JSONObject object= JSON.parseObject(req);
//        MemberBean member = (MemberBean) object.get("member");
//        String email = (String) object.get("email");
//        sendEmailToUser(member,email);
//        modelAndView.addObject("miEmail", member.getMiEmail());
//        modelAndView.setViewName("/FrontEnd/registerconfirm");
//        return modelAndView;
//    }

    /*??????????????????*/
    @GetMapping("/my-account-home")
    public String myAccountHome(Model model, Principal principal) {

        MemberBean memberBean = memberService.findByMiAccount(principal.getName());
        List<ActivityApply> ActivityBeans = activityApplyService.findAllByMember(memberBean);
        System.out.println(ActivityBeans);
        System.out.println(memberBean);

        model.addAttribute("activities", ActivityBeans);
        model.addAttribute("keyword", memberBean.getMiNo());
        return "FrontEnd/member/my-account"; }

    /* ???????????? */
    @GetMapping("/getMemberforUpdate")
    public @ResponseBody Map<String, String> getMemberforUpdate(){
        String miName = commonService.getCurrentMemerBean().getMiName();
        String miAccount = commonService.getCurrentMemerBean().getMiAccount();
        String miGender = commonService.getCurrentMemerBean().getMiGender();
        Date miBirth =commonService.getCurrentMemerBean().getMiBirth();
        String miId = commonService.getCurrentMemerBean().getMiId();
        String miPhone =commonService.getCurrentMemerBean().getMiPhone();
        String miEmail = commonService.getCurrentMemerBean().getMiEmail();
        String miCity = commonService.getCurrentMemerBean().getMiCity();
        String miDistrict =commonService.getCurrentMemerBean().getMiDistrict();
        String miAddress = commonService.getCurrentMemerBean().getMiAddress();

        Map<String, String> member = new HashMap<String, String>();
        member.put("miName",miName);
        member.put("miAccount",miAccount);
        member.put("miGender",miGender);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        member.put("miBirth", f.format(new java.util.Date(miBirth.getTime())));
        member.put("miId",miId);
        member.put("miPhone",miPhone);
        member.put("miEmail",miEmail);
        member.put("miCity",miCity);
        member.put("miDistrict",miDistrict);
        member.put("miAddress",miAddress);

        return member;
    }

    /* ???????????????????????? */
    @PostMapping(value = "/saveMemberforUpdate", produces = { "application/json" })
    public @ResponseBody Map<String, String> saveMemberforUpdate(@RequestBody String member){
        JSONObject object= JSON.parseObject(member);
        MemberBean saveMember = commonService.getCurrentMemerBean();
        saveMember.setMiName((String) object.get("miName"));
        saveMember.setMiAccount((String) object.get("miAccount"));
        saveMember.setMiGender((String) object.get("miGender"));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String sMiBirth = (String) object.get("miBirth");
        Date miBirth;
        try {
            java.util.Date tmp = f.parse(sMiBirth);
            miBirth = new Date(tmp.getTime());
            saveMember.setMiBirth(miBirth);
        } catch (Exception ex) {
            // ??????????????????
        }
        saveMember.setMiId((String) object.get("miId"));
        saveMember.setMiPhone((String) object.get("miPhone"));
        saveMember.setMiEmail((String) object.get("miEmail"));
        saveMember.setMiCity((String) object.get("miCity"));
        saveMember.setMiDistrict((String) object.get("miDistrict"));
        saveMember.setMiAddress((String) object.get("miAddress"));
        memberService.updateNoPwdEncoding(saveMember); //?????????????????????????????????

        HashMap<String, String> msg = new HashMap<>();
        msg.put("success","success");
        return msg;
    }

    @PostMapping(value = "/saveHeadshotforUpdate", produces = { "application/json" })
    public @ResponseBody Map<String, String> saveHeadshotforUpdate(@RequestParam("headshot") MultipartFile picture){
        MemberBean memberBean = commonService.getCurrentMemerBean();
        // setImage (??????Blob??????????????? Hibernate ???????????????)
        if (picture != null && !picture.isEmpty()) {
            // ?????????????????????
            try {
                byte[] b = picture.getBytes();
                Blob blob = new SerialBlob(b);
                memberBean.setImage(blob);   //???BLOB
                memberBean.setLocalfileName(System.currentTimeMillis() + "_" + picture.getOriginalFilename()); // ??????????????????
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("????????????????????????: " + e.getMessage());
            }
        }else {
            // ????????????????????????
                try {
                    memberBean.setImage(memberBean.getImage());  // ???DB???????????????
                    memberBean.setLocalfileName(memberBean.getLocalfileName());  // ???DB???????????????
                    System.out.println(" > setImaget ??????");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("????????? ????????????????????????: " + e.getMessage());
                }
        }
        memberService.updateNoPwdEncoding(memberBean);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("success","success");
        return msg;
    }


    /* ???????????????????????????db???????????? */
    @PostMapping(value = "/checkPassword", produces = { "application/json" })
    public @ResponseBody Map<String, String> checkPassword(@RequestBody String inputpwd){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JSONObject object= JSON.parseObject(inputpwd);
        String passwordToCheck = (String) object.get("passwordToCheck");

        String currentMemberPassword = commonService.getCurrentMemerBean().getMiPassword();
//        Long currentMiNo = commonService.getCurrentMemerBean().getMiNo();

        Map<String, String> msg = new HashMap<>();  // ????????????map

        System.out.println();
        if(passwordEncoder.matches(passwordToCheck, currentMemberPassword)){
            msg.put("checkPasswordResult","success");
        }else{
            msg.put("checkPasswordResult","fail");
        }
        return msg;
    }


    /* ???????????? */
    @PostMapping(value = "/savePasswordforUpdate", produces = { "application/json" })
    public @ResponseBody Map<String, String> savePasswordforUpdate(@RequestBody String pwd){
        JSONObject object= JSON.parseObject(pwd);
        MemberBean savePassword = commonService.getCurrentMemerBean();
        savePassword.setMiPassword(((String) object.get("newPassword")));
        //??????service??????????????????
        memberService.save(savePassword);

        HashMap<String, String> msg = new HashMap<>();
        msg.put("success","success");
        return msg;
    }


    @PostMapping(value = "/CheckMemberAccount", produces = { "application/json" })
    public @ResponseBody Map<String, Boolean> existsByMiAccount(@RequestBody String res) {
        JSONObject object= JSON.parseObject(res);
        String miAccount = (String) object.get("accountToCheck");
        Long miNo;
        if(object.get("miNo") == null){
            miNo = null;
        } else {
            miNo = Long.parseLong((String) object.get("miNo"));
        }

        Map<String, Boolean> map = new HashMap<>();  // ????????????map
        Boolean accountExisted = memberService.existsByMiAccount(miAccount);

        if(miNo ==null){  // ?????? -> ??????db???????????????????????????
            // ????????????(true)????????? -> false , ???????????????(false)????????? -> true
            map.put("accountCanUse", !accountExisted);
        }else{  // ??????
            MemberBean member = memberService.findById(miNo); // ???db???????????????????????????
            // user?????????????????? ->???user????????????????????? ?????????db????????????????????????
            if(member.getMiAccount().equals(miAccount)){
                //??????????????????(?????????????????????)
                map.put("accountCanUse", true);
            }else {
                // user??????????????? -> ????????????????????????
                map.put("accountCanUse", !accountExisted);
            }
        }
        return map;
    }

}
