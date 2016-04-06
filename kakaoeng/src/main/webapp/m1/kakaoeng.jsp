<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>카카오영어는?</title>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script type="text/javascript" src="../js/simplePlayer.js"></script>
<style type="text/css">
.section > img  { clear:both; }
</style> 
<script type="text/javascript">
$(function() {
	//https://www.youtube.com/watch?v=hIX4r_3U1H4
	$("#video1").simplePlayer({video : "hIX4r_3U1H4", width:"640px"}); 
});
</script>
</head>
<body>
 <jsp:include page="../include/header.jsp" flush="false"></jsp:include>
	<div id="sec1" class="section">
		<img src="./images/m1_img_01.jpg" width="1000" height="180" alt="카카오영어는?">
		<img src="./images/m1_img_02.png" width="1000" height="618" alt="영어회화를 잘하는 방법?">
		<img src="./images/m1_img_03.png" width="1000" height="735" alt="">
		<img src="./images/m1_img_04.png" width="1000" height="116" alt="영어를 말한다고 모두 선생님일까요?">
		<div id="video1">
			<img src="./images/m1_img_04_movie.jpg" width="640" height="360" alt="당신이 영어를 못하는 진짜이유 - KBS스페셜">
		</div>
		<br/><br/>
        <img src="./images/m1_img_05.png" width="1000" height="1070" alt="">
		 <img src="./images/m1_img_06.png" width="1000" height="473" alt="새로운 기준을 제시합니다.">
	</div>  
	<div id="sec2" class="section">
	    <img src="./images/m1_img_07.png" width="1000" height="981" alt="선생님[1타 강사진]"></p>
		<img src="./images/m1_img_08.png" width="1000" height="253" alt="">
		<img src="./images/m1_img_09.png" alt="" width="1000" height="338" usemap="#map_img_09" border="0">
        <map name="map_img_09">
          <area shape="rect" coords="300,222,530,270" href="http://www.4icu.org/ph/" target="_blank" alt="필리핀 대학랭킹 보기">
          <area shape="rect" coords="539,221,771,270" href="http://www.finduniversity.ph/philippine-universities-ranking/" target="_blank" alt="자격시험 통과기준 보기">
        </map>
		<img src="./images/m1_img_11.png" width="1000" height="271" alt="">
		<img src="./images/m1_img_12.png" width="1000" height="231" alt="">
	</div>  
	<div id="sec3" class="section">
		<img src="./images/m1_img_13.png" width="1000" height="852" alt="스마트예약시스템">
		<img src="./images/m1_img_14.png" width="1000" height="729" alt="">
	</div>  
	<div id="sec4" class="section"> 
		<img src="./images/m1_img_15.png" width="1000" height="1242" alt="교재">
	</div> 
	<br/><br/><br/>
  <jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
</body>
</html>
