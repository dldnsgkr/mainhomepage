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
	private int age;
	private String address;
	private String e;
	private String mail;
	
	public Member toEntity() {
		Member build = Member.builder()
				.id(id)
				.memid(memid)
				.mempw(mempw)
				.name(name)
				.age(age)
				.address(address)
				.e(e)
				.mail(mail)
				.build();
		return build;
	}
	
	@Builder
	public MemberDTO(Long id, String memid, String mempw, String name,int age, String address, String e, String mail) {
		this.id = id;
		this.memid = memid;
		this.mempw = mempw;
		this.name = name;
		this.age = age;
		this.address = address;
		this.e = e;
		this.mail = mail;
		
	}
}
