<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="BoardList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="NoticeBoardList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="pagination" scope="request" class="java.lang.Object"></jsp:useBean>

<link href="../minimal/grey.css" rel="stylesheet">
<script src="../icheck.js"></script>
<c:set scope="page" value="free" var="boardName"></c:set>

<script>
	$(document).ready(function(){
		<c:if test="${null ne sessionScope.admin }">	
		$('#deleteButton').click(function(){
			if (!confirm('정말로 삭제 하시겠습니까??')) {
				return;
			}
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
		</c:if>
		$('.search-method').iCheck({
			checkboxClass:'icheckbox_minimal-grey',
			radioClass:'iradio_minimal-grey',
			increaseArea:'50%'
		});
		$('#checkAll').change(function(){
			
			if($('#checkAll:checked').length == 1){
				$.each($('.manageCheck'), function(index, value){
					value.checked=true;
				})
			}
			else{
				$.each($('.manageCheck'), function(index, value){
					value.checked=false;
				})
			}
		})
	})
</script>

<style>
#${boardName}BoardTable thead tr th{
	height : 34px !important;
	border:none !important;
	padding:0 !important;
	text-align: center;
}
#${boardName}BoardTable tbody tr:last-of-type td{
	border: none !important;
}
#${boardName}BoardTable td{
	padding: 0 !important;
	height : 36px !important;
	border-top: none !important;
}
.search-method{
	float:left;
}
.search_check{
	float:left;
	overflow: hidden;
}
.iradio_minimal-grey{
	float:left;
}
</style>	
<table id="${boardName}BoardTable" style="text-align: center; margin: 0; border: 1px solid #c0c0c0;" class="table">
	<thead style='width:1000px; height:34px;'>
		<tr style="height: 34px;">
			<th style="width: 36px;  background-image : url('../artifact/student_boardlist_header_check.png');">
				<input id="checkAll" class="align_vertical_center" type="checkbox"/>
			</th>	
			<th style="width: 62px; background-image : url('../artifact/student_boardlist_header_no.png');"></th>
			<th style="width: 571px; background-image : url('../artifact/student_boardlist_header_title.png');"></th>
			<th style="width: 150px; background-image : url('../artifact/student_boardlist_header_writer.png');"></th>
			<th style="width: 94px; background-image : url('../artifact/student_boardlist_header_date.png');"></th>
			<th style="width: 87px; background-image : url('../artifact/student_boardlist_header_count.png');"></th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="Board" items="${NoticeBoardList }">
			<tr>
				<td style="width: 20px; text-align: center; border-bottom: none;">
					<input class="manageCheck align_vertical_center" type="checkbox" data-id="${Board.id }"/>
				</td>	
				<td style="border-bottom: 1px dotted rgb(264,208,197)"><div style="margin-top:7px;width:58px; height:22px; background-image: url('../artifact/student_boardlist_notice.png'); background-repeat: no-repeat;"></div></td>
				<td style="text-align: left; cursor: pointer;border-bottom: 1px solid rgb(264,208,197);" onclick='window.location="./${boardName}BoardView.view?boardId=${Board.id }&page=${param.page }&viewPerPage=${param.viewPerPage }&searchMethod=${param.searchMethod }&searchText=${param.searchText }"; return false;'>
					<p class="align_vertical_center" style="font-weight: bold;">&nbsp;&nbsp;${Board.title }<c:if test="${Board.commentList.size() > 0 }">&nbsp;<span class="badge">${Board.commentList.size() }</span></c:if></p>
				</td>
				<td style="border-bottom: 1px solid rgb(264,208,197);"><p class="align_vertical_center" style="font-weight: bold;">${Board.author.className }</p></td>
				<td style="border-bottom: 1px solid rgb(264,208,197);"><p class="align_vertical_center" style="font-weight: bold; font-size: 13px;">${Board.dateForm }</p></td>
				<td style="border-bottom: 1px dotted rgb(264,208,197);"><p class='align_vertical_center' style="font-weight: bold;">${Board.count }</p></td>
			</tr>
		</c:forEach>
		<c:forEach var="Board" items="${BoardList }">
			<tr style="border: none;">
				<td style="width: 20px; text-align: center; border-bottom: none;">
					<input class="manageCheck align_vertical_center" type="checkbox" data-id="${Board.id }"/>
				</td>	
				<td style="border-bottom: 1px dotted rgb(264,208,197)"><p class="align_vertical_center">${pagination.index }</p></td>
				<td style="text-align: left; cursor: pointer;border-bottom: 1px solid rgb(264,208,197);" onclick='window.location="./${boardName}BoardView.view?boardId=${Board.id }&page=${param.page }&viewPerPage=${param.viewPerPage }&searchMethod=${param.searchMethod }&searchText=${param.searchText }"; return false;'>
					<p class="align_vertical_center">&nbsp;&nbsp;${Board.title }<c:if test="${Board.commentList.size() > 0 }">&nbsp;<span class="badge">${Board.commentList.size() }</span></c:if></p>
				</td>
				<td style="border-bottom: 1px solid rgb(264,208,197);"><p class="align_vertical_center">${Board.author.className }</p></td>
				<td style="border-bottom: 1px solid rgb(264,208,197);"><p class="align_vertical_center" style="font-size:13px;">${Board.dateForm }</p></td>
				<td style="border-bottom: 1px dotted rgb(264,208,197);"><p class='align_vertical_center'>${Board.count }</p></td>
			</tr>
			</c:forEach>
	</tbody>
