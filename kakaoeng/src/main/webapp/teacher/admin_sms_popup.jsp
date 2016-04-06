<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html style="width:550px; padding-left:25px;">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script src="../js/autoSizePopup.js"></script>
<title>SMS popup</title>
<script>
var selected = null;
var selectedJSON = null;
function receiveMessage(event)
{	
	var parsed = JSON.parse(event.data);
	list = parsed["sendList"];
	selected = JSON.parse(parsed["selectedJSON"]);
	$('#sendList').val(list);
}
window.addEventListener("message", receiveMessage, false);
function getSMSTextLength(str) {
    var len = 0;
    for (var i = 0; i < str.length; i++) {
        if (escape(str.charAt(i)).length == 6) {
            len++;
        }
        len++;
    }
    return len;
}

$(document).ready(function(){
	
	$('#msg').keyup(function(){
		var bytes = getSMSTextLength($('#msg').val());
		$('#allBytes').html(bytes+'bytes');
	})
	$('#sendSMS').click(function(){
		var s ={"msg":$('#msg').val(),
				"selected":JSON.stringify(selected)};
		$.ajax({
			type:'POST',
			url:makeUrl('./adminHandleSMSPopup.do'),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
			success: function(data){
				alert(data);
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
		
	})
	
	window.opener.postMessage('','*');
	autoSizePopup();
})
</script>
</head>
<body style="width: 500px;">


<div class="popup_wrap" style="background-color: white; width: 500px;">
	<div style="width: 500px;">
		<h2 style="text-align: center;">SMS보내기</h2>
	
		<div style="width:100%; overflow: hidden;">
			<button id="sendSMS" class="btn btn-info" style="float:right;">SMS</button>
		</div>
		
		<div style="width: 100%; margin: 10px 0">
			<textarea id="sendList" rows="6" disabled="disabled" style="width: 100%;"></textarea>
		</div>
		<div style="width: 100%; margin: 10px 0">
			<textarea id="msg" rows="6" style="width: 100%;" placeholder="SMS 내용을 여기에 입력하세요"></textarea>
			<span id="allBytes">0bytes</span>
		</div>
		
	</div>
</div>
</body>
</html>


