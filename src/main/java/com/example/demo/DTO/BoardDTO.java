package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.domain.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class BoardDTO {

	private Long id;
	private String writer;
	private String title;
	private String content;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private String whatpart;
	
	public Board toEntity() {
		Board build = Board.builder()
				.id(id)
				.writer(writer)
				.title(title)
				.content(content)
				.whatpart(whatpart)
				.build();
		return build;
	}
	
	@Builder
	public BoardDTO(Long id, String title, String content, String writer,LocalDateTime createdDate , LocalDateTime modifiedDate, String whatpart) {
		this.id = id;
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.whatpart = whatpart;
		
	}
}
