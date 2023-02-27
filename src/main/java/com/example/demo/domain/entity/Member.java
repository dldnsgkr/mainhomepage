package com.example.demo.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
	
	@Column(length = 100, nullable = false)
	private int age;
	
	@Column(length = 1000, nullable = false)
	private String address;
	
	@Column(length = 100, nullable = false)
	private String e;
	
	@Column(length = 100, nullable = false)
	private String mail;
	
	@Builder
	public Member(Long id, String memid, String mempw, String name, int age, String address, String e, String mail) {
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
