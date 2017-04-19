function createMainPageSearchKSPanel(jsonObject, isFromDfPanel){
	var showThisPanelHeader = true;
	if(isFromDfPanel!="undefined" && isFromDfPanel!=null) showThisPanelHeader = false;
	
	var MainPageSearchKSPanel = Ext.create("Ext.FormPanel",{
		title		: '搜索考生',
		header		: showThisPanelHeader,
		collapsible	: true,
		hideCollapseTool: false,
		overflowY	: 'hidden',
		//margins		: '5 0 5 5',
		bodyPadding	: 10,
		margin		: '0 0 2 0',
		width		: "100%", 
		//height		: 200,
		layout		: 'form',
		items		: [
				{
					xtype: "fieldcontainer",
					fieldLabel		: '搜索操作',
					hideLabel		: true,
					combineErrors	: false,
					//msgTarget		: 'side',
					layout			: 'hbox',
					height			: 35,
					defaults		: {hideLabel: false},
					items			: [
						{
							xtype     	: 'textfield',
							name      	: 'KSH_SEARCH_MSID',
							fieldLabel	: '请输入面试申请号',
							labelWidth	: 130,
							value		: '',
							enableKeyEvents	: true,
							margin		: '0 30 0 0',
							allowBlank	: true,
							width		: 320
						},
						{
							xtype     	: 'textfield',
							name      	: 'KSH_SEARCH_NAME',
							fieldLabel	: '或&nbsp;&nbsp;姓名',
							labelWidth	: 75,
							value		: '',
							enableKeyEvents	: true,
							margin		: '0 5 0 0',
							allowBlank	: true,
							width		: 270
						},
						{
							xtype		: 'hiddenfield',
							name		: 'KSH_BMBID',
							value		: ''
						},
						{
							xtype		: 'hiddenfield',
							name		: 'KSH_KSNAME',
							value		: ''
						},
						{
							xtype		: 'hiddenfield',
							name		: 'KSH_MSID',
							value		: ''
						}
					  ]
},
{
	xtype: "fieldcontainer",
	hidden			: false,
	fieldLabel		: '搜索结果',
	hideLabel		: true,
	combineErrors	: false,
	//msgTarget		: 'side',
	layout			: 'hbox',
	//height			: 35,
	defaults		: {hideLabel: false},
	items			: [
						{
							xtype		: 'displayfield',
							name		: 'SearchKSResult',
							value		: '请输入面试申请号或姓名进行查找',
							width		: 1000
						}
					  ]
					  
},
{
	xtype: "fieldcontainer",
	fieldLabel		: '操作按钮',
	hideLabel		: true,
	combineErrors	: false,
	//msgTarget		: 'side',
	layout			: 'hbox',
	//height			: 35,
	defaults		: {hideLabel: false},
	items			: [
		{
			xtype		: 'button',
			text		: '查&nbsp;&nbsp;找',
			width		: 100,
			//height		: 25,
			margin		: '10 10 10 0',
			handler 	: function() {
					if(MainPageSearchKSPanel.getForm().isValid()){
						
						var searchKSForm = MainPageSearchKSPanel.getForm();
						
						var searchMSID = Ext.String.trim(searchKSForm.findField('KSH_SEARCH_MSID').getValue());
						var searchKSNM = Ext.String.trim(searchKSForm.findField('KSH_SEARCH_NAME').getValue());
						searchKSForm.findField('KSH_SEARCH_MSID').setValue(searchMSID);
						searchKSForm.findField('KSH_SEARCH_NAME').setValue(searchKSNM);
						
						searchKSForm.findField('KSH_BMBID').setValue('');
						
						if(searchMSID=='' && searchKSNM==''){
							Ext.Msg.alert('提示','面试申请号或姓名至少输入一项！');
							searchKSForm.findField('SearchKSResult').setValue('请输入面试申请号或姓名进行查找');
							return;
						}
						
						searchKSForm.findField('SearchKSResult').setValue('正在查找，请稍后');
						
						maskWindow("正在查找，请稍后...");

					    try
					    {
					        Ext.Ajax.request(
					            {
					                url: window.baseUrl,
					                params:{
					                	LanguageCd:'ZHS',
										type:'search',
		                                BaokaoClassID:jsonObject['ps_class_id'],
		                                BaokaoPCID:jsonObject['ps_pc_id'],
		                                KSH_SEARCH_MSID:searchMSID,
		                                KSH_SEARCH_NAME:searchKSNM
					                },
					                timeout: 60000,
					                async: true,
					                success: function(response, opts)
					                {
					                    //返回值内容
					                    var jsonText = response.responseText;
					                    try
					                    {
					                        var responseJsonObject = Ext.util.JSON.decode(jsonText);
					                        /*判断服务器是否返回了正确的信息*/
					                        if(responseJsonObject.state.errcode == 1){
					                        	Ext.Msg.alert("提示",responseJsonObject.state.errdesc);
					                        }else{
					                        	if(responseJsonObject.comContent.error_code=="0"){
													//显示查询结果
													var searchRstZJHM = Ext.String.trim(responseJsonObject.comContent.ps_ksh_zjhm);
													searchRstZJHM = searchRstZJHM==''?'无':searchRstZJHM;
													
													searchKSForm.findField("SearchKSResult").setValue('<b>查询结果：</b>面试申请号【'+ responseJsonObject.comContent.ps_ksh_msid +'】，报名表编号【'+ responseJsonObject.comContent.ps_ksh_bmbid +'】，姓名【'+ responseJsonObject.comContent.ps_ksh_xm +'】');
													
													searchKSForm.findField("KSH_BMBID").setValue(responseJsonObject.comContent.ps_ksh_bmbid);
													searchKSForm.findField("KSH_KSNAME").setValue(responseJsonObject.comContent.ps_ksh_xm);
													searchKSForm.findField("KSH_MSID").setValue(responseJsonObject.comContent.ps_ksh_msid);
													
												}else{
													Ext.Msg.alert('失败', responseJsonObject.comContent.error_decription);
													searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
												}
					                        }
					                    }
					                    catch(e)
					                    {
					                        Ext.Msg.alert("提示","查询失败！请重试！多次失败请联系管理员！");
					                        searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
					                    }
					                },
					                failure: function(response, opts)
					                {
					                	var respText = Ext.util.JSON.decode(response.responseText);
					                	Ext.Msg.alert("提示","查询失败："+respText.error+"，请与系统管理员联系。");
					                	searchKSForm.findField('SearchKSResult').setValue('请输入考生申请号或姓名进行查找');
					                },
					                callback: function(opts,success,response)
					                {
					                    unmaskWindow();
					                }
					            });
					    }
					    catch(e1)
					    {
					    	Ext.Msg.alert("提示","保存：请与系统管理员联系。");
					    	unmaskWindow();
					    }
					}
			}
			
		},
		{
			xtype		: 'button',
			hidden		: false,
			text		: '进行评审',
			width		: 100,
			//height		: 25,
			margin		: '10 10 10 0',
			handler 	: function() {
				if(MainPageSearchKSPanel.getForm().isValid()){
					var searchKSForm = MainPageSearchKSPanel.getForm();
					
					var searchKSBMBID = Ext.String.trim(searchKSForm.findField("KSH_BMBID").getValue());
					var searchKSKSNAME = Ext.String.trim(searchKSForm.findField("KSH_KSNAME").getValue());
					var searchKSMSID = Ext.String.trim(searchKSForm.findField("KSH_MSID").getValue());
					
					if(searchKSBMBID==""){
						Ext.Msg.alert("提示","请先查找需要评审的考生。");
						return;
					}

					maskWindow();
					try
				    {
				        Ext.Ajax.request(
				            {
				                url: window.baseUrl,
				                params:{
				                	LanguageCd:'ZHS',
				                	type:'add',
		                            BaokaoClassID:jsonObject['ps_class_id'],
		                            BaokaoPCID:jsonObject['ps_pc_id'],
				            		KSH_BMBID:searchKSBMBID
				                },
				                timeout: 60000,
				                async: true,
				                success: function(response, opts)
				                {
				                    //返回值内容
				                    var jsonText = response.responseText;
				                    try
				                    {
				                        var responseJsonObject = Ext.util.JSON.decode(jsonText);
				                        /*判断服务器是否返回了正确的信息*/
				                        if(responseJsonObject.state.errcode == 1){
				                        	Ext.Msg.alert("提示",responseJsonObject.state.errdesc);
				                        }else{
				                        	//unmask window
											unmaskWindow();											
											//0-新抽取的考生 或者是 2-列表中已存在的考生，则直接跳转到评分页面
											if(responseJsonObject.comContent.error_code=="0" ||responseJsonObject.comContent.error_decription=="2"){
												
												//var searchKSBMBID = Ext.string.trim(searchKSForm.fineField("KSH_BMBID").getValue());
												
												//局部刷新本地数据并跳转到打分页面
												if(window.KSINFO_JSON_DATA == null)
												{
													window.KSINFO_JSON_DATA = new Array();
												}
												
												var tmpBmbID = searchKSBMBID;
												KSINFO_JSON_DATA[tmpBmbID] = jsonObject;

												var tzEObject = new tzEvaluateObject();
												
				                                tzEObject.baokaoClassID = jsonObject['ps_class_id'];
				                                tzEObject.baokaoClassName = jsonObject['ps_class_mc'];
				                                tzEObject.baokaoPcID = jsonObject['ps_pc_id'];
				                                tzEObject.baokaoPcName = jsonObject['ps_pc_name'];

												tzEObject.applicantName = searchKSKSNAME;
												tzEObject.applicantInterviewID = searchKSMSID;
												tzEObject.applicantBaomingbiaoID = searchKSBMBID;
												
												//获取新的局部数据，并使用局部数据刷新当前页面

				                                //
				                                var cls_pc_id = jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id'];
												//若是在打分页面，则刷新；否则要跳转到打分页面
												if(showThisPanelHeader){
													//getPartBatchDataByBatchId(jsonObject['ps_bkfx_id'],loadApplicantData,tzEObject,'NXT');
				                                    getPartBatchDataByBatchId(cls_pc_id,loadApplicantData,tzEObject,'NXT');
												}else{
													//getPartBatchDataByBatchId(jsonObject['ps_bkfx_id'],loadApplicantData,tzEObject,'NXT');
				                                    getPartBatchDataByBatchId(cls_pc_id,loadApplicantData,tzEObject,'NXT');
												}
												
											}else{												
												Ext.Msg.alert('失败', jsonObject.comContent.error_decription);
											}
				                        }
				                    }
				                    catch(e)
				                    {
				                    	console&&console.error(e);
				                        Ext.Msg.alert("提示","进行评审失败！请重试！多次失败请联系管理员！");
				                    }
				                },
				                failure: function(response, opts)
				                {
				                	var respText = Ext.util.JSON.decode(response.responseText);
				                	Ext.Msg.alert("提示",respText.error+"，请与系统管理员联系。");
				                },
				                callback: function(opts,success,response)
				                {
				                    unmaskWindow();
				                }
				            });
				    }
				    catch(e1)
				    {
				    	console&&console.error(e1);
				    	Ext.Msg.alert("提示","出现错误：请与系统管理员联系。");
				    	unmaskWindow();
				    }
				}

			}
				
			}
		  ]
						  
	}

]
	});	
	
	
	
	return MainPageSearchKSPanel;
}

