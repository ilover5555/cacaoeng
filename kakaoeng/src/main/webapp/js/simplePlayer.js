
var YTdeferred = jQuery.Deferred();
window.onYouTubeIframeAPIReady = function() {
	YTdeferred.resolve(window.YT);
};

(function( $ ) {
	$.ajaxSetup({
		cache: true
	});
	$.getScript( "https://www.youtube.com/iframe_api")
		.done(function( script, textStatus ) {
	});

	$.fn.simplePlayer = function(cfg) {
		var	video = $(this);
		var videoId = video.attr("id");
		var num = Math.floor(Math.random()*10000);

		var playId = 'play' + num;
		var playerId = 'player' + num;
		var play = $('<div />', { id: playId }).hide();
 
		var style = [
			"<style type='text/css'>",
			"#"+ videoId +" { position: relative; background: #000; margin: 20px auto; width:"+ cfg.width +" }",
			"#"+ videoId +" img, #"+ videoId +" iframe { display: block; }",
			"#"+ playId +" { position: absolute; top: 0; left: 0; width: 100%; background-size: auto, cover; z-index: 9999;",
			"	height: 100%;  cursor: pointer; background: url('../images/play-button.png') no-repeat 50% 50%; }", 
			"#"+ playId +":hover { background-color: rgba(0,0,0,0.2) !important; }",
			"</style>"
		];
		$("body").append(style.join(""));
		var defaults = {
				autoplay: 1,
				autohide: 1,
				border: 0,
				wmode: 'opaque',
				enablejsapi: 1,
				modestbranding: 1,
				version: 3,
				hl: 'en_US',
				rel: 0,
				showinfo: 0,
				hd: 1,
				iv_load_policy: 3 // add origin
			};

		// onYouTubeIframeAPIReady

		YTdeferred.done(function(YT) {
			play.appendTo( video ).fadeIn('slow');
		});
		function onPlayerStateChange(event) {
			if (event.data == YT.PlayerState.ENDED) {
				play.fadeIn(500);
			}
		}
		function onPlayerReady(event) {
			var replay = document.getElementById(playId);
			replay.addEventListener('click', function() {
				player.playVideo();
			});
		}
		play.bind('click', function () {
			if ( !$('#'+playerId ).length ) {
				var data = (cfg.video) ? cfg.video : video.data('video');
				$('<iframe />', {
					id: playerId,
					src: 'https://www.youtube.com/embed/' + data + '?' + $.param(defaults)
				})
				.attr({ width: video.width(), height: video.height(), seamless: 'seamless' })
				.css('border', 'none')
				.appendTo( video );
				video.children('img').hide();
				$(this).css('background-image', 'url(../images/play-button.png), url(' + video.children().attr('src') + ')').hide();	
				player = new YT.Player(playerId, {
					 //height: cfg.height,
					 //width: cfg.width,
					videoId: cfg.video,
					events: {'onStateChange': onPlayerStateChange, 'onReady': onPlayerReady}
				});
			}
			$(this).hide();
		});
		return this;
	};
}( jQuery ));