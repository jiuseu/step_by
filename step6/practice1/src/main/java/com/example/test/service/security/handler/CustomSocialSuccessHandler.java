package com.example.test.service.security.handler;

import com.example.test.dto.MemberJoinDTO;
import com.example.test.service.security.dto.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)throws IOException, ServletException{

        log.info("------------------------ on Authentication Success ------------------------");
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodePw = memberSecurityDTO.getMpw();

        if(memberSecurityDTO.isSocial() &&
                (memberSecurityDTO.getMpw().equals("1111")||
                        passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))){

            log.info("Should Change Password");
            log.info("Redirect to Member Modify");
            response.sendRedirect("/member/modify");

            return;
        }else{
            response.sendRedirect("/board/list");
        }

    }
}
