var urlBegin= TzUniversityContextPath + "/dispatcher";
var clsTzXlColuServiceImpl = "com.tranzvision.gd.TZSitePageBundle.service.impl.TzXlColuServiceImpl";
var clsTzHyColuServiceImpl = "com.tranzvision.gd.TZSitePageBundle.service.impl.TzHyColuServiceImpl";
var clsTzZxColuServiceImpl = "com.tranzvision.gd.TZSitePageBundle.service.impl.TzZxColuServiceImpl";

/*
function checkHisApply(classId,languageCd){
	  var confirmValue = false;
	  var siteid=$("#siteid").val();
	  var tzParams = '{"ComID":"TZ_APPLY_CENTER_COM","PageID":"TZ_APPLY_CENT_PAGE","OperateType":"QF","comParams":{"classId":"'+classId+'","siteid":"'+siteid+'"}}';

		$.ajax({
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				dataType:'json',
				success:function(response){
					  var HaveHisApplyForm = response.comContent.HaveHisApplyForm;
					  var HaveHCBJ=response.comContent.HaveHCBJ;
					  if(HaveHCBJ=="true"){
						if(languageCd == "ENG"){
							alert("Our system has detected existing registration information from an application you previously started, and you are not allowed to  the application for other programs related.");
						}else{
							alert("系统检测到您已经申请了一个项目，不允许再申请相关的其他项目。");
						}
					  }else{
						if(HaveHisApplyForm == "true"){
							if(languageCd == "ENG"){
						  	confirmValue = confirm("Our system has detected existing registration information from an application you previously started. Would you like to copy your previously entered application information into the new application form?");
						  }else{
						  	confirmValue = confirm("系统检测到您曾经报过名，是否从过往报名表中带入历史数据？");
						  }
						}
						 
						if(confirmValue==true){
							location.href =urlBegin+'?classid=appId&APPCOPY=Y&TZ_CLASS_ID='+classId+'&SITE_ID='+siteid;
						}else{
							location.href =urlBegin+'?classid=appId&TZ_CLASS_ID='+classId+'&SITE_ID='+siteid;
						} 
					}
				}   
			});

}
*/

function getQueryString(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
		var r = window.location.search.substr(1).match(reg); 
		if (r != null) return unescape(r[2]); return null; 
}

function BindEnter(obj)
 			{
	 			/*if(obj.keyCode == 13)
	 			{              
					Login();
				}
				*/
				document.onkeydown = function(e){
				var ev = document.all ? window.event : e;
					if(ev.keyCode==13) {
						Login();
				}
			}
}

function backToHome(siteId,orgId){
	var confirmValue = confirm("确认回到首页？");
	if(confirmValue==true){
		window.location = TzUniversityContextPath + "/site/index/" + orgId.toLowerCase() + "/" + siteId;
	}
}

function JumpToColu(siteId,menuId,opentype){

	if(opentype=="index"){
		window.location = TzUniversityContextPath + "/site/index/" + TZ_GD_LOGIN_SITEI_ORG_CODE.toLowerCase() + "/" + TZ_GD_LOGIN_SITEI_ID;
	} else if (opentype=="_blank") {
		window.open(urlBegin+'?classid=askMenu&siteId='+siteId+'&menuId='+menuId+'&oprate=R',"newwindow");
	}else{
		location.href =urlBegin+'?classid=askMenu&siteId='+siteId+'&menuId='+menuId+'&oprate=R';
	}
}


