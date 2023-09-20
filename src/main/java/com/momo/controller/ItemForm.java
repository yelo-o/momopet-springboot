package com.momo.controller;

import com.momo.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ItemForm {
    private String nickname;
    private Gender gender;
    private String introduction;
    private int price;
    private LocalDateTime availableDate;
}