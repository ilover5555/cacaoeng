<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="studentList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="countMap" scope="request" class="java.util.HashMap"></jsp:useBean>
<jsp:useBean id="stateMap" scope="request" class="java.util.HashMap"></jsp:useBean>
<jsp:useBean id="totalStudent" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="onGoing" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="hasLog" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var sendObj = null;
function receiveMessage(event)
{	sendObj["popup"].postMessage(sendObj["sendData"], '*');
}
window.addEventListener("message", receiveMessage, false);
	$(document).ready(function(){
		$('.studentLectureList').click(function(){
			
			var id = $(this).attr('data-id');
			
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./adminStudentLectureList.view?studentId="+id),
				method : "GET",
				dataType : "html",
				success:function(msg){
					$('#light').html(msg);
					scheduleCell.showBox(scrollY);
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
		<c:if test="${lc.adminLogin}">
		$('#deleteButton').click(function(){
			if(!confirm("선택된 학생들을 삭제합니다.\n정말로 삭제하시겠습니까?"))
				return;
			
			var selected = [];
			$('.studentSelectCheckBox:checked').each(function(){
				selected.push($(this).attr('data-id'))
			})
			var s ={"selected" :  JSON.stringify(selected)};
			
			$.ajax({
				type:'POST',
				url:makeUrl('./adminDeleteStudent.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		</c:if>
		$('#sendSMS').click(function(){
			var selected = [];
			$('.studentSelectCheckBox:checked').each(function(){
				selected.push($(this).attr('data-id'))
			})
			var s ={"selected" :  JSON.stringify(selected)};
			$.ajax({
				type:'POST',
				url:makeUrl('./adminRequestSMSPopup.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					var result = JSON.parse(data);
					var sendList = result["sendList"];
					var selectedJSON = result["selectedJSON"];
					var e = window.open(makeUrl("./admin_sms_popup.jsp"), "", "width=550, height=300, resizable=0, scrollbars=0, status=0;");
					sendObj = {"sendData" : data, "popup" : e};
					
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
			
			
			return false;
		})
	})
	
	$(function() {
		$("#generalStudentTable").dataTable({
			columnDefs : [{"targets":'no-sort', "orderable":false,},
			              {"targets":0, "orderable":false, "visible" : false}
			              ]		
		});
	});

</script>
</head>
<body>

	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">
				<h1>학생 관리</h1>
			
				<div>
					<div style="width: 100%">
					
						<div style="width:100%; overflow: hidden;">
						<c:if test="${lc.adminLogin }">
							<button id="deleteButton" class="btn btn-danger" style="float:left; margin-right: 10px; padding-top: 1px; padding-bottom: 1px;">삭제</button>
						</c:if>
							<button id="sendSMS" class="btn btn-info" style="float:left; margin-right: 10px; padding-top: 1px; padding-bottom: 1px;">SMS</button>
							<p style="float: left;">[회원수: ${totalStudent}명]/[수강중:${onGoing }개]/[이력회원:${hasLog }개]</p>
						</div>
						<table id="generalStudentTable" class="table table-bordered">

							<thead>
								<tr>
									<th></th>
									<th class="no-sort">선택</th>
									<th>상태</th>
									<th class="no-sort">등록일</th>
									<th>이름</th>
									<th class="no-sort">SkypeID</th>
									<th>생년월일</th>
									<th class="no-sort">연락처</th>
									<th>수강 횟수</th>
								</tr>
							</thead>

							<tbody>
							<c:forEach items="${studentList }" var="student">
								<tr>
									<td></td>
									<td><input class="studentSelectCheckBox" type="checkbox" data-id="${student.id }" /> </td>
									<td><a class="studentLectureList" href="#" data-id="${student.id}">${stateMap[student.id] }</a></td>
									<td>${student.registerDateForm }</td>
									<td>
										<a  href="./adminChangeStudentInformation.edit?studentId=${student.id }">
										<c:if test="${student.name ne '' }">${student.name }</c:if>
										<c:if test="${student.name eq '' }">${student.className }</c:if>
										</a>
									</td>
									<td>${student.skype }</td>
									<td>${student.birthFormat }</td>
									<td>${student.cellPhone }</td>
									<td>${countMap[student.id] }</td>
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
	
	<div id="light" class="white_content"></div>
	<div id="fade" class="black_overlay"></div>
</body>
</html>