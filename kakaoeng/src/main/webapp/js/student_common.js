
$(function() {
	//상단메뉴활성화, 상단서브메뉴활성화
	$(".header-menu img").hover(function(e) { 
		var me = $(this); 
		me.attr("src", me.attr("src").replace("_off", "_on"));
		me.addClass("on");
		$(".header-menu img").each(function() { 
			var src = $(this).attr("src"); 
			if($(this).attr("alt") == me.attr("alt")){}
			else { 
				$(this).attr("src", src.replace("_on", "_off")); 
				me.removeClass("on");
			}
		});
	}); 

	//상단서브메뉴활성화
	$("#top-sub-menu img").mouseover(function() { 
		var me = $(this); 
		me.attr("src", me.attr("src").replace("_off", "_on"));
	});  
	$(".header-menu img, #top-sub-menu img").mouseout(function() { 
		var me = $(this); 
		me.attr("src", me.attr("src").replace("_on", "_off")); 
	});

	var _offing = false, _oning = false; 
	$('.header-menu').mouseenter(function () {
		if(!_oning){
			_oning = true; 
			$(this).find('.sub-menu').show( "blind", {direction: "down" }, 700, function(){
				_oning = false; 
				
			}); 
		}
    });       
	$('.header-menu').mouseleave(function () { 
		if(!_offing){
			_offing = true; 
			$(this).find('.sub-menu').delay(200).hide( "blind", {direction: "down" }, 700, function(){
				_offing = false; 
			});
		}
	}); 

	//이미지 미리 로딩
	var imgList = ["../main/images/top_bg_02.jpg"];
	$("#top-sub-menu img, .header-menu img").each(function() { 
		var src = $(this).attr("src"); 
		imgList.push(src.replace("_on", "_off")); 
	}); 
	$.preload(imgList);
}); 
loadCSS = function(href) {
     var cssLink = $("<link rel='stylesheet' type='text/css' href='"+href+"'>");
     $("head").append(cssLink); 
};