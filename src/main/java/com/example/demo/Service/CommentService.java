package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.CommentDTO;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.entity.Comment;
import com.example.demo.domain.repository.BoardRepository;
import com.example.demo.domain.repository.CommentRepository;


@Service
public class CommentService {

	private CommentRepository commentRepository;
	
	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	public List<CommentDTO> getCommentlist(Long id) {
		List<Comment> comments = commentRepository.boardid(id);
		List<CommentDTO> commentDtoList = new ArrayList<>();
		for(Comment comment : comments) {
		CommentDTO commentDto = CommentDTO.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.writer(comment.getWriter())
				.boardid(comment.getBoardid())
				.createdDate(comment.getCreatedDate())
				.build();
		commentDtoList.add(commentDto);
		}
		return commentDtoList;
	}

	@Transactional
	public void save(CommentDTO commentDTO) {
		commentRepository.save(commentDTO.toEntity());
	}

	public CommentDTO getPost(Long id) {
		Optional<Comment> commentwrapper = commentRepository.findById(id);
		Comment comment = commentwrapper.get();
		
		CommentDTO commentDTO = CommentDTO.builder()
				.id(comment.getId())
				.content(comment.getContent())
				.writer(comment.getWriter())
				.boardid(comment.getBoardid())
				.createdDate(comment.getCreatedDate())
				.build();
		
		return commentDTO;
	}

	public void savePost(CommentDTO commentDTO) {
		commentRepository.save(commentDTO.toEntity());
	}

	public void deletePost(Long id) {
		commentRepository.deleteById(id);
	}
}
