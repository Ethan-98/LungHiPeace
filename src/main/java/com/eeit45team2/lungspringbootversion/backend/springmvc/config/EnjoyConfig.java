package com.eeit45team2.lungspringbootversion.backend.springmvc.config;

import com.jfinal.template.Engine;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnjoyConfig {

    @Bean
    public void enjoyConfigBefore(){
        ClassPathSourceFactory classPathSourceFactory = new ClassPathSourceFactory();
        Engine.create("memberMail")
                .setBaseTemplatePath("/templates/FrontEnd/member/")
                .setDatePattern("yyyy-MM-dd HH:mm:ss")
                .setSourceFactory(classPathSourceFactory);
    }
}
