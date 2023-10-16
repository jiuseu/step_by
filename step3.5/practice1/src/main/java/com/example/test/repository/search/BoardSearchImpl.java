package com.example.test.repository.search;

import com.example.test.domain.Board;
import com.example.test.domain.QBoard;
import com.example.test.domain.QReply;
import com.example.test.dto.BoardOfReplyDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable){

        JPQLQuery query = from(board)
                .where(CheckTitle(types,keyword),
                        CheckContent(types,keyword),
                        CheckUser(types, keyword),
                        board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable,query);
        List<Board> list = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<Board>(list,pageable,count);
    }

    @Override
    public Page<BoardOfReplyDTO> searchBoardOfReply(String[] types, String keyword, Pageable pageable){

        JPQLQuery<Board> query = from(board)
                .leftJoin(reply).on(reply.board.eq(board))
                .groupBy(board)
                .where(CheckAll(types, keyword));

        JPQLQuery<BoardOfReplyDTO> dtoQuery = query.select(
                Projections.bean(BoardOfReplyDTO.class,
                        board.bno,
                        board.title,
                        board.user,
                        board.regDate,
                        reply.count().as("replyCount")));

        this.getQuerydsl().applyPagination(pageable,dtoQuery);
        List<BoardOfReplyDTO> list = dtoQuery.fetch();
        Long count = dtoQuery.fetchCount();

        return new PageImpl<>(list,pageable,count);
    }

    private BooleanExpression CheckAll(String[] types, String keyword){
        if(types == null && keyword == null){
            return board.bno.gt(0L);
        }else{
            return board.bno.gt(0L)
                    .and(CheckTitle(types,keyword)
                            .or(CheckContent(types,keyword))
                            .or(CheckUser(types,keyword)));
        }
    }

    private BooleanExpression CheckTitle(String[] types, String keyword){
        if(types == null){
            return null;
        }else{
            return Arrays.asList(types).contains("t") == true ? board.title.contains(keyword) : null;
        }
    }

    private BooleanExpression CheckContent(String[] types, String keyword){
        if(types == null){
            return null;
        }else{
            return Arrays.asList(types).contains("c") == true ? board.content.contains(keyword) : null;
        }
    }

    private BooleanExpression CheckUser(String[] types, String keyword){
        if(types == null){
            return null;
        }else{
            return Arrays.asList(types).contains("u") == true ? board.user.contains(keyword) : null;
        }
    }
}
