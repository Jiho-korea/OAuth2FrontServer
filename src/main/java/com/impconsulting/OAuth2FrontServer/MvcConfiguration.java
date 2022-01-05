package com.impconsulting.OAuth2FrontServer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OAuth2Interceptor())
                .addPathPatterns("/*")
                .addPathPatterns("/front/*")
        		.excludePathPatterns("/front/login/");
    }
    
    @Bean
    @Qualifier("oAuth2ApiWebClient")
    public WebClient oAuth2ApiWebClient() {
		//return WebClient.builder().baseUrl("http://localhost:8088").build(); // 테스트 용 API 주소
		return WebClient.builder().baseUrl("http://localhost:8089").build();
    }
    
    @Bean
    @Qualifier("oAuth2ServerWebClient")
    public WebClient oAuth2ServerwebClient() {
    	return WebClient.builder().baseUrl("http://localhost:8099").filter(ExchangeFilterFunctions
                .basicAuthentication("oauth2-jwt-client", "pass")).build();
    }
}
