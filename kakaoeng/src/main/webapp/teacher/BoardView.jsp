
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="adminBoard" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Insert title here</title>
<style>
.cell{
	border: 1px solid #c0c0c0;
}
</style>
</head>
<body>
	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	
	<div class="content" style="width:600px; margin: 0 auto">
		<div style="width:100%; overflow: hidden">
			<div class="cell" style="float: left; width: 100px;"><p class="align_vertical_center" style="text-align: center;">Title</p></div>
			<div class="cell" style="float:left; width:500px;"><p class="align_vertical_center">&nbsp;&nbsp;${adminBoard.title }</p></div>
		</div>
		<div style="width:100%; overflow: hidden">
			<div class="cell" style="float: left; width:100px;"><p class="align_vertical_center" style="text-align: center;">Author</p></div>
			<div class="cell" style="float:left; width:200px;"><p class="align_vertical_center">${adminBoard.writer }</p></div>
			<div class="cell" style="float: left; width:100px;"><p class="align_vertical_center" style="text-align: center;">Date</p></div>
			<div class="cell" style="float:left; width:200px;"><p class="align_vertical_center">${adminBoard.dateForm }</p></div>
		</div>
		<div class="cell" style="width:100%">
			${adminBoard.contents }
		</div>
	</div>

	<jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>