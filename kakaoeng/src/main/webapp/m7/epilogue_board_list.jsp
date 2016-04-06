<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<c:set scope="page" value="epilogue" var="boardName"></c:set>
<title>Insert title here</title>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<link href="minimal/grey.css" rel="stylesheet">
<script src="icheck.js"></script>

</head>
<body style="background-color: white;">
		


		
	
		<div id="wrap">
			<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
			<div style="height:47px;"></div>
			<jsp:include page="${boardName }BoardListT.jsp" flush="false"></jsp:include>
			<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
		</div>
		
		
	
</body>
</html>