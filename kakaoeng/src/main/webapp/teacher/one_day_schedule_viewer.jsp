<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<script>
/*
	$(document).ready(function(){
		var date = $('input[name="date"]').val();
		var base = new Date(date);
		var d = new Date();
		var e = d.getFullYear()+"-"+"%02d".format((d.getMonth()+1))+"-"+"%02d".format((d.getDate()))
		var k = new Date(e)
		if(k.getTime() < base.getTime())
			$('.report').css('display', 'none');
	})
*/
$(function() {
	$("#oneDaySchedule").dataTable({
		columnDefs : [{"targets":'no-sort', "orderable":false,},
		              {"targets":0, "orderable":true, "visible" : false}
		              ]		
	});
});
</script>
<style>
table{
	text-align: center;
}

</style>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
</head>
<body>
	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        
        	<jsp:useBean id="dvo" scope="request" class="java.lang.String"></jsp:useBean>
			<jsp:useBean id="teacher" scope="request" class="java.lang.String"></jsp:useBean>
			<jsp:useBean id="student" scope="request" class="java.lang.Object"></jsp:useBean>
			<jsp:useBean id="classStateMap" scope="request" class="java.util.HashMap"></jsp:useBean>
			
				<div class="content" style="width: 100%; margin: 0 auto">
					<div>
						<input type="hidden" name="date" value="${dvo }" />
						<div><h2>Teacher ${teacher}'s Class of [${dvo }]</h2></div>
						<table id="oneDaySchedule" class="table table-bordered">
							<thead>
								<tr>
									<th></th>
									<th class="no-sort" style="width:90px;text-align: center;">Start Time</th>
									<th class="no-sort" style="text-align: center; min-width: 150px;">Course</th>
									<th class="no-sort" style="width:130px;text-align: center;">Class Time</th>
									<th class="no-sort" style="width:60px;text-align: center;">Count</th>
									<th class="no-sort" style="width:130px;text-align: center;">Student</th>
									<th class="no-sort" style="width:60px;text-align: center;">Report</th>
									<th class="no-sort" style="width:100px;text-align: center;">Status</th>
								</tr>
							</thead>
							<tbody>
							<c:set var="count" scope="request" value="${1 }"></c:set>
							<c:forEach var="set" items="${classStateMap}">
								<tr>
									<td>${set.key.stamp}</td>
									<td><a href="./lectureInformation.view?oneClassId=${set.key.id }">${set.key.teacherStartTimeString }</a></td>
									<td>${set.key.lecture.book }</td>
									<td>${set.key.duration.duration * 25 }</td>
									<td>${set.key.order }/${set.key.lecture.fullClass }</td>
									<td>${set.key.student.className }</td>
									<td>
										<c:choose>
											<c:when test="${(set.value.classState eq 'LevelTestReserved') or (set.value.classState eq 'LevelTestCompleted') or (set.value.classState eq 'LevelTestUncompleted')}">
												<a class="btn btn-primary report" style="padding-top: 2px; padding-bottom: 2px;" onclick='window.open("./levelTest.do?oneClassId=${set.key.id }&date=${dvo}", "", "width=530, height=560, resizable=0, scrollbars=0, status=0;"); return false;'>Report</a>
											</c:when>
											<c:when test="${(set.value.classState eq 'Completed') or (set.value.classState eq 'AbsentStudent') or (set.value.classState eq 'Uncompleted') or (set.value eq null)}">
												<a class="btn btn-primary report" style="padding-top: 2px; padding-bottom: 2px;" onclick='window.open("./levelTest.do?oneClassId=${set.key.id }&date=${dvo}", "", "width=530, height=560, resizable=0, scrollbars=0, status=0;"); return false;'>Report</a>
											</c:when>
										</c:choose>
									</td>
									<td>${set.value.classState }<c:if test="${set.value eq null }">Reserved</c:if></td>
									<c:set var="count" scope="request" value="${count+1 }"></c:set>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>