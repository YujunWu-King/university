/*生成返回菜单的函数*/
function createReturnMenu(parentObject, jsonObject)
{
	var tb = Ext.create('Ext.toolbar.Toolbar');
	
	tb.render(parentObject);
  
	tb.add(
			{
				text: '返回批次列表页面',
				tooltip:'单击此按钮返回评审批次列表页面。',
				width: 130,
				handler: function(item,pressed){showPreviousEvaluatePage(1);},
				pressed: true
			},
			'<b>'+jsonObject['ps_baok_nf'] + jsonObject['ps_baok_pc'] + jsonObject['ps_baok_zy']+'</b>'
			/*
			{
				xtype: "",
				text: ,
				width: 200,
				pressed: false
			}
			*/
  	);
}


function loadEvaluateBatchDataById(batchId,jsonObject)
{
	//加载选中批次的考生列表数据
	if(mainPageObjectArray['PreviousBatchId'] != batchId)
	{
		if(mainPageObjectArray['PreviousBatchId'] == '' || mainPageObjectArray['PreviousBatchId'] == null || mainPageObjectArray['PreviousBatchId'] == 'undefined')
		{
			mainPageObjectArray['PreviousBatchId'] = batchId;
		}
		else
		{
			var pBatchId = mainPageObjectArray['PreviousBatchId'];
			
			mainPageObjectArray['PreviousBatchId'] = batchId;
			mainPageObjectArray['DivObjectList'][pBatchId].hide();
		}
		
		var tmpDivObject = null;
		if(mainPageObjectArray['DivObjectList'][batchId] == null)
		{
			var tmpMEP = initializeMainEvaluatePage(batchId,jsonObject);
			
			if(window.batchEvaluateMainPageObject[batchId] == null)
			{
				window.batchEvaluateMainPageObject[batchId] = tmpMEP;
			}
			
			tmpDivObject = $('<div/>');
			
			
			window.myPageSlider[0].divPage.show();
			
			
			tmpDivObject.appendTo($('#tz_msps_main_page'));
			mainPageObjectArray['DivObjectList'][batchId] = tmpDivObject;
			
			
			//生成返回菜单并挂载到批次评审主页面
			createReturnMenu(tmpDivObject[0], jsonObject);
			//挂载生成的批次评审主页面
			tmpMEP.render(tmpDivObject[0]);
			
		}
		else
		{
			tmpDivObject = mainPageObjectArray['DivObjectList'][batchId];
			tmpDivObject.show();
		}
	}
	
}


function loadEvaluateBatchData(callBackFunction)
{
	var loadSuccess = false;
	
	/*本地调试代码*/
	//callBackFunction(jsonEvaluateBatchListDataObject1[0]);
	//return true;
	
	/*服务器联调代码*/
	Ext.Ajax.request(
					{
						url:window.getBatchListUrl,
						method:'POST',
						timeout:10000,
						params:{LanguageCd:'ZHS',MaxRowCount:1000,StartRowNumber:1,MoreRowsFlag:'N'},
						success:function(response)
						{
							var jsonObject = null;
							
							try
							{
								jsonObject = Ext.JSON.decode(response.responseText);
								
								if(jsonObject.error_code != '0')
								{
									loadSuccess = false;
									alert('当前面试评审批次数据加载失败：' + jsonObject.error_decription + '[错误码：' + jsonObject.error_code + ']。');
								}
								else
								{
									loadSuccess = true;
									callBackFunction(jsonObject);
								}
							}
							catch(e1)
							{
								loadSuccess = false;
								if(window.evaluateSystemDebugFlag == 'Y')
								{
									alert('当前面试评审批次数据加载失败，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
								}
								else
								{
									alert('当前面试评审批次数据加载失败，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
								}
							}
						},
						failure:function(response)
						{
							loadSuccess = false;
							if(window.evaluateSystemDebugFlag == 'Y')
							{
								alert('当前面试评审批次数据加载失败，请与系统管理员联系：' + response.responseText);
							}
							else
							{
								alert('当前面试评审批次数据加载失败，请与系统管理员联系。');
							}
						}
					}
				);
	
	
	return loadSuccess;
}

