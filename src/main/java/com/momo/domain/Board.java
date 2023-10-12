package com.momo.domain;


import com.momo.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Board extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String title;
    private String content;
    private int views;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User sitter;

    @Column
    private String name;

    private String photo;


    @OneToMany(mappedBy = "board" , cascade = CascadeType.ALL)
    private List<Reply> replies;


}
