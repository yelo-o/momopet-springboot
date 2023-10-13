package com.momo.service;

import com.momo.domain.Reply;
import com.momo.repository.BoardRepository;
import com.momo.repository.ReplyRepository;
import com.momo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {


    private final ReplyRepository replyRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;


    // 댓글 아이디로 댓글 조회 메서드
    public Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId).orElse(null);
    }


    // 댓글 삭제 메서드
    public void deleteReply(Long replyId) {
        // 댓글을 찾아서 삭제
        replyRepository.deleteById(replyId);
    }


}

