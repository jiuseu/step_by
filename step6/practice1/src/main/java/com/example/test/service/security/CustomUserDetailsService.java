package com.example.test.service.security;

import com.example.test.domain.Member;
import com.example.test.repository.MemberRepository;
import com.example.test.service.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{

        log.info("loadUserByUsername: "+username);

        Optional<Member> result = memberRepository.getWithRoles(username);
        Member member = result.get();
        MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority(
                        "ROLE_"+memberRole.name()
                )).collect(Collectors.toList())
        );

        log.info("===================== LoadUserByUsername MemberSecurityDTO =====================");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }
}
