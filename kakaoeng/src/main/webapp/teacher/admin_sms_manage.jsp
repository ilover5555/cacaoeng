<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="ALL_MESSAGE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="NEW_REGISTER_MESSAGE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="NEW_LECTURE_MESSAGE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="REQUEST_LECTURE_MESSAGE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="QUERY_PW_MESSAGE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="AFETER_REQUEST_LECTURE_MESSAGE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="NEW_REGISTER_MODE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="LECTURE_REGISTER_MODE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="REQUEST_LECTURE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="AFTER_REQUEST_LECTURE_MODE" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="LECTURE_REGISTER_DIFFER" scope="request" type="java.lang.Integer"></jsp:useBean>
<jsp:useBean id="AFTER_LECTURE_REGISTER_DIFFER" scope="request" type="java.lang.Integer"></jsp:useBean>
<jsp:useBean id="inLectureStudentList" scope="request" class="java.util.ArrayList"></jsp:useBean>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
	function getSMSTextLength(str) {
	    var len = 0;
	    for (var i = 0; i < str.length; i++) {
	        if (escape(str.charAt(i)).length == 6) {
	            len++;
	        }
	        len++;
	    }
	    return len;
	}

	var inLectureList = [];
	
	function textProcessHandler(name){
		$('#'+name+'Bytes').html(getSMSTextLength($('#'+name+'TextArea').val())+'bytes');
		$('#'+name+'TextArea').keyup(function(){
			var bytes = getSMSTextLength($(this).val())
			$('#'+name+'Bytes').html(bytes+'bytes');
		})
	}
	
	$(document).ready(function(){
		<c:forEach items="${inLectureStudentList}" var="student">inLectureList.push("${student.name}(${student.cellPhone})");</c:forEach>
		
		$("#loginBaseDate").datepicker({
			dateFormat : 'yy-mm-dd',
			onSelect : function(date){
				$.ajax({
					type:'GET',
					url:makeUrl('./getStudentListByLastLogion.get?baseDate='+date),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						var result =  JSON.parse(data);
						$('#allNumber').html(result["number"]);
						var list = result["list"]
						$('#sendList').val(list);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}
		});
		
		$("#classBaseDate").datepicker({
			dateFormat : 'yy-mm-dd',
			onSelect : function(date){
				$.ajax({
					type:'GET',
					url:makeUrl('./getStudentListByClassDate.get?baseDate='+date),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						var result =  JSON.parse(data);
						$('#inDateNumber').html(result["number"]);
						var list = result["list"]
						$('#sendList').val(list);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}
		});
		
		$('#sendAll').click(function(){
			if($('input[name="all_sort"]:checked').val() == 'all' && $("#loginBaseDate").val() != ''){
				$.ajax({
					type:'POST',
					url:makeUrl('./getStudentListByLastLogion.get?baseDate='+$("#loginBaseDate").val()),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						alert(data);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}
			if($('input[name="all_sort"]:checked').val() == 'inLecture'){
				$.ajax({
					type:'POST',
					url:makeUrl('./adminSMSManage.view'),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						alert(data);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}
			if($('input[name="all_sort"]:checked').val() == 'inDate' && $("#classBaseDate").val() != ''){
				$.ajax({
					type:'POST',
					url:makeUrl('./getStudentListByClassDate.get?baseDate='+$("#classBaseDate").val()),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						alert(data);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}
		})
		
		$('input[name="all_sort"]').click(function(){
			$('#sendList').val('');
			if($(this).attr('value') == 'all' && $("#loginBaseDate").val() != ''){
				$.ajax({
					type:'GET',
					url:makeUrl('./getStudentListByLastLogion.get?baseDate='+$("#loginBaseDate").val()),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						var result =  JSON.parse(data);
						$('#inDateNumber').html(result["number"]);
						var list = result["list"]
						$('#sendList').val(list);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}
			if($(this).attr('value') == 'inLecture'){
				$('#sendList').val(inLectureList);
			}
			if($(this).attr('value') == 'inDate' && $("#classBaseDate").val() != ''){
				$.ajax({
					type:'GET',
					url:makeUrl('./getStudentListByClassDate.get?baseDate='+$("#classBaseDate").val()),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						var result =  JSON.parse(data);
						$('#inDateNumber').html(result["number"]);
						var list = result["list"]
						$('#sendList').val(list);
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
			}

		})
		
		$('.saveButton').click(function(){
			var id = $(this).attr('data-id');
			var msg = $('#'+id+'TextArea').val();
			var s = {"type" : id, "msg" : msg};
			$.ajax({
				type:'POST',
				url:makeUrl('./adminSMSMessageSet.do'),
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
		
		$('.sendSMS').click(function(){
			var id = $(this).attr('data-id');
			$.ajax({
				type:'GET',
				url:makeUrl('./adminSMSRequestLEctureManual.do?type='+id),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				success: function(data){
					var result =  JSON.parse(data);
					alert(result["result"]);
					$('#'+id+'SendList').val(result["list"]);
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		
		$('.saveDifferButton').click(function(){
			var id = $(this).attr('data-id');
			var differ = $('#'+id+'Differ').val();
			var s = {"type" : id, "differ" : differ};
			$.ajax({
				type:'POST',
				url:makeUrl('./adminSMSDifferSet.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					var result =  JSON.parse(data);
					alert(result["result"]);
					$('#'+id+'SendList').val(result["list"]);
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
		
		$('.modeSelect').change(function(){
			$.ajax({
				type:'GET',
				url:makeUrl('./adminSMSModeSet.do?type='+$(this).attr('name')+'&mode='+$(this).attr('value')),
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
	    	<h2>SMS 관리</h2>
	    		<div style="overflow: hidden;">
	    		<h4>SMS 전체 발송</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio">
								<label style="display: inline-block ; vertical-align: middle;"><input type="radio" name="all_sort" value="all">전체회원발송(마지막 접속일:<input class="date" id="loginBaseDate" type="text" name="loginBaseDate" style="display: inline; width: 85px; height:26px;" disabled="disabled"><span class="glyphicon glyphicon-calendar" style="font-size: 20px; vertical-align: sub;" onclick='$("#loginBaseDate").datepicker("show"); return false;'></span>)[회원수:<span id="allNumber"></span>명]</label>
							</div>
							<div class="radio">
								<label style="overflow: hidden; display: block;">
									<span style="float: left;"><input type="radio" name="all_sort" value="inLecture">수강중인회원 발송 [회원수:<span>${inLectureStudentList.size() }</span>명]</span>
								</label>
							</div>
							<div class="radio">
								<label style="overflow: hidden; display: block;">
									<span style="float: left;"><input type="radio" name="all_sort" value="inDate">수업일기준발송(수업일:<input class="date" id="classBaseDate" type="text" name="classBaseDate" style="display: inline; width: 85px; height:26px;" disabled="disabled"><span class="glyphicon glyphicon-calendar" style="font-size: 20px;vertical-align: sub;" onclick='$("#classBaseDate").datepicker("show"); return false;'></span>)[회원수:<span id="inDateNumber"></span>명]</span>
									<span style="float: right;"> 
										<button data-id="all" class="btn btn-primary saveButton">저장</button>
										<button id="sendAll" class="btn btn-info" >발송</button>
									</span>
								</label>
							</div>
							
							<div style="width: 100%; margin-bottom: 10px;" class="input-group">
								<textarea id="sendList" rows="5" class="form-control" style="z-index: 0" disabled="disabled"></textarea>
							</div>
							<div style="width: 100%" class="input-group">
								<textarea id="allTextArea" rows="5" class="form-control" name="comment" style="z-index: 0">${ALL_MESSAGE }</textarea>
								<span id="allBytes">0bytes</span>
							</div>
							
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>신규 가입 SMS</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="new_sort" value="auto" <c:if test="${NEW_REGISTER_MODE eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="new_sort" value="manual" <c:if test="${NEW_REGISTER_MODE eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right;">
									<button data-id="new" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<textarea id="newTextArea" rows="5" class="form-control" name="comment" style="z-index: 0">${NEW_REGISTER_MESSAGE }</textarea>
								<span id="newBytes">0bytes</span>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>수강신청완료</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="lecture_sort" value="auto" <c:if test="${LECTURE_REGISTER_MODE eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="lecture_sort" value="manual" <c:if test="${LECTURE_REGISTER_MODE eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right;">
									<button data-id="lecture" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<textarea id="lectureTextArea" rows="5" class="form-control" name="comment" style="z-index: 0">${NEW_LECTURE_MESSAGE }</textarea>
								<span id="lectureBytes">0bytes</span>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>재수강 요청(전)</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="re_sort" value="auto" <c:if test="${REQUEST_LECTURE eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="re_sort" value="manual" <c:if test="${REQUEST_LECTURE eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right; overflow: hidden;">
								
									<button data-id="re" class="btn btn-primary saveButton" style="float: left;">저장</button>
									<button data-id="re" class="btn btn-info sendSMS" style="float:left">발송</button>
									<input id="reDiffer" value="${LECTURE_REGISTER_DIFFER }" style="float: left; height:34px; width : 30px; margin-left: 10px;">
									<button data-id="re" class="btn btn-primary saveDifferButton" style="float:left">Differ 저장</button>
									
								</span>
							</div>
							<div style="width: 100%; margin-bottom: 10px;" class="input-group">
								<textarea id="reSendList" rows="5" class="form-control" style="z-index: 0" disabled="disabled"></textarea>
							</div>
							<div style="width: 100%" class="input-group">
								<textarea id="reTextArea" rows="5" class="form-control" name="comment" style="z-index: 0">${REQUEST_LECTURE_MESSAGE }</textarea>
								<span id="reBytes">0bytes</span>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>재수강 요청(후)</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<label><input class="modeSelect" type="radio" name="re_after_sort" value="auto" <c:if test="${AFTER_REQUEST_LECTURE_MODE eq 'auto' }">checked="checked"</c:if>>자동</label>
								<label><input class="modeSelect" type="radio" name="re_after_sort" value="manual" <c:if test="${AFTER_REQUEST_LECTURE_MODE eq 'manual' }">checked="checked"</c:if>>수동</label>
								<span style="float: right; overflow: hidden;">
									<button data-id="re_after" class="btn btn-primary saveButton" style="float: left;">저장</button>
									<button data-id="re_after" class="btn btn-info sendSMS" style="float:left">발송</button>
									<input id="re_afterDiffer" value="${AFTER_LECTURE_REGISTER_DIFFER }" style="float: left; height:34px; width : 30px; margin-left: 10px;">
									<button data-id="re_after" class="btn btn-primary saveDifferButton" style="float:left;">Differ 저장</button>
									
								</span>
							</div>
							<div style="width: 100%; margin-bottom: 10px;" class="input-group">
								<textarea id="re_afterSendList" rows="5" class="form-control" style="z-index: 0" disabled="disabled"></textarea>
							</div>
							<div style="width: 100%" class="input-group">
								<textarea id="re_afterTextArea" rows="5" class="form-control" name="comment" style="z-index: 0">${AFETER_REQUEST_LECTURE_MESSAGE }</textarea>
								<span id="re_afterBytes">0bytes</span>
							</div>
						</div>
					</div>
				</div>
				
				<div style="overflow: hidden;">
				<h4>ID 비밀번호 찾기 SMS</h4>
		    		<div class="form-group" style="float:left; width:100%; height:100%; margin-right: 2.6%">
						<div style="width: 100%" class="input-group">
							<div class="radio" style="overflow: hidden;">
								<span style="float: right;">
									<button data-id="pw" class="btn btn-primary saveButton">저장</button>
								</span>
							</div>
								
							<div style="width: 100%" class="input-group">
								<textarea id="pwTextArea" rows="5" class="form-control" name="comment" style="z-index: 0">${QUERY_PW_MESSAGE }</textarea>
								<span id="pwBytes">0bytes</span>
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