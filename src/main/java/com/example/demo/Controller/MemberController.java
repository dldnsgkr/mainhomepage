package com.example.demo.Controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.DTO.BoardDTO;
import com.example.demo.DTO.MemberDTO;
import com.example.demo.domain.entity.Member;
import com.example.demo.domain.repository.MemberRepository;
import com.example.demo.service.BoardService;
import com.example.demo.service.MemberService;




@Controller
public class MemberController {
	
private MemberService memberService;
	
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "member/signup";
	}
	
	@PostMapping("/signuping")
	public String signuping(MemberDTO memberDTO) {
		String memid = memberDTO.getMemid();
		int memberDto = memberService.checkMemid(memid);
		System.out.println(memberDto);
		if(memberDto == 0) {
			memberService.saveMember(memberDTO);
			return "redirect:/";
		} else {
			return "member/signupfault.html";
		}
	}
	
	
	@GetMapping("/login")
	public String lgin() {
		return "member/login.html";
	}
	
	@PostMapping("/logining")
	@ResponseBody
	public boolean logining(MemberDTO memberDTO,Model model,HttpServletResponse response ,HttpServletRequest request, @RequestParam(value = "autologin" , required = false)String autologin) {
		String memid = memberDTO.getMemid();
		String mempw = memberDTO.getMempw();
		System.out.println(memid);
		System.out.println(mempw);
		MemberDTO memberDto = memberService.login(memid,mempw);
	    if (memberDto != null) {
	    	// 로그인 성공
	    	String name = memberDto.getName();

			HttpSession session = request.getSession();
			session.setAttribute("mempw", mempw);
			session.setAttribute("memid", memid);
			session.setAttribute("name", name);
			session.setAttribute("loginstate", true);
			if(autologin != null)
			{
				Cookie cookie1 = new Cookie("autoid", memid);
				Cookie cookie2 = new Cookie("autopw", mempw);
				cookie1.setPath("/");
				cookie1.setMaxAge(60*60*24*7);
				cookie2.setPath("/");
				cookie2.setMaxAge(60*60*24*7);
				response.addCookie(cookie1);
				response.addCookie(cookie2);
			}
	      return true;
	    } else {
	    	// 로그인 실패
		      return false;
	    }
			//return "member/loginfault.html";
		
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.removeAttribute("memid");
		session.removeAttribute("mempw");
		session.removeAttribute("name");
		session.removeAttribute("loginstate");
		Cookie cookie1 = new Cookie("autoid", null);
		Cookie cookie2 = new Cookie("autopw", null);
		cookie1.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
		cookie1.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
		cookie2.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
		cookie2.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
	    response.addCookie(cookie1);
	    response.addCookie(cookie2);
	    session.setAttribute("loginstate", false);
		return "common/index.html";
	}

}
