<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="ESLList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="FreeTalk" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="TextBook" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="Business" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="Exam" scope="request" class="java.util.ArrayList"></jsp:useBean>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>카카오영어-교과과정</title>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="../css/common.css" rel="stylesheet" type="text/css">
<style type="text/css">
#schedule {
	margin-top: 16px;
}

#books {
	text-align: center;
	margin-top: 100px;
}

.book {
	margin-bottom: 35px;
	position: relative;
}

#books .title {
	margin-bottom: 40px;
	margin-top: 40px;
}

hr.titleline {
	display: block;
	height: 26px;
	/*margin: 32px 0 0 0;*/
	border: 0;
	background-image: url(images/book_title_bar.png);
	background-repeat: repeat-x
}

.section {
	text-align: center;
}
.enroll{
	width: 110px;
	height: 38px;
	position: absolute;
	right: 272px;
	bottom: 39px;
	background-color: transparent;
	cursor: pointer
}
.link{
	width:110px;
	height: 38px;
	position : absolute;
	bottom: 39px;
	right: 159px;
}
</style>
<script type="text/javascript">
$(function() {
        $("map area").click(function() {
			var target = this.href;
			var id = target.substring(target.indexOf("#"));
            var position = $(id).offset();
            $("html, body").animate({ scrollTop: position.top - 20 }, "slow");
        });
});
</script>
</head>
<body>
	<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
	<div class="section">
		<img src="images/m3_img_01.jpg" width="1000" height="180" alt="">
	</div>
	<div id="schedule" class="section">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="2"><img src="images/tbl_header.png" width="1001"
					height="37" alt="" /></td>
			</tr>
			<tr>
				<td><img src="images/tbl_sec_01.png" width="152" height="220"
					alt="ESL Course Books" /></td>
				<td><a href="#book01"><img src="images/tbl_list_01.png"
						alt="" width="849" height="220" usemap="#map_01" border="0" /></a></td>
			</tr>
			<tr>
				<td><img src="images/tbl_sec_02.png" width="152" height="131"
					alt="Free Talking" /></td>
				<td><a href="#book02"><img src="images/tbl_list_02.png"
						alt="" width="849" height="131" usemap="#map_02" border="0" /></a></td>
			</tr>
			<tr>
				<td><img src="images/tbl_sec_03.png" width="152" height="65"
					alt="미국초등교과서" /></td>
				<td><a href="#book03"><img src="images/tbl_list_03.png"
						alt="" width="849" height="65" usemap="#map_03" border="0" /></a></td>
			</tr>
			<tr>
				<td><img src="images/tbl_sec_04.png" width="152" height="107"
					alt="Business" /></td>
				<td><a href="#book04"><img src="images/tbl_list_04.png"
						alt="" width="849" height="107" usemap="#map_04" border="0" /></a></td>
			</tr>
			<tr>
				<td><img src="images/tbl_sec_05.png" width="152" height="132"
					alt="시험대비" /></td>
				<td><a href="#book05"><img src="images/tbl_list_05.png"
						alt="" width="849" height="132" usemap="#map_05" border="0" /></a></td>
			</tr>
		</table>
	</div>
	<div id="books" class="section">

		<div id="book01">
			<div class="title">
				<img src="images/books_title_01.png" alt="ESL Course books">
				<hr class="titleline" />
			</div>

			<c:forEach items="${ESLList }" var="book">
				<div class="book" id="book${book.id }">
					<img src="../${book.bookPicture }" width="1000" height="315" alt="${book.title }">
					<a class="enroll" data-id="${book.id }" href="./match.view?sort=SmartMatch&course=${book.course}&title=${book.title }"></a>
					<a class="link" href="${book.bookLink }" onclick="window.open('${book.bookLink}', 'newwindow', 'width=300, height=250'); return false;"></a>
				</div>
			</c:forEach>

		</div>

		<div id="book02">
			<div class="title">
				<img src="images/books_title_02.png" width="1000" height="90"
					alt="Discussion (Free Talking)">
				<hr class="titleline" />
			</div>

			<c:forEach items="${FreeTalk }" var="book">
				<div class="book" id="book${book.id }">
					<img src="../${book.bookPicture }" width="1000" height="315" alt="${book.title }">
					<a class="enroll" data-id="${book.id }" href="./match.view?sort=SmartMatch&course=${book.course}&title=${book.title }"></a>
					<a class="link" href="${book.bookLink }" onclick="window.open('${book.bookLink}', 'newwindow', 'width=300, height=250'); return false;"></a>
				</div>
			</c:forEach>

		</div>
		<div id="book03">
			<div class="title">
				<img src="images/books_title_03.png" width="1000" height="90" alt="">
				<hr class="titleline" />
			</div>
			<c:forEach items="${TextBook }" var="book">
				<div class="book" id="book${book.id }">
					<img src="../${book.bookPicture }" width="1000" height="315" alt="${book.title }">
					<a class="enroll" data-id="${book.id }" href="./match.view?sort=SmartMatch&course=${book.course}&title=${book.title }"></a>
					<a class="link" href="${book.bookLink }" onclick="window.open('${book.bookLink}', 'newwindow', 'width=300, height=250'); return false;"></a>
				</div>
			</c:forEach>

		</div>
		<div id="book04">
			<div class="title">
				<img src="images/books_title_04.png" width="1000" height="90"
					alt="Business">
				<hr class="titleline" />
			</div>
			<c:forEach items="${Business }" var="book">
				<div class="book" id="book${book.id }">
					<img src="../${book.bookPicture }" width="1000" height="315" alt="${book.title }">
					<a class="enroll" data-id="${book.id }" href="./match.view?sort=SmartMatch&course=${book.course}&title=${book.title }"></a>
					<a class="link" href="${book.bookLink }" onclick="window.open('${book.bookLink}', 'newwindow', 'width=300, height=250'); return false;"></a>
				</div>
			</c:forEach>

		</div>
		<div id="book03">
			<div class="title">
				<img src="images/books_title_05.png" width="1000" height="90"
					alt="Examination &amp; Grammar">
				<hr class="titleline" />
			</div>
			<c:forEach items="${Exam }" var="book">
				<div class="book" id="book${book.id }">
					<img src="../${book.bookPicture }" width="1000" height="315" alt="${book.title }">
					<a class="enroll" data-id="${book.id }" href="./match.view?sort=SmartMatch&course=${book.course}&title=${book.title }"></a>
					<a class="link" href="${book.bookLink }" onclick="window.open('${book.bookLink}', 'newwindow', 'width=300, height=250'); return false;"></a>
				</div>
			</c:forEach>

		</div>
	</div>
	<map name="map_01">
		<area shape="rect" coords="1,8,304,30" href="#book01_01"
			alt="Let's Go  (1~6)">
		<area shape="rect" coords="1,37,363,59" href="#book01_03"
			alt="Back Pack (1~6)">
		<area shape="rect" coords="59,67,364,90" href="#book01_03"
			alt="National Geographic Our World (1~6)">
		<area shape="rect" coords="120,98,606,121" href="#book01_04"
			alt="Side by Side (1~4)">
		<area shape="rect" coords="313,129,727,152" href="#book01"
			alt="Exploring english (1~6)">
		<area shape="rect" coords="417,158,848,181" href="#book01_05"
			alt="Interchange (1~3)">
		<area shape="rect" coords="417,187,847,209" href="#book01_06">
	</map>
	<map name="map_02">
		<area shape="rect" coords="242,19,674,44" href="#book02_02"
			alt="Small Group Discussion">
		<area shape="rect" coords="242,51,727,75" href="#book02_01"
			alt="Active english Discussion (1~2)">
		<area shape="rect" coords="242,83,728,106" href="#book02_03"
			alt="Can you Believe it? (1~3)">
	</map>
	<map name="map_03">
		<area shape="rect" coords="120,22,607,46" href="#book03_01"
			alt="American Textbook Wonder Series (1~6)">
	</map>
	<map name="map_04">
		<area shape="rect" coords="242,12,728,36" href="#book04_01"
			alt="Business Venture (1~2)">
		<area shape="rect" coords="286,42,772,65" href="#book04_02"
			alt="English for Persentations">
		<area shape="rect" coords="361,72,849,95" href="#book04_03"
			alt="301 Smart Answers to Interview Question">
	</map>
	<map name="map_05">
		<area shape="rect" coords="119,30,426,53" href="#book05_01"
			alt="Grammar in Use (Basic)">
		<area shape="rect" coords="423,61,728,86" href="#book05_01"
			alt="Grammar In Use (Intermediate)">
		<area shape="rect" coords="484,93,850,117" href="#"
			target="#book05_01" alt="ILETS / TOFEL / TOIEC">
	</map>
	<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
</body>
</html>
