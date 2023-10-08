package com.momo.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class BoardForm {

    @NotEmpty(message = "제목을 입력하세요.")
    private String title;

    @NotEmpty(message = "내용을 입력하세요.")
    private String content;

    private MultipartFile photo; //사진

}
