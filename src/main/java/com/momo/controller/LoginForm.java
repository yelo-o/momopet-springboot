package com.momo.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class LoginForm {

    @NotEmpty(message = "아이디 입력은 필수입니다.")
    private String loginId;

    @NotEmpty(message = "패스워드 입력은 필수입니다.")
    private String pwd;

}
