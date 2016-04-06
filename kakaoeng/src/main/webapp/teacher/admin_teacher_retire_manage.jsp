<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="retireList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="classCountSet" scope="request" class="java.util.HashMap"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script src="js/sorttable.js"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<link rel="stylesheet" type="text/css" href="css/match.css" />
<link rel="stylesheet" type="text/css" href="css/popup.css" />
<script src="js/scheduleCell.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
	$(document).ready(function(){
		$('#backButton').click(function(){
			var selected = [];
			$('.teacherSelectCheckBox:checked').each(function(){
				selected.push($(this).attr('data-id'))
			})
			var s ={"selected" :  JSON.stringify(selected)};
			$.ajax({
				type:'POST',
				url:makeUrl('./adminTeacherBack.do'),
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
		$('#removeButton').click(function(){
			var selected = [];
			$('.teacherSelectCheckBox:checked').each(function(){
				selected.push($(this).attr('data-id'))
			})
			var s ={
				"selected" :  JSON.stringify(selected),
				"retire" : "true"
				};
			
			$.ajax({
				type:'POST',
				url:makeUrl('./adminTeacherReject.do'),
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
		$('.teacherAvail').click(function(){
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./adminViewTeacherAvail.view?teacherId="+$(this).attr('data-id')),
				method : "GET",
				dataType : "html",
				success:function(msg){
					$('#light').html(msg);
					scheduleCell.showBox(scrollY, common.init);
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
		$('.teacherStatus').click(function(){
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./adminViewTeacherStatus.view?teacherId="+$(this).attr('data-id')),
				method : "GET",
				dataType : "html",
				success:function(msg){
					$('#light').html(msg);
					scheduleCell.showBox(scrollY, common.init);
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
		$('.viewLectureList').click(function(){
			var id = $(this).attr('data-id');
			
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./adminTeacherLectureList.view?teacherId="+id),
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
	})
</script>
<style type="text/css">
table.sortable th:not(.sorttable_sorted):not(.sorttable_sorted_reverse):not(.sorttable_nosort):after { 
    content: " \25B4\25BE" 
}
</style>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">

				<div>
					<div style="width: 100%">
					
						<h1>퇴사 관리</h1>
					
						<div style="width:100%; overflow: hidden;">
							<div style="float: left;">
								<button id="backButton">복귀</button>
								<button id="removeButton">삭제</button>
							</div>
							<div style="float: right;">
								<form method="get" action="./adminTeacherRetireManage.do">
								<label for="searchText">이름:</label>
								<input name="searchText" value="${param.searchText }"/>
								<button type="submit">검색</button>
								</form>
							</div>
						</div>
						<table id="generalLectureTable" class="sortable table table-bordered">

							<thead>
								<tr>
									<th class="sorttable_nosort">선택</th>
									<th class="sorttable_nosort">이름</th>
									<th>등록일</th>
									<th>Last Login</th>
									<th>그룹</th>
									<th>생년월일</th>
									<th>Salary</th>
									<th>강의수</th>
									<th class="sorttable_nosort">가능 시간</th>
									<th class="sorttable_nosort">일정표</th>
								</tr>
							</thead>

							<tbody>
							<c:forEach items="${retireList }" var="teacher">
								<tr>
									<td><input class="teacherSelectCheckBox" type="checkbox" data-id="${teacher.id }"></td>
									<td><a href="./adminChangeTeacherInfo.do?teacherId=${teacher.id }" target="_blank">${teacher.className }</a></td>
									<td>${teacher.registerDateForm }</td>
									<td>${teacher.lastLogin }</td>
									<td>${teacher.rate }</td>
									<td>${teacher.birthFormat }</td>
									<td>${teacher.salary }</td>
									<td sorttable_customkey="${classCountSet[teacher.id].done }">
										<a class="viewLectureList" href="#" data-id="${teacher.id }">${classCountSet[teacher.id].done }/${classCountSet[teacher.id].full }</a>
									</td>
									<td><a class="teacherAvail" href="#" data-id="${teacher.id }">보기</a></td>
									<td><a class="teacherStatus" href="#" data-id="${teacher.id }">보기</a></td>
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