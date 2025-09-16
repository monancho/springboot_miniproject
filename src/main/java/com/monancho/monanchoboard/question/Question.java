package com.monancho.monanchoboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.monancho.monanchoboard.answer.Answer;
import com.monancho.monanchoboard.comment.Comment;
import com.monancho.monanchoboard.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // DB 테이블과 매핑할 entity 클래스 설정
@Table(name = "question")
@SequenceGenerator(
		name = "QUESTION_SEQ_GENERATOR", // JPA 내부 시퀀스 이름
		sequenceName = "QUESTION_SEQ", // 실제 DB 시퀀스 이름
		initialValue = 1, // 시퀀스 시작값
		allocationSize = 1 // 시퀀스 증가치
		)

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_GENERATOR")
	private Integer id; // 질문게시판의 글번호(기본키 자동증가 옵션) 
	
	@Column(length = 200) // 200자까지 허용
	private String subject; // 질문게시판의 제목
	
	@Column(length = 500) 
	private String content; // 질문게시판의 내용
	
	
	private LocalDateTime createdate; // 생성 날짜
	 private LocalDateTime modifyDate; // 수정 날짜
	 
	 private String boardType;
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	// cascade -> 질문글(부모글)이 삭제될 경우 답변들(자식글)이 함께 삭제되게 하는 설정
	private List<Answer> answerList;
	
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
	private List<Comment> commentList;
	
	// N:1
	@ManyToOne
	private SiteUser author;
	
	
	
	@ManyToMany
	Set<SiteUser> voter; // 추천한 유저가 중복 없이 여러 명의 유저가 저장-> 유저의 수 -> 추천수
	//Set -> 중복 제거용 컬렉션 사용 -> 유저 한명당 1개만 가능하게
	@ManyToMany
	Set<SiteUser> disvoter; // 추천한 유저가 중복 없이 여러 명의 유저가 저장-> 유저의 수 -> 추천수
	//Set -> 중복 제거용 컬렉션 사용 -> 유저 한명당 1개만 가능하게
	
	private Integer hit = 0; // 질문글 조회수
}
