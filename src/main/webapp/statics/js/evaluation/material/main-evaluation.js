function createMainPageHeader(jsonObject)
{
	//创建评委评审主页面页头区
	
	var desc = Ext.String.format(jsonObject['ps_description']);
	
	var mainPageHeader = Ext.create('Ext.panel.Panel',{
			//title: '报考年份：' + jsonObject['ps_baok_nf'] + '，报考批次：' + jsonObject['ps_baok_pc'] + '，报考项目：' + jsonObject['ps_baok_zy'],
			title: '评审说明',
			collapseMode:'header',
			collapsible:true,
			collapsed:true,
            width:'100%',
			margin:'0 0 2 0',
			layout:'fit',
			bodyPadding:'10 10 10 15',
			html :desc,
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
				var tmpColumn = {
              						text     : tmpArray[i]['ps_sub_col'][j][subColName],
              						width    : 150,
              						sortable : false,
              						resizable: true,
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

    statisticsGridDataModel['gridData'] = tmpArray;
	
	return statisticsGridDataModel;
}

function createPJFTJGrid(batchId,jsonObject)
{
	var myDataModel = getDataModelForPJFTJGrid(jsonObject);

	var store = Ext.create('Ext.data.Store', {
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
	
	for(var i=0;i<jsonObject['ps_data_fb'].length;i++)
	{
		for(var j=0;j<jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'].length;j++)
		{
			
			var tmpObj = Ext.clone(jsonObject['ps_data_fb'][i]['ps_fszb_fbsj'][j]);
			tmpObj["ps_fszb_mc"] = jsonObject['ps_data_fb'][i]['ps_fszb_mc']
			ebList.push(tmpObj);
		}
	}
	
	return ebList;
}

function createFenbuGrid(jsonObject)
{
	var myData = getDataForFenbuGrid(jsonObject);
	
	var store = Ext.create('Ext.data.Store', {
      fields: ['ps_fszb_mc','ps_fszb_mc','ps_bzfb_bilv','ps_bzfb_rshu','ps_bzfb_wcrs','ps_sjfb_bilv','ps_sjfb_rshu','ps_sjfb_wcrs','ps_sjfb_fhyq'],
      groupField: 'ps_fszb_mc',
      //sorters: ['zb_fb_mc','ps_bzfb_bilv','ps_bzfb_rshu','ps_sj_fblv','ps_sj_fbrs'],
      data: myData
  });
  
  var groupingFeature = Ext.create('Ext.grid.feature.Grouping',{
        groupHeaderTpl: '{name}',
        hideGroupedHeader: true,
        enableNoGroups:false
    });
  
  var columns = [{
	     text     : '指标名称',
	     flex     : 1,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_fszb_mc'
	 },{
	     text     : '分布名称',
	     width    : 180,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_fb_mc'
	 },{
	     text     : '标准分布比率',
	     flex     : 1,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_bzfb_bilv'
	 },{
	     text     : '标准分布人数',
	     flex     : 1,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_bzfb_rshu'
	 },{
	     text     : '允许误差人数',
	     flex     : 1,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_bzfb_wcrs'
	 },{
	      text     : '您目前评审分布比率',
	      flex     : 1,
	      sortable : false,
	      resizable: false,
	      dataIndex: 'ps_sjfb_bilv'
	  },{
	      text     : '您目前评审分布人数',
	      flex     : 1,
	      sortable : false,
	      resizable: false,
	      dataIndex: 'ps_sjfb_rshu'
	  },{
	     text     : '您目前评分误差人数',
	     flex     : 1,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_sjfb_wcrs'
	  },{
	     text     : '是否符合要求',
	     flex     : 1,
	     sortable : false,
	     resizable: false,
	     dataIndex: 'ps_sjfb_fhyq'
	  }];
  
  //是否显示评分标准？不显示则移除分布标准列。
  if(jsonObject["ps_show_standard"]!=undefined&&jsonObject["ps_show_standard"]==false){
	  columns.splice(8,1);
	  columns.splice(7,1);
	  columns.splice(4,1);
	  columns.splice(3,1);
	  columns.splice(2,1);
  }
  
  var grid = Ext.create('Ext.grid.Panel', {
      store: store,
      stateful: true,
      collapsible: false,
      multiSelect: false,
      columnLines: true,
      frame:true,
      stateId: 'EvaluateFenbuGrid',
      features: groupingFeature,
      margin:0,
      padding:0,
      columns: columns,
      width: "100%",
      title: '已评审考生评议结果分布统计',
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
    var dataRow = tmpArray;
	
    if(statisticsChartDataModel['minValue'] == statisticsChartDataModel['maxValue'] && statisticsChartDataModel['minValue'] == 0)
    {
        statisticsChartDataModel['minValue'] = 0;
        statisticsChartDataModel['maxValue'] = 100;
    }


    statisticsChartDataModel['chartData'] = tmpArray;
	
	return statisticsChartDataModel;
}

function createStatisticsChart(jsonObject,chartStore,totalWidth)
{
	var retChartObject = null;
	
	
	var chartDataModel = getDataModelForStatisticsChart(jsonObject);
	
	
	if(chartDataModel['chartFields'].length >= 1 && chartDataModel['chartData'].length >= 1 && chartDataModel['dataFields'].length >= 1 && chartDataModel['seriesTitle'].length >= 1)
	{
		var store1 = null;
		
		if(chartStore == null)
		{
			store1 = Ext.create('Ext.data.Store',{
 						fields: chartDataModel['chartFields'],
 						data: chartDataModel['chartData']
 					});
		}
		else
		{
			store1 = chartStore;
		}
		
		
		var fsChart2 = Ext.create('Ext.chart.Chart',{
	 		xtype: 'chart',
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
	 		series: [{
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
				margin:'0 0 2 0',
				padding:0,
				layout:'fit',
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
		if(Ext.isNumeric(chartDataArray[i]['ps_bzfb_bilv']) == true)
		{
			tmpNumber1 = 1.0 * chartDataArray[i]['ps_bzfb_bilv'];
		}
		if(Ext.isNumeric(chartDataArray[i]['ps_sjfb_bilv']) == true)
		{
			tmpNumber2 = 1.0 * chartDataArray[i]['ps_sjfb_bilv'];
		}
		
		data.push({name:chartDataArray[i]['ps_fb_mc'],data1:chartDataArray[i].ps_bzfb_bilv,data2:chartDataArray[i].ps_sjfb_bilv });

	}

	return data;
}

function createSubStatisticsCharts(chartDataArray,chartStore)
{
	var store1 = null;
	
	if(chartStore == null)
	{
		store1 = Ext.create('Ext.data.JsonStore',
				{
					// 2015-12-08 解决曲线显示不正确的问题 fields: ['name', 'data1','data2'],
                    fields : [{
                        name : 'name',
                        type : 'string'
                    }, {
                        name : 'data1',
                        type : 'float'
                    }, {
                        name : 'data2',
                        type : 'float'
                    }],
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
		title: '实际分布曲线',
		tips: {
				trackMouse: true,
				width: 180,
				renderer: function(storeItem, item)
				{
					this.setTitle('分布区间[' + storeItem.get('name') + ']<br>实际分布比率: ' + Ext.util.Format.number(storeItem.get('data2'),'000.00'));
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

	var fsChart1 = null;
	if(seriesArray.length >= 1)
	{
		fsChart1 = Ext.create('Ext.chart.Chart',
			{
				xtype: 'chart',
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
						fields: [ 'data2'],
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
				//width:'100%',
				items:fsChart1
		};
	}
	
	return retObject;
}

function createStatisticsCharts(jsonObject,chartStoreArray,totalWidth)
{
	var chartArray = [];
	
	for(var i=0;i<jsonObject['ps_data_fb'].length;i++)
	{
		var tmpChart = null;
		
		if(chartStoreArray != null)
		{
			tmpChart = createSubStatisticsCharts(jsonObject['ps_data_fb'][i],chartStoreArray[i]);
		}
		else
		{
			tmpChart = createSubStatisticsCharts(jsonObject['ps_data_fb'][i],null);
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
				title:'评审结果分布曲线图',
				margin: 0,
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
	var clHeader = [
	                'ps_ksh_bmbid','ps_msh_id',
	                {name:'ps_ksh_cpm',type:"number"},
	                'ps_ksh_dt',
	                {name:'ps_ksh_id',type:"number"},
	                {name:'ps_ksh_ppm',type:"number"},
	                'ps_ksh_type',
	                {name:'ps_ksh_xh',type:"number"},
	                'ps_ksh_xm','ps_ksh_zt','ps_row_id','ps_ksh_pc',
	                'ps_re_evaluation','ps_ksh_school','ps_ksh_company'];

	for(itm in jsonObject)
	{
		clHeader.push({name:itm,type:"number"});
	}
	
	return clHeader;
}

function getApplicantListColumns(jsonObject,ps_show_deviation)
{
	var columnList = [
      {text:"序号",width:50,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_xh"},
      {text:"考生编号",minWidth:80,flex:1,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_id",
       renderer:function(value){return Ext.String.format('<a id="ks_id_{1}" href="JavaScript:void(0)" title="单击此链接进入该考生材料评审主页面。">{0}</a>',value,value);}
	  },
	  {text:'考生姓名',minWidth:80,flex:1,align:'left',sortable:true,resizable:true,dataIndex:"ps_ksh_xm"},
	  {text:"上次排名",minWidth:80,flex:1,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_ppm"},
	  {text:"本次排名",minWidth:80,flex:1,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_cpm"},
	];
	
	//动态列：先进行排序
	var dynamicColumns = [];
	for(itm in jsonObject){
		dynamicColumns.push(itm);
	}
	dynamicColumns.sort(function(a,b){
        return a>b;
    });
	for(var i=0;i<dynamicColumns.length;i++){
		columnList.push({text:jsonObject[dynamicColumns[i]],minWidth:80,flex:1,align:'left',sortable:true,resizable:true,dataIndex:dynamicColumns[i], renderer: function (v, metaData) {
            var resultHTML=Ext.util.Format.htmlEncode(v)
            return resultHTML;
        }});
	}
	
	columnList.push({text:"本科院校",width:150,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_school"});
	columnList.push({text:"工作单位",width:150,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_company"});
	columnList.push({text:"评议状态",width:80,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_zt"});
	columnList.push({text:"评审时间",flex:1,minWidth:130,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_dt"});
	//columnList.push({text:"考生类别",flex:1,minWidth:120,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_type"});
	if(ps_show_deviation!=undefined&&ps_show_deviation==true){
		//评委可见偏差
		columnList.push({text:"评委间偏差",width:95,align:'left',sortable:true,resizable:false,dataIndex:"ps_ksh_pc"});
	}
	columnList.push({text:"其他评委已复评",width:120,align:'left',sortable:true,resizable:false,dataIndex:"ps_re_evaluation"});
	
	columnList.push({
		text:'评审',
		width:100,
		sortable:false,
		resizable:false,
		dataIndex:'pw_evaluate_col',
		align    : 'center',
		renderer : function(value)
				 {
				 		var tmpBtn = Ext.create('Ext.Button',{text:"进行评审",height:20,margin:0,padding:0,width:60,tooltip:'单击此按钮进入该考生评审主页面'});
				 		var divTmp = $('<div/>');
				 		
				 		tmpBtn.setTooltip('单击此按钮进入该考生材料评审主页面。');
				 		divTmp[0].id = Ext.id();
				 		tmpBtn.render(divTmp[0]);
				 		
				 		return divTmp[0].outerHTML;
				 }
	});
	return columnList;
}

function getApplicantListColumnValues(jsonObject)
{
	return jsonObject||[];
	/*var cValues = [];
	
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
			tmpRow.push(jsonObject[i]['ps_row_id']);
			tmpRow.push(tmpBMBID);
		}
		
		cValues.push(tmpRow);
	}
	
	return cValues;
	*/
}

/*全部提交功能，即评委提交当前评审批次*/
function submitEvaluateBatch(classid,pc_id)
{
    var batchId = classid+"_"+pc_id;
	if(batchId == null || batchId == '' || batchId == 'undefined')
	{
		Ext.Msg.alert("提示","提交当前评审批次时发生错误：没有指定需要提交的评审批次编号。");
	}

	//mask window
	maskWindow();
	
	Ext.Ajax.request(
			{
				url:window.submitApplicantDataUrl,
				method:'POST',
				timeout:30000,
				params: {
					LanguageCd:'ZHS',
					OperationType:'SUBMTALL',
                    BaokaoClassID:classid,
                    BaokaoPCID:pc_id
				},
				success:function(response)
				{
					//unmask window
					unmaskWindow();

					//返回值内容
                    var jsonText = response.responseText;
                    
					var jsonObject = null;
					
					try
					{
						var jsonObject = Ext.util.JSON.decode(jsonText);
		                /*判断服务器是否返回了正确的信息*/
		                if(jsonObject.state.errcode == 1){
		                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
		                }else{
		                	jsonObject = jsonObject.comContent;
							
							if(jsonObject.error_code != '0')
							{
								Ext.Msg.alert("提示",'提交当前评审批次时发生错误：' + jsonObject.error_decription);
							}
							else
							{
								//局部刷新当前当前评审批次数据
								//getPartBatchDataByBatchId(batchId,null,{applicantBaomingbiaoID:''},'SUBMTALL','当前评审班级批次[' + getBatchNameById(batchId) + ']提交成功。');
	                            //更新总体提交状态
								Ext.Msg.alert("提示","当前评审班级批次提交成功。");
	                            
	                            var ps_kslb_submtall_status = "已提交";
	                            $("#ps_kslb_submtall_"+(jsonObject['ps_class_id']+"_"+jsonObject['ps_bkpc_id'])).html("【"+ps_kslb_submtall_status+"】");
							}
		                }
						
					}
					catch(e1)
					{
						if(window.evaluateSystemDebugFlag == 'Y')
						{
							Ext.Msg.alert("提示",'提交当前评审批次时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
						}
						else
						{
							Ext.Msg.alert("提示",'提交当前评审批次时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
						}
						
					}
				},
				failure:function(response)
				{
					//unmask window
					unmaskWindow();
					
					if(window.evaluateSystemDebugFlag == 'Y')
					{
						Ext.Msg.alert("提示",'提交当前评审批次时发生错误，请与系统管理员联系：' + response.responseText);
					}
					else
					{
						Ext.Msg.alert("提示",'提交当前评审批次时发生错误，请与系统管理员联系。');
					}
				}
			}
		);
}


/*获取下一个考生的方法*/
function getNextApplicant(jsonObject)
{
	//mask window
	maskWindow();
	
	Ext.Ajax.request(
		{
			url:window.getNextApplicantUrl,
			method:'POST',
			timeout:30000,
			params: {
				LanguageCd:'ZHS',
				OperationType:'NXT',
				BaokaoClassID:jsonObject['ps_class_id'],
                BaokaoPCID:jsonObject['ps_bkpc_id']
			},
			success:function(response)
			{
				//unmask window
				unmaskWindow();
				
				//返回值内容
                var jsonText = response.responseText;
                
				var jsonObject = null;
				
				try
				{
					var jsonObject = Ext.util.JSON.decode(jsonText);
	                /*判断服务器是否返回了正确的信息*/
	                if(jsonObject.state.errcode == 1){
	                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
	                }else{
	                	jsonObject = jsonObject.comContent;
						
						if(jsonObject.error_code != '0')
						{
							Ext.Msg.alert("提示",'获取考生信息时发生错误：' + jsonObject.error_decription );
						}
						else
						{
							if(window.KSINFO_JSON_DATA == null)
							{
								window.KSINFO_JSON_DATA = new Array();
							}
							
							var tmpBmbID = jsonObject['ps_ksh_bmbid'];
							if(KSINFO_JSON_DATA[tmpBmbID] != 'undefined' && KSINFO_JSON_DATA[tmpBmbID]!= null && KSINFO_JSON_DATA[tmpBmbID]!= '')
							{							
								Ext.Msg.alert("提示",'获取考生信息时发生错误，请与系统管理员联系：获取到重复的考生信息。');
							}
							else
							{
								KSINFO_JSON_DATA[tmpBmbID] = jsonObject;
								
								//加载指定考生评审信息页面并显示
								var tzEObject = new tzEvaluateObject();
	                            tzEObject.baokaoClassID = jsonObject['ps_class_id'];
	                            tzEObject.baokaoClassName = jsonObject['ps_class_name'];
	                            tzEObject.baokaoPcID = jsonObject['ps_bkpc_id'];
	                            tzEObject.baokaoPcName = jsonObject['ps_baok_pc'];
								tzEObject.applicantName = jsonObject['ps_ksh_xm'];
	                            tzEObject.applicantInterviewID = jsonObject['ps_msh_id'];
								tzEObject.applicantBaomingbiaoID = jsonObject['ps_ksh_bmbid'];
								
								//获取新的局部数据，并使用局部数据刷新当前页面
	                            var cls_pc_id = jsonObject['ps_class_id'] +"_" + jsonObject['ps_bkpc_id'];
								getPartBatchDataByBatchId(cls_pc_id,loadApplicantData,tzEObject,'NXT');
							}
						}
	                }
					
				}
				catch(e1)
				{					
					if(window.evaluateSystemDebugFlag == 'Y')
					{
						Ext.Msg.alert("提示",'获取考生信息时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
					}
					else
					{
						Ext.Msg.alert("提示",'获取考生信息时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
					}
				}
			},
			failure:function(response)
			{
				//unmask window
				unmaskWindow();
					
				if(window.evaluateSystemDebugFlag == 'Y')
				{
					Ext.Msg.alert("提示",'获取考生信息失败，请与系统管理员联系：' + response.responseText);
				}
				else
				{
					Ext.Msg.alert("提示",'获取考生信息失败，请与系统管理员联系。');
				}
			}
		}
	);
}

function createApplicantList(jsonObject)
{	
	var store1 = Ext.create('Ext.data.Store', {
      fields: getApplicantListColumnHeaders(jsonObject['ps_data_kslb']['ps_ksh_list_headers']),
      data: getApplicantListColumnValues(jsonObject['ps_data_kslb']['ps_ksh_list_contents']),
      sorters: [{
          property: 'ps_ksh_dt',
          direction: 'DESC'  
      }], 
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
      minHeight:200,
      style:"overflow-x:auto",
      columns: getApplicantListColumns(jsonObject['ps_data_kslb']['ps_ksh_list_headers'],jsonObject["ps_show_deviation"]),
      title: '当前已归属您的评审考生列表',
      viewConfig: {
          stripeRows: true,
          enableTextSelection: true,
          forceFit: true,
          scrollOffset: 0
      },
      tbar: [
      				{
      					text: '获取下一个考生',
      					tooltip:'单击此按钮获取下一个待评审考生及其相关材料，并进入该考生材料评审主页面。',
      					width:120,
      					pressed: true,
      					handler: function()
      									{
      										getNextApplicant(jsonObject);
      									}
      				}/*,
      				{
      					text: '返回批次列表页面',
      					tooltip:'单击此按钮返回评审批次列表页面。',
      					width:130,
      					pressed: true,
      					handler: function(){showPreviousEvaluatePage(1);}
      				},
      				'->',
      				{
      					text:'提  交',
      					tooltip:'单击此按钮提交当前评审批次。',
      					pressed: true,
      					handler : function()
      										{
      											Ext.Msg.confirm('提示', '是否提交本次评议的全部考生信息？<br />提交后将无法对考生评议成绩进行修改，是否继续？', function(button) {
													if (button === 'yes') {
														
														submitEvaluateBatch(jsonObject['ps_bkfx_id']);
														
													} else {

													}
												});
												
      										}
      				},
      				{
      					text:'打印评分总表',
      					tooltip:'单击此按钮打印当前评审批次评分统计表。',
      					pressed: true,
      					handler : function()
      										{
      											printStatisticsTotalTable(jsonObject['ps_bkfx_id'],getBatchNameById(jsonObject['ps_bkfx_id']));
      										}
      				}*/
      			],
      fbar: [
      				{
      					text: '获取下一个考生',
      					tooltip:'单击此按钮获取下一个待评审考生及其相关材料，并进入该考生材料评审主页面。',
      					width:120,
      					handler: function()
      									{
      										getNextApplicant(jsonObject);
      									}
      				},
      				/*{
      					text: '返回批次列表页面',
      					tooltip:'单击此按钮返回评审批次列表页面。',
      					width:130,
      					handler: function(){showPreviousEvaluatePage(1);}
      				},*/
      				'->',
					'总体提交状态：<span id="ps_kslb_submtall_'+ jsonObject['ps_class_id']+"_"+jsonObject['ps_bkpc_id'] +'" style="margin-right:10px;font-weight:bold;">【' + ps_kslb_submtall_status +'】</span>',
      				{
      					text:'提交全部考生数据',
      					tooltip:'单击此按钮提交当前评审批次。',
      					handler : function()
      										{
                                                if(jsonObject['ps_kslb_submtall']=="Y" || $("#ps_kslb_submtall_"+(jsonObject['ps_class_id']+"_"+jsonObject['ps_bkpc_id'])).html() == '【已提交】'){
                                                	Ext.Msg.alert("提示","您已经提交，不能重复提交！");
                                                }else{
                                                    Ext.Msg.confirm('提示', '评审完成后都需要提交全部考生的数据！<br />是否提交本次评议的全部考生信息？<br />提交后将无法对考生评议成绩进行修改，是否继续？', function(button) {
                                                        if (button === 'yes') {

                                                            submitEvaluateBatch(jsonObject['ps_class_id'],jsonObject['ps_bkpc_id']);

                                                        } else {

                                                        }
                                                    });
                                                }
												
      										}
      				},
      				{
      					text:'打印评审总表',
      					tooltip:'单击此按钮打印当前评审批次评审统计表。',
      					handler : function()
							{
                                printStatisticsTotalTable(jsonObject['ps_class_id'],jsonObject['ps_bkpc_id'],jsonObject['ps_class_mc'],jsonObject['ps_bkpc_mc']);
							}
      				}
      			]
  });
  
  grid.on('cellClick', function(gridViewObject,cellHtml,colIndex,dataModel,rowHtml,rowIndex){
					var rec = store1.getAt(rowIndex);
					//var clickColName = rec.self.getFields()[colIndex]['name'];
					var clickColName = gridViewObject.grid.columns[colIndex]["dataIndex"];
					
					gridViewObject.getSelectionModel().getSelection()[0].index = rowIndex;
					
					if(clickColName == 'pw_evaluate_col' || rec.get(clickColName) == rec.get('ps_ksh_id'))
					{
						var tmpKshID = jQuery.trim(rec.get('ps_ksh_bmbid'));
						
						if(tmpKshID == null || tmpKshID == '' || tmpKshID == 'undefined')
						{
							Ext.Msg.alert("提示",'系统错误：无法获取指定考生对应的编号。');
						}
						else
						{
							//mask window
							maskWindow();
							
							//加载指定考生评审信息页面并显示
							var tzEObject = new tzEvaluateObject();
                            tzEObject.baokaoClassID = jsonObject['ps_class_id'];
                            tzEObject.baokaoClassName = jsonObject['ps_class_mc'];
                            tzEObject.baokaoPcID = jsonObject['ps_bkpc_id'];
                            tzEObject.baokaoPcName = jsonObject['ps_baok_pc'];
							tzEObject.applicantName = rec.get('ps_ksh_xm');
							tzEObject.applicantInterviewID = rec.get('ps_ksh_bmbid');
							tzEObject.applicantBaomingbiaoID = rec.get('ps_ksh_bmbid');

							loadApplicantData(tzEObject);
						}
					}
				}
  			 );
  grid.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
  
  //window.library_main_evalute_page_ks_grid[jsonObject['ps_bkfx_id']] = grid;
    window.library_main_evalute_page_ks_grid[jsonObject['ps_class_id']+"_"+jsonObject['ps_bkpc_id']] = grid;
  
  return grid;
}

function createStatisticsArea(batchId,jsonObject)
{
	var itemArray = new Array();
	
	
	/*创建概要信息显示区*/
	var gaiyaoArea = {
			xtype: 'component',
			html: Ext.String.format('<ul><li>' + jsonObject['ps_gaiy_info'] + '</li></ul>'),
			style: 'margin-bottom:10px;margin-top:2px;font-size:13px;'
	 };
	itemArray.push(gaiyaoArea);

	/*创建评委评分平均分统计表格*/
	var tjPJFgrid = createPJFTJGrid(batchId,jsonObject);
	itemArray.push(tjPJFgrid);

	/*创建评委评分分布统计表格*/
	var tjFBgrid = createFenbuGrid(jsonObject);
	itemArray.push(tjFBgrid);

	/*创建评委评分平均分统计图表*/
	var tjCharts1 = createStatisticsChart(jsonObject,null,"100%");
	if(tjCharts1 != null)
	{
		itemArray.push(tjCharts1);
	}
	
	/*创建评委评分分布统计图表*/
	var tjCharts2 = createStatisticsCharts(jsonObject,null,"100%");
	if(tjCharts2 != null)
	{
		itemArray.push(tjCharts2);
	}
	
	
	var tjArea = Ext.create('Ext.Panel',
		 {
			title:'评审统计信息区',
			collapsible:true,
			margin:'0 0 2 0',
			bodyPadding:15,
			autoHeight:true,
			width:"100%",
			layout: {
                type: 'vbox',
                align: 'stretch'
            },
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
		window.evaluateDfPanelDisplayZf ="Y";

		var itemArray = new Array();

		//创建评委评审主页面页头区
		var mainPageHeader = createMainPageHeader(jsonObject);
		mainPageHeader.on({expand:function(){myPageSlider[0].adjustHeight();},collapse:function(){myPageSlider[0].adjustHeight();}});
		itemArray.push(mainPageHeader);

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
				margin:0,
				//bodyPadding:"0 17px 0 0",
				cls:'empty',
				bodyStyle:'background:#ffffff',
				//layout:{type:'table',columns: 1},
				width:"100%",
				autoHeight:true,
				items:itemArray
			});
	}
	mainPageFrame.on("resize",function(t,width,height){
		t.suspendEvent("resize");
		var mainEl = $(".main")[0];
		//判断是否有滚动条
		if(height>Ext.getBody().getHeight()-$(".top_main").height()-$(".footer").height()){
			t.setWidth(Ext.getBody().getWidth()-17);
		}else{
			t.setWidth(Ext.getBody().getWidth());
		}
		t.resumeEvent("resize");
		
		t.updateLayout();
		
	})
	
	return mainPageFrame;
}

/*加载指定考生数据，并进入该考生详细资料评审页面*/
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

	  Ext.Msg.alert("提示",'材料评审系统发生错误：评审班级批次信息丢失。');
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
				//unmask window
				unmaskWindow();
				
				//返回值内容
                var jsonText = response.responseText;
                
				var jsonObject = null;
				
				try
				{
					var jsonObject = Ext.util.JSON.decode(jsonText);
	                /*判断服务器是否返回了正确的信息*/
	                if(jsonObject.state.errcode == 1){
	                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
	                }else{
	                	jsonObject = jsonObject.comContent;
						
						if(jsonObject.error_code != '0')
						{					
							loadSuccess = false;
							Ext.Msg.alert("提示",'刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误：' + jsonObject.error_decription);
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
							refreshBatchDataByBatchId(jsonObject,'ps_ksh_bmbid',applicantObject.applicantBaomingbiaoID);
							
							//回调指定函数
							if(operationType == 'NXT')
							{//因为获取下一个考生而产生的回调，该回调将导致当前页面切换到指定考生资料评审主页面
								callBackFunction(applicantObject);
							}
							else
							{
								if(operationType == 'RFH') {
									//保存并获取下一个考生使用，报名表加载完成前不能操作,显示mask窗口
									maskWindow();
									console.log("getPartBatchDataByBatchId-->"+applicantObject.applicantBaomingbiaoID);
								} else {
									//其他暂无操作
								}
							}
							
							if(tipMessage != null && tipMessage != '' && tipMessage != 'undefined')
							{
								Ext.Msg.alert("提示",tipMessage);
							}
						}
						
	                }
					
				}
				catch(e1)
				{
					loadSuccess = false;
					if(window.evaluateSystemDebugFlag == 'Y')
					{
						Ext.Msg.alert("提示",'刷新当前评审批次[' + batchId + ']数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']' + response.responseText);
					}
					else
					{
						Ext.Msg.alert("提示",'刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1.description + ']。');
					}
				}
			},
			failure:function(response)
			{
				//unmask window
				unmaskWindow();
				
				loadSuccess = false;
				if(window.evaluateSystemDebugFlag == 'Y')
				{
					Ext.Msg.alert("提示",'刷新当前评审批次[' + batchId + ']数据时发生错误，请与系统管理员联系：' + response.responseText);
				}
				else
				{
					Ext.Msg.alert("提示",'刷新当前评审批次[' + getBatchNameById(batchId) + ']数据时发生错误，请与系统管理员联系。');
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


	//myPageSlider[0].showAllDivpages();


	/*刷新评委评审概要信息*/
	try
	{
		var pwAbstractInfo = Ext.String.format('<ul><li>' + jsonObject['ps_gaiy_info'] + '</li></ul>');
		//lw rfObject1['items']['items'][1]['items']['items'][0].getEl().setHTML(pwAbstractInfo);
        rfObject1['items']['items'][1]['items']['items'][0].getEl().setHtml(pwAbstractInfo);
	}
	catch(e1)
	{
		;
	}

	/*刷新实际打分统计数据（平均分、均方差等）*/
	try
	{
		var myDataModel = getDataModelForPJFTJGrid(jsonObject);
		var pwStatisticsGrid = rfObject1['items']['items'][1]['items']['items'][1];

		pwStatisticsGrid.getStore().loadData(myDataModel['gridData']);
	}
	catch(e2)
	{
		;
	}

	/*刷新实际打分分布统计数据*/
	try
	{
		var pwFenbuGridStore = rfObject1['items']['items'][1]['items']['items'][2].getStore();
		pwFenbuGridStore.loadData(getDataForFenbuGrid(jsonObject));
	}
	catch(e3)
	{
		;
	}

	/*刷新实际打分统计数据对比柱状图*/
    if(window.evaluateDfPanelDisplayZf == "Y") {
        try {
            var chartDataModel = getDataModelForStatisticsChart(jsonObject);
            var pwColumnChart = rfObject1['items']['items'][1]['items']['items'][3]['items']['items'][0];


            pwColumnChart['axes']['items'][0]['maximum'] = chartDataModel['maxValue'];
            pwColumnChart['axes']['items'][0]['minimum'] = chartDataModel['minValue'];


            pwColumnChart.redraw(true);
            pwColumnChart.getStore().loadData(chartDataModel['chartData']);
        }
        catch (e4) {
            ;
        }
    }

	/*刷新实际打分分布统计数据曲线图*/
	try
	{
		var pwFenbuCharts = rfObject1['items']['items'][1]['items']['items'][4]['items']['items'];
		for(var i=0;i<pwFenbuCharts.length;i++)
		{
			var myStore = pwFenbuCharts[i]['items']['items'][0].getStore();
			myStore.loadData(getSubDataForFenbuChart(jsonObject['ps_data_fb'][i]['ps_fszb_fbsj']));
		}
	}
	catch(e5)
	{
		;
	}

	/*刷新评委当前已评审考生列表*/
	try
	{
		//更新总体提交状态
		var ps_kslb_submtall_status = (jsonObject['ps_kslb_submtall']=="Y"?"已提交":"未提交");
		//lw $("#ps_kslb_submtall_"+(jsonObject['ps_class_id']+"_"+jsonObject['ps_pc_id'])).html("【"+ps_kslb_submtall_status+"】");
        $("#ps_kslb_submtall_"+(jsonObject['ps_class_id']+"_"+jsonObject['ps_bkpc_id'])).html("【"+ps_kslb_submtall_status+"】");

		var currentSelectedRow = rfObject1['items']['items'][2].getSelectionModel().getSelection();
		var pwKaoshengListStore = rfObject1['items']['items'][2].getStore();

		pwKaoshengListStore.loadData(getApplicantListColumnValues(jsonObject['ps_data_kslb']['ps_ksh_list_contents']));

		if(dataIndexName == null || dataIndexValue == null)
		{
			if(currentSelectedRow != null && currentSelectedRow != 'undefined')
			{
				if(currentSelectedRow[0] != null && currentSelectedRow[0] != 'undefined')
				{
					autoHighlightRow(rfObject1['items']['items'][2],'ps_ksh_bmbid',currentSelectedRow[0].get('ps_ksh_bmbid'));
					autoHighlightRow(dfPageWest_grid[batchId],'ps_ksh_bmbid',currentSelectedRow[0].get('ps_ksh_bmbid'));

					//lw var tmpApplicantInterviewId = currentSelectedRow[0].get('ps_kaosheng_id');
                    var tmpApplicantInterviewId = currentSelectedRow[0].get('ps_ksh_bmbid');
					if(tmpApplicantInterviewId != null && tmpApplicantInterviewId != '' && tmpApplicantInterviewId != 'undefined')
					{
						window.myPageSlider[0].autoScrollHtmlTagId = 'ks_id_' + tmpApplicantInterviewId;
					}
				}
			}
		}
		else
		{
			autoHighlightRow(rfObject1['items']['items'][2],dataIndexName,dataIndexValue);
			autoHighlightRow(dfPageWest_grid[batchId],dataIndexName,dataIndexValue);

			//lw var tmpApplicantInterviewId = rfObject1['items']['items'][2].getSelectionModel().getSelection()[0].get('ps_kaosheng_id');
            var tmpApplicantInterviewId = rfObject1['items']['items'][2].getSelectionModel().getSelection()[0].get('ps_ksh_bmbid');
			if(tmpApplicantInterviewId != null && tmpApplicantInterviewId != '' && tmpApplicantInterviewId != 'undefined')
			{
				window.myPageSlider[0].autoScrollHtmlTagId = 'ks_id_' + tmpApplicantInterviewId;
			}
		}
        //alert(window.myPageSlider[0].autoScrollHtmlTagId + "=====");
	}
	catch(e6)
	{
	}

	//myPageSlider[0].hideAllDivpages();
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
				//返回值内容
                var jsonText = response.responseText;
                
				var jsonObject = null;
				
				try
				{
					var jsonObject = Ext.util.JSON.decode(jsonText);
	                /*判断服务器是否返回了正确的信息*/
	                if(jsonObject.state.errcode == 1){
	                	Ext.Msg.alert("提示",jsonObject.state.timeout==true?"您当前登录已超时或者已经退出，请重新登录！":jsonObject.state.errdesc);
	                }else{
	                	jsonObject = jsonObject.comContent;
						
						refreshBatchDataByBatchId(jsonObject,null,null);
	                }
					
				}
				catch(e1)
				{
					Ext.Msg.alert("提示",'局部数据刷新失败，JSON数据解析错误:' + e1.description);
				}
			},
			failure:function(response)
			{
				Ext.Msg.alert("提示",'局部数据刷新失败，服务器错误。');
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
		var tmpStore = rfObject1['items']['items'][1]['items']['items'][3]['items']['items'][0].getStore();
		retCCObject = createStatisticsChart(window.batchJSONArray[batchId],tmpStore,"100%");
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
		var pwFenbuCharts = rfObject1['items']['items'][1]['items']['items'][4]['items']['items'];
		for(var i=0;i<pwFenbuCharts.length;i++)
		{
			chartStoreArray[i] = pwFenbuCharts[i]['items']['items'][0].getStore();
		}
		
		retLCObject = createStatisticsCharts(window.batchJSONArray[batchId],chartStoreArray,"100%");
	}
	catch(e5)
	{
		;
	}
	
	return retLCObject;
}