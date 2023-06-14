package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Gpt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class GptDTO {

	private Long id;
	private String question;
	
	public Gpt toEntity() {
		Gpt build = Gpt.builder()
				.id(id)
				.question(question)
				.build();
		return build;
	}
	
	@Builder
	public GptDTO(Long id, String question) {
		this.id = id;
		this.question = question;
		
	}
}
