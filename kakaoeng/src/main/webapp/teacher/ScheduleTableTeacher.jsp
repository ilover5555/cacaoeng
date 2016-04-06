<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="classList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<style>

.table_header_column {
	width: 130px;
	text-align: center;
}
.slot-l {
	float: left;
	border-radius: 13px;
}

.slot-r {
	float: right;
	border-radius: 13px;
}


.clear {
	background-color: #E9E9E9;
}

.avail {
	background-color: #92D050;
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

.clicked {
	background-color: #40CFFF;
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
</style>
<script>

$(document).ready(function(){
	var avail=null;
	<c:forEach var="classItem" items="${classList }">
		avail = $('[data-date="${classItem.stamp}"]');
		avail.toggleClass('clear');
		avail.toggleClass('avail');
	</c:forEach>
})
</script>
<div style="width : 850px; background-color: white;">
<table id="scheduleTable" class="table table-bordered" style="text-align:center; margin: 0">
			<thead>
				<tr>
					<th class="table_header_column" colspan="2"></th>
					<th class="table_header_column" style="background-color: rgb(255,161,161)">Sunday</th>
					<th class="table_header_column">Monday</th>
					<th class="table_header_column">Tuesday</th>
					<th class="table_header_column">Wednesday</th>
					<th class="table_header_column">Thursday</th>
					<th class="table_header_column">Friday</th>
					<th class="table_header_column" style="background-color: rgb(219,233,246)">Saturday</th>
				</tr>
			</thead>
	
			<tbody>
				<tr>
					<td class="AM" rowspan="8" align="center" style="width:20px; vertical-align: middle">A M</td>
				</tr>
	
				<tr>
					<td class="slot-container time_cell AM" data-hour="6" >05~06</td>
					<td class="slot-container AM">
						<div data-date="0060" class="slot-l clear"></div>
						<div data-date="0061" class="slot-r clear"></div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1060" class="slot-l clear"> </div>
						<div  data-date="1061" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2060" class="slot-l clear"> </div>
						<div  data-date="2061" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3060" class="slot-l clear"> </div>
						<div  data-date="3061" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4060" class="slot-l clear"> </div>
						<div  data-date="4061" class="slot-r clear"> </div>
					</td >
					<td class="slot-container AM">
						<div  data-date="5060" class="slot-l clear"> </div>
						<div  data-date="5061" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6060" class="slot-l clear"> </div>
						<div  data-date="6061" class="slot-r clear"> </div>
					</td>
					
				</tr>
	
				<tr>
					<td class="slot-container time_cell AM" data-hour="7">06~07</td>
					<td class="slot-container AM">
						<div  data-date="0070" class="slot-l clear"> </div>
						<div  data-date="0071" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1070" class="slot-l clear"> </div>
						<div  data-date="1071" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2070" class="slot-l clear"> </div>
						<div  data-date="2071" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3070" class="slot-l clear"> </div>
						<div  data-date="3071" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4070" class="slot-l clear"> </div>
						<div  data-date="4071" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="5070" class="slot-l clear"> </div>
						<div  data-date="5071" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6070" class="slot-l clear"> </div>
						<div  data-date="6071" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell AM" data-hour="8">07~08</td>
					<td class="slot-container AM">
						<div  data-date="0080" class="slot-l clear"> </div>
						<div  data-date="0081" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1080" class="slot-l clear"> </div>
						<div  data-date="1081" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2080" class="slot-l clear"> </div>
						<div  data-date="2081" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3080" class="slot-l clear"> </div>
						<div  data-date="3081" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4080" class="slot-l clear"> </div>
						<div  data-date="4081" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="5080" class="slot-l clear"> </div>
						<div  data-date="5081" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6080" class="slot-l clear"> </div>
						<div  data-date="6081" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell AM" data-hour="9">08~09</td>
					<td class="slot-container AM">
						<div  data-date="0090" class="slot-l clear"> </div>
						<div  data-date="0091" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1090" class="slot-l clear"> </div>
						<div  data-date="1091" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2090" class="slot-l clear"> </div>
						<div  data-date="2091" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3090" class="slot-l clear"> </div>
						<div  data-date="3091" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4090" class="slot-l clear"> </div>
						<div  data-date="4091" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="5090" class="slot-l clear"> </div>
						<div  data-date="5091" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6090" class="slot-l clear"> </div>
						<div  data-date="6091" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell AM" data-hour="10">09~10</td>
					<td class="slot-container AM">
						<div  data-date="0100" class="slot-l clear"> </div>
						<div  data-date="0101" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1100" class="slot-l clear"> </div>
						<div  data-date="1101" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2100" class="slot-l clear"> </div>
						<div  data-date="2101" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3100" class="slot-l clear"> </div>
						<div  data-date="3101" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4100" class="slot-l clear"> </div>
						<div  data-date="4101" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="5100" class="slot-l clear"> </div>
						<div  data-date="5101" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6100" class="slot-l clear"> </div>
						<div  data-date="6101" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell AM" data-hour="11">10~11</td>
					<td class="slot-container AM">
						<div  data-date="0110" class="slot-l clear"> </div>
						<div  data-date="0111" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1110" class="slot-l clear"> </div>
						<div  data-date="1111" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2110" class="slot-l clear"> </div>
						<div  data-date="2111" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3110" class="slot-l clear"> </div>
						<div  data-date="3111" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4110" class="slot-l clear"> </div>
						<div  data-date="4111" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="5110" class="slot-l clear"> </div>
						<div  data-date="5111" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6110" class="slot-l clear"> </div>
						<div  data-date="6111" class="slot-r clear"> </div>
					</td>
					
				</tr>
				
				<tr>
					<td class="slot-container time_cell AM" data-hour="12">11~12</td>
					<td class="slot-container AM">
						<div  data-date="0120" class="slot-l clear"> </div>
						<div  data-date="0121" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="1120" class="slot-l clear"> </div>
						<div  data-date="1121" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="2120" class="slot-l clear"> </div>
						<div  data-date="2121" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="3120" class="slot-l clear"> </div>
						<div  data-date="3121" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="4120" class="slot-l clear"> </div>
						<div  data-date="4121" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="5120" class="slot-l clear"> </div>
						<div  data-date="5121" class="slot-r clear"> </div>
					</td>
					<td class="slot-container AM">
						<div  data-date="6120" class="slot-l clear"> </div>
						<div  data-date="6121" class="slot-r clear"> </div>
					</td>
					
				</tr>
				
				<tr>
					<td class="PM"rowspan="12" align="center" style="width:20px; vertical-align: middle">P M</td>
				</tr>
	
				
	
				<tr>
					<td class="slot-container time_cell PM" data-hour="13">12~01</td>
					<td class="slot-container PM">
						<div  data-date="0130" class="slot-l clear"> </div>
						<div  data-date="0131" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1130" class="slot-l clear"> </div>
						<div  data-date="1131" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2130" class="slot-l clear"> </div>
						<div  data-date="2131" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3130" class="slot-l clear"> </div>
						<div  data-date="3131" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4130" class="slot-l clear"> </div>
						<div  data-date="4131" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5130" class="slot-l clear"> </div>
						<div  data-date="5131" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6130" class="slot-l clear"> </div>
						<div  data-date="6131" class="slot-r clear"> </div>
					</td>
					
				</tr>
	
				<tr>
					<td class="slot-container time_cell PM" data-hour="14">01~02</td>
					<td class="slot-container PM">
						<div  data-date="0140" class="slot-l clear"> </div>
						<div  data-date="0141" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1140" class="slot-l clear"> </div>
						<div  data-date="1141" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2140" class="slot-l clear"> </div>
						<div  data-date="2141" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3140" class="slot-l clear"> </div>
						<div  data-date="3141" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4140" class="slot-l clear"> </div>
						<div  data-date="4141" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5140" class="slot-l clear"> </div>
						<div  data-date="5141" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6140" class="slot-l clear"> </div>
						<div  data-date="6141" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="15">02~03</td>
					<td class="slot-container PM">
						<div  data-date="0150" class="slot-l clear"> </div>
						<div  data-date="0151" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1150" class="slot-l clear"> </div>
						<div  data-date="1151" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2150" class="slot-l clear"> </div>
						<div  data-date="2151" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3150" class="slot-l clear"> </div>
						<div  data-date="3151" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4150" class="slot-l clear"> </div>
						<div  data-date="4151" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5150" class="slot-l clear"> </div>
						<div  data-date="5151" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6150" class="slot-l clear"> </div>
						<div  data-date="6151" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="16">03~04</td>
					<td class="slot-container PM">
						<div  data-date="0160" class="slot-l clear"> </div>
						<div  data-date="0161" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1160" class="slot-l clear"> </div>
						<div  data-date="1161" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2160" class="slot-l clear"> </div>
						<div  data-date="2161" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3160" class="slot-l clear"> </div>
						<div  data-date="3161" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4160" class="slot-l clear"> </div>
						<div  data-date="4161" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5160" class="slot-l clear"> </div>
						<div  data-date="5161" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6160" class="slot-l clear"> </div>
						<div  data-date="6161" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="17">04~05</td>
					<td class="slot-container PM">
						<div  data-date="0170" class="slot-l clear"> </div>
						<div  data-date="0171" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1170" class="slot-l clear"> </div>
						<div  data-date="1171" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2170" class="slot-l clear"> </div>
						<div  data-date="2171" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3170" class="slot-l clear"> </div>
						<div  data-date="3171" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4170" class="slot-l clear"> </div>
						<div  data-date="4171" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5170" class="slot-l clear"> </div>
						<div  data-date="5171" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6170" class="slot-l clear"> </div>
						<div  data-date="6171" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="18">05~06</td>
					<td class="slot-container PM">
						<div  data-date="0180" class="slot-l clear"> </div>
						<div  data-date="0181" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1180" class="slot-l clear"> </div>
						<div  data-date="1181" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2180" class="slot-l clear"> </div>
						<div  data-date="2181" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3180" class="slot-l clear"> </div>
						<div  data-date="3181" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4180" class="slot-l clear"> </div>
						<div  data-date="4181" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5180" class="slot-l clear"> </div>
						<div  data-date="5181" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6180" class="slot-l clear"> </div>
						<div  data-date="6181" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="19">06~07</td>
					<td class="slot-container PM">
						<div  data-date="0190" class="slot-l clear"> </div>
						<div  data-date="0191" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1190" class="slot-l clear"> </div>
						<div  data-date="1191" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2190" class="slot-l clear"> </div>
						<div  data-date="2191" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3190" class="slot-l clear"> </div>
						<div  data-date="3191" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4190" class="slot-l clear"> </div>
						<div  data-date="4191" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5190" class="slot-l clear"> </div>
						<div  data-date="5191" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6190" class="slot-l clear"> </div>
						<div  data-date="6191" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="20">07~08</td>
					<td class="slot-container PM">
						<div  data-date="0200" class="slot-l clear"> </div>
						<div  data-date="0201" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1200" class="slot-l clear"> </div>
						<div  data-date="1201" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2200" class="slot-l clear"> </div>
						<div  data-date="2201" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3200" class="slot-l clear"> </div>
						<div  data-date="3201" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4200" class="slot-l clear"> </div>
						<div  data-date="4201" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5200" class="slot-l clear"> </div>
						<div  data-date="5201" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6200" class="slot-l clear"> </div>
						<div  data-date="6201" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="21">08~09</td>
					<td class="slot-container PM">
						<div  data-date="0210" class="slot-l clear"> </div>
						<div  data-date="0211" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1210" class="slot-l clear"> </div>
						<div  data-date="1211" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2210" class="slot-l clear"> </div>
						<div  data-date="2211" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3210" class="slot-l clear"> </div>
						<div  data-date="3211" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4210" class="slot-l clear"> </div>
						<div  data-date="4211" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5210" class="slot-l clear"> </div>
						<div  data-date="5211" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6210" class="slot-l clear"> </div>
						<div  data-date="6211" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="22">09~10</td>
					<td class="slot-container PM">
						<div  data-date="0220" class="slot-l clear"> </div>
						<div  data-date="0221" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1220" class="slot-l clear"> </div>
						<div  data-date="1221" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2220" class="slot-l clear"> </div>
						<div  data-date="2221" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3220" class="slot-l clear"> </div>
						<div  data-date="3221" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4220" class="slot-l clear"> </div>
						<div  data-date="4221" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5220" class="slot-l clear"> </div>
						<div  data-date="5221" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6220" class="slot-l clear"> </div>
						<div  data-date="6221" class="slot-r clear"> </div>
					</td>
					
				</tr>
				<tr>
					<td class="slot-container time_cell PM" data-hour="23">10~11</td>
					<td class="slot-container PM">
						<div  data-date="0230" class="slot-l clear"> </div>
						<div  data-date="0231" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="1230" class="slot-l clear"> </div>
						<div  data-date="1231" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="2230" class="slot-l clear"> </div>
						<div  data-date="2231" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="3230" class="slot-l clear"> </div>
						<div  data-date="3231" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="4230" class="slot-l clear"> </div>
						<div  data-date="4231" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="5230" class="slot-l clear"> </div>
						<div  data-date="5231" class="slot-r clear"> </div>
					</td>
					<td class="slot-container PM">
						<div  data-date="6230" class="slot-l clear"> </div>
						<div  data-date="6231" class="slot-r clear"> </div>
					</td>
					
				</tr>
			</tbody>
	
		</table>
</div>
		