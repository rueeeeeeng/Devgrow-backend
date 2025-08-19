package com.inhatc.devgrow.auth.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


/**
 * 기존 User 인터페이스를 확장하여 username과 email을 모두 포함하는 interface
 */
@Getter
public class CustomUser extends User {

    private final Long Id;
    private final String nickname;

    public CustomUser(String email, String password, Collection<? extends GrantedAuthority> authorities, String nickname, Long id) {
        super(email, password, authorities);
        this.Id = id;
        this.nickname = nickname;
    }
}
