
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
ul li{
	list-style: none;
}
</style>
<style>
	#teacherCard .audioplayer{
		z-index : 100;
		width:100%;
		margin: 0 0 !important;
	}
	#teacherCard .audioplayer{
		width: 100%;
	}
	#teacherCard .audioplayer-time-current{
		display: none;
	}
	#teacherCard .audioplayer-bar{
		left:2.5em;
	}
	#teacherCard .audioplayer-playpause{
		z-index: 10 !important;
	}
	#teacherCard .audioplayer-time{
		width:2.875em !important;
	}
</style>

<jsp:useBean id="repList" scope="request" class="java.util.ArrayList"></jsp:useBean>
		<c:forEach items="${repList }" var="teacher">
			<div id="teacherCard" style="overflow:hidden ; width:447px; height : 235px; border-top: 4px #a70920 solid; background-color: white; float:left; border-left: 1px #c0c0c0 solid; border-right: 1px #c0c0c0 solid; border-bottom: 1px #c0c0c0 solid; box-shadow: 10px 10px 18px 0px rgba(192,192,192,1); margin-left : 0px; margin-right: 20px; margin-bottom: 18px;">
				<div style="float:left; width:140px; height:100%; padding-left: 21px; padding-top: 19px;">
					<div>
						<img class="profilePicture" style="width:107px; height:113px;" src="../${teacher.primaryProfilePicture }">
					</div>
					<div id="audioWrapper" style="margin-top : 5px; margin-bottom: 10px; height: 30px; width:107px;">
						<c:if test="${teacher.voice.fileName eq '' }"><span class="center_text">No Audio File</span></c:if>
						<c:if test="${teacher.voice.fileName ne '' }">
							<audio class="profileVoice" style="width:100%" autobuffer="autobuffer" controls>
								<source src="../${teacher.primaryVoice }"/>
							</audio>
						</c:if>
					</div>
					<div>
						<div class="view-teacher-schedule" style="width:107px; height:32px; background-image: url('../artifact/button_background.png')" data-id="${teacher.id }">
							<p style="color: white; font-size: 15px; line-height: 32px; text-align: center;">일정보기</p>
						</div>
					</div>
				</div>
				<div style="float:left; width:305px; height:100%; padding-top: 19px;">
					<div style="">
						<h4 id="name-${teacher.id }" style="margin:0; padding:0; font-weight: normal; color: black; font-size: 23px;">${teacher.className }</h4>
					</div>
					<div style="margin-top:5px;">
						<c:forEach items="${teacher.upperList }" var="upper">
							<span style="display:block; height: 16px; line-height: 15px; font-size: 15px;">${upper}</span>
						</c:forEach>
									
						<div style="margin-top:25px; margin-left: 15px;">
							<h6 style="font-size: 16px; color: white; background-color: rgb(168,9,33); display: inline; font-weight: bolder;">&nbsp;&nbsp;${teacher.shortClassName } 선생님은...&nbsp;&nbsp;</h6>
							<ul style="margin-top : 7px; ;margin-left:25px;">
								<c:forEach items="${teacher.lowerList }" var="lower">
									<li style="height: 17px; line-height: 16px; font-size: 16px;"><img class="align_vertical_center" style="width:15px;height:15px" src="../artifact/list-style.png"> ${lower}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>