package com.monancho.monanchoboard.comment;

import java.time.LocalDateTime;
import java.util.Set;

import com.monancho.monanchoboard.question.Question;
import com.monancho.monanchoboard.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@SequenceGenerator(
		name = "COMMENT_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "COMMENT_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1, // 시퀀스 시작값
		allocationSize = 1 // 시퀀스 증가치
		)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
	private Integer id;
	
	@Column(length = 500)
	private String content;
	

	private LocalDateTime createdate;
	
	@ManyToOne
	private Question question;
	
	@ManyToOne
	private SiteUser author;
	
	
}
