//招生活动和报考通知更多链接点击事件
function hdtzMore(siteId){
	var columnId = $(".zhaos .list .list_on").attr("date-column");
	location.href = TzUniversityContextPath + "/dispatcher?classid=mEventNotice&siteId=" + siteId + "&columnId=" + columnId ;
}

//查看电子版条件录取通知书;
function openRqQrcode(appIns){	
	var rqQrcodeUrl = encodeURI(TzUniversityContextPath + '/dispatcher?tzParams={"ComID":"TZ_APPLY_CENTER_COM","PageID":"TZ_PRINT_RQTZ_STD","OperateType":"HTML","comParams":{"appIns":"'+appIns+'"}}');
	up = layer.open({
	  	  type: 2,
	  	  title: false,
	  	  fixed: false,
	  	  closeBtn: 0,
	      shadeClose: true,
	      shade : [0.3 , '#000' , true],
	      border : [3 , 0.3 , '#000', true],
	      offset: ['20%',''],
	      area: ['250px','270px'],
	  	 content: rqQrcodeUrl
	 });
}