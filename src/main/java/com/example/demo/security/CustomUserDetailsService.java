package com.example.demo.security;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

@Component
// UserDetailsService라는 인터페이스를 구현한 클래스
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
	private MemberMapper mapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = mapper.selectById(username);
		
		if(member == null) {
			throw new UsernameNotFoundException(username + "회원이 없습니다.");
		}
		//username(member.getId()) = username
		
		//
		//아래의.authorities(member.getAuthority().stream().map(SimpleGrantedAuthority::new).toList())를 풀어쓴 코드
		
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		
		for(String auth : member.getAuthority()) {
			authorityList.add(new SimpleGrantedAuthority(auth));
		}
		
		//
		
		UserDetails user = User.builder()
							   .username(member.getId())
							   .password(member.getPassword())
//							   .authorities(member.getAuthority().stream().map(SimpleGrantedAuthority::new).toList())
							   .authorities(authorityList)
							   .build();
		return user;
	}
}
