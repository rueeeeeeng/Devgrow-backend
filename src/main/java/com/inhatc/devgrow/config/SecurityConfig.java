package com.inhatc.devgrow.config;

import com.inhatc.devgrow.auth.handler.CustomAuthenticationSuccessHandler;
import com.inhatc.devgrow.auth.service.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final OAuth2UserServiceImpl oAuth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpsecurity) throws Exception {

        httpsecurity.csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login((oauth2) -> oauth2
                        .defaultSuccessUrl("http://localhost:3000") // 로그인 성공 시 이동할 페이지
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(oAuth2UserService)));
//        httpsecurity.formLogin(login -> login
////                       // .loginPage("/login")
////                       // .loginProcessingUrl("/login")
////                        .defaultSuccessUrl("/")
////                        .usernameParameter("email"));
//                .logout(logout -> logout
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                        .logoutSuccessUrl("/"));

//        httpsecurity.authorizeHttpRequests(authorize -> authorize.requestMatchers("/post").authenticated());

        httpsecurity.authorizeRequests()
                .requestMatchers("/login", "/postList", "/signUp", "/post/**", "/postDetail","/home","/logout", "githubLogin").permitAll()
                .requestMatchers("/myPage/**").permitAll()
                .requestMatchers("C:/**").permitAll()
                .anyRequest().authenticated();

        httpsecurity.sessionManagement((httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)));


        return httpsecurity.build();
    }

}
