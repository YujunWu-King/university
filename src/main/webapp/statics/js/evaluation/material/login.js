$(function(){
	
	function setValidationCodeImg(){
		$("#yzmImg").attr("src",ContextPath + "/captcha" + "?" + Math.random());
	}
	
	function login(){
		
	    var userName = encodeURI($("#userName").val());

		var password = encodeURI($("#password").val());

		var yzm = encodeURI($("#yzm").val());
		
		if($.trim(userName)==""){
			$("#errormsg").children("span").html("请输入帐号！");
			$("#errormsg").show();
			return; 
		}
		
		if($.trim(password)==""){
			$("#errormsg").children("span").html("请输入密码！");
			$("#errormsg").show();
			return; 
		}
		
		if($.trim(yzm)==""){
			$("#errormsg").children("span").html("请输入验证码！");
			$("#errormsg").show();
			return; 
		}
		
		var index = layer.load(1, {
			  shade: [0.5,'#999']
			});
		
		$.ajax({
			type:"POST",
			url: ContextPath + "/evaluation/login",
			data:{
				orgId:OrgCode,
				userName:userName,
				password:password,
				yzm:yzm,
				type:'material'
			},
			dataType:'json',
			success:function(response){
				window.setTimeout("layer.close("+index+")",500);
				if (response.success == "success") {  
				   $("#errormsg").hide();
	               	window.location.href=ContextPath+response.indexUrl;
					
	            }else{
				   if (response.success=="1")
				   {
					    $("#errormsg").children("span").html(response.error);
						$("#errormsg").show();
						
				   }else
				   {
					   if (response.success=="2")
					   {
							$("#password").val("");
							$("#yzm").val("");
							setValidationCodeImg();
					   }
					   if (response.errorCode=="3")
					   {
						    $("#yzm").val("");
						    setValidationCodeImg();
					   }

						 $("#errormsg").children("span").html(response.error);
						 $("#errormsg").show();
				   }
				  
				}    
			},
		    failure: function () {
		    	window.setTimeout("layer.close("+index+")",500);
				$("#errormsg").children("span").innerHTML= "数据请求异常,请检查网络或联系管理员！";
				$("#errormsg").show();
		    }    
		});
	}
	
	//验证码初始化
	setValidationCodeImg();
	
	$("#yzmImg").click(setValidationCodeImg);
	$("a.update").click(setValidationCodeImg);
	$("#submitbtn").click(login);
	
	document.onkeydown = function(e){
		var ev = document.all ? window.event : e;
			if(ev.keyCode==13) {
				login();
		}
	}
	
});