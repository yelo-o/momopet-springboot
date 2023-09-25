package com.momo.controller;

import com.momo.domain.Board;
import com.momo.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")     //localhost:9090/board/write
    public String boardWriteForm(){
        return "board/boardwrite";
    }

    @PostMapping("/board/writepro")
    public  String boardWritePro(Board board,
                                 @RequestParam("title") String title) {

        log.info("제목 가져오기" + title);

        //boardService.write(board);
        return "board/boardlist";
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

}
