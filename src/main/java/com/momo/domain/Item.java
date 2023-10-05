package com.momo.domain;

import com.momo.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User sitter;

    @Column
    private int price; // 하루 일당

    @Column
    private String introduction; //시터의 자기소개

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    @Column
    private Boolean dog;

    @Column
    private Boolean cat;

    @Enumerated(EnumType.STRING)
    private Status status; //활성화, 비활성화


}
