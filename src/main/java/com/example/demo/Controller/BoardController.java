package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.MemberDTO;
import com.example.demo.Service.BoardService;
import com.example.demo.Service.CommentService;
import com.example.demo.Service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


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
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
		String formatedNow = now.format(formatter);
		if(formatedNow == "01/01")
		{
			memberService.updateage();
		}
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
	
	@GetMapping("/post/{no}")
	public String detail(@PathVariable("no") Long id,  Model model,HttpServletRequest request) {
		BoardDTO boardDto = boardservice.getPost(id);
		List<CommentDTO> commentDtoList = commentService.getCommentlist(id);
		model.addAttribute("boardDto",boardDto);
		model.addAttribute("commentDtoList",commentDtoList);
		HttpSession session = request.getSession();
		String name = (String)session.getAttribute("name");
		model.addAttribute("name",name);
		return "board/detail.html";
	}
	
	@GetMapping("/post/comment/{no}")
	public String commentpage(@PathVariable("no") Long id,  Model model, HttpServletRequest request) {
		model.addAttribute("boardid", id);
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
	public String comment(CommentDTO commentDTO) {
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
	
	@DeleteMapping("/post/{no}")
	public String delete(@PathVariable("no") Long id) {
		boardservice.deletePost(id);
		return "redirect:/";
	}
	
	@GetMapping("/board/search")
	public String search(@RequestParam(value = "keyword") String keyword,  Model model) {
		List<BoardDTO> boardDtoList = boardservice.searchPosts(keyword);
		
		model.addAttribute("boardList",boardDtoList);
		return "board/list.html";
	}

}
