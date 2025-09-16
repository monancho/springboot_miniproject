package com.monancho.monanchoboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.monancho.monanchoboard.DataNotFoundException;
//import com.monancho.monanchoboard.answer.Answer;
import com.monancho.monanchoboard.user.SiteUser;

//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Join;
//import jakarta.persistence.criteria.JoinType;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
//	@Autowired
	private final QuestionRepository questionRepository;
	// @RequiredArgsConstructor에 의해 생성자 방식으로 주입된 questionRepostitory
	
//	public Page<Question> getList(int page) { //모든 질문글 가져오기
//		
//		Pageable pageable = PageRequest.of(page, 10);
//		return questionRepository.findAll(pageable);
//		
//	}
	public Question getQuestion(Integer id) {
		Optional<Question> optional = questionRepository.findById(id);
		if(optional.isPresent()) return optional.get();
		else throw new DataNotFoundException("question not found");
		
	}
	public void createQuestion(String subject, String content, SiteUser user, String BoardType) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreatedate(LocalDateTime.now());
		question.setAuthor(user); // 글쓴이 엔티티 추가
		question.setBoardType(BoardType);
		questionRepository.save(question);
	}
	public void createQuestion2(String subject, String content, SiteUser user, LocalDateTime date, String BoardType) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreatedate(LocalDateTime.now());
		question.setModifyDate(date);
		question.setBoardType(BoardType);
		
		question.setAuthor(user); // 글쓴이 엔티티 추가
		questionRepository.save(question);
	}
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		questionRepository.save(question);
	}
	public void modify2(Question question, String subject, String content, LocalDateTime date) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(date);
		questionRepository.save(question);
	}
	public void delete(Question question) {
		questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) { // -> update문으로 만들어줘야 함
		question.getVoter().add(siteUser);
		//question->추천을 받은 글의 번호로 조회한 질문 엔디티
		//question의 멤버인 voter를 get 해서 voter에 추천을 누른 유저의 엔티티를 추가해 줌
		questionRepository.save(question);// 추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	public void disvote(Question question, SiteUser siteUser) { // -> update문으로 만들어줘야 함
		question.getDisvoter().add(siteUser);
		//question->추천을 받은 글의 번호로 조회한 질문 엔디티
		//question의 멤버인 voter를 get 해서 voter에 추천을 누른 유저의 엔티티를 추가해 줌
		questionRepository.save(question);// 추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	
	public void hit(Question question) {
		question.setHit(question.getHit()+1);
		questionRepository.save(question);
		
	}
	
	public Page<Question> getPageQuestions(int page, String kw, String boardType) {
		
		int size = 10;
		
		int startRow = page * size;
		int endRow = startRow +size;
		
		List<Question> pageQuestionList;
		int totalSearchQuestion;
		// 검색어 없이 리스트 조회
		
		
		pageQuestionList = questionRepository.searchFindQuestionsWithPaging(kw ,startRow, endRow,boardType);
		totalSearchQuestion = questionRepository.countSearchResult(kw,boardType);
		
		Page<Question> pagingList = new PageImpl<>(pageQuestionList, PageRequest.of(page, size), totalSearchQuestion);
		
		return pagingList;
		
	}
	
//	private Specification<Question> search(String kw) {
//		
//		return new Specification<Question>() {
//			private static final long SerialVersionUID = 1L;
//			
//			@Override
//			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				
//				query.distinct(true); // 중복제거
//				Join<Question, SiteUser> ul = q.join("author", JoinType.LEFT); // question + siteUser left 조인
//				Join<Question, Answer> a =  q.join("answerList", JoinType.LEFT); //  question + answer left 조인
//				Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT); // answer + siteUser left 조인
//				
//				return cb.or(
//						cb.like(q.get("subject"), "%" + kw + "%"),
//						cb.like(q.get("content"), "%" + kw + "%"),
//						cb.like(ul.get("username"), "%" + kw + "%"),
//						cb.like(a.get("content"), "%" + kw + "%"),
//						cb.like(u2.get("username"), "%" + kw + "%")
//						);
//			}
//		};
	}
