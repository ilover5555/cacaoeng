<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./mailSend.do" method="POST">
		<div>
			<label>받는사람</label>
			<input name="to"/>
		</div>
		<div>
			<label>제목</label>
			<input name="subject"/>
		</div>
		<div>
			<label>내용</label>
			<textarea rows="10" cols="100" name="text"></textarea>
		</div>
		<button type="submit">전송</button>
	</form>
</body>
</html>