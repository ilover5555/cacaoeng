/**
 * 
 */



function initFunction(){
	
	$('.center_text').each(function() {
		var parentHeight = $(this).parent().height();
		$(this).css('lineHeight', parentHeight + "px");
	});
	
	$('.table_header_column_student').each(function(){
		var width = $(this).children('img').width();
		var height = $(this).children('img').height();
		$(this).css('border-left-style', 'none');
		$(this).css('border-right-style', 'none');
		$(this).css('padding', 0);
		$(this).css('width', width + 'px');
		$(this).css('height', height + 'px');
	})
	
	$('.slot-l').each(function() {
		var parentWidth = $(this).parent().width();
		var newWidth = parentWidth*(0.49);
		var parentHeight = $(this).parent().height();
		var newHeight = parentHeight*1.0;
		if(newHeight > 18)
			newHeight = 18;
		$(this).css('width', newWidth + "px");
		$(this).css('height', newHeight + "px");
	});
	
	$('.slot-r').each(function() {
		var parentWidth = $(this).parent().width();
		var newWidth = parentWidth*(0.49);
		var parentHeight = $(this).parent().height();
		var newHeight = parentHeight*1.0;
		if(newHeight > 18)
			newHeight = 18;
		$(this).css('width', newWidth + "px");
		$(this).css('height', newHeight + "px");
	});
	
	$('.nominated').each(function() {
		var parentWidth = $(this).parent().width();
		var newWidth = (parentWidth-3)*(0.8);
		var parentHeight = $(this).parent().height();
		var newHeight = parentHeight*0.9;
		$(this).css('width', newWidth + "px");
		$(this).css('height', newHeight + "px");
	});
	
	$('.align_vertical_center').each(function(){
		var target = $(this);
		var parentHeight = target.parent().height();
		var borderTop = Number(target.css('border-top-width').slice(0,-2));
		var borderBottom = Number(target.css('border-bottom-width').slice(0,-2));
		var thisHeight = target.height() + Number(target.css('padding-top').slice(0,-2)) + Number(target.css('padding-bottom').slice(0,-2));
		var margin = (parentHeight - thisHeight - borderTop - borderBottom)/2;
		$(this).css('margin-top', margin + "px");
		$(this).css('margin-bottom', margin + "px");
	})
	
	$('.align_compact_vertical_center').each(function(){
		var target = $(this);
		var maxHeight = 0;
		$.each(target.children(), function(index, value){
			var height = $(value).height();
			height += Number($(value).css('border-top-width').slice(0,-2));
			height += Number($(value).css('border-bottom-width').slice(0,-2));
			height += Number($(value).css('margin-top').slice(0,-2));
			height += Number($(value).css('margin-bottom').slice(0,-2));
			height += Number($(value).css('padding-top').slice(0,-2));
			height += Number($(value).css('padding-bottom').slice(0,-2));
			if(height > maxHeight)
				maxHeight = height;
			})
		
		$(this).height(maxHeight);
			
		var parentHeight = target.parent().height();
		var borderTop = Number(target.css('border-top-width').slice(0,-2));
		var borderBottom = Number(target.css('border-bottom-width').slice(0,-2));
		var thisHeight = target.height() + Number(target.css('padding-top').slice(0,-2)) + Number(target.css('padding-bottom').slice(0,-2));
		var margin = (parentHeight - thisHeight - borderTop - borderBottom)/2;
		$(this).css('margin-top', margin + "px");
		$(this).css('margin-bottom', margin + "px");
	})
	
	$('.align_horizontal_center').each(function(){
		var target = $(this);
		var thisWidth = 2;
		$.each(target.children(), function(index, value){
			thisWidth += $(value).width();
			thisWidth += Number($(value).css('border-left-width').slice(0,-2));
			thisWidth += Number($(value).css('border-right-width').slice(0,-2));
			thisWidth += Number($(value).css('margin-left').slice(0,-2));
			thisWidth += Number($(value).css('margin-right').slice(0,-2));
			thisWidth += Number($(value).css('padding-left').slice(0,-2));
			thisWidth += Number($(value).css('padding-right').slice(0,-2));
			})
		$(this).css('width',thisWidth+"px");
		$(this).css('margin', "0 auto");
	})
	
	if (!Date.now) {
	    Date.now = function() { return new Date().getTime(); }
	}
	
	
}

$(document).ready(function() {
	initFunction();
})

String.prototype.repeat = function(n) {
  var sRet = "";
  for (var i = 0; i < n; i++) sRet += this;
  return sRet;
}

String.prototype.format = function(/* ... */) {

	var args = arguments;
	var idx = 0;

	return this.replace(
			/%(-?)([0-9]*\.?[0-9]*)([s|f|d|x|X|o])/g,
			function(all, sign, format, type) {

				var arg;
				var prefix = format.charAt(0);

				format = format.split(/\./);

				format[0] = parseInt(format[0], 10) || 0;
				format[1] = format[1] === undefined ? NaN : parseInt(format[1],
						10) || 0;

				if (type == 's') {
					arg = isNaN(format[1]) ? args[idx] : args[idx].substr(0,
							format[1]);
				} else {

					if (type == 'f') {
						arg = (format[1] === 0 ? parseInt(args[idx], 10)
								: parseFloat(args[idx])).toString();
						if (!isNaN(format[1]))
							arg = arg.replace(RegExp('(\\.[0-9]{' + format[1]
									+ '})[0-9]*'), '$1');
					} else if (type == 'd') {
						arg = parseInt(args[idx], 10).toString();
					} else if (type == 'x') {
						arg = parseInt(args[idx], 10).toString(16)
								.toLowerCase();
					} else if (type == 'X') {
						arg = parseInt(args[idx], 10).toString(16)
								.toUpperCase();
					} else if (type == 'o') {
						arg = parseInt(args[idx], 10).toString(8);
					}

					if (prefix == '0')
						arg = '0'.repeat(format[0] - arg.length) + arg;

				}

				if (sign == '-') {
					arg += ' '.repeat(format[0] - arg.length);
				} else {
					arg = ' '.repeat(format[0] - arg.length) + arg;
				}

				idx++;
				return arg;

			}).replace(/%%/g, '%');

}

function PopupCenter(url, title, w, h) {
    // Fixes dual-screen position                         Most browsers      Firefox
    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : screen.left;
    var dualScreenTop = window.screenTop != undefined ? window.screenTop : screen.top;

    var width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;
    var height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;

    var left = ((width / 2) - (w / 2)) + dualScreenLeft;
    var top = ((height / 2) - (h / 2)) + dualScreenTop;
    var newWindow = window.open(url, title, 'scrollbars=yes, width=' + w + ', height=' + h + ', top=' + top + ', left=' + left);

    // Puts focus on the newWindow
    if (window.focus) {
        newWindow.focus();
    }
}

var common = {
	init : initFunction,
	"PopupCenter" : PopupCenter
};
