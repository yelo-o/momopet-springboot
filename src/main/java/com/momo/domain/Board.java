package com.momo.domain;


import lombok.Getter;
import lombok.Setter;

import com.momo.domain.member.Member;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter

public class Board {
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

    private String title;
    private String content;
    private LocalDateTime postingDate;

    private int view; //조회수

}
