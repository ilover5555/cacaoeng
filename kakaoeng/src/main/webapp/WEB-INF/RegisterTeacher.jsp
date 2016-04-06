<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="../css/audioplayer.css" />
<script src="../js/audioplayer.js"></script>
<title>Insert title here</title>
<script>
	$(document).ready(function(){
		$('#submitButton').click(function(){
			formData = getFormData();
			$.ajax({
	            url: makeUrl('./registerTeacher.do'),
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
	})
</script>
</head>
<body>
	
	
	<div id="tooplate_wrapper">
	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	<jsp:useBean id="messages" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<String>"></jsp:useBean>
	 <div id="tooplate_main">
        <div id="tooplate_content">
	        <c:forEach var="message" items="${messages }">
				<div class="alert alert-danger"><strong>Error!</strong> ${message }</div>
			</c:forEach>
			<form role="form" enctype="multipart/form-data" action="./registerTeacher.do" method="post" accept-charset="utf-8">
				<jsp:include page="TeacherInformationForm.jsp" flush="false"></jsp:include>
				<button type="submit" class="btn btn-primary btn-block">Submit For Approval</button>
			</form>
        	
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->

	<jsp:include page="Tail.jsp" flush="false"></jsp:include>
	</div>
</body>
</html>