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
</head>
<body>
	<my:navBar current="list" />

	<my:alert></my:alert>

	<!-- 18번째 줄의 코드는 아래의 코드로 보여지는 모든 것들이 화면의 중앙에 오도록 하기 위해서 작성 -->
	<div class="container-lg">
		<h1>게시물 목록</h1>
		<!-- table.table>thead>tr>th*4^^tbody -->
		<table class="table">
			<thead>
				<tr>
					<th scope="col">#</th>
					<th scope="col">TITLE</th>
					<th scope="col">WRITER</th>
					<th scope="col">INSERTED</th>
				</tr>
			</thead>
			<tbody class="table-group-divider">
				<c:forEach items="${boardList}" var="board">
					<tr>
						<td>${board.id }</td>
						<td>
							<!-- path variable --> <a href="/id/${board.id }">${board.title }</a>
						</td>
						<td>${board.writer }</td>
						<td>${board.inserted }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div class="container-lg">
		<div class="row">
			<nav aria-label="Page navigation example">
				<!-- page번호를 페이지 가운데에 위치시키기 위해서 -->
				<ul class="pagination justify-content-center">
				
				<!-- 이전 버튼 -->
				
				<!-- currentPageNum이 1이 아닐때 '이전' 버튼을 보여줘라 
				<c:if test="${pageInfo.currentPageNum ne 1 }" /> -->
				
				<!-- currentPageNum이 1보다 클 때 '이전' 버튼을 보여줘라  -->
				<c:if test="${pageInfo.currentPageNum gt 1 }">
					<c:url value="/list" var="pageLink">
						<c:param name="page" value="${pageInfo.currentPageNum - 1 }"/>
					</c:url>			
					<li class="page-item">
						<a class="page-link" href="${pageLink }">
							<i class="fa-solid fa-angle-left"></i>
						</a>
					</li>
				</c:if>
				
				<!-- 계속 설정이 반복될 수 있도록 c:foreach -->
				<c:forEach begin="${pageInfo.leftPageNum }" end="${pageInfo.rightPageNum }" var="pageNum">	
					<c:url value="/list" var="pageLink">
						<c:param name="page" value="${pageNum }"/>
					</c:url>			
					<li class="page-item">
						<!-- 현재 페이지 active표시 -->
						<a class="page-link ${pageNum eq pageInfo.currentPageNum ? 'active' : '' }" href="${pageLink }">${pageNum }</a>
					</li>
				</c:forEach>
				
				<!-- 다음 버튼 -->
				<!-- currentPageNum이 lastPageNum보다 작을 때만 '다음' 버튼을 보여줘라 -->
				<c:if test="${pageInfo.currentPageNum lt pageInfo.lastPageNum }">
					<c:url value="/list" var="pageLink">
						<c:param name="page" value="${pageInfo.currentPageNum + 1 }"/>
					</c:url>			
					<li class="page-item">
						<a class="page-link" href="${pageLink }">
							<i class="fa-solid fa-angle-right"></i>
						</a>
					</li>
				</c:if>
				</ul>
			</nav>
		</div>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js" integrity="sha512-pumBsjNRGGqkPzKHndZMaAG+bir374sORyzM3uulLV14lN5LyykqNk8eEeUlUkB3U0M4FApyaHraT65ihJhDpQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<!--  
	<c:if test="${param.success eq 'remove' }">
		<script>
			alert("게시물이 삭제되었습니다.");
		</script>
	</c:if>
	 -->
	
</body>
</html>