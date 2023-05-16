package com.example.demo.service;

import java.io.*;
import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.multipart.*;

import com.example.demo.domain.*;
import com.example.demo.mapper.*;

import software.amazon.awssdk.core.sync.*;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;

// @Component
@Service // Service 역할을 하는 Component라는 의미
// 이왕이면 서비스 파일이니까 서비스로 명시적으로 어노테이션 달아주기
@Transactional(rollbackFor = Exception.class)
// class level에서 transactional 어노테이션을 붙여서 모든 exception 발생시 rollback되도록
public class BoardService {
	
	@Autowired
	private S3Client s3;
	
	@Value("${aws.s3.bucketName}")
	private String bucketName;
	
	// controller가 service한테 일을 시키면
	// service가 mapper한테 일을 시킴
	// mapper가 일을 주입받아야함
	
	@Autowired
	private BoardMapper mapper;
	
	@Autowired
	private BoardLikeMapper likeMapper;

	public List<Board> listBoard() {
		List<Board> list = mapper.selectAll();
		return list;
	}

	public Board getBoard(Integer id, Authentication authentication) {
		Board board = mapper.selectById(id);

		
		// 현재 로그인한 사람이 이 게시물에 좋아요 했는지?
		if (authentication != null) {
			Like like = likeMapper.select(id, authentication.getName());
			if (like != null) {
				board.setLiked(true);
			}
		}
		
		return board;
	}

	public boolean modify(Board board, MultipartFile[] addFiles, List<String> removeFileNames) throws Exception {
		// FileName 테이블 삭제
		if(removeFileNames != null && !removeFileNames.isEmpty()) {
			for(String fileName : removeFileNames) {
				
				// s3에서 파일(객체) 삭제
				String objectKey = "board/" + board.getId() + "/" + fileName;
				DeleteObjectRequest dor = DeleteObjectRequest.builder()
						.bucket(bucketName)
						.key(objectKey)
						.build();
				
				s3.deleteObject(dor);
				
				/* s3와 연결한 이후로는 필요 없음
				// 하드디스크에서 삭제
				String path = "C:\\study\\upload\\" + board.getId() + "\\" + fileName;
				File file = new File(path);
				if(file.exists()) {
					file.delete();
				} */
				
				// 테이블에서 삭제
				mapper.deleteFileNameByBoardIdAndFileName(board.getId(), fileName);
			}
		}
		
		// 새 파일 추가 : 위의 코드때문에 새 파일을 추가하면 자동으로 기존 파일은 삭제됨
		for(MultipartFile newFile : addFiles) {
			if(newFile.getSize() > 0) {
				// 테이블에 파일명 추가
				mapper.insertFileName(board.getId(), newFile.getOriginalFilename());
				
				// s3에 파일(객체) 업로드
				String objectKey = "board/" + board.getId() + "/" + newFile.getOriginalFilename();
				PutObjectRequest por = PutObjectRequest.builder()
						.acl(ObjectCannedACL.PUBLIC_READ)
						.bucket(bucketName)
						.key(objectKey)
						.build();
				RequestBody rb = RequestBody.fromInputStream(newFile.getInputStream(), newFile.getSize());
				
				s3.putObject(por, rb);
				
				/* s3와 연결한 이후로 필요없음
				String fileName = newFile.getOriginalFilename();
				String folder = "C:\\study\\upload\\" + board.getId(); 
				String path = folder + "\\" + fileName; 
			
				// 디렉토리 없으면 만들기
				File dir = new File(folder);
				if(!dir.exists()) {
					dir.mkdirs(); //mkdirs : 새로운 디렉토리를 만드는 메소드
				}
				
				// 파일을 하드디스크에 저장	
				File file = new File(path);
				newFile.transferTo(file);
				*/
			}
		}
		
		// 게시물(Board) 테이블 수정
		int cnt = mapper.update(board);

		return cnt == 1;
	}

	public boolean remove(Integer id) {
		//파일명 조회
		List<String> fileNames = mapper.selectFileNameByBoardId(id);
		
		//FileName 테이블의 데이터 지우기
		mapper.deleteFileNameByBoardId(id);
		
		// s3 bucket의 파일(객체) 지우기
		for (String fileName : fileNames) {
			String objectKey = "board/" + id + "/" + fileName;
			DeleteObjectRequest dor = DeleteObjectRequest.builder()
														 .bucket(bucketName)
														 .key(objectKey)
														 .build();
			s3.deleteObject(dor);
		}
		
		
		/* s3와 연결했으므로 이제 필요없는 코드
		//하드디스크의 파일 지우기
		for (String fileName : fileNames) {
			String path = "C:\\study\\upload\\" + id + "\\" + fileName;
			File file = new File(path);
			if(file.exists()) {
				file.delete();
			}
		}
		*/
		
		//게시물 테이블의 데이터 지우기
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
				
				String objectKey = "board/" + board.getId() + "/" + file.getOriginalFilename();
				
				PutObjectRequest por = PutObjectRequest.builder()
													   .bucket(bucketName)
													   .key(objectKey)
													   .acl(ObjectCannedACL.PUBLIC_READ)
													   .build();
			
				RequestBody rb = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
				
				s3.putObject(por, rb);
				
				/* s3와 연결한 이후로는 하드디스크와의 연결은 필요없어짐
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
				*/
				
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

	
	
	public void removeByWriter(String writer) {
		List<Integer> idList = mapper.selectBoardIdByWriter(writer);
		
		for(Integer id : idList) {
			remove(id);
		}
	
	}

	public Map<String, Object> like(Like like, Authentication authentication) {
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("like", false);
		
		like.setMemberId(authentication.getName());
		Integer deleteCnt = likeMapper.delete(like);
		
		if(deleteCnt != 1) {
			Integer insertCnt = likeMapper.insert(like);
			result.put("like", true);
		}
		Integer count = likeMapper.countByBoardId(like.getBoardId());
		result.put("count", count);
		return result;
	}

	public Board getBoard(Integer id) {

		return getBoard(id, null);
	}
}
