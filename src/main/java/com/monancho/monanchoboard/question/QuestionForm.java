package com.monancho.monanchoboard.question;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionForm { // 질문 글의 제목과 내용의 유효성 체크용 클래스
	
	@NotEmpty(message = "제목은 필수 항목입니다.") //제목이 공란으로 들어오면 작동
	@Size(max=200, message = "제목은 최대 {max}글자까지 허용됩니다.") // 제목이 최대 200글자까지 허용
	@Size(min=5, message = "제목은 최소 {min}글자까지 허용됩니다.")
	private String subject;
	
	@NotEmpty(message = "내용은 필수 항목입니다.")
	@Size(max=500, message = "내용은 최대 {max}글자까지 허용됩니다.")
	@Size(min=5, message = "내용은 최소 {min}글자까지 허용됩니다.") //제목이 최대 500글자까지 허용

	private String content;
	
	@NotEmpty(message = "잘못된 접근 입니다.")
	private String boardType;
	
	private LocalDateTime date;
}
