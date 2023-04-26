package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

// @Component
@Service //Service 역할을 하는 Component라는 의미 
// 이왕이면 서비스 파일이니까 서비스로 명시적으로 어노테이션 달아주기
public class BoardService {
	// controller가 service한테 일을 시키면
	// service가 mapper한테 일을 시킴
	// mapper가 일을 주입받아야함 
	
	@Autowired
	private BoardMapper mapper;
	
	public List<Board> listBoard() {
		List<Board> list = mapper.selectAll();
		return list;
	}

	public Board getBoard(Integer id) {
		return mapper.selectById(id);
	}

	public boolean modify(Board board) {
		int cnt = mapper.update(board);
		
		return cnt == 1;
	}

	public boolean remove(Integer id) {
		int cnt = mapper.deleteById(id);
		return cnt == 1;
	}
}
