function checkExplorer(language){
	
	var checkBZ = false;
	var explorer = window.navigator.userAgent ;
	//ie 
	if ("ActiveXObject" in window) {
		if(navigator.userAgent.indexOf("MSIE 6.0")>0||navigator.userAgent.indexOf("MSIE 7.0")>0){
			//if(navigator.userAgent.indexOf("MSIE 6.0")>0||navigator.userAgent.indexOf("MSIE 7.0")>0||navigator.userAgent.indexOf("MSIE 8.0")>0){
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
	if(!checkBZ){
		if(language == "ENG"){
			alert('The browser version you are using is too low, please choose IE9 above or a newer version of Firefox, Chrome, Safari and other browsers, if you are using IE9 or later, turn off the "Compatibility View", if you stick with the current browser, may unknown error has occurred. ');
		}else{
			alert("您所使用的浏览器版本过低，请选择IE8以上或较新版本的Firefox、Chrome、Safari等浏览器，如果您使用的是IE8以上版本，请关闭“兼容性视图”，如您坚持使用当前浏览器，可能会发生未知错误。");
		}
	}
	return checkBZ
}