package com.example.test.service;

import com.example.test.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception{
    }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException;
}
