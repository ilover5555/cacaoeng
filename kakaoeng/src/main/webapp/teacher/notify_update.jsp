<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Blue Wave Template</title>
<link href="../external/google-code-prettify/prettify.css" rel="stylesheet">
<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.no-icons.min.css" rel="stylesheet">
<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="http://netdna.bootstrapcdn.com/font-awesome/3.0.2/css/font-awesome.css" rel="stylesheet">
<link href="../css/bootstrap.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js"></script>
<script src="../external/jquery.hotkeys.js"></script>
<script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
<script src="../external/google-code-prettify/prettify.js"></script>
<script src="../bootstrap-wysiwyg.js"></script>
<script src="../js/common.js"></script>
<link href="../index.css" rel="stylesheet">
<script src="http://yui.yahooapis.com/3.18.1/build/yui/yui-min.js"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script language="javascript" type="text/javascript">
function makeUrl(url) {
	var index = url.indexOf('?');
	if (index == -1) {
		url = url + '?stamp=' + Date.now();
	} else {
		url = url + '&stamp=' + Date.now();
	}
	return url;
}
function clearText(field)
{
    if (field.defaultValue == field.value) field.value = '';
    else if (field.value == '') field.value = field.defaultValue;
}
function register(){
	   var s = {};
	   s["boardId"] = ${param.boardId}
	   s["page"] = ${param.page}
	   s["viewPerPage"] = ${param.viewPerPage}
	   s["title"] = $('#title').val();
	   s["contents"] = $('#editor')[0].innerHTML;
	   $.ajax({
		   type:'POST',
		   url:makeUrl('./adminBoardUpdate.do'),
		   contentType:'application/x-www-form-urlencoded; charset=UTF-8',
		   datatype: 'text/plain',
		   data : s,
		   success: function(data){
			   alert(data);
			   window.location = './adminBoardView.view?boardId=${param.boardId}&page=${param.page}&viewPerPage=${param.viewPerPage}';
			   },
			   error: function(xhr, status, error){
				   alert(xhr);
				   alert(status);
				   alert(error);
				   }
			   })
}
$(document).ready(function(){
	$(document).on('click', "#filenames > tbody > tr", function(){
		$.each($('#filenames > tbody > tr'), function(index, value){
			if($(value).hasClass('selected'))
				$(value).toggleClass('selected')
		});
		$(this).toggleClass('selected');
	})
})
</script>
<style>
.btn{
	line-height: 0px !important;
}
.selected{
	background-color: rgb(181,215,250);
}
</style>
</head>
<body>

	<c:if test="${(null eq sessionScope.admin) and (null eq sessionScope.exe) }">
		<c:redirect url="./index.jsp"></c:redirect>
	</c:if>
	<jsp:useBean id="adminBoard" scope="request" class="java.lang.Object"></jsp:useBean>
	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
        
        
        	<h1>Notify</h1>
        	<div id="alerts"></div>
			<div class="btn-toolbar" data-role="editor-toolbar"
				data-target="#editor">
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown"
						title="Font"><i class="icon-font"></i><b class="caret"></b></a>
					<ul class="dropdown-menu">
					</ul>
				</div>
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown"
						title="Font Size"><i class="icon-text-height"></i>&nbsp;<b
						class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a data-edit="fontSize 5"><font size="5">Huge</font></a></li>
						<li><a data-edit="fontSize 3"><font size="3">Normal</font></a></li>
						<li><a data-edit="fontSize 1"><font size="1">Small</font></a></li>
					</ul>
				</div>
				<div class="btn-group">
					<a class="btn" data-edit="bold" title="Bold (Ctrl/Cmd+B)"><i
						class="icon-bold"></i></a> <a class="btn" data-edit="italic"
						title="Italic (Ctrl/Cmd+I)"><i class="icon-italic"></i></a> <a
						class="btn" data-edit="strikethrough" title="Strikethrough"><i
						class="icon-strikethrough"></i></a> <a class="btn" data-edit="underline"
						title="Underline (Ctrl/Cmd+U)"><i class="icon-underline"></i></a>
				</div>
				<div class="btn-group">
					<a class="btn" data-edit="insertunorderedlist" title="Bullet list"><i
						class="icon-list-ul"></i></a> <a class="btn"
						data-edit="insertorderedlist" title="Number list"><i
						class="icon-list-ol"></i></a> <a class="btn" data-edit="outdent"
						title="Reduce indent (Shift+Tab)"><i class="icon-indent-left"></i></a>
					<a class="btn" data-edit="indent" title="Indent (Tab)"><i
						class="icon-indent-right"></i></a>
				</div>
				<div class="btn-group">
					<a class="btn" data-edit="justifyleft"
						title="Align Left (Ctrl/Cmd+L)"><i class="icon-align-left"></i></a>
					<a class="btn" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)"><i
						class="icon-align-center"></i></a> <a class="btn"
						data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)"><i
						class="icon-align-right"></i></a> <a class="btn" data-edit="justifyfull"
						title="Justify (Ctrl/Cmd+J)"><i class="icon-align-justify"></i></a>
				</div>
				<div class="btn-group">
					<a class="btn dropdown-toggle" data-toggle="dropdown"
						title="Hyperlink"><i class="icon-link"></i></a>
					<div class="dropdown-menu input-append">
						<input class="span2" placeholder="URL" type="text"
							data-edit="createLink" />
						<button class="btn" type="button">Add</button>
					</div>
					<a class="btn" data-edit="unlink" title="Remove Hyperlink"><i
						class="icon-cut"></i></a>
		
				</div>
		
				<div class="btn-group">
					<a class="btn" title="Insert picture (or just drag & drop)"
						id="pictureBtn"><i class="icon-picture"></i></a>
							<input id="imageFile" type="file"
							data-role="magic-overlay" data-target="#pictureBtn"
							data-edit="insertImage" />
				</div>
				<div class="btn-group">
					<a class="btn" data-edit="undo" title="Undo (Ctrl/Cmd+Z)"><i
						class="icon-undo"></i></a> <a class="btn" data-edit="redo"
						title="Redo (Ctrl/Cmd+Y)"><i class="icon-repeat"></i></a>
				</div>
				<input type="text" data-edit="inserttext" id="voiceBtn"
					x-webkit-speech="">
			</div>
				<div style="width: 100%; margin-bottom: 5px;" class="input-group">
					<input id="title" style="border-radius : 5px; height: 30px;" type="text" class="form-control" placeholder="Title" value="${Board.title }">
				</div>
				<div id="editor">${Board.contents }</div> 
		
			<div style="height:20px;"></div>
			<button style="height:30px;" onclick="register(); return false;" class="btn btn-primary btn-block">Update</button>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<script>
		$(function() {
			function initToolbarBootstrapBindings() {
				var fonts = [ 'Serif', 'Sans', 'Arial', 'Arial Black',
						'Courier', 'Courier New', 'Comic Sans MS', 'Helvetica',
						'Impact', 'Lucida Grande', 'Lucida Sans', 'Tahoma',
						'Times', 'Times New Roman', 'Verdana' ], fontTarget = $(
						'[title=Font]').siblings('.dropdown-menu');
				$
						.each(
								fonts,
								function(idx, fontName) {
									fontTarget
											.append($('<li><a data-edit="fontName ' + fontName +'" style="font-family:\''+ fontName +'\'">'
													+ fontName + '</a></li>'));
								});
				$('a[title]').tooltip({
					container : 'body'
				});
				$('.dropdown-menu input').click(function() {
					return false;
				}).change(
						function() {
							$(this).parent('.dropdown-menu').siblings(
									'.dropdown-toggle').dropdown('toggle');
						}).keydown('esc', function() {
					this.value = '';
					$(this).change();
				});

				$('[data-role=magic-overlay]').each(
						function() {
							var overlay = $(this), target = $(overlay
									.data('target'));
							overlay.css('opacity', 0).css('position',
									'absolute').offset(target.offset()).width(
									target.outerWidth()).height(
									target.outerHeight());
						});
				if ("onwebkitspeechchange" in document.createElement("input")) {
					var editorOffset = $('#editor').offset();
					$('#voiceBtn').css('position', 'absolute').offset(
							{
								top : editorOffset.top,
								left : editorOffset.left
										+ $('#editor').innerWidth() - 35
							});
				} else {
					$('#voiceBtn').hide();
				}
			}
			;
			function showErrorAlert(reason, detail) {
				var msg = '';
				if (reason === 'unsupported-file-type') {
					msg = "Unsupported format " + detail;
				} else {
					console.log("error uploading file", reason, detail);
				}
				$(
						'<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>'
								+ '<strong>File upload error</strong> '
								+ msg
								+ ' </div>').prependTo('#alerts');
			}
			;
			initToolbarBootstrapBindings();
			$('#editor').wysiwyg({
				fileUploadError : showErrorAlert
			});
			window.prettyPrint && prettyPrint();
		});
	</script>
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>