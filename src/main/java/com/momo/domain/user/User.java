package com.momo.domain.user;

import com.momo.domain.*;
import com.momo.domain.member.Pet;
import com.momo.domain.member.Point;
import com.momo.domain.member.PrivateInformation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.momo.domain.user.Role.USER;

@Getter @Setter
@NoArgsConstructor
@Table(name = "users") //H2 데이터베이스에서 "user"가 예약어로 지정되어 있어서 "users"로 변경
@Entity
public class User extends BaseTimeEntity  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String loginId;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    private UserType userType; //OWNER, SITTER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //ROLE_GUEST, ROLE_USER

    @OneToMany(mappedBy = "sitter")
    private List<Board> boards = new ArrayList<>();


    @Embedded
    private PrivateInformation privateInformation;

    @OneToMany(mappedBy = "sitter")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    @Column
    private Long balance; //잔액

    @OneToMany(mappedBy = "user")
    private List<Point> points = new ArrayList<>();
  
    @Builder
    public User(String name, String email, String picture, Role role, PrivateInformation privateInformation) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.privateInformation = privateInformation;
    }


    @OneToOne(mappedBy = "owner")
    private Pet pet;

    @Builder
    public User(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    //개인 정보 업데이트 메소드(개인정보 추가 -> 유저타입 SITTER로 변경)
    public User update(PrivateInformation privateInformation) {
        this.privateInformation = privateInformation;
        this.userType = UserType.SITTER;

        return this;
    }


    //유저 개인정보 업데이트 메소드
    public User updateInfo(PrivateInformation privateInformation) {
        this.privateInformation = privateInformation;

        return this;
    }

    public User upgrade() {
        this.userType = UserType.OWNER;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    // 충전 금액 증가 메소드
    public User pointUp(Long balance) {
        this.balance = balance;
        return this;
    }


}
