package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Board;
import com.momo.domain.user.User;
import com.momo.service.BoardService;
import com.momo.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/board/write")     //localhost:9090/board/write
    public String boardWriteForm(Model model){
        model.addAttribute("form", new BoardForm());
        return "board/boardWrite";
    }

    @PostMapping("/board/write")
    public  String boardWritePro(@Valid BoardForm form, BindingResult bindingResult, Model model, @LoginUser SessionUser user) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("form", form);
            return "board/boardWrite";
        }

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        log.info("로그인 이메일" + user.getEmail());

        User findUser = memberService.findOne(user.getEmail());
        board.setSitter(findUser);
        board.setName(findUser.getName());

        //log.info("제목 가져오기" + form.getTitle());

        boardService.write(board);



        //게시글 작성 후 list페이지로 이동
        model.addAttribute("message", "글작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "board/message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model){

        model.addAttribute("list", boardService.boardList());

        return "board/boardList";
    }

    @GetMapping ("/board/view") //localhost:9090/board/view?id=1
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", boardService.boardView(id));
        return "board/boardView";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {
        boardService.boardDelete(id);

        model.addAttribute("message", "글이 삭제되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "board/message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model){

        model.addAttribute("board", boardService.boardView(id));

        return "board/boardModify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model) {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp);

        model.addAttribute("message", "작성글이 수정되었습니다.");
        model.addAttribute("searchUrl", "/board/list");


        return "board/message";
    }

}
