<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="levelTestVOList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var tableSel = null;
$(function() {
	tableSel = $("#levelTestTable");
	$("#levelTestTable").dataTable({
		columnDefs : [{"targets":'no-sort', "orderable":false,},
		              {"targets":0, "orderable":false, "visible" : false}
		              ]		
	});
});
<c:if test="${lc.adminLogin}">
$(document).ready(function(){
	$('#checkAll').change(function(){
		
		if($('#checkAll:checked').length == 1){
			$.each($('.manageCheck'), function(index, value){
				value.checked=true;
			})
		}
		else{
			$.each($('.manageCheck'), function(index, value){
				value.checked=false;
			})
		}
	})
	
	$('#removeButton').click(function(){
		var checked = $('.manageCheck:checked');
		var list = [];
		checked.each(function(index, value){
			list.push($(this).attr('data-id'));
		})
		var s = {"list" : JSON.stringify(list)}
		var url = makeUrl('./adminRemoveFamily.do'	);
		$.ajax({
			type:'POST',
			url: url,
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
	
	$('.alignButton').click(function(){
		var align = $(this).attr('data-align');
		var lectureId = $(this).attr('data-id');
		var url = makeUrl('./adminLevelTestAlign.do?lectureId='+lectureId+'&align='+align);
		$.ajax({
			type:'GET',
			url: url,
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
	})
	$('.levelSelect').change(function(){
		var level = $('option:selected', $(this)).val();
		var studentId = $(this).attr('data-id');
		var url = makeUrl('./adminLevel.edit?studentId='+studentId+'&level='+level);
		$.ajax({
			type:'GET',
			url: url,
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
	})
})
	</c:if>
	
	<c:if test="${lc.execLogin}">
		$(document).ready(function(){
			$('#levelTestTable select').attr('disabled', 'disabled');
		})
	</c:if>

</script>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">
				<h1>레벨 테스트 관리</h1>
				<div>
					<div style="width: 100%">
					
						<div style="height: 30px;"></div>
						<c:if test="${lc.adminLogin }">
						<button id="removeButton" class="btn btn-danger" style="margin-bottom: 10px;">삭제</button>
						</c:if>
						
						<table id="levelTestTable" class="sortable table table-bordered">

							<thead>
								<tr>
									<th></th>
									<c:if test="${lc.adminLogin }">
										<th><input type="checkbox" id="checkAll" /></th>
									</c:if>
									<th>이름</th>
									<th>요청일</th>
									<th>신청일</th>
									<th>시간</th>
									<th>연락처</th>
									<th>SkypeID</th>
									<c:if test="${lc.adminLogin }"><th>배정</th></c:if>
									<th>Level</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${levelTestVOList }" var="vo">
									<tr>
										<td></td>
										<c:if test="${lc.adminLogin }"><td><input class="manageCheck" type="checkbox" data-id="${vo.purchase.id }" /></td></c:if>
										<td>${vo.student.name }</td>
										<td>${vo.lecture.startDateForm }</td>
										<td>${vo.purchase.purchaseDate }</td>
										<td>${vo.oneClass.duration.rt.startTimeFormat }</td>
										<td>${vo.student.cellPhone }</td>
										<td>${vo.student.skype }</td>
										<c:if test="${lc.adminLogin }">
										<td>
											<c:if test="${vo.lecture.align eq true }">
												<button class="btn btn-danger alignButton" style="padding-top: 2px; padding-bottom: 2px;" data-align="false" data-id="${vo.lecture.id }">배정철회</button>
											</c:if>
											<c:if test="${vo.lecture.align eq false }">
												<button class="btn btn-primary alignButton" style="padding-top: 2px; padding-bottom: 2px;" data-align="true" data-id="${vo.lecture.id }">배정하기</button>
											</c:if>
										</td>
										</c:if>
										<td>
											<select class="levelSelect" data-id="${vo.student.id }">
												<option value="Untested" <c:if test="${vo.student.level eq 'Untested' }">selected="selected"</c:if>>Untested</option>
												<option value="Level1" <c:if test="${vo.student.level eq 'Level1' }">selected="selected"</c:if>>Level1</option>
												<option value="Level2" <c:if test="${vo.student.level eq 'Level2' }">selected="selected"</c:if>>Level2</option>
												<option value="Level3" <c:if test="${vo.student.level eq 'Level3' }">selected="selected"</c:if>>Level3</option>
												<option value="Level4" <c:if test="${vo.student.level eq 'Level4' }">selected="selected"</c:if>>Level4</option>
												<option value="Level5" <c:if test="${vo.student.level eq 'Level5' }">selected="selected"</c:if>>Level5</option>
												<option value="Level6" <c:if test="${vo.student.level eq 'Level6' }">selected="selected"</c:if>>Level6</option>
												<option value="Level7" <c:if test="${vo.student.level eq 'Level7' }">selected="selected"</c:if>>Level7</option>
											</select>
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