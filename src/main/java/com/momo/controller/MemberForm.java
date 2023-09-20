package com.momo.controller;

import com.momo.domain.member.Address;
import com.momo.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MemberForm {

    private String pwd;

    private String name;
    private String email;
    private LocalDateTime birthDate;
    private Gender gender; //FEMALE, MALE
    private Address address;
    private String phoneNumber;

}
