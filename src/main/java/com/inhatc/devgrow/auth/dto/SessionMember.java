package com.inhatc.devgrow.auth.dto;


import com.inhatc.devgrow.auth.entity.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable { // 직렬화 기능을 가진 세션 DTO

    // 인증된 사용자 정보만 필요 => name, email, picture 필드만 선언
    private String nickname;
    private String provider;
    private Long memberId;
    private String email;

    public SessionMember(Member member) {
        this.nickname = member.getNickname();
        this.memberId  = member.getId();
        this.provider = member.getProvider();
        this.email = member.getEmail();
    }
}
