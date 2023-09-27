package com.momo.controller;

import com.momo.domain.file.FileEntity;
import com.momo.dto.FileDto;
import com.momo.service.FileService;
import com.momo.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/api/upload")
    public String goToUpload() {
        return "/api/upload";
    }

    @PostMapping("/api/upload")
    public String uploadFile(FileDto fileDto) throws IOException {
        String url = s3Service.uploadFile(fileDto.getFile());

        fileDto.setUrl(url);
        fileService.save(fileDto);

        return "redirect:/api/list";
    }

    @GetMapping("/api/list")
    public String listPage(Model model) {
        List<FileEntity> fileList = fileService.getFiles();
        model.addAttribute("fileList", fileList);
        return "/api/list";
    }
}
