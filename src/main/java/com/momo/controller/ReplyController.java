package com.momo.controller;


import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Board;
import com.momo.domain.Reply;
import com.momo.repository.BoardRepository;
import com.momo.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReplyController {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;


    @GetMapping("/board/{id}/reply")
    public String getBoard(@PathVariable Long id, Model model) {
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            List<Reply> replies = board.getReplies(); // 댓글 목록을 받아옵니다.
            model.addAttribute("board", board);
            model.addAttribute("replies", replies); // 댓글 목록을 모델에 추가합니다.
        }
        return "board";
    }

    @PostMapping("/board/{id}/reply")
    public String addReply(@PathVariable Long id, @RequestParam String text, @LoginUser SessionUser user, Model model) {

        // 로그인되어 있지 않으면 댓글 작성 권한이 없다고 처리
        if (user == null) {
            model.addAttribute("message", "로그인 하고 오세요");
            model.addAttribute("searchUrl", "/board/list");
            return "board/message";
        }

        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            Reply reply = new Reply();
            reply.setText(text);
            reply.setBoard(board);
            replyRepository.save(reply);
        }
        return "redirect:/board/{id}";
    }

}
