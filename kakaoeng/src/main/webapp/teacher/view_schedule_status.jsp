<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<style>
.center_text {
	margin: 0 auto;
	display: block;
	text-align: center;
	vertical-align: middle;
	height: 100%;
	letter-spacing: 1px
}

.modal-dialog {
	outline: none;
	top: 200px;
	right: 1%;
}

.show-case{
	width:150px;
	height:30px;
	border-radius:5px;
	float:left;
	margin: 5px
}
</style>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script>
$(document).ready(function(){
	$('#showIndex').click(function(){
		$('#showIndex').hide(0);
		$('#hideIndex').show(0);
		$('#indexPanel').show(500);
		return false;
	})
	$('#hideIndex').click(function(){
		$('#hideIndex').hide(0);
		$('#showIndex').show(0);
		$('#indexPanel').hide(500);
		return false;
	})
})
</script>
</head>
<body>

<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        
        	<jsp:useBean id="classList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="reservedList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="completedList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="uncompletedList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="postStudentList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="postTeacherList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="absentStudentList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="absentTeacherList" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="levelTestReservedClassTime" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="levelTestCompletedClassTime" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<jsp:useBean id="levelTestUncompletedClassTime" scope="request" class="java.util.ArrayList"></jsp:useBean>
			<div id="content" class="container" style="width: 100%">
				<jsp:include page="ScheduleStatusTableTeacher.jsp" flush="false"></jsp:include>
				<div style="height: 10px;"></div>
				<a id="showIndex" href="#" style="font-weight: normal; font-size: 16px;"><i class="fa fa-caret-square-o-down"></i>&nbsp;&nbsp;Show Index</a>
				<a id="hideIndex" href="#" style="font-weight: normal; font-size: 16px; display: none;"><i class="fa fa-caret-square-o-up"></i>&nbsp;&nbsp;Hide Index</a>
				<div id="indexPanel" style="overflow: hidden; display: none;">
					<div class="levelTestReserved show-case">
						<p class="center_text" style = "color: white; text-align: center">Level Test</p>
					</div>
					<div class="reserved show-case">
						<p class="center_text" style = "color: white; text-align: center">Reserved</p>
					</div>
					<div class="completed show-case">
						<p class="center_text" style = "color: white; text-align: center">Completed</p>
					</div>
					<div class="uncompleted show-case">
						<p class="center_text" style = "color: white; text-align: center">Uncompleted</p>
					</div>
		
					<div class="postStudent show-case">
						<p class="center_text" style = "color: white; text-align: center">Postpone Student</p>
					</div>
					<div class="postTeacher show-case">
						<p class="center_text" style = "color: white; text-align: center">Postpone Teacher</p>
					</div>
					<div class="absentStudent show-case">
						<p class="center_text" style = "color: white; text-align: center">Absent Student</p>
					</div>
					<div class="absentTeacher show-case">
						<p class="center_text" style = "color: white; text-align: center">Absent Teacher</p>
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