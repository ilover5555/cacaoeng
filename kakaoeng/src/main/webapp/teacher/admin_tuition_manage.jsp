<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<<jsp:useBean id="SMART_FINAL_PRICE" scope="request" class="java.lang.Object"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<title>Insert title here</title>
<style>
[contenteditable]{
	background-color: yellow;
}
.price{
	text-align: right;
}
td {
	padding-top: 0px !important;
	padding-bottom: 0px !important;
}
.bold{
	font-weight: bold;
}
</style>
<script type="text/javascript">
function strip_tags (input, allowed) {
    allowed = (((allowed || "") + "").toLowerCase().match(/<[a-z][a-z0-9]*>/g) || []).join(''); // making sure the allowed arg is a string containing only tags in lowercase (<a><b><c>)
    var tags = /<\/?([a-z][a-z0-9]*)\b[^>]*>/gi,
        commentsAndPhpTags = /<!--[\s\S]*?-->|<\?(?:php)?[\s\S]*?\?>/gi;
    return input.replace(commentsAndPhpTags, '').replace(tags, function ($0, $1) {        return allowed.indexOf('<' + $1.toLowerCase() + '>') > -1 ? $0 : '';
    });
}
function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
	$(document).ready(function(){
		$('#Button_CoppyAll').click(function(){
			for(var i=2; i<=40; i++){
				var id = 'SMART_FINAL_'+i;
				var ad = $('#AD_'+id).html();
				$('#'+id).html(ad);
			}
		})
		$('.price_input').keyup(function(){
			var rawNumber = strip_tags($(this).html()).replace(/,/g,'');
			var comma = numberWithCommas(rawNumber);
			$(this).html(comma);
		})
		
		$("#changeButton").click(function(){
			var f = new FormData();
			f.append('ONE_MONTH_DISCOUNT_PERCENT', strip_tags($('#ONE_MONTH_DISCOUNT_PERCENT').html()));
			f.append('THREE_MONTH_DISCOUNT_PERCENT', strip_tags($('#THREE_MONTH_DISCOUNT_PERCENT').html()));
			f.append('SIX_MONTH_DISCOUNT_PERCENT', strip_tags($('#SIX_MONTH_DISCOUNT_PERCENT').html()));
			f.append('TWELVE_MONTH_DISCOUNT_PERCENT', strip_tags($('#TWELVE_MONTH_DISCOUNT_PERCENT').html()));
			f.append('DAY_DISCOUNT_PERCENT', strip_tags($('#DAY_DISCOUNT_PERCENT').html()));
			
			f.append('SMART_DISCOUNT_NAME', strip_tags($('#SMART_DISCOUNT_NAME').html()));
			f.append('SMART_DISCOUNT_PERCENT', strip_tags($('#SMART_DISCOUNT_PERCENT').html()));
			f.append('SMART_ADJUST_PERCENT', strip_tags($('#SMART_ADJUST_PERCENT').html()));
			f.append('SMART_BASE_PRICE', strip_tags($('#SMART_BASE_PRICE').html()).replace(/,/g,''));
			
			for(var i=2; i<=40; i++){
				f.append('SMART_FINAL_'+i, strip_tags($('#SMART_FINAL_'+i).html()).replace(/,/g,''));
			}
			
			$.ajax({
	            url: './adminSetTuition.do',
	            processData: false,
	            contentType: false,
	            data: f,
	            type: 'POST',
	            success: function(result){
	            	alert(result);
	            	location.reload();
	            },
	            error:function(xhr, status, error){
	            	alert(xhr.responseText);
	            }
	        });
		})
	})
