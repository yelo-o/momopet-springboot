package com.momo.controller;


import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Board;
import com.momo.domain.Reply;
import com.momo.repository.BoardRepository;
import com.momo.repository.ReplyRepository;
import com.momo.service.BoardService;
import com.momo.service.ReplyService;
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
    private BoardService boardService;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyService replyService;


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


//    @GetMapping("/board/{id}/reply/delete/{replyId}")
//    public String deleteReply(@PathVariable Long id, @PathVariable Long replyId,
//                              @LoginUser SessionUser sessionUser, Model model) {
//        Board board = boardService.boardView(id);
//        Reply reply = replyService.getReplyById(replyId);
//
//        // 세션에 저장된 사용자 정보와 댓글 작성자의 이메일을 확인
//        if (sessionUser != null && sessionUser.getEmail().equals(board.getSitter().getEmail())) {
//            // 사용자 정보가 일치하면 댓글을 삭제한다.
//            replyService.deleteReply(replyId);
//
//            model.addAttribute("message", "댓글이 삭제되었습니다.");
//            model.addAttribute("searchUrl", "redirect:/board/{id}");
//        } else {
//            // 사용자 정보가 일치하지 않으면 권한이 없음을 응답한다.
//            model.addAttribute("message", "댓글 삭제 권한이 없습니다.");
//            model.addAttribute("searchUrl", "redirect:/board/{id}");
//        }
//        return "board/message";
//    }

    @PostMapping("/board/{id}/reply/delete/{replyId}")
    public String deleteReply1(@PathVariable Long id, @PathVariable Long replyId,
                              @LoginUser SessionUser sessionUser, Model model) {
        // 댓글 정보를 불러온다.
        Board board = boardService.boardView(id);
        Reply reply = replyService.getReplyById(replyId);

        // 세션에 저장된 사용자 정보와 댓글 작성자의 이메일이 같은지 확인
        if (sessionUser != null && sessionUser.getEmail().equals(board.getSitter().getEmail())) {
            // 사용자 정보가 일치하면 댓글을 삭제한다.
            replyService.deleteReply(replyId);

            model.addAttribute("message", "댓글이 삭제되었습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
        } else {
            // 사용자 정보가 일치하지 않으면 권한이 없음을 응답한다.
            model.addAttribute("message", "댓글 삭제 권한이 없습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
        }
        return "board/message";
    }

}
