package com.monancho.monanchoboard.comment;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import com.monancho.monanchoboard.SecurityConfig;
import com.monancho.monanchoboard.question.Question;
import com.monancho.monanchoboard.question.QuestionService;
import com.monancho.monanchoboard.user.SiteUser;
import com.monancho.monanchoboard.user.UserService;

import jakarta.validation.Valid;

@RequestMapping("/comment")
@Controller
public class CommentController {

	
	@Autowired
	private QuestionService questionService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;

 
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createComment(Model model, @PathVariable("id") Integer id, @Valid CommentForm commentForm, BindingResult bindingResult, Principal principal) {
		Question question = questionService.getQuestion(id);
		
		SiteUser siteUser = userService.getUser(principal.getName());
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
		Comment comment = commentService.create(question, commentForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String commentDelete(@PathVariable("id") Integer id, Principal principal) {
		Comment comment = commentService.getComment(id);
		if(!comment.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
		}
		commentService.delete(comment);
		return String.format("redirect:/question/detail/%s", comment.getQuestion().getId());
		
	}
}
