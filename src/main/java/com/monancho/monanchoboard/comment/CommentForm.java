package com.monancho.monanchoboard.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentForm {
	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;
}
