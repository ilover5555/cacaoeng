<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
<meta charset="utf-8"/>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="../css/audioplayer.css" />
<script src="../js/audioplayer.js"></script>
<script>
	$(document).ready(function(){
		$( 'audio' ).audioPlayer();
	})
</script>
<style>
</style>
<title>Blue Wave Template</title>
</head>
<body>

<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <jsp:useBean id="messages" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<String>"></jsp:useBean>
    <jsp:useBean id="successes" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<String>"></jsp:useBean>
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        	<c:forEach var="message" items="${messages }">
				<div class="alert alert-danger"><strong>Error!</strong> ${message }</div>
			</c:forEach>

			<c:forEach var="message" items="${successes }">
				<div class="alert alert-success"><strong>Success!</strong> ${message }</div>
			</c:forEach>
        	
        	<div class="col_w880">
            	<form role="form" enctype="multipart/form-data" action="./adminChangeTeacherInfo.do?teacherId=${param.teacherId }" method="post">
					<jsp:include page="TeacherInformationForm.jsp" flush="false"></jsp:include>
					<button type="submit" class="btn btn-primary btn-block">Save And Exit</button>
				</form>
                <div class="cleaner"></div>
            </div>
            
            <div class="col_w880">
            	<form role="form" action="./adminChangeTeacherAdminInfo.do?teacherId=${param.teacherId }" method="post" accept-charset="utf-8">
					<div style="width: 100%" class="form-group">
						<label for="id" class="control-label">
							<h3>Admin</h3>
						</label>
						<div style="width: 100%" class="input-group">
							<div style="width: 150px" class="input-group-addon">Rate</div>
							<select class="form-control" name="rate">
								<option <c:if test="${ 'A' eq teacher.rate}">selected="selected"</c:if>>A</option>
								<option <c:if test="${ 'B' eq teacher.rate}">selected="selected"</c:if>>B</option>
								<option <c:if test="${ 'C' eq teacher.rate}">selected="selected"</c:if>>C</option>
								<option <c:if test="${ 'Wait' eq teacher.rate}">selected="selected"</c:if>>Wait</option>
							</select>
						</div>
						<div style="width: 100%" class="input-group">
							<div style="width: 150px" class="input-group-addon">Representitive</div>
							<select class="form-control" name="representitive">
								<option <c:if test="${ true eq teacher.representitive}">selected="selected"</c:if> value="true">Representitive</option>
								<option <c:if test="${ false eq teacher.representitive}">selected="selected"</c:if> value="false">Normal</option>
							</select>
						</div>
						<div style="width: 100%" class="input-group">
							<span style="width: 150px" class="input-group-addon">Salary</span>
							<input type="text" class="form-control" name="salary" value="${teacher.salary }"
								placeholder="Salary"/>
						</div>
						<div style="width: 100%" class="input-group">
							<div style="width: 150px" class="input-group-addon">Pronunciation</div>
							<select class="form-control" name="pronunciation">
								<option <c:if test="${ 'Normal' eq teacher.pronunciation}">selected="selected"</c:if>>Normal</option>
								<option <c:if test="${ 'Better' eq teacher.pronunciation}">selected="selected"</c:if>>Better</option>
								<option <c:if test="${ 'Best' eq teacher.pronunciation}">selected="selected"</c:if>>Best</option>
							</select>
						</div>
						<div style="width: 100%" class="input-group">
							<div style="width: 150px" class="input-group-addon">Accent</div>
							<select class="form-control" name="accent">
								<option <c:if test="${ 'Normal' eq teacher.accent}">selected="selected"</c:if>>Normal</option>
								<option <c:if test="${ 'Better' eq teacher.accent}">selected="selected"</c:if>>Better</option>
								<option <c:if test="${ 'Best' eq teacher.accent}">selected="selected"</c:if>>Best</option>
							</select>
						</div>
						<div style="width: 100%" class="input-group">
							<span style="width: 150px" class="input-group-addon">Comment</span>
							<textarea rows="5" class="form-control" name="comment">${teacher.comment }</textarea>
						</div>
					</div>
					
					
					<button type="submit" class="btn btn-primary btn-block">Save And Exit</button>
				</form>
                <div class="cleaner"></div>
            </div>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>
	
	
</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>