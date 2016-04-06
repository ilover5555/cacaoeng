<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="classLogList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Insert title here</title>
<c:if test="${lc.execLogin }">
<script>
	$(document).ready(function(){
		$('select').attr('disabled', 'disabled');
	})
</script>
</c:if>
<c:if test="${lc.adminLogin }">
<script type="text/javascript">
$(document).ready(function(){
	$('.saveButton').click(function(){
		var button = $(this);
		var classLogId = button.attr('data-id');
		var scrollY = $(window).scrollTop();
		var s ={};
		s["classLogId"] = classLogId;
		s["classState"] = $('#state_'+classLogId+' option:checked').val();
		s["reason"] =  $('#state_'+classLogId+' option:checked').val();
		$.ajax({
			type:'POST',
			url:makeUrl('./adminLectureClassLogUpdate.do'),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
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
</c:if>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">
				<div style="overflow: hidden;">
					<div style="width: 100%">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>수업No</th>
									<th>날짜</th>
									<th>시작시간</th>
									<th>수업시간</th>
									<th>Class Type</th>
									<th>Class Status</th>
									<c:if test="${lc.adminLogin }">
										<th></th>
									</c:if>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${classLogList }" var="classLog">
								<tr>
									<td>${count.index }</td>
									<td>${classLog.classDateForm }</td>
									<td>${classLog.oneClass.duration.rt.startTimeFormat }</td>
									<td>${classLog.oneClass.duration.duration*25 }</td>
									<td>Normal</td>
									<td>
										<select id="state_${classLog.id }" <c:if test='${classLog.classState eq "Holiday" }'>disabled="disabled"</c:if>>
											<option value="Completed" <c:if test='${classLog.classState eq "Completed" }'>selected="selected"</c:if>>Completed</option>
											<option value="PostponeStudent" <c:if test='${classLog.classState eq "PostponeStudent" }'>selected="selected"</c:if>>PostponeStudent</option>
											<option value="PostponeTeacher" <c:if test='${classLog.classState eq "PostponeTeacher" }'>selected="selected"</c:if>>PostponeTeacher</option>
											<option value="AbsentStudent" <c:if test='${classLog.classState eq "AbsentStudent" }'>selected="selected"</c:if>>AbsentStudent</option>
											<option value="AbsentTeacher" <c:if test='${classLog.classState eq "AbsentTeacher" }'>selected="selected"</c:if>>AbsentTeacher</option>
											<option value="Uncompleted" <c:if test='${classLog.classState eq "Uncompleted" }'>selected="selected"</c:if>>Uncompleted</option>
											<option value="Uncompleted_0" <c:if test='${classLog.classState eq "Uncompleted_0" }'>selected="selected"</c:if>>Uncompleted_0</option>
											<option value="Uncompleted_30" <c:if test='${classLog.classState eq "Uncompleted_30" }'>selected="selected"</c:if>>Uncompleted_30</option>
											<option value="Uncompleted_50" <c:if test='${classLog.classState eq "Uncompleted_50" }'>selected="selected"</c:if>>Uncompleted_50</option>
											<option value="Uncompleted_100" <c:if test='${classLog.classState eq "Uncompleted_100" }'>selected="selected"</c:if>>Uncompleted_100</option>
											<option value="Holiday" <c:if test='${classLog.classState eq "Holiday" }'>selected="selected" disabled="disabled"</c:if>>Holiday</option>
										</select>
									</td>
									<c:if test="${lc.adminLogin }">
										<td><a data-id="${classLog.id }" class="saveButton" href="#">Save</a></td>
									</c:if> 
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