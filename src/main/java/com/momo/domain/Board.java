package com.momo.domain;


import lombok.Getter;
import lombok.Setter;

import com.momo.domain.member.Member;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
<<<<<<< Updated upstream
public class Board {
=======
@Data
public class Board extends BaseTimeEntity{
>>>>>>> Stashed changes

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

<<<<<<< Updated upstream
    private String title;
    private String content;
    private LocalDateTime postingDate;
=======

    //private LocalDateTime postingDate;
>>>>>>> Stashed changes

    private int view; //조회수

}
