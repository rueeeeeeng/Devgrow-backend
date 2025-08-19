package com.inhatc.devgrow.auth.entity;

import com.inhatc.devgrow.auth.constant.Role;
import com.inhatc.devgrow.auth.dto.MemberDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자 만들어줌
//@DynamicUpdate //update 할때 실제 값이 변경됨 컬럼으로만 update 쿼리를 만듬
@Entity //JPA Entity 임을 명시
@Getter //Lombok 어노테이션으로 getter
@Table(name = "member") //테이블 관련 설정 어노테이션
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "provider")
    private String provider;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String picture;     //프로필사진

    @Column
    private String info;     //한줄 소개
    
    @Builder //생성을 Builder 패턴으로 하기 위해서
    public Member(String nickname , String password, String email, String provider,String picture, String info,PasswordEncoder passwordEncoder) {
        System.out.println(passwordEncoder);
        System.out.println(password);
        if(passwordEncoder != null && password != null){
            password = passwordEncoder.encode(password);
            this.password = password;
        }
        this.nickname = nickname;
        this.email = email;
        this.role = Role.USER;
        this.picture = picture;
        this.provider = provider;
        this.info = info;
    }


    public Member memberUpdate(String nickname, String picture, String provider) {
        this.nickname = nickname;
        this.picture  = picture;
        this.provider = provider;
        return this;
    }

    public Member updateInfo(MemberDto dto){
        if(dto.getNickname()!= null)
            this.nickname = dto.getNickname();
        if(dto.getInfo()!= null)
            this.info = dto.getInfo();
        return this;
    }

}