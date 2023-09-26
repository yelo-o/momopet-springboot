package com.momo.domain.user;

import com.momo.domain.BaseTimeEntity;
import com.momo.domain.Board;
import com.momo.domain.Item;
import com.momo.domain.member.Pet;
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
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    private UserType userType; //OWNER, SITTER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //ROLE_GUEST, ROLE_USER

    @OneToMany(mappedBy = "sitter")
    private List<Board> boards = new ArrayList<>();


    //=멤버 변수=//
    @Embedded
    private PrivateInformation privateInformation;

    @OneToMany(mappedBy = "sitter")
    private List<Item> items = new ArrayList<>();
  
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


    //=멤버 변수=//
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

    public User upgrade() {
        this.userType = UserType.OWNER;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

}
