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

@Controller
@RequestMapping("member")
public class MemberController {
	
	@Autowired
	private MemberService service;

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
	@PreAuthorize("isAuthenticated()")
	public void list(Model model) {
		List<Member> list = service.listMember();
		model.addAttribute("memberList", list);
	}
	
	// 경로 : /member/info?id=aa
	@GetMapping("info")
	//회원 정보는 로그인한 사람만 볼 수 있도록
	@PreAuthorize("isAuthenticated()")
	//public void info(@RequestParam("id") String id) {
	public void info(String id, Model model) {
		Member member = service.get(id);
		model.addAttribute("member", member);
	}
	
	@PostMapping("remove")
	//로그인한 사람만 삭제할 수 있도록
	@PreAuthorize("isAuthenticated()")
	public String remove(Member member, RedirectAttributes rttr) {
		
		boolean ok = service.remove(member);
		
		if (ok) {
			rttr.addFlashAttribute("message", "회원 탈퇴하였습니다.");
			return "redirect:/list";
		} else {
			rttr.addFlashAttribute("message", "회원 탈퇴시 문제가 발생하였습니다.");
			return "redirect:/member/info?id=" + member.getId(); 
		}
	}
	
	// 1.
	@GetMapping("modify")
	//로그인한 사람만 수정할 수 있도록
	@PreAuthorize("isAuthenticated()")
	public void modifyForm(String id, Model model) {
		Member member = service.get(id);
		model.addAttribute("member", member);
//		model.addAttribute(service.get(id));
		
	}
	
	
	// 2.
	@PostMapping("modify")
	@PreAuthorize("isAuthenticated()")
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
