package com.example.demo.DTO;

import java.time.LocalDateTime;

import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.NaverLogin;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class NaverLoginDTO {

	private Long id;
	private String nickname;
	private String profile_image;
	private String gender;
	private String email;
	private String path;
	
	public NaverLogin toEntity() {
		NaverLogin build = NaverLogin.builder()
				.id(id)
				.nickname(nickname)
				.profile_image(profile_image)
				.gender(gender)
				.email(email)
				.path(path)
				.build();
		return build;
	}
	
	@Builder
	public NaverLoginDTO(Long id, String nickname, String profile_image, String gender, String email, String path) {
		this.id = id;
		this.nickname =nickname;
		this.profile_image = profile_image;
		this.gender = gender;
		this.email = email;
		this.path = path;
		
	}
}
