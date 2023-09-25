package com.momo.repository;

import com.momo.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
}
