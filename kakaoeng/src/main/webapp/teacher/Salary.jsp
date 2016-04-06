
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	<div class="content" style="width:960px; height:1000px; margin: 0 auto">
		<table style="text-align: center;" class="table table-bordered">
			<thead>
				<tr>
					<th style="width: 50px; text-align: center;"><p class="center_text">No</p></th>
					<th style="text-align: center;"><p class="center_text">Lecture</p></th>
					<th style="width:150px;text-align: center;"><p class="center_text">Student</p></th>
					<th style="width:50px;text-align: center;"><p class="center_text">Class</p></th>
					<th style="width:60px;text-align: center;"><p class="align_vertical_center">Work<br/>Time</p></th>
					<th style="width:60px;text-align: center;"><p class="center_text">Deduct</p></th>
					<th style="width:60px;text-align: center;"><p class="center_text">Actual</p></th>
					<th style="width:60px;text-align: center;"><p class="center_text">Hour</p></th>
					<th style="width:60px;text-align: center;"><p class="center_text">Pay/h</p></th>
					<th style="width:110px;text-align: center;"><p class="center_text">Payment</p></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="adminBoard" items="${adminBoardList }">
					<tr>
						<td>${count.index }</td>
						<td>Let's Go</td>
						<td>Sunny Kim</td>
						<td>13</td>
						<td>325</td>
						<td>50</td>
						<td>275</td>
						<td>4.58</td>
						<td>220</td>
						<td>1007.6</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>