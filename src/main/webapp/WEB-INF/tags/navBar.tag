<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="current"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- navbar를 bootstrap의 도움을 받아 아래에서 작성했으니 이제 필요 없음
<div>
	<div>
		<a href="/list">목록</a>
	</div>
	<div>
		<a href="/add">글쓰기</a>
	</div>
</div> 
 -->

<!-- navbar와 리스트 사이의 간격 조정(mb-3) -->
<nav class="navbar navbar-expand-lg bg-dark mb-3" data-bs-theme="dark">
	<!--  <div class="container-fluid"> navbar가 적당한 위치에 오도록 아래와 같이 수정-->
	<div class="container-lg">
		<a class="navbar-brand" href="/list">
			<img src="/img/spring-logo.png" alt="" height="30">
			board
			</img>
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<!-- 필요없는 코드들도 생략 -->
				<!-- 2번째줄의 코드와 맞춰서 29-30코드 작성 -->
				<li class="nav-item">
					<a class="nav-link ${current eq 'list' ? 'active' : ''} " href="/list">목록</a>
				</li>
				<li class="nav-item">
					<a class="nav-link ${current eq 'add' ? 'active' : ''} " href="/add">글작성</a>
				</li>
				<li class="nav-item">
					<a class="nav-link ${current eq 'signup' ? 'active' : ''} " href="/member/signup">회원가입</a>
				</li>
				<li class="nav-item">
					<a class="nav-link ${current eq 'memberList' ? 'active' : ''} " href="/member/list">회원목록</a>
				</li>

			</ul>

			<!-- 링크 두 개밖에 없으니까 아래는 다 필요가 없음
				<li class="nav-item dropdown"><aclass="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"> Dropdown </a>
					<ul class="dropdown-menu">
						<li><a class="dropdown-item" href="#">Action</a></li>
						<li><a class="dropdown-item" href="#">Another action</a></li>
						<li><hr class="dropdown-divider"></li>
						<li><a class="dropdown-item" href="#">Something else here</a></li>
					</ul>
				</li>
				<li class="nav-item"><a class="nav-link disabled">Disabled</a></li> -->

			<form action="/list" class="d-flex" role="search">

				<div class="input-group">
					<select class="form-select flex-grow-0" style="width: 100px;" name="type" id="">
						<option value="all">전체</option>
						<option value="title" ${param.type eq 'title' ? 'selected' : '' }>제목</option>
						<option value="body" ${param.type eq 'body' ? 'selected' : '' }>본문</option>
						<option value="writer" ${param.type eq 'writer' ? 'selected' : '' }>작성자</option>
					</select>

					<input value="${param.search }" name="search" class="form-control" type="search" placeholder="Search" aria-label="Search">
					<button class="btn btn-outline-success" type="submit">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</div>
			</form>
		</div>
	</div>
</nav>