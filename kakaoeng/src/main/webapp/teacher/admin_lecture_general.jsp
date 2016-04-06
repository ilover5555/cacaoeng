<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="notRejectedList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="onGoingCount" scope="request" type="java.lang.Integer"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
$(function() {
	$("#generalLectureTable").dataTable({
		columnDefs : [{"targets":'no-sort', "orderable":false,},
		              {"targets":0, "orderable":false, "visible" : false}
		              ]		
	});
});
$(document).ready(function(){
	$('.delButton').click(function(){
		if(!confirm("이 강의 ID를 가진 패밀리 강의와\n그와 관련된 모든 기록이 삭제됩니다.\n이 작업은 영구적인 삭제를 의미합니다.\n자체적으로 되돌릴 방법이 없습니다.\n삭제 하시겠습니까?"))
			return;
		$.ajax({
			url : makeUrl("./adminRemoveFamily.do?purchaseNumber="+$(this).html()),
			method : "GET",
			dataType : "text/plain",
			success:function(msg){
				alert(msg);
				location.reload()
			},
			error: function(request,status,error){
				alert(request.responseText);
			}
		});
	})
})
</script>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">
				<h1>강의 종합</h1>

				<div>
					<div style="width: 100%">
					
						<div style="width:100%; overflow: hidden;">
							<p style="float: left;">[전체강의: ${notRejectedList.size()}개]/[강의중:${onGoingCount }개]</p>
						</div>
						<table id="generalLectureTable" class="sortable table table-bordered">

							<thead>
								<tr>
									<th></th>
									<th>강의ID</th>
									<th>상태</th>
									<th class="no-sort">Class</th>
									<th>Teacher</th>
									<th>Student</th>
									<th class="no-sort">Course</th>
									<th>Start Date</th>
									<th class="no-sort">End Date</th>
									<th class="no-sort"></th>
								</tr>
							</thead>

							<tbody>
								<c:forEach items="${notRejectedList }" var="lecture">
								<tr>
									<td></td>
									<td>
										<c:if test="${lc.adminLogin }">
											<a href="#" class="delButton">${lecture.purchaseNumber}</a>
										</c:if>
										<c:if test="${lc.execLogin }">
											${lecture.purchaseNumber}
										</c:if>
									</td>
									<td><a href="./adminLectureClassDetail.edit?purchaseNumber=${lecture.purchaseNumber }&lectureId=${lecture.id}">${lecture.status}</a></td>
									<td>${lecture.done}/${lecture.purchase.fullClass }</td>
									<td>${lecture.teacher.className }</td>
									<td>${lecture.student }</td>
									<td>${lecture.book }</td>
									<td>${lecture.startDateForm }</td>
									<td>${lecture.endDateForm }</td>
									<td>
										<c:if test="${lecture.status eq 'OnGoing' and (lc.adminLogin) }">
											<a href="./adminLectrueDetail.edit?purchaseNumber=${lecture.purchaseNumber }&lectureId=${lecture.id}">Edit</a>
										</c:if>
										<c:if test="${lecture.status ne 'OnGoing' or (lc.execLogin) }">
											<a href="./adminLectrueDetail.edit?purchaseNumber=${lecture.purchaseNumber }&lectureId=${lecture.id}">View</a>
										</c:if>
									</td>
								</tr>
								</c:forEach>
							</tbody>

						</table>
					</div>
				</div>
			</div>
			<!-- end of content -->

		</div>
		<jsp:include page="Tail.jsp" flush="false"></jsp:include>
		<!-- end of main -->
	</div>
	
</body>
</html>