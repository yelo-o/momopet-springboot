package com.momo.domain;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member owner;
    @Enumerated(EnumType.STRING)
    private PetType type;
    private String name;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String breed;
    private LocalDateTime birthDate;
    private String remark;
}
