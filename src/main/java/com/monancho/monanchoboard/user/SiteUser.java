package com.monancho.monanchoboard.user;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "siteuser")
@SequenceGenerator(
		name = "USER_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "USER_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1, // 시퀀스 시작값
		allocationSize = 1 // 시퀀스 증가치
		)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteUser {
		
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ_GENERATOR")
		private Long id;
		
		@Column(unique = true) // 아이디 -> 중복 불가
		private String username;
		
		
		private String password;
		
		@Column(unique = true)
		private String email;
}
