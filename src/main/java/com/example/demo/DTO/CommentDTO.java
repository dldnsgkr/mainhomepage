package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CommentDTO {

	private Long id;
	private String writer;
	private String content;
	private String boardid;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	
	public Comment toEntity() {
		Comment build = Comment.builder()
				.id(id)
				.writer(writer)
				.content(content)
				.boardid(boardid)
				.build();
		return build;
	}
	
	@Builder
	public CommentDTO(Long id, String content, String writer, String boardid, LocalDateTime createdDate , LocalDateTime modifiedDate) {
		this.id = id;
		this.writer = writer;
		this.content = content;
		this.boardid = boardid;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
}
}