function createMainPageHeader(jsonObject)
{
	//创建评委评审主页面页头区
	var mainPageHeader = Ext.create('Ext.panel.Panel',
		 {
				title: '评审说明',
				collapsible:true,
				collapsed:true,
				margin:'0 0 2 0',
				width: "100%", 
				layout:'fit',
				bodyPadding:'10 10 10 15',
				html :Ext.String.format(jsonObject['ps_description']),
				autoEl:{
					tag: 'div',
					html:'<p>通知与说明显示区</p>'
				}
		});
	
	return mainPageHeader;
}

//记录 “已评审考生得分统计” 中需要居中的 Column Header Text
var PJFTJGridColumnHeaderTextAry = new Array();
//记录 PJFTJGrid 的实例ID
var PJFTJGrid_batchID;

function getDataModelForPJFTJGrid(jsonObject)
{
	var statisticsGridDataModel = {gridFields:[],gridColumns:[],gridData:[]};
	var tmpArray = jsonObject['ps_data_cy']['ps_tjzb_btmc'];
	
	PJFTJGridColumnHeaderTextAry = new Array();
	
	for(var i=0;i<tmpArray.length;i++)
	{	
		var colName = '00' + (i + 1);
		colName = 'col' + colName.substr(colName.length - 2);
		
		PJFTJGridColumnHeaderTextAry.push(tmpArray[i][colName]);
		
		if(tmpArray[i]['ps_grp_flg'] == 'Y')
		{
			var tmpObject = {text:tmpArray[i][colName],columns:[]};
												
			for(var j=0;j<tmpArray[i]['ps_sub_col'].length;j++)
			{
				var subColName = '00' + (j + 1);
				
				subColName = 'sub_col' + subColName.substr(subColName.length - 2);
				
				//只显示平均分，其他的隐藏
				var hidden_sub_col = true;
				if(subColName == "sub_col03" ){
					hidden_sub_col = false;
				}
				
				var tmpColumn = {
              						text     : tmpArray[i]['ps_sub_col'][j][subColName],
              						width    : 400,
              						sortable : false,
              						resizable: true,
									hidden	 : hidden_sub_col,
              						dataIndex: colName + '_' + subColName
              					};
				
				tmpObject['columns'].push(tmpColumn);
				statisticsGridDataModel['gridFields'].push({name:colName + '_' + subColName});
			}
			
			statisticsGridDataModel['gridColumns'].push(tmpObject);
		}
		else
		{
			var tmpObject = {
              					text     : tmpArray[i][colName],
              					flex     : 1,
              					sortable : false,
              					resizable: false,
              					align    : 'center',
              					dataIndex: colName
              				};
			statisticsGridDataModel['gridFields'].push({name:colName});
			statisticsGridDataModel['gridColumns'].push(tmpObject);
			
		}
	}
	
	tmpArray = jsonObject['ps_data_cy']['ps_tjzb_mxsj'];
    var dataRow = [];
	for(var i=0;i<tmpArray.length;i++)
	{

        var colName = '00' + (i + 1);
        colName = 'col' + colName.substr(colName.length - 2);
        dataRow.push(tmpArray[i][colName]);
		
		/*for(itm in tmpArray[i])
		{
			if(Object.prototype.toString.call(tmpArray[i][itm]) == '[object Object]')
			{
				for(itm1 in tmpArray[i][itm])
				{
					dataRow.push(tmpArray[i][itm][itm1]);
				}
			}
			else
			{
				dataRow.push(tmpArray[i][itm]);
			}
		}*/
		

	}
    statisticsGridDataModel['gridData'].push(dataRow);
	
	return statisticsGridDataModel;
}

function createPJFTJGrid(batchId,jsonObject,doHidePanel)
{

	var boolHidePanel = true;
    if(jsonObject['ps_display_fs'] == "Y"){
        if(doHidePanel=='Y'){
            boolHidePanel = false;
        }
    }


	var myDataModel = getDataModelForPJFTJGrid(jsonObject);
	
	var store = Ext.create('Ext.data.ArrayStore', {
													fields: myDataModel['gridFields'],
													data: myDataModel['gridData']
												});
  
  PJFTJGrid_batchID = 'EvaluatePJFTJGrid' + batchId;
  
  var grid = Ext.create('Ext.grid.Panel', {
      store: store,
      stateful: true,
      collapsible: false,
      multiSelect: false,
      columnLines: true,
	  hidden: boolHidePanel,
	  	id: PJFTJGrid_batchID,
      stateId: 'EvaluatePJFTJGrid',
      width: "100%",
      columns: myDataModel['gridColumns'],
      title: '已评审考生得分统计',
      viewConfig: {
          stripeRows: true,
          enableTextSelection: true
      }
  });
  
  return grid;
}

