package com.momo.domain.member;

import com.momo.domain.Board;
import com.momo.domain.Item;
import com.momo.domain.Order;
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

    private String loginId; //로그인 아이디
    private String pwd; //로그인 패스워드
    private String introduce; //회원가입

    private PrivateInformation privateInformation;

    @Enumerated(EnumType.STRING)
    private MemberType memberType; //멤버 타입 [OWNER, SITTER]

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "sitter")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Board> boards = new ArrayList<>();

}
