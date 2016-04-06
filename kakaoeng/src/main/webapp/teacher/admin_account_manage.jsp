<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" type="text/css" href="css/match.css" />
<link rel="stylesheet" type="text/css" href="css/popup.css" />
<link rel="stylesheet" type="text/css" href="css/font-awesome.min.css" />
<script src="js/scheduleCell.js"></script>
<script src="js/bootstrap-filestyle.min.js"></script>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<title>Insert title here</title>
<style type="text/css">
	.bootstrap-filestyle{
		width: 100%;
	}
</style>
<script type="text/javascript">


$(document).ready(function(){
	
	$('.deleteButton').click(function(){
		if(!confirm("해당 계정을 삭제하시겠습니까?"))
			return;
		var id = $(this).attr('data-id');
		
		$.ajax({
				type:'GET',
				url:makeUrl('./adminAccountDelete.do?id='+id),
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
	
	$('.modifyButton').click(function(){
		var id = $(this).attr('data-id');
		var course = $('option:selected',$('#course'+id)).val();
		var level = $('option:selected', $('#level'+id)).val();
		var title = $('#title'+id).val();
		var s = {"bookId":id,"course":course, "level":level, "title":title};
		$.ajax({
			type:'POST',
			url:makeUrl('./adminBookUpdate.do'),
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
	
	$('#registerButton').click(function(e){
		var parent = $('#registerPanel')
		var formData = new FormData();
		formData.append("id", $('[name="id"]', parent).val());
		formData.append("pw", $('[name="pw"]', parent).val());
		formData.append("name", $('[name="name"]', parent).val());
		formData.append("cellPhone", $('[name="cellPhone"]', parent).val());
		formData.append("address", $('[name="address"]', parent).val());
		formData.append("userType", $('select[name="userType"] > option:selected', parent).val());
		$.ajax({
            url: makeUrl('./adminAccountRegister.do'),
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result){
            	location.reload();
            },
            error:function(xhr, status, error){
            	alert(xhr.responseText);
            }
        });
	})
	
	$(":file").filestyle();
	
	$('.bootstrap-filestyle').each(function(index, value){
		var parent = $(this).parent();
		var file = $(':file', parent);
		var init = file.attr('data-init');
		var display = $('input', $(this));
		display.val(init);
	})
})

$(function() {
		$("#accountTable").dataTable({
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
				<div id="registerPanel" class="form-group">
					<label>
						<h3>관리자/운영자 등록 및 변경</h3>
					</label>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">ID</span>
						<input type="text" class="form-control" name="id" placeholder="ID">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">PW</span>
						<input type="text" class="form-control" name="pw" placeholder="Password">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">이름</span>
						<input type="text" class="form-control" name="name" placeholder="이름">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">연락처</span>
						<input type="text" class="form-control" name="cellPhone" placeholder="연락처">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">주소</span>
						<input type="text" class="form-control" name="address" placeholder="주소">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">권한</span>
						<select class="form-control" name="userType">
							<option value="Admin">관리자</option>
							<option value="Executor">운영자</option>
						</select>
					</div>
				</div>
				<button id="registerButton" class="btn btn-primary btn-block">Submit</button>
				
				<div style="height: 30px;"></div>
				<table id="accountTable" class="table table-bordered" width="1300px;">
					<thead>
						<tr>
							<th></th>
							<th>권한</th>
							<th>아이디</th>
							<th>이름</th>
							<th>연락처</th>
							<th>주소</th>
							<th class="no-sort"></th>
						</tr>
					</thead>

					<tbody>
					<c:forEach items="${managerList }" var="manager">
						<tr>
							<td></td>
							<td>${manager.userType }</td>
							<td>${manager.id }</td>
							<td>${manager.name }</td>
							<td>${manager.cellPhone }</td>
							<td>${manager.address }</td>
							<td><a href="./adminAccountUpdate.do?id=${manager.id }" target="_blank">수정</a>/<a href="#" data-id="${manager.id }" class="deleteButton">삭제</a></td>
						</tr>
					</c:forEach>
					</tbody>

				</table>
			</div>
			<!-- end of content -->

		</div>
		<jsp:include page="Tail.jsp" flush="false"></jsp:include>
		
		<div id="light" class="white_content"></div>
		<div id="fade" class="black_overlay"></div>
		<!-- end of main -->
	</div>
</body>
</html>