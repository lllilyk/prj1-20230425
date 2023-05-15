package com.example.demo.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import com.example.demo.domain.*;
import com.example.demo.service.*;

import jakarta.servlet.http.*;

@Controller
@RequestMapping("member")
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	//{id}는 pathvariable
	@GetMapping("checkId/{id}")
	@ResponseBody
//	응답하는 것이 json형식이어야 하니까 responsebody 붙여주기
	public Map<String, Object> checkId(@PathVariable("id") String id){
		return service.checkId(id);
	}

	@GetMapping("checkNickName/{nickName}")
	@ResponseBody
//	응답하는 것이 json형식이어야 하니까 responsebody 붙여주기
	public Map<String, Object> checkNickName(@PathVariable("nickName") String nickName) {
		return service.checkNickName(nickName);
	}
	
	@GetMapping("checkEmail/{email}")
	@ResponseBody
	public Map<String, Object> checkEmail(@PathVariable("email") String email) {
		return service.checkEmail(email);
	}
	
	@GetMapping("signup")
	//get방식으로 signup 경로로 들어가는 경우에는 login이 안 되어있는 경우에만 가능하도록!
	@PreAuthorize("isAnonymous()")
	public void signupForm() {
		
	}
	
	@GetMapping("login")
	public void loginForm() {
		
	}
	
	@PostMapping("signup")
	@PreAuthorize("isAnonymous()")
	public String signupProcess(Member member, RedirectAttributes rttr) {
	
		try {service.signup(member);
			rttr.addFlashAttribute("message", "회원 가입되었습니다.");
			return "redirect:/list";
		} catch(Exception e) {
			e.printStackTrace();
			rttr.addFlashAttribute("member", member);
			rttr.addFlashAttribute("message", "회원 가입 중 문제가 발생했습니다.");
			return "redirect:/member/signup";
		}
	}
	
	@GetMapping("list")
	@PreAuthorize("hasAuthority('admin')")
	public void list(Model model) {
		List<Member> list = service.listMember();
		model.addAttribute("memberList", list);
	}
	
	// 경로 : /member/info?id=aa
	@GetMapping("info")
	// 회원 정보는 로그인한 id와 동일한 사람것만 볼 수 있도록
	// and가 or보다 우선순위가 높으므로 괄호()는 편의를 위해서 붙이는 것일 뿐
	@PreAuthorize("hasAuthority('admin') or (isAuthenticated() and (authentication.name eq #id))")
	//public void info(@RequestParam("id") String id) {
	public void info(String id, Model model) {
		Member member = service.get(id);
		model.addAttribute("member", member);
	}
	
	@PostMapping("remove")
	//member 테이블의 id와 로그인한 id가 같은 사람만 탈퇴할 수 있도록
	@PreAuthorize("isAuthenticated() and (authentication.name eq #member.id)")
	public String remove(Member member, 
						 RedirectAttributes rttr,
						 HttpServletRequest request) throws Exception {
		
		boolean ok = service.remove(member);
		
		if (ok) {
			rttr.addFlashAttribute("message", "회원 탈퇴하였습니다.");
			
			// 탈퇴하면 바로 로그아웃되도록
			request.logout();
			
			return "redirect:/list";
		} else {
			rttr.addFlashAttribute("message", "회원 탈퇴시 문제가 발생하였습니다.");
			return "redirect:/member/info?id=" + member.getId(); 
		}
	}
	
	// 1.
	@GetMapping("modify")
	//로그인한 id와 동일한 경우에만 수정이 가능하도록
	@PreAuthorize("isAuthenticated() and (authentication.name eq #id)")
	public void modifyForm(String id, Model model) {
		Member member = service.get(id);
		model.addAttribute("member", member);
//		model.addAttribute(service.get(id));
		
	}
	// 2.
	@PostMapping("modify")
	//로그인한 id와 동일한 경우에만 수정이 가능하도록
	@PreAuthorize("isAuthenticated() and (authentication.name eq #member.id)")
	public String modifyProcess(Member member, String oldPassword, RedirectAttributes rttr) {
		boolean ok = service.modify(member, oldPassword);
		
		if (ok) {
			rttr.addFlashAttribute("message", "회원 정보가 수정되었습니다.");
			return "redirect:/member/info?id=" + member.getId();
		} else {
			rttr.addFlashAttribute("message", "회원 정보 수정시 문제가 발생하였습니다.");
			return "redirect:/member/modify?id=" + member.getId();
		}
	}
}
