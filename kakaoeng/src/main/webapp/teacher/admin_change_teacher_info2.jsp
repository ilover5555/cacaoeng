<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="contentType" content="text/html;charset=UTF-8">
<style>
</style>
<title>Blue Wave Template</title>
</head>
<body>

            	<form role="form" action="./adminChangeTeacherAdminInfo.do?teacherId=${param.teacherId }" method="post">
							<select name="rate">
								<option >A</option>
								<option >B</option>
								<option >C</option>
								<option >Wait</option>
							</select>
							<select name="representitive">
								<option  value="true">Representitive</option>
								<option  value="false">Normal</option>
							</select>
							<input type="text" name="salary" value="${teacher.salary }"
								placeholder="Salary"/>
							<select name="pronunciation">
								<option >Normal</option>
								<option >Better</option>
								<option >Best</option>
							</select>
							<select name="accent">
								<option >Normal</option>
								<option >Better</option>
								<option >Best</option>
							</select>
					
					<input rows="5" name="comment"></input>
					<button type="submit">Save And Exit</button>
				</form>
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>