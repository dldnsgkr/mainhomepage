package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.NaverLoginDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverService {

	 public NaverLoginDTO naverprofile(String daccessToken) {
	        String token = daccessToken; // 네이버 로그인 접근 토큰;
	        String header = "Bearer " + token; // Bearer 다음에 공백 추가


	        String apiURL = "https://openapi.naver.com/v1/nid/me";


	        Map<String, String> requestHeaders = new HashMap<>();
	        requestHeaders.put("Authorization", header);
	        String responseBody = get(apiURL,requestHeaders);

	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
	            System.out.println(responseMap);
	            Map<String, Object> response = (Map<String, Object>) responseMap.get("response");

	            NaverLoginDTO naverLoginDTO = NaverLoginDTO.builder()
	                    .nickname((String) response.get("nickname"))
	                    .profile_image((String) response.get("profile_image"))
	                    .gender((String) response.get("gender"))
	                    .email((String) response.get("email"))
	                    .build();

	                return naverLoginDTO;
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to parse JSON response", e);
	        }
	        
	    }


	    private static String get(String apiUrl, Map<String, String> requestHeaders){
	        HttpURLConnection con = connect(apiUrl);
	        try {
	            con.setRequestMethod("GET");
	            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
	                con.setRequestProperty(header.getKey(), header.getValue());
	            }


	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
	                return readBody(con.getInputStream());
	            } else { // 에러 발생
	                return readBody(con.getErrorStream());
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("API 요청과 응답 실패", e);
	        } finally {
	            con.disconnect();
	        }
	    }


	    private static HttpURLConnection connect(String apiUrl){
	        try {
	            URL url = new URL(apiUrl);
	            return (HttpURLConnection)url.openConnection();
	        } catch (MalformedURLException e) {
	            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	        } catch (IOException e) {
	            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	        }
	    }


	    private static String readBody(InputStream body){
	        InputStreamReader streamReader = new InputStreamReader(body);


	        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
	            StringBuilder responseBody = new StringBuilder();


	            String line;
	            while ((line = lineReader.readLine()) != null) {
	                responseBody.append(line);
	            }


	            return responseBody.toString();
	        } catch (IOException e) {
	            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
	        }
	    }
	    
	    
	    
	    public String getAccessToken(String clientId, String clientSecret, String code, String state, String redirectUri) {
	    	// 액세스 토큰을 얻기 위한 엔드포인트 URL
	        String apiUrl = "https://nid.naver.com/oauth2.0/token";

	        // HTTP 요청 헤더 설정
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	        // HTTP 요청 바디 설정
	        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
	        requestBody.add("grant_type", "authorization_code");
	        requestBody.add("client_id", clientId);
	        requestBody.add("client_secret", clientSecret);
	        requestBody.add("code", code);
	        requestBody.add("state", state);
	        requestBody.add("redirect_uri", redirectUri);

	        // HTTP POST 요청 생성
	        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
	                .post(apiUrl)
	                .headers(headers)
	                .body(requestBody);

	        // REST 템플릿을 사용하여 HTTP 요청 전송
	        ResponseEntity<Map> response = new RestTemplate().exchange(request, Map.class);

	        // 응답 결과에서 액세스 토큰 추출
	        if (response.getStatusCode().is2xxSuccessful()) {
	            Map<String, Object> responseBody = response.getBody();
	            if (responseBody != null) {
	                return (String) responseBody.get("access_token");
	            }
	        }

	        return null;
	    }
	    
	    
}
