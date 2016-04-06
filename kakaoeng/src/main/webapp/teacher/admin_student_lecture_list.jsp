<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="lectureList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="absentMap" scope="request" class="java.util.HashMap"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<div style="width: 850px; background-color: white; border: 7.5px solid rgb(51,122,183); margin:7.5px;">
	<table id="teacherLectureTable" class="table table-bordered" style="margin:0">
		<thead>
			<tr>
				<th>No</th>
				<th>상태</th>
				<th>Course</th>
				<th>Start Date</th>
				<th>End Date</th>
				<th>수강횟수</th>
				<th>결강횟수</th>
				<th>선생님</th>
				<th>수업료</th>
			</tr>
		</thead>

		<tbody>
		<c:forEach items="${lectureList }" var="lecture">
			<tr>
				<td>${count.index }</td>
				<td>${lecture.status }</td>
				<td>${lecture.book }</td>
				<td>${lecture.startDateForm }</td>
				<td>${lecture.endDateForm }</td>
				<td>${lecture.fullClass }</td>
				<td>${absentMap[lecture.id] }</td>
				<td>${lecture.teacher.className }</td>
				<td>${lecture.purchase.price }</td>
			</tr>
		</c:forEach>
		</tbody>
		
	</table>
</div>