package com.example.demo.service;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

// @Component
@Service // Service 역할을 하는 Component라는 의미
// 이왕이면 서비스 파일이니까 서비스로 명시적으로 어노테이션 달아주기
@Transactional(rollbackFor = Exception.class)
// class level에서 transactional 어노테이션을 붙여서 모든 exception 발생시 rollback되도록
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

	public boolean addBoard(Board board, MultipartFile[] files) throws Exception {
		// 게시물 입력(insert)
		int cnt = mapper.insert(board);
		
		/*
		 * 파일 업로드 하는 중에 문제가 생기면(파일을 올리지 않으면) Exception이 생김
		 * 게시물이 올라가지 않음 위에서 @Transactional을 작성했기때문에 rollback되므로!
		if(true) {
			throw new Exception("테스트용.. 지울것");
		}*/
		
		for(MultipartFile file : files) {
			if(file.getSize() > 0) {
				System.out.println(file.getOriginalFilename());
				System.out.println(file.getSize());
				// 파일 저장 (파일 시스템에)
				// 폴더 만들기
				String folder = "C:\\study\\upload\\" + board.getId();
				File targetFolder = new File(folder);
				if(!targetFolder.exists()) {
					targetFolder.mkdirs();
				}
				
				String path = folder + "\\" + file.getOriginalFilename();
				File target = new File(path);
				//transferTo 메소드는 exception을 던지기 때문에 위에서 throws Exception을 꼭 작성해줘야 함! 
				//addBoard메소드를 사용하는 addProcesscontroller에서도 throws Exception!
				file.transferTo(target);
				
				// db에 관련 정보 저장(insert)
				mapper.insertFileName(board.getId(), file.getOriginalFilename());
			}
		}
				
		// 일부러 fail 상황을 만들기 위해 작성한 코드
//		int cnt = 0; // 실패
		return cnt == 1;
	}

	public Map<String, Object> listBoard(Integer page, String search, String type) {
		// 페이지당 행의 수
		Integer rowPerPage = 15;

		// 쿼리 LIMIT 절에 사용할 시작 인덱스
		Integer startIndex = (page - 1) * rowPerPage;

		// 페이지네이션이 필요한 정보
		// 전체 레코드 수
		Integer numOfRecords = mapper.countAll(search, type);
		// 마지막 페이지 번호
		Integer lastPageNumber = (numOfRecords - 1) / rowPerPage + 1;
		// 페이지네이션 왼쪽번호
		Integer leftPageNum = page - 5;
		// 1보다 작을 수 없음
		leftPageNum = Math.max(leftPageNum, 1);

		// 페이지네이션 오른쪽번호
		Integer rightPageNum = leftPageNum + 9;
		// 마지막페이지보다 클 수 없음
		rightPageNum = Math.min(rightPageNum, lastPageNumber);

		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo.put("rightPageNum", rightPageNum);
		pageInfo.put("leftPageNum", leftPageNum);
		pageInfo.put("currentPageNum", page);
		pageInfo.put("lastPageNum", lastPageNumber);

		// 게시물 목록
		List<Board> list = mapper.selectAllPaging(startIndex, rowPerPage, search, type);
		
		return Map.of("pageInfo", pageInfo,
				"boardList", list);
	}
}
