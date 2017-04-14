var jzNum = 0;

function getNotices(siteid,columnId,pagenum){
	$('.viewport-adaptive').dropload({
		scrollArea: window,
		loadDownFn: function(me) {
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_WEB_ZSRL_STD","OperateType":"LOADZSRL","comParams":{"siteId": "'+ siteid +'","columnId": "' + columnId + '","pagenum": '+pagenum+'}}'
				},
				url: TzUniversityContextPath+"/dispatcher",
				dataType: 'json',
				success: function(resultJson) {	
					pagenum=pagenum+1;
					var resultNum = resultJson.comContent.resultNum;
					if(resultNum > 0){
						jzNum = jzNum + resultNum;
						// 插入数据到页面，放到最后面
						$('.ziliao').append(resultJson.comContent.result);
					}else{
						 // 锁定
                        me.lock();
                        // 无数据
                        me.noData();
                  
					}
					
					me.resetload();
					if(jzNum > 0){
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