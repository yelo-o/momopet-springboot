package com.momo.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Embeddable
@Getter
public class PrivateInfo {
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private Address address;

    public PrivateInfo() {

    }

    public PrivateInfo(String name, String email, LocalDateTime birthDate, Gender gender, Address address, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }


}
