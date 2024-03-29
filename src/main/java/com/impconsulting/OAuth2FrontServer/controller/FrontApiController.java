package com.impconsulting.OAuth2FrontServer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/front")
public class FrontApiController {

	private static final Log LOG = LogFactory.getLog(FrontApiController.class);

	@Autowired
	@Qualifier("oAuth2ApiWebClient")
	private WebClient oAuth2ApiWebClient;

	@Autowired
	@Qualifier("oAuth2ServerWebClient")
	private WebClient oAuth2ServerWebClient;

	@RequestMapping(value = "/js", method = RequestMethod.GET)
	public String frontJsGet(Model model, HttpServletResponse response, HttpSession session) throws Exception {
		// 세션에 담겨있는 세션 정보를 header 정보에 넣어준다.
		response.addHeader("Authorization",
				(String) session.getAttribute("tokenType") + (String) session.getAttribute("accessToken"));
		response.addHeader("accesstoken", (String) session.getAttribute("accessToken"));
		response.addHeader("refreshtoken", (String) session.getAttribute("refreshToken"));
		response.addHeader("scope", (String) session.getAttribute("scope"));
//    	LOG.info("front accesstoken" + (String) session.getAttribute("accessToken"));
//    	LOG.info("front refreshtoken" + (String) session.getAttribute("refreshToken"));
		
		return "frontJs-apiServer";
	}
	
	@RequestMapping(value = "/js", method = RequestMethod.POST)
	public ResponseEntity<String> frontJsPost(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		// 자바스크립트에서 받은 refresh_token 정보로 세션 업데이트하기
		
		String tokenType = request.getHeader("tokenType");
    	String accessToken = request.getHeader("accessToken");
    	String refreshToken = request.getHeader("refreshToken");
    	String scope = request.getHeader("scope");
    	
    	LOG.info("session refresh for Javascript");
    	session.setAttribute("tokenType", tokenType);
    	session.setAttribute("accessToken", accessToken);
    	session.setAttribute("refreshToken", refreshToken);
    	session.setAttribute("scope", scope);

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/server", method = RequestMethod.GET)
	public String frontServerGet(Model model, HttpServletResponse response, HttpSession session) throws Exception {
		return "frontServer-apiServer";
	}
	
	@RequestMapping(value = "/server", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> mainPost(Model model, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
//		 LOG.info("front accesstoken = "+ (String) session.getAttribute("accessToken"));
		Map<String, Object> data = null;
		try {
			// API 서버로 요청 (@PreAuthorize 어노테이션에 의해 checkToken 수행됨)
			// 예외: WebClientResponseException 
			// 응답코드: 400-잘못된 토큰, 401-만료된 토큰
			data = callApi("/forexApi/report/ccr", (String) session.getAttribute("tokenType"), (String) session.getAttribute("accessToken"), (String) session.getAttribute("scope"));
			//data = callApi("/user/data", (String) session.getAttribute("tokenType"), (String) session.getAttribute("accessToken"), (String) session.getAttribute("scope")); // 테스트 용
			//LOG.info(data);
			return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
		} catch (WebClientResponseException checkException) { // 토큰 문제 발생 시 수행되는 catch 문 -> 토큰 refresh 해야함
			//checkException.printStackTrace();
			try {
				
				// Body 정보 입력 (form-data) 
				// 예외: IllegalArgumentException - builder.part 메소드의 value 값이 null일 경우 발생 (토큰이 null일 경우 발생함)
				MultipartBodyBuilder builder = new MultipartBodyBuilder();
				builder.part("grant_type", "refresh_token");
				builder.part("refresh_token", (String) session.getAttribute("refreshToken"));

				// 토큰 refresh 수행
				// 결과: access_token, token_type, refresh_token 등등
				Map<String, Object> result = null;
				result = oAuth2ServerWebClient.post().uri("/auth/oauth/token").syncBody(builder.build()).retrieve()
						.bodyToMono(HashMap.class).block();
				
				// LOG.info("refresh result = " + result);
				
				String tokenType = (String) result.get("token_type");
				String accessToken = (String) result.get("access_token");
				String refreshToken = (String) result.get("refresh_token");
				String scope = (String) result.get("scope");
				
				LOG.info("session refresh in FrontServer");
				// 세션에 담긴 token 정보 수정 (인터셉터에서 처리할 수 있으나 우선 여기서 수정함)
				session.setAttribute("tokenType", tokenType);
		    	session.setAttribute("accessToken", accessToken);
		    	session.setAttribute("refreshToken", refreshToken);
		    	session.setAttribute("scope", scope);

		    	// retresh 하여 새로 받은 토큰으로 API 서버에 요청
				if(result != null) {
					data = callApi("/forexApi/report/ccr", tokenType, accessToken, scope);
					// data = callApi("/user/data", tokenType, accessToken, scope); // 테스트용 API 호출
					
					return new ResponseEntity<Map<String, Object>>(data, HttpStatus.OK);
				}else {
					return null;
				}
			} catch (WebClientResponseException refreshException) {
				// refresh 토큰 까지 없거나 만료되었을 떄 수행 -> null 리턴하여 로그아웃 처리하도록 함
				refreshException.printStackTrace();
				return null;
			}catch (IllegalArgumentException nullException) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}
	
	private Map<String, Object> callApi(String url, String tokenType, String accessToken, String scope){
		// API 서버 요청 메소드
		return oAuth2ApiWebClient.get().uri(url).headers(headers -> {
			headers.add("authorization",
					tokenType + accessToken);
			headers.add("scope", scope);
		}).retrieve().bodyToMono(HashMap.class).block();
	}
}
