<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-iecdLmaskl7CVkqkXNQ/ZH/XLlvWZOJyj7Yy7tcenmpD1ypASozpmT/E0iPtmFIB46ZmdtAc9eNBvH0H/ZpiBw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<style>
	.form-check-input:checked {
		background-color: #cd3c3c;;
		border-color: #cd3c3c;;
	}
</style>
</head>
<body>
	<my:navBar />
	<my:alert></my:alert>
	<div class="container-lg">

		<div class="row justify-content-center">
			<div class="col-12 col-md-8 col-lg-6">

				<h1><i class="fa-solid fa-eraser" style="color: #657be6;"></i> ${board.id }번게시물 수정</h1>
				<!-- enctype 작성하는 것 까먹으면 안됨@~ 파일이 올라가지 못함! -->
				<form method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${board.id }" />
					<div class="mb-3">
						<label for="titleInput" class="form-label">제목</label>
						<input class="form-control" id="titleInput" type="text" name="title" value="${board.title }" />
					</div>
					
					<!-- 그림 파일 출력 -->
					<div class="mb-3">
						<!-- foreach 때문에 id가 한 페이지에서 여러번 반복되는 것을 막기위해서 varStatus사용 --> 
						<!-- 한 id는 한 페이지에서 딱 한 번만 사용되어야 함 -->
						<c:forEach items="${board.fileName }" var="fileName" varStatus="status"> 
						<div class="mb-3">
								<div class="row">
									<div class="col-2 d-flex">
										<div class="form-check form-switch m-auto">
											<input name="removeFiles" value="${fileName }" class="form-check-input" type="checkbox" role="switch" id="removeCheckBox${status.index }">
											<label class="form-check-label" for="removeCheckBox${status.index }">
												<i class="fa-regular fa-square-minus fa-lg" style="color: #cd3c3c;"></i>
											</label>
										</div>
									</div>

							<div class="col-10">
								<!-- localhost:8080/image/게시물번호/fileName -->
								<!-- http://localhost:8080/image/412/%EB%AA%85%ED%97%8C.jpg -->
								<!-- 하드디스크에서 사진을 불러올 때 사용한 코드
								<img src="http://localhost:8080/image/${board.id }/${fileName}" class="img-thumbnail img-fluid" alt="" />
								 -->
								 <!-- 이제 s3로 연결해서 사진을 불러오니까 아래 두 줄의 코드 사용 -->
 								<img src="${bucketUrl }/${board.id }/${fileName}" class="img-thumbnail" class="img-fluid" alt="" />
							</div>
						</c:forEach>
					</div>
					
					<div class="mb-3">
						<label for="bodyTextarea" class="form-label">본문</label>
						<textarea class="form-control" id="bodyTextarea" rows="10" name="body">${board.body }</textarea>
					</div>
					<div class="mb-3">
						<label class="form-label" for="writerInput">작성자</label>
						<input class="form-control" id="writerInput" type="text" name="writer" value="${board.writer }" />
					</div>
					<div class="mb-3">
						<label for="" class="form-label">작성일시</label>
						<input class="form-control" type="text" value="${board.inserted }" readonly />
					</div>
					
					<!-- 새 그림 파일 추가 input -->
					<div class="mb-3">
						<label for="fileInput" class="form-label">그림 파일</label>
						<input class="form-control" type="file" id="fileInput" name="files" accept="image/*" multiple>
						<div class="form-text">
							총 10MB, 하나의 파일은 1MB를 초과할 수 없습니다.
						</div>
					</div>
					
					
					<div class="mb-3">
						<input class="btn btn-secondary" type="submit" value="수정" />
					</div>
				</form>
			</div>
		</div>
	</div>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js" integrity="sha512-pumBsjNRGGqkPzKHndZMaAG+bir374sORyzM3uulLV14lN5LyykqNk8eEeUlUkB3U0M4FApyaHraT65ihJhDpQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	
	<!--  
	<c:if test="${not empty param.fail}">
		<script>
			alert("게시물이 수정되지 않았습니다.");
		</script>
	</c:if>
	 -->
	
</body>
</html>