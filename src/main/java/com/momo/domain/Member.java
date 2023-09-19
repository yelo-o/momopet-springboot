package com.momo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String loginId;

    private String pwd;

    @Enumerated(EnumType.STRING)
    private MemberType membertype;

    @Embedded
    private PrivateInfo privateInfo;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "sitter")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Board> boards = new ArrayList<>();

}
