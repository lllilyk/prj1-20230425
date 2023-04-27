<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<!-- c:if를 사용한 이유 = not empty 즉, 값이 있을 때만 alert창을 띄우도록 -->
	<c:if test="${not empty message }">
		<div class="container-lg">
			<div class="alert alert-warning alert-dismissible fade show" role="alert">
				${message }
				<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
			</div>
		</div>
	</c:if>