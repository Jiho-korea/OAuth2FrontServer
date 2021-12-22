package com.impconsulting.OAuth2FrontServer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;


@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OAuth2Interceptor())
                .addPathPatterns("/*")
        		.excludePathPatterns("/login");
    }
    
    @Bean
    @Qualifier("oAuth2ApiWebClient")
    public WebClient oAuth2ApiWebClient() {
		//return WebClient.builder().baseUrl("http://localhost:8088").build(); // 테스트 용 API 주소
		return WebClient.builder().baseUrl("http://127.0.0.1:8089").build();
    }
    
    @Bean
    @Qualifier("oAuth2ServerWebClient")
    public WebClient oAuth2ServerwebClient() {
    	return WebClient.builder().baseUrl("http://127.0.0.1:8099").filter(ExchangeFilterFunctions
                .basicAuthentication("oauth2-jwt-client", "pass")).build();
    }
}
