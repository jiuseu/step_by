package com.example.test.repository.search;

import com.example.test.domain.Board;
import com.example.test.domain.QBoard;
import com.example.test.domain.QReply;
import com.example.test.dto.BoardListReplyCountDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

public class BoardSearchImpl extends Querydsl5RepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable){

        return applyPagination(pageable, query -> query.selectFrom(board)
                .where(board.bno.gt(0L)
                        .or(CheckTitle(types,keyword))
                        .or(CheckContent(types,keyword))
                        .or(CheckUser(types,keyword)))
        );
    }

    @Override
    public Page<BoardListReplyCountDTO> boardOfReply(String[] types, String keyword, Pageable pageable){

        return applyPagination(pageable, query -> query.select(Projections.bean(
                                BoardListReplyCountDTO.class,
                                board.bno,
                                board.title,
                                board.user,
                                board.regDate,
                                reply.count().as("replyCount")
                        )).from(board).leftJoin(reply).on(reply.board.eq(board))
                        .groupBy(board)
                .where(board.bno.gt(0L)
                        .or(CheckTitle(types,keyword))
                        .or(CheckContent(types,keyword))
                        .or(CheckUser(types,keyword)))
        );
    }

    private BooleanExpression CheckAll(String[] types, String keyword){

        if((types == null ||types.length == 0 ) && keyword == null){
            return board.bno.gt(0L);
        }else{
            return  board.bno.gt(0L)
                    .and(CheckTitle(types,keyword)
                            .or(CheckContent(types,keyword))
                            .or(CheckUser(types, keyword)));
        }
    }

    private BooleanExpression CheckTitle(String[] types, String keyword){
        if(types == null || keyword == null){
            return null;
        }else{
            return Arrays.asList(types).contains("t") == true ? board.title.contains(keyword) : null;
        }
    }

    private BooleanExpression CheckContent(String[] types, String keyword){
        if(types == null || keyword == null){
            return null;
        }else{
            return Arrays.asList(types).contains("c") == true ? board.content.contains(keyword) : null;
        }
    }

    private BooleanExpression CheckUser(String[] types, String keyword){
        if(types == null || keyword == null){
            return null;
        }else{
            return Arrays.asList(types).contains("u") == true ? board.user.contains(keyword) : null;
        }
    }
}
