<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<style>

.slot-l {
	float: left;
	border-radius: 3px;
}

.slot-r {
	float: right;
	border-radius: 3px;
}

.clear {
	background-color: #E9E9E9;
}

.avail {
	background-color: #92D050;
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
	letter-spacing: 1px
}

.clicked {
	background-color: #C5C833;
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
}

.slot-container {
	padding: 3px !important;
	overflow: hidden;
	vertical-align: middle;
	text-align: center;
}
#scheduleTable th, #scheduleTable td { border:1px solid black; }
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

	$(document).ready(
			function() {
				var avail = null;
				<c:forEach var="classItem" items="${classList }">
				avail = $('[data-date="${classItem.stamp}"]');
				avail.toggleClass('clear');
				avail.toggleClass('avail');
				</c:forEach>

				$(".slot-l,.slot-r").click(function(e) {
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
			});
</script>
</head>
<body>
	<jsp:useBean id="classList" scope="request" class="java.util.ArrayList"></jsp:useBean>
	<jsp:useBean id="baseDate" scope="request" class="java.lang.String"></jsp:useBean>
	<jsp:useBean id="teacherId" scope="request" class="java.lang.String"></jsp:useBean>
	<jsp:useBean id="teacherName" scope="request" class="java.lang.String"></jsp:useBean>
	<div style="width: 934px">
		<div style=" border-top: 2px #c0c0c0 solid; height : 55px;">
			<div style ="height : 54px; width:229px;">
				<input class="align_vertical_center" style ="border-radius:5px; -webkit-border-radius:5px; -moz-border-radius:5px; width:122px; height:25px;float:left; margin-left: 30px;" type="text" id="classStartDate" value="${baseDate }">
				<img class="align_vertical_center" onclick='$("#classStartDate").datepicker("show"); return false;' src="artifact/calendar.png"/>
			</div>
		</div>
		<jsp:include page="ScheduleTable.jsp" flush="false"></jsp:include>
		<div id="selectSchedule" style="background-image: url(http://placehold.it/129x34); margin: 0 auto; width : 129px; height:34px; cursor: pointer;">수강신청</div>
	</div>
</body>
</html>