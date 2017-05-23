var myHdNum = 0;

function revokeActivity(artId, bmrId,el){
	if($(el).hasClass("btn-disabled")) return;  //不可撤销
	//隐藏该报名div;
	var hideDivId = "hd_" + artId + "_" + bmrId;
	var index;
	//var tzParams = '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_MYEVENT_STD","OperateType":"REVOKE","comParams":{"artId":"'+artId+'","bmrId":"'+bmrId+'"}}';
	var tzParams = '{"ComID":"TZ_APPONL_COM","PageID":"TZ_APPBAR_VIEW_STD","OperateType":"EJSON","comParams":{"APPLYID":"'+ artId +'","BMRID":"'+ bmrId+'"}}';
	$.ajax({
		type:"POST",
		url: TzUniversityContextPath+"/dispatcher",
		beforeSend:function(){
			index = layer.open({
				type: 2,
				content: '处理中...'
			});
		},
		data:{
			tzParams:tzParams
		},
		success:function(response){
			layer.close(index);
			var responseJson = eval("(" + response + ")");
			//console.log(responseJson);
			//console.log(responseJson.comContent.success);
			var successValue = responseJson.comContent.result;
			var resultDesc = responseJson.comContent.resultDesc;
			
			//信息框
			layer.open({
			    content: resultDesc,
			    btn: '关闭'
			 });
			
			
			if(successValue == "0"){
				$("#"+hideDivId).hide();
			}

		},
		failure: function () {
			layer.close(index);
		  	//alert("撤销失败");
			layer.open({
			    content: "撤销失败",
			    btn: '关闭'
			 });
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
				success: function(resultJson) {	
					pagenum=pagenum+1;
					var resultNum = resultJson.comContent.resultNum;
					if(resultNum > 0){
						myHdNum = myHdNum + resultNum;
						// 插入数据到页面，放到最后面
	                	$('.bg').append(resultJson.comContent.result);
					}else{
						 // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
					}
					
					// 每次数据插入，必须重置
                    me.resetload();
                    if(myHdNum > 0){
                    	$('.dropload-noData').html("数据已全部加载");
					}
				},
				error: function(xhr, type) {
					alert('数据加载失败!');
					me.resetload();
				}
			});
		}
	});
	
}
