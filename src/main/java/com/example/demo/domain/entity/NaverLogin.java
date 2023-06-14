package com.example.demo.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.demo.DTO.NaverLoginDTO.NaverLoginDTOBuilder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverLogin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 10, nullable = false)
	private String nickname;
	
	@Column(length = 100, nullable = false)
	private String profile_image;
	
	@Column(length = 10, nullable = false)
	private String gender;
	
	@Column(length = 10, nullable = false)
	private String email;
	
	@Column(length = 10, nullable = false)
	private String path;
	
	@Builder
	public NaverLogin(Long id, String nickname, String profile_image, String gender, String email, String path) {
		this.id = id;
		this.nickname = nickname;
		this.profile_image = profile_image;
		this.gender = gender;
		this.email = email;
		this.path = path;
	}
}
