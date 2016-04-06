<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="../tooplate_style.css" rel="stylesheet" type="text/css" />
<style>
body{
	font-size: 12px !important;
}
</style>
<script>
	$(document).ready(function(){
		var d = document.location.pathname.split('/');
		var e = $('a[href^="' + d[d.length-1]+ '"]');
		e.toggleClass('current');
		
		$('#createClassRoom').click(function(){
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./teacherCreateBBB.do"),
				method : "GET",
				dataType : "html",
				success:function(msg){
					var wX = screen.availWidth;
					 var wY = screen.availHeight;
					 wY = (wY-38);

					var winIntro = window.open(
					                                        msg,
					                                        "팝업창이름",
					                                        "width="+ wX + ", height="+ wY + ", scrollbars=no, status=yes, scrollbars=no,  resizable=yes, direction=yes, location=no,  menubar=no, toolbar=no, titlebar=yes"
					);

					 winIntro.focus();
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
	})
</script>

<style>
	.nav-tabs > li > a:hover{
		background-color: transparent;
		border-color: transparent;
	}
	.more{
		line-height: 25px;
	}
</style>

<div id="tooplate_header"<c:if test="${(lc.adminLogin) or (lc.execLogin) }">style="height:50px;"</c:if>>
        <div id="tooplate_menu" style="position: relative;">
        	<c:choose>
   				<c:when test="${lc.teacherLogin }">
   					<ul>
		                <li><a href="myprofile.jsp">My Profile</a></li>
		                <li><a href="adminBoardList.view?page=1&viewPerPage=20">Notification</a></li>
		                <li><a href="viewSchedule.do">Availabilty</a></li>
		                <li><a href="viewTeacherScheduleState.do">Schedule</a></li>
		                <li><a href="teacherSalary.view">Salary</a></li>
		                <li><a href="contact.html">Contact</a></li>
		            </ul>
   				</c:when>
   				<c:when test="${lc.teacherLogin or lc.adminLogin or lc.execLogin }">
   					<ul>
		                <li><a href="adminBoardList.view?page=1&viewPerPage=20">Notification</a></li>
		            </ul>
   				</c:when>
   			</c:choose>
   			<c:if test="${lc.adminLogin or lc.execLogin }">
        	<ul class="nav nav-tabs" style="border: none;">
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#">
						선생님 관리 <span class="caret"></span>
					</a>
	
					<ul class="dropdown-menu">
						<c:if test="${lc.adminLogin }">
							<li><a href="./adminTeacherAccept.do">신규 관리</a></li>
						</c:if>
						<li><a href="./adminTeacherBasic.view">기본 관리</a></li>
						<li><a href="./adminTeacherRateList.view">그룹 관리</a></li>
						<c:if test="${lc.adminLogin }">
							<li><a href="./adminTeacherRetireManage.do">퇴사 관리</a></li>
							<li><a href="./adminSalary.view">급여 관리</a></li>
							<li><a href="./adminMailManage.view">Email 관리</a></li>
						</c:if>
					</ul>
				</li>
			
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#">
						강의 관리 <span class="caret"></span>
					</a>
	
					<ul class="dropdown-menu">
						<li><a href="./adminLectureNew.view">신규 강의</a></li>
						<li><a href="./adminLectureGeneral.view">강의 종합</a></li>
						<c:if test="${lc.adminLogin }">
							<li><a href="./holidayServlet.do">공휴일</a></li>
							<li><a href="./adminBookList.view">교재 관리</a></li>
						</c:if>
						<li><a href="./adminDailyClass.view">수업 상황표</a></li>
					</ul>
				</li>
				<li class="dropdown">
					<a class="dropdown-toggle" data-toggle="dropdown" href="#">
						학생 관리 <span class="caret"></span>
					</a>
	
					<ul class="dropdown-menu">
						<li><a href="./adminStudentBasic.view">학생 관리</a></li>
						<li><a href="./adminLevelTestManage.view">레벨 테스트 관리</a></li>
						<c:if test="${lc.adminLogin }">
							<li><a href="./adminSMSManage.view">SMS 관리</a></li>
						</c:if>
						
					</ul>
				</li>
				<c:if test="${lc.adminLogin }">
					<li><a href="./adminViewTuition.view">수업료 관리</a></li>
					<li><a href="./adminAccount.view">계정 관리</a></li>
				</c:if>
			</ul>
			</c:if>
            <div class="cleaner"></div>
            <c:if test="${(lc.teacherLogin) or (lc.adminLogin) or (lc.execLogin) }">
	   			<c:choose>
	  
	   				<c:when test="${lc.adminLogin }">
	   					<c:set var="user" scope="request" value="${lc.loginAdminObject }"></c:set>
	   				</c:when>
	   				<c:when test="${lc.teacherLogin }">
	   					<c:set var="user" scope="request" value="${lc.loginTeacherObject }"></c:set>
	   				</c:when>
	   				<c:when test="${lc.execLogin }">
	   					<c:set var="user" scope="request" value="${lc.loginExecObject }"></c:set>
	   				</c:when>
	   			</c:choose>
	   			<c:if test="${(lc.teacherLogin) }">
		   		<div class="float_r" style="overflow: hidden;">
		   		
		   			<span style="position: absolute ; right: 80px; color: rgb(255,192,0); font-size: 25px; font-weight: 700; top: 90px;">Welcome!!</span>
            		<span style="position: absolute ; right: 60px; top:115px; color: rgb(59,137,201); font-size: 25px; font-weight: 700;">${user.userType } ${user.shortClassName }</span>
		   		</div>
		   		</c:if>
	   		</c:if>
            
        </div> <!-- end of menu -->
    
    </div> <!-- end of header -->
    
    <div id="tooplate_middle" style="overflow: hidden;">
   		<c:if test="${(!lc.teacherLogin) and (!lc.adminLogin) and (!lc.execLogin) }">
	   		<div class="float_r">
	   			<a class="more" href="log_in.jsp" style="line-height: 25px;  font-size: 13px;">Log In</a>
	   		</div>
   		</c:if>
   		<c:if test="${(lc.teacherLogin) or (lc.adminLogin) or (lc.execLogin) }">
	   		<div class="float_r" style="overflow: hidden;">
	   			<c:if test="${lc.teacherLogin }">
	   				<a id="createClassRoom" class="more" href="#" style="line-height: 25px; font-size: 13px; float: left;">ClassRoom</a>
	   			</c:if>
	   			<c:if test="${(null ne sessionScope.admin) or (null ne sessionScope.exe) }">
	   				<div style="float:left; margin-right: 15px;"><h3>${user.userType } ${user.name }</h3></div>
	   			</c:if>
	   			<div style="float: left;"><a class="more" href="logout.do" style="line-height: 25px; font-size: 13px; float: left;">Log Out</a></div>
	   		</div>
   		</c:if>
    </div> <!-- end of middle -->