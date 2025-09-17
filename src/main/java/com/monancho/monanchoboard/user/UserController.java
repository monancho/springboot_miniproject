package com.monancho.monanchoboard.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.val;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserRepository userRepository;
	@Autowired
	private UserService userService;


    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	
	@GetMapping("signup")
	public String signup(UserCreateForm userCreateForm) {
		
		return "signup_form";
	}
	
	@PostMapping("signup")
	public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "signup_form";
		} 
		
		if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}
		try {
			userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			// 이미 등록된 사용자 아이디의 경우 발생하는 에러 추가
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "회원가입 실패 입니다.");
			return "signup_form";
		}
		
		
		return "redirect:/";
		
	}
	@GetMapping("login")
	public String login() {
		return "login_form";
	}
	@GetMapping("page")
	public String userPage(Principal principal, Model model) {
		model.addAttribute("user",userService.getUser(principal.getName()));
		return "user_page";
	}
	@GetMapping("edit")
	public String userEdit(Principal principal, Model model) {
		model.addAttribute("email",userService.getUser(principal.getName()).getEmail());
		return "user_update";
	}
	@PostMapping("edit")
	public String userEdit( UserCreateForm createForm, Principal principal, BindingResult bindingResult, Model model) {
		SiteUser siteUser = userService.getUser(principal.getName());
		model.addAttribute("email",createForm.getEmail());
		if(bindingResult.hasErrors()) {
			return "user_update";}
		if(siteUser == null) {
			return "redirect:/user/login";}
		//TODO 비밀번호 확인은 나중에 시큐어리티 더 공부후 구현
		try {
			userService.userUpdate(siteUser, createForm);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			bindingResult.reject("passwordFailed", "비밀번호가 틀렸습니다");
			model.addAttribute("error", "비밀번호가 틀렸습니다");
			return "user_update";
			
		}
		
		return "redirect:/user/page";
			
		}
	
		
		
			
			
			
		
		
		
	
		

}
