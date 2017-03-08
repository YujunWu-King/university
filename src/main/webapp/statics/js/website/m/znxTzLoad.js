function getZnxList(siteid,pagenum){	
	$('.viewport-adaptive').dropload({
		scrollArea: window,
		loadDownFn: function(me) {
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_SYSINFO_STD","OperateType":"LOADZNX","comParams":{"siteId": "'+ siteid + '","pagenum": '+pagenum+'}}'
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