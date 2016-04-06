<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" type="text/css" href="css/match.css" />
<link rel="stylesheet" type="text/css" href="css/popup.css" />
<title>Insert title here</title>
<script type="text/javascript">


$(document).ready(function(){
	$('#modifyButton').click(function(e){
		var parent = $('#registerPanel')
		var formData = new FormData();
		formData.append("id", $('[name="id"]', parent).val());
		formData.append("pw", $('[name="pw"]', parent).val());
		formData.append("name", $('[name="name"]', parent).val());
		formData.append("cellPhone", $('[name="cellPhone"]', parent).val());
		formData.append("address", $('[name="address"]', parent).val());
		formData.append("userType", $('select[name="userType"] > option:selected', parent).val());
		$.ajax({
            url: makeUrl('./adminAccountUpdate.do'),
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result){
            	alert(result);
            	location.reload();
            },
            error:function(xhr, status, error){
            	alert(xhr.responseText);
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
				<div id="registerPanel" class="form-group">
					<label>
						<h3>관리자/운영자 등록 및 변경</h3>
					</label>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">ID</span>
						<input type="text" class="form-control" name="id" placeholder="ID" value="${manager.id }" disabled="disabled">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">PW</span>
						<input type="text" class="form-control" name="pw" placeholder="Password">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">이름</span>
						<input type="text" class="form-control" name="name" placeholder="이름" value="${manager.name }">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">연락처</span>
						<input type="text" class="form-control" name="cellPhone" placeholder="연락처" value="${manager.cellPhone }">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">주소</span>
						<input type="text" class="form-control" name="address" placeholder="주소" value="${manager.address }">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">권한</span>
						<select class="form-control" name="userType">
							<option value="Admin" <c:if test="${manager.userType eq 'Admin' }">selected="selected"</c:if>>관리자</option>
							<option value="Executor" <c:if test="${manager.userType eq 'Executor' }">selected="selected"</c:if>>운영자</option>
						</select>
					</div>
				</div>
				<button id="modifyButton" class="btn btn-primary btn-block">Update Submit</button>
				
				<div style="height: 30px;"></div>
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