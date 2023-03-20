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
public class Comment extends TimeEntity{

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 10, nullable = false)
	private String writer;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	@Column(length = 255, nullable = false)
	private String boardid;
	
	@Column(length = 255, nullable = false)
	private String whatpart;
	
	@Column(length = 255, nullable = false)
	private String choose_b_c;
	
	@Builder
	public Comment(Long id, String writer, String content, 
			String whatpart, String choose_b_c, String boardid) {
		this.id = id;
		this.writer = writer;
		this.content = content;
		this.boardid = boardid;
		this.whatpart = whatpart;
		this.choose_b_c = choose_b_c;
	}
}
