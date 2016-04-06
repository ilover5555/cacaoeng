<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
	
	$(document).ready(function(){
		$('.saveButton').click(function(){
			var id = $(this).attr('data-id');
			var msg = $('#'+id+'TextArea').val();
			var subject = $('#'+id+'Input').val();
			var s = {"type" : id, "msg" : msg, "subject" : subject};
			$.ajax({
				type:'POST',
				url:makeUrl('./adminMailMessageSet.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		
		$('.modeSelect').change(function(){
			$.ajax({
				type:'GET',
				url:makeUrl('./adminMailModeSet.do?type='+$(this).attr('name')+'&mode='+$(this).attr('value')),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				success: function(data){
					alert(data);
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		
		textProcessHandler('all');
		textProcessHandler('new');
		textProcessHandler('lecture');
		textProcessHandler('re');
		textProcessHandler('re_after');
		textProcessHandler('pw');
	})
</script>
</head>
<body>

	<div id="tooplate_wrapper">
    	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	    <div id="tooplate_main">
	    	<div id="tooplate_content">
	    	<h2>Email 알림 관리</h2>
				<div style="overflow: hidden;">
				<h4>선생님 등록 완료</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="confirm_sort" value="auto" <c:if test="${EVENT_MAIL_MODE_TEACHER_CONFIRM eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="confirm_sort" value="manual" <c:if test="${EVENT_MAIL_MODE_TEACHER_CONFIRM eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right;">
									<button data-id="confirm" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<input id="confirmInput" class="form-control" style="margin-bottom: 5px;" value="${EVENT_MAIL_SUBJECT_TEACHER_CONFIRM }"/>
								<textarea id="confirmTextArea" rows="15" class="form-control" name="comment" style="z-index: 0">${EVENT_MAIL_TEXT_TEACHER_CONFIRM }</textarea>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>수강신청 완료</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="lecture_sort" value="auto" <c:if test="${EVENT_MAIL_MODE_LECTURE_REGISTERED eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="lecture_sort" value="manual" <c:if test="${EVENT_MAIL_MODE_LECTURE_REGISTERED eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right;">
									<button data-id="lecture" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<input id="lectureInput" class="form-control" style="margin-bottom: 5px;" value="${EVENT_MAIL_SUBJECT_LECTURE_REGISTERED }"/>
								<textarea id="lectureTextArea" rows="15" class="form-control" name="comment" style="z-index: 0">${EVENT_MAIL_TEXT_LECTURE_REGISTERED }</textarea>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>월급 지불</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="payed_sort" value="auto" <c:if test="${EVENT_MAIL_MODE_PAYED eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="payed_sort" value="manual" <c:if test="${EVENT_MAIL_MODE_PAYED eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right;">
									<button data-id="payed" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<input id="payedInput" class="form-control" style="margin-bottom: 5px;" value="${EVENT_MAIL_SUBJECT_PAYED }"/>
								<textarea id="payedTextArea" rows="15" class="form-control" name="comment" style="z-index: 0">${EVENT_MAIL_TEXT_PAYED }</textarea>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>Level Test Demo Class 요청</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="level_sort" value="auto" <c:if test="${EVENT_MAIL_MODE_LEVEL_TEST eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="level_sort" value="manual" <c:if test="${EVENT_MAIL_MODE_LEVEL_TEST eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right;">
									<button data-id="level" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<input id="levelInput" class="form-control" style="margin-bottom: 5px;" value="${EVENT_MAIL_SUBJECT_LEVEL_TEST }"/>
								<textarea id="levelTextArea" rows="15" class="form-control" name="comment" style="z-index: 0">${EVENT_MAIL_TEXT_LEVEL_TEST }</textarea>
							</div>
						</div>
					</div>
				</div>
				
			</div> <!-- end of content -->
		</div>	<!-- end of main -->
    </div>
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>