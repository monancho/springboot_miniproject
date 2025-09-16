package com.monancho.monanchoboard.comment;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monancho.monanchoboard.DataNotFoundException;
import com.monancho.monanchoboard.question.Question;
import com.monancho.monanchoboard.user.SiteUser;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	public Comment create(Question question, String content, SiteUser author) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setCreatedate(LocalDateTime.now());
		comment.setQuestion(question);
		comment.setAuthor(author);
		
		commentRepository.save(comment);
		
		return comment;
	}
	
	public Comment getComment(Integer id) {
		Optional<Comment> _comment = commentRepository.findById(id);
		if(_comment.isPresent()) {
			return _comment.get();
		} else {
			throw new DataNotFoundException("해당 댓글이 존재하지 않습니다");
		}
	}
	public void delete(Comment comment) {
		commentRepository.delete(comment);
	}
}
