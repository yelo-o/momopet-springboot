package com.momo.service;

import com.momo.domain.Board;
import com.momo.domain.Comment;
import com.momo.repository.BoardRepository;
import com.momo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;


    public void save(Comment comment) {
        commentRepository.save(comment);
    }



}
