package com.example.demo.Controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.MemberDTO;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;
import com.example.demo.service.MemberService;


import org.springframework.ui.Model;
@Controller
public class BoardController {

	private BoardService boardservice;
	private CommentService commentService;
	private MemberService memberService;
	
	public BoardController(BoardService boardService,CommentService commentService, MemberService memberService) {
		this.boardservice = boardService;
		this.commentService = commentService;
		this.memberService = memberService;
	}
	
	@GetMapping("/")
	public String start(@CookieValue(value = "autoid", required=false) String autoid, @CookieValue(value = "autopw", required=false) String autopw
			, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(autoid != null && autopw != null) {
		MemberDTO memberDto = memberService.login(autoid,autopw);
		if(memberDto == null) {
			String message = "아이디 비번 입력 오류";
			model.addAttribute("message",message);
			session.setAttribute("mempw", null);
			session.setAttribute("memid", null);
			session.setAttribute("loginstate", false);
			return "member/loginfault.html";
		}
		String name = memberDto.getName();
		System.out.println(name);
		session.setAttribute("mempw", autopw);
		session.setAttribute("memid", autoid);
		session.setAttribute("name", name);
		session.setAttribute("loginstate", true);
		} 
		return "common/index.html";
	}
	
	@GetMapping("/main")
	public String main() {
		return "common/index.html";
	}
	
	@GetMapping("/post")
	public String write(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("memid") == null)
		{
			return "member/mustlogin.html";
		}
		String name = (String) session.getAttribute("name");
		System.out.println(name);
		model.addAttribute("writer", name);
		return "board/write.html";
	}	
	
	@PostMapping("/post")
	public String write(BoardDTO boardDTO){
		
            boardservice.savePost(boardDTO);
		
		return "redirect:/";
	}
	
	@GetMapping("/list")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
		List<BoardDTO> boardDtoList = boardservice.getBoardList(pageNum);
		Integer[] pageList = boardservice.getPageList(pageNum);
		model.addAttribute("boardList",boardDtoList);
		model.addAttribute("pageList",pageList);
		return "board/list.html";
	}
	
	@GetMapping("/share")
	public String sharelist(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
		List<BoardDTO> boardDtoList = boardservice.getShareList(pageNum);
		Integer[] pageList = boardservice.getsharePageList(pageNum);
		Long contentcount = boardservice.getShareBoardCount();
		Integer lastpage = boardservice.gettotalLastPageNum();
		Integer curPageNum = boardservice.getCurPageNum(pageNum);
		model.addAttribute("boardList",boardDtoList);
		model.addAttribute("pageList",pageList);
		model.addAttribute("number",curPageNum);
		model.addAttribute("contentcount",contentcount);
		model.addAttribute("lastpage",lastpage);
		return "board/sharelist.html";
	}
	
	@GetMapping("/post/{no}")
	public String detail(@PathVariable("no") Long id, String recieve_read, Model model,HttpServletRequest request, HttpServletResponse response) {
		BoardDTO boardDto = boardservice.getPost(id);
		List<CommentDTO> commentDtoList = commentService.getCommentlist(id);
			boardservice.readcnt(id, request, response);
	   	
		model.addAttribute("boardDto",boardDto);
		model.addAttribute("commentDtoList",commentDtoList);
		HttpSession session = request.getSession();
		String name = (String)session.getAttribute("name");
		model.addAttribute("name",name);
		System.out.println(name);
		System.out.println(recieve_read);
		return "board/detail.html";
	}
	
	@GetMapping("/post/comment/{no}/{whatpart}/{choosebc}/{commentid}")
	public String commentpage(@PathVariable("no") Long id,@PathVariable("whatpart") String whatpart,@PathVariable("choosebc") String choosebc,@ PathVariable("commentid") Long commentid, 
			Model model, HttpServletRequest request) {
		model.addAttribute("commentid", commentid);
		model.addAttribute("boardid", id);
		model.addAttribute("whatpart", whatpart);
		System.out.println(whatpart);
		model.addAttribute("choose_b_c", choosebc);
		HttpSession session = request.getSession();
		if(session.getAttribute("memid") == null)
		{
			return "member/mustlogin.html";
		}
		String name = (String)session.getAttribute("name");
		model.addAttribute("name",name);
		return "board/commentwrite.html";
	}
	
	@PostMapping("/post/comment")
	public String comment(CommentDTO commentDTO,HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("memid") == null)
		{
			return "member/mustlogin.html";
		}
		commentService.save(commentDTO);
		return "redirect:/";
	}
	
	
	
	
	
	
	@GetMapping("/post/comment/update/{no}")
	public String commentedit(@PathVariable("no") Long id,  Model model) {
		CommentDTO commentDTO = commentService.getPost(id);
		System.out.println(commentDTO);
		model.addAttribute("commentDTO",commentDTO);
		return "board/commentupdate.html";
	}
	
	@PutMapping("/post/comment/updateing/{no}")
	public String commentedit(CommentDTO commentDTO) {
		commentService.savePost(commentDTO);
		return "redirect:/";
	}
	
	@GetMapping("/post/comment/delete/{no}")
	public String commentdelete(@PathVariable("no") Long id) {
		commentService.deletePost(id);
		return "redirect:/";
	}
	
	
	
	
	
	
	
	@GetMapping("/post/edit/{no}")
	public String edit(@PathVariable("no") Long id,  Model model) {
		BoardDTO boardDto = boardservice.getPost(id);
		
		model.addAttribute("boardDto",boardDto);
		return "board/update.html";
	}
	
	@PutMapping("/post/edit/{no}")
	public String edit(BoardDTO boardDTO) {
		boardservice.savePost(boardDTO);
		return "redirect:/";
	}
	
//	@DeleteMapping("/post/{no}")
//	public String delete(@PathVariable("no") Long id) {
//		boardservice.deletePost(id);
//		return "redirect:/";
//	}
	
	@GetMapping("/post/delete/{no}")
	public String delete(@PathVariable("no") Long id) {
		boardservice.deletePost(id);
		return "redirect:/";
	}
	
	@GetMapping("/board/search")
	public String search(@RequestParam(value = "keyword") String keyword, 
			@RequestParam(value = "whataboutsearch") String whataboutsearch, Model model) {
		if(true) {}
		System.out.println(whataboutsearch);
		if(whataboutsearch.equals("제목")) {
		List<BoardDTO> boardDtoList = boardservice.searchtitle(keyword);
		model.addAttribute("boardList",boardDtoList);
		if(boardDtoList.isEmpty()) {
			model.addAttribute("torf","true");
		} else {
			model.addAttribute("torf","false");
		}
		return "board/titlelist.html";
		} else if(whataboutsearch.equals("내용")) {
			List<BoardDTO> boardDtoList = boardservice.searchcontent(keyword);
			model.addAttribute("boardList",boardDtoList);
			System.out.println(boardDtoList.isEmpty());
			if(boardDtoList.isEmpty()) {
				model.addAttribute("torf","true");
			} else {
				model.addAttribute("torf","false");
			}
			return "board/contentlist.html";
		} else {
			List<BoardDTO> boardDtoList = boardservice.searchPosts(keyword);
			model.addAttribute("boardList",boardDtoList);
			if(boardDtoList.isEmpty()) {
				model.addAttribute("torf","true");
			} else {
				model.addAttribute("torf","false");
			}
			return "board/contilist.html";
		}
		
	}
	
}