function getDataForFenbuGrid(jsonObject)
{
	var ebList = [];
	var counter = 0;
	
	for(var i=0;i<jsonObject['ps_data_fb'].length;i++)
	//for(var i=jsonObject['ps_data_fb'].length-1;i>=0;i--)
	{
		for(var j=0;j<jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'].length;j++)
		{
			var tmpArray = [];
			
			tmpArray[0] = jsonObject['ps_data_fb'][i]['ps_fszb_mc'];
			tmpArray[1] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_fb_mc'];
			tmpArray[2] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_bzfb_bilv'];
			tmpArray[3] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_bzfb_rshu'];
			tmpArray[4] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_bzfb_wcrs'];
			tmpArray[5] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_sjfb_bilv'];
			tmpArray[6] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_sjfb_rshu'];
			tmpArray[7] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_sjfb_wcrs'];
			tmpArray[8] = jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]['ps_sjfb_fhyq'];
			
			ebList[counter] = tmpArray;
			counter ++;
		}
	}
	
	return ebList;
}

function createFenbuGrid(jsonObject,doHidePanel)
{
	
	var boolHidePanel = true;
	if(doHidePanel=='Y'){
		boolHidePanel = false;
	}

	var myData = getDataForFenbuGrid(jsonObject);
	
	var store = Ext.create('Ext.data.ArrayStore', {
      fields: [{name:'zb_mc'},{name:'zb_fb_mc'},{name:'ps_bj_fblv'},{name:'ps_bj_fbrs'},{name:'ps_bj_fbwc'},{name:'ps_sj_fblv'},{name:'ps_sj_fbrs'},{name:'ps_sj_fbwc'},{name:'ps_sj_valid'}],
      groupField: 'zb_mc',
      sorters: ['zb_fb_mc','ps_bj_fblv','ps_bj_fbrs','ps_bj_fbwc','ps_sj_fblv','ps_sj_fbrs','ps_sj_fbwc','ps_sj_valid'],
      data: myData
  });
  
  var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
        groupHeaderTpl: '{name}',
        hideGroupedHeader: true,
        enableNoGroups:false
    });
  
  var grid = Ext.create('Ext.grid.Panel', {
      store: store,
      stateful: true,
      collapsible: false,
      multiSelect: false,
      columnLines: true,
	  hidden: boolHidePanel,
      stateId: 'EvaluateFenbuGrid',
      features: groupingFeature,
      columns: [
          {
              text     : '指标名称',
              flex     : 1,
              sortable : false,
              resizable: false,
              dataIndex: 'zb_mc'
          },
          {
              text     : '分布名称',
              //width    : 180,
			  flex     : 1,
              sortable : false,
              resizable: false,
              dataIndex: 'zb_fb_mc'
          },
          {
              text     : '标准分布比率',
              width    : 80,
              sortable : false,
              resizable: false,
			  hidden   : true,
              dataIndex: 'ps_bj_fblv'
          },
          {
              text     : '标准分布人数',
              width    : 80,
              sortable : false,
              resizable: false,
			  hidden   : true,
              dataIndex: 'ps_bj_fbrs'
          },
          {
              text     : '允许误差人数',
              width    : 80,
              sortable : false,
              resizable: false,
			  hidden   : true,
              dataIndex: 'ps_bj_fbwc'
          },
          {
              text     : '您目前评分分布比率',
              //width    : 130,
			  width    : 330,
              sortable : false,
              resizable: false,
              dataIndex: 'ps_sj_fblv'
          },
          {
              text     : '您目前评分分布人数',
              //width    : 130,
			  width    : 330,
              sortable : false,
              resizable: false,
              dataIndex: 'ps_sj_fbrs'
          },
          {
              text     : '您目前评分误差人数',
              width    : 130,
              sortable : false,
              resizable: false,
			  hidden   : true,
              dataIndex: 'ps_sj_fbwc'
          },
          {
              text     : '是否符合要求',
              //flex     : 1,
              sortable : false,
              resizable: false,
			  hidden   : true,
              dataIndex: 'ps_sj_valid'
          }
      ],
      width: "100%",
      title: '已评审考生得分分布统计',
      viewConfig: {
          stripeRows: true,
          enableTextSelection: true
      }
  });
  
  grid.on({resize:function(){myPageSlider[0].adjustHeight();}});
  
  return grid;
}

function getDataModelForStatisticsChart(jsonObject)
{
	var statisticsChartDataModel = {chartFields:[],chartData:[],dataFields:[],seriesTitle:[],seriesTips:{},maxValue:0,minValue:0};
	var tmpArray = jsonObject['ps_data_cy']['ps_tjzb_btmc'];
	var drawChartFields = {};
	
	for(var i=0;i<tmpArray.length;i++)
	{
		var colName = '00' + (i + 1);
		var dfRow = [];
		
		colName = 'col' + colName.substr(colName.length - 2);
		if(tmpArray[i]['ps_grp_flg'] == 'Y')
		{
			for(var j=0;j<tmpArray[i]['ps_sub_col'].length;j++)
			{
				var subColName = '00' + (j + 1);
				var tmpSubColName = '';
				
				subColName = 'sub_col' + subColName.substr(subColName.length - 2);
				tmpSubColName = colName + '_' + subColName;
				
				if(tmpArray[i]['ps_sub_col'][j]['ps_cht_flg'] == 'Y')
				{
					drawChartFields[tmpSubColName] = 'Y';
					statisticsChartDataModel['chartFields'].push(tmpSubColName);
					statisticsChartDataModel['dataFields'].push(tmpSubColName);
					statisticsChartDataModel['seriesTitle'].push(tmpArray[i]['ps_sub_col'][j][subColName]);
					statisticsChartDataModel['seriesTips'][tmpSubColName] = tmpArray[i]['ps_sub_col'][j][subColName];
				}
			}
		}
		else
		{
			if(tmpArray[i]['ps_cht_flg'] == 'N')
			{
				statisticsChartDataModel['chartFields'].push(colName);
			}
			else
			{
				statisticsChartDataModel['dataFields'].push(colName);
				statisticsChartDataModel['seriesTitle'].push(tmpArray[i][colName]);
				statisticsChartDataModel['seriesTips'][colName] = tmpArray[i][colName];
			}
		}
	}
	
	tmpArray = jsonObject['ps_data_cy']['ps_tjzb_mxsj'];
    var dataRow = {};
	for(var i=0;i<tmpArray.length;i++)
	{
        var colName = '00' + (i + 1);
        colName = 'col' + colName.substr(colName.length - 2);
        dataRow[colName] = tmpArray[i][colName];
	
	}
	
    if(statisticsChartDataModel['minValue'] == statisticsChartDataModel['maxValue'] && statisticsChartDataModel['minValue'] == 0)
    {
        statisticsChartDataModel['minValue'] = 0;
        statisticsChartDataModel['maxValue'] = 100;
    }


    statisticsChartDataModel['chartData'].push(dataRow);

	return statisticsChartDataModel;
}

