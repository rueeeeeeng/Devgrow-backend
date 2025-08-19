package com.inhatc.devgrow.auth.handler;

import com.inhatc.devgrow.auth.dto.OAuthAttributes;
import com.inhatc.devgrow.auth.dto.SessionMember;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication
    ) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();// Authentication 객체로부터 권한 정보 get
//        String authorityString  = authorities.isEmpty() ? "" : authorities.iterator().next().getAuthority();// 권한 정보가 비어있지 않은 경우, 첫 번째 권한을 String으로 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = "";
        System.out.println("=============================================> getName : " + authentication.getName());
        System.out.println("=============================================> getPrincipal : " + authentication.getPrincipal());
        System.out.println("=============================================>" + response);
        String userId = authentication.getName();

        HttpSession session = request.getSession(false);
        /*session.setAttribute("userId", userId);*/
        if(session!= null ){
            SessionMember member = (SessionMember) session.getAttribute("member");
            System.out.println("=============================================>" + member.getNickname());
        }
        if ("google".equals(registrationId)) {
            email = OAuthAttributes.getGoogleEmail(oAuth2User.getAttributes());
        }
//        String accessToken  = tokenProvider.createAccessToken(email, authorityString);
//        tokenProvider.saveCookie(response,"accessToken"); // 응답에 토큰을 저장
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Authorization", "Bearer " + session);
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000");   // 로그인 성공 후 success 페이지 리디렉션
    }
}