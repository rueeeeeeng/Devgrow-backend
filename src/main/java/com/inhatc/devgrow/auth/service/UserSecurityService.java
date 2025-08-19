package com.inhatc.devgrow.auth.service;

import com.inhatc.devgrow.auth.constant.Role;
import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;
    public CustomUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUser) {
            return (CustomUser) principal;
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("UserSecurityService=========>email 입니다. " + email);
        Member member = memberRepository.findByEmail(email);
        if (member == null) { //DB에 유저정보가 없으면 null 리턴
            return null;
        }
        Collection<? extends GrantedAuthority> authorities =
                getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(member.getRole().toString()))
                        .collect(Collectors.toList());
        //User 객체를 상속받는 CustomUser 객체 생성
        CustomUser customUser = new CustomUser(member.getEmail(), member.getPassword(), authorities, member.getNickname(), member.getId());

        return customUser;
    }

    public List<Role> getRoles() {
        List roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.USER);
        return roles;
    }
}