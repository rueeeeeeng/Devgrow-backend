package com.inhatc.devgrow.auth.service;

import com.inhatc.devgrow.auth.constant.Role;
import com.inhatc.devgrow.auth.dto.OAuthAttributes;
import com.inhatc.devgrow.auth.dto.SessionMember;
import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Bean
    public RequestContextListener requestContextListener(){    return new RequestContextListener();}
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User           = super.loadUser(userRequest);
        System.out.println("===========================> loadUser : "+oAuth2User);
        String registrationId           = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName    = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes      = null;
        if("google".equals(registrationId)){
            attributes      = OAuthAttributes.googleMemberInfo(userNameAttributeName, oAuth2User.getAttributes());
        }

        System.out.println("===========================> loadUser name : "+ attributes.getName());
        System.out.println("===========================> loadUser email : "+ attributes.getEmail());
        System.out.println("===========================> loadUser email : "+ oAuth2User);


        Member member = saveOrUpdateMember(attributes, registrationId);

        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        httpSession.setAttribute("member", new SessionMember(member));


        log.info(">>>>>>httpSession httpSession : " + httpSession);
        log.info(">>>>>>httpSession httpSession : " + httpSession.getId());
        log.info(">>>>>>httpSession httpSession : " + httpSession.getAttribute("member"));
        SessionMember member1 = (SessionMember) httpSession.getAttribute("member");
        log.info(">>>>>>httpSession httpSession : " + member1.getNickname());
        // email을 통해 멤버를 찾으며 없을 경우  member 데이터 신규 저장
//        if( !memberRepository.existsByEmail(email) )
//        {
//            insertNewMember(email);
//        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.USER.toString());
        return new DefaultOAuth2User(
                Collections.singleton(authority),
                attributes.getAttributes(),
                userNameAttributeName);
    }

    private Member saveOrUpdateMember(OAuthAttributes attributes, String registrationId){
        Member member = memberRepository.findByEmail(attributes.getEmail());
        if(member != null) {
            System.out.println("===========================> loadUser email 존재O : "+ attributes.getEmail());
            member.memberUpdate(attributes.getName(), attributes.getPicture(), registrationId);
        }else{
            System.out.println("===========================> loadUser email 존재X : "+ attributes.getEmail());
            member = Member.builder()
                    .email(attributes.getEmail())
                    .nickname(attributes.getName())
                    .provider(registrationId)
                    .picture(attributes.getPicture())
                    .build();
        }
        return memberRepository.save(member);
    }
}