package com.momo.domain;

import com.momo.domain.user.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.oauth2.core.oidc.StandardClaimAccessor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@Entity(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private String text;

    private String userName;        //추가내용

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;


}
