package com.example.demo.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.demo.domain.entity.Board;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
create table board (
       id bigint not null auto_increment,
        content TEXT not null,
        title varchar(100) not null,
        writer varchar(10) not null,
        primary key (id)
    ) engine=InnoDB
*/
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends TimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 10, nullable = false)
	private String writer;
	
	@Column(length = 100, nullable = false)
	private String title;
	
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	@Column(length = 100, nullable = false)
	private String whatpart;
	
	@Builder
	public Board(Long id, String title, String content, String writer, String whatpart) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.whatpart = whatpart;
	}
}