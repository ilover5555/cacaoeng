<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" href="../css/audioplayer.css" />
<script src="../js/audioplayer.js"></script>
<script src="../js/jquery.form.js"></script>
<title>Blue Wave Template</title>
<script>
function showResponse(responseText, statusText, xhr, $form)  {
    alert(responseText);
    window.location = './index.jsp';
}
function errorFunction(e1, e2, e3, e4, e5){
	var data = JSON.parse(e1.responseText);
	var messages = data.msg;
	var panel = $('#messagePanel');
	panel.html('');
	$.each(messages, function(index, value){
		var tag = '<div class="alert alert-danger"><strong>Error!</strong> '+value+'</div>';
		var jTag = $(tag);
		jTag.appendTo(panel);
	})
	$(window).scrollTop($('#messagePanel').offset().top - 10);
	$("#progressbar").css('display', 'none');
	
}

function progress(event, position, total, percent){
	var val = progressbar.progressbar( "value" ) || 0;
	 
    progressbar.progressbar( "value", percent );
}

function preReq(formData, jqForm, options){
	var top = $('#progressbar').offset().top;
	$(window).scrollTop(top);
	$("#progressbar").css('display', 'block');
}

	$(document).ready(function(){
		var progressbar = $( "#progressbar" ),
	      progressLabel = $( ".progress-label" );
		progressbar.progressbar({
		      value: false,
		      change: function() {
		        progressLabel.text( progressbar.progressbar( "value" ) + "%" );
		      },
		      complete: function() {
		        progressLabel.text( "Complete!" );
		      }
		    });
		$( 'audio' ).audioPlayer();
		
		var options = { 
				beforeSubmit:  preReq,
		        success:       showResponse,  // post-submit callback 
		        error : errorFunction,
		        type:		'POST',
		        uploadProgress : progress,
		 
		        // other available options: 
		        //url:       url         // override for form's 'action' attribute 
		        //type:      type        // 'get' or 'post', override for form's 'method' attribute 
		        //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
		        //clearForm: true        // clear all form fields after successful submit 
		        //resetForm: true        // reset the form after successful submit 
		 
		        // $.ajax options can be used here too, for example: 
		        //timeout:   3000 
		    }; 
		 
		$('#registerForm').submit(function() { 
	        // inside event callbacks 'this' is the DOM element so we first 
	        // wrap it in a jQuery object and then invoke ajaxSubmit 
	        $(this).ajaxSubmit(options); 
	 
	        // !!! Important !!! 
	        // always return false to prevent standard browser submit and page navigation 
	        return false; 
	    }); 
	})
</script>
<style>
.ui-progressbar {
	position: relative;
}

.progress-label {
	position: absolute;
	left: 50%;
	top: 4px;
	font-weight: bold;
	text-shadow: 1px 1px 0 #fff;
}
</style>
</head>
<body>

	<c:if test="${(null ne sessionScope.teacher) or (null ne sessionScope.admin) or (null ne sessionScope.exe) }">
		<c:redirect url="./index.jsp"></c:redirect>
	</c:if>
		

	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        
        	<jsp:useBean id="messages" scope="request" class="java.util.ArrayList" type="java.util.ArrayList<String>"></jsp:useBean>
	
			<div id="messagePanel">
			<c:forEach var="message" items="${messages }">
				<div class="alert alert-danger"><strong>Error!</strong> ${message }</div>
			</c:forEach>
			</div>
			
			<div class="content" style="width: 100%; height: auto; margin: 0 auto">
				<form id="registerForm" role="form" enctype="multipart/form-data" action="./registerTeacher.do" method="post" accept-charset="utf-8">
					<jsp:include page="TeacherInformationForm.jsp" flush="false"></jsp:include>
					<div id="progressbar" style="display:none; margin-bottom: 10px;"><div class="progress-label">Loading...</div></div>
					<button type="submit" class="btn btn-primary btn-block">Submit For Approval</button>
				</form>
			</div>
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>