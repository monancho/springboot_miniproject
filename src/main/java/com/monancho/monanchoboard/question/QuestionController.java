package com.monancho.monanchoboard.question;

import java.security.Principal;
//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.monancho.monanchoboard.answer.AnswerForm;
import com.monancho.monanchoboard.comment.CommentForm;
import com.monancho.monanchoboard.user.SiteUser;
import com.monancho.monanchoboard.user.UserService;

//import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
//import oracle.jdbc.proxy.annotation.Pre;



@RequestMapping("/question")
@Controller
public class QuestionController {

	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;
	
//	@GetMapping(value="/list")
//	//@ResponseBody
//	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
//		List<Question> questionList = questionRepository.findAll();
////		Page<Question> paging = questionService.getList(page);
//		model.addAttribute("paging", questionList);
//		
//		return "question_list";
//	}
//	@GetMapping(value="/list")
//	//@ResponseBody
//	public String allList(Model model, 
//			@RequestParam(value = "page", defaultValue = "0") int page,
//			@RequestParam(value = "kw", defaultValue = "") String kw) {
////		List<Question> questionList = questionRepository.findAll();
//		Page<Question> paging = questionService.getPageQuestions(page, kw, "");
//		
//		model.addAttribute("paging", paging);
//		model.addAttribute("kw", kw);
//		model.addAttribute("boardType","");
//		return "question_list";
//	}
	@GetMapping(value="/list")
	//@ResponseBody
	public String list(Model model, 
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw, 
			@RequestParam(value = "type", defaultValue = "") String boardType) {
//		List<Question> questionList = questionRepository.findAll();
		Page<Question> paging = questionService.getPageQuestions(page, kw, boardType);
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		model.addAttribute("type",boardType);
		return "question_list";
	}
	@GetMapping(value="/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm, CommentForm commentForm) {
		questionService.hit(questionService.getQuestion(id));
		
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
	}
	
//	@GetMapping("/create/") //질문 등록 폼만 매핑해주는 메서드 => GET
//	public String questionCreate(Model model,QuestionForm questionForm) {
//		model.addAttribute("board","");
//		return "question_form";
//	}
	@GetMapping("/create") //질문 등록 폼만 매핑해주는 메서드 => GET
	public String questionCreate(Model model,QuestionForm questionForm,@RequestParam(value = "type", defaultValue = "") String boardType) {
		//if(boardType.isEmpty()) return "redirect:question/create"; // 보드 타입 없을떄
		model.addAttribute("board",boardType);
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create") //질문 내용 db 저장 메서드 => POST
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		
		if(questionForm.getBoardType().isEmpty()) questionForm.setBoardType("free");
		if(bindingResult.hasErrors()) return "question_form"; 
		// 에러 발생 시 다시 질문 등록 폼으로 이동
		SiteUser siteUser = userService.getUser(principal.getName());
		System.out.println("보드타입은" + questionForm.getBoardType());
		
		
		if(questionForm.getBoardType().equals("time")) {
			this.questionService.createQuestion2(questionForm.getSubject(), questionForm.getContent(), siteUser, questionForm.getDate(), questionForm.getBoardType());
			System.out.println("예약 입력");
			return "redirect:/question/list";
			
		} else {
		this.questionService.createQuestion(questionForm.getSubject(), questionForm.getContent(), siteUser, questionForm.getBoardType());
		//TODO: 질문을 DB에 저장하기
			System.out.println("글 쓰기 입력");
		return "redirect:/question/list";}
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);
		
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		Question question = questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다.");
		}
		if (question.getBoardType().equals("time")) {
			questionService.modify2(question, questionForm.getSubject(), questionForm.getContent(), questionForm.getDate());
			return String.format("redirect:/question/detail/%s", id);
		}
		questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete( @PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);
		if(!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다.");
		}
		questionService.delete(question);
		return "redirect:/question/list";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(@PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);
		
		SiteUser siteUser = userService.getUser(principal.getName());
		
		questionService.vote(question, siteUser);
		
		return String.format("redirect:/question/detail/%s", id);
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/disvote/{id}")
	public String questiondisVote(@PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);
		
		SiteUser siteUser = userService.getUser(principal.getName());
		
		questionService.disvote(question, siteUser);
		
		return String.format("redirect:/question/detail/%s", id);
	}
}