<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="result" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="year" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="month" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="end" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="USD" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="total" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td{
	padding-top: 1px !important;
	padding-bottom: 1px !important;
}
</style>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>

<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script>
	$(document).ready(function(){
		$('.payButton').click(function(){
			var id = $(this).attr('data-id');
			$.ajax({
				type:'GET',
				url:makeUrl('./admimUpdatePayed.do?month=${month}&payed=true&teacherId='+id),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		$('.backButton').click(function(){
			var id = $(this).attr('data-id');
			$.ajax({
				type:'GET',
				url:makeUrl('./admimUpdatePayed.do?month=${month}&payed=false&teacherId='+id),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		$('#saveUSD').click(function(){
			var USD = Number($('input[ng-model="USD"]').val());
			$.ajax({
				type:'GET',
				url:makeUrl('./saveUSD.do?USD='+USD),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
	})
</script>
<title>Blue Wave Template</title>
</head>
<body>

<%--
	<c:if test="${(null eq sessionScope.teacher) }">
		<c:redirect url="./index.jsp"></c:redirect>
	</c:if>
 --%>	

	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content" ng-app="">
        
        	<h1>Payment Sheet</h1>
        	
        	<div style="overflow: hidden; width:300px; margin : 10px auto;">
        		<i class="fa fa-arrow-circle-left" style="float: left;color: rgb(56,143,212);font-size: 35px;cursor: pointer;" onclick="window.location = './adminSalary.view?year=${year }&month=${month-1}'; return false;"></i>
        		<p class="center_text" style="float: left; font-size: 35px; margin : 0 5px;">${year }-${month }</p>
        		<c:if test="${end eq false }">
        			<i class="fa fa-arrow-circle-right" style="float: left;color: rgb(56,143,212);font-size: 35px;cursor: pointer;" onclick="window.location = './adminSalary.view?year=${year }&month=${month+1}'; return false;"></i>
        		</c:if>
        		<c:if test="${end eq true }">
        			<i class="fa fa-arrow-circle-right" style="float: left;font-size: 35px;" ></i>
        		</c:if>
        	</div>
        	<div style="overflow: hidden; margin-bottom: 15px;">
        		<div style="float:left">
        			<span>Total USD : {{USD*${total} | number}}</span>
        		</div>
        		<button id="saveUSD" class="btn btn-primary" style="float:right; padding-top:3px; padding-bottom: 3px; padding-left : 6px; padding-right:6px; margin-left: 3px;">Save USD</button>
        		<input type="text" style="float: right; height: 28px; max-width: 50px;" ng-init="USD=${USD }" ng-model="USD"/>
        		
        	</div>
        	
        	<table class="table table-bordered">
        		<thead>
        			<tr>
        				<th>No</th>
        				<th>Teacher</th>
        				<th>Lecture</th>
        				<th>Class</th>
        				<th>Work Time</th>
        				<th>Deduct</th>
        				<th>Actual</th>
        				<th>Hour</th>
        				<th>Pay/h</th>
        				<th>Payment</th>
        				<th>USD</th>
        				<th></th>
        			</tr>
        		</thead>
        		<tbody>
        		<c:forEach items="${result }" var="vo">
        			<tr>
        				<td>${count.index }</td>
        				<td><a href="./adminChangeTeacherInfo.do?teacherId=${vo.teacher.id }" target="_blank">${vo.teacher.className }</a></td>
        				<td>${vo.lectureCount }</td>
        				<td>${vo.classTime }</td>
        				<td><a href="adminTeacherSalary.view?teacherId=${vo.teacher.id }" target="_blank">${vo.workTime }</a></td>
        				<td>${vo.deduct }</td>
        				<td>${vo.workTime - vo.deduct }</td>
        				<td>
        					<fmt:formatNumber value="${(vo.workTime-vo.deduct)/60 }" pattern="0.00"/>
        				</td>
        				<td>${vo.teacher.salary }</td>
        				<td><fmt:formatNumber value="${vo.payment }" pattern="0.00"/></td>
        				<td><p style="margin: 0">{{USD*${vo.payment}}}</p></td>
        				<td style="overflow: hidden;">
	        				<c:if test="${vo.payed eq false }">
	        					<span style="float: left; line-height: 22px;">미지급</span><a style="float: right; color: white; height:22px; padding-left: 3px; padding-right: 3px; padding-top: 1px; padding-bottom: 1px;" class="payButton btn btn-primary" href="#" data-id="${vo.teacher.id }">완료</a>
	        				</c:if>
	        				<c:if test="${vo.payed eq true }">
	        					<span style="float: left; line-height: 22px;">지급</span><a style="float: right; color: white; height:22px; padding-left: 3px; padding-right: 3px; padding-top: 1px; padding-bottom: 1px;" class="backButton btn btn-danger" href="#" data-id="${vo.teacher.id }">철회</a>
	        				</c:if>
        					
        					
        				</td>
        			</tr>
        		</c:forEach>
        		</tbody>
        	</table>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>