package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;import javax.print.attribute.standard.PageRanges;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	private static final int BLOCK_PAGE_NUM_COUNT = 10; // 블럭에 존재하는 페이지 수
	private static final int PAGE_POST_COUNT = 10; //한 페이지에 존재하는 게시글 수
	
	
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
		
		Double postsTotalCount = Double.valueOf(this.getBoardCountByWhatpart("share"));
		Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
		if(pageNum == -1) {
			System.out.println("a");
			pageNum = totalLastPageNum;
		}
		
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
//					Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT)
//							? curPageNum + BLOCK_PAGE_NUM_COUNT
//							: totalLastPageNum;
					if(curPageNum == -1) {
						System.out.println("a");
						curPageNum = totalLastPageNum;
					}
					Integer blockLastPageNum = (curPageNum + BLOCK_PAGE_NUM_COUNT - 1);
					
					//페이지 시작 번호 조정
//					curPageNum = (curPageNum<=3) ? 1 : curPageNum-2;
					
					//1페이지 일떄
					if(curPageNum == 1) {
						System.out.println("1페이지");
						for(int val=curPageNum, i=0;val<=totalLastPageNum;val++, i++) {
							//총 페이지가 10페이지를 넘을때
							if(val == 11)	{
								break;
							}
							pageList[i] = val;
						}
						//현재 페이지가 마지막 페이지까지 10페이지가 나오지 않을때
					} else if(curPageNum > 10 && curPageNum + 9 >= totalLastPageNum){
						System.out.println("현재 페이지가 마지막 페이지까지 10페이지가 나오지 않을때");
						//페이지 번호 할당
						for(int val=totalLastPageNum-9, i=0;val<=totalLastPageNum;val++, i++) {
							//
//							if(val == totalLastPageNum-10)	{
//								break;
//							}
							pageList[i] = val;
							System.out.println(val);
						}
					} else if(curPageNum < 10 && curPageNum + 9 >= totalLastPageNum){
						System.out.println("총 10페이지가 안될떄");
						//페이지 번호 할당
						for(int val=1, i=0;val<=totalLastPageNum;val++, i++) {
							//
//							if(val == totalLastPageNum-10)	{
//								break;
//							}
							pageList[i] = val;
							System.out.println(val);
						}
					} else {
						//페이지 번호 할당
						for(int val=curPageNum, i=0;val<=blockLastPageNum;val++, i++) {
							if(val == totalLastPageNum+1)	{
								break;
							}
							pageList[i] = val;
							
						}
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
				.readcnt(board.getReadcnt())
				.build();
		
		return boardDTO;
	}
	

	public void readcnt(Long id, HttpServletRequest request, HttpServletResponse response) {
		
		Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        // request에 쿠키들이 있을 때
        for (int i = 0; cookies != null && i < cookies.length; i++) {
        	// postView 쿠키가 있을 때
            if (cookies[i].getName().equals("postView")) {
            	// cookie 변수에 저장
                cookie = cookies[i];
                // 만약 cookie 값에 현재 게시글 번호가 없을 때
                if (!cookie.getValue().contains("[" + id + "]")) {
                	// 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                	boardRepository.readcntup(id);
                    cookie.setValue(cookie.getValue() + "[" + id + "]");
                }
                isCookie = true;
                break;
            }
        }
        // 만약 postView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) { 
        	boardRepository.readcntup(id);
            cookie = new Cookie("postView", "[" + id + "]"); // oldCookie에 새 쿠키 생성
        }
        
        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    
		
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

	public Long getShareBoardCount() {
		Long postsTotalCount = this.getBoardCountByWhatpart("share");
		return postsTotalCount;
	}

	public Integer gettotalLastPageNum() {
		Double postsTotalCount = Double.valueOf(this.getBoardCountByWhatpart("share"));
		
		//총 게시글 수를 기준으로 계산한 마지막 페이지 번호 계산
		Integer totalLastPageNum = (int)(Math.ceil((postsTotalCount/PAGE_POST_COUNT)));
		return totalLastPageNum;
	}
	
	
	
	
	




	public Integer getCurPageNum(Integer pageNum) {
		Integer a = gettotalLastPageNum();
		if(pageNum == -1) {
			pageNum = a;
		}
		return pageNum;
	}
}
