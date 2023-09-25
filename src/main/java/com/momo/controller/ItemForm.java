package com.momo.controller;

import com.momo.domain.member.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemForm {
    private String nickname;
    private Gender gender;
    private String introduction;
    private Integer price;
    private String startDate;
    private String endDate;
}