</table>

		<div style="height:9px;"></div>
			
		<div style="text-align: center;">
			<ul class="align_compact_vertical_center" style="overflow: hidden; height:39px; display: inline-block;">
				<li class="align_vertical_center" style="list-style: none; float: left; margin-right: 13px;"><a style="font-size: 1em" href="${boardName}BoardList.view?page=${pagination.minPage }&viewPerPage=${pagination.viewPerPage}&searchMethod=${param.searchMethod }&searchText=${param.searchText }"><span style="color: black;" class="glyphicon glyphicon-triangle-left"></span></a></li>
				<c:set var="wing" scope="request" value="${pagination.getWings(5) }"></c:set>
				<c:forEach var="i" begin="0" end="${wing.size()-1 }">
				<li class="align_vertical_center" style="list-style: none; float: left;"><a style="color: black;" href="${boardName}BoardList.view?page=${wing.get(i) }&viewPerPage=${pagination.viewPerPage}&searchMethod=${param.searchMethod }&searchText=${param.searchText }">${wing.get(i) }</a></li>
				<c:if test="${i<wing.size()-1 }">
					<li class="align_vertical_center" style="list-style: none; float: left; margin-left: 15px; margin-right: 15px;"><span style="color: black;">|</span></li>
				</c:if>
				</c:forEach>
				<li class="align_vertical_center" style="list-style: none; float: left; margin-left: 13px;"><a href="${boardName}BoardList.view?page=${pagination.maxPage }&viewPerPage=${pagination.viewPerPage}&searchMethod=${param.searchMethod }&searchText=${param.searchText }"><span style="color: black;" class="glyphicon glyphicon-triangle-right"></span></a></li>
			</ul>
		</div>
		<div style="text-align: center;">
			<div style="height : 21px; display: inline-block;">
				<form action="./${boardName}BoardList.view" style="overflow: hidden;" method="GET">
					<input type="hidden" name="page" value="${param.page }"/>
					<input type="hidden" name="viewPerPage" value="${param.viewPerPage }"/>
					<div class="align_vertical_center search_check"><input class="search-method" type="radio" name="searchMethod" value="name" <c:if test="${param.searchMethod eq 'name' }">checked</c:if>><span class="center_text" style="float:left">이름</span></div>
					<div class="align_vertical_center search_check"><input class="search-method" type="radio" name="searchMethod" value="title" <c:if test="${(param.searchMethod eq 'title') or (param.searchMethod eq null) or (param.searchMethod eq '') }">checked</c:if>><span class="center_text" style="float:left">제목</span></div>
					<div class="align_vertical_center search_check"><input class="search-method" type="radio" name="searchMethod" value="contents" <c:if test="${param.searchMethod eq 'contents' }">checked</c:if>><span class="center_text" style="float:left">내용</span></div>
					<input name="searchText" type="text" style="width: 164px; height:21px; border : 1px solid #c0c0c0; margin-left: 5px; margin-right: 5px; float:left;" value="${param.searchText }"/>
					<button type="submit" style="border: none; width:72px; height:22px; float:left; background-image: url('../artifact/student_boardlist_search.png')"></button>
				</form>
			</div>
		</div>
		<div style="overflow: hidden; height: 39px;">
			<div style="overflow: hidden;">
				<c:if test="${lc.studentLogin or lc.adminLogin }">
					<button id="writeButton" style="float: right; width:136px; height:39px; background-image: url('../artifact/student_write_board.png'); border: none;" onclick='window.location="./${boardName}_board_write.jsp"; return false;'></button>
				</c:if>
				
				<c:if test="${(null ne param.searchMethod) and ('' ne param.searchMethod) }">
						<button style="float: right; width:136px; height:39px; background-image: url('../artifact/student_boardwrite_list.png'); border: none; margin-right: 5px;" onclick='window.location="./${boardName}BoardList.view"; return false;'></button>			
				</c:if>
				<c:if test="${lc.adminLogin }">
						<button id="deleteButton" style="float: right; width:136px; height:39px; background-image: url('../artifact/student_boardview_delete.png'); border: none; margin-right: 5px;"></button>			
				</c:if>
			</div>
			
			
		</div>
		
		<div style="height:20px;"></div>
		
		
		
</body>
</html>