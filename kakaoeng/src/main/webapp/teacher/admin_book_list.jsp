<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="bookList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="pagination" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link rel="stylesheet" type="text/css" href="../css/font-awesome.min.css" />
<script src="../js/bootstrap-filestyle.min.js"></script>
<title>Insert title here</title>
<style type="text/css">
	.bootstrap-filestyle{
		width: 100%;
	}
</style>
<script type="text/javascript">


$(document).ready(function(){
	
	$('.deleteButton').click(function(){
		if(!confirm("책정보를 삭제하시겠습니까?"))
			return;
		var id = $(this).attr('data-id');
		
		$.ajax({
				type:'GET',
				url:makeUrl('./adminBookDelete.do?bookId='+id),
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
	})
	$('.disableButton').click(function(){
		var id = $(this).attr('data-id');
		
		$.ajax({
				type:'GET',
				url:makeUrl('./adminBookDisable.do?disabled=true&bookId='+id),
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
	})
	$('.enableButton').click(function(){
		var id = $(this).attr('data-id');
		
		$.ajax({
				type:'GET',
				url:makeUrl('./adminBookDisable.do?disabled=false&bookId='+id),
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
	})
	$('.imageFile').change(function(e){
		var parent = $(this).parent();
		var formData = new FormData();
		formData.append("bookId", $('input[name="bookId"]', parent).val())
		formData.append("image", $('input[name="image"]', parent)[0].files[0])
		$.ajax({
            url: makeUrl('./adminBookPicture.do'),
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result){
            	alert(result);
            	location.reload();
            },
            error:function(xhr, status, error){
            	alert(xhr.responseText);
            }
        });
	})
	
	$('.linkButton').click(function(){
		var parent = $(this).parent();
		var input = $('input', parent);
		var id = input.attr('data-id');
		var link = input.val();
		var s = {"bookId" : Number(id), "link" : link};
		$.ajax({
			type:'POST',
			url:makeUrl('./adminBookLink.edit'),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
	})
	
	$('.modifyButton').click(function(){
		var id = $(this).attr('data-id');
		var course = $('option:selected',$('#course'+id)).val();
		var level = $('option:selected', $('#level'+id)).val();
		var title = $('#title'+id).val();
		var s = {"bookId":id,"course":course, "level":level, "title":title};
		$.ajax({
			type:'POST',
			url:makeUrl('./adminBookUpdate.do'),
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
	})
	
	$('#registerButton').click(function(e){
		var parent = $('#registerPanel')
		var formData = new FormData();
		formData.append("course", $('select[name="course"] > option:selected', parent).val());
		formData.append("level", $('select[name="level"] > option:selected', parent).val());
		formData.append("title", encodeURIComponent($('input[name="title"]', parent).val()));
		formData.append("image", $('input[name="image"]', parent)[0].files[0])
		formData.append("link", encodeURIComponent($('input[name="link"]', parent).val()));
		$.ajax({
            url: makeUrl('./adminBoookRegister.do'),
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            success: function(result){
            	location.reload();
            },
            error:function(xhr, status, error){
            	alert(xhr.responseText);
            }
        });
	})
	
	$(":file").filestyle();
	
	$('.bootstrap-filestyle').each(function(index, value){
		var parent = $(this).parent();
		var file = $(':file', parent);
		var init = file.attr('data-init');
		var display = $('input', $(this));
		display.val(init);
	})
})
</script>
</head>
<body>
	<div id="tooplate_wrapper">

		<jsp:include page="Header.jsp" flush="false"></jsp:include>

		<div id="tooplate_main">

			<div id="tooplate_content">
				<div id="registerPanel" class="form-group">
					<label>
						<h3>교재 등록</h3>
					</label>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">구분</span>
						<select class="form-control" name="course">
							<option value="TypeEasy">초급/주니어</option>
							<option value="TypeMiddle">중급/시니어</option>
							<option value="TypeFreeTalk">Free Talking</option>
							<option value="TypeElementBook">미국초등교과서</option>
							<option value="TypeBussiness">Business</option>
							<option value="TypeExam">시험대비</option>
						</select>
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">레벨</span>
						<select class="form-control" name="level">
							<option value="Level1">Level1</option>
							<option value="Level2">Level2</option>
							<option value="Level3">Level3</option>
							<option value="Level4">Level4</option>
							<option value="Level5">Level5</option>
							<option value="Level6">Level6</option>
							<option value="Level7">Level7</option>
						</select>
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">제목</span>
						<input type="text" class="form-control" name="title" placeholder="제목">
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">대표이미지</span>
						<input type="file" name="image" />
					</div>
					<div style="width: 100%" class="input-group">
						<span style="width: 150px" class="input-group-addon">링크</span>
						<input type="text" class="form-control" name="link" placeholder="링크">
					</div>
				</div>
				<button id="registerButton" class="btn btn-primary btn-block">Submit</button>
				
				<div style="height: 30px;"></div>
				<table class="table table-bordered" width="1300px;">

							<thead>
								<tr>
									<th style="width: 30px;"></th>
									<th style="width: 50px;">번호</th>
									<th style="width: 130px;">구분</th>
									<th style="width: 85px;">레벨</th>
									<th>제목</th>
									<th style="width:150px;"></th>
								</tr>
							</thead>

							<tbody>
							<c:forEach items="${bookList }" var="book">
								<tr>
									<td data-toggle="collapse" data-target="#row${book.id }" class="accordion-toggle"><button class="btn btn-default btn-xs align_vertical_center"><span class="glyphicon glyphicon-eye-open"></span></button></td>
									<td><p class="align_vertical_center" style="text-align: center;">${pagination.index }</p></td>
									<td>
										<select id="course${book.id }" class="align_vertical_center" name="course" style="height: 35px;">
											<option value="TypeEasy" <c:if test="${book.course eq 'TypeEasy' }">selected="selected"</c:if>>초급/주니어</option>
											<option value="TypeMiddle" <c:if test="${book.course eq 'TypeMiddle' }">selected="selected"</c:if>>중급/시니어</option>
											<option value="TypeFreeTalk" <c:if test="${book.course eq 'TypeFreeTalk' }">selected="selected"</c:if>>Free Talking</option>
											<option value="TypeElementBook" <c:if test="${book.course eq 'TypeElementBook' }">selected="selected"</c:if>>미국초등교과서</option>
											<option value="TypeBussiness" <c:if test="${book.course eq 'TypeBussiness' }">selected="selected"</c:if>>Business</option>
											<option value="TypeExam" <c:if test="${book.course eq 'TypeExam' }">selected="selected"</c:if>>시험대비</option>
										</select>
									</td>
									<td>
										<select id="level${book.id }" class="align_vertical_center" name="level" style="height: 35px;">
											<option value="Level1" <c:if test="${book.level eq 'Level1' }">selected="selected"</c:if>>Level1</option>
											<option value="Level2" <c:if test="${book.level eq 'Level2' }">selected="selected"</c:if>>Level2</option>
											<option value="Level3" <c:if test="${book.level eq 'Level3' }">selected="selected"</c:if>>Level3</option>
											<option value="Level4" <c:if test="${book.level eq 'Level4' }">selected="selected"</c:if>>Level4</option>
											<option value="Level5" <c:if test="${book.level eq 'Level5' }">selected="selected"</c:if>>Level5</option>
											<option value="Level6" <c:if test="${book.level eq 'Level6' }">selected="selected"</c:if>>Level6</option>
											<option value="Level7" <c:if test="${book.level eq 'Level7' }">selected="selected"</c:if>>Level7</option>
										</select>
									</td>
									<td>
										<div class="form-group align_vertical_center">
											<div style="width: 100%" class="input-group">
												<input id="title${book.id }" style="height: 35px;" type="text" class="form-control" name="title" placeholder="Title" value="${book.title }">
											</div>
										</div>
									</td>
									<td>
										<div class="align_vertical_center" style="width: 113px; height:35px; margin: 0 auto;">
											<a class="modifyButton" href="#" data-id="${book.id }"><i style="font-size: 35px;" class="fa fa-floppy-o"></i></a>
											<a class="deleteButton" href="#" data-id="${book.id }"><i style="font-size: 35px;" class="fa fa-trash-o"></i></a>
											<c:if test="${book.disabled eq false }">
												<a class="disableButton" href="#" data-id="${book.id }"><i style="font-size:35px;" class="fa fa-unlock"></i></a>
												
											</c:if>
											<c:if test="${book.disabled eq true }">
												<a class="enableButton" href="#" data-id="${book.id }"><i style="font-size:35px;" class="fa fa-lock"></i></a>
											</c:if>
										</div>
									</td>
								</tr>
								<tr>
									<td class="hiddenRow" colspan="8">
										<div class="accordian-body collapse" id="row${book.id }">
											<table class="table table-bordered">
												<thead>
													<tr>
														<th width="150px;">속성</th>
														<th>값</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td style="text-align: center; vertical-align: middle">대표이미지</td>
														<td style="vertical-align: middle; padding-bottom: -10px; overflow: hidden;">
															<div class="form-group" style="overflow: hidden; margin-bottom: -19px;;">
																<div  style="width: 100%" class="input-group">
																	<input data-init="${book.bookPictureFile.fileName }" class="imageFile" type="file" name="image" data-buttonText="Find file" ><br/>
																	<input type="hidden" value="${book.id }" name="bookId" />
																</div>
															</div>
														</td>
													</tr>
													<tr>
														<td style="text-align: center; vertical-align: middle;">책소개링크</td>
														<td style="vertical-align: middle;">
															<div class="form-group" style="vertical-align: middle; display: table-cell; text-align: center; width: 700px;">
																<div  style="width: 100%" class="input-group">
																	<input data-id="${book.id }" type="text" class="form-control" style="width:90%; height: 30px;" name="link" placeholder="Link" value="${book.bookLink }">
																	<button class="linkButton" style="width:10%; height: 30px;" class="input-group-addon btn btn-primary">입력</button>
																</div>
															</div>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
									</td>
								</tr>
							</c:forEach>
							</tbody>

						</table>
						
						<ul class="pagination">
							<c:forEach items="${pagination.getWings(5) }" var="pageIndex">
							<li><a href="adminBookList.view?page=${pageIndex }&viewPerPage=${pagination.viewPerPage}">${pageIndex }</a></li>
							</c:forEach>
							
						</ul>
			</div>
			<!-- end of content -->

		</div>
		<jsp:include page="Tail.jsp" flush="false"></jsp:include>
		
		<div id="light" class="white_content"></div>
		<div id="fade" class="black_overlay"></div>
		<!-- end of main -->
	</div>
</body>
</html>