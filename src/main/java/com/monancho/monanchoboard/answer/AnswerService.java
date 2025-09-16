package com.monancho.monanchoboard.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.monancho.monanchoboard.DataNotFoundException;
import com.monancho.monanchoboard.question.Question;
import com.monancho.monanchoboard.user.SiteUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;
	
	public Answer create(Question question ,String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreatedate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setAuthor(author);
		answerRepository.save(answer);
		
		return answer;
	}
	
	public Answer getAnswer(Integer id) {
		Optional<Answer> _answer = answerRepository.findById(id);
		if(_answer.isPresent()) {
			return _answer.get();
		} else {
			throw new DataNotFoundException("해당 답변이 존재하지 않습니다");
		}
	}
	
	public void modify(Answer answer, String content) { //답변 수정하기
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now()); // 답변 수정 일시
		answerRepository.save(answer);
	}
	
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
	public void vote(Answer answer, SiteUser siteUser) { // -> update문으로 만들어줘야 함
		answer.getVoter().add(siteUser);
		answerRepository.save(answer);// 추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	public void disvote(Answer answer, SiteUser siteUser) { // -> update문으로 만들어줘야 함
		answer.getDisvoter().add(siteUser);
		answerRepository.save(answer);// 추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
}
