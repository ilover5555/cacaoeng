<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>카카오영어 - 학습방법</title>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="../css/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../js/simplePlayer.js"></script>
<style type="text/css">
.section>img {
	clear: both;
}
</style>
<script type="text/javascript">
	$(function() {
		$("#video1").simplePlayer({
			video : "7AuhjzxAudY",
			width : "800px"
		});
		$("#video2").simplePlayer({
			video : "oB4MPIfhsRs",
			width : "550px"
		});
	});
</script>
</head>
<body>
	<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
	<div id="sec1" class="section">
		<img src="images/m2_img_01.jpg" width="1000" height="180"
			alt="화상영어는 왜 필요한걸까요?">
		<div style="height: 60px;"></div>
		<img src="images/m2_img_02.jpg" alt="한국인의 영어 말하기 순위 121위" width="800"
			height="727"> <img src="images/m2_img_03.jpg"
			alt="이학생은 수영을 잘하게 될까요?" width="1000" height="748"> <img
			src="images/m2_img_04.jpg" width="800" height="439"
			alt="언어를 배우는것도 마찬가지라고 합니다"> <img src="images/m2_img_05.jpg"
			width="1000" height="1203" alt="화상영어는 왜 필요한가요?">
	</div>
	<div id="sec2" class="section">
		<img src="images/m2_img_06.jpg" width="1000" height="1124"
			alt="1:1온라인화상영어">
	</div>
	<div id="sec3" class="section">
		<img src="images/m2_img_07.jpg" width="1000" height="493"
			alt="빅블루버튼 소개"> <img src="images/m2_img_08.jpg" width="1000"
			alt="빅블루버튼 특장점"> <img src="images/m2_img_09.jpg" width="1000"
			height="810" alt="빅블루버튼 온라인 교육활용예제">
	</div>
	<div id="sec4" class="section">
		<br />
		<br /> <img src="images/m2_img_11.jpg" width="1000" height="660"
			alt="skype소개"> <br />
		<br />
		<div id="video1" title="https://www.youtube.com/watch?v=7AuhjzxAudY">
			<img src="images/m2_img_12.jpg" width="800" alt="스카이프소개 동영상">
		</div>
		<br /> <br /> <img src="images/m2_img_13.jpg" width="1000"
			height="514" alt="스카이프로 할수 있는일">
	</div>
	<div id="sec5" class="section">
		<table width="1000" border="0" cellspacing="0" cellpadding="0">
			<tr valign="top">
				<td rowspan="3"><img src="images/m2_img_14.jpg" width="400"
					height="527" alt=""></td>
				<td colspan="2"><img src="images/m2_img_15.jpg" width="600"
					height="135" alt="스카이프모바일"></td>
			</tr>
			<tr>
				<td width="555" align="center"><div id="video2"
						title="https://www.youtube.com/watch?v=oB4MPIfhsRs">
						<img src="images/m2_img_16.jpg" width="550" height="315" alt="">
					</div></td>
				<td width="45">&nbsp;</td>
			</tr>
			<tr>
				<td align="right"><a
					href="http://skype.daesung.com/download/downloadMain.asp"
					target="_blank"><img src="images/m2_img_16_btn.jpg" width="247"
						height="50" alt="스카이프설치"></a></td>
				<td align="right">&nbsp;</td>
			</tr>
		</table>
		<br />
		<br />
		<br />
		<br />
	</div>
	<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
</body>
</html>
