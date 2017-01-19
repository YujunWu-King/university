$(document).ready(function(e) {
    $(".sdk-page-nav > li").hover(function(){
		if($(this).hasClass("page-dropdown")){
			$(this).addClass("moon page-moon");
		}else{
			$(this).addClass("moon");
		}
	},function(){
		if($(this).hasClass("page-dropdown")){
			$(this).removeClass("moon page-moon");
		}else{
			$(this).removeClass("moon");
		}
	});
	
});

function DecoratedHeaderMargin(){
	$("#headarea").addClass("header-mt46");
}