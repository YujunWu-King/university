﻿(function($){
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
	_pwdformat_eng:"Password format does not meet the requirements!",
	_pwdformat_zhs:"密码格式不符合要求！",
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

/*注册页面提交按钮事件*/
function submitEnroll() {

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


	var _nameFlg=$("#status_TZ_REALNAME").val();
	var _emailFlg=$("#status_TZ_EMAIL").val();
	var _moblieFlg=$("#status_TZ_MOBILE").val();
	var _passwordFlg=$("#status_TZ_PASSWORD").val();
	var _pwdFlg=$("#status_PASSWORD").val();
	var _yzmFlg=$("#status_yzm").val();
	var _statusFlg="";
	
	
	for (var key in jsonValue){
		if(key=="TZ_REALNAME"){//姓名
			if(jsonValue[key] == "Y"){
				if(_nameFlg !=0 || $('#TZ_REALNAME').val()==''){
					$('#TZ_REALNAME_status').html("<span>"+TipBlank+"</span>");
					$("#TZ_REALNAMEStyle").removeClass("alert_display_none");
					_statusFlg="error";
				}
			}
		}else if(key=="TZ_EMAIL"){//邮箱
			
			if(jsonValue[key] == "Y"){
				if(_emailFlg !=0 || $('#TZ_EMAIL').val()==''){
					if ($('#TZ_EMAIL_status').html())
					{
						$("#TZ_EMAILStyle").removeClass("alert_display_none");
						_statusFlg="error";
					}else{
						$('#TZ_EMAIL_status').html("<span>"+TipEmail+"</span>");
						$("#TZ_EMAILStyle").removeClass("alert_display_none");
						_statusFlg="error";
					}
				}
			}
		}else if(key=="TZ_MOBILE"){//手机
			if(jsonValue[key] == "Y"){
				if(_moblieFlg !=0 || $('#TZ_MOBILE').val()==''){
					$('#TZ_MOBILE_status').html("<span>"+TipPhone+"</span>");
					$("#TZ_MOBILEStyle").removeClass("alert_display_none");
					_statusFlg="error";
				}
			}
		}else if(key=="TZ_PASSWORD"){//密码
			if($('#TZ_PASSWORD').val() ==''){
				$('#TZ_PASSWORD_status').html("<span>"+TipBlank+"</span>");
				$("#TZ_PASSWORDStyle").removeClass("alert_display_none");
				_statusFlg="error";
			}
			if(_pwdFlg != 0){
				$('#TZ_PASSWORD_status').html("<span>"+PwdError+"</span>");
				$("#TZ_PASSWORDStyle").removeClass("alert_display_none");
				_statusFlg="error";
			}
		}else if(key=="TZ_REPASSWORD"){//确认密码
			if($('#TZ_REPASSWORD').val()==''){
				$('#TZ_REPASSWORD_status').html("<span>"+TipBlank+"</span>");
				$("#TZ_REPASSWORDStyle").removeClass("alert_display_none");
				_statusFlg="error";
			}
		}else if(key=="TZ_SCH_CNAME_Country"){
			var schCountry = $("#TZ_SCH_CNAME_Country").attr("ccode");
			if(schCountry!=null&&schCountry!=undefined){
				$("#TZ_SCH_COUNTRY").val(schCountry);
			}			
		}else{//其他
			if(jsonValue[key] == "Y"){
				var val;

				if (key=="TZ_GENDER")
				{
				  val=$('input:radio[name="TZ_GENDER"]:checked').val();
				}else{
				  val=$('#' + key).val();			
				}
				
				if(!val){
					$('#' + key + '_status').html("<span>"+TipBlank+"</span>");
					$('#' + key + 'Style').removeClass("alert_display_none");
					_statusFlg="error";
			   }
			}
		}
	}
	
	if($('#TZ_PASSWORD').val() !='' && ($('#TZ_PASSWORD').val() !=$('#TZ_REPASSWORD').val()|| _passwordFlg !=0)){
		
		$('#TZ_REPASSWORD_status').html("<span>"+PwdCor+"</span>");
		$("#TZ_REPASSWORDStyle").removeClass("alert_display_none");
		_statusFlg="error";
	}

	if($("#yzfs").val() == "M"){
		if(_yzmFlg !=0 || $('#yzm').val() ==''){
			$('#yzm_status').html("<span>"+TipCode+"</span>");
			$("#yzmStyle").removeClass("alert_display_none");
			_statusFlg="error";
		}
	}else{

		if(_yzmFlg !=0 || $('#yzmEmail').val() ==''){

			$('#yzm_Emailstatus').html("<span>"+TipCode+"</span>");
			$("#yzmEmailStyle").removeClass("alert_display_none");
			_statusFlg="error";
		}
	}
   
	//alert(_statusFlg+" "+_nameFlg+" "+_emailFlg+" "+_moblieFlg+" "+_passwordFlg+" "+_yzmFlg);
	if(_statusFlg=="error"){
		return false;
	}

	if(_nameFlg=="0" && _emailFlg=="0" && _moblieFlg=="0" && _passwordFlg=="0" && _yzmFlg=="0"){
		//$('#submitbutton').submit();
		//var signupsContent = $("#signupForm").serialize();
		
		var signupsContent =$("#signupForm").serializeJson();

		var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"QF","comParams":{"data":'+JSON.stringify(signupsContent)+',"orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"2"}}';
		$.ajax({
			type: "post",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "json",
			success: function(result){
				if(result.comContent=='success'){
					//$("#resetbtn").trigger("click");
					//loading();
					window.location.href=result.resultDescr;
				}else{
					alert(result.state.errdesc);
				}
			}
		});
  	}

  	create_yzm();
	$("#TZ_PASSWORD").val('');
	$("#TZ_REPASSWORD").val('');
	$("#status_TZ_PASSWORD").val('');
	$("#status_yzm").val('');
	$("#yzm").val('');
	$("#yzmEmail").val('');
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
	$('#TZ_EMAIL_status').html("<span>请勿使用hotmail或outlook邮箱</span>");
	$("#TZ_EMAILStyle").removeClass("alert_display_none");
	document.getElementById('signupForm').reset();
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
			PwdError = RegisterTips._pwdformat_eng;
			TipCode = RegisterTips._code_eng;
			PwdLen=RegisterTips._pwdl_eng;
		}else{
			TipBlank = RegisterTips._blank_zhs;
			TipEmail = RegisterTips._email_zhs;
			TipPhone = RegisterTips._phone_zhs;
			PwdError = RegisterTips._pwdformat_zhs;
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
								var reg=/^((?!<|>).)*$/;
								if(!reg.test(val)){
								$('#' + fieldId + '_status').html("<span>"+"姓名不能包含<>字符"+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
								}
								else if(val.length>1&&reg.test(val)){
									$('#' + fieldId + '_status').html("");
									$('#status_' + fieldId).attr("value", 0);
									$('#' + fieldId + 'Style').addClass("alert_display_none");
								}else{
									$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
									$('#status_' + fieldId).attr("value", 1);
									$('#' + fieldId + 'Style').removeClass("alert_display_none");
								}
						
						}else{
							if(data[fieldId] == "Y"){
								//$('#' + fieldId).val("请输入真实名字");
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}else if(fieldId=="TZ_EMAIL"){//邮箱
						if(val!=''){
							var patrn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
							if (!patrn.test(val)){
							   $('#' + fieldId + '_status').html("<span>"+TipEmail+"</span>");
							   $('#status_' + fieldId).attr("value", 1);
							   $('#' + fieldId + 'Style').removeClass("alert_display_none");
							}else{
								var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_MAIL_STD","OperateType":"QF","comParams":{"email":"'+val+'","orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"1"}}';
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
											$('#' + fieldId + 'Style').addClass("alert_display_none");
										}else{
											$('#' + fieldId + '_status').html("<span>"+result.state.errdesc+"</span>");
					   						$('#status_' + fieldId).attr("value", 1);
											$('#' + fieldId + 'Style').removeClass("alert_display_none");
										}
									}
								});
							}
						}else{
							if(data[fieldId] == "Y"){
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}else if(fieldId=="TZ_MOBILE"){//手机
						if(val!=''){
							var patrn=/^\d{8}$|^\d{11}$/;
							if (!patrn.test(val)){
								$('#' + fieldId + '_status').html("<span>"+TipPhone+"</span>");
							  	$('#status_' + fieldId).attr("value", 1);
							  	$('#' + fieldId + 'Style').removeClass("alert_display_none");
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
											$('#' + fieldId + 'Style').addClass("alert_display_none");
										}else{
											$('#' + fieldId + '_status').html("<span>"+result.state.errdesc+"</span>");
											$('#status_' + fieldId).attr("value", 1);
											$('#' + fieldId + 'Style').removeClass("alert_display_none");
										}
									}
								});
							}
						}else{
							if(data[fieldId] == "Y"){
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}else if(fieldId=="TZ_COMMENT9"){
						var reg=/^((?!<|>|\").)*$/;
						if(!reg.test(val)){
							$('#' + fieldId + '_status').html("<span>"+"姓名不能包含<>\"字符"+"</span>");
							$('#status_' + fieldId).attr("value", 1);
							$('#' + fieldId + 'Style').removeClass("alert_display_none");
						}else{
							$('#' + fieldId + '_status').html("");
							$('#status_' + fieldId).attr("value", 0);
							$('#' + fieldId + 'Style').addClass("alert_display_none");
						}
					}else if(fieldId=="TZ_COMMENT11"){
						var reg=/^((?!<|>|\").)*$/;
						if(!reg.test(val)){
							$('#' + fieldId + '_status').html("<span>"+"号码中不能包含<>\"字符"+"</span>");
							$('#status_' + fieldId).attr("value", 1);
							$('#' + fieldId + 'Style').removeClass("alert_display_none");
						}else{
							$('#' + fieldId + '_status').html("");
							$('#status_' + fieldId).attr("value", 0);
							$('#' + fieldId + 'Style').addClass("alert_display_none");
						}
					}else if(fieldId=="TZ_PASSWORD"){//密码
						$("#J_PwdTip").css("display","none");
						if(val !=''){
							var pwd = $("#status_PASSWORD").val();
							if(pwd == 0){
								$('#' + fieldId + '_status').html("");
					        	$('#status_' + fieldId).attr("value", 0); 
								$('#' + fieldId + 'Style').addClass("alert_display_none");
							}else{
								$('#' + fieldId + '_status').html(PwdError); 
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");		
							}
							/*
							if(val.length<6 ||val.length>32){
								$('#' + fieldId + '_status').html("密码长度必须大于6小于32位"); 
								$('#status_' + fieldId).attr("value", 1);
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}else{
								$('#' + fieldId + '_status').html("");
					        	$('#status_' + fieldId).attr("value", 0); 
								$('#' + fieldId + 'Style').addClass("alert_display_none");
							}
							*/
						}else {
							$('#' + fieldId + '_status').html(TipBlank);
							$('#status_' + fieldId).attr("value", 1); 
							$('#' + fieldId + 'Style').removeClass("alert_display_none");
						}
					}else if(fieldId=="TZ_REPASSWORD"){//确认密码
						var password =$("#TZ_PASSWORD").val();
						if(password !=''){
							if(password.length<6 ||password.length>32){
								$('#TZ_PASSWORD_status').html(PwdLen); 
								$('#status_TZ_PASSWORD').attr("value", 1);
								$("#TZ_PASSWORDStyle").removeClass("alert_display_none");
							}else if(val ==''){
								$('#TZ_PASSWORD_status').html("");
								$('#TZ_REPASSWORD_status').html(TipBlank);
						     	$('#status_TZ_PASSWORD').attr("value", 1); 
								$('#TZ_PASSWORDStyle').addClass("alert_display_none");
								$('#TZ_REPASSWORDStyle').removeClass("alert_display_none");
							}else if(val == password){
								$('#TZ_PASSWORD_status').html("");
								$('#TZ_REPASSWORD_status').html("");
					        	$('#status_TZ_PASSWORD').attr("value", 0); 
								$('#TZ_PASSWORDStyle').addClass("alert_display_none");
								$('#TZ_REPASSWORDStyle').addClass("alert_display_none");
							}else if(val != password){
								$('#TZ_PASSWORD_status').html("");
								$('#TZ_REPASSWORD_status').html(PwdCor); 
								$('#status_TZ_PASSWORD').attr("value", 1);
								$('#TZ_PASSWORDStyle').addClass("alert_display_none");
								$('#TZ_REPASSWORDStyle').removeClass("alert_display_none");
							}
						}else {
							$('#TZ_PASSWORD_status').html(TipBlank);
							$('#status_TZ_PASSWORD').attr("value", 1); 
							$('#TZ_PASSWORDStyle').removeClass("alert_display_none");
						}
					}else{//其他
						if(data[fieldId] == "Y"){
							if(val != ""){
								$('#' + fieldId + 'Style').addClass("alert_display_none");
							}else{
								$('#' + fieldId + '_status').html("<span>"+TipBlank+"</span>");
								$('#' + fieldId + 'Style').removeClass("alert_display_none");
							}
						}
					}
				})
		    }
		}
	});
	
	setInterval(function(){
		var val = $("#TZ_PASSWORD").val();
		if(val != "" && val != null){
			var patrn;
			var num = 0;
			var num1 = 0;
			var num2 = 0;
			var num3 = 0;
			var num4 = 0;
			var num5=0;
			var num6=0;
			var str1 = 0;
			var str2 = 0;
			var cr;
			for (var i=0;i<val.length ;i++ )
			{
				 cr = val.charAt(i);
						
				
				/*
				patrn= /[^\d\w\s]/;
				if (patrn.test(val)){
					num = num + 1;
				}
				*/
				patrn= /\w/;
				if (patrn.test(cr)){
					num = num + 1;
				}else{
					num5=1;
				}
				
			}
			
				patrn= /[A-Z]/;
				if (patrn.test(val)){
					num6 = num6+1;
				}
				patrn= /[a-z]/;
				if (patrn.test(val)){
					num6 = num6+1;
				}
				patrn= /[0-9]/;
				if (patrn.test(val)){
					num6 = num6+1;
				}
				patrn= /[_]/;
				if (patrn.test(val)){
					num6 = num6+1;
				}

			if(val.length >= 6 && val.length <= 32){
				num1 = 1;				
				$("#J_PwdTip .pw-rule-length .iconfont").html("√");
				$("#J_PwdTip .pw-rule-length .iconfont").css("color","#14c2b3");
			}else{
				num1 = 0;
				$("#J_PwdTip .pw-rule-length .iconfont").html("X");
				$("#J_PwdTip .pw-rule-length .iconfont").css("color","#FF460F");
			}
			
			if(num > 0 && num5==0){
				num2 = 1;
				$("#J_PwdTip .pw-rule-legal .iconfont").html("√");
				$("#J_PwdTip .pw-rule-legal .iconfont").css("color","#14c2b3");
			}else{
				
				num2 = 0;
				$("#J_PwdTip .pw-rule-legal .iconfont").html("X");
				$("#J_PwdTip .pw-rule-legal .iconfont").css("color","#FF460F");
			}
			
			if(num5==0 && num6>1){
				num3 = 1;
				$("#J_PwdTip .pw-rule-multi .iconfont").html("√");
				$("#J_PwdTip .pw-rule-multi .iconfont").css("color","#14c2b3");
			}else{
				num3 = 0;
				$("#J_PwdTip .pw-rule-multi .iconfont").html("X");
				$("#J_PwdTip .pw-rule-multi .iconfont").css("color","#FF460F");
			}
			
			num4 = num1 + num2 + num3;
			if(num4 < 1&&num5!=1){
				$("#J_PwdTip .pw-strength-1").css("background-color","");
				$("#J_PwdTip .pw-strength-2").css("background-color","");
				$("#J_PwdTip .pw-strength-3").css("background-color","");
				
				$("#J_PwdTip .pw-rule-length .iconfont").html("О");
				$("#J_PwdTip .pw-rule-length .iconfont").css("color","");
				$("#J_PwdTip .pw-rule-legal .iconfont").html("О");
				$("#J_PwdTip .pw-rule-legal .iconfont").css("color","");
				$("#J_PwdTip .pw-rule-multi .iconfont").html("О");
				$("#J_PwdTip .pw-rule-multi .iconfont").css("color","");
				
				$("#J_PwdTip .pw-strength .pw-strength-bar em").html("");
				$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","");
				
				$("#J_PwdTip .pw-strength .pw-strength-bar em").html("");
			}
			if(num4 >= 1){
				$("#J_PwdTip .pw-strength-1").css("background-color","#FF460F");
				$("#J_PwdTip .pw-strength-2").css("background-color","");
				$("#J_PwdTip .pw-strength-3").css("background-color","");
				if ($("#lang").val()=="ENG"){
					$("#J_PwdTip .pw-strength .pw-strength-bar em").html("weak");
					$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","#FF460F");
				}else{
					$("#J_PwdTip .pw-strength .pw-strength-bar em").html("低");
					$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","#FF460F");
				}
			}
			if(num4 >= 2){
				$("#J_PwdTip .pw-strength-2").css("background-color","#FF460F");
				$("#J_PwdTip .pw-strength-3").css("background-color","");
				if ($("#lang").val()=="ENG"){
					$("#J_PwdTip .pw-strength .pw-strength-bar em").html("average");
					$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","#0A9E00");
				}else{
					$("#J_PwdTip .pw-strength .pw-strength-bar em").html("中");
					$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","#FF460F");
				}
			}

			if(num5==0 && num4>=3 && num>10){
				$("#J_PwdTip .pw-strength-1").css("background-color","#0A9E00");
				$("#J_PwdTip .pw-strength-2").css("background-color","#0A9E00");
				$("#J_PwdTip .pw-strength-3").css("background-color","#0A9E00");
				if ($("#lang").val()=="ENG"){
				
					$("#J_PwdTip .pw-strength .pw-strength-bar em").html("strong");
					$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","#0A9E00");
				}else{
					$("#J_PwdTip .pw-strength .pw-strength-bar em").html("高");
					$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color","#0A9E00");
				}
			}

			if(num4 >= 3){
				$('#status_PASSWORD').attr("value", 0);
			}else{
				$('#status_PASSWORD').attr("value", 1);
			}
			
		}else{
			$("#J_PwdTip .pw-strength-1").css("background-color","");
			$("#J_PwdTip .pw-strength-2").css("background-color","");
			$("#J_PwdTip .pw-strength-3").css("background-color","");
			
			$("#J_PwdTip .pw-rule-length .iconfont").html("О");
			$("#J_PwdTip .pw-rule-length .iconfont").css("color","");
			$("#J_PwdTip .pw-rule-legal .iconfont").html("О");
			$("#J_PwdTip .pw-rule-legal .iconfont").css("color","");
			$("#J_PwdTip .pw-rule-multi .iconfont").html("О");
			$("#J_PwdTip .pw-rule-multi .iconfont").css("color","");
			
			$("#J_PwdTip .pw-strength .pw-strength-bar em").html("");
		}
		addCheck();
	},200);
	
	$("#TZ_PASSWORD").on("focus",function(){
		var top = $("#TZ_PASSWORD").position().top;
		var left = $("#TZ_PASSWORD").position().left;
		var height = $("#TZ_PASSWORD").height()
		var width = $("#TZ_PASSWORD").width()
		var hi = $("#J_PwdTip").height();
		
		left = left + width + 15;
		top = top + (height - hi) / 2;
		$("#J_PwdTip").css("display","block");
		$("#J_PwdTip").css("left",left);
		$("#J_PwdTip").css("top",top);
	});
	$("#BIRTHDATE").click(function() {
		laydate();
	});
	setInterval(function(){
		if($("#BIRTHDATE").val() != ""){
			$('#BIRTHDATE_status').html("");
			$('#BIRTHDATEStyle').addClass("alert_display_none");
		}
	},300);
	$.each([$("#TZ_COUNTRY"),$("#TZ_COUNTRY_click")],function(i,el){
		el.click(function(e) { 
			$("#ParamCon").val("TZ_COUNTRY");
			s = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['50%',''],
				area: ['830px','610px'],
				iframe: {src: TzUniversityContextPath + encodeURI('/dispatcher?tzParams={"ComID":"TZ_COMMON_COM","PageID":"TZ_COUNTRY_STD","OperateType":"HTML","comParams":{"siteId":"'+$("#siteid").val()+'"}}')}
			});
		});
	});
	$("#TZ_COUNTRY_click").mouseover(function() {
	   	$("#TZ_COUNTRY_click").css("cursor","pointer");
	});
	$("#TZ_COUNTRY_click").mouseout(function() {
	   	$("#TZ_COUNTRY_click").css("cursor","");
	});
	$("#TZ_SCH_CNAME_Country").click(function(e){
		$("#ParamCon").val("TZ_SCH_CNAME_Country");
		s = $.layer({
			type: 2,
			title: false,
			fix: false,
			closeBtn: false,
			shadeClose: false,
			shade : [0.3 , '#000' , true],
			border : [3 , 0.3 , '#000', true],
			offset: ['30%',''],
			area: ['830px','610px'],
			iframe: {src: TzUniversityContextPath + encodeURI('/dispatcher?tzParams={"ComID":"TZ_COMMON_COM","PageID":"TZ_COUNTRY_STD","OperateType":"HTML","comParams":{"siteId":"'+$("#siteid").val()+'"}}')}
		});
	});
	//默认国家为中国
	$("#TZ_SCH_CNAME_Country").val("中国");
	$("#TZ_SCH_CNAME_Country").attr("ccode","CHN");
	$.each([$("#TZ_SCH_CNAME"),$("#TZ_SCH_CNAME_click")],function(i,el){	
		el.click(function(e){
			$("#ParamValue").val("TZ_SCH_CNAME");		
			s = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['50%',''],
				area: ['830px','720px'],
				//iframe: {src: '/tranzvision/colselector_liu.html'}
				iframe: {src: TzUniversityContextPath + encodeURI('/dispatcher?tzParams={"ComID":"TZ_COMMON_COM","PageID":"TZ_SCHOOL_STD","OperateType":"HTML","comParams":{"siteId":"'+$("#siteid").val()+'","Type":"A"}}')}
			});
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
			prov = $.layer({
				type: 2,
				title: false,
				fix: false,
				closeBtn: false,
				shadeClose: false,
				shade : [0.3 , '#000' , true],
				border : [3 , 0.3 , '#000', true],
				offset: ['100px',''],
				area: ['588px','300px'],
				iframe: {src: TzUniversityContextPath + encodeURI('/dispatcher?tzParams={"ComID":"TZ_COMMON_COM","PageID":"TZ_PROVINCE_STD","OperateType":"HTML","comParams":{"TZ_PROV_ID":"'+_prov_id+'","siteId":"'+$("#siteid").val()+'"}}')},
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
				iframe: {src: TzUniversityContextPath + encodeURI('/dispatcher?tzParams={"ComID":"TZ_COMMON_COM","PageID":"TZ_CITY_STD","OperateType":"HTML","comParams":{"OType":"CITY","TZ_CITY_ID":"'+_city_id+'","siteId":"'+$("#siteid").val()+'"}}')},
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
			$("#yzfsMobile").show();
			$("#button_yzfs").show();
		}else{
			$("#yzfsEmail").show();
			$("#yzfsMobile").hide();
			$("#button_yzfs").hide();
		}
    });
	$("#changeImgEmail").on("click",function(){
		create_yzm();
	});
}).on("click", "#submitbutton", function () {
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
			PwdError = RegisterTips._pwdformat_eng;
			TipCode = RegisterTips._code_eng;
			PwdLen=RegisterTips._pwdl_eng;
			PwdCor=RegisterTips._pwdc_eng;
		}else{
			TipBlank = RegisterTips._blank_zhs;
			TipEmail = RegisterTips._email_zhs;
			TipPhone = RegisterTips._phone_zhs;
			PwdError = RegisterTips._pwdformat_zhs;
			TipCode = RegisterTips._code_zhs;
			PwdLen=RegisterTips._pwdl_zhs;
			PwdCor=RegisterTips._pwdc_zhs;
        }

	for (var key in jsonValue){
		if(key=="TZ_REALNAME"){//姓名
			if(jsonValue[key] == "Y"){
				if(_nameFlg !=0 || $('#TZ_REALNAME').val()==''){
					$('#TZ_REALNAME_status').html("<span>"+TipBlank+"</span>");
					$("#TZ_REALNAMEStyle").removeClass("alert_display_none");
					_statusFlg="error";
				}
			}
		}else if(key=="TZ_EMAIL"){//邮箱
			if(jsonValue[key] == "Y"){
				if(_emailFlg !=0 || $('#TZ_EMAIL').val()==''){
					if ($('#TZ_EMAIL_status').html())
					{
						$("#TZ_EMAILStyle").removeClass("alert_display_none");
						_statusFlg="error";
					}else{
						$('#TZ_EMAIL_status').html("<span>"+TipEmail+"</span>");
						$("#TZ_EMAILStyle").removeClass("alert_display_none");
						_statusFlg="error";
					}
				}
			}
		}else if(key=="TZ_MOBILE"){//手机
			if(jsonValue[key] == "Y"){
				if(_moblieFlg !=0 || $('#TZ_MOBILE').val()==''){
					$('#TZ_MOBILE_status').html("<span>"+TipPhone+"</span>");
					$("#TZ_MOBILEStyle").removeClass("alert_display_none");
					_statusFlg="error";
				}
			}
		}else if(key=="TZ_PASSWORD"){//密码
			if($('#TZ_PASSWORD').val() ==''){
				$('#TZ_PASSWORD_status').html("<span>"+TipBlank+"</span>");
				$("#TZ_PASSWORDStyle").removeClass("alert_display_none");
				_statusFlg="error";
			}
			if(_pwdFlg != 0){
				$('#TZ_PASSWORD_status').html("<span>"+PwdError+"</span>");
				$("#TZ_PASSWORDStyle").removeClass("alert_display_none");
				_statusFlg="error";
			}
		}else if(key=="TZ_REPASSWORD"){//确认密码
			if($('#TZ_REPASSWORD').val()==''){
				$('#TZ_REPASSWORD_status').html("<span>"+TipBlank+"</span>");
				$("#TZ_REPASSWORDStyle").removeClass("alert_display_none");
				_statusFlg="error";
			}
		}else if(key=="TZ_SCH_CNAME"){			
			var schCountry = $("#TZ_SCH_CNAME_Country").attr("ccode");
			if(schCountry!=null&&schCountry!=undefined){
				$("#TZ_SCH_COUNTRY").val(schCountry);
			}
			if(jsonValue["TZ_SCH_CNAME"]=="Y"){
				if($("#TZ_SCH_CNAME_Country").val()==''||$("#TZ_SCH_CNAME").val()==''){
					$('#TZ_SCH_CNAME_status').html("<span>"+TipBlank+"</span>");
					$("#TZ_SCH_CNAMEStyle").removeClass("alert_display_none");
					_statusFlg="error";
				}else{
					$("#TZ_SCH_CNAMEStyle").addClass("alert_display_none");
				}				
			}
		}else{//其他
			if(jsonValue[key] == "Y"){
				if($('#' + key).val() == ''){
					$('#' + key + '_status').html("<span>"+TipBlank+"</span>");
					$('#' + key + 'Style').removeClass("alert_display_none");
					_statusFlg="error";
				}
			}
		}
	}
		
	if($('#TZ_PASSWORD').val() !='' && ($('#TZ_PASSWORD').val() !=$('#TZ_REPASSWORD').val()|| _passwordFlg !=0)){
		
		$('#TZ_REPASSWORD_status').html("<span>"+PwdCor+"</span>");
		$("#TZ_REPASSWORDStyle").removeClass("alert_display_none");
		_statusFlg="error";
	}


	if($("#yzfs").val() == "M"){
		if(_yzmFlg !=0 || $('#yzm').val() ==''){
			$('#yzm_status').html("<span>"+TipCode+"</span>");
			$("#yzmStyle").removeClass("alert_display_none");
			_statusFlg="error";
		}
	}else{

		if(_yzmFlg !=0 || $('#yzmEmail').val() ==''){

			$('#yzm_Emailstatus').html("<span>"+TipCode+"</span>");
			$("#yzmEmailStyle").removeClass("alert_display_none");
			_statusFlg="error";
		}
	}
 

	if(_statusFlg=="error"){
		return false;
	}

	if(_nameFlg=="0" && _emailFlg=="0" && _moblieFlg=="0" && _passwordFlg=="0" && _yzmFlg=="0"){
		//$('#submitbutton').submit();
		//var signupsContent = $("#signupForm").serialize();
		
		var signupsContent =$("#signupForm").serializeJson();

		var tzParams = '{"ComID":"TZ_SITE_UTIL_COM","PageID":"TZ_SITE_ENROLL_STD","OperateType":"QF","comParams":{"data":'+JSON.stringify(signupsContent)+',"orgid":"'+strJgid+'","siteId":"'+strSiteId+'","lang":"'+$("#lang").val()+'","sen":"2"}}';
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
							alert("Registration successful.");
						}else{
							alert("注册成功");
						}
					}*/
					window.location.href=result.comContent.jumpurl;
				}else{
					alert(result.state.errdesc);
				}
			}
		});
  	}

  	create_yzm();
	$("#TZ_PASSWORD").val('');
	$("#TZ_REPASSWORD").val('');
	$("#status_TZ_PASSWORD").val('');
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
					$("#yzmStyle").addClass("alert_display_none");
				}else{
					$('#yzm_status').html("<span>"+result.errorDescr+"</span>");
					$('#status_yzm').attr("value", 1);
					$("#yzmStyle").removeClass("alert_display_none");
				}
			}
		});
	}else{
		$('#yzm_status').html("<span>"+TipBlank+"</span>");
		$('#status_yzm').attr("value", 1);
		$("#yzmStyle").removeClass("alert_display_none");
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
					$("#yzmEmailStyle").addClass("alert_display_none");
				}else{
					$('#yzm_Emailstatus').html("<span>"+result.state.errdesc+"</span>");
					$('#status_yzm').attr("value", 1);
					$("#yzmEmailStyle").removeClass("alert_display_none");
				}
			}
		});
	}else{
		$('#yzm_Emailstatus').html("<span>"+TipBlank+"</span>");
		$('#status_yzm').attr("value", 1);
		$("#yzmEmailStyle").removeClass("alert_display_none");
	}
}

