$(document).ready(function(){
	$('.login-btn').click(function(){
		_login();
	});
});

function validateForm(){
	//验证机构;
	var val = $("#TZ_ORG_NAME").val();
	if(val==''){
		return "机构名称必填";
	}
	//联系人;
	var val = $("#TZ_CONTACT_NAME").val();
	if(val==''){
		return "联系人必填";
	}
	//联系电话;
	var val = $("#TZ_CONTACT_PHONE").val();
	if(val==''){
		return "联系电话必填";
	}
	//验证邮箱;
	var val = $("#TZ_EMAIL").val();
	if(val!=''){
		var patrn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		if (!patrn.test(val)){
			return "邮箱格式不正确";
		}
	}else{
		return "邮箱必填";
	}
	
	return "";
	
}


function _login(){
	var formData = $("#login_form").serialize();
	var validateStr = validateForm();
	if(validateStr == ""){
		$.ajax({
			type: "post",
			async :true,
			data:formData,
			url: TzUniversityContextPath + "/m/trialSubmit",
			dataType: "json",
			beforeSend:function(XMLHttpRequest){
				var loadi = layer.load('提交中,请稍后...');
				layer.area(loadi);
	     	},
			success: function(result){
				var loginFlg=result.resultFlg;
				if(loginFlg=="success"){
					location.href=result.loginURL;
				}else{
					layer.closeAll();
					alert(result.errorDescr);
					$(".cont_tab_ts").html(result.errorDescr);				
				}
			},error:function(XMLHttpRequest,textStatus,errorThrown){
				$(".cont_tab_ts").html("登陆失败！请稍后再试！");
				layer.closeAll();
				alert(errorThrown);
			}
		});
	}else{
		alert(validateStr);
	}

}