package com.inhatc.devgrow.auth.dto;

import com.inhatc.devgrow.auth.constant.Role;
import com.inhatc.devgrow.auth.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto implements Serializable {
    private Long memberId;
    private String nickname;
    private String email;
    private String password;
    private String provider;
    private Role role;

    private String info;
    @Builder //생성을 Builder 패턴으로 하기 위해서
    public MemberDto(String nickname , String password, String email, String provider, String info ,PasswordEncoder passwordEncoder) {
        this.nickname = nickname;
        password = passwordEncoder.encode(password);
        this.password = password;
        this.email = email;
        this.role = Role.USER;
        this.provider = provider;
        this.info = info;
    }

    public MemberDto(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.role = Role.USER;
        this.provider = member.getProvider();
    }

    public MemberDto memberDtoUpdate(String nickname, String email, String provider) {
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
        return this;
    }

    public MemberDto MemberDtoUpdate(Member member){
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.role = Role.USER;
        this.provider = member.getProvider();

        return this;

    }

}
