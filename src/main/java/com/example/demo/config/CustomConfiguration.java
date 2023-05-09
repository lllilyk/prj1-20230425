package com.example.demo.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.config.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.access.expression.*;

import jakarta.annotation.*;
import jakarta.servlet.*;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.*;
import software.amazon.awssdk.services.s3.*;

@Configuration
@EnableMethodSecurity
public class CustomConfiguration {
	
	@Value("${aws.accessKeyId}")
	private String accessKeyId;
	
	@Value("${aws.secretAccessKeyId}")
	private String secretAccessKeyId;
	
	@Value("${aws.bucketUrl}")
	private String bucketUrl;
	
	@Autowired
	private ServletContext application;
	
	@PostConstruct
	public void init() {
		application.setAttribute("bucketUrl", bucketUrl);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		//기본 스타일을 사용하는 코드
		//http.formLogin(Customizer.withDefaults());
		
		//직접 로그인/로그아웃 페이지를 제공하는 코드
		http.formLogin().loginPage("/member/login");
		http.logout().logoutUrl("/member/logout");
		
		/* access 메소드를 사용한 아래의 코드를 작성하면서 필요없어졌으므로 주석처리함
		// logout상태에서 글을 작성하려고 하면 login화면이 계속 나오도록 하는 코드
		// login한 사람만 add로 갈 수 있도록
		http.authorizeHttpRequests().requestMatchers("/add").authenticated();
		
		// 회원가입은 logout상태에서만 가능하도록
		http.authorizeHttpRequests().requestMatchers("/member/signup").anonymous();
		
		// add 외에 다른 곳(/**)은 아무나 들어올 수 있도록
		http.authorizeHttpRequests().requestMatchers("/**").permitAll();
		*/
		
		// 위와 같이 코드를 작성해도 되지만 좀 더 복잡한 코드를 작성하게 될 경우 아래와 같이 작성하면 좋다
		
		// https://docs.spring.io/spring-security/reference/servlet/authorization/expression-based.html
		// new WebExpressionAuto~ ( Common Built-in Expression을 참조 )
		/* 이제 MemberController에 메소드로 명시할거니까 필요없어서 주석처리함 //@PreAuthorize("isAnonymous()")
		 * 이렇게 메소드로 만들어버리면 파일을 왔다갔다할 필요가 없으니까 편리
		http.authorizeHttpRequests()
			.requestMatchers("/add")
			.access(new WebExpressionAuthorizationManager("isAuthenticated()"));
		
		http.authorizeHttpRequests()
			.requestMatchers("/member/signup")
			.access(new WebExpressionAuthorizationManager("isAnonymous()"));
		
		http.authorizeHttpRequests()
			.requestMatchers("/**")
			.access(new WebExpressionAuthorizationManager("permitAll"));
		*/
		return http.build();
	}
	
	@Bean
	public S3Client s3client() {
		AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKeyId);
		AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);
		
		S3Client s3client = S3Client.builder()
									.credentialsProvider(provider)
									.region(Region.AP_NORTHEAST_2)
									.build();
		
		return s3client;
	}
}
