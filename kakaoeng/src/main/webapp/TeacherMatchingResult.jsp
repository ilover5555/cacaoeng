<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
.scheduleCell .iradio_minimal-grey{
	left : 10px;
	top : 6px;
}
</style>
<style>
	#resultPanel .audioplayer{
		z-index : 100;
		width:100%;
		margin: 0 0 ;
	}
	#resultPanel .audioplayer{
		width: 100%;
	}
	#resultPanel .audioplayer-time-current{
		display: none;
	}
	#resultPanel .audioplayer-bar{
		left:2.5em;
	}
	#resultPanel .audioplayer-playpause{
		z-index: 10 !important;
	}
	#resultPanel .audioplayer-time{
		width:2.875em !important;
	}
</style>
<link rel="stylesheet" href="css/audioplayer.css" />
<script src="js/audioplayer.js"></script>
<jsp:useBean id="exactMap" scope="request" class="java.util.TreeMap"></jsp:useBean>
<jsp:useBean id="similarMap" scope="request" class="java.util.TreeMap"></jsp:useBean>
<jsp:useBean id="realStartDate" scope="request" class="java.lang.String"></jsp:useBean>
<jsp:useBean id="realEndDate" scope="request" class="java.lang.String"></jsp:useBean>
	<div id="resultPanel" style="display :inline-block; ;width:520; height : auto; background: black;">
		<div style="margin: 7px 15px;">
		<input id="startDateHolder" type="hidden" value="${realStartDate}">
		<input id="endDateHolder" type="hidden" value="${realEndDate}">
		<c:if test="${ exactMap.size() > 0}">
			<div style="background-image: url(artifact/match_exact_label.png); width:480px; height:38px">
				<p class="center_text" style="margin-left:20px; color: white; font-weight: bold;"> 요청 시간과 일치하는 선생님 ${exactMap.size() }분</p>
			</div>
		<c:forEach items="${exactMap }" var="element">
			<div style="overflow:hidden ;margin-top:7px; width:480px; height : 235px; border-top: 4px #a70920 solid; background-color: white;">
				<div style="float:left; width:135px; height:100%; padding-left: 23px; padding-top: 19px;">
					<div>
						<img style="width:107px; height:113px;" src="${element.key.teacher.primaryProfilePicture }">
					</div>
					<div style="margin-top:7px; margin-bottom: 10px; width: 107px;">
						<c:if test="${(element.key.teacher.voice.fileName eq '') or (element.key.teacher.voice.fileName eq null) }"><span class="center_text">No Audio File</span></c:if>
						<c:if test="${element.key.teacher.voice.fileName ne '' }">
							<audio style="width:100%" src="${element.key.teacher.primaryVoice }" preload="auto" controls></audio>
						</c:if>
					</div>
					<div>
						<div class="teacher-select-button" data-id="${element.key.teacher.id }" data-name="${element.key.teacher.className }" data-sort="similar"></div>
					</div>
				</div>
				<div style="float:left; width:335px; height:100%; padding-top: 19px; padding-left: 10px;">
					<div style="">
						<h6 id="name-${element.key.teacher.id }" style="margin:0; padding:0; font-weight: bold;font-size: 25px;">${element.key.teacher.className }</h6>
					</div>
					<div style="margin-top:7px;">
						<c:forEach items="${element.key.teacher.upperList }" var="upper">
							<span style="display:block; height:16px; line-height: 15px; font-size: 15px;">${upper}</span>
						</c:forEach>
						
						<div style="margin-top:20px; margin-left: 10px;">
							<h6 style="font-size: 18px; color: white; background-color: rgb(168,9,33); display: inline;">&nbsp;&nbsp;${element.key.teacher.shortClassName } 선생님은...</h6>
							<ul style="margin-left:25px; margin-top: 9px;">
								<c:forEach items="${element.key.teacher.lowerList }" var="lower">
									<li style="height:17px; line-height: 16px; font-size: 16px;"><img class="align_vertical_center" style="width:15px;height:15px" src="artifact/list-style.png"> ${lower}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
			
			<div data-id="schedule-${element.key.teacher.id }" style="width:480px; height:125px; background-color: white; overflow: hidden; display : none;">
				<c:forEach items="${element.value }" var="setElement">
					<c:set var="request" value="${setElement.key }"></c:set>
					<c:set var="response" value="${setElement.value }"></c:set>
					<c:set var="length" value="0"></c:set>
					
					<div class="scheduleCell" data-day="${request.dayOfWeek }" data-duration="${request.duration.duration }" style="width:${480/element.value.size()}px; height:100%; border-right: 1px #505050 solid; float:left">
						<div style="width:100%; height:30px; background-color: #BE0606;">
							<p class="center_text" style="text-align: center;  color: white; font-weight: bold">${request.dayOfWeekKorean}</p>
						</div>
						<div style="width:100%; height:65px; ">
							<c:forEach var="time" items="${response }">
								<label style="text-align: center;  <c:if test="${response.size() eq 1 }">checked </c:if>">
									<c:if test="${response.size() ne 1 }"><input data-hour="${time.startHour }" data-minute="${time.startMinute }" class="choice-similar choice-similar-check" type="radio" data-name="choice-${element.key.teacher.id }-${request.dayOfWeek}"></c:if>
									<c:if test="${response.size() eq 1 }"><input data-hour="${time.startHour }" data-minute="${time.startMinute }" class="choice-similar-check" type="radio" data-name="choice-${element.key.teacher.id }-${request.dayOfWeek}" style="display:none;" checked="checked"></c:if>
									<c:if test="${response.size() ne 1 }"><span class="center_text align_vertical_center">&nbsp;${time.timeString }</span></c:if>
									<c:if test="${response.size() eq 1 }"><p class="align_vertical_center">&nbsp;${time.timeString }</p></c:if>
								</label>
							</c:forEach>
						</div>
						<div style="width:100%; height:30px; background-color: #E1E1E1"></div>
					</div>
				</c:forEach>
			</div>
			
			<div style="width: 100%; height:13px"></div>
			</c:forEach>
			</c:if>
			
			<c:if test="${ similarMap.size() > 0}">
			<div style="background-image: url(artifact/match_similar_label.png); width:480px; height:38px">
				<p class="center_text" style="margin-left:20px; color: white; font-weight: bold;"> 요청 시간과 유사한 선생님 ${similarMap.size() }분</p>
			</div>
			<c:forEach items="${similarMap }" var="element">
			<div style="overflow:hidden ;margin-top:7px; width:480px; height : 235px; border-top: 4px #a70920 solid; background-color: white;">
				<div style="float:left; width:135px; height:100%; padding-left: 23px; padding-top: 19px;">
					<div>
						<img style="width:107px; height:113px;" src="${element.key.teacher.primaryProfilePicture }">
					</div>
					<div style="margin-top:7px; margin-bottom: 10px; width: 107px;">
						<c:if test="${(element.key.teacher.voice.fileName eq '') or (element.key.teacher.voice.fileName eq null) }"><span class="center_text">No Audio File</span></c:if>
						<c:if test="${element.key.teacher.voice.fileName ne '' }">
							<audio style="width:100%" src="${element.key.teacher.primaryVoice }" preload="auto" controls></audio>
						</c:if>
					</div>
					<div>
						<div class="teacher-select-button" data-id="${element.key.teacher.id }" data-name="${element.key.teacher.className }" data-sort="similar"></div>
					</div>
				</div>
				<div style="float:left; width:335px; height:100%; padding-top: 19px; padding-left: 10px;">
					<div style="">
						<h6 id="name-${element.key.teacher.id }" style="margin:0; padding:0; font-weight: bold;font-size: 25px;">${element.key.teacher.className }</h6>
					</div>
					<div style="margin-top:7px;">
						<c:forEach items="${element.key.teacher.upperList }" var="upper">
							<span style="display:block; height:17px; line-height: 16px; font-size: 16px;">${upper}</span>
						</c:forEach>
						
						<div style="margin-top:25px; margin-left: 10px;">
							<h6 style="font-size: 18px; color: white; background-color: rgb(168,9,33); display: inline;">&nbsp;&nbsp;${element.key.teacher.shortClassName } 선생님은...</h6>
							<ul style="margin-left:25px; margin-top: 5px;">
								<c:forEach items="${element.key.teacher.lowerList }" var="lower">
									<li style="height:17px; line-height: 16px; font-size: 16px;"><img class="align_vertical_center" style="width:15px;height:15px" src="artifact/list-style.png"> ${lower}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
				
			</div>
			
			<div style="margin-top : 4px; margin-bottom : 4px; "></div>
			
			<div data-id="schedule-${element.key.teacher.id }" style="width:480px; height:125px; background-color: white; overflow: hidden">
				<c:forEach items="${element.value }" var="setElement">
					<c:set var="request" value="${setElement.key }"></c:set>
					<c:set var="response" value="${setElement.value }"></c:set>
					<c:set var="length" value="0"></c:set>
					
					<div class="scheduleCell" data-day="${request.dayOfWeek }" data-duration="${request.duration.duration }" style="width:${480/element.value.size()}px; height:100%; border-right: 1px #505050 solid; float:left">
						<div style="width:100%; height:30px; background-color: #BE0606;">
							<p class="center_text" style="text-align: center;  color: white; font-weight: bold">${request.dayOfWeekKorean}</p>
						</div>
						<div style="width:100%; height:65px; ">
						<div style="<c:if test="${request.exact eq false }">background-color : rgb(252,255,206);</c:if>; height:100%;">
							<c:forEach var="time" items="${response }">
									<label style="text-align: center; height:30px;  <c:if test="${response.size() eq 1 }">checked </c:if>">
										<c:if test="${response.size() ne 1 }"><input data-hour="${time.startHour }" data-minute="${time.startMinute }" class="choice-similar choice-similar-check" type="radio" name="choice-${element.key.teacher.id }-${request.dayOfWeek}-${request.startHour}-${request.startMinute}" data-name="choice-${element.key.teacher.id }-${request.dayOfWeek}"></c:if>
										<c:if test="${response.size() eq 1 }"><input data-hour="${time.startHour }" data-minute="${time.startMinute }" class="choice-similar-check" type="radio" name="choice-${element.key.teacher.id }-${request.dayOfWeek}-${request.startHour}-${request.startMinute}" data-name="choice-${element.key.teacher.id }-${request.dayOfWeek}" style="display:none;" checked="checked"></c:if>
										<c:if test="${response.size() ne 1 }"><span class="center_text">&nbsp;${time.timeString }</span></c:if>
										<c:if test="${response.size() eq 1 }"><span class="center_text">&nbsp;${time.timeString }</span></c:if>
									</label>
							</c:forEach>
						</div>
						</div>
						<div style="width:100%; height:30px; background-color: <c:if test="${request.exact eq true }">#E1E1E1;</c:if><c:if test="${request.exact eq false }">rgb(222,225,182);</c:if>">
							<p class="center_text" style="text-align: center;">${request.duration.duration*25 }분 수업 </p>
						</div>
					</div>
				</c:forEach>
			</div>
			</c:forEach>
			</c:if>
		</div>
	</div>