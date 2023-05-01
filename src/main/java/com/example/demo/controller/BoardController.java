package com.example.demo.controller;

import java.util.*;

import org.apache.catalina.mapper.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

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
	
	// 경로1 : http://localhost:8080?page=3
	// 경로2 : http://localhost:8080/list?page=5
	// 게시물 목록
	// @RequestMapping(value = {"/", "list"}, method = RequestMethod.GET)
	@GetMapping({"/", "list"})
	//페이지 번호가 쿼리스트링으로 붙으니까 Integer page를 넣어주는데
	//1페이지의 경우에는 queryString에 ?page=1이렇게 붙지 않으니까
	//그럼에도 불구하고 default value를 얻어 페이지를 잘 보여줄 수 있도록 
	//@RequestParam 파라미터에 (value="page", defaultValue = "1")를 작성
	public String list(Model model, 
				@RequestParam(value="page", defaultValue = "1") Integer page) {
		//1. request param 수집 / 가공
		
		//2. business logic 처리 : 게시물 목록 보여주기 (mapper가 일할 것임)
		
		//아래의 코드는 @RequestParam에 defaultValue를 추가함으로써 필요없어짐
		//<Board> list = service.listBoard(); //page 처리 전
		List<Board> list = service.listBoard(page); //page처리 후
		
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
	public String modifyProcess(Board board, RedirectAttributes rttr) {

		boolean ok = service.modify(board);
		
		if(ok) {
			// 해당 게시물 보기로 리디렉션
			// RedirectAttributes를 사용하면 쿼리스트링에 붙어서 넘어감
			//rttr.addAttribute("success", "success");
			
			rttr.addFlashAttribute("message", board.getId() + "번 게시물이 수정되었습니다.");
			return "redirect:/id/" + board.getId();
		}else {
			// 수정form으로 리디렉션
			//rttr.addAttribute("fail", "fail");
			
			rttr.addFlashAttribute("message", board.getId()+"번 게시물이 수정되지 않았습니다.");
			return "redirect:/modify/" + board.getId();
		}
	}
	
	@PostMapping("remove")
	public String remove(@RequestParam Integer id, RedirectAttributes rttr) {
		boolean ok = service.remove(id);
		if(ok) {
			//addAttribute로 쿼리 스트링에 붙도록 했는데 이렇게 하면 
			//새로고침할 때 같은 "삭제되었습니다" 알림창이 계속 뜸
			//rttr.addAttribute("success", "remove");
			
			//그래서 변경 > 모델에 추가
			//삭제 후 list.jsp에서 20번째 코드에 의해서 list목록에서 확인가능
			rttr.addFlashAttribute("message", id + "번 게시물이 삭제되었습니다.");
			return "redirect:/list";
		} else {
			return "redirect:/id/" + id;
		}
	}
	
	@GetMapping("add")
	public void addForm() {
		//게시물 작성 form(view)로 포워드
		
	}
	
	@PostMapping("add")
	public String addProcess(Board board, RedirectAttributes rttr) {
		// 새 게시물 db에 추가
		// 1. 
		// 2.
		boolean ok = service.addBoard(board);
		// 3.
		if(ok) {
			rttr.addFlashAttribute("message", board.getId() + "번 게시물이 등록되었습니다.");
			return "redirect:/id/" + board.getId();
			// Auto_Increment 설정때문에 삭제해도 Id값은 계속 1씩 증가
			// 만약 모르는 여러 사람이 여러 게시글을 작성한다고 할 때,
			// 동일한 게시물을 동일한 이름으로 작성하면
			// 누가 몇 번째 id의 게시물을 작성했는지 알수가 없음
			// 따라서 Controller에 이렇게 작성하고 끝내버리면
			// Controller에게 요청을 받아서 일해야 하는 Mapper는 
			// Id값을 모르니까 null오류가 발생
			// BoardMapper.jsp에서 @Options어노테이션을 사용해서 
			// 자동증가하는 키값을 사용하겠다고 설정해주기!
		} else {
			rttr.addFlashAttribute("message", "게시물 등록 중 문제가 발생하였습니다.");
			rttr.addFlashAttribute("board", board);
			// 문제가 발생한 경우에는 다시 입력창으로 돌아가도록!
			return "redirect:/add";
		}
		// 4.
	}
	

}
