const toast = new bootstrap.Toast(document.querySelector("#liveToast"));

$("#likeIcon").click(function() {
	// 게시물 번호 request body에 추가
	const boardId = $("#boardIdText").text().trim();
	// const data = {boardId : boardId};
	const data = {boardId}; //위 코드와 같은 코드(생략가능)
	
	$.ajax("/like", {
		method: "post",
		contentType: "application/json",
		data: JSON.stringify(data),
		
		success: function(data) {
			if (data.like) {
				// 꽉찬 하트
				$("#likeIcon").html(`<i class="fa-solid fa-heart"></i>`);
			} else {
				// 빈 하트
				$("#likeIcon").html(`<i class="fa-regular fa-heart"></i>`);
			}
			//좋아요 수 업데이트
			$("#likeNumber").text(data.count);
		},
		error: function(jqXHR){
			//console.log("좋아요 실패");
			//console.log(jqXHR);
			//console.log(jqXHR.responseJSON);
			//아래 코드는 html에 출력되는 것을 확인하기 위한 코드
			//$("body").prepend(jqXHR.responseJSON.message);
			$(".toast-body").text(jqXHR.responseJSON.message);
			toast.show(); 
		},
		//complete:,
	});
});