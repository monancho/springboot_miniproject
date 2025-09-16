package com.monancho.monanchoboard.answer;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@SequenceGenerator(
		name = "ANSWER_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "ANSWER_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1, // 시퀀스 시작값
		allocationSize = 1 // 시퀀스 증가치
		)

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANSWER_SEQ_GENERATOR")
	private Integer id;
	
	@Column(length = 500)
	private String content;
	

	private LocalDateTime createdate;
	 private LocalDateTime modifyDate; 
	
	@ManyToOne
	private Question question;
	
	@ManyToOne
	private SiteUser author;
	
	@ManyToMany
	Set<SiteUser> voter;
	
	@ManyToMany
	Set<SiteUser> disvoter;
}
