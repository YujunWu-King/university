$(document).ready(function(){
		//重新加载验证码
		function create_yzm(){
			var _captchaURL = "/captcha";
			$('#yzmImg').attr('src',_captchaURL + "?" + Math.random());
		}
		

		$("#forgetPassV").on("click",function(){
			$("#forgetPassV").css("color","gray");
			$("#forgetPassV").attr("onclick","return false;");
			//alert("忘记密码");
			//alert($("#cssPath").val());
			var letterId=SurveyBuild.refLetterId;
			var tzParam='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_RESET_PASS_STD","OperateType":"RESET_TJX_PASS","comParams":{"letterId":"'+letterId+'","cssPath":"'+$("#cssPath").val()+'"}}';
			$.ajax({
				type: "POST",
				url: SurveyBuild.tzGeneralURL,
				data: {
					tzParams: tzParam
				},
				dataType: "JSON",
				success: function(f) {
					console.dir(f);
					if(f.comContent.success=="0"){
						alert(MsgSet["TJXPWDOK"]);
					} else {
						$("#forgetPassV").removeAttr("onclick");
						$("#forgetPassV").css("color","red");
						alert(MsgSet["TJXPWDERROR"]);
					}
				}
			});
		});
	});