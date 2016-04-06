<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<jsp:useBean id="classLog" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="mode" scope="request" class="java.lang.Object"></jsp:useBean>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<style>
	p {
	margin: 0 0;
	}
	.radio{
	margin-top: 0;
	}
}
</style>
<script>
	$(document).ready(function(){
		$('#submitButton').click(function(){
			var s = {};
			s["status"] = $('input[name="status"]:checked').val();
			s["reason"] = $('textarea[name="reason"]').val();
			s["classDate"] = $('input[name="classDate"]').val();
			s["oneClassId"] = $('input[name="oneClassId"]').val();
			$.ajax({
				type:'POST',
				url:makeUrl('./classReportHandler.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					window.opener.document.location.reload();
					window.self.close();
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
	<div class="content" style="width: 500px; margin: 0 auto">
			<div style="width: 100%; float: left" class="form-group">
				<input type="hidden" class="form-control" value="${oneClass.id }"
					name="oneClassId" /> <input type="hidden" class="form-control"
					value="${dvo.dateForm }" name="classDate" />

				<p style="text-align: center; text-decoration: underline; font-size: xx-large; margin-bottom: 10px;">Class Report</p>
				<p>Class Name :${oneClass.lecture.book }</p>
				<p>Class Time : ${dvo.dateForm }(${dvo.dayOfWeek })
					${oneClass.teacherStartTimeString }</p>
				<p>Teacher Name : ${teacher.className }</p>
				<p>Student Name : <c:if test="${mode eq 'teacher' }">${student.className }</c:if><c:if test="${mode eq 'admin' }">${student.name }</c:if></p>

				<hr style="border-top: 1px solid #C0C0C0 ; margin: 10px 0;" />

				<div style="width: 100%; height: auto;" class="input-group">
					<div class="radio">
						<label><input type="radio" name="status" value="Completed" />Class Completed</label>
					</div>
					<div class="radio">
						<label><input type="radio" name="status"
							value="AbsentStudent" />Student Absence</label>
					</div>

				</div>

				<div style="height: 20px;"></div>

				<div style="width: 100%; border: 1px solid #c0c0c0; padding: 5px;"
					class="input-group">
					<div class="radio">
						<label><input type="radio" name="status"
							value="Uncompleted" <c:if test="${classLog.classState eq 'Uncompleted' }">checked="checked"</c:if> />Uncompleted</label>
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 110px" class="input-group-addon">Reason
							for <br /> Uncompleted
						</span>
						<textarea rows="5" class="form-control" name="reason"><c:if test="${classLog.classState eq 'Uncompleted' }">${classLog.reason }</c:if></textarea>
					</div>
				</div>

				
				<c:if test="${mode ne 'admin' }">
					<div style="height: 20px;"></div>
					<button id="submitButton" class="btn btn-primary btn-block">Save And Exit</button>
				</c:if>
			</div>
	</div>
	<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>