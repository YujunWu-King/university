var urlBegin= TzUniversityContextPath + "/dispatcher";

//招生活动和报考通知更多链接点击事件
function hdtzMore(siteId){
	var columnId = $(".zhaos .list .list_on").attr("date-column");
	location.href = TzUniversityContextPath + "/dispatcher?classid=mZsrl&siteId=" + siteId + "&columnId=" + columnId ;
}


//报考通知
function hdtzMore2(siteId){
	var columnId = 0;
	$.each($(".zhaos .list li"),function(i,obj){
		//if ($(obj).html()=="报考通知") {
			 columnId = $(obj).attr("date-column");
		//}
	})
	console.log(columnId);
	location.href = TzUniversityContextPath + "/dispatcher?classid=mZsrl&siteId=" + siteId + "&columnId=" + columnId ;
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
	      area: ['215px','250px'],
	  	 content: rqQrcodeUrl
	 });
}


/*退出系统*/
function Logout(){
	window.location = TzUniversityContextPath + "/user/login/logout";
	/*
	 var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_STU_LOGIN_STD","OperateType":"QF","comParams":{"siteId":"'+$("#siteid").val()+'","typeflg":"logout"}}';
	 $.ajax({
	 type:"POST",
	 url:urlBegin,
	 data:{
	 tzParams:tzParams
	 },
	 dataType:'json',
	 success:function(response){

	 if (response.comContent.success == "true") {
	 window.location.href=response.comContent.url;

	 }else{
	 alert(response.state.errdesc);
	 }
	 },
	 failure: function () {
	 alert(response.state.errdesc);
	 }
	 });
	 */
}


//验证用户是否已成功登录过并未安全退出，则可直接显示登录页面，卢艳添加，2017-4-17
function verifyUser(siteId,orgId) {
	var flag = true;

	var tzParams = '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_WEB_INDEX_STD","OperateType":"verify","comParams":{"orgId":"' + orgId + '","siteId":"' + siteId + '"}}';

	$.ajax({
		type: "POST",
		url: urlBegin,
		data: {
			tzParams: tzParams
		},
		dataType:'json',
		async :false,
		success: function (response) {
			if (response.comContent.success == "true") {
			} else {
				window.location.href = response.comContent.url;
				flag = false;
			}
		},
		failure: function () {
			alert(response.state.errdesc);
			flag = false;
		}
	});

	return flag;
}