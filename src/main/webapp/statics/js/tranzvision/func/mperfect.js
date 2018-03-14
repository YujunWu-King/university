(function($){
$.fn.serializeJson=function(){
var serializeObj={};
var array=this.serializeArray();
var str=this.serialize();
$(array).each(function(){
if(serializeObj[this.name]){
if($.isArray(serializeObj[this.name])){
serializeObj[this.name].push(this.value);
}else{
serializeObj[this.name]=[serializeObj[this.name],this.value];
}
}else{
serializeObj[this.name]=this.value;
}
});
return serializeObj;
};
})(jQuery);



var jsonValue;

var RegisterTips={
	_blank_eng:"It cannot be blank!",
	_blank_zhs:"必填项!",
	_email_eng:"The email address is incorrect!",
	_email_zhs:"邮箱地址不正确！",
	_phone_eng:"The mobile phone is incorrect!",
	_phone_zhs:"手机号码不正确！",
	_pwd_eng:"Stronger password needed.",
	_pwd_zhs:"密码不正确！",
	_pwdl_eng:"Password length must be greater than 6 less than 32!",
	_pwdl_zhs:"密码长度必须大于6小于32位!",
	_pwdc_eng:"Password and confirm password is Inconsistent!",
	_pwdc_zhs:"密码和确认密码不一致！",
	_code_eng:"The security code is incorrect.",
	_code_zhs:"验证码不正确!",
	_send_eng:"Send security code",
	_send_zhs:"发送验证码",
	_send2_eng:"send again",
	_send2_zhs:"重新发送",
	_enring_eng:"wait a moment, please...",
	_enring_zhs:"注册中，请稍等...",
	_register_eng:"register successfully",
	_register_zhs:"注册成功",
	_SmsSuccess_zhs:"短信已发送到您手机，请注意查收！",
	_SmsSuccess_eng:"Sent successfully",
	_SmsTimeshort_zhs:"发送时间间隔太短,请等待一段时间后再试。",
	_SmsTimeshort_eng:"Send too fast,Try again later",
}

var userName;

function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
} 

userName = getQueryString("userName");
console.log(userName);

function layerMsg(content){
	layer.open({
	    content: content,
	    skin: 'msg',
	    time: 2 //2秒后自动关闭
	});	
}

function BindEnter(obj)
{
	if(obj.keyCode == 13)        
	{              
		 $("#submitbutton").trigger("click");
	}
}



