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

    @Id @GeneratedValue
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;


    public Pet() {
    }

    @Builder
    public Pet(Long id, String name, PetType petType, Gender gender, String breed,
               LocalDate birthDate, String remark, User owner) {
        this.id = id;
        this.name = name;
        this.petType = petType;
        this.gender = gender;
        this.breed = breed;
        this.birthDate = birthDate;
        this.remark = remark;
        this.owner = owner;
    }

    //펫 정보 업데이트 메소드
    public Pet update(String name, Gender gender, PetType petType,
                      String breed, LocalDate birthDate, String remark) {
        this.name = name;
        this.gender = gender;
        this.petType = petType;
        this.breed = breed;
        this.birthDate = birthDate;
        this.remark = remark;

        return this;
    }
}
