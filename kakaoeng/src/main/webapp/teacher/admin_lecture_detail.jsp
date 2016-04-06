<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:if test="${!lc.adminLogin && !lc.execLogin }">
	<c:redirect url="./index.jsp"></c:redirect>
</c:if>
<jsp:useBean id="oneClassList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="bookList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="purcahse" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="lecture" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="../minimal/grey.css" rel="stylesheet">
<script src="../icheck.js"></script>
<title>Insert title here</title>
<c:if test="${lc.execLogin }">
<script>
	$(document).ready(function(){
		$('select').attr('disabled', 'disabled');
		$('input').attr('disabled', 'disabled');
	})
</script>
</c:if>

<c:if test='${(lecture.status eq "OnGoing") and (lc.adminLogin) }'>
<script type="text/javascript">
function dialogInit(){
	common.init();
	$('.choice-similar').iCheck({
		checkboxClass:'icheckbox_minimal-grey',
		radioClass:'iradio_minimal-grey',
		increaseArea:'50%'
	});
	$('.teacher-select-button').click(function(){
		
		var id = $(this).attr('data-id');
		var baseDate = $('#classStartDate').val();
		var name = $('h6[id="name-' + id + '"]').text();
		selectedResult["baseDate"] = baseDate;
		selectedResult["id"] = id;
		selectedResult["name"] = name;
		selectedResult["sort"] = "${lecture.course}";
		selectedResult["student"] = "${lecture.student.id}";
		selectedResult["status"] = "TeacherChange";
		selectedResult["purchaseNumber"] = "${purchase.id}";
		selectedResult["lectureId"] = "${lecture.id}";
		var count = 0;
		var valid = true;
		$('div[data-id="schedule-' + id+ '"] .scheduleCell').each(function(){
			var row = {};
			var target = $(this);
			var dayOfWeek = target.attr('data-day');
			var duration = Number(target.attr('data-duration'));
			var checked = $('.choice-similar-check[data-name="choice-' + id + '-' + dayOfWeek + '"]:checked', target);
			
			if(checked.length == 0){
				alert('시간을 선택하세요');
				valid =false;
				return;
			}
			var hour = Number(checked.attr('data-hour'));
			var minute = Number(checked.attr('data-minute'));
			
			row["dayOfWeek"] = dayOfWeek;
			row["hour"] = hour;
			row["minute"] = minute;
			row["duration"] = duration;
			var selected = $('#monthSelect').val()
			row["weeks"] = Number($('#left'+(count+1)).val());
			row["oneClassId"] = Number($('#id'+(count+1)).val());
			selectedResult["index"+count] = JSON.stringify(row);
			count++;
		});
		selectedResult["length"] = count;
		if(valid == false){
			selectedResult = {};
			return;
		}
		
		common.init();
		if(!confirm("선택 하시면 기존 강의가 취소되고 선택하신 선생님으로 새롭게 예약됩니다.\n정말로 진행하시겠습니까?"))
			return;
		selectedResult["book"] = "${lecture.book}";
		selectedResult["method"] = "${purchase.method}";
		$.ajax({
			url : makeUrl("../rebook.do"),
			method : "POST",
			data : selectedResult,
			dataType : "text",
			success:function(msg){
				alert(msg);
				window.location.reload()
			},
			error: function(request,status,error){
				alert(request.responseText);
			}
		});
		scheduleCell.hideBox();
	})
}
var selectedResult = {};
function changeTime(){
	selectedResult["baseDate"] = $('#classStartDate').val();
	selectedResult["book"] = "${lecture.book}";
	selectedResult["id"] = "${lecture.teacherId}";
	selectedResult["method"]  = "${purchase.method}";
	selectedResult["name"] = "${lecture.teacher.className}";
	selectedResult["sort"] = "${lecture.course}";
	selectedResult["length"] = ${oneClassList.size()};
	
	selectedResult["student"] = "${lecture.student.id}";
	selectedResult["status"] = "TimeChange";
	selectedResult["purchaseNumber"] = "${purchase.id}";
	selectedResult["lectureId"] = "${lecture.id}";
	var count=0;
	var row={};
	<c:forEach items="${oneClassList }" var="oneClass">
	row["dayOfWeek"] = $('#day'+(count+1)+'>option:selected').val()
	row["hour"] = Number($('#hour'+(count+1)).val());
	row["minute"] = Number($('#min'+(count+1)).val());
	row["duration"] = Number($('#duration'+(count+1)).val())/25;
	row["weeks"] = Number($('#left'+(count+1)).val());
	row["oneClassId"] = Number($('#id'+(count+1)).val());
	selectedResult["index"+count] = JSON.stringify(row);
	count++;
	</c:forEach>
	if(!confirm("현재 지정된 시간으로 변경됩니다.\n정말로 진행하시겠습니까?"))
		return;
	selectedResult["book"] = "${lecture.book}";
	selectedResult["method"] = "${purchase.method}";
	$.ajax({
		url : makeUrl("../rebook.do"),
		method : "POST",
		data : selectedResult,
		dataType : "text",
		success:function(msg){
			alert(msg);
			window.location.reload()
		},
		error: function(request,status,error){
			alert(request.responseText);
		}
	});
}

