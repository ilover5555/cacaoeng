<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="../js/bootstrap-filestyle.min.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="teacher" scope="request" class="java.lang.Object"></jsp:useBean>
<style>
	.audioplayer{
		z-index : 100;
		float: left;
		width:25%;
		margin: 0 0 ;
	}
	#teacherCard .audioplayer{
		width: 100%;
	}
	.audioplayer-time-current{
		display: none;
	}
	.audioplayer-bar{
		left:2.5em;
	}
	.audioplayer-playpause{
		z-index: 10 !important;
	}
</style>

<script>
	function makeO(){
		return {
				count : 0,
				done : false,
				used : {},
				start : function(){
					this.done = true;
					this.count++;
					},
				end : function(){
					this.count--;
					},
				finished : function(){
					if(this.count == 0)
						return true;
					else
						return false;
				},
				isUsed : function(){
					if(this.done == true)
						return true;
					else
						return false;
				},
				setDefault : function(key, d){
					this.used[key] = d;
				},
				isDefault : function(key){
					return this.used[key];
				},
				appendResult : function(key, value){
					this[key] = value;
				},
				get : function(key){
					return this[key];
				}
		}
	};
	
	function fileToBase64(selector, size, obj){
		var f = $(selector)[0];
		if(f.files && f.files[0]){
			var FR = new FileReader();
			FR.onloadstart = function(e){
				if(e.total > size){
					alert('File Size Cannot Over '+size+'bytes');
					e.target.abort();
					return;
				}
				obj.start();
			}
			FR.onload = function(e){
				obj.appendResult(selector, e.target.result);
				obj.setDefault(selector, false);
				obj.end();
			}
			FR.readAsDataURL(f.files[0]);
		}else{
			obj.start();
			obj.appendResult(selector, $(selector).attr('data-raw'));
			obj.setDefault(selector, true);
			obj.end();
		}
	}

	function getFormData(){
		var formData = new FormData();
		formData.append("className", $('[name="className"]').val());
		formData.append("competency", $('[name="competency"]').val());
		
		return formData;
	}

	var o = null;
	
	$(document).ready(function(){
		
		
		$('#previewButton').click(function(){
			o = makeO();
			fileToBase64('input[name="primaryProfile"]', 5*1024*1024, o);
			fileToBase64('input[name="primaryVoice"]', 5*1024*1024, o);
			$('#loading').css('display', 'block');
			var formData = getFormData();
			var scrollY = $(window).scrollTop();
			$.ajax({
	            url: makeUrl('../profilePreview.view'),
	            data: formData,
	            processData: false,
	            contentType: false,
	            
	            type: 'POST',
	            success: function(result){
	            	$('#light').html(result);
	            	$('#light > div').css('display', 'none');
	            	$('<i id="loading" style="position: fixed;left: 50%;top: 0px;font-size: 100px" class="fa fa-spinner fa-spin" style="font-size: 50px;"></i>').appendTo($('#light'));
	            	scheduleCell.showBox(scrollY, common.init, true, true);
					
					var t = setInterval(function(){ 
						if(o.finished()){
							$('#light > div').css('border-top', '4px #a70920 solid');
							if(o.isDefault('input[name="primaryProfile"]'))
								$('#light > div  .profilePicture').attr('src', '../'+o.get('input[name="primaryProfile"]'));
							else
								$('#light > div  .profilePicture').attr('src', o.get('input[name="primaryProfile"]'));
							if(o.isDefault('input[name="primaryVoice"]'))
								$('#light > div  .profileVoice > source').attr('src', '../'+o.get('input[name="primaryVoice"]'));
							else
								$('#light > div  .profileVoice > source').attr('src', o.get('input[name="primaryVoice"]'));
							if(o.get('input[name="primaryVoice"]') != ''){
								$('#noAudio').css('display', 'none');
								jQuery('#teacherCard audio').audioPlayer();
								theAudio  = jQuery('#teacherCard audio')[0];
								theAudio.addEventListener( 'loadeddata', function()
								{
									jQuery('#loading').css('display', 'none');
									scheduleCell.showBox(scrollY, common.init, false, true);
									jQuery('#light > div').css('border-top', '4px #a70920 solid');
									jQuery('#light > div').css('display', 'block');
									return true;
								});
								
							}else{
								$('#loading').css('display', 'none');
								scheduleCell.showBox(scrollY, common.init, false, true);
								$('#light > div').css('border-top', '4px #a70920 solid');
								$('#light > div').css('display', 'block');
								$('#audioWrapper').html('No Audio File');
							}
							
							clearInterval(t);
							
						}
					}, 500);
					
					
					
	            },
	            error:function(xhr, status, error){
	            	alert(xhr.responseText);
	            }
	        });
			return false;
		})
	})
	
