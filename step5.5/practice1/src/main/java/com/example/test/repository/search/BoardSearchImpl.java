package com.example.test.repository.search;

import com.example.test.domain.Board;


import com.example.test.domain.QBoard;
import com.example.test.domain.QBoardImage;
import com.example.test.domain.QReply;
import com.example.test.dto.BoardImageDTO;
import com.example.test.dto.BoardListAllDTO;
import com.example.test.dto.BoardListReplyCountDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.hibernate.query.criteria.JpaExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.list;


public class BoardSearchImpl extends Querydsl5RepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;

    QBoardImage boardImage = QBoardImage.boardImage;

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

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable){

        JPQLQuery<Board> query = from(board)
                                 .where(board.bno.gt(0L)
                                 .and(allCond(types,keyword)))
                                 .leftJoin(reply).on(reply.board.eq(board)).groupBy(board);
        getQuerydsl().applyPagination(pageable, query);

        List<Tuple> tupleQuery = query.select(board, reply.countDistinct()).fetch();
        List<BoardListAllDTO> dtoList = tupleQuery.stream().map(tuple ->{
                    Board board1 = (Board)tuple.get(board);
                    Long replyCount = tuple.get(1,Long.class);

                    BoardListAllDTO boardListAllDTO = BoardListAllDTO.builder()
                            .bno(board1.getBno())
                            .title(board1.getTitle())
                            .user(board1.getUser())
                            .regDate(board1.getRegDate())
                            .replyCount(replyCount)
                            .build();

                    List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                            .map(boardImage -> BoardImageDTO.builder()
                                    .uuid(boardImage.getUuid())
                                    .fileName(boardImage.getFileName())
                                    .ord(boardImage.getOrd())
                                    .build()).collect(Collectors.toList());

                    boardListAllDTO.setBoardImage(imageDTOS);

                    return boardListAllDTO;
                }).collect(Collectors.toList());

        Long count = query.fetchCount();
        return new PageImpl<>(dtoList,pageable,count);

    }

    // BooleanBuilder
    private BooleanBuilder allCond(String[] types, String keyword) {
        BooleanBuilder builder = new BooleanBuilder();
       if((types != null && types.length > 0) && keyword != null ){
           return builder
                   .or(CheckTitle(types,keyword))
                   .or(CheckContent(types,keyword))
                   .or(CheckUser(types,keyword));
       }
       return null;
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

        return Arrays.asList(types).contains("t") == true ? board.title.contains(keyword) : null;

    }

    private BooleanExpression CheckContent(String[] types, String keyword){

            return Arrays.asList(types).contains("c") == true ? board.content.contains(keyword) : null;

    }

    private BooleanExpression CheckUser(String[] types, String keyword){

            return Arrays.asList(types).contains("u") == true ? board.user.contains(keyword) : null;

    }
}
