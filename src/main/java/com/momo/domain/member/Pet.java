package com.momo.domain.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Pet {

    @Id @GeneratedValue
    @Column(name = "pet_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PetType petType; //강아지, 고양이

    private Gender gender; //수컷, 암컷
    private String breed; //품종

    private LocalDateTime birthDate;
    private String remark; //주의사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;

}
