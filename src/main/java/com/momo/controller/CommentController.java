package com.momo.controller;

import com.momo.domain.Board;
import com.momo.domain.Comment;
import com.momo.repository.CommentRepository;
import com.momo.service.BoardService;
import com.momo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/comment", method = RequestMethod.POST)
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BoardService boardService;

    @GetMapping("/save")
    @PostMapping(value = "/save",
            consumes = "application/son",
            produces = { MediaType.TEXT_PLAIN_VALUE})
    public @ResponseBody ResponseEntity<List<Comment>> saveComment(@RequestParam("commentWriter") String commentWriter,
                                                                   @RequestParam("commentContents") String commentContents,
                                                                   @RequestParam("board_id") Long boardId,
                                                                   @RequestParam(value="pbNum", required=false) String pbNum) {


        Board board = boardService.findById(boardId);
//        System.out.println("Comment = " + comment);
//        commentService.save(comment);

        if (board != null) {
            Comment comment = new Comment();
            comment.setCommentWriter(commentWriter);
            comment.setCommentContents(commentContents);
            comment.setBoard(board);

            commentService.save(comment); // 댓글 저장

            List<Comment> commentList = board.getComments(); // 게시글에 달린 댓글 목록을 가져옴
            return ResponseEntity.ok(commentList); // 저장된 댓글 목록을 응답으로 반환
        }else {
            // 게시글을 찾지 못한 경우 에러 응답을 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
