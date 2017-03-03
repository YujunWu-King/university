function revokeActivity(artId, bmrId){
	//TODO访问撤销报名;
	
	//隐藏改报名;
	var hideDivId = "hd_" + artId + "_" + bmrId;
	
	var tzParams = '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_MYEVENT_STD","OperateType":"REVOKE","comParams":{"artId":"'+artId+'","bmrId":"'+bmrId+'"}}';
	
	$.ajax({
		type:"POST",
		url: TzUniversityContextPath+"/dispatcher",
		data:{
			tzParams:tzParams
		},
		success:function(response){
			var responseJson = eval("(" + response + ")");
			//console.log(responseJson);
			//console.log(responseJson.comContent.success);
			var successValue = responseJson.comContent.success;
			if(successValue == "true"){
				$("#"+hideDivId).hide();
			}else{
				alert("撤销失败");
			}
		},
		failure: function () {
		  	alert("撤销失败");
		} 
		
	});
	
	
}
