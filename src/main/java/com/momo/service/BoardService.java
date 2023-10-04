package com.momo.service;

import com.momo.domain.Board;
import com.momo.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    //게시글 작성 처리
    public void write(Board board) {
        boardRepository.save(board);
    }

    //게시글 리스트 처리
    public List<Board> boardList(){
        return boardRepository.findAll();
    }

    //특정 게시글 불러오기
    public Board boardView(Long id){
        return boardRepository.findById(id).get();
    }

    //게시글 삭제
    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }

    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }


}