//发送验证码
function send_yzm(){
	var yzfs = $("#yzfs").val();
	var email = $("#TZ_EMAIL").val();
	var mobile = $("#TZ_MOBILE").val();
	if(yzfs == "M"){
		if(mobile == ""){
			$('#TZ_MOBILE_status').html("<span>请输入正确的手机号</span>");
		  	$('#status_TZ_MOBILE').attr("value", 1);
		  	$('#TZ_MOBILEStyle').removeClass("alert_display_none");
			alert("请填写正确的手机");
			return;
		}
	}else{
		if(email == ""){
			$('#TZ_EMAIL_status').html("<span>请输入正确的邮箱</span>");
		  	$('#status_TZ_EMAIL').attr("value", 1);
		  	$('#TZ_EMAILStyle').removeClass("alert_display_none");
			alert("请输入正确的邮箱");
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
				atime();
				if ($("#lang").val()=="ENG")
					{
					alert(RegisterTips._SmsSuccess_eng);
				}else{
					alert(RegisterTips._SmsSuccess_zhs);
				}
			}
			else if(result.comContent=="shtime"){
				if ($("#lang").val()=="ENG"){
					alert(RegisterTips._SmsTimeshort_eng);
				}else{
					alert(RegisterTips._SmsTimeshort_zhs);
				}
			}
			else{
					alert(result.state.errdesc);
			}
		}
	});
}

