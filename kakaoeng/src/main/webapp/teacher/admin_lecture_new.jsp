<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${!lc.adminLogin and !lc.execLogin }">
	<c:redirect url="./index.jsp"></c:redirect>
</c:if>
<jsp:useBean id="unconfirmedPurchaseList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Insert title here</title>
<script type="text/javascript">
<c:if test="${lc.adminLogin}">
	$(document).ready(function(){
		$('.approveButton').click(function(){
			var button = $(this);
			$.ajax({
				type:'GET',
				url:makeUrl('./adminLectureConfirm.do?purchaseId='+button.attr('data-id')),
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
		$('.rejectButton').click(function(){
			var button = $(this);
			$.ajax({
				type:'GET',
				url:makeUrl('./adminLectureReject.do?purchaseId='+button.attr('data-id')),
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
</c:if>
</script>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>
	    
	    <div id="tooplate_main">
	                
	        <div id="tooplate_content">
	        
	        	<div>
	        		<h1>신규 강의</h1>
	        	
	        		<div style="width:100%;">
	        			<table class = "table table-bordered">
						   
						   <thead>
						      <tr>
						         <th>StartDate</th>
						         <th>수업기간</th>
						         <th>횟수</th>
						         <th>Teacher</th>
						         <th>Student</th>
						         <th>Course</th>
						         <th>결재상태</th>
						         <c:if test="${lc.adminLogin }">
						         	<th></th>
						         </c:if>
						      </tr>
						   </thead>
						   
						   <tbody>
						   <c:forEach items="${unconfirmedPurchaseList }" var="purchase">
						   	<fmt:parseNumber var="monthDuration" type="number" value="${purchase.weeks/4 }" />
						      <tr>
						         <td>${purchase.lecture.startDateForm }</td>
						         <td>${monthDuration }개월</td>
						         <td>${purchase.lecture.fullClass }</td>
						         <td>${purchase.lecture.teacher.className }</td>
						         <td>${purchase.lecture.student.className }</td>
						         <td>${purchase.lecture.book }</td>
						         <td>
						         	<c:choose>
						         		<c:when test='${!purchase.approvedNumber.startsWith("Wait") }'>완료</c:when>
						         		<c:otherwise>미결</c:otherwise>
						         	</c:choose>
						         </td>
						         <c:if test="${lc.adminLogin }">
							         <td>
							         	<button class="approveButton" data-id="${purchase.id }">확인</button>
							         	<c:if test='${purchase.approvedNumber.startsWith("Wait") }'>
							         	/<button class="rejectButton" data-id="${purchase.id }">삭제</button>
							         	</c:if>
							         </td>
						         </c:if>
						      </tr>
						   </c:forEach>
						   </tbody>
							
						</table>
	        		</div>
	        	</div>
	        </div> <!-- end of content -->
	        
	    </div>	<!-- end of main -->
    </div>
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>