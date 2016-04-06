<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<style>

.center_text {
	margin: 0 auto;
	display: block;
	text-align: center;
	vertical-align: middle;
	height: 100%;
	letter-spacing: 1px
}

.modal-dialog {
	outline: none;
	top: 200px;
	right: 1%;
}
.time_cell{
	cursor: pointer;
}
</style>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<script type="text/javascript">

	Map = function() {
		this.map = new Object();
	};
	Map.prototype = {
		put : function(key, value) {
			if(typeof(value) === "undefined")
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
		<c:forEach var="classItem" items="${classList }">
			originalMap.put("${classItem.stamp}");
		</c:forEach>
		
		$(".slot-l,.slot-r").click(function(e) {
			var date = $(this).attr('data-date');
			if ($(this).hasClass('avail')) {
				$(this).toggleClass('avail');
				$(this).toggleClass('clear');
				if(originalMap.containsKey(date))
					clearMap.put(date);
				else
					availMap.remove(date);
			} else if ($(this).hasClass('clear')) {
				$(this).toggleClass('clear');
				$(this).toggleClass('avail');
				clearMap.remove(date);
				if(!originalMap.containsKey(date))
					availMap.put(date);
			}

		});

		$("#save").click(function(e) {
			if((availMap.size() == 0) && (clearMap.size() == 0))
				return;
			var json_data = {};
			json_data["avail"] = availMap.keys();
			json_data["clear"] = clearMap.keys();
			var result = $.ajax({
				url : makeUrl("teacherChangeSchedule.do"),
				method : "POST",
				data : {"classData" : JSON.stringify(json_data)},
				dataType : "json",
				success:function(e){
					$.each(availMap.keys(), function(index, value){
						originalMap.put(value);
					})
					availMap.clear();
					$.each(clearMap.keys(), function(index, value){
						originalMap.remove(value);
					})
					clearMap.clear();
					alert('Successfully change your availabilty');
					location.reload();
				},
				error: function(request,status,error){
					alert(request.responseText);
					location.reload();
				}
			});
		});
		
		$('.time_cell').click(function(){
			var hour = Number($(this).attr('data-hour'));
			var stamp = '';
			for(var i=0; i<7; i++){
				for(var j=0; j<2; j++){
					stamp = i + '%02d'.format(hour) + j;
					var slot = $('div[data-date="'+stamp+'"]');
					if(slot.hasClass('clear')){
						slot.toggleClass('clear');
						slot.toggleClass('avail');
						clearMap.remove(stamp);
						if(!originalMap.containsKey(stamp))
							availMap.put(stamp);
					}

				}
			}
		})
	});
</script>
</head>
<body>

<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
    	<h2 style="color: #3779ba;font-weight: normal;">Input Your availability here</h2>
        <div id="tooplate_content">
			<div class="container" style="width: 100%">
				<jsp:include page="ScheduleTableTeacher.jsp" flush="false"></jsp:include>
				<button id="save" style="margin-top: 10px; margin-top: 10px;"
					type="button" class="btn btn-primary btn-lg btn-block">
					Save</button>
		
			</div>
		
			<div class="modal fade" id="chageSuccessModal" tabindex="-1" role="dialog"
				aria-labelledby="changeSuccess" aria-hidden="true">
		
				<div class="modal-dialog">
					<div class="modal-content">
		
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
		
							<h4 class="modal-title" id="myModalLabel">Schedule Change Success!!!</h4>
						</div>
		
						<div class="modal-body">Your request for changing teaching schedule is successfully completed.</div>
		
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">
								Close
							</button>
						</div>
		
					</div>
				</div>
			</div>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>