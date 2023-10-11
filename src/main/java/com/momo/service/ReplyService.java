package com.momo.service;

import com.momo.domain.Board;
import com.momo.domain.Reply;
import com.momo.domain.user.User;
import com.momo.repository.BoardRepository;
import com.momo.repository.ReplyRepository;
import com.momo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {


    private final ReplyRepository replyRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;


}
