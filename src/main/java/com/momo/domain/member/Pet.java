package com.momo.domain.member;

import com.momo.domain.BaseTimeEntity;
import com.momo.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Pet extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PetType petType; //강아지, 고양이

    @Enumerated(EnumType.STRING)
    private Gender gender; //수컷, 암컷

    private String breed; //품종
    private LocalDate birthDate;
    private String remark; //주의사항

    private String photo; //펫 사진 1장


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    public Pet() {
    }

    @Builder
    public Pet(Long id, String name, PetType petType, Gender gender, String breed,
               LocalDate birthDate, String remark, User owner, String photo) {
        this.id = id;
        this.name = name;
        this.petType = petType;
        this.gender = gender;
        this.breed = breed;
        this.birthDate = birthDate;
        this.remark = remark;
        this.owner = owner;
        this.photo = photo;
    }

    //펫 정보 업데이트 메소드
    public Pet update(String name, Gender gender, PetType petType,
                      String breed, LocalDate birthDate, String remark, String photo) {
        this.name = name;
        this.gender = gender;
        this.petType = petType;
        this.breed = breed;
        this.birthDate = birthDate;
        this.remark = remark;
        this.photo = photo;

        return this;
    }
}
