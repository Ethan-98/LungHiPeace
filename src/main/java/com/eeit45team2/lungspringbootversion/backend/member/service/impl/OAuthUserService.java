package com.eeit45team2.lungspringbootversion.backend.member.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class OAuthUserService {
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        Resource.AuthenticationType authType = Resource.AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        //repo.updateAuthenticationType(username, authType);
    }
}
