package com.example.test.service;

import com.example.test.domain.Member;
import com.example.test.domain.MemberRole;
import com.example.test.dto.MemberJoinDTO;
import com.example.test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void join(MemberJoinDTO memberJoinDTO)throws MidExistException{

        log.info("========================= MemberJoinDTO JOINING... =========================");

        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);
        if(exist){
           throw new MidExistException();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRoles(MemberRole.USER);

        log.info("============================== JOIN SUCCESS ==============================");
        log.info("======== Member ========");
        log.info(member);

        memberRepository.save(member);
    }
}