var stop;
var wait=60;
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

/**
*
* 张超  增加对密码的新校验
* 密码必须包含数字和字母
* 密码中数字和字母不能超过两位
* 相同的字母和数字不能超过三位
*/
function addCheck() {
   var TZ_PASSWORD=$("#TZ_PASSWORD").val();
   if(TZ_PASSWORD.length<1){
       $("#J_PwdTip .pw-rule-numAndLetter").css("background-color","");
       $("#J_PwdTip .pw-rule-continuity").css("background-color","");
       $("#J_PwdTip .pw-rule-contain").css("background-color","");

       $("#J_PwdTip .pw-rule-numAndLetter .iconfont").html("О");
       $("#J_PwdTip .pw-rule-numAndLetter .iconfont").css("color","");
       $("#J_PwdTip .pw-rule-continuity .iconfont").html("О");
       $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","");
       $("#J_PwdTip .pw-rule-contain .iconfont").html("О");
       $("#J_PwdTip .pw-rule-contain .iconfont").css("color","");
   }else{
       // console.log(TZ_PASSWORD);
       //判断密码必须包含数字和字母
       var  NuM=0;
       var Letter=0;
       var charNum=null;
       for (var i=0;i<TZ_PASSWORD.length;i++){
           charNum=TZ_PASSWORD.charCodeAt(i);
           if(charNum>47&&charNum<58){
               NuM=-1;
           }else if((charNum>64&&charNum<90)||(charNum>96&&charNum<123)){
               Letter=-1;
           }
       }

       if(NuM*Letter==1){
           $("#J_PwdTip .pw-rule-numAndLetter .iconfont").html("√");
           $("#J_PwdTip .pw-rule-numAndLetter .iconfont").css("color","#14c2b3");
       }else{
           $('#status_PASSWORD').attr("value", 1);
           $("#J_PwdTip .pw-rule-numAndLetter .iconfont").html("X");
           $("#J_PwdTip .pw-rule-numAndLetter .iconfont").css("color","#FF460F");
       }
       //连续数字或字母不能超过两位
       if(TZ_PASSWORD.length>1){
           var front=null;
           var after=null;
           var falg=999999;
           var derail=0;
           var falgStatic=true;
           var newTZ_PASSWORD=TZ_PASSWORD.toLowerCase();
           for(var i=1;i<newTZ_PASSWORD.length;i++){
               front=newTZ_PASSWORD.charCodeAt(i);
               after=newTZ_PASSWORD.charCodeAt(i-1);
               if(after-front==falg){
                   derail++;
               }else{
                   falg=after-front;
                   derail=0;
               }
               if(derail>0&&(falg==-1||falg==1)){
                   $('#status_PASSWORD').attr("value", 1);
                   falgStatic=false;
                   $("#J_PwdTip .pw-rule-continuity .iconfont").html("X");
                   $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","#FF460F");
                   break;
               }else{
                   $("#J_PwdTip .pw-rule-continuity .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","#14c2b3");

               }
           }
           if(falgStatic){
               var arrPass=new Array();
               for(var i=0;i<TZ_PASSWORD.length;i++){
                   arrPass[i]=TZ_PASSWORD.charCodeAt(i);
               }
               arrPass.sort(function(a, b){return a - b});
               if(arrPass.length>2){
                   for(var i=2 ;i<arrPass.length;i++){
                       if(((arrPass[i]>47&&arrPass[i]<58)||(arrPass[i]>64&&arrPass[i]<90)||(arrPass[i]>96&&arrPass[i]<123))&&(arrPass[i]==arrPass[i-1]&&arrPass[i-1]==arrPass[i-2])){
                           $('#status_PASSWORD').attr("value", 1);
                           $("#J_PwdTip .pw-rule-continuity .iconfont").html("X");
                           $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","#FF460F");
                           break;
                       }else{
                           $("#J_PwdTip .pw-rule-continuity .iconfont").html("√");
                           $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","#14c2b3");
                       }
                   }
               }else{
                   $("#J_PwdTip .pw-rule-continuity .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","#14c2b3");
               }
           }
       }else if(TZ_PASSWORD.length>0){
           $("#J_PwdTip .pw-rule-continuity .iconfont").html("√");
           $("#J_PwdTip .pw-rule-continuity .iconfont").css("color","#14c2b3");
       }

       //不能包含手机号和邮箱
       var TZ_EMAIL=$("#TZ_EMAIL").val();
       var TZ_MOBILE=$("#TZ_MOBILE").val();
       // console.log(TZ_EMAIL);
       // console.log(TZ_MOBILE);
       if(!(TZ_MOBILE==""||TZ_MOBILE==undefined||TZ_MOBILE=="undefined")){
           //手机不能为空
           if(!(TZ_EMAIL==""||TZ_EMAIL==undefined||TZ_EMAIL=="undefined")){
               // console.log("邮箱不能为空,手机不为空");
               //邮箱不能为空,手机不为空
               if(TZ_PASSWORD.indexOf(TZ_EMAIL)==-1&&TZ_PASSWORD.indexOf(TZ_MOBILE)==-1){
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#14c2b3");
               }else{
                   $('#status_PASSWORD').attr("value", 1);
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("X");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#FF460F");
               }
           }else{
               // console.log("邮箱为空，手机不为空");
           	//邮箱为空，手机不为空
               if(TZ_PASSWORD.indexOf(TZ_MOBILE)==-1){
               	// console.log("对号");
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#14c2b3");
               }else{
                   // console.log("叉号");
                   $('#status_PASSWORD').attr("value", 1);
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("X");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#FF460F");
               }
           }
       }else{
           //手机为空
           if(!(TZ_EMAIL==""||TZ_EMAIL==undefined||TZ_EMAIL=="undefined")){
               // console.log("邮箱不能为空，手机为空");
               //邮箱不能为空，手机为空
               if(TZ_PASSWORD.indexOf(TZ_EMAIL)==-1){
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#14c2b3");
               }else{
                   $('#status_PASSWORD').attr("value", 1);
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("X");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#FF460F");
               }
           }else{
               // console.log("邮箱为空，手机为空");
               //邮箱为空，手机为空
               $("#J_PwdTip .pw-rule-contain .iconfont").html("√");
               $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#14c2b3");
           }
       }
	}

}

$("#J_PwdTip").ready(function () {
    addTips();
});
function addTips(){
    $("#J_PwdTip .pw-tip-bd .pw-strength").next().append("<div class=\"pw-rule-item pw-rule-numAndLetter\"><i class=\"iconfont\">О</i><span\n" +
        "\t\t\t\t\tdata-phase-id=\"u_ui_numAndLetter\">必须包含字母、数字</span></div>\n" +
        "\t\t\t<div class=\"pw-rule-item pw-rule-continuity\"><i class=\"iconfont\">О</i><span data-phase-id=\"u_ui_continuity\">连续数字、字母和相同数字、字母最多为两位</span></div>\n" +
        "\t\t\t<div class=\"pw-rule-item pw-rule-contain\"><i class=\"iconfont\">О</i><span\n" +
        "\t\t\t\t\tdata-phase-id=\"u_ui_contain\">不能包含手机号和邮箱</span></div>");

}