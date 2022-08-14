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
        Boolean isInsert = (member.getMiNo() ==null); // 判斷是否為insert -> 因為是註冊，所以基本上就是新增，不是修改
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
        /* START塞值到模板 */
        Template template = Engine.use("memberMail").getTemplate("sendMailTemplate.html");
        Map<String, Object> data = new HashMap<>();
        data.put("title", "浪孩和平 會員Email認證信");
        data.put("toWho",member.getMiName());
        data.put("contentOne","我們已經收到您的註冊申請!");
        data.put("contentBefore", "請點選<a id=\"astyle\" href=\"" );
        data.put("link","http://localhost:8080/Lung/FrontMember/confirm-account?token=" + confirmationToken.getConfirmationToken());
        data.put("contentAfter", "\">連結</a>進行認證！" );
        String html = template.renderToString(data);
        /* END塞值到模板 */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("LungHiPeace0302@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject("LungHi Peace浪孩和平 Email認證信");
            messageHelper.setText(html, true);
            emailSenderService.htmlMail(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendEmailForResetPassword(MemberBean member, String email){
        ConfirmationToken confirmationToken = new ConfirmationToken(member);
        confirmationTokenRepository.save(confirmationToken); //?
        /* START塞值到模板 */
        Template template = Engine.use("memberMail").getTemplate("sendMailTemplate.html");
        Map<String, Object> data = new HashMap<>();
        data.put("title", "浪孩和平 會員忘記密碼信");
        data.put("toWho",member.getMiName());
        data.put("contentOne","我們已經收到您的重設密碼申請!");
        data.put("contentBefore", "請點選<a id=\"astyle\" href=\"" );
        data.put("link","http://localhost:8080/Lung/FrontMember/resetPassword?email="
                + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&token="+ confirmationToken.getConfirmationToken());
        data.put("contentAfter", "\">連結</a>重新設定您的密碼！" );
        String html = template.renderToString(data);
        /* END塞值到模板 */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("LungHiPeace0302@gmail.com");
            messageHelper.setTo(email);
            messageHelper.setSubject("LungHi Peace浪孩和平 重設密碼信");
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
            modelAndView.addObject("message","連結已失效，請重新驗證。");
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

        Map<String, Boolean> map = new HashMap<>();  // 塞訊息的map
        MemberBean memberExisted = userRepository.findByMiEmailIgnoreCase(emailToCheck);
        boolean emailExisted;
        emailExisted = memberExisted != null;

        if(miNo ==null){  // 新增
            // email存在(true)的相反 -> false , email不存在(false)的相反 -> true
            map.put("emailCanUse", !emailExisted);
        }else{  // 修改
            MemberBean member = memberService.findById(miNo); // 從db抓這筆會員的舊email
            // user沒有修改email ->　user現在輸入的email 是否和db中自己的email一樣
            if(member.getMiEmail().equals(emailToCheck)){
                //不塞錯誤訊息(不提示帳號重複)
                map.put("emailCanUse", true);
            }else {
                // user有修改帳號
                map.put("emailCanUse", !emailExisted);
            }
        }
        return map;
    }


    @RequestMapping(value="/forgetPassword", method = RequestMethod.GET)
    public String forgetPassword() {
        return "/FrontEnd/forgetPassword";
    }

    /* 忘記密碼的接口 */
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
            //丟進service再做密碼加密
            memberService.save(member, miPassword); /* 呼叫專門for前台重設密碼用的 */
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

    /* 重寄驗證信 ->  email token invalid*/
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

    /*會員中心頁面*/
    @GetMapping("/my-account-home")
    public String myAccountHome(Model model, Principal principal) {

        MemberBean memberBean = memberService.findByMiAccount(principal.getName());
        List<ActivityApply> ActivityBeans = activityApplyService.findAllByMember(memberBean);
        System.out.println(ActivityBeans);
        System.out.println(memberBean);

        model.addAttribute("activities", ActivityBeans);
        model.addAttribute("keyword", memberBean.getMiNo());
        return "FrontEnd/member/my-account"; }

    /* 修改會員 */
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

    /* 修改會員基本資料 */
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
            // 用原本生日改
        }
        saveMember.setMiId((String) object.get("miId"));
        saveMember.setMiPhone((String) object.get("miPhone"));
        saveMember.setMiEmail((String) object.get("miEmail"));
        saveMember.setMiCity((String) object.get("miCity"));
        saveMember.setMiDistrict((String) object.get("miDistrict"));
        saveMember.setMiAddress((String) object.get("miAddress"));
        memberService.updateNoPwdEncoding(saveMember); //呼叫修改沒有密碼加密版

        HashMap<String, String> msg = new HashMap<>();
        msg.put("success","success");
        return msg;
    }

    @PostMapping(value = "/saveHeadshotforUpdate", produces = { "application/json" })
    public @ResponseBody Map<String, String> saveHeadshotforUpdate(@RequestParam("headshot") MultipartFile picture){
        MemberBean memberBean = commonService.getCurrentMemerBean();
        // setImage (建立Blob物件，交由 Hibernate 寫入資料庫)
        if (picture != null && !picture.isEmpty()) {
            // 如果有上傳照片
            try {
                byte[] b = picture.getBytes();
                Blob blob = new SerialBlob(b);
                memberBean.setImage(blob);   //塞BLOB
                memberBean.setLocalfileName(System.currentTimeMillis() + "_" + picture.getOriginalFilename()); // 暫時先這樣寫
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("檔案上傳發生異常: " + e.getMessage());
            }
        }else {
            // 如果沒有上傳照片
                try {
                    memberBean.setImage(memberBean.getImage());  // 找DB中的舊照片
                    memberBean.setLocalfileName(memberBean.getLocalfileName());  // 找DB中的舊檔名
                    System.out.println(" > setImaget 成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("修改時 檔案上傳發生異常: " + e.getMessage());
                }
        }
        memberService.updateNoPwdEncoding(memberBean);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("success","success");
        return msg;
    }


    /* 確認密碼是否和自己db中的一樣 */
    @PostMapping(value = "/checkPassword", produces = { "application/json" })
    public @ResponseBody Map<String, String> checkPassword(@RequestBody String inputpwd){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        JSONObject object= JSON.parseObject(inputpwd);
        String passwordToCheck = (String) object.get("passwordToCheck");

        String currentMemberPassword = commonService.getCurrentMemerBean().getMiPassword();
//        Long currentMiNo = commonService.getCurrentMemerBean().getMiNo();

        Map<String, String> msg = new HashMap<>();  // 塞訊息的map

        System.out.println();
        if(passwordEncoder.matches(passwordToCheck, currentMemberPassword)){
            msg.put("checkPasswordResult","success");
        }else{
            msg.put("checkPasswordResult","fail");
        }
        return msg;
    }


    /* 修改密碼 */
    @PostMapping(value = "/savePasswordforUpdate", produces = { "application/json" })
    public @ResponseBody Map<String, String> savePasswordforUpdate(@RequestBody String pwd){
        JSONObject object= JSON.parseObject(pwd);
        MemberBean savePassword = commonService.getCurrentMemerBean();
        savePassword.setMiPassword(((String) object.get("newPassword")));
        //丟進service再做密碼加密
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

        Map<String, Boolean> map = new HashMap<>();  // 塞訊息的map
        Boolean accountExisted = memberService.existsByMiAccount(miAccount);

        if(miNo ==null){  // 新增 -> 判斷db中是否有重複的帳號
            // 帳號存在(true)的相反 -> false , 帳號不存在(false)的相反 -> true
            map.put("accountCanUse", !accountExisted);
        }else{  // 修改
            MemberBean member = memberService.findById(miNo); // 從db抓這筆會員的舊帳號
            // user沒有修改帳號 ->　user現在輸入的帳號 是否和db中自己的帳號一樣
            if(member.getMiAccount().equals(miAccount)){
                //不塞錯誤訊息(不提示帳號重複)
                map.put("accountCanUse", true);
            }else {
                // user有修改帳號 -> 判斷帳號是否重複
                map.put("accountCanUse", !accountExisted);
            }
        }
        return map;
    }

}
