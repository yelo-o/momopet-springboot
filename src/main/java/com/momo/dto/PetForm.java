package com.momo.dto;

import com.momo.validation.ImageFileArgumentNotValidation;
import com.momo.validation.ValidFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class PetForm {

    @NotEmpty(message = "이름 입력은 필수입니다.")
    private String name;

    @NotEmpty(message = "성별 입력은 필수입니다.")
    private String gender;

    @NotEmpty(message = "품종을 입력해주세요.")
    private String breed;

    @NotEmpty(message = "펫 종류를 입력해주세요.")
    private String petType;

    @NotEmpty(message = "생년월일 입력은 필수입니다.")
    private String birthDate;

    @NotEmpty(message = "주의사항을 입력해주세요.")
    private String remark; //주의사항

    @ValidFile(message = "이미지 파일 첨부는 필수입니다.")
    private MultipartFile photo; //사진



    public void validate() throws ImageFileArgumentNotValidation {
        if(photo.isEmpty()){
            throw new ImageFileArgumentNotValidation("image","이미지 파일은 필수입니다.");
        }
    }

}
