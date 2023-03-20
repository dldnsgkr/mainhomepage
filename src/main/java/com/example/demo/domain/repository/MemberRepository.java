package com.example.demo.domain.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.DTO.MemberDTO;
import com.example.demo.domain.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{

	@Query(value = "select * from member m where m.memid = :memid and m.mempw = :mempw", nativeQuery = true)
	Optional<Member> login(@Param(value = "memid")String memberid,
					@Param(value = "mempw") String memberpassword);

	@Query(value = "select count(*) from member m where m.memid = :memid", nativeQuery = true)
	int checkmemid(@Param(value = "memid")String memberid);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE member m SET m.age = :age + 1 WHERE m.age is not null", nativeQuery = true)
	void updateage();


}
