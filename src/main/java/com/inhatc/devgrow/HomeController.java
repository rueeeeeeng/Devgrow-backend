package com.inhatc.devgrow;

import com.inhatc.devgrow.auth.dto.SessionMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@CrossOrigin
@Slf4j
public class HomeController {

    @GetMapping("/home")
    public SessionMember getUser() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        HttpSession httpSession = httpServletRequest.getSession();
        log.info("====>  현재 세션 : "+ httpSession.getId());
        SessionMember member = null;
        if(httpSession != null){
            log.info("====> HomeController : "+ httpSession.getId());
            member = (SessionMember) httpSession.getAttribute("member");
            log.info("====> HomeController member : "+ member.getNickname());
        }
        // ... (토큰 유효성 검증 및 사용자 정보 가져오기)

        // 3. 세션이 유효하면 사용자 정보 반환
//        if (sessionIsValid) {
//            return ResponseEntity.ok(user);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        return member;
    }

    @GetMapping("/mainLogout")
    public void logout() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = httpServletRequest.getSession();
        if (session != null) {
            session.invalidate();
            log.info("================================>세션 삭제");
        }
    }
}
