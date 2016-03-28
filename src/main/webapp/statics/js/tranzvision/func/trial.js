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


/*注册页面提交按钮事件*/
function submitEnroll() {
	var _statusFlg = "";
	var _orgNameFlg=$("#status_TZ_ORG_NAME").val();
	var _orgContactNameFlg=$("#status_TZ_CONTACT_NAME").val();
	var _orgContactPhoneFlg=$("#status_TZ_CONTACT_PHONE").val();
	var _orgEmailFlg=$("#status_TZ_EMAIL").val();
	var _yzmFlg=$("#status_yzm").val();
	
	if(_orgNameFlg ==1 || $('#TZ_ORG_NAME').val()==''){
		$('#TZ_ORG_NAME_status').html("<span>请填写机构名称</span>");
		$("#TZ_ORG_NAMEStyle").removeClass("alert_display_none");
		_statusFlg="error";
	}
	
	if(_orgContactNameFlg ==1 || $('#TZ_CONTACT_NAME').val()==''){
		$('#TZ_CONTACT_NAME_status').html("<span>请填写联系人</span>");
		$("#TZ_CONTACT_NAMEStyle").removeClass("alert_display_none");
		_statusFlg="error";
	}
	
	if(_orgContactPhoneFlg ==1 || $('#TZ_CONTACT_PHONE').val()==''){
		$('#TZ_CONTACT_PHONE_status').html("<span>请填写联系电话</span>");
		$("#TZ_CONTACT_PHONEStyle").removeClass("alert_display_none");
		_statusFlg="error";
	}
	
	if(_orgEmailFlg ==1 || $('#TZ_EMAIL').val()==''){
		if ($('#TZ_EMAIL_status').html())
		{
			$("#TZ_EMAILStyle").removeClass("alert_display_none");
			_statusFlg="error";
		}else{
			$('#TZ_EMAIL_status').html("<span>请填写正确的Email地址</span>");
			$("#TZ_EMAILStyle").removeClass("alert_display_none");
			_statusFlg="error";
		}
	}
	
	
	if(_yzmFlg ==1 || $('#yzmEmail').val() ==''){
		$('#yzm_Emailstatus').html("<span>验证码不正确</span>");
		$("#yzmEmailStyle").removeClass("alert_display_none");
		_statusFlg="error";
	}
	
	if(_statusFlg=="error"){
		return false;
	}

	if(_orgNameFlg=="0" && _orgContactNameFlg=="0" && _orgContactPhoneFlg=="0" && _orgEmailFlg=="0" && _yzmFlg=="0"){

		var signupsContent =$("#signupForm").serializeJson();

		var tzParams = '{"ComID":"TZ_ON_TRIAL_COM","PageID":"TZ_ON_TRIAL_STD","OperateType":"QF","comParams":{"data":'+JSON.stringify(signupsContent)+',"sen":"2"}}';
		$.ajax({
			type: "post",
			async :false,
			data:{
				tzParams:tzParams
			},
			url: TzUniversityContextPath + "/dispatcher",
			dataType: "json",
			success: function(result){
				var success = result.comContent;
				if(success == "success"){
					window.open(TzUniversityContextPath + "/trialSuccess",'_self') 
				}else{
					alert(result.state.errdesc);
					create_yzm();
					$("#yzmEmail").val("");
				}
			}
		});
  	}
}


function BindEnter(obj){
	if(obj.keyCode == 13){              
		submitEnroll();
	}
}

