package com.momo.domain;

import com.momo.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "items")
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User sitter;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String picture;

    @Column
    private String si;

    @Column
    private String gu;

    @Column(nullable = false)
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

}
