$(document).ready(function(){
	var curid = 0;
	$(".sPic .info td").click(function(){
		$(".sPic .info tr").find("td").removeClass("current");
		$(this).addClass("current");
		curid = $(this).index();
		$(".load_pic").css('display','block');
		$(".bigPic .info img").attr('src',$(this).find("img").attr('bigsrc'));
		$(".bigPic .info img").load(function(){$(".load_pic").css('display','none');});
		$(".show_bg_pic").attr('href',$(this).find("img").attr('bigsrc'));
		$("#img_title_id").html($(this).find("img").attr('title'));
		
		if ($(this).find("img").attr('piclink')==" "){
			$(".bigPic .info a").attr('href',"javascript:void(0);");
		}else{
			$(".bigPic .info a").attr('href',$(this).find("img").attr('piclink'));
		}
	});
	
	$(".bigPic a.btnleft").click(function(){
		curid--;
		if(curid<0){
			//alert("已经是第一张了！");
			curid=0;
		}else{
			$(".load_pic").css('display','block');
			$(".bigPic .info img").attr('src',$(".sPic .info tr").find("td").eq(curid).find("img").attr('bigsrc'));
			$(".bigPic .info img").load(function(){$(".load_pic").css('display','none');});
			$(".show_bg_pic").attr('href',$(".sPic .info tr").find("td").eq(curid).find("img").attr('bigsrc'));
			$("#img_title_id").html($(".sPic .info tr").find("td").eq(curid).find("img").attr('title'));
			
			if ($(".sPic .info tr").find("td").eq(curid).find("img").attr('piclink')==" "){
				$(".bigPic .info a").attr('href',"javascript:void(0);");
			}else{
				$(".bigPic .info a").attr('href',$(".sPic .info tr").find("td").eq(curid).find("img").attr('piclink'));
			}
			
		}
		$(".sPic .info tr").find("td").removeClass("current");
		$(".sPic .info tr").find("td").eq(curid).addClass("current");
		$(".sPic .info").animate({"scrollLeft":"-=123"})
	});
	
	$(".bigPic a.btnright").click(function(){
		curid++;
		if(curid>$(".sPic .info tr").find("td").size()-1){
			//alert("已经是最后一张了！");
			curid=$(".sPic .info tr").find("td").size()-1;
		}else{
			$(".load_pic").css('display','block');
			$(".bigPic .info img").attr('src',$(".sPic .info tr").find("td").eq(curid).find("img").attr('bigsrc'));
			$(".bigPic .info img").load(function(){$(".load_pic").css('display','none');});
			$(".show_bg_pic").attr('href',$(".sPic .info tr").find("td").eq(curid).find("img").attr('bigsrc'));
			$("#img_title_id").html($(".sPic .info tr").find("td").eq(curid).find("img").attr('title'));
			
			if ($(".sPic .info tr").find("td").eq(curid).find("img").attr('piclink')==" "){
				$(".bigPic .info a").attr('href',"javascript:void(0);");
			}else{
				$(".bigPic .info a").attr('href',$(".sPic .info tr").find("td").eq(curid).find("img").attr('piclink'));
			}
		}
		$(".sPic .info tr").find("td").removeClass("current");
		$(".sPic .info tr").find("td").eq(curid).addClass("current");
		$(".sPic .info").animate({"scrollLeft":"+=123"})
	});
	
	$(".sPic a.btnleft").click(function(){
		$(".sPic .info").animate({"scrollLeft":"-=123"})
	});
	
	$(".sPic a.btnright").click(function(){
		$(".sPic .info").animate({"scrollLeft":"+=123"})
	});
});