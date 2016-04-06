<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="BoardList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="NoticeBoardList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="pagination" scope="request" class="java.lang.Object"></jsp:useBean>
<c:set scope="page" value="admin" var="boardName"></c:set>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<c:if test="${lc.adminLogin }">	
<script>
	$(document).ready(function(){
		$('#deleteButton').click(function(){
			var idList = [];
			var checkList = $('.manageCheck:checked');
			$.each(checkList, function(index, value){
				idList.push($(value).attr('data-id'));
			})
			if(idList.legnth == 0){
				alert('No selection');
				return;
			}
			var s = {"deleteList" : JSON.stringify(idList) };
			$.ajax({
				type:'POST',
				url:makeUrl('./${boardName}DeleteBoard.do'),
				contentType:'application/x-www-form-urlencoded; charset=UTF-8',
				datatype: 'text/plain',
				data : s,
				success: function(data){
					alert(data);
					window.location = './${boardName}BoardList.view';
					},
					error: function(xhr, status, error){
						alert(xhr);
						alert(status);
						alert(error);
						}
					}
			)
		})
		
	})
</script>		
</c:if>

<style>
.notice_row{
	color: rgb(0,112,192);
	font-weight: bold;
}
</style>

<div class="content" style="width: 100%; margin: 0 auto">
	<table style="text-align: center; margin: 0; border: 1px solid #c0c0c0;" class="table">
		<thead>
			<tr>
				<c:if test="${lc.adminLogin }">	
					<th style="width: 20px; text-align: center;"></th>	
   				</c:if>
				<th style="width: 100px; text-align: center;">No</th>
				<th style="text-align: center;">Title</th>
				<th style="width: 150px; text-align: center;">Author</th>
				<th style="width: 100px; text-align: center;">Date</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="Board" items="${NoticeBoardList }">
				<tr>
					<c:if test="${lc.adminLogin }">	
						<th style="width: 20px; text-align: center;">
							<input class="manageCheck" type="checkbox" data-id="${Board.id }"/>
						</th>	
   					</c:if>
					<td class="notice_row"><p style="margin: 0">Notice</p></td>
					<td class="notice_row" style="text-align: left; cursor: pointer;" onclick='window.location="./${boardName}BoardView.view?boardId=${Board.id }&page=${param.page }&viewPerPage=${param.viewPerPage }"; return false;'>
						${Board.title }<c:if test="${Board.commentList.size() > 0 }">&nbsp;[${Board.commentList.size() }]</c:if>
					</td>
					<td class="notice_row">${Board.author.className }</td>
					<td class="notice_row">${Board.dateForm }</td>
				</tr>
			</c:forEach>
			<c:forEach var="Board" items="${BoardList }">
				<tr>
					<c:if test="${lc.adminLogin }">	
						<th style="width: 20px; text-align: center;">
							<input class="manageCheck" type="checkbox" data-id="${Board.id }"/>
						</th>		
   					</c:if>
					<td>${pagination.index }</td>
					<td style="text-align: left; cursor: pointer;" onclick='window.location="./${boardName}BoardView.view?boardId=${Board.id }&page=${param.page }&viewPerPage=${param.viewPerPage }"; return false;'>
						${Board.title }<c:if test="${Board.commentList.size() > 0 }">&nbsp;[${Board.commentList.size() }]</c:if>
					</td>
					<td>${Board.author.className }</td>
					<td>${Board.dateForm }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${lc.adminLogin }">
   		<div style="width:100%; overflow: hidden; margin-top: 5px;">
			<button id="writeButton" class="btn btn-primary" style="float: right;" onclick='window.location="./notify.jsp"; return false;'>Write</button>
			<button id="deleteButton" class="btn btn-danger" style="float: right;">Delete</button>
		</div>			
   	</c:if>
	
	
	<ul class="pagination">
		<c:forEach items="${pagination.getWings(5) }" var="pageIndex">
		<li><a href="${boardName}BoardList.view?page=${pageIndex }&viewPerPage=${pagination.viewPerPage}">${pageIndex }</a></li>
		</c:forEach>
		
	</ul>
</div>

<div style="height:20px;"></div>
