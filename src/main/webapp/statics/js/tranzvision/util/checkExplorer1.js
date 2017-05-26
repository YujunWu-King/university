function checkExplorer1(language){
	
	var checkBZ = false;
	var explorer = window.navigator.userAgent ;
	
	//ie
	console.log()
	if ("ActiveXObject" in window) {
		if (navigator.userAgent.indexOf("MSIE 6.0") >= 0 || navigator.userAgent.indexOf("MSIE 7.0") >= 0 || navigator.userAgent.indexOf("MSIE 8.0") >= 0 || navigator.userAgent.indexOf("MSIE 9.0") >= 0) {			
			
		}else{
			if(document.documentMode){
				checkBZ =true;
			}
		}
	}
	//firefox 
	else if (explorer.indexOf("Firefox") >= 0) {
		checkBZ =true;
	}
	//Chrome
	else if(explorer.indexOf("Chrome") >= 0){
		checkBZ =true;
	}
	//Opera
	else if(explorer.indexOf("Opera") >= 0){
		checkBZ =true;
	}
	//Safari
	else if(explorer.indexOf("Safari") >= 0){
		checkBZ =true;
	}else{
	
	}
	var buttonLabel = "我知道了";
	if(language=="ENG"){
		buttonLabel = "OK";
	}
	var borwer_tip="";
	borwer_tip = borwer_tip + '<div id="tranzvision_browser_tip" style="margin-top:20px">';
	borwer_tip = borwer_tip + '  <div class="popup-box">';
	//borwer_tip = borwer_tip + '    <div id="browser_tip_close" class="close"></div>';	
	borwer_tip = borwer_tip + '    <div style="width: 90%;max-height: 290px;margin: 0 auto;font-size: 13px;line-height: 22px;color: #666;margin-bottom: 16px;">';
	//borwer_tip = borwer_tip + '      <img src="/university/statics/images/login/alert.png" />';
	borwer_tip = borwer_tip + '      <span style="color:#8e60bc;font-size:14px;">系统检测到您所使用的浏览器版本过低，为保证您正常登录此网站，推荐使用如下浏览器：</span>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">Windows环境：Chrome 内核版本47.0以上，Firefox 内核版本49.0 以上，IE11、Edge浏览器。</div>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">Mac环境：Chrome浏览器，Firefox浏览器，Safari浏览器。</div>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">如果您仍坚持使用当前浏览器，可能会发生未知错误。</div>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">网站试运行期间技术支持电话：010-58448503（9:00~18:00）；邮箱：service@tranzvision.com.cn</div>';
	borwer_tip = borwer_tip + '      <span style="color:#8e60bc;font-size:14px;">The system detects that the browser version you are using is too old .<br/> To ensure you log on to this system,it is recommended to use the browsers as follows:</span>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">Windows:Chrome 47.0+/Firefox 49.0+/IE11/Edge</div>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">Mac:Chrome/Firefox/Safari</div>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">If you stick with the current browser,unknown error may occur.</div>';
	borwer_tip = borwer_tip + '      <div style="font-size:12px;">IT Support for the new website: service@tranzvision.com.cn</div>';
	borwer_tip = borwer_tip + '    </div>';
	borwer_tip = borwer_tip + '    <div id="browser_tip_popup_button" style="width: 130px;height: 34px;font-size: 14px;color: #fff;background-color: #8e60bc;border-radius: 6px;-webkit-border-radius: 6px;text-align: center;line-height: 34px;margin: 0 auto;cursor: pointer;margin-bottom:10px;">' + buttonLabel + '</div>';
	borwer_tip = borwer_tip + '  </div>';
	borwer_tip = borwer_tip + '  <div class="mask"></div>';
	borwer_tip = borwer_tip + '</div>';
	
	if(!checkBZ){
		window.location.href="/statics/js/tranzvision/util/tip.html";
	}
}

$(document).ready(function(){
	var lang = $("#lang").val();
	checkExplorer1(lang);
})