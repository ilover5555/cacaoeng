<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
     <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <jsp:useBean id="bookList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="course" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Insert title here</title>
<link href="../minimal/grey.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="../css/match.css" />
<link rel="stylesheet" type="text/css" href="../css/popup.css" />
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script src="../js/scheduleCell.js"></script>
<script src="../icheck.js"></script>
<script type="text/javascript" src="../js/wSelect.min.js"></script>
<link rel="stylesheet" type="text/css" href="../wSelect.css" />
<script type="text/javascript" src="../js/pace.js"></script>
<link rel="stylesheet" type="text/css" href="../css/pace-theme-bounce.css" />
<style type="text/css">
#date-holder:not(hover){
	border:solid #CCC 1px ;
	border-radius:5px;
	-webkit-border-radius:5px;
	-moz-border-radius:5px;
	display: inline-block;
	margin-left: 30px;
	background-color: #FAFAFA;
	position: relative;
	
}

#date-holder.hover {
	border-color: rgb(82, 168, 236);
	border-color: rgba(82, 168, 236, 0.8);
	-moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px
		rgba(82, 168, 236, 0.6);
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px
		rgba(82, 168, 236, 0.6);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px
		rgba(82, 168, 236, 0.6);
}

#date-holder.hover .arrow{
	background-position: -30px center;
	color : rgb(82, 168, 236);
}
.arrow{
	
	position: absolute;
	width: 25px;
	right: 0px;
	top: 0px;
	height: 100%;
	background-repeat: no-repeat;
	background-position: 0px center;
}

#classStartDate{
	width:122px;
	height:30px;
	float:left;
	background-color: #FAFAFA;
	border: none;
	outline-color: transparent;
	border-radius : 5px;
	padding: 3px;
}

