<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>카카오영어</title> 
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="../css/main.css" rel="stylesheet" type="text/css"> 
<script type="text/javascript" src="../js/main.js"></script> 
<link href="../minimal/grey.css" rel="stylesheet">
<script src="../icheck.js"></script>
<link rel="stylesheet" type="text/css" href="../css/match.css" />
<link rel="stylesheet" type="text/css" href="../css/popup.css" />
<script src="../js/scheduleCell.js"></script>
<script>

$(document).ready(function(){
	$('#loginButton').click(function(e){
		s = {};
		s["id"] = $('[name="loginId"]').val();
		s["pw"] = $('[name="loginpwd"]').val();
		$.ajax({
			type:'POST',
			url:makeUrl('../studentLogin.do'),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
			success: function(data){
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
		e.preventDefault();
	})
	$('#naverLogin').click(function(){
		var win = window.open("../naverLoginRequest.do", "", "width=370, height=360, resizable=no, scrollbars=no, status=no;"); 
		return false;
		win.onbeforeunload = function(){
			alert('abc');
		}
	})
	$('#joinButton').click(function(){
		var scrollY = $(window).scrollTop();
		$.ajax({
			url : makeUrl("../register_student.jsp"),
			method : "GET",
			dataType : "html",
			async : false,
			success:function(msg){
				$('#light').html(msg);
				scheduleCell.showBox(scrollY, common.init, true, true);
			},
			error: function(request,status,error){
				alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	})
	
	$('#myClassRoom').click(function(){
		$.ajax({
			url : makeUrl("./studentEnterBBB.do"),
			method : "GET",
			dataType : "html",
			async : false,
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
				alert(request.responseText);
			}
		});
	});
	
});
</script>
</head> 
<body> 
	<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
   	 <div class="section">     
   	   <div id="banner01">
		   <div class="clsBannerScreen">
				<div class="images" style="display:block"><img src="../main/images/rolling_banner_01.jpg" width="713" height="358" alt="불편한진실"> </div>
				<div class="images"><img src="../main/images/rolling_banner_02.jpg" width="713" height="358" alt="하늘과 땅사이"></div>
				<div class="images"><img src="../main/images/rolling_banner_03.jpg" width="713" height="358" alt="스마트매칭시스템"></div>
				<div class="images"><img src="../main/images/rolling_banner_04.jpg" width="713" height="358" alt="월드클래스교재"></div>
				<div class="images"><img src="../main/images/rolling_banner_05.jpg" width="713" height="358" alt="카카오영어의약속"></div>
		 </div>
         <ul class="clsBannerButton">
			<li><a href="#"><img class="fir" oversrc="../main/images/rolling_menu_on_01.png" outsrc="../main/images/rolling_menu_off_01.png" width="143" height="37" alt="불편한진실"></a></li>
			<li><a href="#"><img oversrc="../main/images/rolling_menu_on_02.png" outsrc="../main/images/rolling_menu_off_02.png" width="143" height="37" alt="하늘과땅차이"></a></li>
			<li><a href="#"><img oversrc="../main/images/rolling_menu_on_03.png" outsrc="../main/images/rolling_menu_off_03.png" width="143" height="37" alt="스마트매칭시스템"></a></li>
			<li><a href="#"><img oversrc="../main/images/rolling_menu_on_04.png" outsrc="../main/images/rolling_menu_off_04.png" width="142" height="37" alt="월드클래스교재"></a></li>
			<li><a href="#"><img oversrc="../main/images/rolling_menu_on_05.png" outsrc="../main/images/rolling_menu_off_05.png" width="142" height="37" alt="카카오영어의 약속"></a></li>
           </ul>
         </div>       
    	<div id="banner01_r"> 
        	<div id="login">
            <c:if test="${(null eq sessionScope.student)}">
	        	<form>
		            <table cellpadding="0" cellspacing="0">
		            
		              <tr>
		                <td colspan="2"><input type="text" id="loginId" name="loginId"></td>
		              </tr>
		              <tr>
		                <td colspan="2"><input type="password" id="loginpwd" name="loginpwd">
		                </td>
		              </tr>
		              <tr>
		                <td height="24" colspan="2">
			                <a href="#" id="joinButton">
			                	<img src="../images/main/btn_join.gif" width="48" height="15" alt=""><img src="../images/main/btn_pwd_search.gif" width="92" height="15" alt="">
			                </a>
		                </td>
		              </tr>
		              <tr>
		                <td height="5" colspan="2"></td>
		              </tr>
		              <tr>
		                <td colspan="2"><button type="submit" id="loginButton"><img src="../images/main/btn_login.gif" width="114" height="31" alt="로그인"></button>				
		                <a href="#" id="naverLogin"><img src="../images/main/btn_login_naver.gif" width="114" height="31" alt="네이버로그인"></a></td>
		              </tr>
		              <tr>
		                <td height="18" colspan="2"></td>
		              </tr>
		              <tr>
		                <td colspan="2"><a href="#"><img src="../images/main/btn_my_course.jpg" width="236" height="32" alt="나의강의실"></a></td>
		              </tr>
		            </table>
	            </form>
	        </c:if>
	        <c:if test="${(null ne sessionScope.student)}">
	        	<div class="align_vertical_center" style="font-size: 17px; text-align: left; height: 168px;">
	        		<c:if test="${sessionScope.student.name ne '' }">
	        			<p>환영합니다. ${sessionScope.student.name } 회원님 </p>
	        		</c:if>
	        		<c:if test="${sessionScope.student.name eq '' }">
	        			<p>환영합니다. ${sessionScope.student.className } 회원 님 </p>
	        		</c:if>
	        		<p>무료수강권 : ${sessionScope.student.coupon }장 <a href="#" style="text-decoration: underline;">사용방법</a></p>
	        		<p>마지막 방문일 : ${sessionScope.student.lastLogin }</p>
	        		<div style="height: 21px;"></div>
	        		<form action="../studentLogout.do" method="get">
						<button type="submit" style="width:236px; background-image : url('../artifact/logout_background.png'); color: white; font-size: 18px; font-weight: bold; cursor: pointer; height:31px; margin-bottom: 4px;">로그아웃</button>
					</form>
					<div style="height:5px;"></div>
					<a id="myClassRoom" href="#"><img src="../images/main/btn_my_course.jpg" width="236" height="32" alt="나의강의실"></a>
				</div>
	        </c:if>
            
            </div>
            <div> <a href="#"><img src="../main/images/banner_course_regist.png" width="268" height="183" alt="수강신청"></a>
            </div>
        </div> 
        </div>
   	  <div class="section" id="line02">
       	<img src="../main/images/section_line_01.gif" width="1000" height="4" alt=""></td>
      </div>            
       <div class="section">  
         <ul id="banner02"> 
             <li> <a href="#"><img src="../main/images/banner_02_1.gif" width="354" height="166" alt="화상영어 1타 선생님들"></a></li>
            <li> <a href="#"><img src="../main/images/banner_02_2.gif" width="354" height="166" alt="수강시간 선택의 자유"></a></li>
            <li> <a href="#"><img src="../main/images/banner_02_3.gif" width="354" height="166" alt="완벽히 검증된 교재사용"></a></li>
            <li> <a href="#"><img src="../main/images/banner_02_4.gif" width="354" height="166" alt="선생님 정보 100%공개"></a></li>
           </ul> 
         
         <div id="banner02_r"> 
       	   <div id="notice">
       	      <table width="100%" id="notice_t">
              <tr>
              <td><img src="../main/images/notice_title.gif" width="73" height="21" alt="공지사항"></td>
              <td align="right"><a href="#"><img src="../main/images/notice_more.gif" width="52" height="16" alt="공지사항 더보기"></a></td>
              </tr>              
              </table> 
            <div id="notice_l">
            <table width="250">
            <tr>
                <td class="subject"><a href="#">카카오영어 공지사항입니다. 화이팅입니다.</a> </td> 
                <td class="date">2016-01-06</td>
            </tr> 
             <tr>
                <td class="subject"><a href="#">카카오영어 공지사항입니다. 화이팅입니다.</a> </td> 
                <td class="date">2016-01-06</td>
            </tr> 
             <tr>
                <td class="subject"><a href="#">카카오영어 공지사항입니다. 화이팅입니다.</a> </td> 
                <td class="date">2016-01-06</td>
            </tr> 
             <tr>
                <td class="subject"><a href="#">카카오영어 공지사항입니다. 화이팅입니다.</a> </td> 
                <td class="date">2016-01-06</td>
            </tr> 
             <tr>
                <td class="subject"><a href="#">카카오영어 공지사항입니다. 화이팅입니다.</a> </td> 
                <td class="date">2016-01-06</td>
            </tr> 
            </table> 
            </div> 
           </div>
           <div id="customer-center">
         	<img src="../main/images/banner_customer_center.gif" width="268" height="166" alt="고객센터">         
         </div>
         </div>          
      </div>         
        <div class="section">  
          <div id="banner03">  
             <ul >
                <li><a href="#"><img src="../main/images/banner_03_1.png" width="250" height="102" alt=""></a></li>
                <li><a href="#"><img src="../main/images/banner_03_2.png" width="251" height="102" alt=""></a></li>
                <li><a href="#"><img src="../main/images/banner_03_3.png" width="250" height="102" alt=""></a></li>
                <li><a href="#"><img src="../main/images/banner_03_4.png" width="249" height="102" alt=""></a></li>
            </ul>
          </div>
        </div> 
	<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
</body>
</html>