/*首页中初始化活动列表和新闻列表*/
function iniArea(){

$(".autoload").each(function(index,element){

    var siteid=$("#siteid").val();
	var areaid=$(element).attr("area-id");
	if (!areaid)
		{
			areaid="";
		}
	var areaZone=$(element).attr("area-postion");
	if (!areaZone)
		{
			areaZone="";
		}
	var areaType=$(element).attr("area-type");
	if (!areaType)
		{
			areaType="";
	    }
	var page =1;
	var type="0";

	var appCls ="";
	if(areaType =="HY"){
		appCls = clsTzHyColuServiceImpl;
	}

	if(areaType =="XL"){
		appCls = clsTzXlColuServiceImpl;
		
	}

		var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","appCls":"'+appCls+'","page":"'+page+'","pagesize":"5","type":"'+type+'","qureyFrom":"A"}}';

		$.ajax({
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				dataType:'json',
				success:function(response){
							if (response !="false"){
									var tips="暂无相关信息！";
								
									if ($("#lang").val()=="ENG")
									{
										tips="No relevant information!";
									}
							if (!response.comContent.coluItem){$(element).find("ul").html(tips);}else{
									$(element).find("ul").html(response.comContent.coluItem);
								}
								
							}else{
							
							}
				},
	    		failure: function () {
			  		alert(response.state.errdesc);
	    		}    
			});

});

}

function QueryColu(page){
	/*
	var urlParams=getQueryString("tzParams");

		urlParams=$.parseJSON(urlParams);

	var siteid=urlParams.comParams.siteId;

	var menuid=urlParams.comParams.menuId;
*/
	var siteid=getQueryString("siteId");

	var menuid=getQueryString("menuId");

	var qureyFrom="";

	if(menuid){
		qureyFrom="M";
	}else{
		qureyFrom="A";
	}

	var areaid=	getQueryString("areaId");
    if (!areaid)
	{
		areaid="";
	}

	var areaZone=getQueryString("areaZone");
	if (!areaZone)
	{
		areaZone="";
	}

	var areaType=getQueryString("areaType");
	if (!areaType)
	{
		areaType="";
	}


	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'","appCls":"'+clsTzXlColuServiceImpl+'","page":"'+page+'","pagesize":"10","qureyFrom":"'+qureyFrom+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'"}}';
	//var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'","appCls":"'+clsTzXlColuServiceImpl+'","page":"'+page+'","qureyFrom":"'+qureyFrom+'"}}';

$.ajax({
		type:"POST",
		url:urlBegin,
		data:{
			tzParams:tzParams
		},
		dataType:'json',
		success:function(response){
			if (response !="false"){
				$("#countlist").html(response.comContent.coluItem);
				$("#pagecount").html(response.comContent.divPage);
				if (! response.comContent.divPage)
							{
								$("#curentcoutpage").hide();
							}else{
								$("#curentcoutpage").show();
							}
				$("#curentcoutpage").html(response.comContent.nowPageDesc);
			}else{			
		  } 
		},
	    failure: function () {
			  alert(response.state.errdesc);
	    }    
	});

}

function QueryColuZX(page){
	/*
	var urlParams=getQueryString("tzParams");

	urlParams=$.parseJSON(urlParams);

	var siteid=urlParams.comParams.siteId;

	var menuid=urlParams.comParams.menuId;
*/
	var siteid=getQueryString("siteId");

	var menuid=getQueryString("menuId");

    var qureyFrom="";

	if(menuid){
		qureyFrom="M";
	}else{
		qureyFrom="A";
	}


	var areaid=	getQueryString("areaId");
    if (!areaid)
	{
		areaid="";
	}

	var areaZone=getQueryString("areaZone");
	if (!areaZone)
	{
		areaZone="";
	}

	var areaType=getQueryString("areaType");
	if (!areaType)
	{
		areaType="";
	}

    	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'","appCls":"'+clsTzZxColuServiceImpl+'","page":"'+page+'","pagesize":"10","qureyFrom":"M"}}';
   // var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'","appCls":"'+clsTzZxColuServiceImpl+'","page":"'+page+'","qureyFrom":"'+qureyFrom+'"}}';

	$.ajax({
		type:"POST",
		url:urlBegin,
		data:{
			tzParams:tzParams
		},
		dataType:'json',
		success:function(response){
			if (response !="false"){
							$("#countlist").html(response.comContent.coluItem);
							$("#pagecount").html(response.comContent.divPage);
							if (! response.comContent.divPage)
							{
								$("#curentcoutpage").hide();
							}else{
								$("#curentcoutpage").show();
							}
							$("#curentcoutpage").html(response.comContent.nowPageDesc);
						}else{
							
			}
		},
	    failure: function () {
			  alert(response.state.errdesc);
	    }    
	});
}

