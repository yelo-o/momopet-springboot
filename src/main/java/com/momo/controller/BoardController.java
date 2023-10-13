package com.momo.controller;

import com.momo.config.auth.LoginUser;
import com.momo.config.auth.dto.SessionUser;
import com.momo.domain.Board;
import com.momo.domain.user.User;
import com.momo.repository.BoardRepository;
import com.momo.service.BoardService;
import com.momo.service.MemberService;
import com.momo.service.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private final S3Service s3Service;
    @Autowired
    private final BoardRepository boardRepository;

    public BoardController(S3Service s3Service, BoardRepository boardRepository) {
        this.s3Service = s3Service;
        this.boardRepository = boardRepository;
    }

    private LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }


    @GetMapping("/board/write")     //localhost:9090/board/write
    public String boardWriteForm(Model model, @LoginUser SessionUser user){

        if(user == null) {
            model.addAttribute("message", "로그인 하십시오");
            model.addAttribute("searchUrl", "/board/list");
            return "board/message";
        } else {
            model.addAttribute("form", new BoardForm());
            return "board/boardWrite";
        }
    }

    @PostMapping("/board/write")
    public  String boardWritePro(@ModelAttribute("form") @Valid BoardForm form, BindingResult bindingResult,
                                 Model model, @LoginUser SessionUser user) throws IOException {


        if(bindingResult.hasErrors()) {
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

        if (form.getPhoto()!=null) {
            //사진 url 받기
            String photo = s3Service.uploadFile(form.getPhoto());
            board.setPhoto(photo);
        }

        boardService.write(board);



        //게시글 작성 후 list페이지로 이동
        model.addAttribute("message", "글작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "board/message";
    }

    //게시글리스트 불러오기 + 페이징처리
    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page = 0, size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        Page<Board> list = boardService.boardList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;      //0부터 시작이기때문에 1을 더해줘야 1부터 시작되게 된다.
        int startPage = Math.max(nowPage - 4, 1);   //1보다 작게 나오면 1이 나오게 한다.
        int endPage= Math.min(nowPage + 5, list.getTotalPages());


        model.addAttribute("list", list);
        model.addAttribute("nowPage",nowPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "board/boardList";

    }

    @GetMapping ("/board/view") //localhost:9090/board/view?id=1
    public String boardView(Model model, Long id) {
        model.addAttribute("board", boardService.boardView(id));
        return "board/boardView";
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam Long id, @LoginUser SessionUser sessionUser, Model model) {
        // 요청된 id에 해당하는 게시글을 DB에서 불러온다.
        Board board = boardService.boardView(id);

        // 세션에 저장된 사용자 정보와 게시글 작성자의 이메일이 같은지 확인
        if (sessionUser != null && sessionUser.getEmail().equals(board.getSitter().getEmail())) {
            // 사용자 정보가 일치하면 게시글을 삭제한다.
            boardService.boardDelete(id);

            model.addAttribute("message", "글이 삭제되었습니다.");
            model.addAttribute("searchUrl", "/board/list");
        } else {
            // 사용자 정보가 일치하지 않으면 권한이 없음을 응답한다.
            model.addAttribute("message", "글 삭제 권한이 없습니다.");
            model.addAttribute("searchUrl", "/board/view?id=" + id);
        }

        return "board/message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Long id, Model model){

        model.addAttribute("board", boardService.boardView(id));

        return "board/boardModify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Long id, Board board, Model model, @LoginUser SessionUser sessionUser) {

        Board boardTemp = boardService.boardView(id);

        if(sessionUser != null && sessionUser.getEmail().equals(boardTemp.getSitter().getEmail())) {
            boardTemp.setTitle(board.getTitle());
            boardTemp.setContent(board.getContent());

            boardService.write(boardTemp);

            model.addAttribute("message", "작성글이 수정되었습니다.");
            model.addAttribute("searchUrl", "/board/{id}");
        } else  {
            model.addAttribute("message", "글 수정 권한이 없습니다.");
            model.addAttribute("searchUrl", "/board/list");
        }


        return "board/message";
    }

    //조회수 증가 조회수 증가 조회수 증가 조회수 증가 조회수 증가 조회수 증가 조회수 증가 조회수 증가
    @GetMapping("/board/{id}")
    public String getBoard(@PathVariable("id") Long id, Model model) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if(optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            board.setViews(board.getViews() + 1);   //조회수 증가
            boardRepository.save(board);            //변경된 조회수를 저장
            model.addAttribute("board", board);
        }
        return "board/boardView";
    }

}