</script>
<div id="teacherInfoPanel">
	<div style="width: 100%" class="form-group">
		<label for="id" class="control-label">
			<h3>General Info</h3>
		</label>
		<div style="width: 100%" class="input-group">
			<div style="width: 150px" class="input-group-addon">ID*</div>
			<input type="text" class="form-control" value="${teacher.id }" name="id"
				placeholder="ID(must be email)">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Password*</span> <input
				type="password" class="form-control" name="pw"
				placeholder="Password">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Password
				Confirm*</span> <input type="password" class="form-control" name="pwConfirm"
				placeholder="Password Confirm">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Full Name*</span>
			<input type="text" class="form-control" name="name" value="${teacher.name }"
				placeholder="Name">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Class Name*</span>
			<input type="text" class="form-control" name="className" value="${teacher.className }"
				placeholder="The name used everytime in teaching. Keep the format [easy first name] [your last name]">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Gender</span> <select
				class="form-control" name="gender">
				<option <c:if test="${ 'Male' eq teacher.gender}">selected</c:if> value="Male" >Male</option>
				<option <c:if test="${ 'Female' eq teacher.gender}">selected</c:if> value="Female">Female</option>
			</select>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Cell Phone*</span>
			<input type="text" class="form-control" name="cellPhone" value="${teacher.cellPhone }"
				placeholder="Cell Phone">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Home Phone</span>
			<input type="text" class="form-control" name="homePhone" value="${teacher.homePhone }"
				placeholder="Home Phone">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Address</span>
			<input type="text" class="form-control" name="address" value="${teacher.address }" placeholder="Address">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Birth*</span> <input
				type="text" class="form-control" name="birth" value="${teacher.birthFormat }"
				placeholder="Birth (Format yyyy-mm-dd)">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">SkypeID*</span>
			<input type="text" class="form-control" name="skype" value="${teacher.skype }" placeholder="Skype ID">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">TelegramID</span>
			<input type="text" class="form-control" name="lineId" value="${teacher.lineId }" placeholder="It will be used for urgent communication w/ company. Pls install Telegram in your PC and Mobile.">
		</div>
	</div>
	<div class="form-group">
		<label>
			<h3>Education Info</h3>
		</label>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Univ/Location*</span>
			<input type="text" class="form-control" name="univ" value="${teacher.univ }"
				placeholder="University / Location">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Degree/Major*</span>
			<input type="text" class="form-control" name="univDetail" value="${teacher.univDetail }"
				placeholder="Degree / Major">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Other Edu Info</span>
			<textarea rows="5" class="form-control" name="education">${teacher.education }</textarea>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Teaching<br/>Experience</span>
			<textarea rows="5" class="form-control" name="experience">${teacher.experience }</textarea>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">TOEFL/TOEIC</span>
			<select class="form-control" name="toefl">
				<option <c:if test="${ true eq teacher.toefl}">selected="selected"</c:if>>Able</option>
				<option <c:if test="${ false eq teacher.toefl}">selected="selected"</c:if>>Disable</option>
			</select>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">ILETS/OPIC</span>
			<select class="form-control" name="ilets">
				<option <c:if test="${ 'true' eq teacher.ilets}">selected="selected"</c:if>>Able</option>
				<option <c:if test="${ 'false' eq teacher.ilets}">selected="selected"</c:if>>Disable</option>
			</select>
		</div>
	</div>
	<div class="form-group">
		<label>
			<h3 style="display: inline;">Make Your Profile</h3><a href="./profile_how_to_make.html" target="_blank"><h6 style="display: inline;">(How To Make)</h6></a>
		</label>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Profile Picture*</span>
			<div class="form-control" style="height:100%; overflow: hidden;">
				<div style="float:left; width:90%">
					<input type="file" accept="image/*" name="primaryProfile" data-raw="${teacher.primaryProfilePicture }" data-init="${teacher.picture.fileName }">
				</div>
				<a href="../${teacher.primaryProfilePicture }" target="_blank" class="btn btn-info" style="float:left; width: 10%; color: white;">View</a>
			</div>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Profile Voice*</span>
			<div class="form-control" style="height:100%;">
				<div style="float:left; width:75%">
					<input type="file" accept="audio/*" name="primaryVoice" data-raw="${teacher.primaryVoice }" data-init="${teacher.voice.fileName}">
				</div>
				<c:if test="${teacher.voice.fileName eq '' }"><span class="center_text">No Audio File</span></c:if>
				<c:if test="${teacher.voice.fileName ne '' }">
					<audio src="../${teacher.primaryVoice }" preload="auto" controls></audio>
				</c:if>
				
			</div>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Competency*</span>
			<textarea rows="5" class="form-control" name="competency">${teacher.competency }</textarea>
			<a id="previewButton" href="#" style="width: 100px; text-decoration: none; cursor: pointer; background-color: rgb(91,192,222); color: white; font-size: 14px;" class="input-group-addon">Preview</a>
		</div>
	</div>
	<div class="form-group">
		<label>
			<h3>Teaching Environment</h3>
		</label>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">PC Spec <a href="#">(Help)</a></span>
			<div class="form-control" style="height:100%; overflow: hidden;">
				<div style="float:left; width:90%">
					<input type="file" accept="image/*" name="specImage" data-init="${teacher.spec.fileName }">
				</div>
				<a href="../${teacher.specImage }" target="_blank" class="btn btn-info" style="float:left; width: 10%; color: white;">View</a>
			</div>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Planed Speed</span>
			<select class="form-control" name="internetSpeed">
				<option <c:if test="${ '2M' eq teacher.internetSpeed}">selected</c:if>>2M</option>
				<option <c:if test="${ '3M' eq teacher.internetSpeed}">selected</c:if>>3M</option>
				<option <c:if test="${ '5M' eq teacher.internetSpeed}">selected</c:if>>5M</option>
				<option <c:if test="${ 'More' eq teacher.internetSpeed}">selected</c:if>>More</option>
			</select>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Internet Provider</span>
			<input type="text" class="form-control" name="internetProvider" value="${teacher.internetProvider }"placeholder="Internet Proivder">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Internet Type</span>
			<select class="form-control" name="internetType">
				<option <c:if test="${ 'DSL' eq teacher.internetType}">selected</c:if>>DSL</option>
				<option <c:if test="${ 'Cable' eq teacher.internetType}">selected</c:if>>Cable</option>
			</select>
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Upload Speed</span>
			<input type="text" class="form-control" name="upSpeed" value="${teacher.upSpeed }"
				placeholder="Upload Speed at P.M. 8:00">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Download Speed</span>
			<input type="text" class="form-control" name="downSpeed" value="${teacher.downSpeed }"
				placeholder="Download Speed at P.M. 8:00">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">OS System</span>
			<input type="text" class="form-control" name="os" value="${teacher.os }"
				placeholder="OS Info">
		</div>
	</div>
	
	<div class="form-group">
		<label>
			<h3>Paypal Info</h3>
		</label>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Paypal Account</span>
			<input type="text" class="form-control" name="bankName" value="${teacher.bankName }" placeholder="Payment will be sent thru Paypal ">
		</div>
		<div style="width: 100%" class="input-group">
			<span style="width: 150px" class="input-group-addon">Bank Account</span>
			<input type="text" class="form-control" name="bankAccount" value="${teacher.bankAccount }" placeholder="Ignore this filed">
		</div>
	</div>
</div>

<div id="light" class="white_content">

</div>
<div id="fade" class="black_overlay">
</div>
<script>


$(":file").filestyle();

$('.bootstrap-filestyle').each(function(index, value){
	var parent = $(this).parent();
	var file = $(':file', parent);
	var init = file.attr('data-init');
	var display = $('input', $(this));
	display.val(init);
})
</script>
