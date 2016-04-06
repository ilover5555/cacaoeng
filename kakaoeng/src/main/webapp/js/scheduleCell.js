function makeScheduleCell(row, width){
	var time = null;
	if(Number(row["hour"]) <= 11){
		time = 'AM';
	}
	else if(Number(row["hour"]) == 12){
		time = 'PM';
	}
	else{
		time = 'PM';
		row["hour"] = Number(row["hour"])-12;
	}
	var hour = "%02d".format(Number(row["hour"]));
	var minute = "%02d".format(row["minute"]);
	var tag = '<div class="scheduleCell" style="width:'+ width +'px; height:100%;  float:left">'+
	'<div style="width:100%; height:30px; background-color: #BE0606;"><p class="center_text" style="text-align: center;  color: white; font-weight: bold">'+
	dayOfWeekMap[row["dayOfWeek"]]+
	'</p></div><div style="width:100%; height:77px; text-align: center;">'+
		'<p class="center_text" style="height:100%">&nbsp;'+ time+' ' + hour +':'+ minute +'</p>'+
	'</div><div style="width:100%; height:30px;"><p class="center_text" style="height:100%; background-color: #E1E1E1; margin: 0 auto">'+
	(Number(row["duration"])*25)+
	'분 수업</p></div></div></div>';
	return tag;
}

function makeSelectedTeacherDisplay(selectedResult){
	return "담당선생님: "+selectedResult["name"];
}

function makeLevelTestClassDateDisplay(realStartDate){
	var sDate = new Date(realStartDate);
	var txt = "레벨테스트(시범수업) 신청: "+ sDate.getFullYear() +"년"+ (sDate.getMonth()+1) +"월"+sDate.getDate()+"일("+dayOfWeekMap[sDate.getDay()]+")";
	return txt;
}

function makeClassRangeDisplay(realStartDate, realEndDate){
	var sDate = new Date(realStartDate);
	var eDate = new Date(realEndDate);
	var txt = "수업기간: "+ sDate.getFullYear() +"년"+ (sDate.getMonth()+1) +"월"+sDate.getDate()+"일("+dayOfWeekMap[sDate.getDay()]+")";
	txt += ' ~ ';
	txt += eDate.getFullYear() +"년"+ (eDate.getMonth()+1) +"월"+eDate.getDate()+"일("+dayOfWeekMap[eDate.getDay()]+")";
	return txt;
}

function getCurrentBrowerSize(){
	var uniwin = {
			width: window.innerWidth || document.documentElement.clientWidth
				|| document.body.offsetWidth,
			height: window.innerHeight || document.documentElement.clientHeight
				|| document.body.offsetHeight
		};
	
	return uniwin;
}

function showBox(scrollY, init, offExit, offBorder){
	if (typeof scrollY == 'undefined')
		scrollY = 0;
	var margin = 2.5;
	var marginRight = Number($('#light > div').css('margin-right').slice(0,-2));
	var paddingRight = Number($('#light > div').css('padding-right').slice(0,-2));
	var iTag = '<i style="position:absolute; right: 0; top:0; font-size : 25px; cursor:pointer;" onclick="hideBox(); return;" class="fa fa-times"></i>';
	if(offExit == true){
		margin = 0;
		iTag = '';
	}
	var i = $(iTag);
	i.css('color' , 'rgb(199,9,40)');
	i.appendTo('#light');
	document.getElementById('light').style.display='block';
	
	
	var border = 14;
	if(offBorder == true){
		border = 0;
	}
	if(typeof init != 'undefined')
		init();
	$('#light > div').css('border', border+'px solid rgb(90,90,90)');
	var width = Number($('#light > div').css('width').slice(0,-2));
	
	var height = Number($('#light > div').css('height').slice(0,-2));
	$('#light > div').css('width' , (width + (2*border)) + 'px')
	var w = (width+i.width()+margin + (2*border) + marginRight + paddingRight);
	if(isNaN(w))
		w = (width+margin + (2*border));
	$('#light').css('width', w +'px');
	$('#light').css('height', (height + (2*border) + 20)+'px');
	
	
	
	var uniwin = getCurrentBrowerSize();
	var top = (uniwin.height - height)/2;
	$('#light').css('top', (top+scrollY)+'px');
	$(document).scrollTop(scrollY);
	
	if($('#light').position().top <= 0)
		$('#light').css('top', '50px');
	
	document.getElementById('fade').style.display='block';
	$('#fade').width(window.screen.width+'px');
	$('#fade').height($(document).height()+'px');
}

function hideBox(){
	document.getElementById('light').style.display='none';
	document.getElementById('fade').style.display='none';
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

$(document).keyup(function(e) {
    if (e.keyCode == 27) {
   	 hideBox();
   }
});

$(document).resize(function(){
	alert('a');
})

var scheduleCell= {"makeScheduleCell" : makeScheduleCell, "makeSelectedTeacherDisplay" : makeSelectedTeacherDisplay,
		"makeClassRangeDisplay" : makeClassRangeDisplay, "showBox" : showBox, "hideBox" : hideBox, "getCurrentBrowerSize" : getCurrentBrowerSize,
		"makeLevelTestClassDateDisplay" : makeLevelTestClassDateDisplay, "numberWithCommas" : numberWithCommas}; 