var jsonValue;
$(document).ready(function(){
	//document.getElementById('signupForm').reset();
	create_yzm();
	var fieldParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"GETNOWFIELD","comParams":{"strJgid":"'+strJgid+'","siteId":"'+strSiteId+'"}}';
	//加载页面字段
	$.ajax({
		type:"post",
		//data:"strJgid=" + strJgid,
		data:{
			tzParams:fieldParams
		},
		dataType:"json",
		async:false,
		url: TzUniversityContextPath + "/dispatcher",
		success:function(data){
		data = data.comContent;
		var TipBlank = "";
		var TipEmail = "";
		var TipPhone = "";
		var PwdError = "";
		var TipCode = "";
		var PwdLen ="";
		var PwdCor="";
		if ($("#lang").val()=="ENG")
		{
			TipBlank = RegisterTips._blank_eng;
			TipEmail = RegisterTips._email_eng;
			TipPhone = RegisterTips._phone_eng;
			PwdError = RegisterTips._pwd_eng;
			TipCode = RegisterTips._code_eng;
			PwdLen=RegisterTips._pwdl_eng;
			PwdCor=RegisterTips._pwdc_eng;
		}else{
			TipBlank = RegisterTips._blank_zhs;
			TipEmail = RegisterTips._email_zhs;
			TipPhone = RegisterTips._phone_zhs;
			PwdError = RegisterTips._pwd_zhs;
			TipCode = RegisterTips._code_zhs;
			PwdLen=RegisterTips._pwdl_zhs;
			PwdCor=RegisterTips._pwdc_zhs;
        }

			jsonValue = data;
			for (var key in data){
				$("#" + key).on("blur",function(){
					var val = this.value;					
					var fieldId = this.id;
					if(fieldId=="TZ_REALNAME"){//姓名
						if(val !=''){
								if(val.length>1){
									$('#' + fieldId + '_status').html("");
									$('#status_' + fieldId).attr("value", 0);
									//$('#' + fieldId + '_status').hide();
									changeImg(fieldId,"S");
								}else{
									$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
									$('#status_' + fieldId).attr("value", 1);
									//$('#' + fieldId + '_status').show();
									changeImg(fieldId,"F");
								}
						
						}else{
							if(data[fieldId] == "Y"){
								//$('#' + fieldId).val("请输入真实名字");
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								//$('#' + fieldId + '_status').show();
								changeImg(fieldId,"F");
							}
						}
					}else if(fieldId=="TZ_MOBILE"){//手机
						if(val!=''){
							var patrn=/^\d{8}$|^\d{11}$/;
							if (!patrn.test(val)){
								$('#' + fieldId + '_status').html("<span>"+TipPhone+"</span>");
							  	$('#status_' + fieldId).attr("value", 1);
							  	//$('#' + fieldId + '_status').show();
							  	changeImg(fieldId,"F");
							}else{
								var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_SMS_STD","OperateType":"QF","comParams":{"phone":"'+val+'","orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"1"}}';
								$.ajax({
									type: "get",
									async :false,
									data:{
										tzParams:tzParams
									},
									url: TzUniversityContextPath + "/dispatcher",
									dataType: "json",
									success: function(result){
										if(result.comContent =="success"){
											$('#' + fieldId + '_status').html("");
											$('#status_' + fieldId).attr("value", 0); 
											//$('#' + fieldId + '_status').hide();
											changeImg(fieldId,"S");
										}else{
											$('#' + fieldId + '_status').html("<span>"+result.state.errdesc+"</span>");
											$('#status_' + fieldId).attr("value", 1);
											//$('#' + fieldId + '_status').show();
											changeImg(fieldId,"F");
										}
									}
								});
							}
						}else{
							if(data[fieldId] == "Y"){
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								//$('#' + fieldId + '_status').show();
								changeImg(fieldId,"F");
							}
						}					
					}else if(fieldId=="TZ_SCH_CNAME"){
						
					}else if(fieldId=="TZ_LEN_PROID"){
						
					}else{//其他
						if(data[fieldId] == "Y"){
							if(val != ""){
								//$('#' + fieldId + '_status').hide();
								changeImg(fieldId,"S");
							}else{
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								//$('#' + fieldId + '_status').show();
								changeImg(fieldId,"F");
							}
						}
					}
				});
				//为select添加事件
				if(key=="TZ_COMP_INDUSTRY"){
					$("#" + key).on("change",function(){
						var selectFieldId = this.id;
						var sValue = this.value;
						if(sValue!=""){
							changeImg(selectFieldId,"S");
						}else{
							changeImg(selectFieldId,"F");
						}
					});
				}
		    }
		}
	});
		
	
	$("#BIRTHDATE").click(function() {
		laydate({
			elem:"#BIRTHDATE"
		});
	});
	setInterval(function(){
		if($("#BIRTHDATE").val() != ""){
			$('#BIRTHDATE_status').html("");
			$('#BIRTHDATEStyle').hide();
		}
	},300);
	
	/**取消input***/
	$.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click"),$("#TZ_SCH_CNAME_Country"),$("#TZ_SCH_CNAME"),$("#TZ_LEN_PROID")],function(i,el){	
		el.focus(function(){
        document.activeElement.blur();
    })
   });
		
	$.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click"),$("#TZ_SCH_CNAME_Country")],function(i,el){	
		el.click(function(e) { 
			$("#ParamCon").val(el.attr("id"));
			var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_COUNTRY_STD","OperateType":"HTML","comParams":{"orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"2"}}';
			$.ajax({
				type: "post",
				async :false,
				data:{
					tzParams:tzParams
				},
				url: TzUniversityContextPath + "/dispatcher",
				dataType: "html",
				success: function(result){
					$("#searchCountry").html("");
					$("#searchCountry").html(result);
					$("#body").css("position","fixed");
					$("#before").hide();
				    $("#searchCountry").fadeIn("slow"); 
					/*$(".shade").show();
				    $("#searchCountry").show();*/
				}
			});
		});
	});
	$("#TZ_COUNTRY_click").mouseover(function() {
	   	$("#TZ_COUNTRY_click").css("cursor","pointer");
	});
	$("#TZ_COUNTRY_click").mouseout(function() {
	   	$("#TZ_COUNTRY_click").css("cursor","");
	});
	//默认国家为中国
	$("#TZ_SCH_CNAME_Country").val("中国");
	$("#TZ_SCH_CNAME_Country").attr("ccode","CHN");
	$("#TZ_SCH_CNAME").click(function(e) {
		$("#ParamCon").val("TZ_SCH_CNAME");
		var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_SCHOOL_STD","OperateType":"HTML","comParams":{"orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","Type":"A"}}';
		$.ajax({
			type: "post",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "html",
			success: function(result){
				$("#searchSchool").html("");
				$("#searchSchool").html(result);
				$("#body").css("position","fixed");
				$("#before").hide();
				$("#searchSchool").fadeIn("slow");
				/*$(".shade").show();
			    $("#searchSchool").show();*/
			}
		});
    });
    $("#TZ_SCH_CNAME_click").mouseover(function() {
	   	$("#TZ_SCH_CNAME_click").css("cursor","pointer");
	});
	$("#TZ_SCH_CNAME_click").mouseout(function() {
	   	$("#TZ_SCH_CNAME_click").css("cursor","");
	});
	$.each([$("#TZ_LEN_PROID"),$("#TZ_LEN_PROID_click")],function(i,el){
		el.click(function(e) { 
			var _prov_id = "TZ_LEN_PROID";
			$("#ParamCon").val(el.attr("id"));
			var tzParams = '{"ComID":"TZ_COMMON_COM","PageID":"TZ_M_PROVINCE_STD","OperateType":"HTML","comParams":{"TZ_PROV_ID":"'+_prov_id+'","siteId":"'+strSiteId+'"}}';
			$.ajax({
				type: "post",
				async :false,
				data:{
					tzParams:tzParams
				},
				url: TzUniversityContextPath + "/dispatcher",
				dataType: "html",
				success: function(result){
					$("#searchState").html("");
					$("#searchState").html(result);
					$("#body").css("position","fixed");
					$("#before").hide();
				    $("#searchState").fadeIn("slow");
					/*$(".shade").show();
				    $("#searchState").show();*/
				}
			});
		});
    });
    $("#TZ_LEN_PROID_click").mouseover(function() {
	   	$("#TZ_LEN_PROID_click").css("cursor","pointer");
	});
	$("#TZ_LEN_PROID_click").mouseout(function() {
	   	$("#TZ_LEN_PROID_click").css("cursor","");
	});
	$.each([$("#TZ_LEN_CITY"),$("#TZ_LEN_CITY_click")],function(i,el){
		el.click(function(e) { 
			var _city_id = "TZ_LEN_CITY";
			i2 = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['100px',''],
				area: ['588px','400px'],
				iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams='+encodeURIComponent('{"ComID":"TZ_COMMON_COM","PageID":"TZ_CITY_STD","OperateType":"HTML","comParams":{"OType":"CITY","TZ_CITY_ID":"'+_city_id+'","siteId":"'+$("#siteid").val()+'"}}')},
			});
		});
    });
    $("#TZ_LEN_CITY_click").mouseover(function() {
	   	$("#TZ_LEN_CITY_click").css("cursor","pointer");
	});
	$("#TZ_LEN_CITY_click").mouseout(function() {
	   	$("#TZ_LEN_CITY_click").css("cursor","");
	});
	$("#yzfs").change(function() {
		var yzfs = $("#yzfs").val();
		if(yzfs == "M"){
			$("#yzfsEmail").hide();
			$("#yzm_Emailstatus").hide();
			$("#yzfsMobile").show();
			$("#button_yzfs").show();
			$("#yzm_status").show();
		}else{
			$("#yzfsEmail").show();
			$("#yzm_Emailstatus").show();
			$("#yzfsMobile").hide();
			$("#button_yzfs").hide();
			$("#yzm_status").hide();
		}
    });
	$("#changeImgEmail").on("click",function(){
		create_yzm();
	});
}).on("click", "#submitbutton", function () {
	console.log($("#signupForm").serializeJson());
	var _nameFlg=$("#status_TZ_REALNAME").val();
	var _emailFlg=$("#status_TZ_EMAIL").val();
	var _moblieFlg=$("#status_TZ_MOBILE").val();
	var _passwordFlg=$("#status_TZ_PASSWORD").val();
	var _pwdFlg=$("#status_PASSWORD").val();
	var _yzmFlg=$("#status_yzm").val();
	var _statusFlg="";
	var TipBlank = "";
	var TipEmail = "";
	var TipPhone = "";
	var PwdError = "";
	var TipCode = "";
	var PwdLen ="";
	var PwdCor="";
	if ($("#lang").val()=="ENG")
	{
		TipBlank = RegisterTips._blank_eng;
		TipEmail = RegisterTips._email_eng;
		TipPhone = RegisterTips._phone_eng;
		PwdError = RegisterTips._pwd_eng;
		TipCode = RegisterTips._code_eng;
		PwdLen=RegisterTips._pwdl_eng;
		PwdCor=RegisterTips._pwdc_eng;
	}else{
		TipBlank = RegisterTips._blank_zhs;
		TipEmail = RegisterTips._email_zhs;
		TipPhone = RegisterTips._phone_zhs;
		PwdError = RegisterTips._pwd_zhs;
		TipCode = RegisterTips._code_zhs;
		PwdLen=RegisterTips._pwdl_zhs;
		PwdCor=RegisterTips._pwdc_zhs;
    }
	for (var key in jsonValue){
		if(key=="TZ_REALNAME"){//姓名
			if(jsonValue[key] == "Y"){
				if(_nameFlg !=0 || $('#TZ_REALNAME').val()==''){
					$('#TZ_REALNAME_status').html("<span>"+TipBlank+"</span>");
					//$("#TZ_REALNAMEStyle").show();
					changeImg(key,"F");
					_statusFlg="error";
				}
			}
		}else if(key=="TZ_MOBILE"){//手机
			if(jsonValue[key] == "Y"){
				if(_moblieFlg !=0 || $('#TZ_MOBILE').val()==''){
					$('#TZ_MOBILE_status').html("<span>"+TipPhone+"</span>");
					//$("#TZ_MOBILE_status").show();
					changeImg(key,"F");
					_statusFlg="error";
				}
			}else{
				//$('#' + key + '_status').hide();
				changeImg(key,"S");
			}
		}else if(key=="TZ_SCH_CNAME_Country"){
			var schCountry = $("#TZ_SCH_CNAME_Country").attr("ccode");
			if(schCountry!=null&&schCountry!=undefined){
				$("#TZ_SCH_COUNTRY").val(schCountry);
			}			
		}else{//其他
			if(jsonValue[key] == "Y"){
				if($('#' + key).val() == ''){
					$('#' + key + '_status').html("<span>"+TipBlank+"</span>");
					//$('#' + key + '_status').show();
					changeImg(key,"F");
					_statusFlg="error";
				}else{
					//$('#' + key + '_status').hide();
					changeImg(key,"S");
				}
			}
		}
	}
	

	if($("#yzfs").val() == "M"){
		if(_yzmFlg !=0 || $('#yzm').val() ==''){
			$('#yzm_status').html("<span>"+TipCode+"</span>");
			$("#yzm_status").show();
			_statusFlg="error";
		}
	}else{

		if(_yzmFlg !=0 || $('#yzmEmail').val() ==''){

			$('#yzm_Emailstatus').html("<span>"+TipCode+"</span>");
			$("#yzm_Emailstatus").show();
			_statusFlg="error";
		}
	}
 

	if(_statusFlg=="error"){
		return false;
	}
	 
     console.log( _nameFlg+":"+_moblieFlg+":"+_yzmFlg)
	if(_nameFlg=="0"&& _moblieFlg=="0" && _yzmFlg=="0"){
		//$('#submitbutton').submit();
		//var signupsContent = $("#signupForm").serialize();
		
		var signupsContent =$("#signupForm").serializeJson();
		
		console.log(signupsContent);

		var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"QF","comParams":{"data":'+JSON.stringify(signupsContent)+',"orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"5","isMobile":"Y","userName":"' + userName + '"}}';
		$.ajax({
			type: "post",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "json",
			success: function(result){
					
				if(result.comContent.result=='success'){
					//$("#resetbtn").trigger("click");
					//loading();
					/*if($("#yzfs").val() == "M"){
						if ($("#lang").val()=="ENG"){
							layerMsg("Save successful.");
						}else{
							layerMsg("保存成功");
						}
					}*/
					window.location.href=result.comContent.jumpurl;
				}else{
					layerMsg(result.state.errdesc);
				}
			}
		});
  	}

  	create_yzm();
	$("#status_yzm").val('');
	$("#yzm").val('');
	$("#yzmEmail").val('');
});


/*
$(document).keyup(function(event){
  if(event.keyCode ==13){
    $("#submitbutton").trigger("click");
  }
});
*/
var i;
function loading(){
	var Tipenr="";
	if ($("#lang").val()=="ENG")
		{
			Tipenr=RegisterTips._enring_eng;
			
		}else{
			Tipenr = RegisterTips._enring_zhs;

        }
	var loadi = layer.load(Tipenr);
	layer.area(loadi,"top:200px");
}

//判断输入的验证码是否一致
function check_yzm(val){
	var TipBlank = "";
		if ($("#lang").val()=="ENG")
		{
			TipBlank = RegisterTips._blank_eng;
		}else{
			TipBlank = RegisterTips._blank_zhs;
        }

	var yzfs = $("#yzfs").val();
	var email = $("#TZ_EMAIL").val();
	var mobile = $("#TZ_MOBILE").val();
	if(val !=''){
		var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"CODEVALIDATOR","comParams":{"yzm":"'+val+'","yzfs":"'+yzfs+'","TZ_EMAIL":"'+email+'","TZ_MOBILE":"'+mobile+'","siteId":"'+strSiteId+'","strJgid":"'+strJgid+'"}}';
		$.ajax({
			type: "get",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "json",
			success: function(result){
				result = result.comContent;
				if(result.resultFlg =="success"){
					$('#yzm_status').html("");
					$('#status_yzm').attr("value", 0); 
					$("#yzm_status").hide();
				}else{
					$('#yzm_status').html("<span>"+result.errorDescr+"</span>");
					$('#status_yzm').attr("value", 1);
					$("#yzm_status").show();
				}
			}
		});
	}else{
		$('#yzm_status').html("<span>"+TipBlank+"</span>");
		$('#status_yzm').attr("value", 1);
		$("#yzm_status").show();
	}
}

