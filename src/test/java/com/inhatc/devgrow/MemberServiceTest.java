package com.inhatc.devgrow;

import com.inhatc.devgrow.auth.entity.Member;
import com.inhatc.devgrow.auth.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    public Member createMember(){
        return Member.builder()
                .email("test@email.com")
                .username("박보명")
                .password("1234")
                .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = createMember();
        Member savedMember = memberService.saveMember(member);
        System.out.println("member"+member.getUsername());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getUsername(), savedMember.getUsername());
        assertEquals(member.getPassword(), savedMember.getPassword());
    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest(){
        Member member1 = createMember();
        Member member2 = createMember();
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);});
        assertEquals("이미 가입된 회원입니다.", e.getMessage());
    }
}