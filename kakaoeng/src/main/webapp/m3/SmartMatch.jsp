<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="bookList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<jsp:useBean id="course" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<link href="../minimal/grey.css" rel="stylesheet">
<script src="../icheck.js"></script>
<link rel="stylesheet" type="text/css" href="../css/match.css" />
<link rel="stylesheet" type="text/css" href="../css/popup.css" />
<script src="../js/scheduleCell.js"></script>
<script type="text/javascript" src="../js/wSelect.min.js"></script>
<link rel="stylesheet" type="text/css" href="../wSelect.css" />
<script type="text/javascript" src="../js/pace.js"></script>
<link rel="stylesheet" type="text/css" href="../css/pace-theme-bounce.css" />



<title>Insert title here</title>
<style>
#SmartMatchStart {
	width: 129px;
	height: 34px;
	background-image: url(../artifact/match_start_button.png);
	float: right;
	cursor: pointer;
}

#selectDisplay>.scheduleCell {
	border-right: 1px #505050 solid;
}

#selectDisplay>.scheduleCell:last-of-type {
	border: none;
}

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

<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script>

	var dayOfWeekMap ={"Sunday" : "일요일", "Monday" : "월요일", "Tuesday" : "화요일", "Wednesday" : "수요일", "Thursday" : "목요일" , "Friday" : "금요일", "Saturday" : "토요일",
			0 : "일", 1 : "월", 2:"화", 3:"수", 4:"목", 5:"금", 6:"토"}
	
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
	
	$(document).keyup(function(e) {
	     if (e.keyCode == 27) {
	    	 scheduleCell.hideBox();
	    }
	});
	
	function dialogInit(){
		common.init();
		$('.choice-similar').iCheck({
			checkboxClass:'icheckbox_minimal-grey',
			radioClass:'iradio_minimal-grey',
			increaseArea:'50%'
		});
		$('#resultPanel .iradio_minimal-grey').each(function(index, value){
			var parent = $(this).parent();
			var size = $(this).width();
			var margin = 10;
			var left = (parent.width()/2)-size-margin;
			$(this).css('left', left+'px');
		})
		
		
		$('.teacher-select-button').click(function(){
			
			var id = $(this).attr('data-id');
			var baseDate = $('#classStartDate').val();
			var name = $('h6[id="name-' + id + '"]').text();
			selectedResult["baseDate"] = baseDate;
			selectedResult["id"] = id;
			selectedResult["name"] = name;
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
				var selected = $('#monthSelect').val()
				row["weeks"] = Number($('#monthSelect>option[value="'+ selected +'"').attr('data-duration'));
				selectedResult["index"+count] = JSON.stringify(row);
				count++;
			});
			selectedResult["length"] = count;
			if(valid == false){
				selectedResult = {};
				return;
			}
			$('#teacherNameDisplay').text(scheduleCell.makeSelectedTeacherDisplay(selectedResult));
			var realStartDate = $('#startDateHolder').val();
			var realEndDate = $('#endDateHolder').val();
			$('#baseDateDisplay').text(scheduleCell.makeClassRangeDisplay(realStartDate, realEndDate));
			
			var resultDisplay = $('#selectDisplay');
			resultDisplay.html('')
			var totalWidth = resultDisplay.width();
			var width = totalWidth / selectedResult["length"];
			
			
			for(i=0; i< selectedResult["length"]; i++){
				var row = JSON.parse(selectedResult["index"+i]);
				var tag = scheduleCell.makeScheduleCell(row, width);
				resultDisplay.html(resultDisplay.html() + tag);
			}
			common.init();
			selectedResult["matchSort"] = "smart";
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
					scheduleCell.hideBox();
					window.scrollTo(0,1440)
				},
				error: function(request,status,error){
					alert(request.responseText);
				}
			});
			
		})
	}
	
	$(document).ready(function(){
		
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
		
		$('.pay-method').iCheck({
			checkboxClass:'icheckbox_minimal-grey',
			radioClass:'iradio_minimal-grey',
			increaseArea:'50%'
		});
		$("#classStartDate").datepicker({
			dateFormat : 'yy-mm-dd',
		});
		$('#classStartDate').datepicker('setDate', '+1');

		$('#SmartMatchStart').click(function(){
			var matchSort = "smart";
			if($('#classStartDate').val() == ''){
				alert('날짜를 입력해 주세요');
				return;
			}
			var baseDate = $('#classStartDate').val();
			var monthDuration = $('#monthSelect').val();
			
			
			if((availMap.size() == 0)){
				alert('No Selected Slot.')
				return;
			}
			var json_data = {};
			json_data["avail"] = availMap.keys();
			
			var sendData = {
					"matchSort" : matchSort,
					"baseDate" : baseDate,
					"monthDuration" : monthDuration,
					"data" : JSON.stringify(json_data)
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

		
		$('#selectWrapper').load('./StudentSelectSchedule.jsp')
		
		$('.dataHolder').hover(function(){
			$('#date-holder').toggleClass('hover');
		}, function(){
			$('#date-holder').toggleClass('hover');
		})
		
		
		classSortMenuHandler = toggleHandle('class_sort_menu');
		periodMenuHandler = toggleHandle('period_menu');
		classDurationHandler = toggleHandle('class_duration');

		periodMenuHandler.enableHandle();
		classDurationHandler.enableHandle();
		
		$('#monthSelect').val('${param.month}month').change();
		
		Pace.on('done', function(){
			$('#selectWrapper').css('visibility', 'visible');
			common.init();
			})
	});
</script>
</head>
<body style="background-color: white">
	<div id="wrap">
		<jsp:include page="../include/header.jsp" flush="false"></jsp:include>
		<div style="overflow: hidden; margin-left: 33px; margin-right: 34px; margin-top: 78px; margin-bottom: 27px;">
			<div id="matchFixed"class="match_sort_menu_unselected"onclick="location.href = './match.view?sort=FixMatch&course=${param.course}&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"></div>
			<div id="matchSmart"class="match_sort_menu_selected"></div>
			<div id="matchTeacher"class="match_sort_menu_unselected"onclick="location.href = './match.view?sort=TeacherMatch&course=${param.course}&title=${param.title }&month=${param.month }&period=${param.period }&duration=${param.duration }'; return false;"></div>
		</div>
		
		<img src="../artifact/smart_match_sort_help.png" style="display : block; margin : 0 auto;"/>
		
		<div style="margin-top: 66px; margin-left: 33px; margin-right: 33px; margin-bottom: 64px; ">
			<img src="../artifact/smart_step1.png" style="margin-bottom: 8px"/>
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
		
		<div style="margin-left: 33px; margin-right: 33px; margin-bottom: 69px; width:938px;">
			<img src="../artifact/smart_step2.png" style="margin-bottom: 8px"/>
			<div style=" border-top: 2px #c0c0c0 solid; height : 54px;">
				<div style="float : left; width : 235px; height : 54px; background-image: url(../artifact/label_smart_class_range.png)"></div>
				<div style="float : left; width : 234px; height : 54px; border: 1px #c0c0c0 solid">
					<div id="" style ="height : 54px; width:229px;">
						<select id="monthSelect" class="wSelect-el">
							<option value="1month" data-duration="4">1개월</option>
							<option value="3month" data-duration="12">3개월</option>
							<option value="6month" data-duration="24">6개월</option>
							<option value="12month" data-duration="48">12개월</option>
						</select>
					</div>
				</div>
				<div style="float : left; width : 235px; height : 54px; background-image: url(../artifact/label_smart_start_date.png)"></div>
				<div style="float : left; width : 234px; height : 54px; border: 1px #c0c0c0 solid">
					<div style ="height : 54px; width:234px;">
						<div id="date-holder" class="align_vertical_center dataHolder" style="overflow: hidden;">
							<input type="text" id="classStartDate"  style="float: left;">
							<div class="arrow" onclick='$("#classStartDate").datepicker("show"); return false;' style="float: left;">
								<span class="glyphicon glyphicon-calendar align_vertical_center" style="font-size: 20px; display: block; margin-top: 1px;"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="selectWrapper" style="visibility: hidden;">
				<jsp:include page="StudentSelectSchedule.jsp" flush="false"></jsp:include>
			</div>
			<div style="height: 10px;"></div>
			<div style="overflow: hidden">
				<div id="SmartMatchStart"></div>
			</div>
		</div>
		
		<div style="margin-left: 33px; margin-right: 33px; margin-bottom: 69px; ">
			<img src="../artifact/smart_step3.png" style="margin-bottom: 8px"/>
			<div style="width:100%; height : 220px; overflow: hidden; border-left : 1px #c0c0c0 solid; border-right : 1px #c0c0c0 solid; border-bottom : 1px #c0c0c0 solid; border-top: 2px #c0c0c0 solid">
				<div style="width:861px; margin : 25px auto;">
					<div style="overflow: hidden; margin-bottom: 8px;">
						<p id="baseDateDisplay" class="center_text" style="float:left"> 수업기간: </p>
						<p id="teacherNameDisplay" class="center_text" style="float:right">담당 선생님: </p>
					</div>
					<div id="selectDisplay" style="width : 861px; height : 137px; border : 1px #505050 solid; overflow: hidden; font-size: 15px;">
						
					</div>
				</div>
			</div>
		</div>
		
		<div style="margin-left: 33px; margin-right: 33px; margin-bottom: 35px; ">
			<img src="../artifact/smart_step4.png" style="margin-bottom: 8px"/>
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
			$(this).height(height+6);
		})
		
	</script>
</body>
</html>