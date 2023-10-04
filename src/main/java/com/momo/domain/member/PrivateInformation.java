package com.momo.domain.member;

import com.momo.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Embeddable
@Getter @Setter
public class PrivateInformation {
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender; //FEMALE, MALE

    private Address address;
    private String phoneNumber;

    //public 대신에 protected 사용(JPA 스펙에서 허용가능 범위)
    protected PrivateInformation() {
    }

    //값 타입은 기본적으로 변경이 불가능하게 설계되어야 한다. -> 생성할 때만 값이 셋팅이 되고 setter는 제공하지 말아야 한다.
    public PrivateInformation(LocalDate birthDate, Address address, String phoneNumber, Gender gender) {
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;

    }
}
