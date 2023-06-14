package com.example.demo.Controller;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.DTO.MemberDTO;
import com.example.demo.DTO.NaverLoginDTO;
import com.example.demo.service.MemberService;
import com.example.demo.service.NaverService;
import com.fasterxml.jackson.databind.ObjectMapper;




@Controller
public class MemberController {
	
private MemberService memberService;
private NaverService naverService;
	
	public MemberController(MemberService memberService, NaverService naverService) {
		this.memberService = memberService;
		this.naverService = naverService;
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
			System.out.println(autologin+12);
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
	
	@GetMapping("/naverLogin")
	public String naverlogin (HttpServletRequest request, HttpServletResponse response) throws IOException {

		return "common/loading.html";
	}

	@GetMapping("/naverinfo")
	public ResponseEntity<?> naverinfo(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String accessToken = "";
		
		Cookie[] cookiena = request.getCookies();
        if (cookiena != null) {
            for (Cookie cookie : cookiena) {
            	if (cookie.getName().equals("accessToken")) {
                	accessToken = cookie.getValue();
                }
            }
        }
        System.out.println(accessToken);
		//acessToken디코딩
		String DaccessToken = URLDecoder.decode(accessToken, "UTF-8");
		System.out.println(DaccessToken);
		NaverLoginDTO naverLoginDTO = naverService.naverprofile(DaccessToken);
		
		HttpSession session = request.getSession();
		session.setAttribute("memid", "naver");
		session.setAttribute("name", naverLoginDTO.getNickname());
		UUID uuid = UUID.randomUUID();
        String randomUUID = uuid.toString();
		session.setAttribute("mempw", randomUUID);
		session.setAttribute("loginstate", true);
		System.out.println(naverLoginDTO.getNickname());
		
		String url = "";
        // 쿠키 읽기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("url")) {
                    url = cookie.getValue();
                } 
            }
        }
        
        //url 디코딩
		String decodedUrl = URLDecoder.decode(url, "UTF-8");
		//url path만 추출
		URL url2 = new URL(decodedUrl);
		String path = url2.getPath();
		
		// 객체 생성 및 데이터 설정
	    NaverLoginDTO yourObject = new NaverLoginDTO();
	    yourObject.setPath(path);
	    
	    // JSON 변환
	    ObjectMapper objectMapper = new ObjectMapper();
	    String json = objectMapper.writeValueAsString(yourObject);
	    
	    // JSON 응답 반환
	    return ResponseEntity.ok(json);
		
	}

}
