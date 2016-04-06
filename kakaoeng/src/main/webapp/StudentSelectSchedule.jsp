<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<style>
.time_cell{
	width: 81px;
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

.avail {
	background-color: #C70C27;
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
	background-color: #FFFFFF;
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
	font-weight: bold;
}

.AM{
	background-color: rgb(235,240,246);
}
.PM{
	background-color: rgb(243,245,234);
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

	var originalMap = new Map();
	var availMap = new Map();
	var clearMap = new Map();

	$(document).ready(
			function() {
				var avail = null;
				<c:forEach var="classItem" items="${classList }">
				avail = $('[data-date="${classItem.stamp}"]');
				avail.toggleClass('clear');
				avail.toggleClass('avail');
				originalMap.put("${classItem.stamp}");
				</c:forEach>

				$(".slot-l,.slot-r").click(function(e) {
					var date = $(this).attr('data-date');
					if ($(this).hasClass('avail')) {
						$(this).toggleClass('avail');
						$(this).toggleClass('clear');
						if (originalMap.containsKey(date))
							clearMap.put(date);
						else
							availMap.remove(date);
					} else if ($(this).hasClass('clear')) {
						$(this).toggleClass('clear');
						$(this).toggleClass('avail');
						clearMap.remove(date);
						if (!originalMap.containsKey(date))
							availMap.put(date);
					}

				});
			});
</script>
</head>
<body>
	<jsp:useBean id="classList" scope="request" class="java.util.ArrayList"></jsp:useBean>
	<div style="width: 934px">
		<jsp:include page="ScheduleTable.jsp" flush="false"></jsp:include>
	</div>
</body>
</html>