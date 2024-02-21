package com.example.test.service.security;

import com.example.test.domain.Member;
import com.example.test.domain.MemberRole;
import com.example.test.repository.MemberRepository;
import com.example.test.service.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomDefaultOAuth2Service extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)throws OAuth2AuthenticationException{

        log.info("---------------------------- OAuth2 load User..----------------------------");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        String email = null;

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String,Object> paramMap = oAuth2User.getAttributes();

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("======================================");
        log.info(email);
        log.info("======================================");

        return generateDTO(email, paramMap);
    }

    private MemberSecurityDTO generateDTO(String email,Map<String,Object>param){

        Optional<Member> result = memberRepository.findByEmail(email);
        if(result.isEmpty()){
           Member member = Member.builder()
                   .mid(email)
                   .mpw(passwordEncoder.encode("1111"))
                   .email(email)
                   .social(true)
                   .build();
           member.addRoles(MemberRole.USER);
           memberRepository.save(member);

           MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(email,"1111",email,false,true,
                   Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
           memberSecurityDTO.setProps(param);

           return memberSecurityDTO;
        }else{
          Member member = result.get();
          MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
               member.getMid(),
               member.getMpw(),
               member.getEmail(),
               member.isDel(),
               member.isSocial(),
               member.getRoleSet().stream().map(memberRole ->
                       new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toList())
          );

          return memberSecurityDTO;
        }
    }
    public String getKakaoEmail(Map<String,Object>paramMap){

        log.info("------------------ KAKAO ------------------");

        Object value = paramMap.get("kakao_account");
        LinkedHashMap linkedHashMap = (LinkedHashMap) value;
        String email = (String)linkedHashMap.get("email");

        log.info("KAKAO Email : "+email);
        return email;
    }
}
