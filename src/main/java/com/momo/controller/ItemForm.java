package com.momo.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ItemForm {

    private Integer price;
    private String introduction;
    private String startDate;
    private String endDate;
    private Boolean dog;
    private Boolean cat;
    private String si;
    private String gu;
}
