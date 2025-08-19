package com.inhatc.devgrow.auth.service;

import com.inhatc.devgrow.auth.dto.MemberDto;
import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 
     * @param userDetails
     * @param pw
     * @return memberDto
     * 비밀번호 비교 및 로그인 완료 상태 여부 체크
     */
    public Map<String, Object> validateLogin(CustomUser userDetails, String pw) {
        MemberDto memberDto = new MemberDto();
        Map<String, Object> resultMap = new HashMap<>();
        log.info(">>>>>>memberService role : " + userDetails.getNickname());
        String message = "";
        if (userDetails != null) { //DB에 member의 정보가 있을 때
            boolean isPasswordMatch = passwordEncoder.matches(pw, userDetails.getPassword());
            if (!isPasswordMatch) { //내가 입력한 pw와 DB에서의 pw가 일치하지 않는 경우
                message = "비밀번호가 일치하지 않습니다.";
            } else { //DB에 member의 정보가 있고 pw가 일치하는 경우 -> 로그인 성공
                message = "로그인이 완료되었습니다.";
                memberDto.setMemberId(userDetails.getId());
                memberDto.setEmail(userDetails.getUsername());
                memberDto.setNickname(userDetails.getNickname());
                memberDto.setPassword(userDetails.getPassword());
            }
        } else {
            log.info("userDetail 없음");
            message = "사용자 정보가 존재하지 않습니다.";
        }
        resultMap.put("message", message);
        resultMap.put("member", memberDto);

        return resultMap;
    }

    public Map<String, Object> saveMember(Member member) {
        log.info("MemberService saveMember Start");
        var validate = validateDuplicatemember(member);
        var message = "";
        if (validate) { // validate true => 중복  이메일일 때
            System.out.println("중복 이메일");
            message = "중복된 이메일입니다.";

        } else { //validate false => 이메일 중복 아닐 때
            System.out.println("중복 이메일 아님");
            memberRepository.saveAndFlush(member);
            message = "회원가입이 완료되었습니다.";
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", message);
        resultMap.put("result", validate);
        log.info("MemberService saveMember End");
        return resultMap;
    }

    /**
     * @param member
     * @return false = 중복이 아닐 때 / true = 중복일 때
     */
    private boolean validateDuplicatemember(Member member) {
        boolean validate = false;
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) { //DB에 이미 있는 email -> 중복
            validate = true;
            //throw new IllegalStateException("이미 가입한 회원입니다.");
        }
        return validate;
    }

}
