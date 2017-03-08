function getNotices(siteid,columnId,pagenum){
	alert("ddd");
	$('.ziliao').dropload({
		scrollArea: window,
		loadDownFn: function(me) {
			$.ajax({
				type: 'GET',
				data:{
					"tzParams": '{"ComID":"TZ_M_WEB_INDEX_COM","PageID":"TZ_M_HDTZ_LIST","OperateType":"LOADNOTICES","comParams":{"siteId": "'+ siteid +'","columnId": "' + columnId + '","pagenum": '+pagenum+'}}'
				},
				url: TzUniversityContextPath+"/dispatcher",
				dataType: 'json',
				success: function(result) {	
					pagenum=pagenum+1;
					var resultNum = result.comContent.resultNum;
					var result = "";
					if(resultNum > 0){
						// 插入数据到页面，放到最后面
						result = result.comContent.result;
					}
					alert(pagenum);
					$('.ziliao').append(result);
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