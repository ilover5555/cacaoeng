<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
<script src="../js/scheduleCell.js"></script>
<script src="../js/bootstrap.js"></script>
<link href="../css/bootstrap.css" rel="stylesheet">
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>
<title>Insert title here</title>
<style>
#registerPanel input{
	font-size: 14px !important;
}
#registerPanel select{
	font-size: 14px !important;
}
.cell{
	float: left;
	height: 100%;
}
.row{
	height:32px;
	border-bottom: 1px solid #c0c0c0;
	width: 100%;
	margin: 0 auto;
	clear: both;
}
.form_label{
	width: 137px;
	height: 31px;
}
.form_content{
	padding-left: 13px;
	padding-top: 0;
	overflow: hidden;
	max-width: 400px;
}
.register_input{
	border: 1px solid #c0c0c0;
	width: 181px;
	height: 21px;
}
.white_content{
	background-color: transparent;
}
#registerExit{
	width: 65px;
	height:65px;
	float:left;
	background-image: url('../artifact/register_exit.png');
	cursor: pointer;
}
</style>
<script>
	var duplicateChecked = false;
	$(document).ready(function(){
		$('[name="id"]').keydown(function(){
			duplicateChecked = false;
		})
		$('#emailSelect').change(function(){
			var selected = $('#emailSelect>option:selected').val();
			$('input[name="emailHost"]').val(selected);
		})
		$('#registerExit').click(function(){
			scheduleCell.hideBox();
		})
		$('#duplicateCheck').click(function(){
				$.ajax({
					type:'GET',
					url:makeUrl('../duplicateIdCheck.do?id='+$('[name="id"]').val()),
					contentType:'application/x-www-form-urlencoded; charset=UTF-8',
					datatype: 'text/plain',
					success: function(data){
						alert(data);
						duplicateChecked = true;
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
					},
				})
		})
		$('#submitButton').click(function(){
			if(duplicateChecked == false){
				alert('아이디중복체크를 해주세요');
				return;
			}
			var s = {};
			s["id"] = $('[name="id"]').val()
			s["pw"] = $('[name="pw"]').val()
			s["pwConfirm"] = $('[name="pwConfirm"]').val()
			s["name"] = $('[name="name"]').val()
			s["className"] = $('[name="className"]').val()
			s["emailId"] = $('[name="emailId"]').val()
			s["emailHost"] = $('[name="emailHost"]').val()
			s["skype"] = $('[name="skype"]').val()
			s["phone1"] = $('[name="phone1"]').val()
			s["phone2"] = $('[name="phone2"]').val()
			s["phone3"] = $('[name="phone3"]').val()
			s["year"] = $('[name="year"]').val()
			s["month"] = $('[name="month"]').val()
			s["date"] = $('[name="date"]').val()
			s["zip"] = $('[name="zip"]').val()
			s["address"] = $('[name="address"]').val()
			s["detailAddress"] = $('[name="detailAddress"]').val()
			$.ajax({
				type:'POST',
				url:makeUrl('../registerStudent.do'),
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
		$(function() { 
			$("#postcodify_search_button").postcodifyPopUp(); 
			});
	})
</script>
<script>  </script>
</head>
<body style="background-color: transparent;">
	<c:if test="${(null ne sessionScope.student) or (null ne sessionScope.admin) or (null ne sessionScope.exe) }">
		<c:redirect url="./main.jsp"></c:redirect>
	</c:if>
	
	<div id="registerPanel" style="overflow: hidden; width: 642px; background: transparent;">
		<div style="width: 577px; height:65px; float:left; background-image: url('../artifact/register_title.png')"></div>
		<div id="registerExit"></div>
		<div style="clear: both;"></div>
		<div style="float: left; width:577px; background-color: white;">
			<div style="height:24px;"></div>
			<div style="width:314px; height:86px; margin: 0 auto; background-image: url('../artifact/register_logo.png')"></div>
			<div style="height:24px;"></div>
			<div style="width:545px; margin: 0 auto;">
				<div style="width:100%; margin: 0 auto;">
					<div style="background-image: url('../artifact/register_subtitle.png'); width:151px; height:15px; margin-left: 20px; margin-bottom: 8px;"></div>
					<div class="row" style="border-top: 2px solid #c0c0c0; height: 32px;">
						<div class="cell form_label" style="background-image: url('../artifact/register_id.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" name="id" type="text" style="margin-right: 8px; float: left;" />
							<button id="duplicateCheck" class="align_vertical_center" type="button" style="width: 105px; height: 21px; background-image: url('../artifact/register_duplicate_check.png'); border: none; float: left;"></button>
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_pw.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" type="password" style="margin-right: 8px; float: left;" name="pw" />
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_pwConfirm.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" type="password" style="margin-right: 8px; float: left;" name="pwConfirm" />
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_name.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="name" />
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_className.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="className"   data-toggle="tooltip" data-placement="right" title="선생님과 수업 진행시 사용될 영어이름입니다."/>
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_email.png');"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" type="text" style="margin-right: 2px; float: left; width:125px; height:21px; font-size: 14px;" name="emailId" />
							<p class="center_text" style="float:left; color: #c0c0c0; font-size: 14px;">@</p>
							<input class="align_vertical_center register_input" type="text" style="margin-right: 2px; float: left; width:125px; height:21px; font-size: 14px;" name="emailHost" value="naver.com" /> 
							<select id="emailSelect" class="align_vertical_center" style="width:110px; height:21px; font-size: 14px;">
								<option value="naver.com">naver.com</option>
								<option value="gmail.com">gmail.com</option>
								<option value="daum.net">daum.net</option>
								<option value="msn.com">msn.com</option>
								<option value="">사용자 입력</option>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_skype.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="skype" data-toggle="tooltip" data-placement="right" title="Skype를 설치하시고 ID를 만드세요. 공지사항 참조."/>
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_phone.png')"></div>
						<div class="cell form_content" data-toggle="tooltip" data-placement="bottom" title="핸드폰 번호를 입력하셔야 수업관련 메시지를 받으실 수 있습니다.">
							<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left; width:50px; height:21px;" name="phone1" />
							<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left; width:78px; height:21px;" name="phone2" />
							<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left; width:78px; height:21px;" name="phone3" />
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_birth.png')"></div>
						<div class="cell form_content">
							<select id="emailSelect" class="align_vertical_center" style="width:78px; height:21px;" name="year">
								<option value="default">년도</option>
								<c:forEach var="i" begin="1" end="72">
									<option value="${2017-i }">${2017-i }년</option>
								</c:forEach>
							</select>
							<select id="emailSelect" class="align_vertical_center" style="width:78px; height:21px;" name="month">
								<option value="default">월</option>
								<c:forEach var="i" begin="1" end="12">
									<option value="${i }">${i }월</option>
								</c:forEach>
							</select>
							<select id="emailSelect" class="align_vertical_center" style="width:78px; height:21px;" name="date">
								<option value="default">일</option>
								<c:forEach var="i" begin="1" end="31">
									<option value="${i }">${i }일</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_zip.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input postcodify_postcode5" type="text" style="margin-right: 8px; float: left;" name="zip" />
							<button type="button" class="align_vertical_center" id="postcodify_search_button" style="width:105px; height:21px; background-image: url('../artifact/register_zip_search.png'); border: none;"></button>
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_address.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input postcodify_address" type="text" style="margin-right: 8px; float: left; width:375px;" name="address" />
						</div>
					</div>
					<div class="row">
						<div class="cell form_label" style="background-image: url('../artifact/register_detailAddress.png')"></div>
						<div class="cell form_content">
							<input class="align_vertical_center register_input postcodify_details" type="text" style="margin-right: 8px; float: left; width:375px;" name="detailAddress" />
						</div>
					</div>
				</div>
				<div style="height: 25px;"></div>
				<button id="submitButton" style="width: 128px; height: 37px; background-image: url('../artifact/register_register.png'); margin: 0 auto; display: block; border: none;"></button>
				<div style="height: 30px;"></div>
			</div>
		</div>
		
		
	</div>
</body>
</html>