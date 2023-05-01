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

	public boolean addBoard(Board board) {
		int cnt = mapper.insert(board);
		// 일부러 fail 상황을 만들기 위해 작성한 코드
		//int cnt = 0;
		return cnt == 1;
	}

	public Map<String, Object> listBoard(Integer page) {
		//페이지당 행의 수
		Integer rowPerPage = 10;

		// 쿼리 LIMIT절에 사용할 시작 인덱스
		Integer startIndex = (page - 1) * rowPerPage;
		
		// 페이지네이션이 필요한 정보
		// 전체 레코드 수
		Integer numOfRecords = mapper.countAll();
		// 마지막 페이지 번호
		Integer lastPageNumber = (numOfRecords -1) / rowPerPage + 1;
		
		// google 형식
		// pagination 왼쪽 번호
		Integer leftPageNum = page -5;
		// 1보다 작을 수 없음
		leftPageNum = Math.max(leftPageNum, 1);
		
		// pagination 오른쪽 번호
		Integer rightPageNum = leftPageNum + 9;
		// 마지막 페이지 번호보다 클 수 없음
		rightPageNum = Math.min(rightPageNum, lastPageNumber);
		
		// 컨트롤러에 numOfRecords, lastPageNumber, leftPageNum, rightPageNum을
		// 넘겨주기 위한 방법(데이터를 저장하는 방법)으로는 
		// javabean이용 또는 map사용 등등이 있는데 여기서는 map을 사용
		
		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("rightPageNum", rightPageNum);
		pageInfo.put("leftPageNum", leftPageNum);
		// 현재 페이지를 active 상태로 만들기 위해서 currentPageNum 추가
		pageInfo.put("currentPageNum", page);
		pageInfo.put("lastPageNum", lastPageNumber);
		
		// 게시물 목록
		List<Board> list = mapper.selectAllPaging(startIndex, rowPerPage);
		
		return Map.of("pageInfo", pageInfo,
					  "boardList", list);
	}
}
