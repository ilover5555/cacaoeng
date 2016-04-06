<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="holidayFormList" scope="request" class="java.util.ArrayList"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="../HeadSetting.jsp" flush="false"></jsp:include>
<title>Blue Wave Template</title>
<link rel="stylesheet" href="../build/kalendae.css" type="text/css" charset="utf-8"/>
<script src="../build/kalendae.standalone.js" type="text/javascript" charset="utf-8"></script>
<script src="http://ajax.cdnjs.com/ajax/libs/json2/20110223/json2.js"></script>
<style type="text/css" media="screen">
	.kalendae .k-days span.closed {
		background:red;
	}
	.k-btn-next-month, .k-btn-previous-month{
		display: none !important;
	}
	.kalendae .k-days span.k-in-month:not(.k-active){
		background-color: orange;
		color: blue !important;
		cursor: pointer;
	}
	.kalendae .k-days span.k-in-month.deselect{
		background-color: aqua;
		color: blue !important;
		cursor: pointer;
	}
</style>
<script>

var holiday = [<c:forEach items="${holidayFormList }" var="holiday">"${holiday }",</c:forEach>"2016-00-00"];
var deselect = [];
$(document).ready(function(){
	$('#holidaySetButton').click(function(){
		var selected = k.getSelectedAsText();
		var s = {
				"dateList" : JSON.stringify(selected),
				"deselect" : JSON.stringify(deselect)
				};
		$.ajax({
			type:'POST',
			url:'./holidayServlet.do',
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			datatype: 'text/plain',
			data : s,
			success: function(data){
				alert(data);
				location.reload();
			},
			error: function(xhr, status, error){
				alert(xhr.responseText);
			},
		})
	})
	$('.kalendae .k-days span.k-in-month:not(.k-active)').click(function(){
		if(!$(this).hasClass('deselect')){
			deselect.push($(this).attr('data-date'))
			$(this).toggleClass('deselect');
		}else{
			var date = $(this).attr('data-date')
			var index = $.inArray(date, deselect);
			if(index >=0){
				deselect.splice(index, 1);
				$(this).toggleClass('deselect');
			}
		}
	})
})
</script>
</head>
<body>
		

	<div id="tooplate_wrapper">

	<jsp:include page="Header.jsp" flush="false"></jsp:include>
    
    <div id="tooplate_main">
                
        <div id="tooplate_content">
			<h1>공휴일 관리</h1>

        	<div style="overflow: hidden">
        		<a href="./holidayServlet.do?year=${param.year-1 }" style="float: left">이전 해</a>
        		<a href="./holidayServlet.do?year=${param.year+1 }" style="float: right">다음 해</a>
        	</div>
        
        	<div id="calWrapper">
        		
        		<script type="text/javascript" charset="utf-8">
					var k =new Kalendae({
						attachTo:$('#calWrapper')[0],
						months:12,
						mode:'multiple',
						selected:[Kalendae.moment().subtract({M:1}), Kalendae.moment().add({M:1})],
						useYearNav : false,
						blackout: function (date) {
							
							var k =date.year()+"-"+"%02d".format((date.month()+1))+"-"+"%02d".format(date.date())
							if($.inArray(k, holiday) != -1)
								return true;
							return false;
						}

					});
			
					k.setSelected('${param.year}-01-01');
					k.setSelected("");
					
				</script>
        	</div>
        
        	<button id="holidaySetButton" class="btn btn-primary btn-block">공휴일 지정</button>
        
        </div> <!-- end of content -->
        
    </div>	<!-- end of main -->
    
    <jsp:include page="Tail.jsp" flush="false"></jsp:include>

</div> <!-- end of wrapper -->
<!--   Free Website Template by t o o p l a t e . c o m   -->
</body>
</html>