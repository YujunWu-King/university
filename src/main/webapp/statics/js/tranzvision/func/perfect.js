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
	//var _emailFlg=$("#status_TZ_EMAIL").val();
	var _moblieFlg=$("#status_TZ_MOBILE").val();
	//var _passwordFlg=$("#status_TZ_PASSWORD").val();
	//var _pwdFlg=$("#status_PASSWORD").val();
	//var _yzmFlg=$("#status_yzm").val();
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
		}else if(key=="TZ_MOBILE"){//手机
			if(jsonValue[key] == "Y"){
				if(_moblieFlg !=0 || $('#TZ_MOBILE').val()==''){
					$('#TZ_MOBILE_status').html("<span>"+TipPhone+"</span>");
					$("#TZ_MOBILEStyle").removeClass("alert_display_none");
					_statusFlg="error";
				}
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

	//alert(_statusFlg+" "+_nameFlg+" "+_emailFlg+" "+_moblieFlg+" "+_passwordFlg+" "+_yzmFlg);
	if(_statusFlg=="error"){
		return false;
	}

	if(_nameFlg=="0" && _moblieFlg=="0"){
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
				iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_COUNTRY_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22siteId%22:%22'+$("#siteid").val()+'%22}}'}
			});
		});
	});
	$("#TZ_COUNTRY_click").mouseover(function() {
	   	$("#TZ_COUNTRY_click").css("cursor","pointer");
	});
	$("#TZ_COUNTRY_click").mouseout(function() {
	   	$("#TZ_COUNTRY_click").css("cursor","");
	});
	$("#TZ_SCH_CNAME_click").click(function(e) {
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
			iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_SCHOOL_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22siteId%22:%22'+$("#siteid").val()+'%22}}'}
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
				iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_PROVINCE_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22:{%22TZ_PROV_ID%22:%22'+_prov_id+'%22,%22siteId%22:%22'+$("#siteid").val()+'%22}}'},
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
				iframe: {src: TzUniversityContextPath + '/dispatcher?tzParams={%22ComID%22:%22TZ_COMMON_COM%22,%22PageID%22:%22TZ_CITY_STD%22,%22OperateType%22:%22HTML%22,%22comParams%22%3A%7B%22OType%22%3A%22CITY%22%2C%22TZ_CITY_ID%22%3A%22'+_city_id+'%22,%22siteId%22:%22'+$("#siteid").val()+'%22%7D%7D'},
			});
		});
    });
    $("#TZ_LEN_CITY_click").mouseover(function() {
	   	$("#TZ_LEN_CITY_click").css("cursor","pointer");
	});
	$("#TZ_LEN_CITY_click").mouseout(function() {
	   	$("#TZ_LEN_CITY_click").css("cursor","");
	});
	
}).on("click", "#submitbutton", function () {
	var _nameFlg=$("#status_TZ_REALNAME").val();	
	var _moblieFlg=$("#status_TZ_MOBILE").val();
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
					$("#TZ_REALNAMEStyle").removeClass("alert_display_none");
					_statusFlg="error";
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
		
	

	if(_statusFlg=="error"){
		return false;
	}

	if(_nameFlg=="0" && _moblieFlg=="0" ){
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
					if($("#yzfs").val() == "M"){
						if ($("#lang").val()=="ENG"){
							alert("Update personal information successful.");
						}else{
							alert("更新个人信息成功");
						}
					}
					window.location.href=result.comContent.jumpurl;
				}else{
					alert(result.state.errdesc);
				}
			}
		});
  	}
});


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