function QueryColuHY(page,type){


$(".li_tab").each(function(index,element){
	if (index == type )
		{
			$(element).attr({"class":"li_tab now"});
		}else{
			$(element).attr({"class":"li_tab "});
		}
});
/*
	var urlParams=getQueryString("tzParams");

	urlParams=$.parseJSON(urlParams);

	var siteid=urlParams.comParams.siteId;

	var menuid=urlParams.comParams.menuId;
*/

var siteid=getQueryString("siteId");

var menuid=getQueryString("menuId");

	var qureyFrom="";

	if(menuid){
		qureyFrom="M";
	}else{
		qureyFrom="A";
	}
var areaid=	getQueryString("areaId");
    if (!areaid)
	{
		areaid="";
	}

	var areaZone=getQueryString("areaZone");
	if (!areaZone)
	{
		areaZone="";
	}

	var areaType=getQueryString("areaType");
	if (!areaType)
	{
		areaType="";
	}
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'","appCls":"'+clsTzHyColuServiceImpl+'","page":"'+page+'","pagesize":"10","type":"'+type+'","qureyFrom":"'+qureyFrom+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'"}}';

	//var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_COLU_MG_STD","OperateType":"QF","comParams":{"siteId":"'+siteid+'","menuId":"'+menuid+'","appCls":"'+clsTzHyColuServiceImpl+'","page":"'+page+'","type":"'+type+'","qureyFrom":"'+qureyFrom+'"}}';
     
	$.ajax({
		type:"POST",
		url:urlBegin,
		data:{
			tzParams:tzParams
		},
		dataType:'json',
		success:function(response){
			if (response !="false"){
							$("#countlist").html(response.comContent.coluItem);
							$("#pagecount").html(response.comContent.divPage);
							if (! response.comContent.divPage)
							{
								$("#curentcoutpage").hide();
							}else{
								$("#curentcoutpage").show();
							}
							$("#curentcoutpage").html(response.comContent.nowPageDesc);
			}else{
							
			}
		},
	    failure: function () {
			  alert(response.state.errdesc);
	    }    
	});
}

function AreaColu(opt){
	while (true)
	{
		if ($(opt).attr("area-type"))
		{
			break;
		}
		else{
			opt = $(opt).parent();
		}

	}
	/*
	var urlParams=getQueryString("tzParams");

	urlParams=$.parseJSON(urlParams);

	var siteid=urlParams.comParams.siteId;
*/
	var siteid = $("#siteid").val();

	var areaid=	$(opt).attr("area-id");
    if (!areaid)
	{
		areaid="";
	}

	var areaZone=$(opt).attr("area-postion");
	if (!areaZone)
	{
		areaZone="";
	}

	var areaType=$(opt).attr("area-type");
	if (!areaType)
	{
		areaType="";
	}

    var newTab=window.open('about:blank');   
	//newTab.location.href=urlBegin+'?tzParams={"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_COLU_STD","OperateType":"HTML","comParams":{"siteId":"'+siteid+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'"}}';
	newTab.location.href=urlBegin+'?classid=areaColu&siteId='+siteid+'&areaId='+areaid+'&areaZone='+areaZone+'&areaType='+areaType;
}

/*获取二维码*/
function SetImgCode(){
	$('#yzmImg').attr("src", GenCaptchaUrl());
}

