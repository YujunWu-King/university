function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
}

function getOperatorType(){
	
	var thisUrl = window.location.pathname;
	if(thisUrl.indexOf("/decorate/")){
		return "D";
	}else if(thisUrl.indexOf("/preview/")){
		return "P";
	}else{
		return "R";
	}
	
}
