package com.momo.controller;

import com.momo.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ItemForm {
    private int price;
    private String introduction;
    private String startDate;
    private String endDate;
    private Boolean dog;
    private Boolean cat;
    private String si;
    private String gu;
}
