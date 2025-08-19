package com.inhatc.devgrow.auth.controller;

import com.inhatc.devgrow.auth.dto.MemberDto;
import com.inhatc.devgrow.auth.dto.SessionMember;
import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.service.CustomUser;
import com.inhatc.devgrow.auth.service.MemberService;
import com.inhatc.devgrow.auth.service.OAuth2UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class MemberController {


    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    private final OAuth2UserServiceImpl customOAuth2UserService;

    // 의존성 주입을 위한 생성자
    public MemberController(OAuth2UserServiceImpl customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @PostMapping( "/signUp")
    @ResponseBody
    public Map<String, Object> signUp(@RequestBody Map<String, Object> param){
        log.info("MemberController signUp Api Start");
        // Dto 초기화
        MemberDto memberDto = new MemberDto();

        // dto 변수 값 할당
        memberDto.setNickname(param.get("name").toString());
        memberDto.setEmail(param.get("email").toString());
        memberDto.setPassword(param.get("password").toString());

//        log.info(">>>>>>memberDto" + memberDto.getEmail());
//        log.info(">>>>>>memberDto" + memberDto.getName());
//        log.info(">>>>>>memberDto" + memberDto.getPassword());
        // member 클래스 빌드
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .password(memberDto.getPassword())
                .provider(null)
                .passwordEncoder(passwordEncoder)
                .build();
        // service 실행 부분
        Map<String, Object> message = memberService.saveMember(member);

        log.info("MemberController saveMember End");
        return message;

    }

    @PostMapping( "/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, Object> param, HttpServletRequest httpRequest){
        log.info("MemberController login Api Start");

        //프론트에서 입력한 정보
        String email = param.get("email").toString();
        String pw = param.get("password").toString();
        CustomUser userDetails = (CustomUser) userDetailsService.loadUserByUsername(email);

        log.info(">>>>>>memberDto email : " + userDetails.getUsername());
        log.info(">>>>>>memberDto memberID : " + userDetails.getId());
        log.info(">>>>>>memberDto nickname : " + userDetails.getNickname());
        log.info(">>>>>>memberDto password : " + userDetails.getPassword());
        log.info(">>>>>>memberDto role : " + userDetails.getAuthorities());

        Map<String, Object> member = memberService.validateLogin(userDetails, pw); //DB에서 해당 이메일로 회원가입 한 유저 있는지 확인


        MemberDto memberDto = (MemberDto) member.get("member"); //DB에서 가져온 memberDto 저장

        Member mem = Member.builder()
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .build();
        HttpSession session = httpRequest.getSession();
        session.setAttribute("member",new SessionMember(mem));

        SessionMember sessionMember = (SessionMember) session.getAttribute("member");
        log.info(">>>>>>memberDto login : " + sessionMember.getNickname());

//        System.out.println("=================>session : "+session.getId());
//        System.out.println("=================>session : "+session.getAttribute("email"));
        System.out.println(memberDto.getEmail());
        System.out.println(memberDto.getNickname());
        System.out.println("=================>member"+member);


//        log.info(">>>>>>memberDto" + memberDto.getEmail());
//        log.info(">>>>>>memberDto" + memberDto.getName());
//        log.info(">>>>>>memberDto" + memberDto.getPassword());
        // member 클래스 빌드
        log.info("MemberController login End");
        return member;

    }

    @GetMapping("/callback")
    public String getCrewGithubInfo(@RequestParam final String code) {
        log.info("===============<github : "+ code);
        return code;
    }

}