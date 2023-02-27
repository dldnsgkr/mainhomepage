package com.example.demo.DTO;


import java.time.LocalDateTime;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class MemberDTO {

	private Long id;
	private String memid;
	private String mempw;
	private String name;
	
	public Member toEntity() {
		Member build = Member.builder()
				.id(id)
				.memid(memid)
				.mempw(mempw)
				.name(name)
				.build();
		return build;
	}
	
	@Builder
	public MemberDTO(Long id, String memid, String mempw, String name) {
		this.id = id;
		this.memid = memid;
		this.mempw = mempw;
		this.name = name;
		
	}
}
