<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>카카오영어 -  수강안내</title>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<style type="text/css">
.section > img  { clear:both; text-align: center; }
.section > table {margin-left:auto;  margin-right:auto;}
#tbl1 th { background-color: #4A4C63; text-align: center; height:50px; vertical-align: middle; border-right:1px solid white;}
#tbl1 th:last-child { border-right:1px solid #4A4C63; }
#tbl1 td { background-color: #FFF; text-align: center; height:50px; vertical-align: middle;  border:1px solid #4A4C63;}
#tbl2 th { background-color: #4A4C63; text-align: center; height:70px; vertical-align: middle; border-right:1px solid white;}
#tbl2 th:last-child { border-right:1px solid #4A4C63; }
#tbl2 td { background-color: #FFF; text-align: center; height:50px; vertical-align: middle; border:1px solid #4A4C63;}
</style>
<script type="text/javascript">
$(function() { 
	$("#tbl_h1 img.button" ).bind({
	  click: function() {
		var me = $(this);   
		$("#tbl_h1 img.button").each(function( idx, ele ) {
			var o = $(ele);
			if(o.attr("on") == "on"){
				o.attr("on", "off");
				o.attr("src", o.attr("src").replace("_on", "_off"));
			}
		}); 
		me.attr("on", "on");
		me.attr("src", me.attr("src").replace("_off", "_on"));
	  },
	  mouseenter: function() {
		var me = $(this); 
		var on = me.attr("on");
		if( on != "on") me.attr("src", me.attr("src").replace("_off", "_on"));
	  },
	  mouseout: function() {
		var me = $(this); 
		var on = me.attr("on");
		if( on != "on") me.attr("src", me.attr("src").replace("_on", "_off")); 
	  }
	}); 
	$("#tbl_h2 img.button" ).bind({
	  click: function() {
		var me = $(this);   
		$("#tbl_h2 img.button").each(function( idx, ele ) {
			var o = $(ele);
			if(o.attr("on") == "on"){
				o.attr("on", "off");
				o.attr("src", o.attr("src").replace("_on", "_off"));
			}
		}); 
		me.attr("on", "on");
		me.attr("src", me.attr("src").replace("_off", "_on"));
	  },
	  mouseenter: function() {
		var me = $(this); 
		var on = me.attr("on");
		if( on != "on") me.attr("src", me.attr("src").replace("_off", "_on"));
	  },
	  mouseout: function() {
		var me = $(this); 
		var on = me.attr("on");
		if( on != "on") me.attr("src", me.attr("src").replace("_on", "_off")); 
	  }
	}); 
});
</script>
</head>
<body>
 <jsp:include page="../include/header.jsp" flush="false"></jsp:include>
	<div id="sec1" class="section">
		<img src="images/m4_img_01.jpg" width="1000" height="180" alt="생각대로 예약"> 
		<img src="images/m4_img_02.jpg"	width="790" height="800" alt="">
		<img src="images/m4_img_03.jpg" width="1000" height="930" alt="">
		<img src="images/m4_img_04.jpg" width="230" height="260" alt="스마트예약방법">
		<img src="images/m4_img_05.jpg" width="1002" height="275" alt="스마트예약방법 STEP 1">
		<img src="images/m4_img_06.jpg" width="1002" height="554" alt="스마트예약방법 STEP 2">
		<img src="images/m4_img_07.jpg" width="1000" height="794" alt="스마트예약방법 STEP 3">
		<img src="images/m4_img_08.jpg"	width="230" height="258" alt="선생님예약방법 STEP 1">
		<img src="images/m4_img_09.jpg" width="1000" height="249" alt="선생님예약방법 STEP 2">
		<img src="images/m4_img_10.jpg" width="1000" height="197" alt="선생님예약방법 STEP 3">
		<table border="0" cellspacing="0" cellpadding="0" >
			<tr>
				<td><img src="images/m4_img_10_01.jpg" width="375" height="203" alt="Betty Cacnio"></td>
				<td><img src="images/m4_img_10_02.jpg" width="375" height="203" alt="Cherry Ramos"></td>
			</tr>
			<tr>
				<td><img src="images/m4_img_10_03.jpg" width="375" height="203" alt="Belle Fernandez"></td>
				<td><img src="images/m4_img_10_04.jpg" width="375" height="203" alt="Mike Casino"></td>
			</tr>
			<tr>
				<td><img src="images/m4_img_10_05.jpg" width="375" height="203" alt="Meggie Gonzales"></td>
				<td><img src="images/m4_img_10_06.jpg" width="375" height="203" alt="Maria Richelle"></td>
			</tr>
		</table>
		<br/>
		<img src="images/m4_img_11.jpg" width="1002" height="181" alt="선생님예약방법 STEP 3">
		<br/><br/>
	</div>
	<div id="sec2" class="section">
		<img src="images/m4_img_12.jpg" width="1000" height="210"alt="수강료안내">
		<br/><br/><br/>
		<table width="1000" border="0" cellpadding="0" cellspacing="0" height="50" id="tbl_h1">
			<tr>
				<td width="270" align="left"><img src="images/m4_img_13.jpg" width="265" height="47" alt="고정시간예약수강료"></td>
				<td><a href="../m4/studentTuition.view?month=1&v=k#sec2"><img src="images/m4_btn_1m_off.png" width="84" height="28" alt="1개월" class="button"></a>
				<a href="../m4/studentTuition.view?month=3&v=k#sec2"><img src="images/m4_btn_3m_off.png"	width="84" height="28" alt="3개월" class="button"></a>
				<a href="../m4/studentTuition.view?month=6&v=k#sec2"><img src="images/m4_btn_6m_off.png" width="84" height="28" alt="6개월" class="button"></a>
				<a href="../m4/studentTuition.view?month=12&v=k#sec2"><img src="images/m4_btn_12m_off.png" width="84" height="28" alt="12개월" class="button"></a></td>
				<td align="right"><img src="images/m4_img_14.jpg" width="241" height="47" alt="낮시간할인은 1117까지 적용"></td>
			</tr>
		</table>

		<table width="1000" border="0" cellpadding="0" cellspacing="0" id="tbl1">
			<tr>
				<th width="13%"><img src="images/m4_tbl1_s_01.jpg" width="79" height="27" alt="수강회수"></th>
				<th width="13%"><img src="images/m4_tbl1_s_02.jpg" width="76" height="27" alt="수강시간"></th>
				<th width="14%"><img src="images/m4_tbl1_s_03.jpg" width="63" height="27" alt="수강료"></th>
				<th><img src="images/m4_tbl1_s_04.jpg" width="78" height="27" alt="낮시간"></th>
				<th><img src="images/m4_tbl1_s_05.jpg" width="86" height="27" alt="기간할인"></th>
				<th width="14%"><img src="images/m4_tbl1_s_06.jpg" width="98" height="27" alt="최종수강료"></th>
				<th width="14%">&nbsp;</th>
			</tr>
			<tr>
				<td rowspan="2"><img src="images/m4_tbl1_th1_01.jpg" width="79" height="64" alt="주2회(화/목)"></td>
				<td><img src="images/m4_tbl1_th2_01.jpg" width="76" height="29" alt="25분"></td>
				<td>${FixPrice1 }</td>
				<td rowspan="6">${DayDiscount }</td>
				<td rowspan="6">${MonthDiscount }</td>
				<td>${FixFinalPrice1 }</td>
				<td><a href="../m3/match.view?sort=FixMatch&course=TypeEasy&title=&period=2&duration=1&month=${param.month }"><img src="images/m4_tbl1_btn1.jpg" width="121" height="36" alt="수강신청"></a></td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl1_th2_02.jpg" width="76" height="29" alt="50분"></td>
				<td>${FixPrice2 }</td>
				<td>${FixFinalPrice2 }</td>
				<td><a href="../m3/match.view?sort=FixMatch&course=TypeEasy&title=&month=${param.month }&period=2&duration=2"><img src="images/m4_tbl1_btn1.jpg" width="121" height="36" alt="수강신청"></a></td>
			</tr>
			<tr>
				<td rowspan="2"><img src="images/m4_tbl1_th1_02.jpg" width="79" height="70" alt="주3회 (월/수/금)"></td>
				<td><img src="images/m4_tbl1_th2_01.jpg" width="76" height="29" alt="25분"></td>
				<td>${FixPrice3 }</td>
				<td>${FixFinalPrice3 }</td>
				<td><a href="../m3/match.view?sort=FixMatch&course=TypeEasy&title=&month=${param.month }&period=3&duration=1"><img src="images/m4_tbl1_btn1.jpg" width="121" height="36" alt="수강신청"></a></td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl1_th2_02.jpg" width="76" height="29" alt="50분"></td>
				<td>${FixPrice4 }</td>
				<td>${FixFinalPrice4 }</td>
				<td><a href="../m3/match.view?sort=FixMatch&course=TypeEasy&title=&month=${param.month }&period=3&duration=2"><img src="images/m4_tbl1_btn1.jpg" width="121" height="36" alt="수강신청"></a></td>
			</tr>
			<tr>
				<td rowspan="2"><img src="images/m4_tbl1_th1_03.jpg" width="79" height="70" alt="주5회(월~금)"></td>
				<td><img src="images/m4_tbl1_th2_01.jpg" width="76" height="29" alt="25분"></td>
				<td>${FixPrice5 }</td>
				<td>${FixFinalPrice5 }</td>
				<td><a href="../m3/match.view?sort=FixMatch&course=TypeEasy&title=&month=${param.month }&period=5&duration=1"><img src="images/m4_tbl1_btn1.jpg" width="121" height="36" alt="수강신청"></a></td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl1_th2_02.jpg" width="76" height="29" alt="50분"></td>
				<td>${FixPrice6 }</td>
				<td>${FixFinalPrice6 }</td>
				<td><a href="../m3/match.view?sort=FixMatch&course=TypeEasy&title=&month=${param.month }&period=5&duration=2"><img src="images/m4_tbl1_btn1.jpg" width="121" height="36" alt="수강신청"></a></td>
			</tr>
		</table>
		<br/><br/>
		<img src="images/m4_img_12_01.jpg" width="1000" height="230"alt="수강료안내">
		<br/><br/><br/>
		<table width="1000" border="0" cellpadding="0" cellspacing="0" height="50" id="tbl_h2">
			<tr>
				<td width="410" align="left"><img src="images/m4_img_15.jpg" width="409" height="44" alt="스마트예약 및 선생님예약수강료"></td>
				<td><a href="../m4/studentTuition.view?month=1&v=k#sec2"><img src="images/m4_btn_1m_off.png" width="84" height="28" alt="1개월" class="button"></a>
				<a href="../m4/studentTuition.view?month=3&v=k#sec2"><img src="images/m4_btn_3m_off.png"	width="84" height="28" alt="3개월" class="button"></a>
				<a href="../m4/studentTuition.view?month=6&v=k#sec2"><img src="images/m4_btn_6m_off.png" width="84" height="28" alt="6개월" class="button"></a>
				<a href="../m4/studentTuition.view?month=12&v=k#sec2"><img src="images/m4_btn_12m_off.png" width="84" height="28" alt="12개월" class="button"></a></td>
				<td align="right"><img src="images/m4_img_16.jpg" width="207" height="44" alt="25분단위로 예약가능"></td>
			</tr>
		</table>
		<table width="1000" border="0" cellpadding="0" cellspacing="0" id="tbl2">
			<tr>
				<th width="13%"><img src="images/m4_tbl2_s_01.jpg" width="80" height="59" alt="주수강시간"></th>
				<th width="13%"><img src="images/m4_tbl2_s_02.jpg" width="91" height="59" alt="월수강시간"></th>
				<th width="14%"><img src="images/m4_tbl1_s_03.jpg" width="63" height="27" alt="수강료"></th>
				<th><img src="images/m4_tbl1_s_04.jpg" width="78" height="27" alt="낮시간"></th>
				<th><img src="images/m4_tbl1_s_05.jpg" width="86" height="27" alt="기간할인"></th>
				<th width="14%"><img src="images/m4_tbl1_s_06.jpg" width="98" height="27" alt="최종수강료"></th>
				<th width="14%"><img src="images/m4_tbl2_s_07.jpg" width="80" height="32" alt="바로가기"></th>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_01.jpg" width="70" height="25" alt="50분"></td>
				<td><img src="images/m4_tbl2_th2_01.jpg" width="80" height="25" alt="200분"></td>
				<td>${Smart1 }</td>
				<td rowspan="9">${DayDiscount }</td>
				<td rowspan="9">${MonthDiscount }</td>
				<td>${SmartFinal1 }</td>
				<td rowspan="9">
					<div class="btn">
					<a href="../m3/match.view?sort=SmartMatch&course=TypeEasy&title=&month=${param.month }"><img src="images/m4_tbl2_btn1.jpg" width="121" height="36" alt="스마트에약"></a>
					</div>
					<br/>
					<div class="btn">
					<a href="../m3/match.view?sort=TeacherMatch&course=TypeEasy&title=&month=${param.month }"><img src="images/m4_tbl2_btn2.jpg" width="121" height="36" alt="선생님예약"></a>
					</div>
					</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_02.jpg" width="70" height="25" alt="75분"></td>
				<td><img src="images/m4_tbl2_th2_02.jpg" width="80" height="25" alt="300분"></td>
				<td>${Smart2 }</td>
				<td>${SmartFinal2 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_03.jpg" width="70" height="25" alt="100분"></td>
				<td><img src="images/m4_tbl2_th2_03.jpg" width="80" height="25" alt="400분"></td>
				<td>${Smart3 }</td>
				<td>${SmartFinal3 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_04.jpg" width="70" height="25" alt="125분"></td>
				<td><img src="images/m4_tbl2_th2_04.jpg" width="80" height="25" alt="500분"></td>
				<td>${Smart4 }</td>
				<td>${SmartFinal4 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_05.jpg" width="70" height="25" alt="150분"></td>
				<td><img src="images/m4_tbl2_th2_05.jpg" width="80" height="25" alt="600분"></td>
				<td>${Smart5 }</td>
				<td>${SmartFinal5 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_06.jpg" width="70" height="25" alt="175분"></td>
				<td><img src="images/m4_tbl2_th2_06.jpg" width="80" height="25" alt="700분"></td>
				<td>${Smart6 }</td>
				<td>${SmartFinal6 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_07.jpg" width="70" height="25" alt="200분"></td>
				<td><img src="images/m4_tbl2_th2_07.jpg" width="80" height="25" alt="800분"></td>
				<td>${Smart7 }</td>
				<td>${SmartFinal7 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_08.jpg" width="70" height="25" alt="225분"></td>
				<td><img src="images/m4_tbl2_th2_08.jpg" width="80" height="25" alt="900분"></td>
				<td>${Smart8 }</td>
				<td>${SmartFinal8 }</td>
			</tr>
			<tr>
				<td><img src="images/m4_tbl2_th1_09.jpg" width="70" height="25" alt="250분"></td>
				<td><img src="images/m4_tbl2_th2_09.jpg" width="80" height="25" alt="1000분"></td>
				<td>${Smart9 }</td>
				<td>${SmartFinal9 }</td>
			</tr>
		</table>
		<br/><br/><br/><br/>
		<img src="images/m4_img_17.jpg" width="1004" height="236" alt="카카오영어.com의 약속">
		<br/><br/><br/><br/>
	</div> 
  <jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
</body>
</html>