$(document).ready(function(){
	$('#teacherSearchButton').click(function(){
		var button = $(this);
		var purchaseNumber = Number("${param.purchaseNumber}");
		var scrollY = $(window).scrollTop();
		$.ajax({
			url : makeUrl("./adminTeacherSmartMatch.do?purchaseNumber="+purchaseNumber),
			method : "GET",
			dataType : "html",
			success:function(msg){
				$('#light').html(msg);
				scheduleCell.showBox(scrollY, common.init, false, true);
				dialogInit();
				$('#light audio').audioPlayer();
			},
			error: function(request,status,error){
				alert(request.responseText);
			}
		});
	})
	$('#lectureStatusSelect').change(function(){
		if($('#lectureStatusSelect > option:selected').val() == 'Cancel'){
			if(!confirm("이 강의를 취소시키시겠습니까?\n되돌릴수 없거나 힘들 수 있습니다."))
				return;
			alert('취소를 진행합니다.');
			$.ajax({
				url : makeUrl("./adminLectureCancel.do?purchaseNumber=${purchase.id}&lectureStatus=Cancel"),
				method : "GET",
				dataType : "html",
				success:function(msg){
					alert(msg);
					window.location.reload()
				},
				error: function(request,status,error){
					alert(request.responseText);
				}
			});
		}
		if($('#lectureStatusSelect > option:selected').val() == 'Done'){
			if(!confirm("이 강의를 종료시키시겠습니까?\n되돌릴수 없거나 힘들 수 있습니다."))
				return;
			alert('취소를 진행합니다.');
			$.ajax({
				url : "./adminLectureCancel.do?purchaseNumber=${purchase.id}&lectureStatus=Done",
				method : "GET",
				dataType : "html",
				success:function(msg){
					alert(msg);
					window.location.reload()
				},
				error: function(request,status,error){
					alert(request.responseText);
				}
			});
		}
	})
	$('#bookSelect').change(function(){
		var selected = $(':selected', $(this)).val();
		if(!confirm("교재를 변경하시겠습니까?"))
			return;
		var s= { "lectureId" : Number("${lecture.id}"),
				"title" : encodeURIComponent(selected)
		};
		$.ajax({
			url : makeUrl("./adminUpdateLectureBook.do"),
			method : "POST",
			dataType : "html",
			data : s,
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success:function(msg){
				alert(msg);
				window.location.reload()
			},
			error: function(request,status,error){
				alert(request.responseText);
			}
		});
			
	})
	$('.teacherAvail').click(function(){
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./adminViewTeacherAvail.view?teacherId="+$(this).attr('data-id')),
				method : "GET",
				dataType : "html",
				success:function(msg){
					$('#light').html(msg);
					scheduleCell.showBox(scrollY, common.init);
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
		$('.teacherStatus').click(function(){
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./adminViewTeacherStatus.view?teacherId="+$(this).attr('data-id')),
				method : "GET",
				dataType : "html",
				success:function(msg){
					$('#light').html(msg);
					scheduleCell.showBox(scrollY, common.init);
				},
				error: function(request,status,error){
					alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		})
	$("#classStartDate").datepicker({
		dateFormat : 'yy-mm-dd'
	});
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
							<tbody>
								<tr>
									<td>Lecture ID</td>
									<td>${purchase.id }</td>
								</tr>
								<tr>
									<td>StartDate</td>
									<td>
										<input id="classStartDate" value="${lecture.startDateForm }">
										<img onclick='$("#classStartDate").datepicker("show"); return false;' src="../artifact/calendar.png"/>
									</td>
								</tr>
								<tr>
									<td>Teacher Name</td>
									<td>
										<input value="${lecture.teacher.className }" disabled="disabled"/>
										<c:if test="${lc.adminLogin }">
											<button id="teacherSearchButton">Search</button>
										</c:if>
										<a class="teacherAvail" href="#" data-id="${lecture.teacher.id }">Available 보기</a>
										<a class="teacherStatus" href="#" data-id="${lecture.teacher.id }">Schedule Status 보기</a>
									</td>
								</tr>
								<tr>
									<td>Student Name</td>
									<td>${lecture.student.name }(${lecture.student.className })</td>
								</tr>
								<tr>
									<td>Course</td>
									<td>
										<select id="bookSelect">
											<option value="Not yet selected">Not yet selected.</option>
											<c:forEach items="${bookList }" var="book">
												<option value="${book.title }" <c:if test="${lecture.book eq book.title }">selected="selected"</c:if>>${book.title }</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td>Lecture Status</td>
									<td>
										<select id="lectureStatusSelect" <c:if test='${lecture.status ne "OnGoing" }'>disabled="disabled"</c:if>>
											<option value="OnGoing" <c:if test='${lecture.status eq "OnGoing" }'>selected="selected"</c:if>>강의중</option>
											<option value="Done" <c:if test='${lecture.status eq "Done" }'>selected="selected"</c:if>>정상종료</option>
											<option value="TeacherChange" <c:if test='${lecture.status eq "TeacherChange" }'>selected="selected"</c:if>>선생님변경</option>
											<option value="TimeChange" <c:if test='${lecture.status eq "TimeChange" }'>selected="selected"</c:if>>시간변경</option>
											<option value="Cancel" <c:if test='${lecture.status eq "Cancel" }'>selected="selected"</c:if>>강의취소</option>
										</select>
									</td>
								</tr>
								<c:forEach items="${oneClassList }" var="oneClass">
									<c:set scope="request" var="index" value="${count.index }"></c:set>
									<tr>
										<td>Class${index }_Day</td>
										<td>
											<select id="day${index}">
												<option value="Monday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Monday" }'>selected="selected"</c:if>>Monday</option>
												<option value="Tuesday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Tuesday" }'>selected="selected"</c:if>>Tuesday</option>
												<option value="Wednesday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Wednesday" }'>selected="selected"</c:if>>Wednesday</option>
												<option value="Thursday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Thursday" }'>selected="selected"</c:if>>Thursday</option>
												<option value="Friday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Friday" }'>selected="selected"</c:if>>Friday</option>
												<option value="Saturday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Saturday" }'>selected="selected"</c:if>>Saturday</option>
												<option value="Sunday" <c:if test='${oneClass.duration.rt.dayOfWeek eq "Sunday" }'>selected="selected"</c:if>>Sunday</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>Class${index }_StartTime</td>
										<td>
											<input style="width:30px;" id="hour${index }" value="${oneClass.duration.rt.hour }" />
											<select id="min${index }">
												<option value="00" <c:if test='${oneClass.duration.rt.time.minute eq 0 }'>selected="selected"</c:if>>00</option>
												<option value="30" <c:if test='${oneClass.duration.rt.time.minute eq 30 }'>selected="selected"</c:if>>30</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>Class${index }_Duration</td>
										<td>
											<input id="duration${index }" data-duration="${oneClass.duration.duration }" value="${oneClass.duration.duration*25 }" />
											<input type="hidden" value="${lecture.weeks - oneClass.done }" id="left${index}">
											<input type="hidden" value="${oneClass.id }" id="id${index }">
										</td>
									</tr>
								</c:forEach>
							</tbody>

						</table>
						<div style="overflow: hidden; height: auto;">
							<c:if test='${lecture.status eq "OnGoing" and (lc.adminLogin) }'>
								<button onclick="changeTime(); return false;">시간변경</button>
							</c:if>
							<a href="./adminLectureGeneral.view" style="padding: 30px; border: 1px solid black; float: right;">취소</a>
						</div>
						<input id="courseHolder" type="hidden" value="${lecture.course}">
					</div>
				</div>
			</div>
			<!-- end of content -->

		</div>
		<jsp:include page="Tail.jsp" flush="false"></jsp:include>
		
		<div id="light" class="white_content"></div>
		<div id="fade" class="black_overlay"></div>
		<!-- end of main -->
	</div>
	
</body>
</html>