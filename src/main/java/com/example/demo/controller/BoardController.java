package com.example.demo.controller;

import java.util.*;

import org.apache.catalina.mapper.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;
import com.example.demo.service.*;

@Controller
// @Controller를 붙인 것만으로 @Component 어노테이션을 붙인것과 같으므로 스프링이 자바빈 객체와 같이 관리해줌
@RequestMapping("/")
public class BoardController {
	
	//controller가 mapper에게 바로 일 시키지 않으니까 아래 코드는 필요 없음
	// mapper가 null이면 안되니까 controller를 통해 값을 할당(injection)받기 위해서 어노테이션 사용
	//@Autowired 
	//private BoardMapper mapper;
	
	@Autowired
	private BoardService service;
	
	// 경로1 : http://localhost:8080
	// 경로2 : http://localhost:8080/list
	// 게시물 목록
	// @RequestMapping(value = {"/", "list"}, method = RequestMethod.GET)
	@GetMapping({"/", "list"})
	public String list(Model model) {
		//1. request param 수집 / 가공
		//2. business logic 처리 : 게시물 목록 보여주기 (mapper가 일할 것임)
		List<Board> list = service.listBoard();
		//3. add attribute
		model.addAttribute("boardList", list);
		
		System.out.println(list.size());
		//4. forward / redirect
		return "list"; // 경로 1, 2둘 다 가능한데 명시적으로 list를 경로로 사용하도록
	}
	
	@GetMapping("/id/{id}")
	public String board(@PathVariable("id") Integer id, Model model) {
		// 1. request param 
		// 2. business logic : 서비스한테 일 시킴
		Board board = service.getBoard(id);
		// 3. add attribute
		model.addAttribute("board", board);
		System.out.println(board);
		// 4. forward / redirect
		return "get";
	}
	
	@GetMapping("/modify/{id}")
	public String modifyForm(@PathVariable("id") Integer id, Model model) {
		
		model.addAttribute("board", service.getBoard(id));
		return "modify";
		
	}

	//@RequestMapping(value="/modify/{id}", method=RequestMethod.POST)
	@PostMapping("/modify/{id}")
	public String modifyProcess(Board board) {

		service.modify(board);
		 return null;
	}
}