function Login(){
	
    var userName = encodeURI($("#userName").val());

	var password = encodeURI($("#password").val());
	//手机登录不需要验证码
	//var yzm = encodeURI($("#yzm").val());
	
	var siteid = encodeURI($("#siteid").val());
	
	var openid = $("#openid").val();

	//if 
	var classid = getQueryString("classid");
	
	if (classid == undefined) {
		classid = "";
	}

	//var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_STU_LOGIN_STD","OperateType":"QF","comParams":{"orgid":"'+$("#jgid").val()+'","typeflg":"login","userName":"'+userName+'","passWord":"'+password+'","yzmCode":"'+yzm+'","siteid":"'+siteid+'","lang":"'+$("#lang").val()+'"}}';
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_STU_LOGIN_STD","OperateType":"QF","comParams":{"orgid":"'+$("#jgid").val()+'","typeflg":"login","userName":"'+userName+'","passWord":"'+password+'","siteid":"'+siteid+'","lang":"'+$("#lang").val()+'","isMobile":"Y"}}';
	$.ajax({
		type:"POST",
		url: TzUniversityContextPath + "/user/login/dologin",
		data:{
			tzParams:tzParams,
			language:$("#lang").val(),
			classIdParams : classid,
			OPENID: openid
		},
		dataType:'json',
		success:function(response){			
			if (response.success == "true") {  
			   $("#errormsg").hide();
			   var _url = response.url + "&from=m";
               window.location.href = _url;
				
            }else{
			   if (response.errorCode=="1")
			   {
				    $("#errormsg").html(response.errorDesc);
					$("#errormsg").show();
					$("#resetbnt").show();
					
			   }else
			   {
				   if (response.errorCode=="2")
				   {
						$("#password").val("");
						//$("#yzm").val("");
						//SetImgCode();
				   }
				   if (response.errorCode=="3")
				   {
					    //$("#yzm").val("");
						//SetImgCode();
				   }

					 $("#errormsg").html(response.errorDesc);
					 $("#errormsg").show();
			   }
			  
			}    
		},
	    failure: function () {

			 $("#errormsg").children("span").innerHTML= "数据请求异常,请检查网络或联系管理员！";
			 $("#errormsg").show();
	    }    
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

/*跳转到注册页*/
function goToRegister(){
	var url = urlBegin+'?tzParams={"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"HTML","comParams": {"siteid":"'+$("#siteid").val()+'","sen":"9"}}';
	
	$.ajax({
		type:"POST",
		url:url,
		/*data:{
			tzParams:tzParams
		},*/
		dataType:'json',
		success:function(response){	
			//console.log(response.url);
			window.location.href= response.url;
		}   
	});
	//window.location.href= TzUniversityContextPath + "/" + TZ_GD_LOGIN_SITEI_ORG_CODE.toLowerCase() + "/enroll.html";
}

/*忘记密码*/
function forgetPwd(jgid){
	jgid=$("#jgid").val();
	
	window.location.href=urlBegin+'?classid=enrollCls&siteid='+$("#siteid").val()+'&orgid='+jgid+'&lang='+$("#lang").val()+'&sen=10';
}

/*重新激活*/
function reactivate(jgid){
    jgid=$("#jgid").val();
	$("#resetbnt").hide();
	window.location.href=urlBegin+'?classid=enrollCls&siteid='+$("#siteid").val()+'&orgid='+jgid+'&lang='+$("#lang").val()+'&sen=3&isMobile=Y';
}


/*获取个人信息*/
function getPerInfCard(){
	var siteid=$("#siteid").val();
	var oprate=$("#operator").val();
	var orgid=$("#jgid").val();
	
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_SEMINFOCARD_STD","OperateType":"JHTML","comParams":{"orgId":"'+orgid+'","siteId":"'+siteid+'","typeflg":"perinfo","isd":"'+TZ_GD_SITEI_ISD+'"}}';
	
	$.ajax({
		type:"POST",
		url:urlBegin,
		data:{
			tzParams:tzParams
		},
		dataType:'json',
		success:function(response){				
			$('#perInfo').prop('innerHTML', response.comContent);
				//原始高度;
                var tz_flds_h = 158;
                 //展示的字段个数;
                    //var tz_fld_num=document.getElementById("tz_fld_num").value;
                    //如果字段个数大于6 ， 则高度按比例扩大;
                    //if (tz_fld_num > 6){
                        //var tmp = tz_fld_num - 6;
                        //tz_flds_h = tz_flds_h + tmp * 25;
                    //}
                    //重新设置高度;
                    $("#perInfo").height(tz_flds_h|"px");

				},
	    		failure: function () {
				
			  		alert(response.state.errdesc);
	    		},
			});

}




/*报名中心*/
function getApplicationCenter(){	
								
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_PI_INFOCARD_STD","OperateType":"HTML","comParams":{"orgId":"'+$("#jgid").val()+'","siteId":"'+$("#siteid").val()+'","typeflg":"perinfo"}}';

	$.ajax({
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				dataType:'json',
				success:function(response){		
					
					$('#perInfoCard').prop('innerHTML', response.comContent);
				},
	    		failure: function () {
				
			  		alert(response.state.errdesc);
	    		}    
			});

}


/*活动报名*/
function hdbm(artId,ele){

	var url = urlBegin+'?tzParams={"ComID":"TZ_APPONL_COM","PageID":"TZ_APPREG_STD","OperateType":"HTML","comParams":{"APPLYID":"'+artId+'"}}';

	tz_apply_click_to_signin_action2(url);

}
/*活动撤销*/
function hdcx(artId,bmrId,ele){
	var url = urlBegin+'?tzParams={"ComID":"TZ_APPONL_COM","PageID":"TZ_APPBAR_VIEW_STD","OperateType":"EJSON","comParams":{"APPLYID":"'+ artId +'","BMRID":"'+ bmrId+'"}}';

	tz_apply_click_to_cancel_reg(url);
}

var up;
function openUpload(){
    up = $.layer({
			type: 2,
			title: false,
			fix: false,
			closeBtn: 2,
			shadeClose: false,
			shade : [0.3 , '#000' , true],
			border : [3 , 0.3 , '#000', true],
			offset: ['50%',''],
			area: ['840px','610px'],
			iframe: {src: urlBegin+'/WEBLIB_GD_USER.TZ_GD_USER.FieldFormula.IScript_uploadPhoHTML'}
		});
	}
function openUpload2(){
    var photoUrl = encodeURI(urlBegin+'?tzParams={"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_UP_PHOTO_STD","OperateType":"HTML","comParams":{"siteId":"'+$("#siteid").val()+'"}}');
    up = $.layer({
        type: 2,
        title: false,
        fix: false,
        closeBtn: 2,
        shadeClose: false,
        shade : [0.3 , '#000' , true],
        border : [3 , 0.3 , '#000', true],
        offset: ['50%',''],
        area: ['840px','610px'],
        iframe: {src:  photoUrl }
    });
}


function openUpload3(){
    var photoUrl = encodeURI(urlBegin+'?tzParams={"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_SEM_UPPHOTO_STD","OperateType":"HTML","comParams":{"siteId":"'+$("#siteid").val()+'"}}');
    /*up = $.layer({
        type: 2,
        title: false,
        fix: false,
        closeBtn: 2,
        shadeClose: false,
        shade : [0.3 , '#000' , true],
        border : [3 , 0.3 , '#000', true],
        offset: ['50%',''],
        area: ['840px','610px'],
        iframe: {src:  photoUrl }
    });*/
    up = layer.open({
    	  type: 2,
    	  title: false,
    	  fixed: false,
    	  closeBtn: 0,
          shadeClose: true,
          shade : [0.3 , '#000' , true],
          border : [3 , 0.3 , '#000', true],
          offset: ['10%',''],
          area: ['840px','610px'],
    	  content: photoUrl
    	}); 
}

function tz_apply_click_to_signin_action2(displayURL) {
    var cover = document.createElement("div");
    cover.className = "cover";
    cover.style.cssText = "background: none repeat scroll 0 0 rgba(0, 0, 0, 0.5);height: 100%;left: 0;position: fixed;top: 0;width: 100%;z-index: 10000;";
    cover.innerHTML = "<div style='margin:-110px auto 0; z-index:10000;width: 700px;padding-bottom: 20px;'><iframe src='" + displayURL + "' style='border: 0 none;width: 100%;padding: 0;height: 700px;' on></iframe></div>";
    document.body.appendChild(cover);
}


function tz_apply_click_to_cancel_reg(tz_apply_action_url){
	$.ajax({
		type: "POST",
		url: tz_apply_action_url,
		//data: Params,
		dataType:"json",
		success: function(data){
			var result = data.comContent.result;
			var resultDesc = data.comContent.resultDesc;
			var artid=data.comContent.artid;
		
			var bmrid=data.comContent.bmrid;
			if(result == "0"){
				alert(resultDesc);
			}else{
				alert(resultDesc);
				removebmzt(artid,bmrid);
				parent.tz_close_float_iframe_apply();
				
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown){
			if(textStatus == "timeout"){
				alert("服务端请求超时。");
				return;
			}else{
				alert("服务端请求发生错误。");
				return;
			}
		}
	});
}


function tz_close_float_iframe_apply() {

    var cover = $(".cover");
    cover.remove();
    $("#cwin").attr("src", $("#cwin").attr("src") + "&1=1");
				
}





function changebmzt(artid,bmrid){

	var buttonLabel="撤销";
	if ($("#lang").val()=="ENG"){
		buttonLabel="Cancel"
	}

	var htmlCont='<a id="hdcx_'+artid+'" href="javascript:void(0);" onclick="hdcx('+artid+','+bmrid+',this)"><div class="bt_blue">'+buttonLabel+'</div></a>';
	$("#hdbm_"+artid).prop('outerHTML', htmlCont);
} 

function removebmzt(artid,bmrid){

	var buttonLabel="报名";
	if ($("#lang").val()=="ENG"){
		buttonLabel="Sign Up"
	}
	
	var htmlCont='<a id="hdbm_'+artid+'" href="javascript:void(0);" onclick="hdbm('+artid+','+bmrid+',this)"><div class="bt_blue">'+buttonLabel+'</div></a>';
	$("#hdcx_"+artid).prop('outerHTML', htmlCont);
}



//~:加载页头统一方法
function LoadHeader( jgId, siteId, opt){
	 					
			if (!opt)
			{
				opt="R";
			}

			var areaid=	$('.top').attr("area-id");
			if (!areaid)
			{
				areaid="";
			}
			var areaZone=$('.top').attr("area-postion");
			if (!areaZone)
			{
				areaZone="";
			}
			var areaType=$('.top').attr("area-type");
			if (!areaType)
			{
				areaType="";
			}

			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_SETED_STD","OperateType":"HTML","comParams":{"orgId":"'+jgId+'","siteId":"'+siteId+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","oprate":"'+opt+'"}}';
			
			$.ajax({
				async: false,
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				success:function(response){
	
					if (response !="false"){
 						$('.top').prop('outerHTML', response);
						if (opt=="D"){
							/*var obj;
							if (obj=new ElBindEvent())
							{
								obj.bindBasic();
							}
							*/
						//	new ElBindEvent().bindBasic();
						}
					}  
				},
	    		failure: function () {
			  		alert(response.state.errdesc);
	    		}    
			});

}

//#:加载页头统一方法




//~:加载页尾统一方法
function LoadFooter( jgId, siteId, opt){
	 					
			if (!opt)
			{
				opt="R";
			}

			var areaid=	$('.foot').attr("area-id");

			if (!areaid)
			{
				areaid="";
			}
			var areaZone=$('.foot').attr("area-postion");
			if (!areaZone)
			{
				areaZone="";
			}
			var areaType=$('.foot').attr("area-type");
			if (!areaType)
			{
				areaType="";
			}

			var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_AREA_SETED_STD","OperateType":"HTML","comParams":{"orgId":"'+jgId+'","siteId":"'+siteId+'","areaId":"'+areaid+'","areaZone":"'+areaZone+'","areaType":"'+areaType+'","oprate":"'+opt+'"}}';
			$.ajax({
				async: false,
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				success:function(response){
	
					if (response !="false"){
 						$('.foot').prop('outerHTML', response);
						if (opt=="D"){
							/*
						   var obj;
							if (obj=new ElBindEvent())
							{
								obj.bindBasic();
							}
							*/
						//new ElBindEvent().bindBasic();
						}
					}  
				},
	    		failure: function () {
			  		alert(response.state.errdesc);
	    		}    
			});

}

//#:加载页尾统一方法



//~:加载菜单统一方法
function LoadMenu( jgId, siteId, opt){
	 					
			var areaid=	$('.main_left').attr("area-id");

								if (!areaid)
								{
									areaid="";
								}
								var areaZone=$('.main_left').attr("area-postion");
								if (!areaZone)
								{
									areaZone="";
								}
								var areaType=$('.main_left').attr("area-type");
								if (!areaType)
								{
									areaType="";
								}

								var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_MENU_SETED_STD","OperateType":"QF","comParams":{"siteId":"'+siteId+'"}}';
	
			$.ajax({
				async: false,
				type:"POST",
				url:urlBegin,
				data:{
					tzParams:tzParams
				},
				dataType:'json',
				success:function(response){
						
						if (response !="false"){
							$('.main_left').children("div").html(response.comContent.divHtml);
						}else{
							
						}
				},
	    		failure: function () {
			  		alert(response.state.errdesc);
	    		}    
			});

}

//#:加载菜单统一方法


//~:加载左侧区域统一方法
function LoadLeftArea( jgId, siteId, opt){
	
	var tzParams = '{"ComID":"TZ_WEBINFO_SHOW_COM","PageID":"TZ_LEFT_AREA_STD","OperateType":"HTML","comParams":{"siteId":"'+siteId+'"}}';
	
	$.ajax({
		async: false,
		type:"POST",
		url:urlBegin,
		data:{
			tzParams:tzParams
		},
		success:function(response){
				if (response){
					$('.zxj_left').append(response);
				}
		},
		failure: function () {
	  		alert(response.state.errdesc);
		}    
	});

}

//#:加载左侧区域统一方法

function tz_onmousemove(){
    $("#tz_edituser").css("display","block");
}
function tz_onmouseout(){
    $("#tz_edituser").css("display","none");
}


//免登录，卢艳添加，2017-4-15
/*function avoidLogin() {
	var siteid = encodeURI($("#siteid").val());
	
	//var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_STU_LOGIN_STD","OperateType":"QF","comParams":{"orgid":"'+$("#jgid").val()+'","typeflg":"login","userName":"'+userName+'","passWord":"'+password+'","siteid":"'+siteid+'","lang":"'+$("#lang").val()+'","isMobile":"Y"}}';
	var tzParams = '{"ComID":"TZ_SITEI_SETED_COM","PageID":"TZ_STU_LOGIN_STD","OperateType":"QF","comParams":{"orgid":"'+$("#jgid").val()+'","siteid":"'+siteid+'","lang":"'+$("#lang").val()+'","isMobile":"Y"}}';
	$.ajax({
		type:"POST",
		url: TzUniversityContextPath + "/user/login/avoidlogin",
		data:{
			tzParams:tzParams
		},
		dataType:'json',
		success:function(response){			
			if (response.success == "true") {  
			   $("#errormsg").hide();
               	window.location.href=response.url;
				
            }else{
			   if (response.errorCode=="1")
			   {
				    $("#errormsg").html(response.errorDesc);
					$("#errormsg").show();
					$("#resetbnt").show();
					
			   }else
			   {
				   if (response.errorCode=="2")
				   {
						$("#password").val("");
						//$("#yzm").val("");
						//SetImgCode();
				   }
				   if (response.errorCode=="3")
				   {
					    //$("#yzm").val("");
						//SetImgCode();
				   }

					 $("#errormsg").html(response.errorDesc);
					 $("#errormsg").show();
			   }
			  
			}    
		},
	    failure: function () {

			 $("#errormsg").children("span").innerHTML= "数据请求异常,请检查网络或联系管理员！";
			 $("#errormsg").show();
	    }    
	});
}
*/