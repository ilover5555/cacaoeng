<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="lectureList" scope="request" class="java.util.ArrayList"></jsp:useBean>
		<jsp:useBean id="scheduleList" scope="request" class="java.util.ArrayList"></jsp:useBean>
		<jsp:useBean id="customCalendarCodeList" scope="request" class="java.lang.Object"></jsp:useBean>
		<jsp:useBean id="holidayDAO" scope="request" class="java.lang.Object"></jsp:useBean>
		<jsp:useBean id="today" scope="request" class="java.lang.Object"></jsp:useBean>
		<jsp:useBean id="lecture" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script type="text/javascript" src="../js/wSelect.min.js"></script>
<link rel="stylesheet" type="text/css" href="../wSelect.css" />
<title>Insert title here</title>
<style>
#lectureInfo td{
	border: 1px solid rgb(61,61,61) !important;
	padding-top: 13px !important;
	padding-bottom: 13px !important;
	line-height: 15px;
	font-size: 15px;
	font-weight: bolder;
	height: 43px;
}
.schedule_label{
	background-color: rgb(231,231,231);
	text-align: center;
	width: 234px;
}
.schedule_content{
	padding-left: 30px;
}
</style>

<script>
	$(document).ready(function(){
		$('.procrastinateButton').click(function(){
			if (!confirm('수업을 연기하시겠습니까?')) {
				return;
			}
			var button = $(this);
			var date = button.attr('data-date');
			var id = button.attr('data-id');
			$.ajax({
				type:'GET',
				url:makeUrl('./studentProcrastinate.do?oneClassId='+id+"&date="+date),
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
		$('#lectureViewButton').click(function(){
			
			window.location ='./studentSchedule.view?year=${param.year}&month=${param.month}&purchaseNumber='+$('#lectureSelect > option:checked').val();
		})
	})
</script>
</head>
<body>
	<c:if test="${(null eq sessionScope.student)}">
		<c:redirect url="./main.jsp"></c:redirect>
	</c:if>

	<div id="wrap">
		<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
		<div style="height: 78px;"></div>
		
		<c:if test="${lectureList.size() > 1 }">
		<div style="width:934px; margin: 0 auto;">
			<select id="lectureSelect" class="wSelect-el" style="float: right;">
				<c:forEach items="${lectureList }" var="lectureElement">
					<option value="${lectureElement.purchaseNumber }">${lectureElement.course.displayName } -
					<c:if test="${lectureElement.book eq 'Not yet determined'}"> 
						선생님과 상의 후 결정합니다
					</c:if>
					<c:if test="${lectureElement.book ne 'Not yet determined'}"> 
						${lectureElement.book }
					</c:if>
					</option>
				</c:forEach>
			</select>
			<button id="lectureViewButton">조회</button>
		</div>
		</c:if>
		<div style="height:30px;"></div>
		
		<div style="width : 934px; margin: 0 auto;">
			<div style="width:130px; height:24px; background-image: url('../artifact/student_schedule_schedule.png'); margin-bottom: 15px;"></div>
			<table id="lectureInfo" style="border-collapse: collapse; border-top: 2px solid rgb(146,146,146); width:934px;">
				<tr>
					<td class="schedule_label">수업과정</td>
					<td class="schedule_content" colspan="3">${lecture.course.displayName }</td>
				</tr>
				<tr>
					<td class="schedule_label">수업교재</td>
					<td class="schedule_content" colspan="3"><c:if test="${lecture.book eq 'Not yet determined' }">선생님과 상의 후 결정합니다</c:if><c:if test="${lecture.book ne 'Not yet determined' }">${lecture.book }</c:if></td>
				</tr>
				<tr>
					<td class="schedule_label">수업기간</td>
					<fmt:parseNumber var="monthDuration" type="number" value="${lecture.weeks/4 }" />
					<td class="schedule_content" colspan="3">${lecture.startDateForm }~${lecture.calculatedEndDateForm }(${monthDuration }개월)</td>
				</tr>
				<tr>
					<td class="schedule_label">학생이름</td>
					<td class="schedule_content" style="width:233px;">${lecture.student.name }</td>
					<td class="schedule_label">강사이름</td>
					<td class="schedule_content">${lecture.teacher.className }</td>
				</tr>
				<tr>
					<td class="schedule_label">수업횟수</td>
					<td class="schedule_content" style="width:233px;">${lecture.done}/${lecture.fullClass }</td>
					<td class="schedule_label">강사SkypeID</td>
					<td class="schedule_content">${lecture.teacher.skype }</td>
				</tr>
			</table>
		</div>
		<div style="height:88px;"></div>
		<div id="mainContent" style="width:935px; margin: 0 auto;">
		
			<div style="width:149px; height:24px; background-image: url('../artifact/student_schedule_plan.png'); margin-bottom: 9px;"></div>
			<div style="width:934px; height:60px; background-image: url('../artifact/student_schedule_plan_header.png'); overflow: hidden;">
				<a class="align_vertical_center" href="./studentSchedule.view?year=${param.year }&month=${param.month-1 }&purchaseNumber=${param.purchaseNumber}#mainContent" style="float: left; margin-left: 20px;"><i class="fa fa-arrow-circle-left" style="font-size: 50px; color: rgb(255,192,0)"></i></a>
				<p class="align_vertical_center" style="float:left; position: relative; left: 40%; font-size: 20px; color: white;">${param.year }년${param.month }월</p>
				<a class="align_vertical_center" href="./studentSchedule.view?year=${param.year }&month=${param.month+1 }&purchaseNumber=${param.purchaseNumber}#mainContent" style="float: right; margin-right: 20px;"><i class="fa fa-arrow-circle-right" style="font-size: 50px; color: rgb(255,192,0)"></i></a>
			</div>
			<div style="border-left: 1px solid rgb(62,62,62); border-right: 1px solid rgb(62,62,62); border-bottom: 1px solid rgb(62,62,62);
				padding-bottom: 13px; padding-left: 8px; padding-right: 8px; overflow: hidden; width:100%; background-color: rgb(246,246,246)">
				
				<div style="width: 100% ; overflow: hidden; margin-top: 14px; margin-bottom: 10px;">
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_sunday.png)'></div>
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_monday.png)'></div>
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_tuesday.png)'></div>
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_wednesday.png)'></div>
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_thursday.png)'></div>
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_friday.png)'></div>
					<div style='float:left;width:129px; height:42px; margin : 0.5px 1px; border-radius: 5px; background-image: url(../artifact/student_schedule_saturday.png)'></div>
					
				</div>
				
				<c:forEach var="i" begin="1" end="${scheduleList.size() }">
					<div style='width:129px; height:153px; margin : 0.5px 1px; float: left; border-radius: 5px;
						<c:if test="${scheduleList.get(i-1).validation }">border: 1px solid rgb(202,202,202); 
						<c:if test="${scheduleList.get(i-1).dateForm eq today }">background-color:rgb(255,255,231);</c:if>
						<c:if test="${scheduleList.get(i-1).dateForm ne today }">background-color:white;</c:if>
						</c:if>'>
					<c:if test="${scheduleList.get(i-1).validation }">
						<div style="width:118px; height:26px; margin: 3px auto 0 auto; overflow: hidden">
							<c:if test="${scheduleList.get(i-1).dateForm eq lecture.startDateForm }">
								<div class="align_vertical_center" style="float:left; margin-left: 8px;"><p style="text-align: center; font-size: 11px; font-weight: bolder">수업시작</p></div>
							</c:if>
							<div style="background-color: rgb(232,232,232); border-radius : 50%; width:26px; height:26px; float: right;"><p class="center_text">${scheduleList.get(i-1).day }</p></div>
						</div>
						<div style="height:3px; width:107px; border-bottom : 1px solid rgb(202,202,202); margin: 0 auto"></div>
						<div style="height:124px; text-align: center;">
						<c:if test="${customCalendarCodeList.contains(i) }">
						<c:choose>
						<c:when test="${holidayDAO.checkHoliday(scheduleList.get(i-1).date)}">
							<c:choose>
								<c:when test="${holidayDAO.checkHoliday(scheduleList.get(i-1).date)}">
									<img style="width: 100px; height: 100px; margin-top: 10px;" src="../artifact/dayoff.png">
								</c:when>
								<c:when test="${scheduleList[i-1].classState eq 'Completed' }">
									<div style="margin: 0 auto; width:71px; height:118px;">
										<img style="margin : 35px 0;" src="../artifact/student_schedule_complete.png">
									</div>
								</c:when>
								<c:when test="${scheduleList[i-1].classState eq 'AbsentStudent' }">
									<div style="margin: 0 auto; width:49px; height:118px;">
										<img style="margin : 24px 0" src="../artifact/student_schedule_student_absence.png">
									</div> 
								</c:when>
								<c:when test="${scheduleList[i-1].classState eq 'Uncompleted' }">
									<div style="margin: 0 auto; width:70px; height:118px;">
										<img style="margin : 30.5px 0" src="../artifact/student_schedule_uncompleted.png">
									</div>
								</c:when>
								<c:when test="${(scheduleList[i-1].classState eq 'PostponeStudent') or (scheduleList[i-1].classState eq 'PostponeTeacher') }">
									<div style="margin: 0 auto; width:75px; height:118px;">
										<img style="margin : 33px 0" src="../artifact/student_schedule_postpone.png">
									</div>
								</c:when>
							</c:choose>
						</c:when>
						<c:when test="${customCalendarCodeList.getOneClassListByCode(i).size() > 1}">
							일정이  ${customCalendarCodeList.getOneClassListByCode(i).size()}개 있습니다.
						</c:when>
						<c:when test="${(scheduleList[i-1].classState eq null) and (lecture.inRange(scheduleList[i-1].date)) }">
							<div>
								<div style="width:99px; height:61px; border: 1px solid rgb(209,209,209); border-radius : 5px; margin: 10px auto;">
									<p style="text-align: center; font-size: 16px; height: 27px;">${lecture.teacher.shortClassName }</p>
									<div style="height:26px; margin-left: 9px; overflow: hidden;">
										<img style="float: left; margin: 4.5px 0;" src="../artifact/student_schedule_complete_small.png;">
										<div style="width:7px; height:26px; float: left;"></div>
										<div style="float: left; height: 26px;">
											<p style="height:13px; font-size: 10.5px;">${customCalendarCodeList.getOneClassListByCode(i).get(0).koreanStartTime }</p>
											<p style="height:15px; font-size: 10.5px; line-height: 13px;">${customCalendarCodeList.getOneClassListByCode(i).get(0).duration.duration * 25 }분 수업</p>
										</div>
									</div>
								</div>
								<div class="procrastinateButton" data-date="${scheduleList.get(i-1).dateForm }" data-id="${customCalendarCodeList.getOneClassListByCode(i).get(0).id }" style="width:75px; height:22px; background-image: url('../artifact/student_schedule_postpone_request.png'); cursor: pointer; margin: 9px auto 0 auto;"></div>
							</div>
						</c:when>
						<c:when test="${scheduleList.get(i-1).classState eq 'Completed' }">
							<div style="margin: 0 auto; width:71px; height:118px;">
								<img style="margin : 35px 0;" src="../artifact/student_schedule_complete.png">
							</div>
						</c:when>
						<c:when test="${scheduleList.get(i-1).classState eq 'AbsentStudent' }">
							<div style="margin: 0 auto; width:49px; height:118px;">
								<img style="margin : 24px 0" src="../artifact/student_schedule_student_absence.png">
							</div> 
						</c:when>
						<c:when test="${scheduleList.get(i-1).classState eq 'Uncompleted' }">
							<div style="margin: 0 auto; width:70px; height:118px;">
								<img style="margin : 30.5px 0" src="../artifact/student_schedule_uncompleted.png">
							</div>
						</c:when>
						<c:when test="${(scheduleList[i-1].classState eq 'PostponeStudent') or (scheduleList[i-1].classState eq 'PostponeTeacher') }">
							<div style="margin: 0 auto; width:75px; height:118px;">
								<img style="margin : 33px 0" src="../artifact/student_schedule_postpone.png">
							</div>
						</c:when>
						</c:choose>
						</c:if>
						</div>
					</c:if>
					</div>
					
				</c:forEach>
			</div>
			
			<div style="height: 10px"></div>
			
			<div style="width:934px; margin: 0 auto; padding : 5px 5px; background-color: rgb(246,246,246); overflow: hidden; border: 1px solid #c0c0c0">
				<div style="width:83px; height:83px; float:right; margin : 0 2px; background-image: url('../artifact/comple_label.png')"></div>
				<div style="width:83px; height:83px; float:right; margin : 0 2px; background-image: url('../artifact/postpone_label.png')"></div>
				<div style="width:83px; height:83px; float:right; margin : 0 2px; background-image: url('../artifact/cancel_label.png')"></div>
				<div style="width:83px; height:83px; float:right; margin : 0 2px; background-image: url('../artifact/absence_label.png')"></div>
				<div style="width:83px; height:83px; float:right; margin : 0 2px; background-image: url('../artifact/complete_label.png')"></div>
			</div>
		</div>
		
		<script type="text/javascript">
			$('select').wSelect();
			$('.wSelect').css('max-width', '90%');
			$('.wSelect').css('width', '90%');
			$('.wSelect-options-holder').css('max-width', '100%');
			$('.wSelect-options-holder').css('width', '100%');
		</script>
		<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
	</div>
</body>
</html>