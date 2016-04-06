<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="student" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="levelTestLog" scope="request" class="java.lang.Object"></jsp:useBean>
<jsp:useBean id="teacher" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
<style>
.cell{
	float: left;
	height: 100%;
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
	padding-top: 0px;
	overflow: hidden;
}
.register_input{
	border: 1px solid #c0c0c0;
	width: 181px;
	height: 21px;
}
</style>
<c:if test="${lc.execLogin }">
<script>
$(document).ready(function(){
	$('select').attr('disabled', 'disabled');
	$('input').attr('disabled', 'disabled');
	$('textarea').attr('disabled', 'disabled');
})
</script>
</c:if>

<c:if test="${lc.adminLogin }">
<script >
	$(document).ready(function(){
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
				url:makeUrl('./adminChangeStudentInformation.edit?studentId=${student.id}'),
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
		$('#adminSubmitButton').click(function(){
			var s = {};
			s["level"] = $('[name="level"]').val();
			s["coupon"] = $('[name="coupon"]').val();
			s["note"] = $('[name="note"]').val();
			$.ajax({
				type:'POST',
				url:makeUrl('./adminChagneStudentAdminInfo.edit?studentId=${student.id}'),
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
</c:if>
</head>
<body>
		

	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        
        	
			<div style="overflow: hidden; width: 642px; background: transparent; margin: 0 auto">
				<div style="float: left; width:577px;">
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
						<c:if test="${lc.adminLogin }">
							<button id="submitButton" style="width: 128px; height: 37px; background-color: rgb(185,17,32) ; margin: 0 auto; display: block; border: none; font-weight: bold ; color: white; font-size: 20px;">정보수정</button>
						</c:if>
						<div style="height: 30px;"></div>
						<div style="width:100%; margin: 0 auto;">
							<div style="background-image: url('../artifact/register_subtitle.png'); width:151px; height:15px; margin-left: 20px; margin-bottom: 8px;"></div>
							<div class="row" style="border-top: 2px solid #c0c0c0; height: 31px;">
								<div class="cell form_label" style="background-color: rgb(238,238,238)"><p style="font-size: 12px; text-align: center; margin-top: 5px; margin-bottom: 5px; font-weight: bolder;">테스트 날짜</p></div>
								<div class="cell form_content"> 
									<input disabled="disabled" class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" value="${levelTestLog.startDate }" />
								</div>
							</div>
							<div class="row" style="border-top: 2px solid #c0c0c0; height: 31px;">
								<div class="cell form_label" style="background-color: rgb(238,238,238)"><p style="font-size: 12px; text-align: center; margin-top: 5px; margin-bottom: 5px; font-weight: bolder;">테스트 선생님</p></div>
								<div class="cell form_content"> 
									<input disabled="disabled" class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" value="${teacher.className }" />
								</div>
							</div>
							<div class="row" style="border-top: 2px solid #c0c0c0; height: 31px;">
								<div class="cell form_label" style="background-color: rgb(238,238,238)"><p style="margin-top:5px ;font-size: 12px; text-align: center; font-weight: bolder;">레벨</p></div>
								<div class="cell form_content"> 
									<select id="levelSelect" class="align_vertical_center" style="width:150px; height:21px;" name="level">
										<option value="Untested" <c:if test="${student.level eq 'Untested' }">selected="selected"</c:if>>레벨 없음</option>
										<option value="Level1" <c:if test="${student.level eq 'Level1' }">selected="selected"</c:if>>Level1</option>
										<option value="Level2" <c:if test="${student.level eq 'Level2' }">selected="selected"</c:if>>Level2</option>
										<option value="Level3" <c:if test="${student.level eq 'Level3' }">selected="selected"</c:if>>Level3</option>
										<option value="Level4" <c:if test="${student.level eq 'Level4' }">selected="selected"</c:if>>Level4</option>
										<option value="Level5" <c:if test="${student.level eq 'Level5' }">selected="selected"</c:if>>Level5</option>
										<option value="Level6" <c:if test="${student.level eq 'Level6' }">selected="selected"</c:if>>Level6</option>
										<option value="Level7" <c:if test="${student.level eq 'Level7' }">selected="selected"</c:if>>Level7</option>
									</select>
								</div>
							</div>
							<div class="row" style="border-top: 2px solid #c0c0c0; height: 230px;">
								<div class="cell form_label" style="background-color: rgb(238,238,238); height: 100%"><p class="center_text" style="font-size: 12px; text-align: center; margin-top: 8px; margin-bottom: 8px; font-weight: bolder;">학생평가</p></div>
								<div class="cell form_content" style="height: 100%; padding-top: 5px;"> 
									<textarea style="width: 350px; height: 215px;" name="note">${student.note}</textarea>
								</div>
							</div>
							<div class="row" style="border-top: 2px solid #c0c0c0; height: 31px;">
								<div class="cell form_label" style="background-color: rgb(238,238,238)"><p style="font-size: 12px; text-align: center; margin-top: 5px; margin-bottom: 5px; font-weight: bolder;">쿠폰</p></div>
								<div class="cell form_content"> 
									<input class="align_vertical_center register_input" type="text" style="margin-right: 8px; float: left;" name="coupon" value="${student.coupon }" />
								</div>
							</div>
							
						</div>
						<div style="height: 25px;"></div>
						<c:if test="${lc.adminLogin }">
							<button id="adminSubmitButton" style="width: 128px; height: 37px; background-color: rgb(185,17,32) ; margin: 0 auto; display: block; border: none; font-weight: bold ; color: white; font-size: 20px;">정보수정</button>
						</c:if>
						<div style="height: 30px;"></div>
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