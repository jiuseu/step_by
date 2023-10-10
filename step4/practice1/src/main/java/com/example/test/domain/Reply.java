package com.example.test.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Table(name = "Reply", indexes = {@Index(name="idx_reply_board_bno", columnList = "board_bno")})
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyText;

    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public void ChangeReplyText(String replyText){
        this.replyText = replyText;
    }
}
