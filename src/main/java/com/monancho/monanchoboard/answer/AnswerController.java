package com.monancho.monanchoboard.answer;

import java.security.Principal;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.monancho.monanchoboard.question.Question;
import com.monancho.monanchoboard.question.QuestionService;
import com.monancho.monanchoboard.user.SiteUser;
import com.monancho.monanchoboard.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UserService userService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		Question question = questionService.getQuestion(id);
		//principal.getName();
		
		SiteUser siteUser = userService.getUser(principal.getName());
		
		//TODO: 답변을 저장한다.
		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
		Answer answer = answerService.create(question, answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", id, answer.getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal ) {
		Answer answer = answerService.getAnswer(id);
		
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer  = answerService.getAnswer(id);
		answerService.modify(answer, answerForm.getContent()); // 수정 완료
	
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete( @PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		if(!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
		}
		answerService.delete(answer);
		return  String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(@PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		
		SiteUser siteUser = userService.getUser(principal.getName());
		
		answerService.vote(answer, siteUser);
		
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(),answer.getId());
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/disvote/{id}")
	public String answerdisVote(@PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		
		SiteUser siteUser = userService.getUser(principal.getName());
		
		answerService.disvote(answer, siteUser);
		
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
}
