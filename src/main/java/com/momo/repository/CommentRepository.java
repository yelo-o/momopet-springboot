package com.momo.repository;

import com.momo.domain.Board;
import com.momo.domain.Comment;
import com.momo.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
