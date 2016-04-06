<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="lectureList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script src="js/sorttable.js"></script>
<style type="text/css">
table.sortable th:not(.sorttable_sorted):not(.sorttable_sorted_reverse):not(.sorttable_nosort):after { 
    content: " \25B4\25BE" 
}
</style>
<div style="width: 850px; background-color: white;">
	<table id="generalLectureTable" class="sortable table table-bordered" style="margin: 0">
		<thead>
			<tr>
				<th class="sorttable_nosort">No</th>
				<th>강의 ID</th>
				<th>상태</th>
				<th>Start Date</th>
				<th class="sorttable_nosort">End Date</th>
				<th class="sorttable_nosort">Course</th>
				<th>Student</th>
				<th class="sorttable_nosort">총 강의수</th>
			</tr>
		</thead>

		<tbody>
		<c:forEach items="${lectureList }" var="lecture">
			<tr>
				<td>${count.index }</td>
				<td>${lecture.purchaseNumber }</td>
				<td>${lecture.status }</td>
				<td>${lecture.startDateForm }</td>
				<td>${lecture.endDateForm }</td>
				<td>${lecture.book }</td>
				<td>${lecture.student.name }</td>
				<td>
					<c:if test="${lecture.endDate eq null }">
						${lecture.fullClass }
					</c:if>
					<c:if test="${lecture.endDate ne null }">
						${lecture.done }
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		
	</table>
</div>