.ui-widget-content, .ui-datepicker{
	font-size: 12px;
}
</style>
<script>
	function toggleHandle(target){
		var selected_target = target+'_selected';
		var unselected_target = target+'_unselected';
		var result = {
			enableHandle : function (){
				$('.'+unselected_target).click(function(){
					if($(this).hasClass(selected_target))
						return;
					var selected = $('.'+selected_target);
					selected.toggleClass(selected_target);
					selected.toggleClass(unselected_target);
					$(this).toggleClass(selected_target);
					$(this).toggleClass(unselected_target);
				});
				$('.'+selected_target).click(function(){
					if($(this).hasClass(selected_target))
						return;
					var selected = $('.'+selected_target);
					selected.toggleClass(selected_target);
					selected.toggleClass(unselected_target);
					$(this).toggleClass(selected_target);
					$(this).toggleClass(unselected_target);
				});
			},
			
			getSelectedId : function(){
				var selected = $('.'+selected_target);
				return selected.attr('id');
			}
		}
		return result;
	}
	
	var classSortMenuHandler = null;
	var monthMenuHandler = null;
	var periodMenuHandler = null;
	var classDurationHandler = null;
	
	var selectedResult ={};
	
	
	
	function dialogInit(){
		common.init();
		$('.choice-similar').iCheck({
			checkboxClass:'icheckbox_minimal-grey',
			radioClass:'iradio_minimal-grey',
			increaseArea:'50%'
		});
		$('.teacher-select-button').click(function(){
			
			var id = $(this).attr('data-id');
			var name = $(this).attr('data-name');
			var baseDate = $('#classStartDate').val();;
			selectedResult["baseDate"] = baseDate;
			selectedResult["id"] = id;
			selectedResult["sort"] = classSortMenuHandler.getSelectedId().slice(5);
			var count = 0;
			var valid = true;
			$('div[data-id="schedule-' + id+ '"] .scheduleCell').each(function(){
				var row = {};
				var target = $(this);
				var dayOfWeek = target.attr('data-day');
				var duration = Number(target.attr('data-duration'));
				var checked = $('.choice-similar-check[data-name="choice-' + id + '-' + dayOfWeek + '"]:checked', target);
				if(checked.length == 0){
					alert('시간을 선택하세요');
					valid =false;
					return;
				}
				var hour = Number(checked.attr('data-hour'));
				var minute = Number(checked.attr('data-minute'));
				
				row["dayOfWeek"] = dayOfWeek;
				row["hour"] = hour;
				row["minute"] = minute;
				row["duration"] = duration;
				row["weeks"] = Number($('#'+monthMenuHandler.getSelectedId()).attr('data-duration'));
				selectedResult["index"+count] = JSON.stringify(row);
				count++;
			});
			selectedResult["length"] = count;
			if(valid == false){
				selectedResult = {};
				return;
			}
			selectedResult["matchSort"] = "fixed";
			$.ajax({
			url : makeUrl("../calculateTuition.do"),
			method : "POST",
			data : selectedResult,
			dataType : "text",
			success:function(msg){
				var result = JSON.parse(msg);
				$('#finalPrice').html(scheduleCell.numberWithCommas(result["finalPrice"]));
				$('#typeDiscountName').html(result["typeDiscountName"]);
				$('#typeDiscountPercent').html(result["typeDiscountPercent"]);
				$('#monthDiscountPercent').html(result["monthDiscountPercent"]);
				$('#purchasePrice').html(scheduleCell.numberWithCommas(result["purchasePrice"]));
				$('#pricePanel').css('display', 'block');
				
				var book = $('#bookSelect option:selected').val();
				if(book == 'Not yet determined')
					book = "선생님과 상담 후 결정합니다";
				var month = $('#'+monthMenuHandler.getSelectedId()).attr('data-month');
				var description = $('#'+periodMenuHandler.getSelectedId()).attr('data-descript');
				var min = $('#'+classDurationHandler.getSelectedId()).attr('data-min');
				$('#bookPanel').html(book);
				$('#monthPanel').html(month);
				$('#timesPanel').html(description);
				$('#minutePanel').html(min);
				$('#greenPanel').css('display', 'block');
				
				scheduleCell.hideBox();
			},
			error: function(request,status,error){
				alert(request.responseText);
			}
		});
			$('#selectedTeacherId').val(name+' 선생님을 선택하셨습니다.');
		})
	}
	
	$(document).ready(function(){
		
		$('.period').click(function(){
			var period = $(this).attr('data-period');
			var top = $(window).scrollTop();
			window.location = './match.view?sort=${param.sort}&course=${param.course }&title=${param.title }&month=${param.month }&period='+period+'&duration=${param.duration }&top='+top; 
			return false;
		});
		$('.month').click(function(){
			var month = $(this).attr('data-month');
			var top = $(window).scrollTop();
			window.location = './match.view?sort=${param.sort}&course=${param.course }&title=${param.title }&month='+month+'&period=${param.period }&duration=${param.duration }&top='+top; 
			return false;
		});
		
		$("#showBookButton").click(function(){
			var selected = 'book'+$('option:selected', $('#bookSelect')).attr('data-id');
			var win = window.open('./books.view#'+selected, '_blank');
			if(win){
			    //Browser has allowed it to be opened
			    win.focus();
			}else{
			    //Broswer has blocked it
			    alert('Please allow popups for this site');
			}
		});
		
		$("#purchase").click(function(){
			selectedResult["book"] = $('#bookSelect > option:checked').val();
			selectedResult["method"] = $('[name="payMethod"]:checked').val();
			Pace.track(function(){
				$.ajax({
					url : makeUrl("../book.do"),
					method : "POST",
					data : selectedResult,
					dataType : "text",
					success:function(msg){
						alert(msg);
					},
					error: function(request,status,error){
						alert(request.responseText);
					}
				});
			});
			
		})
		
		
		$("#classStartDate").datepicker({
			dateFormat : 'yy-mm-dd',
			minDate : +1,
		});
		$('#classStartDate').datepicker('setDate', '+1');
		
		$('.pay-method').iCheck({
			checkboxClass:'icheckbox_minimal-grey',
			radioClass:'iradio_minimal-grey',
			increaseArea:'50%'
		});
		
		$('#showTeacher').click(function(){
			var matchSort = "fixed";
			if($('#classStartDate').val() == ''){
				alert('날짜를 입력해 주세요');
				return;
			}
			var baseDate = $('#classStartDate').val();
			var hour = $('#hour_select').val();
			var minute = $('#minute_select').val();
			var classDuration = classDurationHandler.getSelectedId();
			var monthDuration = monthMenuHandler.getSelectedId();
			var period = periodMenuHandler.getSelectedId();
			
			
			var sendData = {
				"matchSort" : matchSort,
				"baseDate" : baseDate,
				"hour" : hour,
				"minute" : minute,
				"classDuration" : classDuration,
				"monthDuration" : monthDuration,
				"period" : period
			}
			
			var scrollY = $(window).scrollTop();
			$.ajax({
				url : makeUrl("./match.do"),
				method : "POST",
				data : sendData,
				dataType : "html",
				success:function(msg){
					$('#light').html(msg);
					scheduleCell.showBox(scrollY, common.init, false, true);
					dialogInit();
					$('#light audio').audioPlayer();
				},
				error: function(request,status,error){
					alert(request.responseText);
				}
			});
		});

		$('.dataHolder').hover(function(){
			$('#date-holder').toggleClass('hover');
		}, function(){
			$('#date-holder').toggleClass('hover');
		})
		
		classSortMenuHandler = toggleHandle('class_sort_menu');
		monthMenuHandler = toggleHandle('month_menu');
		periodMenuHandler = toggleHandle('period_menu');
		classDurationHandler = toggleHandle('class_duration');
	});
