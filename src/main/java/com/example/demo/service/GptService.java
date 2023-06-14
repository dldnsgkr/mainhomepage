package com.example.demo.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class GptService {
	//text-davinci-003 형식(gpt3.0 ver)
	private static final String gpt3_ENDPOINT = "https://api.openai.com/v1/";
	
	//gpt-3.5-turbo 형식(gpt3.5 ver)
	private static final String gpt35_ENDPOINT = "https://api.openai.com/v1/chat/";
	@Value("${openai.api-key}")
    private String OPEN_AI_KEY;
	
	public String posttoopenai(String question, String where) {
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_KEY);
        if(where.equals("3.0")) {
        
        	//text-davinci-003 형식(gpt3.0 ver)
            String requestBody = "{\n" +
                    "  \"model\": \"text-davinci-003\",\n" +
                    "  \"prompt\": \""+question+"\",\n" +
                    "  \"max_tokens\": 3000,\n" +
                    "  \"temperature\": 0.5\n" +
                    "}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    		
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = gpt3_ENDPOINT + "completions";
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            
            //gpt 응답
            String result = extractTextFromResponse(response.getBody());
            return result;
            
        } else {
        	//gpt-3.5-turbo 형식(gpt3.5 ver)
        	String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [\n" +
                    "    {\"role\": \"system\", \"content\": \"안녕하세요. 어떤 도움이 필요하신가요?\"},\n" +
                    "    {\"role\": \"user\", \"content\": \""+question+"\"}\n" +
                    "  ]}";
        	HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
    		
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = gpt35_ENDPOINT + "completions";
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            
          //gpt 응답
            String result = extractTextFromResponse(response.getBody());
            return result;
        }
        
	}

	private String extractTextFromResponse(String responseBody) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonResponse = objectMapper.readTree(responseBody);
	        String result = jsonResponse.get("choices").get(0).get("text").asText();
	        return result;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
