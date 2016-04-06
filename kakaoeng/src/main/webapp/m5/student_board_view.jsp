<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set scope="page" value="student" var="boardName"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="../minimal/grey.css" rel="stylesheet">
<script src="../icheck.js"></script>
<title>Insert title here</title>
<script>
	$(document).ready(function(){
		$('#comment').click(function(){
			if($('#commentInput').val().length > 500){
				alert('댓글은 500제로 제한되어있습니다.');
				return;
			}
			var s = {};
			s["boardId"] = Number($('#idHolder').val());
			s["comment"] = $('#commentInput').val();
			$.ajax({
				type:'POST',
				url:makeUrl('./${boardName}Comment.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr.responseText);
				},
			})
		})
	})
	<c:if test="${lc.adminLogin eq true }">
	function noticeFunction(notice){
		$.ajax({
			type:'GET',
			url:makeUrl('./${boardName}Notice.do?boardId=${Board.id}&notice='+notice),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
	}
	</c:if>
</script>
<script>
	$(document).ready(function(){
		$('.delete-comment').click(function(){
			var idList = [];
			idList.push($(this).attr('data-id'));
			var s = {"deleteList" : JSON.stringify(idList) };
			$.ajax({
				type:'POST',
				url:makeUrl('./${boardName}BoardDelete.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					window.location.reload();
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
						}
					}
			)
		})
		$('#removeButton').click(function(){
			if (!confirm('정말로 삭제 하시겠습니까??')) {
				return;
			}
			var idList = [];
			var checkList = $('.manageCheck:checked');
			idList.push("${param.boardId}");
			var s = {"deleteList" : JSON.stringify(idList) };
			$.ajax({
				type:'POST',
				url:makeUrl('./${boardName}BoardDelete.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					window.location = './${boardName}BoardList.view';
					},
					error: function(xhr, status, error){
						alert(xhr.responseText);
						}
					}
			)
		})
		$('#editButton').click(function(){
			window.location = './${boardName}BoardUpdate.do?boardId=${param.boardId}&page=${param.page}&viewPerPage=${param.viewPerPage}'
		})
	})
</script>
<style>
	.comment_row{
		border-top: 1px solid rgb(211,200,204);
	}
	.comment_row:last-of-type {
		border-bottom: 1px solid rgb(211,200,204);
	}
</style>
</head>
<body style="background-color: white;">
	<div id="wrap">
	<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
		<input id="idHolder" type="hidden" value="${param.boardId }" />
        <jsp:useBean id="Board" scope="request" class="java.lang.Object"></jsp:useBean>
        <jsp:useBean id="comments" scope="request" class="java.lang.Object"></jsp:useBean>
        	<div style="height:47px"></div>
        	<div class="content" style="width:100%; margin: 0 auto; border-top: 2px solid rgb(153,96,105)">
				<div style="width:100%; overflow: hidden; border-bottom: 1px solid rgb(153,96,105)">
					<div style="float: left; width: 96px; height:37px; background-image: url('../artifact/student_boardview_title.png')"></div>
					<div style="float:left; width: 904px; height:37px;"><p class="align_vertical_center">&nbsp;&nbsp;${Board.title }</p></div>
				</div>
				<div style="width:100%; overflow: hidden; border-bottom: 1px solid rgb(153,96,105)">
					<div style="float: left; width:96px; height:34px; background-image: url('../artifact/student_boardview_author.png')"></div>
					<div style="float:left; width:664px; height:34px;"><p class="align_vertical_center" style="text-align: left;">&nbsp;&nbsp;${Board.writer }</p></div>
					<div style="float: left; width:134px; height:34px;"><p class="align_vertical_center" style="text-align: center;"><strong>작성일</strong>&nbsp;&nbsp; ${Board.studentBoardDateForm }</p></div>
					<div style="float:left; width:106px; height:34px;"><p class="align_vertical_center" style="text-align: center;"><strong>조회수</strong>&nbsp;&nbsp;${Board.count }</p></div>
				</div>
				<c:if test="${lc.adminLogin }">
   					<div style="width:100%; overflow: hidden; height:40px;">
						<div class="cell" style="float:left; width:100px; height:100%"><p class="align_vertical_center" style="text-align: center;">Notice</p></div>
						<div class="cell" style="float:left; width:580px; height:100%">
						<c:if test="${Board.sort.name() ne 'Normal' }">
							<button class="btn btn-primary" onclick='noticeFunction("unnotice"); return false;'>Un-notice</button>
						</c:if>
						<c:if test="${Board.sort.name() eq 'Normal' }">
							<button class="btn btn-primary" onclick='noticeFunction("notice"); return false;'>notice</button>
						</c:if>
						</div>
						<div class="cell" style="float:left; width:100px; height:100%"><p class="align_vertical_center" style="text-align: center;">Count</p></div>
						<div class="cell" style="float:left; width:100px; height:100%"><p class="align_vertical_center" style="text-align: center;">${Board.count }</p></div>
					</div>			
   				</c:if>
   				<div style="height:20px;"></div>
				<div class="cell" style="width:100%; padding: 5px; overflow: hidden;">
					${Board.contents }
				</div>
				<div style="height:20px;"></div>
				<c:if test="${Board.fileList.size() > 0 }">
				<div style="width:966px;; height:47px; border: 1px solid rgb(153,153,153);">
					<div id="fileLabel" style="width:63px; height:45px; float: left; background-color: rgb(234,234,234)">
						<p class="align_vertical_center center_text" style="color: rgb(153,153,153)">File</p>
					</div>
					<div style="height:100%; overflow:auto; padding: 5px; border-left: 1px solid rgb(153,153,153)">
						<c:forEach items="${Board.fileList }" var="file">
							<a style="display: block;" href="../downloadFile.do?path=${file.DBPath }"> ${file.fileName }</a>
						</c:forEach>
					</div>
				</div>
				</c:if>
				
				
				<div style="height: 10px"></div>
				
				<div style="height: 15px;"></div>
				
				<c:forEach items="${comments }" var="comment">
				<div class="comment_row" style="overflow: hidden;">
					<div style="width:91px; height:85px; float: left; border: none;" >
						<img src="../artifact/profile.jpg" style="margin: 0 auto; width:71px; height:65px; margin: 10px 10px;"/>
					</div>
					<div class="align_vertical_center" style="float: left; margin-left: 4px;  width:905px;">
						<div style="overflow: hidden; margin-bottom: 7px;">
							<p style="float: left; margin-bottom: 0px;">${comment.author.name }</p>
							<p style="float: left; font-size: 10px; margin-left: 10px; margin-bottom: 0px; margin-top: 5px;">${comment.detailDateForm }</p>
							<c:if test="${(lc.loginStudentObject.id eq comment.writer) or (lc.adminLogin)}">
								<p class="delete-comment" data-id="${comment.id }" style="float: left; margin-bottom: 0px; cursor: pointer; margin-left: 10px;">X</p>
							</c:if>
						</div>
						<p style="margin-bottom: 0px; margin-left: 35px;">${comment.contents }</p>
					</div>
				</div>
				</c:forEach>
				
				<div style="height: 15px;"></div>
				
				<c:if test="${lc.studentLogin or lc.adminLogin or lc.execLogin }">
					<div style="border-top: 1px solid rgb(245,245,245); border-bottom: 1px solid rgb(245,245,245); background-color : rgb(234,234,234); overflow: hidden; height:85px; width:100%">
						<div class="align_vertical_center" style="width:65px; height:47px; background-image: url('../artifact/student_boardview_comment.png'); float: left; margin-left: 13px;"></div>
						<textarea id="commentInput" class="align_vertical_center" style="float: left; width: 827px;height:47px; margin-left: 7px; border: 1px solid #c0c0c0"></textarea>
						<button id="comment" class="align_vertical_center" style="float: left;height:47px; width:65px; margin-left: 9px;  background-image: url('../artifact/student_boardview_comment_register.png'); border: none;"></button>
					</div>
				</c:if>
				
				
			</div>
        	
        	
        	
        	<div style="height: 20px;"></div>
        
        	<c:if test="${(lc.loginStudentObject.id eq Board.writer) or lc.adminLogin }">
	        	<div style="overflow: hidden;">
	        		<button id="removeButton" style="float: right; width:136px; height:39px; background-image: url('../artifact/student_boardview_delete.png'); border: none;"></button>
	        		<c:if test="${(lc.loginStudentObject.id eq Board.writer) or ((lc.adminLogin) and (lc.loginAdminObject.id eq Board.writer)) }">
						<button id="editButton" style="float: right; margin-right: 3px; width: 136px; height:39px; background-image: url('../artifact/student_boardview_edit.png'); border: none;"></button>
					</c:if>
				</div>
				<div style="height: 20px;"></div>
			</c:if>
        
        	<jsp:include page="${boardName}BoardListT.jsp" flush="false"></jsp:include>
        	<div style="height:20px;"></div>
        	<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
        	</div>
        	
</body>
</html>