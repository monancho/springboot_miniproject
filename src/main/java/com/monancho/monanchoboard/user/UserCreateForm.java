package com.monancho.monanchoboard.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateForm {
	
	
	@Size(min=3, max=25, message = "사용자 ID의 길이는 {min}자 이상 {max}자 이하 입니다.")
	@NotEmpty(message = "사용자 ID는 필수 항목 입니다.")
	private String username;
	
	@NotEmpty(message = "사용자 비밀번호는 필수 항목 입니다.")
	private String password1;
	
	@NotEmpty(message = "사용자 비밀번호는 필수 항목 입니다.")
	private String password2;
	
	@NotEmpty(message = "이메일은 필수 항목 입니다.")
	@Email(message = "이메일 형식이 맞지 않습니다.")
	private String email;
}
