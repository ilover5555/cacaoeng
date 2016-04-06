<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<style>
#teacherSelectPanel .icheckbox_minimal-grey{
	float:left;
	margin-top: 1px;
}

.clear {
	background-color: #E9E9E9;
}

.avail {
	background-color: #92D050;
}
.slot-l {
	float: left;
	border-radius: 13px;
}

.slot-r {
	float: right;
	border-radius: 13px;
}

.AM > .clear {
	background-color: rgb(211,216,222);
}
.PM > .clear {
	background-color: rgb(219,221,210);
}
.reserved {
	pointer-events: none;
	cursor: default;
	background-color: #F37F26;
}

.center_text {
	margin: 0 auto;
	display: block;
	text-align: center;
	vertical-align: middle;
	height: 100%;
}

.clicked {
	background-color: #C70C27;
}

.modal-dialog {
	outline: none;
	top: 200px;
	right: 1%;
}

.theader_column {
	background-color: #92D050;
	float: left;
	width: 130px;
	height: 30px;
	border-style: solid;
	border-width: 1px;
	border-color: #DDDDDD;
	border-bottom: 2px;
	text-align: center;
}

.slot-container {
	padding: 3px !important;
	overflow: hidden;
	vertical-align: middle;
	text-align: center;
}
.AM{
	background-color: rgb(235,240,246);
}
.PM{
	background-color: rgb(243,245,234);
}
#scheduleTable th, #scheduleTable td { border:1px solid black; }

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
<script type="text/javascript">
	Map = function() {
		this.map = new Object();
	};
	Map.prototype = {
		put : function(key, value) {
			if (typeof (value) === "undefined")
				value = "dummy";
			this.map[key] = value;
		},
		get : function(key) {
			return this.map[key];
		},
		containsKey : function(key) {
			return key in this.map;
		},
		containsValue : function(value) {
			for ( var prop in this.map) {
				if (this.map[prop] == value)
					return true;
			}
			return false;
		},
		isEmpty : function(key) {
			return (this.size() == 0);
		},
		clear : function() {
			for ( var prop in this.map) {
				delete this.map[prop];
			}
		},
		remove : function(key) {
			delete this.map[key];
		},
		keys : function() {
			var keys = new Array();
			for ( var prop in this.map) {
				keys.push(prop);
			}
			return keys;
		},
		values : function() {
			var values = new Array();
			for ( var prop in this.map) {
				values.push(this.map[prop]);
			}
			return values;
		},
		size : function() {
			var count = 0;
			for ( var prop in this.map) {
				count++;
			}
			return count;
		}
	};

	var clickedMap = new Map();

	var levelTest = false;
	
	function reset(){
		$('.clicked').each(function(){
			$(this).toggleClass('clicked');
			$(this).toggleClass('avail');
		})
		clickedMap.clear();
	}
	
	$(document).ready(
			function() {
				var avail = null;
				<c:forEach var="classItem" items="${classList }">
				avail = $('[data-date="${classItem.stamp}"]');
				avail.toggleClass('clear');
				avail.toggleClass('avail');
				</c:forEach>

				$(".slot-l,.slot-r").click(function(e) {
					if(levelTest){
						if($(this).hasClass('clicked')){
							reset();
							return;
						}else{
							reset();
						}
						
					}
					
					var date = $(this).attr('data-date');
					if ($(this).hasClass('avail')) {
						$(this).toggleClass('avail');
						$(this).toggleClass('clicked');
						clickedMap.put(date);
					} else if ($(this).hasClass('clicked')) {
						$(this).toggleClass('clicked');
						$(this).toggleClass('avail');
						clickedMap.remove(date);
					}

				});
				
				$('#levelTest').change(function(){
					reset();
					var checked = $('#levelTest')[0].checked;
					levelTest = checked;
				})
			}
			
			
			
			);
</script>
</head>
<body style="background-color: white;">
	<jsp:useBean id="classList" scope="request" class="java.util.ArrayList"></jsp:useBean>
	<jsp:useBean id="baseDate" scope="request" class="java.lang.String"></jsp:useBean>
	<jsp:useBean id="teacherId" scope="request" class="java.lang.String"></jsp:useBean>
	<jsp:useBean id="teacherName" scope="request" class="java.lang.String"></jsp:useBean>
	<jsp:useBean id="teacherClassName" scope="request" class="java.lang.String"></jsp:useBean>
	<div id="teacherSelectPanel" style="width: 938px; background-color: white; overflow: hidden;">
		<div style=" border-top: 2px #c0c0c0 solid; height : 55px;">
			<input id="idHolder" type="hidden" value="${teacherId }">
			<input id="nameHolder" type="hidden" value="${teacherClassName}">
			<div style="float : left; width : 237px; height : 54px; background-image: url(../artifact/label_smart_class_range.png)"></div>
				<div style="float : left; width : 229px; height : 54px; border: 1px #c0c0c0 solid">
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
				<div style="float : left; width : 237px; height : 54px; border: 1px #c0c0c0 solid">
					<div style ="height : 54px; width:235px;">
						<div id="date-holder" class="align_vertical_center dataHolder" style="overflow: hidden;">
							<input type="text" id="classStartDate"  style="float: left;">
							<div class="arrow" onclick='$("#classStartDate").datepicker("show"); return false;' style="float: left;">
								<span class="glyphicon glyphicon-calendar align_vertical_center" style="font-size: 20px; display: block; margin-top: 1px;"></span>
							</div>
						</div>
					</div>
				</div>
		</div>
		<jsp:include page="ScheduleTable.jsp" flush="false"></jsp:include>
		<table style="width: 100%; margin: 10px auto;">
			<tr>
				<td style="width: 33%"></td>
				<td style="width: 33%">
					<div id="selectSchedule" style="background-image: url(../artifact/background_button2.png); margin: 0 auto; width : 129px; height:37px; cursor: pointer;">
						<p style="text-align: center; line-height: 37px; font-size: 20px; color: white;">수강신청</p>
					</div>
				</td>
				<td style="width: 33%;">
					<div class="checkbox" style="float: right; margin-right: 10px; margin-bottom: 0px; margin-top: 16px;">
						<input id="levelTest" type="checkbox" name="levelTest" class="levelTest" id="levelTest" style="width: 30px; height: 30px; float: left;"> <label for="levelTest">레벨테스트 (시범수업) 신청</label>
					</div>
				</td>
			</tr>
		</table>
		
	</div>
</body>
</html>