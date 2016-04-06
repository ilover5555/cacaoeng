$(function() {
	$("#banner01").jQBanner({ 
		nWidth:713, nHeight:358, nCount:5, isActType:"left", nOrderNo:1, isStartAct:"N", isStartDelay:"Y", nDelay:10000, isBtnType:"img"
	}); 

	//상단메뉴활성화, 상단서브메뉴활성화
	$(".header-menu img").hover(function(e) { 
		var me = $(this); 
		me.attr("src", me.attr("src").replace("off", "on"));
		me.addClass("on");		
		$(".header-menu img").each(function() { 
			var src = $(this).attr("src"); 
			if($(this).attr("alt") == me.attr("alt")){}
			else { 
				$(this).attr("src", src.replace("on", "off")); 
				me.removeClass("on");
			}
		});
	}); 

	//상단서브메뉴활성화
	$("#top-sub-menu img").mouseover(function() { 
		var me = $(this); 
		me.attr("src", me.attr("src").replace("off", "on"));
	});  
	$(".header-menu img, #top-sub-menu img").mouseout(function() { 
		var me = $(this); 
		me.attr("src", me.attr("src").replace("on", "off")); 
	});

	$('.header-menu').mouseenter(function () {
		$(this).find('.sub-menu').show( "blind", {direction: "down" }, 500 );
        //if(!$($(this).find('.sub-menu')).is(':animated')) $(this).find('.sub-menu').slideDown('slow'); 
     }); 
      
	$('.header-menu').mouseleave(function () { 
		//$(this).find('.sub-menu').slideUp('slow'); 
		$(this).find('.sub-menu').hide( "blind", {direction: "down" }, 500 );
	}); 

	//이미지 미리 로딩
	var imgList = ["images/main/top_bg_02.jpg"];
	$("#top-sub-menu img, .header-menu img").each(function() { 
		var src = $(this).attr("src"); 
		imgList.push(src.replace("on", "off")); 
	}); 
	$.preload(imgList);

});