</script>
</head>
<body>

	<div id="tooplate_wrapper">
    	<jsp:include page="Header.jsp" flush="false"></jsp:include>
	    <div id="tooplate_main">
	    	<div id="tooplate_content">
	    	<h2>수업료 관리</h2>
	    		<table id="monthDiscountPanel" class="sortable table table-bordered">
	    		<caption>개월당 할인율</caption>
					<tbody>
						<tr>
							<td></td>
							<td class="bold">1개월</td>
							<td class="bold">3개월</td>
							<td class="bold">6개월</td>
							<td class="bold">12개월</td>
							<td class="bold">낮할인율</td>
						</tr>
						<tr>
							<td>할인율</td>
							<td><div id="ONE_MONTH_DISCOUNT_PERCENT" style="text-align: center;" contenteditable>${ONE_MONTH_DISCOUNT_PERCENT }</div></td>
							<td><div id="THREE_MONTH_DISCOUNT_PERCENT" style="text-align: center;" contenteditable>${THREE_MONTH_DISCOUNT_PERCENT }</div></td>
							<td><div id="SIX_MONTH_DISCOUNT_PERCENT" style="text-align: center;" contenteditable>${SIX_MONTH_DISCOUNT_PERCENT }</div></td>
							<td><div id="TWELVE_MONTH_DISCOUNT_PERCENT" style="text-align: center;" contenteditable>${TWELVE_MONTH_DISCOUNT_PERCENT }</div></td>
							<td><div id="DAY_DISCOUNT_PERCENT" style="text-align: center;" contenteditable>${DAY_DISCOUNT_PERCENT }</div></td>
						</tr>
					</tbody>
				</table>
				
				<table id="fixDiscountPanel" class="sortable table table-bordered">
				<caption>고정수업 가격표(1개월 기준)</caption>
					<tbody>
						<tr>
							<td class="bold">수업횟수</td>
							<td class="bold">분</td>
							<td class="bold">수강료</td>
							<td class="bold">${SMART_DISCOUNT_NAME }</td>
						</tr>
						<tr>
							<td rowspan="2">주2회</td>
							<td>25분</td>
							<td style="text-align: right;"><fmt:formatNumber value="${SMART_FINAL_2 }" pattern="#,###"/></td>
							<td rowspan="6" style="text-align: center;">${SMART_DISCOUNT_PERCENT }</td>
						</tr>
						<tr>
							<td>50분</td>
							<td style="text-align: right;"><fmt:formatNumber value="${SMART_FINAL_PRICE.getValue(4) }" pattern="#,###"/></td>
						</tr>
						<tr>
							<td rowspan="2">주3회</td>
							<td>25분</td>
							<td style="text-align: right;"><fmt:formatNumber value="${SMART_FINAL_PRICE.getValue(3) }" pattern="#,###"/></td>
						</tr>
						<tr>
							<td>50분</td>
							<td style="text-align: right;"><fmt:formatNumber value="${SMART_FINAL_PRICE.getValue(6) }" pattern="#,###"/></td>
						</tr>
						<tr>
							<td rowspan="2">주5회</td>
							<td>25분</td>
							<td style="text-align: right;"><fmt:formatNumber value="${SMART_FINAL_PRICE.getValue(5) }" pattern="#,###"/></td>
						</tr>
						<tr>
							<td>50분</td>
							<td style="text-align: right;"><fmt:formatNumber value="${SMART_FINAL_PRICE.getValue(10) }" pattern="#,###"/></td>
						</tr>
					</tbody>
				</table>
				
				<table id="smartDiscountPanel" class="sortable table table-bordered">
				<caption>스마트예약/선생님예약 가격표(1개월 기준)<button id="Button_CoppyAll" class="btn btn-primary copy" style="float: right;">copyAll</button></caption>
				
					<tbody>
						<tr>
							<td class="bold">Min/W</td>
							<td class="bold">Min/M</td>
							<td class="bold">비율</td>
							<td class="bold">Price</td>
							<td class="bold">Adjust</td>
							<td class="bold">Ad-price</td>
							<td class="bold">FinalPrice</td>
							<td class="bold"><div id="SMART_DISCOUNT_NAME" contenteditable>${SMART_DISCOUNT_NAME }</div></td>
							
						</tr>
						<tr>
							<td>50</td>
							<td>${50*4 }</td>
							<td>0</td>
							<td class="price"><div id="SMART_BASE_PRICE" class="price_input" contenteditable><fmt:formatNumber value="${SMART_BASE_PRICE }" pattern="#,###"/></div></td>
							<td rowspan="39" class="price"><div id="SMART_ADJUST_PERCENT" contenteditable style="text-align: center;">${SMART_ADJUST_PERCENT }</div></td>
							<td class="price" id="AD_${SMART_FINAL_PRICE.getKey(2) }">${SMART_BASE_PRICE }</td>
							<td class="price"><div id="SMART_FINAL_2" class="price_input" contenteditable><fmt:formatNumber value="${SMART_FINAL_2}" pattern="#,###"/></div></td>
							<td rowspan="39" class="price"><div id="SMART_DISCOUNT_PERCENT" contenteditable style="text-align: center;">${SMART_DISCOUNT_PERCENT }</div></td>
						</tr>
						<c:forEach var="i" begin="3" end="40">
							<tr>
								<td>${25*i }</td>
								<td>${25*i*4 }</td>
								<c:set scope="page" value="${(25*i)/(25*(i-1)) }" var="rate"></c:set>
								<td><fmt:formatNumber value="${rate }" pattern="0.00"/></td>
								<td class="price"><fmt:formatNumber value="${SMART_BASE_PRICE * ((25*i)/50) }" pattern="#,###"/></td>
								<c:set scope="page" value="${SMART_BASE_PRICE *((25*(i-1))/50)* ((rate)-(SMART_ADJUST_PERCENT)) }" var="price"></c:set>
								<fmt:parseNumber var="j" integerOnly="true" type="number" value="${(price/100)}" />
								<td class="price" id="AD_${SMART_FINAL_PRICE.getKey(i) }"><fmt:formatNumber value="${j*100 }" pattern="#,###"/></td>
								<td class="price"><div class="price_input" contenteditable id="${SMART_FINAL_PRICE.getKey(i) }"><fmt:formatNumber value="${SMART_FINAL_PRICE.getValue(i) }" pattern="#,###"/></div></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<button id="changeButton" class="btn btn-primary btn-block">저장</button>
				
				<div style="height: 20px;"></div>
			</div> <!-- end of content -->
		</div>	<!-- end of main -->
    </div>
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>
</body>
</html>