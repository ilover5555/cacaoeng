<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
</head>
<body>

	<c:if test="${(null ne sessionScope.teacher) }">
		<c:redirect url="./index.jsp"></c:redirect>
	</c:if>
		

	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        
        	<jsp:useBean id="messages" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<String>"></jsp:useBean>
	
			<c:forEach var="message" items="${messages }">
				<div class="alert alert-danger"><strong>Error!</strong> ${message }</div>
			</c:forEach>
			
			<div style="width:600px; height:auto; margin: 0 auto;">
				<form role="form" action="./login.do" method="post">
					<div style="width:100%; overflow: hidden; margin: 0 auto;" class="form-group">
						<div style="float: left; width:85%">
							<div style="width:100%; height:auto;" class="input-group">
								<div style="width:150px"class="input-group-addon">ID</div>
								<input  style="width:384px;"type="text" class="form-control" name="id" placeholder="ID(must be email)"/>
							</div>
							<div style="width:100%; height:auto;" class="input-group">
								<span style="width:150px"class="input-group-addon">Password</span>
								<input style="width:384px;"type="password" class="form-control" name="pw" placeholder="Password"/>
							</div>
						</div>
						<button type="submit" class="btn btn-primary" style="float:left; height:68px; width:14%">Log In</button>
					</div>
				</form>
				
				<div style="height:15px;"></div>
				
				<button class="btn btn-primary btn-block" onclick='window.location = "./registerTeacher.view"'>Register</button>
			</div>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>