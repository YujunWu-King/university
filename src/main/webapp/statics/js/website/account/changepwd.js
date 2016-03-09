$("#newpass").on("focus", function() {
	var top = $("#newpass").position().top;
	var left = $("#newpass").position().left;
	var height = $("#newpass").height()
	var width = $("#newpass").width()
	var hi = $("#J_PwdTip").height();

	left = left + width + 15;
	top = top + (height - hi) / 2;
	$("#J_PwdTip").css("display", "block");
	$("#J_PwdTip").css("left", left);
	$("#J_PwdTip").css("top", top);
});
$("#newpass").on("blur", function() {
	$("#J_PwdTip").css("display", "none");
});

setInterval(function() {
	var val = $("#newpass").val();
	if (val != "") {
		var patrn;
		var num = 0;
		var num1 = 0;
		var num2 = 0;
		var num3 = 0;
		var num4 = 0;
		var num5 = 0;
		var num6 = 0;
		var str1 = 0;
		var str2 = 0;
		var cr;
		for (var i = 0; i < val.length; i++) {
			cr = val.charAt(i);

			/*
			 * patrn= /[^\d\w\s]/; if (patrn.test(val)){ num = num + 1; }
			 */
			patrn = /\w/;
			if (patrn.test(cr)) {
				num = num + 1;
			} else {
				num5 = 1;
			}

		}

		patrn = /[A-Z]/;
		if (patrn.test(val)) {
			num6 = num6 + 1;
		}
		patrn = /[a-z]/;
		if (patrn.test(val)) {
			num6 = num6 + 1;
		}
		patrn = /[0-9]/;
		if (patrn.test(val)) {
			num6 = num6 + 1;
		}
		patrn = /[_]/;
		if (patrn.test(val)) {
			num6 = num6 + 1;
		}

		if (val.length >= 6 && val.length <= 32) {
			num1 = 1;
			$("#J_PwdTip .pw-rule-length .iconfont").html("√");
			$("#J_PwdTip .pw-rule-length .iconfont").css("color", "#14c2b3");
		} else {
			num1 = 0;
			$("#J_PwdTip .pw-rule-length .iconfont").html("X");
			$("#J_PwdTip .pw-rule-length .iconfont").css("color", "#FF460F");
		}

		if (num > 0 && num5 == 0) {
			num2 = 1;
			$("#J_PwdTip .pw-rule-legal .iconfont").html("√");
			$("#J_PwdTip .pw-rule-legal .iconfont").css("color", "#14c2b3");
		} else {

			num2 = 0;
			$("#J_PwdTip .pw-rule-legal .iconfont").html("X");
			$("#J_PwdTip .pw-rule-legal .iconfont").css("color", "#FF460F");
		}

		if (num5 == 0 && num6 > 1) {
			num3 = 1;
			$("#J_PwdTip .pw-rule-multi .iconfont").html("√");
			$("#J_PwdTip .pw-rule-multi .iconfont").css("color", "#14c2b3");
		} else {
			num3 = 0;
			$("#J_PwdTip .pw-rule-multi .iconfont").html("X");
			$("#J_PwdTip .pw-rule-multi .iconfont").css("color", "#FF460F");
		}

		num4 = num1 + num2 + num3;
		if (num4 < 1) {
			$("#J_PwdTip .pw-strength-1").css("background-color", "");
			$("#J_PwdTip .pw-strength-2").css("background-color", "");
			$("#J_PwdTip .pw-strength-3").css("background-color", "");

			$("#J_PwdTip .pw-rule-length .iconfont").html("О");
			$("#J_PwdTip .pw-rule-length .iconfont").css("color", "");
			$("#J_PwdTip .pw-rule-legal .iconfont").html("О");
			$("#J_PwdTip .pw-rule-legal .iconfont").css("color", "");
			$("#J_PwdTip .pw-rule-multi .iconfont").html("О");
			$("#J_PwdTip .pw-rule-multi .iconfont").css("color", "");

			$("#J_PwdTip .pw-strength .pw-strength-bar em").html("");
			$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color", "");

			$("#J_PwdTip .pw-strength .pw-strength-bar em").html("");
		}
		if (num4 >= 1) {
			$("#J_PwdTip .pw-strength-1").css("background-color", "#FF460F");
			$("#J_PwdTip .pw-strength-2").css("background-color", "");
			$("#J_PwdTip .pw-strength-3").css("background-color", "");

			$("#J_PwdTip .pw-strength .pw-strength-bar em").html(tzGdFixpwdWeakTips);
			$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color",
					"#FF460F");
		}
		if (num4 >= 2) {
			$("#J_PwdTip .pw-strength-2").css("background-color", "#FF460F");
			$("#J_PwdTip .pw-strength-3").css("background-color", "");

			$("#J_PwdTip .pw-strength .pw-strength-bar em").html(tzGdFixpwdMiddleTips);
			$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color",
					"#FF460F");
		}

		if (num5 == 0 && num4 >= 3 && num > 10) {
			$("#J_PwdTip .pw-strength-1").css("background-color", "#0A9E00");
			$("#J_PwdTip .pw-strength-2").css("background-color", "#0A9E00");
			$("#J_PwdTip .pw-strength-3").css("background-color", "#0A9E00");
			$("#J_PwdTip .pw-strength .pw-strength-bar em").html(tzGdFixpwdStrongTips);
			$("#J_PwdTip .pw-strength .pw-strength-bar em").css("color",
					"#0A9E00");
		}

		if (num4 >= 3) {
			$('#status_PASSWORD').attr("value", 0);
		} else {
			$('#status_PASSWORD').attr("value", 1);
		}
	} else {
		$("#J_PwdTip .pw-strength-1").css("background-color", "");
		$("#J_PwdTip .pw-strength-2").css("background-color", "");
		$("#J_PwdTip .pw-strength-3").css("background-color", "");

		$("#J_PwdTip .pw-rule-length .iconfont").html("О");
		$("#J_PwdTip .pw-rule-length .iconfont").css("color", "");
		$("#J_PwdTip .pw-rule-legal .iconfont").html("О");
		$("#J_PwdTip .pw-rule-legal .iconfont").css("color", "");
		$("#J_PwdTip .pw-rule-multi .iconfont").html("О");
		$("#J_PwdTip .pw-rule-multi .iconfont").css("color", "");

		$("#J_PwdTip .pw-strength .pw-strength-bar em").html("");
	}
}, 200);

