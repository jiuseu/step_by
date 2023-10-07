package com.example.test.repository.search;

import com.example.test.domain.Board;
import com.example.test.domain.QBoard;
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
