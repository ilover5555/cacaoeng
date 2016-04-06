<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="unconfirmedList" scope="request" class="java.util.ArrayList"></jsp:useBean>
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

				<div>
					<div style="width: 100%">
					
						<h1>선생님 등록 승인</h1>
						<table class="table table-bordered">

							<thead>
								<tr>
									<th>이름</th>
									<th>신청일</th>
									<th>Birth</th>
									<th>University</th>
									<th>Experience</th>
									<th>email</th>
									<th></th>
								</tr>
							</thead>

							<tbody>
							<c:forEach items="${unconfirmedList }" var="teacher">
								<tr>
									<td><a href="./adminChangeTeacherInfo.do?teacherId=${teacher.id }" target="_blank">${teacher.className }</a></td>
									<td>${teacher.registerDate }</td>
									<td>${teacher.birthFormat }</td>
									<td>${teacher.univ }</td>
									<td>${teacher.experience }</td>
									<td>${teacher.id }</td>
									<td><a href="./adminTeacherConfirm.do?teacherId=${teacher.id }">승인</a> / <a href="./adminTeacherReject.do?teacherId=${teacher.id }">거절</a></td>
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