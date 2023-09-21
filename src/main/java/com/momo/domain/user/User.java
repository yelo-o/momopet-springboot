package com.momo.domain.user;

import com.momo.domain.BaseTimeEntity;
import com.momo.domain.member.Pet;
import com.momo.domain.member.PrivateInformation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; //ROLE_GUEST, ROLE_USER

    //=멤버 변수=//
    @Embedded
    private PrivateInformation privateInformation;

    @Builder
    public User(String name, String email, String picture, Role role, PrivateInformation privateInformation) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.privateInformation = privateInformation;
    }

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets = new ArrayList<>();

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

    public String getRoleKey() {
        return this.role.getKey();
    }

}
