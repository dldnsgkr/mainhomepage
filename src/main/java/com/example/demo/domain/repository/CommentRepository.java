package com.example.demo.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	@Query(value = "select * from comment c where c.boardid = :boardid", nativeQuery = true)
	List<Comment> boardid(@Param(value = "boardid")Long id);

}
