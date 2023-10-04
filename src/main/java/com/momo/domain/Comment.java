package com.momo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter @Setter
public class Comment extends BaseTimeEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentWriter;
    private String commentContents;
    //private Long board_id;   //게시물번호
    //private Timestamp commentCreatedTime;



    // 댓글과 게시글의 양방향 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


}