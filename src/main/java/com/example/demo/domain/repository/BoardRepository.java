package com.example.demo.domain.repository;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.domain.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	List<Board> findByTitleContaining(String keyword);
	
	List<Board> findByContentContaining(String keyword);

	@Query(value = "select * from board b where b.title like %:keyword% or b.content like %:keyword%", nativeQuery = true)
	List<Board> findByPosts(@Param(value = "keyword" )String keyword);

	Page<Board> findByWhatpart(String share, PageRequest of);

	Long countByWhatpart(String whatpart);
	
	List<Board> findByTitleContainingAndWhatpartEquals(String keyword, String whatpart);

	@Transactional
	@Modifying
	@Query(value = "update board b set b.readcnt=b.readcnt+1 where b.id = :id", nativeQuery = true)
	void readcntup(@Param(value = "id") Long id);


}
