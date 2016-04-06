<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="A" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="B" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="C" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="Wait" scope="request" class="java.util.ArrayList"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Insert title here</title>
</head>
<body>

	<div id="tooplate_wrapper">

	
    	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	    <div id="tooplate_main">
	    	<div id="tooplate_content">
	    	<h1>선생님 그룹 현황</h1>
	    		<div style="overflow: hidden; height: 300px;">
		    		<div class="form-group" style="float:left; width:23%; height:100%; margin-right: 2.6%">
						<label for="sel1" style="height: 20px;">A:</label>
						<select multiple class="form-control" id="A" style="height: 270px;">
							<c:forEach items="${A }" var="teacher">
								<option>${teacher.className }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group" style="float:left; width:23%; height:100%; margin-right: 2.6%">
						<label for="sel1" style="height: 20px;">B:</label>
						<select multiple class="form-control" id="B" style="height: 270px;">
							<c:forEach items="${B }" var="teacher">
								<option>${teacher.className }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group" style="float:left; width:23%;  height:100%; margin-right: 2.6%">
						<label for="sel1" style="height:20px;">C:</label>
						<select multiple class="form-control" id="C" style="height: 270px;">
							<c:forEach items="${C }" var="teacher">
								<option>${teacher.className }</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group" style="float:left; width:23%;  height:100%;">
						<label for="sel1" style="height:20px;">Wait:</label>
						<select multiple class="form-control" id="Wait" style="height: 270px;">
							<c:forEach items="${Wait }" var="teacher">
								<option>${teacher.className }</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div> <!-- end of content -->
		</div>	<!-- end of main -->
    </div>
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>