package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.DTO.GptDTO;
import com.example.demo.service.GptService;
import com.example.demo.service.MemberService;

@Controller
public class GptController {

	private GptService gptService;
	
	public GptController(GptService gptService) {
		this.gptService = gptService;
	}
	
	@GetMapping("/gptquestion/{where}")
	public String questionview(@PathVariable("where") String where, Model model) {
		model.addAttribute("where",where);
		return "Gpt/question.html";
	}
	
	@PostMapping("/gptanswer/{where}")
	public String answer(Model model, GptDTO gptDTO,@RequestParam("where") String where) {
		String question = gptDTO.getQuestion();
		String answer = gptService.posttoopenai(question,where);
		System.out.println(answer);
		model.addAttribute("answer", answer);
		return "Gpt/answer.html";
	}
}