function createStatisticsChart(jsonObject,chartStore,totalWidth,doHidePanel)
{
	var boolHidePanel = true;
	if(doHidePanel=='Y'){
		boolHidePanel = false;
	}
	
	var retChartObject = null
	
	
	var chartDataModel = getDataModelForStatisticsChart(jsonObject);
	
	
	if(chartDataModel['chartFields'].length >= 1 && chartDataModel['chartData'].length >= 1 && chartDataModel['dataFields'].length >= 1 && chartDataModel['seriesTitle'].length >= 1)
	{
        var hiddenGrid = true;
        if(jsonObject['ps_display_fs'] == "Y"){
            hiddenGrid = false;
        }
		var store1 = null;

		if(chartStore == null)
		{
			store1 = Ext.create('Ext.data.JsonStore',
 					{
 						fields: chartDataModel['chartFields'],
 						data: chartDataModel['chartData']
 					});
		}
		else
		{
			store1 = chartStore;
		}
		
		
		var fsChart2 = Ext.create('Ext.chart.Chart',
			 {
			 		xtype: 'chart',
					hidden: boolHidePanel,
			 		style: 'background:#fff',
			 		animate: true,
			 		shadow: true,
			 		store: store1,
			 		legend: {position: 'top'},
		 			axes: [
		 							{
			 							type: 'Numeric',
			 							position: 'left',
			 							fields: chartDataModel['dataFields'],
			 							label:{renderer: Ext.util.Format.numberRenderer('000.00')},
			 							title: '统计指标值',
			 							grid: true,
			 							maximum: chartDataModel['maxValue'],
			 							minimum: chartDataModel['minValue']
			 						},
		 							{
		 								type: 'Category',
		 								position: 'bottom',
		 								fields: ['col01'],
		 								title: '统计指标名称'
		 							}
			 					],
			 		series: [
			 							{
			 								type: 'column',
			 								axis: 'left',
			 								highlight: true,
			 								title:chartDataModel['seriesTitle'],
			 								tips: {
			 												trackMouse: true,
		 													width: 180,
		 													renderer: function(storeItem, item)
		 																		{
		 																			this.setTitle(storeItem.get('col01') + '-' + chartDataModel['seriesTips'][item['yField']] + ' : ' + Ext.util.Format.number(storeItem.get(item['yField']),'000.00'));
		 																		}
		 												},
		 									label: {
		 														font: '18px Helvetica, sans-serif',
		 														display: 'insideEnd',
		 														'text-anchor': 'middle',
		 														field: chartDataModel['dataFields'],
		 														renderer: Ext.util.Format.numberRenderer('000.00'),
			 													//orientation: 'vertical',
			 													color: '#333'
			 											 },
			 								xField: 'col01',
			 								yField: chartDataModel['dataFields']
			 							}
			 						]
			 });
		
		var chartPanel = Ext.create('Ext.panel.Panel',
						{
							title: '指标统计柱状图',
							layout:'fit',
                            hidden : hiddenGrid,
							collapsible:true,
							collapsed:true,
							height: 400,
							width: totalWidth,
							items: fsChart2
						});
		
		if(chartStore == null)
		{
			chartPanel.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
		}
		
		retChartObject = chartPanel;
	}
	
	
	return retChartObject;
}

function getSubDataForFenbuChart(chartDataArray)
{
	var data = [];

	for(var i=0;i<chartDataArray.length;i++)
	{
		var tmpNumber1 = 0;
		var tmpNumber2 = 0;
		
		var ps_bzfb_bilv = chartDataArray[i]['ps_bzfb_bilv'];
		ps_bzfb_bilv = ps_bzfb_bilv!=undefined?ps_bzfb_bilv.replace("%",""):0;
		
		var ps_sjfb_bilv = chartDataArray[i]['ps_sjfb_bilv'];
		ps_sjfb_bilv = ps_sjfb_bilv!=undefined?ps_sjfb_bilv.replace("%",""):0;
		
		if(Ext.isNumeric(ps_bzfb_bilv) == true)
		{
			tmpNumber1 = 1.0 * ps_bzfb_bilv;
		}
		if(Ext.isNumeric(ps_sjfb_bilv) == true)
		{
			tmpNumber2 = 1.0 * ps_sjfb_bilv;
		}
		
		data.push({name:chartDataArray[i]['ps_fb_mc'],data1:tmpNumber1,data2:tmpNumber2});
	}
	return data;
}

function createSubStatisticsCharts(chartDataArray,chartStore,boolHidePanel)
{
	var store1 = null;
	
	if(chartStore == null)
	{
		store1 = Ext.create('Ext.data.JsonStore',
				{
					fields: ['name', 'data1','data2'],
					data: getSubDataForFenbuChart(chartDataArray['ps_fszb_fbsj'])
				});
	}
	else
	{
		store1 = chartStore;
	}
	
	
	var series1 = {
				type: 'line',
				highlight: {size: 7,radius: 7},
				axis: 'left',
				smooth: true,
				xField: 'name',
				yField: 'data1',
				markerConfig: {type: 'cross',size: 4,radius: 4,'stroke-width': 0},
				title: '标准分布曲线',
				tips: {
								trackMouse: true,
								width: 180,
								renderer: function(storeItem, item)
													{
														this.setTitle('分布区间[' + storeItem.get('name') + ']<br>标准分布比率: ' + Ext.util.Format.number(storeItem.get('data1'),'000.00'));
													}
							}
			};
	var series2 = {
			type: 'line',
			highlight: {size: 7,radius: 7},
			axis: 'left',
			smooth: true,
			xField: 'name',
			yField: 'data2',
			markerConfig: {type: 'circle',size: 4,radius: 4,'stroke-width': 0},
			title: '分布曲线',
			tips: {
							trackMouse: true,
							width: 180,
							renderer: function(storeItem, item)
												{
													this.setTitle('分布区间[' + storeItem.get('name') + ']<br>分布比率: ' + Ext.util.Format.number(storeItem.get('data2'),'000.00'));
												}
						}
		};
	
	
	var seriesArray = [];
	if(Object.prototype.toString.call(chartDataArray['ps_cht_flds']) == '[object Array]')
	{
		for(var i=0;i<chartDataArray['ps_cht_flds'].length;i++)
		{
			if(chartDataArray['ps_cht_flds'][i] == 'ps_bzfb_bilv')
			{
				seriesArray.push(series1);
			}
			else if(chartDataArray['ps_cht_flds'][i] == 'ps_sjfb_bilv')
			{
				seriesArray.push(series2);
			}
		}
	}
	
	var fsChart1 = null
	if(seriesArray.length >= 1)
	{
		fsChart1 = Ext.create('Ext.chart.Chart',
				{
					xtype: 'chart',
					hidden: boolHidePanel,
					style: 'background:#fff',
					animate: true,
					store: store1,
					shadow: true,
					theme: 'Category1',
					legend: {position: 'top'},
					axes: [
						{
							type: 'Numeric',
							position: 'left',
							fields: ['data1', 'data2'],
							label:{renderer: function(value){return Ext.util.Format.number(value,'000.00');}},
							title: '分布比率',
							grid: true,
							maximum:100,
							minimum:0
						},
						{
							type: 'Category',
							position: 'bottom',
							fields: ['name'],
							title: '分布区间',
							label: {rotate: {degrees: 270}}
						}
				],
					series: seriesArray
				});
	}
	
	
	var retObject = null;
	if(fsChart1 != null)
	{
		retObject = {
				title:chartDataArray['ps_fszb_mc'] + '指标统计曲线图',
				layout:'fit',
				items:fsChart1
				};
	}
	
	return retObject;
}

function createStatisticsCharts(jsonObject,chartStoreArray,totalWidth,doHidePanel)
{
	
	var boolHidePanel = true;
	if(doHidePanel=='Y'){
		boolHidePanel = false;
	}
	
	var chartArray = [];
	
	for(var i=0;i<jsonObject['ps_data_fb'].length;i++)
	{
		var tmpChart = null;
		
		if(chartStoreArray != null)
		{
			tmpChart = createSubStatisticsCharts(jsonObject['ps_data_fb'][i],chartStoreArray[i],boolHidePanel);
		}
		else
		{
			tmpChart = createSubStatisticsCharts(jsonObject['ps_data_fb'][i],null,boolHidePanel);
		}
		
		if(tmpChart != null)
		{
			chartArray.push(tmpChart);
		}
	}
	
	var chartPanel = null;
	if(chartArray.length > 0)
	{
		chartPanel = Ext.create('Ext.panel.Panel',
			{
				title:'各统计指标得分区间人数比率分布统计曲线图',
				margin:0,
				bodyPadding:5,
				padding:0,
				collapsible:true,
				collapsed:true,
				//layout: {type: 'table',columns: chartArray.length},
				defaults: {frame:true, width:totalWidth/*totalWidth/chartArray.length - 2*/, height: 480},
				width: "100%",
				items: chartArray
			});
		
		if(chartStoreArray == null)
		{
			chartPanel.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
		}
	}
	
	return chartPanel;
}

