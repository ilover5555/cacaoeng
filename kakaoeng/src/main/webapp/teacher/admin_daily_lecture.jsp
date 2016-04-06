<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="classList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="count" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="holiday" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="prev" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="next" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="http://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"/>
<script type="text/javascript" src="http://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var sendObj = null;
function receiveMessage(event)
{	sendObj["popup"].postMessage(sendObj["sendData"], '*');
}
window.addEventListener("message", receiveMessage, false);
var tableSel = null;
$(function() {
	tableSel = $("#generalLectureTable");
	$("#generalLectureTable").dataTable({
		columnDefs : [{"targets":'no-sort', "orderable":false,},
		              {"targets":0, "orderable":false, "visible" : false}
		              ]		
	});
});



$.fn.dataTableExt.afnFiltering.push(
   	function( oSettings, aData, iDataIndex ) {
	   	for(var i=0; i<=7; i++){
	   		var rowData = aData[i].toUpperCase();
	   		
	   		
	   		var input = oSettings.oPreviousSearch.sSearch.toUpperCase();
	     
			if(rowData.indexOf(input) != -1)
				return true;
	   	}
	   	var rowData = aData[i];
   		
	   	var data = rowData.split(' ')[0].toUpperCase();
   		var input = oSettings.oPreviousSearch.sSearch.toUpperCase();
     
		if(data.indexOf(input) != -1)
			return true;
	   	
   	}
);
$(document).ready(function(){
	
	$("#classStartDate").datepicker({
		dateFormat : 'yy-mm-dd',
		onSelect : function(dateText){
			window.location = './adminDailyClass.view?baseDate='+dateText;
		}
	});
	$('#classStartDate').datepicker('setDate', '+1');
	
	$('#checkAll').change(function(){
		
		if($('#checkAll:checked').length == 1){
			$.each($('.manageCheck'), function(index, value){
				value.checked=true;
			})
		}
		else{
			$.each($('.manageCheck'), function(index, value){
				value.checked=false;
			})
		}
	})
	
	$('#sendSMS').click(function(){
		var checked = $('.manageCheck:checked');
		var selected = [];
		checked.each(function(){
			selected.push($(this).attr('data-id'));
		})
		var s ={"selected" :  JSON.stringify(selected)};
		$.ajax({
			type:'POST',
			url:makeUrl('./adminRequestSMSPopup.do'),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
			success: function(data){
				var result = JSON.parse(data);
				var sendList = result["sendList"];
				var selectedJSON = result["selectedJSON"];
				var e = window.open(makeUrl("./admin_sms_popup.jsp"), "", "width=550, height=300, resizable=0, scrollbars=0, status=0;");
				sendObj = {"sendData" : data, "popup" : e};
				
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
		
		
		return false;
	})
	
	
	<c:if test="${lc.adminLogin}">
	$('.statusSelect').change(function(){
		var state = $('option:selected', $(this)).val();
		var type = $(this).attr('data-type');
		var oneClassId = $(this).attr('data-id');
		var classLogId = $(this).attr('data-log-id');
		var reason = "";
		var s = {"reason":reason};
		if(state == 'No'){
			alert('아무런 기능도 정의 되지 않았습니다.');
			return;
		}
		var url = makeUrl('./adminLectureClassLogUpdate.do?type='+type+'&baseDate=${param.baseDate}&classState='+state+'&oneClassId='+oneClassId+'&classLogId='+classLogId);
		$.ajax({
			type:'POST',
			url: url,
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
	</c:if>
})
</script>

<c:if test="${lc.execLogin }">
<script>
$(document).ready(function(){
	$('select').attr('disabled', 'disabled');
})
</script>
</c:if>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">
				<h1>수업 상황표</h1>
				<div>
					<div style="width: 100%">
					
						
						<div style="text-align: center;">
							<a href="./adminDailyClass.view?baseDate=${prev }" style="font-size: 50px;"><i class="fa fa-arrow-circle-left"></i></a>
							<span style="font-size : 50px; margin: 0 10px;">${baseDate}</span>
							<a href="./adminDailyClass.view?baseDate=${next }" style="font-size: 50px;"><i class="fa fa-arrow-circle-right"></i></a>
						</div>
						<div style="text-align: center">
							<input type="text" id="classStartDate"  style="display: none;">
							<span class="glyphicon glyphicon-calendar" style="font-size: 20px;" onclick='$("#classStartDate").datepicker("show"); return false;'></span>
						</div>
					
						<c:if test="${holiday eq true }">
							<h1>Today is HOLIDAY</h1>
						</c:if>
						
						<div style="height: 30px;"></div>
						
						<button id="sendSMS" class="btn btn-info" style="margin-bottom: 10px;">Send SMS</button>
						
						<table id="generalLectureTable" class="sortable table table-bordered">

							<thead>
								<tr>
									<th></th>
									<th style="text-align: center;" class="no-sort"><input type="checkbox" id="checkAll" /></th>
									<th>Teacher</th>
									<th>Student</th>
									<th>시작시간</th>
									<th>수업</th>
									<th>ClassName</th>
									<th>ClassStatus</th>
								</tr>
							</thead>
								<c:forEach items="${classList }" var="oneClass">
									<tr>
										<td></td>
										<td style="text-align: center;"><input class="manageCheck" type="checkbox" data-id="${oneClass.oneClass.studentId }" /></td>
										<td>${oneClass.teacherClassName }</td>
										<td>${oneClass.studentClassName }</td>
										<td>${oneClass.oneClass.duration.rt.startTimeFormat }</td>
										<td>${oneClass.oneClass.duration.duration*25 }</td>
										<td>${oneClass.book }</td>
										<td style="overflow: hidden;">
											<span style="display: none">${oneClass.classLog.classState }</span>
											<select style="float:left;" class="statusSelect" data-id="${oneClass.oneClass.id }" data-log-id="${oneClass.classLog.id }" <c:if test="${oneClass.classLog eq null }">data-type="new"</c:if> <c:if test="${oneClass.classLog ne null }">data-type="modify"</c:if>>
												<option value="No"> </option>
												<option value="Completed" <c:if test="${oneClass.classLog.classState eq 'Completed' }">selected="selected"</c:if>>Completed</option>
												<option value="AbsentStudent" <c:if test="${oneClass.classLog.classState eq 'AbsentStudent' }">selected="selected"</c:if>>AbsentStudent</option>
												<option value="AbsentTeacher" <c:if test="${oneClass.classLog.classState eq 'AbsentTeacher' }">selected="selected"</c:if>>AbsentTeacher</option>
												<option value="PostponeStudent" <c:if test="${oneClass.classLog.classState eq 'PostponeStudent' }">selected="selected"</c:if>>PostponeStudent</option>
												<option value="PostponeTeacher" <c:if test="${oneClass.classLog.classState eq 'PostponeTeacher' }">selected="selected"</c:if>>PostponeTeacher</option>
												<option value="Uncompleted" <c:if test="${oneClass.classLog.classState eq 'Uncompleted' }">selected="selected"</c:if>>Uncompleted</option>
												<option value="Uncompleted_100" <c:if test="${oneClass.classLog.classState eq 'Uncompleted_100' }">selected="selected"</c:if>>Uncompleted_100</option>
												<option value="Uncompleted_50" <c:if test="${oneClass.classLog.classState eq 'Uncompleted_50' }">selected="selected"</c:if>>Uncompleted_50</option>
												<option value="Uncompleted_30" <c:if test="${oneClass.classLog.classState eq 'Uncompleted_30' }">selected="selected"</c:if>>Uncompleted_30</option>
												<option value="LevelTestReserved" <c:if test="${oneClass.classLog.classState eq 'LevelTestReserved' }">selected="selected"</c:if>>LevelTestReserved</option>
												<option value="LevelTestCompleted" <c:if test="${oneClass.classLog.classState eq 'LevelTestCompleted' }">selected="selected"</c:if>>LevelTestCompleted</option>
												<option value="LevelTestUncompleted" <c:if test="${oneClass.classLog.classState eq 'LevelTestUncompleted' }">selected="selected"</c:if>>LevelTestUncompleted</option>
											</select>
											<c:choose>
												<c:when test="${(oneClass.classLog.classState eq 'LevelTestCompleted')}">
													<button onclick='common.PopupCenter(makeUrl("./adminLevelTestReport.do?classLogId=${oneClass.classLog.id }&oneClassId=${oneClass.oneClass.id}&date=${baseDate}"), "", "530", "505"); return false;' class="btn btn-primary" style="border-radius:100%; padding: 0; width: 20px; height:20px; float: right;"></button>
												</c:when>
												<c:when test="${(oneClass.classLog.classState eq 'LevelTestUncompleted')}">
													<button onclick='common.PopupCenter(makeUrl("./adminLevelTestReport.do?classLogId=${oneClass.classLog.id }&oneClassId=${oneClass.oneClass.id}&date=${baseDate}"), "", "530", "505"); return false;' class="btn btn-primary" style="border-radius:100%; padding: 0; width: 20px; height:20px; float: right;"></button>
												</c:when>
												<c:when test="${(oneClass.classLog.classState eq 'Uncompleted')}">
													<button onclick='common.PopupCenter(makeUrl("./adminClassReport.do?classLogId=${oneClass.classLog.id }&oneClassId=${oneClass.oneClass.id}&date=${baseDate}"), "", "530", "430");", "", "width=530, height=430, resizable=0, scrollbars=0, status=0;"); return false;' class="btn btn-danger" style="border-radius:100%; padding: 0; width: 20px; height:20px; float: right;"></button>
												</c:when>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							<tbody>
								
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