$(function(){	

	var len=$('.bimg li').length;
	var cur=1;var w=$(document).width()-20;
	$('.snum>span:nth-child(1)').html(cur);
	$('.snum>span:nth-child(2)').html(len);
	if(cur==1) $('.bimg .prev').css('display',"none");
	if(cur==len) $('.bimg .next').css('display',"none");

	$(window).bind( 'orientationchange', function(){        
		w=$(document).width()-20;		
		changeW();          
	});
});