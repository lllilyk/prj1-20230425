<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="current"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
<!-- <nav class="navbar navbar-expand-lg bg-dark mb-3" data-bs-theme="dark"> -->
<nav class="navbar navbar-expand-lg  mb-3" style="background-color: #e3f2fd;">
	<!--  <div class="container-fluid"> navbar가 적당한 위치에 오도록 아래와 같이 수정-->
	<div class="container-lg">
		<a class="navbar-brand" href="/list">
			<img src="/img/hangyodong.png" alt="" height="70"></img>
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<!-- 필요없는 코드들도 생략 -->
				<!-- 2번째줄의 코드와 맞춰서 29-30코드 작성 -->
				<li class="nav-item">
					<!-- 목록 -->
					<a class="nav-link ${current eq 'list' ? 'active' : ''} " href="/list">
						<i class="fa-solid fa-list fa-xl" style="color: #657be6;"></i>
					</a>
				</li>
				<sec:authorize access="isAuthenticated()">
				<li class="nav-item">
					<!-- 글작성 -->
					<a class="nav-link ${current eq 'add' ? 'active' : ''} " href="/add">
						<i class="fa-regular fa-pen-to-square fa-xl" style="color: #657be6;"></i>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
				<li class="nav-item">
					<!-- 회원가입 -->
					<a class="nav-link ${current eq 'signup' ? 'active' : ''} " href="/member/signup">
						<i class="fa-solid fa-user-plus fa-xl" style="color: #657be6;"></i>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize access="hasAuthority('admin')">
					<li class="nav-item">
						<!-- 회원목록 -->
						<a class="nav-link ${current eq 'memberList' ? 'active' : ''} " href="/member/list">
							<i class="fa-solid fa-user-group fa-xl" style="color: #657be6;"></i>
						</a>
					</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
				<li class="nav-item">
					<!-- 마이페이지 -->
					<a class="nav-link ${current eq 'memberInfo' ? 'active' : ''} " href="/member/info?id=<sec:authentication property="name" />">
						<i class="fa-solid fa-circle-user fa-xl" style="color: #657be6;"></i>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize access="isAnonymous()">
				<li class="nav-item">
					<!-- 로그인 -->
					<a class="nav-link ${current eq 'login' ? 'active' : ''} " href="/member/login">
						<i class="fa-solid fa-right-to-bracket fa-xl" style="color: #657be6;"></i>
					</a>
				</li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="nav-item">
						<!-- 로그아웃 -->
						<a class="nav-link" href="/member/logout">
							<i class="fa-solid fa-right-from-bracket fa-xl" style="color: #657be6;"></i>
						</a>
					</li>
				</sec:authorize>
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
					<button class="btn btn-outline-info" type="submit">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</div>
			</form>
		</div>
	</div>
</nav>

<div>
	<sec:authentication property="principal" />
</div>

<!-- https://docs.spring.io/spring-security/reference/servlet/authorization/expression-based.html -->
<!-- 연습용

<div>
	<sec:authorize access="isAuthenticated()" var="loggedIn">
		로그인한 상태
	</sec:authorize>
</div>

<div>
	<sec:authorize access="isAnonymous()">
		로그아웃한 상태
	</sec:authorize>
</div>

<div>
	<sec:authorize access="${loggedIn}">
		또 로그인한 상태
	</sec:authorize>
</div>

-->









