<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
   <jsp:useBean id="student" scope="request" class="java.lang.Object"></jsp:useBean>
   <c:choose>
	<c:when test="${null ne sessionScope.student}">
		<c:set var="student" value="${sessionScope.student }" scope="request"></c:set>
	</c:when>
	<c:otherwise>
		<c:redirect url="./main.jsp"></c:redirect>
	</c:otherwise>
</c:choose>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
<style> 
#registerPanel input{
	font-size: 14px !important;
}
#registerPanel select{
	font-size: 14px !important;
}
#registerExit{
	width: 65px;
	height:65px;
	float:left;
	background-image: url('../artifact/register_exit.png');
	cursor: pointer;
}
.cell{
	float: left;
	height: 26px;
}
.row{
	overflow: hidden;
	height:32px;
	border-bottom: 1px solid #c0c0c0;
	width: 100%;
	margin: 0 auto;
}
.form_label{
	width: 137px;
	height: 31px;
}
.form_content{
	padding-left: 13px;
	padding-top: 5px;
	overflow: hidden;
}
.register_input{
	border: 1px solid #c0c0c0;
	width: 181px;
	height: 21px;
}
</style>
<script>
	$(document).ready(function(){
		$('#registerExit').click(function(){
			scheduleCell.hideBox();
		})
		$('#emailSelect').change(function(){
			var selected = $('#emailSelect>option:selected').val();
			$('input[name="emailHost"]').val(selected);
		})
		$('#submitButton').click(function(){
			var s = {};
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
				url:makeUrl('../changeStudentInformation.edit'),
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
</head>
<body>
<div id="registerPanel" style="overflow: hidden; width: 652px; background: transparent; margin: 0 auto">
	<div style="float: left; width:652px;">
		<div style="width: 577px; height:65px; float:left; background-image: url('../artifact/modify_title.png')"></div>
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
						<input class="align_vertical_center register_input" name="id" type="text" style="margin-right: 8px; float: left;" value="${student.id }" disabled="disabled" />
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
						<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="name" value="${student.name }" />
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_className.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="className" value="${student.className }" />
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_email.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input" type="text" style="margin-right: 2px; float: left; width:112px; height:21px;" name="emailId" value="${student.emailId }" />
						<p style="float:left; color: #c0c0c0; margin: 0 0">@</p>
						<input class="align_vertical_center register_input" type="text" style="margin-right: 2px; float: left; width:112px; height:21px;" name="emailHost" value="${student.emailHost }"/> 
						<select id="emailSelect" class="align_vertical_center" style="width:110px; height:21px;">
							<option value="">사용자 입력</option>
							<option value="naver.com" <c:if test="${student.emailHost eq 'naver.com' }">selected="selected"</c:if>>naver.com</option>
							<option value="gmail.com" <c:if test="${student.emailHost eq 'gmail.com' }">selected="selected"</c:if>>gmail.com</option>
							<option value="daum.net" <c:if test="${student.emailHost eq 'daum.net' }">selected="selected"</c:if>>daum.net</option>
							<option value="msn.com" <c:if test="${student.emailHost eq 'msn.com' }">selected="selected"</c:if>>msn.com</option>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_skype.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="skype" value="${student.skypeId }"/>
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_phone.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left; width:50px; height:21px;" name="phone1" value="${student.getParsedPhone(0) }" />
						<p class="center_text" style="float:left; color: #c0c0c0; margin-right: 8px;">-</p>
						<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left; width:78px; height:21px;" name="phone2" value="${student.getParsedPhone(1) }"/>
						<p class="center_text" style="float:left; color: #c0c0c0; margin-right: 8px;">-</p>
						<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left; width:78px; height:21px;" name="phone3" value="${student.getParsedPhone(2) }"/>
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_birth.png')"></div>
					<div class="cell form_content">
						<select id="emailSelect" class="align_vertical_center" style="width:78px; height:21px;" name="year">
							<option value="default">년도</option>
							<c:forEach var="i" begin="1" end="72">
								<option value="${2017-i }" <c:if test="${student.birthYear eq (2017-i) }">selected="selected"</c:if> >${2017-i }년</option>
							</c:forEach>
						</select>
						<select id="emailSelect" class="align_vertical_center" style="width:78px; height:21px;" name="month">
							<option value="default">월</option>
							<c:forEach var="i" begin="1" end="12">
								<option value="${i }" <c:if test="${student.birthMonth eq (i) }">selected="selected"</c:if>>${i }월</option>
							</c:forEach>
						</select>
						<select id="emailSelect" class="align_vertical_center" style="width:78px; height:21px;" name="date">
							<option value="default">일</option>
							<c:forEach var="i" begin="1" end="31">
								<option value="${i }" <c:if test="${student.birthDate eq (i) }">selected="selected"</c:if>>${i }일</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_zip.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input postcodify_postcode5" type="text" style="margin-right: 8px; float: left;" name="zip" value="${student.ZIP }" />
						<button type="button" class="align_vertical_center" id="postcodify_search_button" style="width:105px; height:21px; background-image: url('../artifact/register_zip_search.png'); border: none;"></button>
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_address.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input postcodify_address" type="text" style="margin-right: 8px; float: left; width:375px;" name="address" value="${student.address }" />
					</div>
				</div>
				<div class="row">
					<div class="cell form_label" style="background-image: url('../artifact/register_detailAddress.png')"></div>
					<div class="cell form_content">
						<input class="align_vertical_center register_input postcodify_details" type="text" style="margin-right: 8px; float: left; width:375px;" name="detailAddress" value="${student.detailAddress }" />
					</div>
				</div>
			</div>
			<div style="height: 25px;"></div>
			<button id="submitButton" style="width: 128px; height: 37px; background-color: rgb(185,17,32) ; margin: 0 auto; display: block; border: none; font-weight: bold ; color: white; font-size: 20px;">정보수정</button>
			<div style="height: 30px;"></div>

		</div>
	</div>
	</div>
</div>
</body>
</html>