</script>
</head>
<body style="background-color: white" onload="$(window).scrollTop('${param.top}');">
	<div id="wrap">
		<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
		<div style="overflow: hidden; margin-left: 33px; margin-right: 34px; margin-top: 78px; margin-bottom: 27px;">
			<div id="matchFixed" class="match_sort_menu_selected"></div>
			<div id="matchSmart" class="match_sort_menu_unselected" onclick="location.href = './match.view?sort=SmartMatch&course=${param.course}&title=${param.title }&month=${param.month }&pereiod=${param.period }&duration=${param.duration }'; return false;"></div>
			<div id="matchTeacher" class="match_sort_menu_unselected" onclick="location.href = './match.view?sort=TeacherMatch&course=${param.course}&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"></div>
		</div>
		
		<img src="../artifact/match_sort_help.png" style="display : block; margin : 0 auto;"/>
		
		<div style="margin-top: 66px; margin-left: 33px; margin-right: 33px; margin-bottom: 64px; ">
			<img src="../artifact/fixed_step1.png" style="margin-bottom: 8px"/>
			<div style="overflow: hidden; border-top: 2px #c0c0c0 solid">
				<div id="classTypeEasy" 
					<c:if test="${course.classType eq 'classTypeEasy' }">class="class_sort_menu_selected"</c:if> 
					<c:if test="${course.classType ne 'classTypeEasy' }">class="class_sort_menu_unselected"</c:if> 
					onclick="window.location = './match.view?sort=${param.sort}&course=TypeEasy&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"
					></div>
				<div id="classTypeMiddle" 
					<c:if test="${course.classType eq 'classTypeMiddle' }">class="class_sort_menu_selected"</c:if> 
					<c:if test="${course.classType ne 'classTypeMiddle' }">class="class_sort_menu_unselected"</c:if> 
					onclick="window.location = './match.view?sort=${param.sort}&course=TypeMiddle&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"
					></div>
				<div id="classTypeFreeTalk" 
					<c:if test="${course.classType eq 'classTypeFreeTalk' }">class="class_sort_menu_selected"</c:if> 
					<c:if test="${course.classType ne 'classTypeFreeTalk' }">class="class_sort_menu_unselected"</c:if> 
					onclick="window.location = './match.view?sort=${param.sort}&course=TypeFreeTalk&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"
					></div>
				<div id="classTypeElementBook" 
					<c:if test="${course.classType eq 'classTypeElementBook' }">class="class_sort_menu_selected"</c:if> 
					<c:if test="${course.classType ne 'classTypeElementBook' }">class="class_sort_menu_unselected"</c:if> 
					onclick="window.location = './match.view?sort=${param.sort}&course=TypeElementBook&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"
					></div>
				<div id="classTypeBussiness"
					<c:if test="${course.classType eq 'classTypeBussiness' }">class="class_sort_menu_selected"</c:if> 
					<c:if test="${course.classType ne 'classTypeBussiness' }">class="class_sort_menu_unselected"</c:if> 
					onclick="window.location = './match.view?sort=${param.sort}&course=TypeBussiness&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"
					></div>
				<div id="classTypeExam"
					<c:if test="${course.classType eq 'classTypeExam' }">class="class_sort_menu_selected"</c:if> 
					<c:if test="${course.classType ne 'classTypeExam' }">class="class_sort_menu_unselected"</c:if> 
					onclick="window.location = './match.view?sort=${param.sort}&course=TypeExam&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"
					></div>
			</div>
			<div style="height:57px;">
				<div style="float : left; width : 235px; height : 57px; background-image: url(../artifact/label_select_book.png)"></div>
				<div style="float : left; width : 699px; height : 57px; border: 1px #c0c0c0 solid">
					<select id="bookSelect" class="wSelect-el">
						<option value="Not yet determined" data-id="s">선생님과 상담 후 결정합니다</option>
						<c:forEach items="${bookList }" var="book">
							<option value="${book.title }" data-id="${book.id }" <c:if test="${book.title eq param.title }">selected="selected"</c:if>>${book.title }</option>
						</c:forEach>
					</select>
					<img id="showBookButton" style="cursor:pointer;" src="../artifact/show_book_button.png">
				</div> 
			</div>
		</div>
		
		<div style="margin-left: 33px; margin-right: 33px; margin-bottom: 64px;">
			<img src="../artifact/fixed_step2.png" style="margin-bottom: 8px"/>
			<div style="border-top: 2px #c0c0c0 solid">
				<div
					style="float: left; width: 235px; height: 56px; background-image: url(../artifact/label_start_date.png)"></div>
				<div
					style="float: left; width: 699px; height: 56px; border: 1px #c0c0c0 solid">
					<div style="height: 55px; ">
						<div id="date-holder" class="align_vertical_center dataHolder" style="overflow: hidden; float: left;">
							<input type="text" id="classStartDate"  style="float: left;">
							<div class="arrow" onclick='$("#classStartDate").datepicker("show"); return false;' style="float: left;">
								<span class="glyphicon glyphicon-calendar align_vertical_center" style="font-size: 20px; display: block; margin-top: 1px;"></span>
							</div>
						</div>
						<select id="hour_select" class="wSelect-el">
							<option value="06">6시</option>
							<option value="07">7시</option>
							<option value="08">8시</option>
							<option value="09">9시</option>
							<option value="10">10시</option>
							<option value="11">11시</option>
							<option value="12">12시</option>
							<option value="13">13시</option>
							<option value="14">14시</option>
							<option value="15">15시</option>
							<option value="16">16시</option>
							<option value="17">17시</option>
							<option value="18">18시</option>
							<option value="19">19시</option>
							<option value="20">20시</option>
							<option value="21">21시</option>
							<option value="22">22시</option>
							<option value="23">23시</option>
							<option value="24">24시</option>
						</select>
						<select id="minute_select" class="wSelect-el">
							<option value="00">00분</option>
							<option value="30">30분</option>
						</select>
					</div>
				</div>

			</div>
			<div style="width : 100%; overflow: hidden;">
				<div id="1month" data-month="1" data-duration="4" 
				<c:if test="${param.month eq 1 }">class="month_menu_selected month" </c:if>
				<c:if test="${param.month ne 1 }">class="month_menu_unselected month" </c:if>
				></div>
				<div id="3month" data-month="3" data-duration="12" 
				<c:if test="${param.month eq 3 }">class="month_menu_selected month" </c:if>
				<c:if test="${param.month ne 3 }">class="month_menu_unselected month" </c:if>
				></div>
				<div id="6month" data-month="6" data-duration="24" 
				<c:if test="${param.month eq 6 }">class="month_menu_selected month" </c:if>
				<c:if test="${param.month ne 6 }">class="month_menu_unselected month" </c:if>
				></div>
				<div id="12month" data-month="12" data-duration="48" 
				<c:if test="${param.month eq 12 }">class="month_menu_selected month" </c:if>
				<c:if test="${param.month ne 12 }">class="month_menu_unselected month" </c:if>
				></div>
			</div>
			<div style="width : 100%; overflow: hidden;">
				<div id="twice" data-descript="주2회(화,목)" data-period="2"
				<c:if test="${param.period eq 2 }">class="period_menu_selected period" </c:if>
				<c:if test="${param.period ne 2 }">class="period_menu_unselected period" </c:if>
				></div>
				<div id="thrice" data-descript="주3회(월,수,금)"  data-period="3"
				<c:if test="${param.period eq 3 }">class="period_menu_selected period" </c:if>
				<c:if test="${param.period ne 3 }">class="period_menu_unselected period" </c:if>
				></div>
				<div id="fiveTimes" data-descript="주5회(월,화,수,목,금)" data-period="5"
				<c:if test="${param.period eq 5 }">class="period_menu_selected period" </c:if>
				<c:if test="${param.period ne 5 }">class="period_menu_unselected period" </c:if>
				></div>
			</div>
			<div style="width : 100%; overflow: hidden;">
				<div style="float: left;width: 235px; height:40px; border: 1px #c0c0c0 solid"><p style="text-align: center;" class="center_text">25분</p></div>
				<div style="float: left;width: 464px; height:40px; border: 1px #c0c0c0 solid"><p id="halfPrice" style="text-align: center;" class="center_text"><fmt:formatNumber value="${halfPrice}" pattern="#,###"/>원</p></div>
				<div style="float: left;width: 235px; height:40px; border: 1px #c0c0c0 solid">
					<div id="halfTypeSelect" data-min="25"
					<c:if test="${param.duration eq 1 }">class="align_vertical_center class_duration_selected" </c:if>
					<c:if test="${param.duration ne 1 }">class="align_vertical_center class_duration_unselected" </c:if>
					onclick="window.location = './match.view?sort=${param.sort}&course=${param.course }&title=${param.title }&month=${param.month }&period=${param.period }&duration=1'; return false;" 
					></div>
				</div>
			</div>
			<div style="width : 100%; overflow: hidden;">
				<div style="float: left;width: 235px; height:40px; border: 1px #c0c0c0 solid"><p style="text-align: center;" class="center_text">50분</p></div>
				<div style="float: left;width: 464px; height:40px; border: 1px #c0c0c0 solid"><p id="fullPrice" style="text-align: center;" class="center_text"><fmt:formatNumber value="${fullPrice}" pattern="#,###"/>원</p></div>
				<div style="float: left;width: 235px; height:40px; border: 1px #c0c0c0 solid">
					<div id="fullTypeSelect" data-min="50"
					<c:if test="${param.duration eq 2 }">class="align_vertical_center class_duration_selected" </c:if>
					<c:if test="${param.duration ne 2 }">class="align_vertical_center class_duration_unselected" </c:if>
					onclick="window.location = './match.view?sort=${param.sort}&course=${param.course }&title=${param.title }&month=${param.month }&period=${param.period }&duration=2'; return false;"
					></div>
				</div>
			</div>
		</div>
		
		<div style="margin-left: 33px; margin-right: 33px; margin-bottom: 69px; ">
			<img src="../artifact/fixed_step3.png" style="margin-bottom: 8px"/>
			<div style="width:100%; overflow: hidden; border-top: 2px #c0c0c0 solid">
				<div style="float : left; width : 235px; height : 57px; background-image: url(../artifact/label_teacher_select.png)"></div>
				<div style="float : left; width : 699px; height : 57px; border: 1px #c0c0c0 solid">
					<input id="selectedTeacherId" class="align_vertical_center" style="width:475px; height:22px; margin-left :30px; margin-right: 30px; border: 1px #c0c0c0 solid" type=text/>
					<img id="showTeacher" style="cursor:pointer" class="align_vertical_center" src="../artifact/show_teacher_button.png">
				</div> 
			</div>
		</div>
		<div style="margin-left: 33px; margin-right: 33px; margin-bottom: 35px; ">
			<div>
				<img src="../artifact/fixed_step4.png" style="margin-bottom: 8px; float: left;"/>
				<p id="greenPanel" style="background-color: rgb(112,173,71); font-size: 15px; color: white; padding: 1px; margin-bottom: 8px; margin-top: -1px; float: right; display: none;">신청내용 : <span id="bookPanel"></span> / <span id="monthPanel"></span>개월 / <span id="timesPanel"></span> / <span id="minutePanel"></span>분</p>
			</div>
			<div style="width:100%; overflow: hidden; border-top: 2px #c0c0c0 solid">
				<div style="float : left; width : 235px; height : 57px; background-image: url(../artifact/label_final_price.png)"></div>
				<div style="float : left; width : 699px; height : 57px; border: 1px #c0c0c0 solid">
					<div id="pricePanel" class="align_vertical_center" style="display: none; padding-left: 15px; font-size: 18px;">
						<span>수강료:<span id="finalPrice"></span>원 - <span id="typeDiscountName"></span><span>(</span><span id="typeDiscountPercent"></span>) - <span>기간할인</span>(<span id="monthDiscountPercent"></span>) = 최종결재금액 <sapn id="purchasePrice"></sapn>원</span>
					</div>
				</div> 
				<div style="float : left; width : 235px; height : 57px; background-image: url(../artifact/label_select_purchase_method.png)"></div>
				<div style="float : left; width : 699px; height : 57px; border: 1px #c0c0c0 solid">
					<div class="align_vertical_center pay-label"><input class="pay-method" type="radio" name="payMethod" value="Credit" checked="checked"><span class="center_text" style="float:left">&nbsp;&nbsp;신용카드</span></div>
					<div class="align_vertical_center pay-label"><input class="pay-method" type="radio" name="payMethod" value="Account"><span class="center_text" style="float:left">&nbsp;&nbsp;계좌이체</span></div>
					<div class="align_vertical_center pay-label"><input class="pay-method" type="radio" name="payMethod" value="CellPhone"><span class="center_text" style="float:left">&nbsp;&nbsp;휴대폰</span></div>
				</div> 
			</div>
		</div>
		
		<div id="purchase" style="width:167px; height:44px; margin: 0 auto; background-image: url(../artifact/purchase_button.png); cursor : pointer;"></div>
		<div style="height: 20px;"></div>
		<jsp:include page="../include/footer.jsp" flush="false"></jsp:include>
	</div>
	
	<div id="light" class="white_content"></div>
	<div id="fade" class="black_overlay"></div>

	<script type="text/javascript">
		$('select').wSelect();
		
		$('.wSelect:first').width('475px');
		$('.wSelect:first').css('max-width', '475px');
		$('.wSelect-options-holder:first').width('475px');
		$('.wSelect-options-holder:first').css('max-width', '475px');
		
		$('.wSelect:nth-child(2)').css('margin-top', '11px');
		$('.wSelect:nth-child(2)').css('margin-bottom', '11px');
		$('.wSelect').addClass("align_vertical_center");
		
		$('.wSelect-options').each(function(index, value){
			var height = $(this).height();
			$(this).height(height+4);
		})
		
		$($('.wSelect-options')[1]).height(212);
	</script>
</body>
</html>