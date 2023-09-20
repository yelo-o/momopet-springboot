package com.momo.domain;

import com.momo.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member sitter;

<<<<<<< Updated upstream
    private int price;
=======
    private String nickname;

    private int price; // 하루 일당

>>>>>>> Stashed changes
    private LocalDateTime availableDate;
    private String introduction;

    private String introduction; //시터의 자기소개

}