$("#oldpass").focus(function() {
	var pass = document.getElementById("oldpass");
	pass.type = "password";
	$(this).val("");
	$("#messTip").hide();
});
$("#newpass").focus(function() {
	var pass = document.getElementById("newpass");
	pass.type = "password";
	$(this).val("");
	$("#messTip").hide();
});
$("#cfpass").focus(function() {
	var pass = document.getElementById("cfpass");
	pass.type = "password";
	$(this).val("");
	$("#messTip").hide();
});
$("#btnpass")
		.click(
				function() {
					var $oldPass = $("#oldpass").val();
					var $newPass = $("#newpass").val();
					var $cfPass = $("#cfpass").val();

					if ($oldPass.length < 1) {
						var pass = document.getElementById("oldpass");
						//pass.type = "text";
						// $("#oldpass").val(tzGdFixpwdOldPass + " " + tzGdFixpwdBlankTips);

						$("#messTip").html(
								'<img src="'+ tzGdFixpwdImgPath +'/alert.png" width="16" height="16" class="alert_img" />'
										+ tzGdFixpwdOldPass + " " + tzGdFixpwdBlankTips);
						$("#messTip").show();
						return false;
					}

					if ($newPass.length < 1) {
						var pass = document.getElementById("newpass");
						//pass.type = "text";
						// $("#newpass").val(tzGdFixpwdNewPass + " " + tzGdFixpwdBlankTips);
						$("#messTip").html(
								'<img src="'+ tzGdFixpwdImgPath +'/alert.png" width="16" height="16" class="alert_img" />'
										+ tzGdFixpwdNewPass + " " + tzGdFixpwdBlankTips);
						$("#messTip").show();

						return false;
					}

					if ($('#status_PASSWORD').val() == "1") {
						$("#messTip").html(
								'<img src="'+ tzGdFixpwdImgPath +'/alert.png" width="16" height="16" class="alert_img" />'
										+ tzGdFixpwdStrongMsg);
						$("#messTip").show();
						return false;
					}

					if ($cfPass.length < 1) {
						var pass = document.getElementById("cfpass");
						//pass.type = "text";
						// $("#cfpass").val(tzGdFixpwdConfPass + " " + tzGdFixpwdBlankTips);
						$("#messTip").html(
								'<img src="'+ tzGdFixpwdImgPath +'/alert.png" width="16" height="16" class="alert_img" />'
										+ tzGdFixpwdConfPass + " " + tzGdFixpwdBlankTips);
						$("#messTip").show();
						return false;
					}
					if ($newPass != $cfPass) {
						var pass = document.getElementById("cfpass");
						//pass.type = "text";
						// $("#cfpass").val(" " + tzGdFixpwdNotSameTips);
						$("#messTip").html(
								'<img src="'+ tzGdFixpwdImgPath +'/alert.png" width="16" height="16" class="alert_img" />'
										+ " " + tzGdFixpwdNotSameTips);
						$("#messTip").show();
						return false;
					}
					/*
					 * if($oldPass==$newPass){ var pass =
					 * document.getElementById("oldpass"); pass.type = "text";
					 * $("#oldpass").val("新旧密码不能相同！"); return false; }
					 */

					/*
					 * var data = "oldPass=" + $oldPass + "&newPass=" +
					 * $newPass;
					 */
					var lang = $("#lang").val();
					var pwd = {
						"oldPass" : $oldPass,
						"newPass" : $newPass,
						"lang" : lang
					};

					$("#messTip").html('');
					$("#messTip").hide();

					var tzParams = '{"ComID":"TZ_GD_ZS_USERMNG","PageID":"TZ_ZS_USERMNG_STD","OperateType":"PWD","comParams":'
							+ JSON.stringify(pwd) + '}';
					$.ajax({
						type : "POST",
						dataType : "json",
						url : tzGdFixpwdUrl,
						data : {
							"tzParams" : tzParams
						},
						success : function(jsondata) {
							$("#oldpass").val("");
							$("#newpass").val("");
							$("#cfpass").val("");
							alert(jsondata.comContent.success);
							/*
							 * if( jsondata == "N"){ var pass =
							 * document.getElementById("oldpass"); pass.type =
							 * "text"; // $("#oldpass").val(tzGdFixpwdOldPass + " " +
							 * tzGdFixpwdErrorTips); $("#messTip").html('<img
							 * src="'+tzGdFixpwdImgPath+'/alert.png" width="16" height="16"
							 * class="alert_img" />'+tzGdFixpwdOldPass + " "  + tzGdFixpwdErrorTips);
							 * $("#messTip").show(); } if(jsondata =="Y"){
							 * $("#oldpass").val(""); $("#newpass").val("");
							 * $("#cfpass").val(""); alert(" " + tzGdFixpwdPassSucTips); }
							 */
						}
					});
					return false;
				});