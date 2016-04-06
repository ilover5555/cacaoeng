<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<c:choose>
	<c:when test="${null ne sessionScope.teacher}">
		<c:set var="teacher" value="${sessionScope.teacher }" scope="request"></c:set>
	</c:when>
	<c:otherwise>
		<c:redirect url="./index.jsp"></c:redirect>
	</c:otherwise>
</c:choose>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="../css/audioplayer.css" />
<script src="../js/audioplayer.js"></script>
<script>
	$(document).ready(function(){
		$( 'audio' ).audioPlayer();
	})
</script>
<style>
</style>
<title>Blue Wave Template</title>
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
        	
        	<div class="col_w880">
            	<form role="form" enctype="multipart/form-data" action="./changeTeacherInfo.do" method="post">
					<jsp:include page="TeacherInformationForm.jsp" flush="false"></jsp:include>
					<button type="submit" style="font-size:15px; font-weight: bolder;" class="btn btn-primary btn-block">Save And Exit</button>
				</form>
                <div class="cleaner"></div>
            </div>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>
	
	
</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>