$(document).ready(function(){
	create_yzm();
	
	$("#TZ_ORG_NAME").on("blur",function(){
		var val = $("#TZ_ORG_NAME").val();
		if(val !=''){
			if(val.length>1){
				$('#TZ_ORG_NAME_status').html("");
				$('#status_TZ_ORG_NAME').attr("value", 0);
				$('#TZ_ORG_NAMEStyle').addClass("alert_display_none");
			}else{
				$('#TZ_ORG_NAME_status').html("<span>请填写机构名称</span>");
				$('#status_TZ_ORG_NAME').attr("value", 1);
				$('#TZ_ORG_NAMEStyle').removeClass("alert_display_none");
			}
	
		}else{
			$('#TZ_ORG_NAME_status').html("<span>请填写机构名称</span>");
			$('#status_TZ_ORG_NAME').attr("value", 1);
			$('#TZ_ORG_NAMEStyle').removeClass("alert_display_none");
		}
	});
	
	$("#TZ_CONTACT_NAME").on("blur",function(){
		var val = $("#TZ_CONTACT_NAME").val();
		if(val !=''){
			if(val.length>1){
				$('#TZ_CONTACT_NAME_status').html("");
				$('#status_TZ_CONTACT_NAME').attr("value", 0);
				$('#TZ_CONTACT_NAMEStyle').addClass("alert_display_none");
			}else{
				$('#TZ_CONTACT_NAME_status').html("<span>请填写联系人</span>");
				$('#status_TZ_CONTACT_NAME').attr("value", 1);
				$('#TZ_CONTACT_NAMEStyle').removeClass("alert_display_none");
			}
	
		}else{
			$('#TZ_CONTACT_NAME_status').html("<span>请填写联系人</span>");
			$('#status_TZ_CONTACT_NAME').attr("value", 1);
			$('#TZ_CONTACT_NAMEStyle').removeClass("alert_display_none");
		}
	});
	
	
	$("#TZ_CONTACT_PHONE").on("blur",function(){
		var val = $("#TZ_CONTACT_PHONE").val();
		if(val !=''){
			if(val.length>1){
				$('#TZ_CONTACT_PHONE_status').html("");
				$('#status_TZ_CONTACT_PHONE').attr("value", 0);
				$('#TZ_CONTACT_PHONEStyle').addClass("alert_display_none");
			}else{
				$('#TZ_CONTACT_PHONE_status').html("<span>请填写联系电话</span>");
				$('#status_TZ_CONTACT_PHONE').attr("value", 1);
				$('#TZ_CONTACT_PHONEStyle').removeClass("alert_display_none");
			}
	
		}else{
			$('#TZ_CONTACT_PHONE_status').html("<span>请填写联系电话</span>");
			$('#status_TZ_CONTACT_PHONE').attr("value", 1);
			$('#TZ_CONTACT_PHONEStyle').removeClass("alert_display_none");
		}
	});
	
	$("#TZ_EMAIL").on("blur",function(){
		var val = $("#TZ_EMAIL").val();
		if(val!=''){
			var patrn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
			if (!patrn.test(val)){
			   $('#TZ_EMAIL_status').html("<span>Email格式不正确</span>");
			   $('#status_TZ_EMAIL').attr("value", 1);
			   $('#TZ_EMAILStyle').removeClass("alert_display_none");
			}else{
				$('#TZ_EMAIL_status').html("");
				$('#status_TZ_EMAIL').attr("value", 0);
				$('#TZ_EMAILStyle').addClass("alert_display_none");
			}
		}else{
				$('#TZ_EMAIL_status').html("<span>请填写Email</span>");
				$('#status_TZ_EMAIL').attr("value", 1);
				$('#TZ_EMAILStyle').removeClass("alert_display_none");
		}
	});

}).on("click", "#submitbutton", function () {
	submitEnroll();
}).on("click","#changeImgEmail",function(){
	create_yzm();
});


$(document).keyup(function(event){
	  if(event.keyCode ==13){
	    $("#submitbutton").trigger("click");
	  }
});
	

//判断输入的验证码是否一致
function check_yzmEmail(val){
	if(val !=''){
		var tzParams = '{"ComID":"TZ_ON_TRIAL_COM","PageID":"TZ_ON_TRIAL_STD","OperateType":"QF","comParams":{"checkCode":"'+val+'","sen":"1"}}';
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
					create_yzm();
				}
			}
		});
	}else{
		$('#yzm_Emailstatus').html("<span>请输入验证码</span>");
		$('#status_yzm').attr("value", 1);
		$("#yzmEmailStyle").removeClass("alert_display_none");
	}
}

//重新加载验证码
function create_yzm(){
	var _captchaURL = TzUniversityContextPath + "/captcha";
	$('#yzmImgEmail').attr('src',_captchaURL + "?" + Math.random());
}

