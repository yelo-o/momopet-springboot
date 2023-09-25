package com.momo.domain;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import com.momo.domain.member.Member;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Board extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Integer id;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    //private LocalDateTime postingDate;

    private int view; //조회수

}
