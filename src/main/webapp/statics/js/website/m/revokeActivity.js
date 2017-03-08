function revokeActivity(artId, bmrId){
	//隐藏该报名div;
	var hideDivId = "hd_" + artId + "_" + bmrId;
	
	//var tzParams = '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_MYEVENT_STD","OperateType":"REVOKE","comParams":{"artId":"'+artId+'","bmrId":"'+bmrId+'"}}';
	var tzParams = '{"ComID":"TZ_APPONL_COM","PageID":"TZ_APPBAR_VIEW_STD","OperateType":"EJSON","comParams":{"APPLYID":"'+ artId +'","BMRID":"'+ bmrId+'"}}';
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
			var successValue = responseJson.comContent.result;
			var resultDesc = responseJson.comContent.resultDesc;
			if(successValue == "0"){
				alert(resultDesc);
				$("#"+hideDivId).hide();
			}else{
				alert(resultDesc);
			}
		},
		failure: function () {
		  	alert("撤销失败");
		} 
		
	});	
}

function getMyAppActivity(siteid,pagenum){	
	$('.viewport-adaptive').dropload({
		scrollArea: window,
		loadDownFn: function(me) {
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_MYEVENT_STD","OperateType":"LOADMYAPPACTIVITY","comParams":{"siteId": "'+ siteid + '","pagenum": '+pagenum+'}}'
				},
				url: TzUniversityContextPath+"/dispatcher",
				dataType: 'json',
				success: function(result) {	
					pagenum=pagenum+1;
					var resultNum = result.comContent.resultNum;
					if(resultNum > 0){
						// 插入数据到页面，放到最后面
	                	$('.bg').append(result.comContent.result);
					}else{
						 // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
					}
					
					// 每次数据插入，必须重置
                    me.resetload();
					
				},
				error: function(xhr, type) {
					alert('数据加载失败!');
					me.resetload();
				}
			});
		}
	});
	
}
