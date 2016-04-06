<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style>
h3{
	margin-top: 10px !important;
	margin-bottom: 3px !important;
}
</style>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<style>
hr {
    display: block;
    height: 1px;
    border: 0;
    border-top: 1px solid #C0C0C0;
    margin: 1em 0;
    padding: 0; 
}
.input-group-addon{
	padding : 0 !important;
	border: 0 !important; 
}


</style>
<script>
	$(document).ready(function(){
		$('#updateNote').click(function(){
			var s = {};
			s["lectureId"] = Number("${lecture.id}");
			s["note"] = $('#note').val();
			$.ajax({
				type:'POST',
				url:makeUrl('./updateNote.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr);
					alert(status);
					alert(error);
				},
			})
		})
		
		$('#updateStudentNote').click(function(){
			var s = {};
			s["studentId"] = "${lecture.student.id}";
			s["note"] = $('#studentNote').val();
			$.ajax({
				type:'POST',
				url:makeUrl('./studentUpdateNote.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr);
					alert(status);
					alert(error);
				},
			})
		})
	})
</script>
</head>
<body>
	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
			<div class="content" style="width:700px; margin: 0 auto">
				<h3>Lecture Information</h3>
				<div style="overflow: hidden; width:100%">
					<div style="float:left; width:700px;">
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td>Class Name</td>
									<td>${lecture.book }</td>
									<td>Teacher Name</td>
									<td>${lecture.teacher.className }</td>
								</tr>
								<tr>
									<td>Start Date</td>
									<td>${lecture.startDateForm }</td>
									<td>End Date</td>
									<td>${lecture.endDateForm }</td>
								</tr>
								<tr>
									<td>Class Done</td>
									<td>${lecture.done }</td>
									<td>Total Class</td>
									<td>${lecture.fullClass }</td>
								</tr>
							</tbody>
						</table>
						
						<hr/>
						
						<h3>Class Time : </h3>
						
						<div style="border: 1px solid #c0c0c0; width:700px; height:100px; overflow: hidden;">
							<c:forEach var="oneClass" items="${oneClassList }">
								<div style="border: 1px solid #c0c0c0; width:${698/oneClassList.size()}px; height:100%; float:left">
									<div style="width:100%; height:30px; border-bottom: 1px solid #c0c0c0">
										<p class="center_text align_vertical_center">${oneClass.duration.rt.dayOfWeek }</p>
									</div>
									<div style="width:100%; height:40px; border-bottom: 1px solid #c0c0c0">
										<p class="center_text align_vertical_center">${oneClass.teacherStartTimeString }</p>
									</div>
									<div style="width:100%; height:30px;">
										<p class="center_text algin_vertical_center">${oneClass.duration.duration*25 }min</p>
									</div>
								</div>
							</c:forEach>
						</div>
						<h3>Class Status:</h3>
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td>Completed</td>
									<td>${completed }</td>
									<td>Uncompleted</td>
									<td>${uncompleted }</td>
								</tr>
								<tr>
									<td>Student Absence</td>
									<td>${absentStudent }</td>
									<td>Teacher Absence</td>
									<td>${absentTeacher }</td>
								</tr>
								<tr>
									<td>Student Postpone</td>
									<td>${postponeStudent }</td>
									<td>Teacher Postpone</td>
									<td>${postponeTeacher }</td>
								</tr>
							</tbody>
						</table>
						
						<hr/>
						
						<h3>Student Information:</h3>
						<table class="table table-bordered">
							<tbody>
								<tr>
									<td style="width: 130px;">Student Name</td>
									<td style="min-width: 150px;">${lecture.student.className }</td>
									<td style="width: 130px;">Gender</td>
									<td style="min-width: 150px;">${lecture.student.gender }</td>
								</tr>
								<tr>
									<td>Age</td>
									<td style="min-width: 150px;">${age }</td>
									<td>English Skill</td>
									<td style="min-width: 150px;">${lecture.student.level }</td>
								</tr>
								<tr>
									<td>Skype ID</td>
									<td style="min-width: 150px;">${lecture.student.skype }</td>
									<td> </td>
									<td> </td>
								</tr>
							</tbody>
						</table>
					</div>
					<div style="float:left; width:700px">
						<p>Lecture Note</p>
						<textarea id="note" rows="16" style="width:100%">${lecture.note }</textarea>
						<button id="updateNote" class="btn btn-primary btn-block">Save Lecture Note</button>
						<p>Student Info</p>
						<textarea rows="15" style="width:100%" disabled="disabled">${lecture.student.note }</textarea>
						<div class="input-group">
							<input id="studentNote" type="text" class=" form-control" />
							<span class="input-group-addon">
								<button id="updateStudentNote" type="button" class="btn btn-primary">Input</button>
							</span>
						</div>
					</div>
				</div>
			</div>
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>