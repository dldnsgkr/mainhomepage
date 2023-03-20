package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;import javax.print.attribute.standard.PageRanges;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.domain.entity.Board;
import com.example.demo.domain.repository.BoardRepository;


@Service
public class BoardService {

	private BoardRepository boardRepository;
	private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 수
	private static final int PAGE_POST_COUNT = 20; //한 페이지에 존재하는 게시글 수
	
	
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	@Transactional
	public void savePost(BoardDTO boardDTO) {
		boardRepository.save(boardDTO.toEntity()).getId();
	}
	
	@Transactional
	public List<BoardDTO> getBoardList(Integer pageNum){
		
		Page<Board> page = boardRepository
				.findAll(PageRequest
						.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));
		
		List<Board> boards = page.getContent(); // 페이징 출력
		//List<Board> boards = boardRepository.findAll(); // 출력
		List<BoardDTO> boardDtoList = new ArrayList<>();
		
		for(Board board : boards) {
			BoardDTO boarddto = BoardDTO.builder()
					.id(board.getId())
					.title(board.getTitle())
					.content(board.getContent())
					.writer(board.getWriter())
					.createdDate(board.getCreatedDate())
					.whatpart(board.getWhatpart())
					.readcnt(board.getReadcnt())
					.build();
			boardDtoList.add(boarddto);
		}
		
		return boardDtoList;
	}
	
	@Transactional
	public List<BoardDTO> getShareList(Integer pageNum){
		
		Page<Board> page = boardRepository.findByWhatpart("share", PageRequest
                .of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.ASC, "createdDate")));
		
		List<Board> boards = page.getContent(); // 페이징 출력
		//List<Board> boards = boardRepository.findAll(); // 출력
		List<BoardDTO> boardDtoList = new ArrayList<>();
		
		for(Board board : boards) {
			BoardDTO boarddto = BoardDTO.builder()
					.id(board.getId())
					.title(board.getTitle())
					.content(board.getContent())
					.writer(board.getWriter())
					.createdDate(board.getCreatedDate())
					.whatpart(board.getWhatpart())
					.readcnt(board.getReadcnt())
					.build();
			boardDtoList.add(boarddto);
		}
		
		return boardDtoList;
	}
	
	//페이징
		public Integer[] getPageList(Integer curPageNum) {
			Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
			
			//총 게시글 수
			Double postsTotalCount = Double.valueOf(this.getBoardCount());
			
			//총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
			Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
			
			//현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
			Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
					? curPageNum + BLOCK_PAGE_NUM_COUNT
					: totalLastPageNum;
			
			//페이지 시작 번호 조정
			curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;
			
			//페이지 번호 할당
			for(int val=curPageNum, i=0;val<=blockLastPageNum;val++, i++) {
				pageList[i] = val;
			}
			
			return pageList;
		}
		
		//share페이징
				public Integer[] getsharePageList(Integer curPageNum) {
					Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];
					
					//총 게시글 수
					Double postsTotalCount = Double.valueOf(this.getBoardCountByWhatpart("share"));
					
					//총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
					Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
					
					//현재 페이지를 기준으로 블럭의 마지막 페이지 번호 계산
					Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
							? curPageNum + BLOCK_PAGE_NUM_COUNT
							: totalLastPageNum;
					
					//페이지 시작 번호 조정
					curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;
					
					//페이지 번호 할당
					for(int val=curPageNum, i=0;val<=blockLastPageNum;val++, i++) {
						pageList[i] = val;
					}
					
					return pageList;
				}
		

	public Long getBoardCountByWhatpart(String whatpart) {
	    return boardRepository.countByWhatpart(whatpart);
	}
	
	@Transactional
	public BoardDTO getPost(Long id) {
		Optional<Board> boardwrapper = boardRepository.findById(id);
		Board board = boardwrapper.get();
		
		BoardDTO boardDTO = BoardDTO.builder()
				.id(board.getId())
				.title(board.getTitle())
				.content(board.getContent())
				.writer(board.getWriter())
				.createdDate(board.getCreatedDate())
				.whatpart(board.getWhatpart())
				.build();
		
		return boardDTO;
	}

	public void deletePost(Long id) {
		boardRepository.deleteById(id);
		
	}

	public List<BoardDTO> searchtitle(String keyword) {
		List<Board> boards = boardRepository.findByTitleContaining(keyword);
		List<BoardDTO> boardDTOList = new ArrayList<>();
		
		if(boards.isEmpty()) return boardDTOList;
		
		for(Board board : boards) {
			boardDTOList.add(this.convertEntityToDto(board));
		}
		
		return boardDTOList;
	}
	
	public List<BoardDTO> searchcontent(String keyword) {
		List<Board> boards = boardRepository.findByContentContaining(keyword);
		List<BoardDTO> boardDTOList = new ArrayList<>();
		
		if(boards.isEmpty()) return boardDTOList;
		
		for(Board board : boards) {
			boardDTOList.add(this.convertEntityToDto(board));
		}
		
		return boardDTOList;
	}
	
	public List<BoardDTO> searchPosts(String keyword) {
		List<Board> boards = boardRepository.findByPosts(keyword);
		List<BoardDTO> boardDTOList = new ArrayList<>();
		
		if(boards.isEmpty()) return boardDTOList;
		
		for(Board board : boards) {
			boardDTOList.add(this.convertEntityToDto(board));
		}
		
		return boardDTOList;
	}

	private BoardDTO convertEntityToDto(Board board) {
		return BoardDTO.builder()
				.id(board.getId())
				.title(board.getTitle())
				.content(board.getContent())
				.writer(board.getWriter())
				.createdDate(board.getCreatedDate())
				.whatpart(board.getWhatpart())
				.build();
	}
	
	@Transactional
	public Long getBoardCount() {
		return boardRepository.count();
	}
	
	
}
