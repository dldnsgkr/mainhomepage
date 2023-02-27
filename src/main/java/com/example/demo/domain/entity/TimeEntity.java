package com.example.demo.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass // 테이블로 매핑하지 않고, 자식 Entity에게 매핑정보를 상속하기 위한 어노테이션
@EntityListeners(AuditingEntityListener.class) // JPA에게 해당 Entity는 AUditing기능을 사용한다는 것을 알리는 어노테이션
public class TimeEntity {
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime modifiedDate;

}
