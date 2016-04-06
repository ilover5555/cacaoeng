<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="result" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="total" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="lecture" scope="request" class="java.lang.Object"></jsp:useBean>
<p style="text-align: center; font-size: 20px;">Details of Classes in lecture '<span style="color: #3779ba">${lecture.book }</span>'</p>

<table class="table table-bordered">
	<thead>
		<tr>
			<th>Date</th>
			<th>Time</th>
			<th>WorkTime</th>
			<th>Status</th>
			<th>Deduct</th>
			<th>Actual</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${result }" var="vo">
		<tr>
			<td>${vo.classLog.classDateForm }</td>
			<td>${vo.oneClass.duration.rt.startTimeFormat }</td>
			<td>${vo.oneClass.duration.duration*25 }</td>
			<td>${vo.classLog.classState }</td>
			<td>${vo.deduct }</td>
			<td>${vo.workTime - vo.deduct }</td>
		</tr>
	</c:forEach>
	<tr style="font-weight: bold;">
		<td>Total</td>
		<td></td>
		<td>${total.workTime }</td>
		<td></td>
		<td>${total.deduct }</td>
		<td>${total.workTime - total.deduct }</td>
	</tr>
	</tbody>
</table>