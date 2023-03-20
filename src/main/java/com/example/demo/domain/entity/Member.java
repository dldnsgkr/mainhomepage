package com.example.demo.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 100, nullable = false)
	private String memid;
	
	@Column(length = 100, nullable = false)
	private String mempw;
	
	@Column(length = 100, nullable = false)
	private String name;
	
	@Builder
	public Member(Long id, String memid, String mempw, String name) {
		this.id = id;
		this.memid = memid;
		this.mempw = mempw;
		this.name = name;
	}
}
