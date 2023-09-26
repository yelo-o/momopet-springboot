package com.momo.controller;

import com.momo.domain.Board;
import com.momo.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")     //localhost:9090/board/write
    public String boardWriteForm(Model model, BoardForm form){
        model.addAttribute("form", form);
        return "board/boardwrite";
    }

    @PostMapping("/board/write")
    public  String boardWritePro(BoardForm form, Model model) {

        Board board = new Board();
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        //log.info("제목 가져오기" + form.getTitle());

        boardService.write(board);

        model.addAttribute("message", "글작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "board/message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model){

        model.addAttribute("list", boardService.boardList());

        return "board/boardlist";
    }

    @GetMapping ("/board/view") //localhost:9090/board/view?id=1
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", boardService.boardView(id));
        return "board/boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model){

        model.addAttribute("board", boardService.boardView(id));

        return "board/boardmodify";
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
