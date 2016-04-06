<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="exactMap" scope="request" class="java.util.TreeMap"></jsp:useBean>
<jsp:useBean id="similarMap" scope="request" class="java.util.TreeMap"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="minimal/grey.css" rel="stylesheet">
<script src="icheck.js"></script>
<title>Insert title here</title>
<script>
	$(document).ready(function(){
		$('.choice-similar').iCheck({
			checkboxClass:'icheckbox_minimal-grey',
			radioClass:'iradio_minimal-grey',
			increaseArea:'50%'
		});
	})
</script>
</head>
<body>
	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	<p> Exact </p>
	<c:forEach items="${exactMap}" var="item">
		<c:set value="${item.key.teacher }" var="teacher"/>
		<p>${teacher.name }</p><br/>
		<c:forEach items="${item.value }" var="resultMap">
			<c:set value="${resultMap.key }" var="request"></c:set>
			<p>Request For : ${request.duration } ${request.startDateString } ${request.endDateString } ${reqeust.weeks }weeks</p>
			<c:set value="${resultMap.value }" var="matchList"></c:set>
			<c:forEach items="${matchList }" var="match">
				<p>${match.duration }</p>
			</c:forEach>
		</c:forEach>
	</c:forEach>
	
	<p>Similar</p>
	<c:forEach items="${similarMap}" var="item">
		<c:set value="${item.key.teacher }" var="teacher"/>
		<p>${teacher.name }</p><br/>
		<c:forEach items="${item.value }" var="resultMap">
			<c:set value="${resultMap.key }" var="request"></c:set>
			<p>Request For : ${request.duration } ${request.startDateString } ${request.endDateString } ${reqeust.weeks }weeks</p>
			<c:set value="${resultMap.value }" var="matchList"></c:set>
			<c:forEach items="${matchList }" var="match">
				<p>${match.duration }</p>
			</c:forEach>
		</c:forEach>
	</c:forEach>
	
	<div class="content" style="width:960px; height:auto; margin: 0 auto">
		
	</div>
	
	<jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>