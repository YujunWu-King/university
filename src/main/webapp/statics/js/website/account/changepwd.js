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
	addCheck();
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
					if ($('#status_PASSWORD').val() == "2") {
						$("#messTip").html(
								'<img src="'+ tzGdFixpwdImgPath +'/alert.png" width="16" height="16" class="alert_img" />'
										+ "密码格式错误");
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
                    var BindEmail=$("#BindEmail").text();
                    var BindMobile=$("#BindMobile").text();
					var pwd = {
						"oldPass" : $oldPass,
						"newPass" : $newPass,
						"lang" : lang,
                        "BindEmail" : BindEmail,
                        "BindMobile" : BindMobile
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

/**
*
* 张超  增加对密码的新校验
* 密码必须包含数字和字母
* 密码中数字和字母不能超过两位
* 相同的字母和数字不能超过三位
*/
function addCheck() {
   var TZ_PASSWORD=$("#newpass").val();
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
           $('#status_PASSWORD').attr("value", 2);
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
                   $('#status_PASSWORD').attr("value", 2);
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
                           $('#status_PASSWORD').attr("value", 2);
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
       var TZ_EMAIL=$("#BindEmail").text();
       var TZ_MOBILE=$("#BindMobile").text();
       if(!(TZ_MOBILE==""||TZ_MOBILE==undefined||TZ_MOBILE=="undefined"||TZ_MOBILE=="无")){
           //手机不能为空
           if(!(TZ_EMAIL==""||TZ_EMAIL==undefined||TZ_EMAIL=="undefined")){
               // console.log("邮箱不能为空,手机不为空");
               //邮箱不能为空,手机不为空
               if(TZ_PASSWORD.indexOf(TZ_EMAIL)==-1&&TZ_PASSWORD.indexOf(TZ_MOBILE)==-1){
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#14c2b3");
               }else{
                   $('#status_PASSWORD').attr("value", 2);
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
                   $('#status_PASSWORD').attr("value", 2);
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("X");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#FF460F");
               }
           }
       }else{
           //手机为空
           if(!(TZ_EMAIL==""||TZ_EMAIL==undefined||TZ_EMAIL=="undefined"||TZ_EMAIL=="无")){
               // console.log("邮箱不能为空，手机为空");
               //邮箱不能为空，手机为空
               if(TZ_PASSWORD.indexOf(TZ_EMAIL)==-1){
                   $("#J_PwdTip .pw-rule-contain .iconfont").html("√");
                   $("#J_PwdTip .pw-rule-contain .iconfont").css("color","#14c2b3");
               }else{
                   $('#status_PASSWORD').attr("value", 2);
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