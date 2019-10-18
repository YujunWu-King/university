//获取url参数
function getURLParamVal(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = document.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return "";
}


function refreshTimer(){
	$.ajax({
		type:"get",
		dataType:"json",
		cache:false,
		url: baseUrl,
		data: {
			type:'timer',
			classId:ClassId,
			batchId: BatchId
		},
		success:function(resp){		
			if(resp.comContent.startDt == undefined)return;
			var startDt = resp.comContent.startDt;
			var seconds = resp.comContent.seconds;
			var name = resp.comContent.name;
			var tempmsStart=msStart;

			if(startDt != msStart){
				msStart = startDt;
				msSeconds = seconds;
				$("#name").text(name);
				if(tempmsStart == ""){ 
					atime();
				}
				
				if(pweiType != "C" && getURLParamVal("page") == "batch"){
					refreshKslist();
				}
			}else if(msSeconds < seconds){
				msSeconds = seconds;
			} 
		}
	});
}


function atime() {
	
	if(msStart == ""){
		$("#timer").hide();
		return;
	}
	var sen = msSeconds;
	var hour = 0;
	var min = parseInt(sen / 60);
	sen = sen % 60;
	if(min > 60){
		hour = parseInt(min / 60);
		min = min % 60;
	}

	if(hour < 10) hour = "0"+hour;
	if(min < 10) min = "0"+min;
	if(sen < 10) sen = "0"+sen;

	var time = hour+":"+min+":"+sen;
	$("#time").text(time);
	$("#timer").show();

	msSeconds ++;
	setTimeout("atime()",1000);
}