<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set scope="page" value="notify" var="boardName"></c:set>
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

<script type="text/javascript" src="../js/jquery.preload.min.js"></script> 
<script type="text/javascript" src="../js/jquery.ui.totop.js"></script> 
<link href="../css/ui.totop.css" rel="stylesheet" type="text/css"> 
<script>

	function makeUrl(url) {
		var index = url.indexOf('?');
		if (index == -1) {
			url = url + '?stamp=' + Date.now();
		} else {
			url = url + '&stamp=' + Date.now();
		}
		return url;
	}
</script>
<script language="javascript" type="text/javascript">

function clearText(field)
{
    if (field.defaultValue == field.value) field.value = '';
    else if (field.value == '') field.value = field.defaultValue;
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
td{
	border: none !important;
}
</style>
</head>
<body style="background-color : white">
	<c:if test="${lc.studentLogin and lc.adminLogin and lc.execLogin }">
		<c:redirect url="./main.jsp"></c:redirect>
	</c:if>
	
	<div id="wrap">
	<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
		<div style="height:50px;"></div>
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
				<input id="title" style="border-radius : 5px; height: 30px;" type="text" class="form-control" placeholder="제목">
			</div>
			<div id="editor" style="height:300px; max-height: 400px;"></div> 
	
		<div style="height:10px;"></div>
	
		<div id="uploaderContainer" style="border-radius:5px; border: 2px solid #c0c0c0">
			<div style="overflow: hidden; width:100%; height:50px; background-color: rgb(227,227,227)">
				<div id="selectFilesButtonContainer" style="float:left; margin-left: 5px; margin-top: 5px;"></div>
				<div id="deleteFilesButtonContainer" style="float:left; margin-left: 5px; margin-top: 5px;">
					<button type="button" id="deleteFileButton" class="yui3-button"
						style="width: 136px; height: 39px; background-image: url('../artifact/student_boardwrite_filedelete.png')"></button>
				</div>
			</div>
			<div id="overallProgress" style="display: none;"></div>
			<div id="filelist" style="width:987px; height:70px;">
				<table id="filenames" style="width: 987px; height:100%;">
					<tbody>
					</tbody>
				</table>
			</div>
		</div>

		<div style="height:20px;"></div>
		<div style="overflow: hidden;">
			<button style="width:136px; height:39px; background-image: url('../artifact/student_boardwrite_list.png'); border: none;float:right; margin-left: 13px;" onclick='window.location = "./${boardName}BoardView.view?boardId=${param.boardId}&page=${param.page}&viewPerPage=${param.viewPerPage}";'></button>
			<button style="width:136px; height:39px; background-image: url('../artifact/student_boardwrite_register.png'); border: none; float:right;" id="submitButton"></button>
		</div>
		<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
	</div>
	
	
<script>

	YUI({filter:"raw"}).use("uploader", function(Y) {
	   if (Y.Uploader.TYPE != "none" && !Y.UA.ios) {
		   var realFilList=[];
	       var uploader = new Y.Uploader({width: "250px",
	                                      height: "35px",
	                                      multipleFiles: true,
	                                      swfURL: "flashuploader.swf?t=" + Math.random(),
	                                      uploadURL: "../uploadFile.do",
	                                      simLimit: 3,
	                                      withCredentials: false
	                                     });
	       var uploadDone = false;
			
	       uploader.render("#selectFilesButtonContainer");
	
	       $('#selectFilesButtonContainer > div > div').css('width', '136px');
	       $('#selectFilesButtonContainer > div').css('width', '136px');
	       $('#selectFilesButtonContainer').css('width', '136px');
	       var selectButton = $('#selectFilesButtonContainer > div > div > button');
	       selectButton.text('');
	       selectButton.css('width', '136px');
	       selectButton.css('height', '39px');	       
	       selectButton.css('background-image', 'url("../artifact/student_boardwrite_fileattach.png")');
	       selectButton.css('padding', '0');
	       
	       uploader.after("fileselect", function (event) {
	
	          var fileList = event.fileList;
	          var fileTable = Y.one("#filenames tbody");
	          if (fileList.length > 0 && Y.one("#nofiles")) {
	            Y.one("#nofiles").remove();
	          }
	
	          if (uploadDone) {
	            uploadDone = false;
	            fileTable.setHTML("");
	          }
	
	          Y.each(fileList, function (fileInstance) {
	        	  if(fileInstance.get("size") > 10485760){
	        		  alert("10MB를 초과할 수 없습니다.");
	        		  return;
	        	  }
	        	  else if(realFilList.length >= 3){
	        		  alert('한번에 업로드 가능한 최대 파일 갯수는 3개입니다.')
	        		  return;
	        	  }
	        	  else{
	        		  realFilList.push(fileInstance);
	        	  }
	              fileTable.append("<tr id='" + fileInstance.get("id") + "_row" + "'>" +
	                                    '<td class="filename"><div style="overflow:hidden; text-overflow:ellipsis; width:100%; height:20px;">' + fileInstance.get("name") + "</div></td>" +
	                                    "<td class='filesize'>" + fileInstance.get("size") + "Bytes</td>" +
	                                    "<td class='percentdone'>Ready</td>");
	              $('<input class="fileUploadHolder" type="hidden" data-holder="'+ fileInstance.get("id") + "_row" +'" data-complete="false"/>').appendTo($('body'));
	                             });
	    
	       });
	
	       uploader.on("uploadprogress", function (event) {
	            var fileRow = Y.one("#" + event.file.get("id") + "_row");
	            if(fileRow == null)
	            	return;
	                fileRow.one(".percentdone").set("text", event.percentLoaded + "%");
	       });
	
	       uploader.on("uploadstart", function (event) {
	            uploader.set("enabled", false);
	            Y.one("#submitButton").addClass("yui3-button-disabled");
	            Y.one("#submitButton").detach("click");
	       });
	
	       uploader.on("uploadcomplete", function (event) {
	    	   var fileRow = Y.one("#" + event.file.get("id") + "_row");
	    	   if(event.data.search("\\Error") != -1){
	    		   fileRow.one(".percentdone").set("text", "Error!");
	    		   return;
	    	   }
	    	   fileRow.one(".percentdone").set("text", "Finished!");
	    	   var holder = $('input[data-holder="'+ event.file.get("id") + "_row" +'"]');
	    	   holder.val(event.data);
	    	   holder.attr('data-complete', 'true');
	       });
	
	       uploader.on("totaluploadprogress", function (event) {
	                Y.one("#overallProgress").setHTML("Total uploaded: <strong>" +
	                                                     event.percentLoaded + "%" +
	                                                     "</strong>");
	       });
	
	       uploader.on("alluploadscomplete", function (event) {
	    	   uploader.set("enabled", true);
	    	   uploader.set("fileList", []);
	    	   Y.one("#submitButton").removeClass("yui3-button-disabled");
	    	   Y.one("#submitButton").on("click", submitHandler);
	    	   Y.one("#overallProgress").set("text", "Uploads complete!");
	    	   uploadDone = true;
	    	   register();
	       });
	
	      
	       
	       Y.one("#submitButton").on("click", submitHandler);
	       Y.one("#deleteFileButton").on("click", function(){
	    	   var fileList = realFilList;
	    	   var selectedId = $('.selected').attr('id').slice(0,-4);
	    	   $.each(fileList, function(index, value){
	    		   if(selectedId === value.get("id")){
	    			   $("#" + selectedId + "_row").remove();
	    			   realFilList.splice(index,1);
	    			   $('input[data-holder="'+ event.file.get("id") + "_row" +'"]').remove();
	    		   }
	    	   })
	       });
	       function submitHandler(){
	    	   if (!uploadDone && realFilList.length > 0) {
		        	 uploader.set("fileList", realFilList);
		            uploader.uploadAll();
		         }
	    	   else
	    		   register();
	       }
	       function register(){
	    	   var s = {};
	    	   s["title"] = $('#title').val();
	    	   s["contents"] = $('#editor')[0].innerHTML;
	    	   var holderList = $('.fileUploadHolder[data-complete="true"]');
	    	   var holderData = [];
	    	   $.each(holderList, function( index, value){
	    		   holderData.push($(value).val())
	    		   })
	    	   s["holderDataList"] = JSON.stringify(holderData);
	    	   $.ajax({
	    		   type:'POST',
	    		   url:makeUrl('./${boardName}BoardWrite.do'),
	    		   contentType:'application/x-www-form-urlencoded; charset=UTF-8',
	    		   datatype: 'text/plain',
	    		   data : s,
	    		   success: function(data){
	    			   alert(data);
	    			   window.location = './${boardName}BoardList.view';
	    			   },
	    			   error: function(xhr, status, error){
	    				   alert(xhr.responseText);
	    				   window.location = './${boardName}BoardList.view';
	    				   }
	    			   })
	       }
	   }
	   else {
	       Y.one("#uploaderContainer").set("text", "We are sorry, but to use the uploader, you either need a browser that support HTML5 or have the Flash player installed on your computer.");
	   }
	
	
	});

</script>
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
</body>
</html>