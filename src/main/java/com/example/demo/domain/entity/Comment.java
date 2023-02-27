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
	
	@Builder
	public Comment(Long id, String writer, String content,String boardid) {
		this.id = id;
		this.writer = writer;
		this.content = content;
		this.boardid = boardid;
	}
}