//重新加载验证码
function create_yzm(){
	var _captchaURL = TzUniversityContextPath + "/captcha";
	$('#yzmImgEmail').attr('src',_captchaURL + "?" + Math.random());
}

//判断输入的验证码是否一致
function check_yzmEmail(val){
	var TipBlank = "";
		if ($("#lang").val()=="ENG")
		{
			TipBlank = RegisterTips._blank_eng;
		}else{
			TipBlank = RegisterTips._blank_zhs;
        }

	if(val !=''){
		var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"QF","comParams":{"checkCode":"'+val+'","orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"1"}}';
		$.ajax({
			type: "get",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "json",
			success: function(result){
				if(result.comContent =="success"){
					$('#yzm_Emailstatus').html("");
					$('#status_yzm').attr("value", 0);
					$("#yzmEmailStyle").hide();
				}else{
					$('#yzm_Emailstatus').html("<span>"+result.state.errdesc+"</span>");
					$('#status_yzm').attr("value", 1);
					$("#yzmEmailStyle").show();
				}
			}
		});
	}else{
		$('#yzm_Emailstatus').html("<span>"+TipBlank+"</span>");
		$('#status_yzm').attr("value", 1);
		$("#yzmEmailStyle").show();
	}
}

//发送验证码
function send_yzm(_this){
	var yzfs = $("#yzfs").val();
	var email = $("#TZ_EMAIL").val();
	var mobile = $("#TZ_MOBILE").val();
	if(yzfs == "M"){
		if(mobile == ""){
			$('#TZ_MOBILE_status').html("<span>请输入正确的手机号</span>");
		  	$('#status_TZ_MOBILE').attr("value", 1);
		  	$('#TZ_MOBILEStyle').show();
			layerMsg("请填写正确的手机");
			return;
		}
	}else{
		if(email == ""){
			$('#TZ_EMAIL_status').html("<span>请输入正确的邮箱</span>");
		  	$('#status_TZ_EMAIL').attr("value", 1);
		  	$('#TZ_EMAILStyle').show();
			layerMsg("请输入正确的邮箱");
			return;
		}
	}
	var tzParams = 	'{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_SMS_STD","OperateType":"QF","comParams":{"phone":"'+mobile+'","orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'",	"sen":"2"}}';
	$.ajax({
		type:"post",
		dataType:"json",
		async:false,
		data: {
			tzParams:tzParams
		},
		url: TzUniversityContextPath + "/dispatcher",
		success:function(result){
			if(result.comContent=="success"){
				time(_this);
				if ($("#lang").val()=="ENG")
					{
					layerMsg(RegisterTips._SmsSuccess_eng);
				}else{
					layerMsg(RegisterTips._SmsSuccess_zhs);
				}
			}
			else if(result.comContent=="shtime"){
				if ($("#lang").val()=="ENG"){
					layerMsg(RegisterTips._SmsTimeshort_eng);
				}else{
					layerMsg(RegisterTips._SmsTimeshort_zhs);
				}
			}
			else{
					layerMsg(result.state.errdesc);
			}
		}
	});
}

var stop;
var wait=60;
function time(o) {
	if (wait == 0) {
	   o.removeAttribute("disabled");   
	   o.value="获取短信密码";
	   wait = 60;
	} else { 
	   o.setAttribute("disabled", true);
	   o.value="重新发送(" + wait + ")";
	   wait--;
	   setTimeout(function() {
		   time(o)
	   },1000)
	}
}

function atime() {
	var sendCode="";
	var sendAgain="";
     if ($("#lang").val()=="ENG")
		{
			sendCode=RegisterTips._send_eng;
			sendAgain = RegisterTips._send2_eng;
		}else{
			sendCode = RegisterTips._send_zhs;
			sendAgain = RegisterTips._send2_zhs;
        }

	var obj;
	var yzfs = $("#yzfs").val();
	if(yzfs == "M"){obj = $("#TZ_MOBILE");}
		else{obj = $("#TZ_EMAIL");}
		
	if(wait <= 0){
		$("#send_Verification").css("cursor","pointer");
		$("#send_Verification").css("opacity","0.8");
		$("#send_Verification").removeAttr("disabled");
		obj.removeAttr("readOnly");
		$("#send_Verification").css("color","#000");			
		$("#send_Verification").val(sendCode);
		wait =60;
	} else{
		$("#send_Verification").css("cursor","default");
		$("#send_Verification").prop("disabled","true");
		$("#send_Verification").css("opacity","1");
		$("#send_Verification").css("color","#A9A9A9");
		$("#send_Verification").val(sendAgain+"("+wait+")");
		obj.prop("readOnly","true");
		if(wait>=0){
			wait=wait-1;}
		stop = setTimeout("atime()",1000);
	}
}

function btime() {
	var sendCode="";
	
     if ($("#lang").val()=="ENG")
		{
			sendCode=RegisterTips._send_eng;
			
		}else{
			sendCode = RegisterTips._send_zhs;

        }

	$("#send_Verification").css("cursor","pointer");
	$("#send_Verification").css("opacity","0.8");
	$("#send_Verification").prop("disabled", "false");		
	$("#send_Verification").val(sendCode);
	wait =60;
}

//手机号码长度
function len(str){
	var i,sum;
	sum=0;
	for(i=0;i<str.length;i++){
		if ((str.charCodeAt(i)>=0) && (str.charCodeAt(i)<=255)) sum=sum+1;
		else sum=sum+2;
	}
	return sum;
}

//验证信息通过或不通过后，更改提示
function changeImg(strId,strSuccessFlg){
	var divBody = $("#" + strId).parent().parent();
	var tipImg = divBody.find("img");
	if(tipImg!=null&&tipImg!=undefined){
		var successImg = TzUniversityContextPath + "/statics/css/website/m/images/reg_right.png";
		var failImg = TzUniversityContextPath + "/statics/css/website/m/images/reg_wrong.png";
		if(strSuccessFlg=="S"){
			tipImg.attr("src",successImg);
			$('#' + strId + '_status').hide();
		}else if(strSuccessFlg=="F"){
			tipImg.attr("src",failImg);
			$('#' + strId + '_status').show();
		}
	}
}
/***页面默认设置**/
var mperjsonValue;
$(document).ready(function(){
		//加载页面字段
		var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"QF","comParams":{"sen":"6","isMobile":"Y","userName":"' + userName + '"}}';
	$.ajax({
		type:"post",
		data:{
			tzParams:tzParams
		},
		dataType:"json",
		async:false,
		url: TzUniversityContextPath + "/dispatcher",
		success:function(data){
		mperjsonValue=data.comContent;
		console.log(mperjsonValue);
		
		$("#TZ_SCH_CNAME_Country").val(mperjsonValue.strCounttryName);
		$("#TZ_SCH_CNAME_Country").css("color","#333");
		$("#TZ_SCH_CNAME").val(mperjsonValue.strschoolName);
		$("#TZ_SCH_CNAME").css("color","#333");
		$("#TZ_LEN_PROID").val(mperjsonValue.strPrivence);
		$("#TZ_LEN_PROID").css("color","#333");
		$("#TZ_COMPANY_NAME").val(mperjsonValue.strCompanName);
		$("#TZ_COMP_INDUSTRY").val(mperjsonValue.strIndustry);
		$("#TZ_SCH_CNAME_Country").attr("ccode",mperjsonValue.strCounttry);
		
		
		
		}
	})
})