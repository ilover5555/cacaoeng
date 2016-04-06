<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:if test="${!lc.adminLogin and !lc.execLogin }">
	<c:redirect url="./index.jsp"></c:redirect>
</c:if>
<jsp:useBean id="active" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="classCountSet" scope="request" class="java.util.HashMap"></jsp:useBean>
<jsp:useBean id="countMap" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<title>Insert title here</title>
<style type="text/css">
#generalLectureTable th{
	vertical-align: middle;
	text-align:  center;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		<c:if test="${lc.execLogin}">
			$('select').attr('disabled', 'disabled');
			$('input').attr('disabled', 'disabled');
		</c:if>
		$('#representitiveButton').click(function(){
			var selected = [];
			$('.teacherSelectCheckBox:checked').each(function(){
				selected.push($(this).attr('data-id'))
			})
			var s ={"selected" :  JSON.stringify(selected)};
			$.ajax({
				type:'POST',
				url:makeUrl('./adminTeacherRepresentitive.edit'),
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
		$('#retirementButton').click(function(){
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
				url:makeUrl('./adminTeacherRetire.edit'),
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
					scheduleCell.showBox(scrollY);
					common.init();
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
					scheduleCell.showBox(scrollY);
					common.init();
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
		$('.rateSelect').change(function(){
			var id=$(this).attr('data-id');
			var selected = $('select[data-id="'+id+'"] > option:selected').val();

			$.ajax({
				url : makeUrl("./adminTeacherChangeRate.do?teacherId="+id+"&rate="+selected),
				method : "GET",
				dataType : "text",
				success:function(msg){
					alert(msg);
					location.reload();
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
	$(function() {
		$("#generalLectureTable").dataTable({
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

				<div>
					<div style="width: 100%">
					
						<div>
							<h1>선생님 관리</h1>
							<p style="text-align: center;">
							[선생님:${active.size() }명]
								<c:forEach items="${countMap }" var="map">
									[${map.key }그룹 : ${map.value }명]
								</c:forEach>
							</p>
						</div>
						<c:if test="${lc.adminLogin }">
						<div style="width:100%; overflow: hidden;">
							<div style="float: left;">
								<button id="representitiveButton" class="btn btn-info">대표강사</button>
								<button id="retirementButton" class="btn btn-danger">퇴사</button>
							</div>
						</div>
						</c:if>
						<table id="generalLectureTable" class="sortable table table-bordered">

							<thead>
								<tr>
									<th></th>
									<th class="no-sort" style="width:1px;"></th>
									<th class="no-sort">이름</th>
									<th style="min-width:52px;">등록일</th>
									<th>그룹</th>
									<th style="min-width: 52px;">생년월일</th>
									<th style="max-width: 40px;">Salary</th>
									<th style="min-width: 40px;">강의수</th>
									<th style="min-width: 38px;">대표<br/>강사</th>
									<th class="no-sort" style="min-width: 30px;">시간</th>
									<th class="no-sort" style="min-width: 40px;">일정표</th>
								</tr>
							</thead>

							<tbody>
							<c:forEach items="${active }" var="teacher">
								<tr>
									<td></td>
									<td style="text-align: center;"><input class="teacherSelectCheckBox" type="checkbox" data-id="${teacher.id }"></td>
									<td><a href="./adminChangeTeacherInfo.do?teacherId=${teacher.id }" target="_blank">${teacher.className }</a></td>
									<td>${teacher.registerDateForm }</td>
									<td sorttable_customkey="${teacher.rate.code }">
										<p style="display: none;">${teacher.rate }</p>
										<select class="rateSelect" data-id="${teacher.id }">
											<option value="A" <c:if test="${teacher.rate.name() eq 'A' }">selected="selected"</c:if>>A</option>
											<option value="B" <c:if test="${teacher.rate.name() eq 'B' }">selected="selected"</c:if>>B</option>
											<option value="C" <c:if test="${teacher.rate.name() eq 'C' }">selected="selected"</c:if>>C</option>
											<option value="Wait" <c:if test="${teacher.rate.name() eq 'Wait' }">selected="selected"</c:if>>Wait</option>
										</select>
									</td>
									<td>${teacher.birthFormat }</td>
									<td>${teacher.salary }</td>
									<td sorttable_customkey="${classCountSet[teacher.id].done }">
										<a class="viewLectureList" href="#" data-id="${teacher.id }">${classCountSet[teacher.id].done }/${classCountSet[teacher.id].full }</a>
									</td>
									<td><c:if test="${teacher.representitive }">대표강사</c:if></td>
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