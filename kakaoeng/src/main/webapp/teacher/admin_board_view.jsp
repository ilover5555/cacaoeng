<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<style>
.cell{
	border: 1px solid #c0c0c0;
}
ol, ul{
	padding-left: 15px;
}
ul{
	list-style-type: disc;
}
ul>li{
	
}
</style>
<script>
	$(document).ready(function(){
		$('#comment').click(function(){
			var s = {};
			s["boardId"] = Number($('#idHolder').val());
			s["comment"] = $('#commentInput').val();
			$.ajax({
				type:'POST',
				url:makeUrl('./adminComment.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					location.reload();
				},
				error: function(xhr, status, error){
					alert(xhr);
					alert(status);
					alert(error);
				},
			})
		})
	})
	<c:if test="${lc.adminLogin }">
	function noticeFunction(notice){
		$.ajax({
			type:'GET',
			url:makeUrl('./adminNotice.do?boardId=${Board.id}&notice='+notice),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr);
				alert(status);
				alert(error);
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
				url:makeUrl('./adminDeleteBoard.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					window.location.reload();
					},
					error: function(xhr, status, error){
						alert(xhr);
						alert(status);
						alert(error);
						}
					}
			)
		})
		$('#removeButton').click(function(){
			var idList = [];
			var checkList = $('.manageCheck:checked');
			idList.push("${param.boardId}");
			var s = {"deleteList" : JSON.stringify(idList) };
			$.ajax({
				type:'POST',
				url:makeUrl('./adminDeleteBoard.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					window.location = './adminBoardList.view';
					},
					error: function(xhr, status, error){
						alert(xhr);
						alert(status);
						alert(error);
						}
					}
			)
		})
		$('#editButton').click(function(){
			window.location = './adminBoardUpdate.do?boardId=${param.boardId}&page=${param.page}&viewPerPage=${param.viewPerPage}'
		})
	})
</script>	
</head>
<body>

	
		

	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
    	<input id="idHolder" type="hidden" value="${param.boardId }" />
        <div id="tooplate_content">
        <jsp:useBean id="Board" scope="request" class="java.lang.Object"></jsp:useBean>
         <jsp:useBean id="comments" scope="request" class="java.lang.Object"></jsp:useBean>
        	<div class="content" style="width:100%; margin: 0 auto">
				<div style="width:100%; overflow: hidden">
					<div class="cell" style="float: left; width: 100px;"><p class="align_vertical_center" style="text-align: center;">Title</p></div>
					<div class="cell" style="float:left; width: 780px;"><p class="align_vertical_center">&nbsp;&nbsp;${Board.title }</p></div>
				</div>
				<div style="width:100%; overflow: hidden">
					<div class="cell" style="float: left; width:100px;"><p class="align_vertical_center" style="text-align: center;">No</p></div>
					<div class="cell" style="float:left; width:70px;"><p class="align_vertical_center" style="text-align: center;">${Board.id }</p></div>
					<div class="cell" style="float: left; width:100px;"><p class="align_vertical_center" style="text-align: center;">Author</p></div>
					<div class="cell" style="float:left; width:200px;"><p class="align_vertical_center" style="text-align: center;">${Board.author.name }</p></div>
					<div class="cell" style="float: left; width:100px;"><p class="align_vertical_center" style="text-align: center;">Date</p></div>
					<div class="cell" style="float:left; width:110px;"><p class="align_vertical_center" style="text-align: center;">${Board.dateForm }</p></div>
					<div class="cell" style="float: left; width:100px;"><p class="align_vertical_center" style="text-align: center;">Reply</p></div>
					<div class="cell" style="float:left; width:100px;"><p class="align_vertical_center" style="text-align: center;">${comments.size() }</p></div>
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
				<div class="cell" style="width:100%; padding: 5px;">
					${Board.contents }
				</div>
				<div style="height:20px;"></div>
				<c:if test="${Board.fileList.size() > 0 }">
				
					<div class="cell" style="width:100%; height:100px;">
						<div id="fileLabel" class="cell" style="height: 100%; float: left;">
							<p class="align_vertical_center center_text" style="width:70px;">File</p>
						</div>
						<div style="height:100%; overflow:auto; padding: 5px;">
							<c:forEach items="${Board.fileList }" var="file">
								<a style="display: block;" href="./downloadFile.do?path=${file.DBPath }"> ${file.fileName }</a>
							</c:forEach>
						</div>
					</div>
				</c:if>
				
				
				<div style="height: 10px"></div>
				
				<c:if test="${lc.adminLogin }">
					<div style="height: 15px;"></div>
				
					<div style="border-top: 1px solid rgb(187,209,227); border-bottom: 1px solid rgb(187,209,227); overflow: hidden; padding-top: 10px; padding-bottom: 10px;">
						<textarea id="commentInput" style="float: left; width: 780px;height:68px;"></textarea>
						<button id="comment" class="btn btn-primary" style="float: left;height:68px; width:100px;">Comment</button>
					</div>
				</c:if>
			</div>
        	
        	<c:forEach items="${comments }" var="comment">
				<div>
					<p style="float: left; margin-bottom: 0px;">${comment.author.className }</p>
					<p style="float: left; font-size: 10px; color: #c0c0c0; margin-left: 10px; margin-bottom: 0px;">${comment.detailDateForm }</p>
					<c:if test="${user.id eq comment.writer }">
						<p class="delete-comment" data-id="${comment.id }" style="float: left; margin-bottom: 0px; cursor: pointer; margin-left: 10px;">X</p>
					</c:if>
					<div style="clear: both;"></div>
					<p style="margin-bottom: 0px;">${comment.contents }</p>
				</div>
				<hr style="margin : 10px auto" />
			</c:forEach>
        	
        	<div style="height: 20px;"></div>
        
        	<c:if test="${user.id eq Board.writer }">
        	<div style="overflow: hidden;">
				<button id="editButton" class="btn btn-warning" style="float: right;">Edit</button>
				<button id="removeButton" class="btn btn-danger" style="float: right; margin-right: 10px;">Remove</button>
			</div>
				<div style="height: 20px;"></div>
			</c:if>
        
        	<jsp:include page="BoardList.jsp" flush="false"></jsp:include>
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>