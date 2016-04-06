<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="result" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="year" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="month" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="end" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="total" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="teacher" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<script>
$(document).ready(function(){
	var date = "%04d".format('${year}')+'-'+'%02d'.format('${month}')
	$('#dateDisplay').html(date);
	
	$('.detailView').click(function(){
		var url = makeUrl('./adminTeacherSalaryDetail.view?lectureId='+$(this).attr('data-id')+'&teacherId='+$(this).attr('data-teacher'));
		$.ajax({
			url : url,
			method : 'GET',
			contentType : 'application/x-www-form-urlencoded; charset=UTF-8',
			dataType : 'html',
			success:function(msg){
				$('#detailPanel').html(msg);
			},
			error: function(request,status,error){
				alert(request.responseText);
			}
		})
	})
});
</script>
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
                
        <div id="tooplate_content">
        
        	<h2>Teacher ${teacher.className }'s Payment Sheet</h2>
        	
        	<div style="text-align: center;">
	        	<div style="overflow: hidden; margin : 10px auto; display: inline-block;">
	        		<i style="float:left; font-size: 50px; color: rgb(56,143,212); cursor: pointer;" class="fa fa-arrow-circle-left" onclick="window.location = './teacherSalary.view?year=${year }&month=${month-1}'; return false;"></i>
	        		<p id="dateDisplay" class="center_text" style="float: left; font-size: 25px; margin : 0 5px;"></p>
	        		<c:if test="${end eq false }">
	        			<i style="float:left; font-size: 50px; color: rgb(56,143,212); cursor: pointer;" class="fa fa-arrow-circle-right" onclick="window.location = './teacherSalary.view?year=${year }&month=${month+1}'; return false;"></i>
	        		</c:if>
	        		<c:if test="${end eq true }">
	        			<i style="float:left; font-size: 50px;" class="fa fa-arrow-circle-right disabled"></i>
	        		</c:if>
	        	</div>
        	</div>
        	
        	<table class="table table-bordered">
        		<thead>
        			<tr>
        				<th>No</th>
        				<th>Lecture</th>
        				<th>Student</th>
        				<th>Class</th>
        				<th>Work Time</th>
        				<th>Deduct</th>
        				<th>Actual</th>
        				<th>Hour</th>
        				<th>Pay/h</th>
        				<th>Payment</th>
        			</tr>
        		</thead>
        		<tbody>
        		<c:forEach items="${result }" var="vo">
        			<tr>
        				<td>${count.index }</td>
        				<td>${vo.lecture.book }</td>
        				<td>${vo.lecture.student.className }</td>
        				<td>${vo.classTime }</td>
        				<td><a href="#" class="detailView" data-id="${vo.lecture.id }" data-teacher="${vo.lecture.teacher.id }">${vo.workTime }</a></td>
        				<td>${vo.deduct }</td>
        				<td>${vo.workTime - vo.deduct }</td>
        				<td><fmt:formatNumber value="${vo.hour }" pattern="0.00"/></td>
        				<td>${vo.lecture.teacher.salary }</td>
        				<td><fmt:formatNumber value="${vo.payment }" pattern="0.00"/></td>
        			</tr>
        		</c:forEach>
        		<tr  style="font-weight: bold;">
        			<td>Total</td>
        			<td></td>
        			<td></td>
        			<td>${total.classTime }</td>
        			<td>${total.workTime }</td>
        			<td>${total.deduct }</td>
        			<td>${total.workTime - total.deduct }</td>
        			<td><fmt:formatNumber value="${total.hour }" pattern="0.00"/></td>
        			<td></td>
        			<td><fmt:formatNumber value="${total.payment }" pattern="0.00"/></td>
        		</tr>
        		</tbody>
        	</table>
        	
        	<div id="detailPanel">
        	</div>
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>