package com.momo.domain;


import com.momo.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
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

    //private LocalDateTime postingDate;

    private int view; //조회수

    private String photo;


    // 게시글과 댓글을 양방향 관계 설정
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBoard(this);
    }

}
