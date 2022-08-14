package com.eeit45team2.lungspringbootversion.backend.oauth;

import com.eeit45team2.lungspringbootversion.backend.member.model.CustomOAuth2User;
import com.eeit45team2.lungspringbootversion.backend.member.model.MemberBean;
import com.eeit45team2.lungspringbootversion.backend.member.repository.UserRepository;
import com.eeit45team2.lungspringbootversion.backend.member.service.MemberService;
import com.eeit45team2.lungspringbootversion.backend.member.service.impl.OAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

@Component
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberService memberService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException, IOException {
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> oauth2Client = oauth2User.getAttributes();
        String email = (String) oauth2Client.get("email");
        String name = (String) oauth2Client.get("name");
        String pictureUrl = (String) oauth2Client.get("picture");
        String userId = (String) oauth2Client.get("sub");

        if (userRepository.findByMiEmailIgnoreCase(email) == null) { // new user
            doInsertNewUser(userId, email, name, pictureUrl);
            downloadImg2Db(pictureUrl, email);
        } else { // old user, update information
            updateUser(email, pictureUrl, name);
        }

        String targetUrl = "/Front";

        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }
        redirectStrategy.sendRedirect(request, response, targetUrl);
        //userService.updateAuthenticationType(username, oauth2ClientName);
        //super.setDefaultTargetUrl("/Front");
        //super.onAuthenticationSuccess(request, response, authentication);
    }

    private void doInsertNewUser(String uid, String email, String name, String pictureUrl) {
        MemberBean memberBean = new MemberBean();
        memberBean.setMiAccount(uid);
        memberBean.setMiEmail(email);
        memberBean.setMiName(name);
        memberBean.setMiGoogleHeadshotUrl(pictureUrl);
        memberBean.setMiLoginType("Google");
        memberBean.setMiActive("Y");
        memberBean.setMiGender("不公開");
        memberBean.setMiRole("USER;ACTIVE");
        memberBean.setMiPassword("DefaultGooglePassword");  //不寫會錯
        memberBean.setLocalfileName("GoogleHeadshot.png"); //不寫會錯
        memberService.save(memberBean);
    }

    private void updateUser(String email, String pictureUrl, String name) { // update name and headshot
        MemberBean memberBean = userRepository.findByMiEmailIgnoreCase(email);
        if (!pictureUrl.equals(memberBean.getMiGoogleHeadshotUrl())) { // update image
            downloadImg2Db(pictureUrl, email);
        }
        if (!name.equals(memberBean.getMiName())) { // update image
            memberBean.setMiName(name);
            //TODO 處理密碼加密兩次的問題
            memberService.save(memberBean);
        }
    }

    private void downloadImg2Db(String pictureUrl, String email) {
        MemberBean memberBean = userRepository.findByMiEmailIgnoreCase(email);
        try {
            URL url = new URL(pictureUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] img = out.toByteArray();

            Blob blob = new SerialBlob(img);
            memberBean.setImage(blob);   //塞BLOB
            //TODO 處理密碼加密兩次的問題
            memberService.save(memberBean);
        } catch (IOException | SQLException e) {
            // handle IOException
            e.printStackTrace();
        }
    }

}