function getApplicantListColumnHeaders(jsonObject)
{
	var clHeader = ["ps_ksh_xh","ps_ksh_id","ps_ksh_bmbid","ps_ksh_xm","ps_ksh_ppm","ps_ksh_zt","ps_ksh_dt"];
	
	for(itm in jsonObject)
	{
		clHeader.push(itm);
	}
	
	return clHeader;
}

function getApplicantListColumns(jsonObject)
{
	var columnList = [
	      {text:"面试顺序",width:80,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_xh"},
	      {text:"考生编号",flex:1,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_id",
				renderer:function(value){return Ext.String.format('<a id="ks_id_{1}" href="JavaScript:void(0)" title="单击此链接进入该考生面试评审主页面。">{0}</a>',value,value);}
			},
		  {text:'考生姓名',flex:1,align:'left',sortable:true,resizable:true,dataIndex:"ps_ksh_xm"},
		  {text:"考生排名",flex:1,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_ppm"}
		];
	
	//动态列
	for(itm in jsonObject)
	{
		columnList.push({text:jsonObject[itm],flex:1,align:'left',sortable:true,resizable:true,dataIndex:itm, renderer: function (v, metaData) {
            var resultHTML=Ext.util.Format.htmlEncode(v)
            return resultHTML;
        }});
	}
	
	columnList.push({text:"评议状态",flex:1,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_zt"});
	columnList.push({text:"评审时间",flex:1,minWidth:140,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_dt"});
	
	if(columnList.length >= 1)
	{
		columnList.push({
				text:'评审',
				width:100,
				sortable:false,
				resizable:false,
				padding:'0 3 0 3',
				dataIndex:'pw_evaluate_col',
				align    : 'center',
				renderer : function(value)
	 				 {
 				 		var tmpBtn = Ext.create('Ext.Button',{text:"进行评审",height:20,margin:'0 0 0 0',padding:'0 0 0 0',valign:'left',tooltip:'单击此按钮进入该考生评审主页面'});
 				 		var divTmp = $('<div/>');
 				 		
 				 		tmpBtn.setTooltip('单击此按钮进入该考生面试评审主页面。');
 				 		divTmp[0].id = Ext.id();
 				 		tmpBtn.render(divTmp[0]);
 				 		
 				 		divTmp.find('span').css({'vertical-align':'middle','margin-left':'1px','margin-right':'1px'});
 				 		
 				 		return divTmp[0].outerHTML;
	 				 }
			}
		 );
									 
									 
									 
		columnList.push({
				text:'移除',
				width:58,
				sortable:false,
				resizable:false,
				padding:'0 3 0 3',
				dataIndex:'pw_delks_col',
				align    : 'center',
				renderer : function(value)
	 				 {
 				 		var tmpBtn = Ext.create('Ext.Button',{text:"移除",height:20,margin:'0 0 0 0',padding:'0 0 0 0',valign:'left',tooltip:'单击此按钮将考生从列表中移除'});
 				 		var divTmp = $('<div/>');
 				 		
 				 		tmpBtn.setTooltip('单击此按钮将考生从列表中移除。');
 				 		divTmp[0].id = Ext.id();
 				 		tmpBtn.render(divTmp[0]);
 				 		
 				 		divTmp.find('span').css({'vertical-align':'middle','margin-left':'1px','margin-right':'1px'});
 				 		
 				 		return divTmp[0].outerHTML;
	 				 }
			}
		 );
	
	}
	
	return columnList;
}

function getApplicantListColumnValues(jsonObject)
{
	return jsonObject||[];
	/*
	var cValues = [];
	
	for(var i=0;i<jsonObject.length;i++)
	{
		var tmpRow = [];
		var tmpBMBID;
		
		for(var j=0;j<jsonObject[i]['ps_row_cnt'].length;j++)
		{
			for(itm in jsonObject[i]['ps_row_cnt'][j])
			{
				if(itm != 'ps_ksh_bmbid')
				{
					if(itm == 'ps_ksh_xh')
					{
						//tmpRow.push(i + 1);
						tmpRow.push(jsonObject[i]['ps_row_cnt'][j][itm]);
					}
					else
					{
						tmpRow.push(jsonObject[i]['ps_row_cnt'][j][itm]);
					}
				}
				else
				{
					tmpBMBID = jsonObject[i]['ps_row_cnt'][j][itm];
				}
			}
		}
		
		if(tmpRow.length >= 1)
		{
			tmpRow.push('进行评审');
			tmpRow.push('移 除');
			tmpRow.push(jsonObject[i]['ps_row_id']);
			tmpRow.push(tmpBMBID);
		}
		
		cValues.push(tmpRow);
	}
	
	return cValues;
	*/
}

/*全部提交功能，即评委提交当前评审批次*/
function submitEvaluateBatch(batchId)
{
	if(batchId == null || batchId == '' || batchId == 'undefined')
	{
		alert('提交当前评审批次时发生错误：没有指定需要提交的评审批次编号。');
	}
	
	//mask window
	maskWindow();
    var arr = batchId.split("_");
    var classid = arr[0];
    var pcid = arr[1];
	Ext.Ajax.request(
		{
			url:window.submitApplicantDataUrl,
			method:'POST',
			timeout:30000,
			params: {
				LanguageCd:'ZHS',
				OperationType:'SUBMTALL',
                BaokaoClassID:classid,
                BaokaoPCID:pcid
			},
			success:function(response)
			{
				var jsonObject = null;
				
				try
				{
					jsonObject = Ext.JSON.decode(response.responseText).comContent;
					
					if(jsonObject.error_code != '0')
					{
						//unmask window
						unmaskWindow();
						
						alert('提交当前评审批次时发生错误：' + jsonObject.error_decription);
					}
					else
					{
						//局部刷新当前当前评审批次数据
						getPartBatchDataByBatchId(batchId,null,{applicantBaomingbiaoID:''},'SUBMTALL','当前评审批次[' + getBatchNameById(batchId) + ']提交成功。');
					}
				}
				catch(e1)
				{
					if(window.evaluateSystemDebugFlag == 'Y')
					{
						alert('提交当前评审批次时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
						var mytmpWindow = window.open("about:blank");
						mytmpWindow.document.body.innerHTML = response.responseText;
					}
					else
					{
						alert('提交当前评审批次时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
					}
					
					//unmask window
					unmaskWindow();
				}
			},
			failure:function(response)
			{
				if(window.evaluateSystemDebugFlag == 'Y')
				{
					alert('提交当前评审批次时发生错误，请与系统管理员联系：' + response.responseText);
				}
				else
				{
					alert('提交当前评审批次时发生错误，请与系统管理员联系。');
				}
				
				//unmask window
				unmaskWindow();
			}
		}
	);
}


function createApplicantList(jsonObject)
{
	var store1 = Ext.create('Ext.data.Store', {
      fields: getApplicantListColumnHeaders(jsonObject['ps_data_kslb']['ps_ksh_list_headers']),
      data: getApplicantListColumnValues(jsonObject['ps_data_kslb']['ps_ksh_list_contents'])
  });
  
  var ps_kslb_submtall_status = (jsonObject['ps_kslb_submtall']=="Y")?"已提交":"未提交";
  
  var grid = Ext.create('Ext.grid.Panel', {
      store: store1,
      stateful: true,
      collapsible: true,
      multiSelect: false,
      columnLines: true,
      stateId: 'ApplicantListGrid',
      layout:'fit',
      bodyPadding:'0 0 0 0',
      scroll:true,
      width:"100%",
      columns: getApplicantListColumns(jsonObject['ps_data_kslb']['ps_ksh_list_headers']),
      title: '当前已归属您的评审考生列表',
      viewConfig: {
          stripeRows: true,
          enableTextSelection: true,
          forceFit: true,
          scrollOffset: 0
      },
      _tbar: [
      				'->',
      				{
      					text:'提 交',
      					tooltip:'单击此按钮提交当前评审批次。',
						width: 80,
						hidden: true,
      					pressed: true,
      					handler : function()
						{
							Ext.Msg.confirm('提示', '是否提交本次评议的全部考生信息？<br />提交后将无法对考生评议成绩进行修改，是否继续？', function(button) {
								if (button === 'yes') {
                                    var cls_pc_id = jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id'];
									//submitEvaluateBatch(jsonObject['ps_bkfx_id']);
                                    submitEvaluateBatch(cls_pc_id);
									
								} else {

								}
							});
							
						}
      				},
      				{
      					text:'打印评审总表',
      					tooltip:'单击此按钮打印当前评审批次评审统计表。',
      					pressed: true,
						hidden: true,
      					handler : function()
      										{
                                                cls_pc_id = jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id'];
      											printStatisticsTotalTable(cls_pc_id,getBatchNameById(cls_pc_id));
      										}
      				}
      			],
      fbar: [
      				/*{
      					text: '获取下一个考生',
      					tooltip:'单击此按钮获取下一个待评审考生及其相关面试，并进入该考生面试评审主页面。',
      					width:120,
      					handler: function()
      									{
      										getNextApplicant(jsonObject);
      									}
      				},*/
      				/*{
      					text: '返回批次列表页面',
      					tooltip:'单击此按钮返回评审批次列表页面。',
      					width:130,
      					handler: function(){showPreviousEvaluatePage(1);}
      				},*/
      				'->',
					'总体提交状态：<span id="ps_kslb_submtall_'+ (jsonObject['ps_class_id']+"_"+jsonObject['ps_pc_id']) +'" style="margin-right:10px;font-weight:bold;">【' + ps_kslb_submtall_status +'】</span>',
      				{
      					text:'提 交',
      					tooltip:'单击此按钮提交当前评审批次。',
						width: 80,
      					handler : function()
						{
							Ext.Msg.confirm('提示', '是否提交本次评议的全部考生信息？<br />提交后将无法对考生评议成绩进行修改，是否继续？', function(button) {
								if (button === 'yes') {
                                    var cls_pc_id = jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id'];
									//submitEvaluateBatch(jsonObject['ps_bkfx_id']);
                                    submitEvaluateBatch(cls_pc_id);
									
								} else {

								}
							});
							
						}
      				},
      				{
      					text:'打印评审总表',
      					tooltip:'单击此按钮打印当前评审批次评审统计表。',
						hidden: true,
      					handler : function()
						{
                            cls_pc_id = jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id'];
							printStatisticsTotalTable(cls_pc_id,getBatchNameById(cls_pc_id));
						}
      				}
      			]
  });
  
  grid.on('cellClick', function(gridViewObject,cellHtml,colIndex,dataModel,rowHtml,rowIndex)
  											{
  												var rec = store1.getAt(rowIndex);
  												//var clickColName = rec.self.getFields()[colIndex]['name'];
												var clickColName = gridViewObject.grid.columns[colIndex]["dataIndex"];
  												
  												
  												gridViewObject.getSelectionModel().getSelection()[0].index = rowIndex;
  												
  												
												if(clickColName == 'pw_delks_col'){
													var doDelKSName = rec.get('ps_ksh_xm');
													Ext.Msg.confirm("提示", "您确定要移除考生【"+ doDelKSName +"】吗？", function(button){
														if(button==="yes"){
							
							maskWindow();
							
							/* Normally we would submit the form to the server here and handle the response... */
							Ext.Ajax.request(
							{
								url:window.baseUrl,
								method:'POST',
								timeout:30000,
								params: {
									LanguageCd:'ZHS',
									type:'remove',
                                    BaokaoClassID:jsonObject['ps_class_id'],
                                    BaokaoPCID:jsonObject['ps_pc_id'],
									KSH_BMBID:rec.get('ps_ksh_bmbid')
								},
								success:function(response)
								{
								
									//unmask window
									unmaskWindow();
									
									var DeljsonObject = null;
									
									try
									{
									
										DeljsonObject = Ext.JSON.decode(response.responseText).comContent;

										if(DeljsonObject.error_code=="0"){
											//移除成功，刷新局部数据
											var cls_pc_id = jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id'];
											getPartBatchDataByBatchId(cls_pc_id,null,{applicantBaomingbiaoID:rec.get('ps_ksh_bmbid')},'RFH');
											
											Ext.Msg.alert('提示', '移除成功！');
											
										}else{
											Ext.Msg.alert('失败', DeljsonObject.error_decription);
										}
									
									}
									catch(e1){
										alert('移除失败！请重试！多次失败请联系管理员！');
									}
								
									
								},
								failure:function(response)
								{
									
									alert('移除考生时发生错误，请与系统管理员联系。');
									
									//unmask window
									unmaskWindow();
								}
							});
					}	
				});
				
				
			}else{
			
				if(clickColName == 'pw_evaluate_col' || rec.get(clickColName) == rec.get('ps_ksh_bmbid'))
				{
					var tmpKshID = jQuery.trim(rec.get('ps_ksh_bmbid'));
					
					if(tmpKshID == null || tmpKshID == '' || tmpKshID == 'undefined')
					{
						alert('系统错误：无法获取指定考生对应的编号。');
					}
					else
					{
						//mask window
						maskWindow();
						
						//加载指定考生评审信息页面并显示
						var tzEObject = new tzEvaluateObject();
						
						/*tzEObject.baokaoDirectionID = jsonObject['ps_bkfx_id'];
						tzEObject.baokaoDirectionName = jsonObject['ps_bkfx_mc'];
						tzEObject.baokaoYear = jsonObject['ps_baok_nf'];
						tzEObject.baokaoBatch = jsonObject['ps_baok_pc'];
						tzEObject.baokaoZhiyuan = jsonObject['ps_baok_zy'];
						*/
						tzEObject.baokaoClassID = jsonObject['ps_class_id'];
						tzEObject.baokaoClassName = jsonObject['ps_class_mc'];
						tzEObject.baokaoPcID = jsonObject['ps_pc_id'];
						tzEObject.baokaoPcName = jsonObject['ps_pc_name'];
						tzEObject.applicantName = rec.get('ps_ksh_xm');
						tzEObject.applicantInterviewID = rec.get('ps_ksh_id');
						tzEObject.applicantBaomingbiaoID = rec.get('ps_ksh_bmbid');
						
						loadApplicantData(tzEObject);
					}
					
				}
			}
		
			
		}
  			 );
  grid.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
  
  window.library_main_evalute_page_ks_grid[jsonObject['ps_class_id'] + "_" + jsonObject['ps_pc_id']] = grid;
  
  return grid;
}

function createStatisticsArea(batchId,jsonObject)
{
	var itemArray = new Array();
	
	
	/*创建概要信息显示区*/
	var gaiyaoArea = {
											xtype: 'component',
											html: Ext.String.format('<ul><li>' + jsonObject['ps_gaiy_info'] + '</li></ul>'),
											style: 'margin-bottom:2px;margin-top:2px;font-size:12'
									 };
	itemArray.push(gaiyaoArea);
	
	//alert(jsonObject.ps_pwkj_tjb);
	//alert(jsonObject.ps_pwkj_fbt);
	
	if(jsonObject.ps_pwkj_tjb=='Y'){
	
		/*创建评委评分平均分统计表格*/
		var tjPJFgrid = createPJFTJGrid(batchId,jsonObject,jsonObject.ps_pwkj_tjb);
		itemArray.push(tjPJFgrid);
		
		/*创建评委评分分布统计表格*/
		var tjFBgrid = createFenbuGrid(jsonObject,jsonObject.ps_pwkj_tjb);
		itemArray.push(tjFBgrid);
	}
	
	if(jsonObject.ps_pwkj_fbt=='Y'){
	
		/*创建评委评分分布统计图表*/
		var tjCharts2 = createStatisticsCharts(jsonObject,null,"100%",jsonObject.ps_pwkj_fbt);
		if(tjCharts2 != null)
		{
			itemArray.push(tjCharts2);
		}
		
		/*创建评委评分平均分统计图表*/
		var tjCharts1 = createStatisticsChart(jsonObject,null,"100%",jsonObject.ps_pwkj_fbt);
		if(tjCharts1 != null)
		{
			itemArray.push(tjCharts1);
		}
	}
	
	
	var tjArea = Ext.create('Ext.Panel',
			 {
				title:'评审统计信息区',
				collapsible:true,
				collapsed:true,
				margin:'0 0 2 0',
				bodyPadding:15,
				layout: {
	                type: 'vbox',
	                align: 'stretch'
	            },
				autoHeight:true,
				width:"100%",
				defaultType:'textfield',
				items:itemArray
			 });
	
	return tjArea;
}

function initializeMainEvaluatePage(batchId,jsonObject)
{
	var mainPageFrame = null
	
	/*本地调试代码*/
	//jsonObject = jsonEvaluateBatchDataObjectArray1['pc_f_01'];
	
	if(jsonObject != null)
	{
        //显示总分
        window.evaluateDfPanelDisplayZf =jsonObject['ps_display_fs'];
		var itemArray = new Array();
		
		//创建评委评审主页面考生搜索区
		var mainPageKsSearch = createMainPageSearchKSPanel(jsonObject);
		mainPageKsSearch.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
		itemArray.push(mainPageKsSearch);
		
		//创建评委评审主页面页头区
		var mainPageHeader = createMainPageHeader(jsonObject);
		mainPageHeader.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
		itemArray.push(mainPageHeader);
	
		/*if(jsonObject.ps_pwkj_tjb=='Y' || jsonObject.ps_pwkj_fbt=='Y'){
			//创建评委打分统计信息区
			var tjArea = createStatisticsArea(batchId,jsonObject);
			tjArea.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
			itemArray.push(tjArea);
		}*/
        //创建评委打分统计信息区
        var tjArea = createStatisticsArea(batchId,jsonObject);
        tjArea.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
        itemArray.push(tjArea);
		
		/*创建考生信息列表信息区*/
		var ksListArea = createApplicantList(jsonObject);
		itemArray.push(ksListArea);
		
		//创建评委打分主页面框架
		mainPageFrame = Ext.create('Ext.Panel',
				{
					region:'center',
					autoScroll:false,
					margins:'0 0 0 0',
					bodyPadding:'0 0 0 0',
					cls:'empty',
					bodyStyle:'background:#ffffff',
					//layout:{type:'table',columns: 1},
					width:Ext.getBody().getWidth(),
					autoHeight:true,
					items:itemArray
				});
	}
	
	mainPageFrame.on("resize",function(t,width,height){
		t.suspendEvent("resize");
		if(Ext.fly("tz_evaluation_main").getHeight()<height){
			t.setWidth(Ext.getBody().getWidth()-17);
		}else{
			t.setWidth(Ext.getBody().getWidth());
		}
		t.resumeEvent("resize");
		
		t.updateLayout();
		
	})
		
	return mainPageFrame;
}

/*加载指定考生数据，并进入该考生详细面试评审页面*/
function loadApplicantData(applicantObject)
{
	var classId = applicantObject.baokaoClassID;
    var batchId = applicantObject.baokaoPcID;
	
  if(classId != null && classId != '' && classId != 'undefined' && batchId != null && batchId != '' && batchId != 'undefined')
  {
  	displayApplicantEvaluatePage(applicantObject,showNextEvaluatePage,1,'ks_id_' + applicantObject.applicantInterviewID);
  }
  else
  {
	  //unmask window
	  unmaskWindow();
	  
  	alert('面试评审系统发生错误：评审批次信息丢失。');
  }
}

/*获取局部数据信息的函数*/
function getPartBatchDataByBatchId(batchId,callBackFunction,applicantObject,operationType,tipMessage)
{
    var arr = batchId.split("_");
    var classid = arr[0];
    var pcid = arr[1];
	Ext.Ajax.request(
										{
											url:window.getBatchDataUrl,
											method:'POST',
											timeout:30000,
											params: {
																	LanguageCd:'ZHS',
                                                                    BaokaoClassID:classid,
                                                                    BaokaoPCID:pcid,
																	RequestDataType:'S',
																	MaxRowCount:1000,
																	StartRowNumber:1,
																	MoreRowsFlag:'N'
															},
											success:function(response)
											{
												var jsonObject = null;
												
												try
												{
													jsonObject = Ext.JSON.decode(response.responseText).comContent;
													
													if(jsonObject.error_code != '0')
													{
														//unmask window
														unmaskWindow();
													
														loadSuccess = false;
														alert('刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误：' + jsonObject.error_decription + '[错误码：' + jsonObject.error_code + ']。');
													}
													else
													{
														/*缓存当前局部刷新数据*/
														window.batchJSONArray[batchId]['ps_gaiy_info'] = jsonObject['ps_gaiy_info'];
														window.batchJSONArray[batchId]['ps_data_cy'] = jsonObject['ps_data_cy'];
														window.batchJSONArray[batchId]['ps_data_fb'] = jsonObject['ps_data_fb'];
														window.batchJSONArray[batchId]['ps_data_kslb'] = jsonObject['ps_data_kslb'];
														window.batchJSONArray[batchId]['ps_kslb_submtall'] = jsonObject['ps_kslb_submtall'];
														
														
														/*获取新的局部数据，并使用局部数据刷新当前批次评审主页面数据*/
														//alert(applicantObject.applicantBaomingbiaoID);
														refreshBatchDataByBatchId(jsonObject,'ps_ksh_bmbid',applicantObject.applicantBaomingbiaoID);
														
														//回调指定函数
														if(operationType == 'NXT')
														{//因为获取下一个考生而产生的回调，该回调将导致当前页面切换到指定考生面试评审主页面
															callBackFunction(applicantObject);
														}
														else
														{
															//其他暂无操作
															
															//unmask window
															unmaskWindow();
														}
														
														
														if(tipMessage != null && tipMessage != '' && tipMessage != 'undefined')
														{
															alert(tipMessage);
														}
													}
												}
												catch(e1)
												{
													loadSuccess = false;
													if(window.evaluateSystemDebugFlag == 'Y')
													{
														alert('2刷新当前评审批次[' + batchId + ']数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']' + response.responseText);
														var mytmpWindow = window.open("about:blank");
														mytmpWindow.document.body.innerHTML = response.responseText;
													}
													else
													{
														alert('3刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']。');
													}
												}
											},
											failure:function(response)
											{
												loadSuccess = false;
												if(window.evaluateSystemDebugFlag == 'Y')
												{
													alert('4刷新当前评审批次[' + batchId + ']数据时发生错误，请与系统管理员联系：' + response.responseText);
												}
												else
												{
													alert('5刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误，请与系统管理员联系。');
												}
											}
										}
									);
}

/*自动重新高亮显示已选中数据行的函数*/
function autoHighlightRow(myGrid,dataIndexName,dataIndexValue)
{
	var myStore = myGrid.getStore();
	var totalCount = myStore.getCount();
	
	for(var i=0;i<totalCount;i++)
	{
		if(myStore.getAt(i).get(dataIndexName) == dataIndexValue)
		{
			myGrid.getSelectionModel().select(i);
			myGrid.getSelectionModel().getSelection()[0].index = i;
			break;
		}
	}
}

/*刷新局部数据的函数*/
function refreshBatchDataByBatchId(jsonObject,dataIndexName,dataIndexValue)
{
	
	/*刷新评审打分统计信息区*/
	/*获取当前评审批次ID*/
	var batchId = window.mainPageObjectArray['PreviousBatchId'];
	if(batchId == null) return;
	
	
	/*获取评审打分统计信息区对象*/
	var rfObject1 = window.batchEvaluateMainPageObject[batchId];
	if(rfObject1 == null) return;
	
	
	//lw myPageSlider[0].showAllDivpages();
	
	
	/*刷新评委评审概要信息*/
	try
	{
		var pwAbstractInfo = Ext.String.format('<ul><li>' + jsonObject['ps_gaiy_info'] + '</li></ul>');
		rfObject1['items']['items'][2]['items']['items'][0].getEl().setHtml(pwAbstractInfo);
	}
	catch(e1)
	{
		//alert("1——"+e1);
	}
	
	
	//为统计表格、图表区域计数
	var grid_and_chart_num = 1;
	
	/*刷新实际打分统计数据（平均分、均方差等）*/
	try
	{
		var myDataModel = getDataModelForPJFTJGrid(jsonObject);
		var pwStatisticsGrid = rfObject1['items']['items'][2]['items']['items'][grid_and_chart_num];
		
		pwStatisticsGrid.getStore().loadData(myDataModel['gridData']);
		
		grid_and_chart_num++;
	}
	catch(e2)
	{
		//alert("2——"+e2);
	}
	
	/*刷新实际打分分布统计数据*/
	try
	{
		var pwFenbuGridStore = rfObject1['items']['items'][2]['items']['items'][grid_and_chart_num].getStore();
		pwFenbuGridStore.loadData(getDataForFenbuGrid(jsonObject));
		
		grid_and_chart_num++;
	}
	catch(e3)
	{
		//alert("3——"+e3);
	}
	

	
	/*刷新实际打分分布统计数据曲线图*/
	try
	{
		var pwFenbuCharts = rfObject1['items']['items'][2]['items']['items'][grid_and_chart_num]['items']['items'];
		for(var i=0;i<pwFenbuCharts.length;i++)
		{
			var myStore = pwFenbuCharts[i]['items']['items'][0].getStore();
			myStore.loadData(getSubDataForFenbuChart(jsonObject['ps_data_fb'][i]['ps_fszb_fbsj']));
		}
		grid_and_chart_num++;
	}
	catch(e5)
	{
		//alert("5——"+e5);
	}
	
	/*刷新实际打分统计数据对比柱状图*/
	try
	{
		
		var chartDataModel = getDataModelForStatisticsChart(jsonObject);
		var pwColumnChart = rfObject1['items']['items'][2]['items']['items'][grid_and_chart_num]['items']['items'][0];
		
		pwColumnChart['axes']['items'][0]['maximum'] = chartDataModel['maxValue'];
		pwColumnChart['axes']['items'][0]['minimum'] = chartDataModel['minValue'];
		
		//pwColumnChart.redraw(true);
		pwColumnChart.getStore().loadData(chartDataModel['chartData']);
		
		grid_and_chart_num++;
	}
	catch(e4)
	{
		//alert("4——"+e4);
	}
	
	/*刷新评委当前已评审考生列表*/
	try
	{
		//由于统计grid和统计图表可隐藏，因此需要动态计算
		var grid_pos_num = rfObject1['items']['items'].length - 1;
		
		//更新总体提交状态
		var ps_kslb_submtall_status = (jsonObject['ps_kslb_submtall']=="Y"?"已提交":"未提交");
		$("#ps_kslb_submtall_"+(jsonObject['ps_class_id']+"_"+jsonObject['ps_pc_id'])).html("【"+ps_kslb_submtall_status+"】");
		
		var currentSelectedRow = rfObject1['items']['items'][grid_pos_num].getSelectionModel().getSelection();
		var pwKaoshengListStore = rfObject1['items']['items'][grid_pos_num].getStore();
		
		pwKaoshengListStore.loadData(getApplicantListColumnValues(jsonObject['ps_data_kslb']['ps_ksh_list_contents']));
		
		if(dataIndexName == null || dataIndexValue == null)
		{
			if(currentSelectedRow != null && currentSelectedRow != 'undefined')
			{
				if(currentSelectedRow[0] != null && currentSelectedRow[0] != 'undefined')
				{
					autoHighlightRow(rfObject1['items']['items'][grid_pos_num],'ps_ksh_bmbid',currentSelectedRow[0].get('ps_ksh_bmbid'));
					autoHighlightRow(dfPageWest_grid[batchId],'ps_ksh_bmbid',currentSelectedRow[0].get('ps_ksh_bmbid'));
					
					var tmpApplicantInterviewId = currentSelectedRow[0].get('ps_ksh_id');
					if(tmpApplicantInterviewId != null && tmpApplicantInterviewId != '' && tmpApplicantInterviewId != 'undefined')
					{
						window.myPageSlider[0].autoScrollHtmlTagId = 'ks_id_' + tmpApplicantInterviewId;
					}
				}
			}
		}
		else
		{
			autoHighlightRow(rfObject1['items']['items'][grid_pos_num],dataIndexName,dataIndexValue);
			autoHighlightRow(dfPageWest_grid[batchId],dataIndexName,dataIndexValue);
			
			var tmpApplicantInterviewId = rfObject1['items']['items'][grid_pos_num].getSelectionModel().getSelection()[0].get('ps_ksh_id');
			if(tmpApplicantInterviewId != null && tmpApplicantInterviewId != '' && tmpApplicantInterviewId != 'undefined')
			{
				window.myPageSlider[0].autoScrollHtmlTagId = 'ks_id_' + tmpApplicantInterviewId;
			}
		}
	}
	catch(e6)
	{
		//alert("6——"+e6);
	}
	
	//lw myPageSlider[0].hideAllDivpages();
}


/*测试局部数据刷新的函数*/
function partRefreshTestFunction(batchId)
{
	Ext.Ajax.request(
										{
											url:window.getBatchDataUrl,
											method:'POST',
											timeout:30000,
											params: {
																LanguageCd:'ZHS',
																BaokaoFXID:batchId,
																RequestDataType:'A',
																MaxRowCount:1000,
																StartRowNumber:1,
																MoreRowsFlag:'N'
															},
											success:function(response)
											{
												var jsonObject = null;
												
												try
												{
													jsonObject = Ext.JSON.decode(response.responseText);
													
													refreshBatchDataByBatchId(jsonObject,null,null);
												}
												catch(e1)
												{
													alert('局部数据刷新测试失败，JSON数据解析错误:' + e1.description);
												}
											},
											failure:function(response)
											{
												alert('局部数据刷新测试失败，服务器错误。');
											}
										}
									);
}


/*获取对比柱状图表对象的方法*/
function getColumnChartObject()
{
	var retCCObject = null;
	
	/*获取当前评审批次ID*/
	var batchId = window.mainPageObjectArray['PreviousBatchId'];
	if(batchId == null) return null;
	
	
	/*获取评审打分统计信息区对象*/
	var rfObject1 = window.batchEvaluateMainPageObject[batchId];
	if(rfObject1 == null) return null;
	
	/*获取对比柱状图表对象的Store*/
	try
	{
		var tmpStore = rfObject1['items']['items'][2]['items']['items'][3]['items']['items'][0].getStore();
		retCCObject = createStatisticsChart(window.batchJSONArray[batchId],tmpStore,960);
	}
	catch(e1)
	{
		;
	}
	
	return retCCObject;
}


/*获取对比曲线图表对象的方法*/
function getLineChartObject()
{
	var retLCObject = null;
	
	/*获取当前评审批次ID*/
	var batchId = window.mainPageObjectArray['PreviousBatchId'];
	if(batchId == null) return null;
	
	
	/*获取评审打分统计信息区对象*/
	var rfObject1 = window.batchEvaluateMainPageObject[batchId];
	if(rfObject1 == null) return null;
	
	try
	{
		var chartStoreArray = [];
		var pwFenbuCharts = rfObject1['items']['items'][2]['items']['items'][4]['items']['items'];
		for(var i=0;i<pwFenbuCharts.length;i++)
		{
			chartStoreArray[i] = pwFenbuCharts[i]['items']['items'][0].getStore();
		}
		
		retLCObject = createStatisticsCharts(window.batchJSONArray[batchId],chartStoreArray,960);
	}
	catch(e5)
	{
		;
	}
	
	return retLCObject;
}