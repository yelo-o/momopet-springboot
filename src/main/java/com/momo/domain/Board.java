package com.momo.domain;


import com.momo.domain.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @JoinColumn(name = "user_id")
    private User sitter;

    @Column
    private String name;

    //private LocalDateTime postingDate;

    private int view; //조회수

}
