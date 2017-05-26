function displayApplicantEvaluatePage(evaluateObject,callBackFunction,tipCount,scrollBackTagId)
{
	var tmpEvaluateObject = evaluateObject;
	var tmpClassId = evaluateObject.baokaoClassID;
	var tmpBatchId = evaluateObject.baokaoPcID;
	
	/*********************************************************************************
	生成中间图表区域的Panel ----- BEGIN
	*********************************************************************************/
	/**
	* 图表区域的Panel实例
	*/
	/*
	if(window.fschartPanel == null)
	{
		window.fschartPanel = {};
	}

	function createDfAreaCharts()
	{
		var chartArray = [];
		
		var chart1 = getColumnChartObject();
		chartArray.push(chart1);
		
		var chart2 = getLineChartObject();
		chartArray.push(chart2);
		
		fschartPanel[tmpBatchId] = Ext.create('Ext.Panel',
										{
											title:'评审打分统计信息区',
											collapsible:true,
											margins:'5 5 5 5',
											bodyPadding:'10 30 10 15',
											layout:'form',
											autoHeight:true,
											width:992,
											defaultType:'textfield',
											items:chartArray
										});
		fschartPanel[tmpBatchId].on({expand:function(){autoAdjustHeight();},collapse:function(){autoAdjustHeight();}});
	}
	*/
	/*********************************************************************************
	生成中间图表区域的Panel ----- END
	*********************************************************************************/

	/*********************************************************************************
	生成上侧考生搜索-----BEGIN
	*********************************************************************************/
	if(window.dfPageWest_KsSearch == null)
	{
		window.dfPageWest_KsSearch = {};
	}
	
	function createKsSearchForm(ksjson_data){
		
		var tmpKsSearchFormPanel = createMainPageSearchKSPanel(ksjson_data, "dfpanel");
	
		dfPageWest_KsSearch[tmpBatchId] = tmpKsSearchFormPanel;
	}
	/*********************************************************************************
	生成上侧考生搜索-----END
	*********************************************************************************/


	/*********************************************************************************
	生成左侧考生列表-----BEGIN
	*********************************************************************************/
	/**
	 * 定义数值型GridCell内容的字体颜色
	 * @param {Object} val
	 */
	function change(val) {
		if (val > 0) {
			return '<span style="color:green;">' + val + '</span>';
		} else if (val < 0) {
			return '<span style="color:red;">' + val + '</span>';
		}
		return val;
	}

	
	/**
	 * 定义数值型GridCell内容的字体颜色，并改为百分数 %
	 * @param {Object} val
	 */
	function pctChange(val) {
		if (val > 0) {
			return '<span style="color:green;">' + val + '%</span>';
		} else if (val < 0) {
			return '<span style="color:red;">' + val + '%</span>';
		}
		return val;
	}
	
	
	/**
	* 功能：在Grid初始化时，设置 姓名 一列的显示样式
	* 参数：
	*/
	function setRealNameClick(value, p, record){
		return Ext.String.format('<span style=" cursor:pointer; color:blue;" title="单击姓名评审该考生">{0}</span>', value);
	}

	
	/**
	* 功能：创建左侧考生列表的Grid Columns Config
	* 参数：ksjson_data  从服务器获取的考生的相关JSON数据
	*/
	function genExamineeGridColumnsConfig(ksjson_data){
	
		var examGridHeader = new Array();
		
		var GridHeaderJsonData = ksjson_data.ksGridHeader;
		
		for(var ExamineeGridFldName in GridHeaderJsonData){
			var columnConfig;
			if(ExamineeGridFldName=="ps_ksh_xm"){
				columnConfig =
					{
						text     : GridHeaderJsonData[ExamineeGridFldName],
						width	 : 70,
						flex     : 1,
						sortable : false,
						dataIndex: ExamineeGridFldName,
						renderer : setRealNameClick
					};
			}else if(ExamineeGridFldName=="ps_msh_id"){
					columnConfig =
					{
						text     : GridHeaderJsonData[ExamineeGridFldName],
						width    : 90,
						sortable : true,
						dataIndex: ExamineeGridFldName
					};	
			}else if(ExamineeGridFldName=="ps_ksh_xh"){	
					columnConfig =
					{
						text     : GridHeaderJsonData[ExamineeGridFldName],
						width    : 90,
						sortable : true,
						dataIndex: ExamineeGridFldName
					};
			}else{
				columnConfig =
					{
						text     : GridHeaderJsonData[ExamineeGridFldName],
						width    : 60,
						sortable : true,
						dataIndex: ExamineeGridFldName
					};
			}
			examGridHeader.push(columnConfig);
		}
	
		return examGridHeader;
		
	}	

	
	/**
	* 功能：创建左侧考生列表的的GridPanel实例
	* 参数：ksjson_data  从服务器获取的考生的相关JSON数据
	*/
	if(window.dfPageWest_grid == null)
	{
		window.dfPageWest_grid = {};
	}
	
	if(window.dfPageWest_grid_store == null)
	{
		window.dfPageWest_grid_store = {};
	}
	function createExamineeGrid(ksjson_data){


		/*获取当前评审班级ID*/
		var classId = tmpClassId;
		/*获取当前评审批次ID*/
		var batchId = tmpBatchId;
		if(classId == null || batchId == null) return;
		
		/*获取评审打分统计信息区对象*/
		var rfObject1 = window.batchEvaluateMainPageObject[classId+"_"+batchId];
		if(rfObject1 == null) return;
		
		//dfPageWest_grid_store[tmpBatchId] = rfObject1['items']['items'][3].getStore();
		var grid_pos_num = rfObject1['items']['items'].length - 1;
		dfPageWest_grid_store[tmpBatchId] = rfObject1['items']['items'][grid_pos_num].getStore();
	
		
		/****此为静态例子 BEGIN ***/
		/*	
		// 左侧考生列表示例数据
		var myData = [
		  //[姓名, 排名, 总分, 面试申请编号, 考生ID, 报名表ID, 报考方向ID]
			['张三',  1, 1,  10,12345, 999,111],
			['李四',  2, 2,  5, 23456, 888,222],
			['王五',  3, 3,  7, 232323, 777,333],
			['张三1', 4, 4,  3, 232345, 666,444],
			['张三2', 5, 5,  10,65432, 555,555],
			['张三3', 6, 6,  8, 23356, 444,666],
			['张三4', 7, 7,  9, 234578, 333,777],
			['张三5', 8, 8,  11,123578, 222,888],
			['张三6', 9, 9,  12,234578, 111,999],
			['张三',  10,10, 15,234567, 000,000]
		];
		dfPageWest_grid_store[tmpBatchId] = Ext.create('Ext.data.ArrayStore', {
								fields: [
								   {name: 'realname'},
								   {name: 'rank',      		type: 'int'},
								   {name: 'totalpoints',	type: 'float'},
								   {name: 'person_mssqID'},
								   {name: 'personid'},
								   {name: 'bmbID'},
								   {name: 'bkfxID'}
								],
								data: myData
							});
		 */	
		/****此为静态例子 END ***/
		
		// create the Grid
		dfPageWest_grid[tmpBatchId] = Ext.create('Ext.grid.Panel', {
			hideCollapseTool: true,
			store: dfPageWest_grid_store[tmpBatchId],
			columnLines: true,
			columns: genExamineeGridColumnsConfig(ksjson_data),
			title: '请给以下考生打分',
			viewConfig: {
				stripeRows: true
			}
		});
		dfPageWest_grid[tmpBatchId].on({'cellclick':function(gridViewObject,cellHtml,colIndex,dataModel,rowHtml,rowIndex)
			{
				/*var rec = dfPageWest_grid_store[tmpBatchId].getAt(rowIndex);
				var clickColName = rec.self.getFields()[colIndex]['ps_ksh_xm'];
			
				window.myPageSlider[0].autoScrollHtmlTagId = 'ks_id_' + rec.get('ps_ksh_id');
																									
				gridViewObject.getSelectionModel().getSelection()[0].index = rowIndex;

				if(clickColName=='ps_ksh_id')
				{
					// mask window
					maskWindow();
					
					changeExaminee(evaluateObject.classID,evaluateObject.baokaoPcID , rec.get('ps_ksh_bmbid'));
				}*/

				var rec = dfPageWest_grid_store[tmpBatchId].getAt(rowIndex);

				window.myPageSlider[0].autoScrollHtmlTagId = 'ks_id_' + rec.get('ps_ksh_id');

				gridViewObject.getSelectionModel().getSelection()[0].index = rowIndex;
				autoHighlightRow(window.library_main_evalute_page_ks_grid[tmpClassId+"_"+tmpBatchId],'ps_ksh_bmbid',rec.get('ps_ksh_bmbid'));


				//点击姓名列
				if(colIndex==1) {
					// mask window
					maskWindow();

					changeExaminee(evaluateObject.baokaoClassID, evaluateObject.baokaoPcID , rec.get('ps_ksh_bmbid'));
				}
			}
		});
		
	}

	
	/**
	* 点击考生操作按钮后，在CenterPanel和EastPanel中加载该考生的信息
	*/
	function changeExaminee(ClassID,BatchID, KSH_BMBID){
		getKSJSONData(ClassID, BatchID, KSH_BMBID, 'update');
	}
	/*********************************************************************************
	生成左侧考生列表-----END
	*********************************************************************************/



	/*********************************************************************************************
	生成与该考生相关的 信息通知Panel、打分总体说明区Panel、基本信息Panel 三个HTML内容区域 ----- BEGIN
	*********************************************************************************************/
	/**
	* 说明信息——信息通知区域的Panel实例
	*/
	if(window.xxtzPanel == null)
	{
		window.xxtzPanel = {};
	}
	/**
	* 信息通知区域的高度
	*/
	if(window.xxtzPanel_Height == null)
	{
		window.xxtzPanel_Height = {};
	}
	if(window.xxtzPanel_Height[tmpBatchId] == null)
	{
		window.xxtzPanel_Height[tmpBatchId] = 180;
	}
	/**
	* 说明信息——打分总体说明区域的Panel实例
	*/
	if(window.dfInfoPanel == null)
	{
		window.dfInfoPanel = {};
	}
	/**
	* 说明信息区域的Panel实例
	*/
	if(window.smxxPanel == null)
	{
		window.smxxPanel = {};
	}
	/**
	* 基本信息区域的Panel实例
	*/
	if(window.ksInfoPanel == null)
	{
		window.ksInfoPanel = {};
	}
	/**
	* 基本信息区域的高度
	*/
	if(window.ksInfoPanel_Height == null)
	{
		window.ksInfoPanel_Height = {};
	}
	if(window.ksInfoPanel_Height[tmpBatchId] == null)
	{
		window.ksInfoPanel_Height[tmpBatchId] = 100;
	}

	if(window.evaluatePanel == null)
	{
		window.evaluatePanel = {};
	}
	function autoAdjustHeight()
	{
		var myTotalHeight = 0;
		
		if(dfAreaPanel[tmpBatchId] != null && window.evaluatePanel[tmpBatchId] != null)
		{		
			myTotalHeight = dfArea_Height[tmpBatchId] + 200;
			dfAreaPanel[tmpBatchId].setHeight(myTotalHeight);
			
			if(myTotalHeight <= 500)
			{
				myTotalHeight = 500;
			}
			
			window.evaluatePanel[tmpBatchId].setHeight(myTotalHeight);
			window.evaluatePanel[tmpBatchId].doLayout();
			
			window.myPageSlider[0].adjustHeight();
		}
	}
	
	
	/**
	* 功能：创建考生HTML相关信息的Panel
	* 参数：从服务器获取的考生的相关JSON数据
	*/
	function createKSInfoPanel(ksinfoJSON){
	
		var show_ksinfohtml=genKSInfoHTML(ksinfoJSON);
		
		ksInfoPanel[tmpBatchId] = Ext.create('Ext.panel.Panel', {
			title: '基本信息',
			collapsible: true,
			collapsed:false,
			hideCollapseTool: true,
			margins:'5 0 5 5',
			//layout:'accordion',
			layout:'fit',
			height: ksInfoPanel_Height[tmpBatchId],
			width:992,
			colspan : 2,
			html:show_ksinfohtml
		});
		//ksInfoPanel[tmpBatchId].on({expand:function(){autoAdjustHeight();},collapse:function(){autoAdjustHeight();}});
	}

	
	/**
	* 功能：创建基本信息区域的HTML代码
	* 参数：从服务器获取的考生的相关JSON数据
	*/
	function genKSInfoHTML(ksinfoJSON){
		var rtn_ksinfohtml = '<table border="0" width="100%" height="100%" style="font-size:12px;">';

		if(ksinfoJSON.interviewApplyId=="undefined") ksinfoJSON.interviewApplyId = "";
		if(ksinfoJSON.name=="undefined") ksinfoJSON.name = "";
		
		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+ksinfoJSON.bmbId+'","TZ_APP_TPL_ID":"'+ksinfoJSON.psBmbTplId+'","isReview":"Y"}}';
		var bmb_url = ContextPath + "/dispatcher" + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
		
		var three_btn_html = '<a href="'+bmb_url+'" target="_blank" title="打开在线报名表"><span style="color:blue;">新开窗口看考生材料</span></a>';

		rtn_ksinfohtml += '<tr height="30"><td style="font-weight:bold;" width="127px">面试申请号：</td><td width="126px">'+ ksinfoJSON.interviewApplyId +'</td><td width="48px" style="font-weight:bold;">姓名：</td><td align="left" width="120px">'+ ksinfoJSON.name +'</td><td width="412px">'+ three_btn_html +'</td></tr>';
		if(ksinfoJSON.examineeTag!="" && ksinfoJSON.examineeTag!=null) {
			rtn_ksinfohtml += '<tr height="30"><td style="font-weight:bold;" width="127px">考生标签：</td><td colspan="4">' + ksinfoJSON.examineeTag + '</td></tr>';
			//【初筛淘汰】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;【校友推荐】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;【自主创业】
		}

		//材料评审成绩参考
		rtn_ksinfohtml += ksinfoJSON.materialReviewDesc;

		rtn_ksinfohtml += '</table>';

		//rtn_ksinfohtml += '<table border="0" width="100%" style="font-size:12px;">';
		//rtn_ksinfohtml += '<tr height="30"><td style="font-weight:bold;" width="127px">材料评审成绩参考：</td><td width="626px">'+ ksinfoJSON.materialReviewDesc +'</td></tr>';
		//本科学校和成绩【80】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;学校中获得的奖励【80】&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;英语水平【90】
		//rtn_ksinfohtml += '<tr height="30"><td style="font-weight:bold;" width="127px"></td><td width="626px">'+ ksinfoJSON.materialReviewDesc +'</td></tr>';
		//rtn_ksinfohtml += '</table>';
		
		return rtn_ksinfohtml;
	}
	/*********************************************************************************************
	生成与该考生相关的 信息通知Panel、打分总体说明区Panel、基本信息Panel 三个HTML内容区域 ----- END
	*********************************************************************************************/



	/*********************************************************************************
	调用JSON数据，动态生成打分区 ----- BEGIN
	*********************************************************************************/	
	/**
	* 常量，定义每个层级的缩进比例（即每个层级相对上一个层级的缩进大小）
	*/
	if(window.treenode_pdl_base_value == null)
	{
		window.treenode_pdl_base_value = {};
	}
	if(window.treenode_pdl_base_value[tmpBatchId] == null)
	{
		window.treenode_pdl_base_value[tmpBatchId] = 40;
	}

	
	/**
	* 打分区 Ext.FormPanel 的 Items 配置数组
	*/
	if(window.allDfAreaFormPanelFieldContainer_config == null)
	{
		window.allDfAreaFormPanelFieldContainer_config = {};
	}
	if(window.allDfAreaFormPanelFieldContainer_config[tmpBatchId] == null)
	{
		window.allDfAreaFormPanelFieldContainer_config[tmpBatchId] = new Array();
	}

	
	/**
	* 标准说明、参考问题、面试方法 TIPS 的配置数组
	*/
	if(window.dfArea_gzsm_ckwt_config == null)
	{
		window.dfArea_gzsm_ckwt_config = {};
	}
	if(window.dfArea_gzsm_ckwt_config[tmpBatchId] == null)
	{
		window.dfArea_gzsm_ckwt_config[tmpBatchId] = new Array();
	}


	/**
	* 标准说明、参考问题、面试方法 TIPS 的对象数组（在刷新打分区域（非IE）时使用）
	*/
	if(window.dfArea_gzsm_ckwt_extObj == null)
	{
		window.dfArea_gzsm_ckwt_extObj = {};
	}
	if(window.dfArea_gzsm_ckwt_extObj[tmpBatchId] == null)
	{
		window.dfArea_gzsm_ckwt_extObj[tmpBatchId] = new Array();
	}
	
	
	/**
	* 打分区域的高度，各FieldContainer高度的和
	*/
	if(window.dfArea_Height == null)
	{
		window.dfArea_Height = {};
	}
	if(window.dfArea_Height[tmpBatchId] == null)
	{
		window.dfArea_Height[tmpBatchId] = 0;
	}


	/**
	* 是否执行更改父亲节点显示值得标识符，Y - 更改，N - 不更改
	*/
	if(window.doChangeFieldValue == null)
	{
		window.doChangeFieldValue = {};
	}
	if(window.doChangeFieldValue[tmpBatchId] == null)
	{
		window.doChangeFieldValue[tmpBatchId] = "Y";
	}
	
	
	/**
	* 保存每个节点 "标准说明"、"参考问题" 、"面试方法"的ID值，用于在页面生成后统一给他们增加 TIPS
	*/
	if(window.allLeavesNodeData == null)
	{
		window.allLeavesNodeData = {};
	}
	if(window.allLeavesNodeData[tmpBatchId] == null)
	{
		window.allLeavesNodeData[tmpBatchId] = new Array();
	}

	/**
	 * 保存每个节点 "参考资料" 的ID值，用于在页面生成后统一给他们增加 TIPS
	 */
	if(window.allLeavesNodeDataCkzl == null)
	{
		window.allLeavesNodeDataCkzl = {};
	}
	if(window.allLeavesNodeDataCkzl[tmpBatchId] == null)
	{
		window.allLeavesNodeDataCkzl[tmpBatchId] = new Array();
	}


	/**
	* 功能：创建非叶子节点的实例对象
	* 参数：
	*		field_label		节点名称
	*		field_level		所处层级，整型，1,2,3,4...
	*		field_value		节点的分值
	*		field_name		文本输入框的名称
	*		field_parent_id	父节点的name值
	*/
	function createParentFieldContainer(field_label,field_level,field_value,field_name,field_parent_id){
		var pdl_value = treenode_pdl_base_value[tmpBatchId] * (field_level-1) + 5; //每个层级以 20px 的倍数向右缩进
		var thisFieldContainerHeight = 30;
		field_value = $.trim(field_value);
		if(field_value=="") field_value = "--";
		if(field_level==1 && field_value != "--"){
			field_value = '<span style="color:red;font-weight:bold;">' + field_value + '</span>'; //若是第一项“总分”，则加粗
		}
		
		var parentFieldContainer = new Ext.form.FieldContainer({
				fieldLabel		: '<span style="padding-left: '+ pdl_value +'px;font-weight:bold;">'+ field_label +'</span>',
				combineErrors	: false,
				labelWidth		: 150,
				height			: thisFieldContainerHeight,
				defaults		: {hideLabel: true},
				items			: [{
									xtype: 'displayfield',
									name : field_name,
									value: field_value,
									listeners: {
												   change : function(thisfield,newValue,oldValue){
														   if(field_parent_id!="" && doChangeFieldValue[tmpBatchId]=="Y")
															   dfAreaPanel[tmpBatchId].getForm().findField(field_parent_id).setValue("--");
												  }
										}
								  }]
		});
		
									
		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(parentFieldContainer);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}


	/**
	* 功能：创建（打分）叶子节点的实例对象，生成 标准说明、参考问题、面试方法、参考资料 的TIP数组
	* 参数：
	*		field_label		节点名称
	*		field_level		所处层级，整型，1,2,3,4...
	*		field_value		节点的分值
	*		field_name		文本输入框的名称
	*		point_begin		分值下限
	*		point_end		分值上限
	*		bzsm_content	标准说明内容
	*		ckwt_content	参考问题内容
	*		msff_content    面试方法内容
	*		ckzl_content    参考资料内容
	*		field_parent_id	父节点的name值
	*		score_model     成绩模型ID
	*		bmb_id          报名表ID*
	*/
	function createLeafPointFieldContainer(field_label,field_level,field_value,field_name,point_begin,point_end,bzsm_content,ckwt_content,msff_content,ckzl_content,field_parent_id,score_model,bmb_id){
		var pdl_value = treenode_pdl_base_value[tmpBatchId] * (field_level-1) + 5; //每个层级以 20px 的倍数向右缩进
		var pointbzsm_id = "pointbzsm_"+ field_name + tmpBatchId;  //标准说明的ID
		var pointckwt_id = "pointckwt_"+ field_name + tmpBatchId;  //参考问题的ID
		var pointmsff_id = "pointmsff_"+ field_name + tmpBatchId;  //面试方法的ID
		var pointckzl_id = "pointckzl_"+ field_name + tmpBatchId;  //参考资料的ID
		var thisFieldContainerHeight = 30;
		
		if(field_value==""){
			if(point_begin<0) 
				field_value = 0;
			else
				field_value = point_begin;
		}

		//标准
		var bzsmItem;
		if(bzsm_content!=null&&bzsm_content!="") {
			bzsmItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointbzsm_id+'">标准</span>',
				width		: 60
			}
		} else {
			bzsmItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 60
			}
		}

		//说明
		var ckwtItem;
		if(ckwt_content!=null&&ckwt_content!="") {
			ckwtItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointckwt_id+'">说明</span>',
				width		: 60
			}
		} else {
			ckwtItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 60
			}
		}

		//面试方法
		var msffItem;
		if(msff_content!=null&&msff_content!="") {
			msffItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointmsff_id+'">面试方法</span>',
				width		: 90
			}
		} else {
			msffItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 90
			}
		}

		//参考资料
		var ckzlItem;
		if(ckzl_content!=null&&ckzl_content!="") {
			ckzlItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointckzl_id+'">参考资料</span>',
				width		: 90
			}
		} else {
			ckzlItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 90
			}
		}

		var leafPointFieldContainer = new Ext.form.FieldContainer({
			fieldLabel		: '<span style="padding-left: '+ pdl_value +'px">'+ field_label +'</span>',
			combineErrors	: false,
			layout			: 'hbox',
			height			: thisFieldContainerHeight,
			labelWidth		: 200,
			defaults		: {hideLabel: true},
			items			: [{
				xtype		: 'numberfield',
				name      	: field_name,
				fieldLabel	: field_label,
				value		: field_value,
				enableKeyEvents	: true,
				margin		: '0 5 0 0',
				allowBlank	: false,
				maxValue	: point_end,
				minValue	: point_begin,
				width		: 120,
				listeners	: {
					change : function(thisfield,newValue,oldValue){
						   if(field_parent_id!="" && doChangeFieldValue[tmpBatchId]=="Y")
						   		dfAreaPanel[tmpBatchId].getForm().findField(field_parent_id).setValue("--");
				  }
				}	
			},{
				xtype		: 'displayfield',
				value		: '（'+ point_begin +' ~ '+ point_end +'）',
				width		: 130
			},
			bzsmItem,
			ckwtItem,
			msffItem,
			ckzlItem
			]
		});
		
		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(leafPointFieldContainer);
		createLeafTipsConfig(pointbzsm_id,pointckwt_id,pointmsff_id,pointckzl_id,bzsm_content,ckwt_content,msff_content,ckzl_content,score_model,bmb_id,field_name);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}


	/**
	* 功能：创建（文本类）叶子节点的实例对象，生成 参考问题 的TIP数组
	* 参数：
	*		field_label		节点名称
	*		field_level		所处层级，整型，1,2,3,4...
	*		field_value		节点的值
	*		field_name		文本输入框的名称
	*		bzsm_content	标准说明内容
	*		ckwt_content	参考问题内容
	*		msff_content	参考问题内容
	*		ckzl_content	参考资料内容
	*		field_pyzs_sx	评语的限制字数上限
	*		field_pyzs_xx	评语的限制字数下限
	*	 	score_model     成绩模型ID
	*		bmb_id          报名表ID
	*/
	function createLeafCommentFieldContainer(field_label,field_level,field_value,field_name,bzsm_content,ckwt_content,msff_content,ckzl_content,field_pyzs_sx,field_pyzs_xx,score_model,bmb_id){
		var pdl_value = treenode_pdl_base_value[tmpBatchId] * (field_level-1) + 5; //每个层级以 20px 的倍数向右缩进
		var pointbzsm_id = "pointbzsm_"+ field_name + tmpBatchId;  //标准说明的ID
		var pointckwt_id = "pointckwt_"+ field_name + tmpBatchId;  //参考问题的ID
		var pointmsff_id = "pointmsff_"+ field_name + tmpBatchId;  //面试方法的ID
		var pointckzl_id = "pointckzl_"+ field_name + tmpBatchId;  //参考资料的ID

		var thisFieldContainerHeight = 84;
		field_value = field_value.replace('\\n',"\n");
		
		//根据评语字数下限，判断该字段是否必填
		var leafC_allowBlank = true;
		var leafC_maxLength = field_pyzs_sx;
		var leafC_minLength = field_pyzs_xx;
		if(field_pyzs_xx > 0){
			leafC_allowBlank = false;
		}

		//说明
		var ckwtItem;
		if(ckwt_content!=null&&ckwt_content!="") {
			ckwtItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointckwt_id+'">说明</span>',
				width		: 60
			}
		} else {
			ckwtItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 60
			}
		}
		
		var leafCommentFieldContainer = new Ext.form.FieldContainer({
			fieldLabel		: '<span style="padding-left: '+ pdl_value +'px">'+ field_label +'</span>',
			combineErrors	: false,
			layout			: 'hbox',
			labelWidth		: 200,
			defaults		: {hideLabel: true},
			items			: [{
				xtype     	: 'textareafield',
				name      	: field_name,
				fieldLabel	: field_label,
				value		: field_value,
				maxLength	: leafC_maxLength,
				minLength	: leafC_minLength,
				margin		: '0 5 0 0',
				allowBlank	: leafC_allowBlank,
				width		: 400,
				height		: thisFieldContainerHeight
			},ckwtItem]
		});
		
		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(leafCommentFieldContainer);
		createLeafTipsConfig(pointbzsm_id,pointckwt_id,pointmsff_id,pointckzl_id,bzsm_content,ckwt_content,msff_content,ckzl_content,score_model,bmb_id,field_name);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}


	/**
	* 功能：创建（下拉框类）叶子节点的实例对象，生成 标准说明、参考问题、面试方法 的TIP数组
	* 参数：
	*		field_label		节点名称
	*		field_level		所处层级，整型，1,2,3,4...
	*		field_value		节点的值
	*		field_name		文本输入框的名称
	*		bzsm_content	标准说明内容
	*		ckwt_content	参考问题内容
	*		msff_content	面试方法内容
	*   	ckzl_content	参考资料内容
	*		field_options	下拉框值
	*	 	score_model     成绩模型ID
	*		bmb_id          报名表ID
	*/
	function createLeafDropdownFieldContainer(field_label,field_level,field_value,field_name,bzsm_content,ckwt_content,msff_content,ckzl_content,field_options,score_model,bmb_id){
		var pdl_value = treenode_pdl_base_value[tmpBatchId] * (field_level-1) + 5; //每个层级以 40px 的倍数向右缩进
		var pointbzsm_id = "pointbzsm_"+ field_name + tmpBatchId;  //标准说明的ID
		var pointckwt_id = "pointckwt_"+ field_name + tmpBatchId;  //参考问题的ID
		var pointmsff_id = "pointmsff_"+ field_name + tmpBatchId;  //面试方法的ID
		var pointckzl_id = "pointckzl_"+ field_name + tmpBatchId;  //参考资料的ID

		var thisFieldContainerHeight = 40;
		
		var store = Ext.create("Ext.data.Store",{
			fields:['itemOptionId','itemOptionName'],
			data:field_options
		});

		//标准
		var bzsmItem;
		if(bzsm_content!=null&&bzsm_content!="") {
			bzItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointbzsm_id+'">标准</span>',
				width		: 60
			}
		} else {
			bzItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 60
			}
		}

		//说明
		var ckwtItem;
		if(ckwt_content!=null&&ckwt_content!="") {
			ckwtItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointckwt_id+'">说明</span>',
				width		: 60
			}
		} else {
			ckwtItem = {
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 60
			}
		}

		//面试方法
		var msffItem;
		if(msff_content!=null&&msff_content!="") {
			msffItem={
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointmsff_id+'">面试方法</span>',
				width		: 90
			}
		} else {
			msffItem={
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 90
			}
		}

		//参考资料
		var ckzlItem;
		if(ckzl_content!=null&&ckzl_content!="") {
			ckzlItem={
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '<span style="cursor:pointer;" id="'+pointckzl_id+'">参考资料</span>',
				width		: 90
			}
		} else {
			ckzlItem={
				xtype		: 'displayfield',
				fieldStyle	: 'text-align:right;',
				value		: '',
				width		: 90
			}
		}

		
		var leafDropdownFieldContainer = new Ext.form.FieldContainer({
			fieldLabel		: '<span style="padding-left: '+ pdl_value +'px">'+ field_label +'</span>',
			combineErrors	: false,
			layout			: 'hbox',
			labelWidth		: 200,
			defaults		: {hideLabel: true},
			items			: [{
				xtype     	: 'combobox',
				name      	: field_name,
				fieldLabel	: field_label,
				value		: field_value,
				store		: store,
				autoShow	: true,
				valueField	: 'itemOptionId',
				displayField: 'itemOptionName',
				queryMode	: 'local',
				editable	: false,
				margin		: '0 5 0 0',
				allowBlank	: false,
				width		: 250
			},
				bzsmItem,
				ckwtItem,
				msffItem,
				ckzlItem
			]
		});
		
		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(leafDropdownFieldContainer);
		createLeafTipsConfig(pointbzsm_id,pointckwt_id,pointmsff_id,pointckzl_id,bzsm_content,ckwt_content,msff_content,ckzl_content,score_model,bmb_id,field_name);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}


	/**
	* 功能：生成 标准说明、参考问题、面试方法 的TIP数组
	* 参数：
	*		pointckwt_id	标准说明的文字ID
	*		pointckwt_id	参考问题的文字ID
	*		pointmsff_id    面试方法的文字ID
	*		pointckzl_id    参考资料的文字ID
	*		bzsm_content	标准说明内容
	*		ckwt_content	参考问题内容
	*		msff_content	面试方法内容
	*		ckzl_content	参考资料内容
	*       scoreModel      成绩模型ID
	*       bmb_id          报名表ID
	*       cjxId           成绩项ID
	*/
	function createLeafTipsConfig(pointbzsm_id,pointckwt_id,pointmsff_id,pointckzl_id,bzsm_content,ckwt_content,msff_content,ckzl_content,scoreModel,bmb_id,cjxId){
	
		var ary1 = new Array(pointbzsm_id, bzsm_content, "标准");
		var ary2 = new Array(pointckwt_id, ckwt_content, "说明");
		var ary3 = new Array(pointmsff_id, msff_content, "面试方法");
		allLeavesNodeData[tmpBatchId].push(ary1, ary2,ary3);

		if(ckzl_content==null||ckzl_content=="") {
		} else {
			var ckzl_content_url = ContextPath + "/refMaterial/onload?classId="+tmpClassId+"&batchId="+tmpBatchId+"&appInsId="+bmb_id+"&model="+scoreModel+"&cjxId="+cjxId;
			//var ckzl_content_url = ContextPath + "/refMaterial/onload?classId=122&batchId=47&appInsId=200001&model=TZ_CLPS_MODEL&cjxId=XXHDJL";
			var ckzl_content = "<iframe src='"+ ckzl_content_url +"' frameborder='0' width='820' height='552'></iframe>";
			var ary4 = new Array(pointckzl_id, ckzl_content, "参考资料");
			allLeavesNodeDataCkzl[tmpBatchId].push(ary4);
		}
		
	}


	/**
	* 功能：生成打分区域表单"隐藏域"
	* 参数：
	* 		ClassIDval		班级编号
	*		BatchIDval		批次编号
	*		KSH_BMBIDval	考生报名表编号
	*/
	function createDfAreaHiddenField(ClassIDval,BatchIDval,KSH_BMBIDval){
		var thisFieldContainerHeight = 10;
		var dfArea_HiddenField = new Ext.form.FieldContainer({
			fieldLabel		: '',
			layout			: 'hbox',
			height			: thisFieldContainerHeight,
			defaults		: {hideLabel: true},
			items			: [{
				xtype	: 'hiddenfield',
				name	: 'LanguageCd',
				value	: 'ZHS'
			},{
				xtype	: 'hiddenfield',
				name	: 'OperationType',
				value	: ''
			},{
				xtype	: 'hiddenfield',
				name	: 'ClassID',
				value	: ClassIDval
			},{
				xtype	: 'hiddenfield',
				name	: 'BatchID',
				value	: BatchIDval
			},{
				xtype	: 'hiddenfield',
				name	: 'KSH_BMBID',
				value	: KSH_BMBIDval
			}]
		});
		
		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(dfArea_HiddenField);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}


	/**
	* 生成“计算总分”按钮
	*/
	/*
	function createCalbuttonConfig(){
		var thisFieldContainerHeight = 50;
		var calbtn = new Ext.button.Button({
										text	: '计算总分',
										maxWidth: 100,
										height	: 30,
										margin	: '10 10 10 0',
										handler : function() {
												if(dfAreaPanel[tmpBatchId].getForm().isValid()){
													var form = dfAreaPanel[tmpBatchId].getForm();
	
													form.findField("OperationType").setValue("CAL");
													
													form.submit({
														clientValidation: false,
														url: dfArea_Submit_URL,
														success: function(form, action) {
															try{
																if(action.result.error_code=="0"){
																	//刷新打分区
																	refreshDfAreaFormPanel(action.result);
																	//刷新当前考生在本地的缓存数据
																	updateKSJSONData(form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), action.result, false);
																	//更新全局缓存，进行局部刷新
																	getPartBatchDataByBatchId(evaluateObject.baokaoDirectionID,null,{applicantBaomingbiaoID:form.findField("KSH_BMBID").getValue()},'RFH');
																}else{
																	Ext.Msg.alert('失败', action.result.error_decription);
																}
															}
															catch(e1){
																alert('计算考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]总分时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']');
															}
														},
														failure: function(form, action) {
															switch (action.failureType) {
																case Ext.form.action.Action.CLIENT_INVALID:
																	Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
																	break;
																case Ext.form.action.Action.CONNECT_FAILURE:
																	Ext.Msg.alert('Failure', 'Ajax communication failed');
																	break;
																case Ext.form.action.Action.SERVER_INVALID:
																   Ext.Msg.alert('Failure', action.result.msg);
														   }
														}
													});
												}
										}
		});
		
		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(calbtn);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}
	*/


	/**
	* 生成底部其他按钮
	*/
	function createSavebuttonConfig(){
		var thisFieldContainerHeight = 150;
		var allBtn = {
				xtype: 'fieldcontainer',
				layout: 'hbox',
				items: [
					new Ext.button.Button({
						formBind: true,
						text	: '保 存',
						width	: 100,
						height	: 30,
						margin	: '10 10 10 0',
						handler : function() {
							if(dfAreaPanel[tmpBatchId].getForm().isValid()){
								// mask window
								maskWindow();
								
								var form = dfAreaPanel[tmpBatchId].getForm();
								
								form.findField("OperationType").setValue("SBM");
								
								try{
									var formParams = form.getValues();
									var formJson = Ext.JSON.encode(formParams);
									
									var comParams = "";
									comParams = '"update":['+formJson+']';
									
									var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"U","comParams":{'+comParams+'}}';
									
									Ext.Ajax.request({
										url : dfArea_Submit_URL,
				                        params : {tzParams:tzParams},
				                        timeout : 60000,
				                        async : true,
				                        success : function(response,opts) {
				                        	 //返回值内容
				                            var jsonText = response.responseText;
				                            try
				                            {
				                                var jsonObject = Ext.util.JSON.decode(jsonText);
												//判断服务器是否返回了正确的信息
												if (jsonObject.comContent.result==undefined || jsonObject.comContent.result == 0) {
													//刷新打分区
													refreshDfAreaFormPanel(jsonObject.comContent);

													//刷新当前考生在本地的缓存数据
													updateKSJSONData(form.findField("ClassID").getValue(), form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), jsonObject.comContent, false);
													//更新全局缓存，进行局部刷新
													getPartBatchDataByBatchId(evaluateObject.baokaoClassID+"_"+evaluateObject.baokaoPcID,null,{applicantBaomingbiaoID:form.findField("KSH_BMBID").getValue()},'RFH');

													//若返回报文中有提示信息，则显示该提示信息，否则显示“提交成功！”
													if ($.trim(jsonObject.comContent.message) != "") {
														Ext.Msg.alert('提示', jsonObject.comContent.message);
													} else {
														// unmask window
														unmaskWindow();

														Ext.Msg.alert('提示', "保存成功！");
													}

												} else {
													// unmask window
													unmaskWindow();

													Ext.Msg.alert('失败', jsonObject.comContent.resultMsg);
												}
				                            }
				                            catch(e1) {
												// unmask window
												//unmaskWindow();
													
												//alert('保存考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']');
											} 

										}
									});
									/*
									form.submit({
										clientValidation: false,
										url: dfArea_Submit_URL,
										success: function(form, action) {
											try{	
												if(action.result.error_code=="0"){
													//刷新打分区
													refreshDfAreaFormPanel(action.result);
												   
													//刷新当前考生在本地的缓存数据
													updateKSJSONData(form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), action.result, false);
													//更新全局缓存，进行局部刷新
													getPartBatchDataByBatchId(evaluateObject.baokaoDirectionID,null,{applicantBaomingbiaoID:form.findField("KSH_BMBID").getValue()},'RFH');
													
													//若返回报文中有提示信息，则显示该提示信息，否则显示“提交成功！”
													// unmask window
													unmaskWindow();
													if($.trim(action.result.error_decription)!=""){
														Ext.Msg.alert('提示', action.result.error_decription);
													}else{
														Ext.Msg.alert('提示', "保存成功！");	
													}
												} else {
													// unmask window
													unmaskWindow();
													
													Ext.Msg.alert('失败', action.result.error_decription);
												}
											} catch(e1){
												// unmask window
												unmaskWindow();
													
												alert('保存考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']');
											}
										},
										failure: function(form, action) {
											// unmask window
											unmaskWindow();
													
											switch (action.failureType) {
												case Ext.form.action.Action.CLIENT_INVALID:
													Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
													break;
												case Ext.form.action.Action.CONNECT_FAILURE:
													Ext.Msg.alert('Failure', 'Ajax communication failed');
													break;
												case Ext.form.action.Action.SERVER_INVALID:
												   Ext.Msg.alert('Failure', action.result.msg);
										   }
										}
									});
									*/
								} catch(eformsubmit){
									// unmask window
									unmaskWindow();
											
									var msg = '保存考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据时发生错误，请与系统管理员联系：错误的JSON数据[' + eformsubmit + ']';
									Ext.Msg.alert('提示', msg);
								}
							}
						}
					}),
					//隐藏此按钮了，功能就没有实现
					/*
					new Ext.button.Button({
						formBind: true,
						text	: '保存并获取下一个考生',
						width	: 160,
						height	: 30,
						margin	: 10,
						hidden	: true,
						handler : function() {
							if(dfAreaPanel[tmpBatchId].getForm().isValid()){
								// mask window
								maskWindow();
								
								var form = dfAreaPanel[tmpBatchId].getForm();
								
								form.findField("OperationType").setValue("SGN");
	
								try{
									var formParams = form.getValues();
									var formJson = Ext.JSON.encode(formParams);
									
									var comParams = "";
									comParams = '"update":['+formJson+']';
									
									var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"U","comParams":{'+comParams+'}}';
									
									Ext.Ajax.request({
										url : dfArea_Submit_URL,
				                        params : {tzParams:tzParams},
				                        timeout : 60000,
				                        async : false,
				                        success : function(response,opts) {
				                            //返回值内容
				                            var jsonText = response.responseText;
				                            try
				                            {
				                                var jsonObject = Ext.util.JSON.decode(jsonText);
				                                //判断服务器是否返回了正确的信息
				                                if(jsonObject.state.errcode == 0) {	
				                                	//更新本地缓存的考生数据
													updateKSJSONData(form.findField("ClassID").getValue(), form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), '', true);
													   
													//刷新打分区为“下一个”考生
													refreshDfAreaFormPanel(jsonObject.comContent);
												   
													//刷新其他的HTML文本区域
													refreshDfHTMLPanel(jsonObject.comContent);
												   
													//将该“下一个”考生的数据到本地
													updateKSJSONData(form.findField("ClassID").getValue(), form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), jsonObject.comContent, false);
												   
													//更新全局缓存，进行局部刷新
													getPartBatchDataByBatchId(evaluateObject.baokaoClassID+"_"+evaluateObject.baokaoPcID, null,{applicantBaomingbiaoID:form.findField("KSH_BMBID").getValue()},'RFH');
											   
											   		//若返回报文中有提示信息，则显示该提示信息，否则显示"提交成功！"
													if($.trim(jsonObject.comContent.message)!=""){
														Ext.Msg.alert('提示', jsonObject.comContent.message + "<br />请给下一个考生打分！");
													}else{
														Ext.Msg.alert('提示', "保存成功！请给下一个考生打分！");
													}
				                                } else {
				                                	// unmask window
													unmaskWindow();
									
													Ext.Msg.alert('失败', jsonObject.state.error);
				                                }
				                            }
				                            catch(e1){
												alert('保存考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据，并获取下一个考生时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']');
												
												// unmask window
												unmaskWindow();
											}
				                        }
									});

									<*
									form.submit({
										clientValidation: false,
										url: dfArea_Submit_URL,
										success: function(form, action) {
											try{
												if(action.result.error_code=="0"){
													
													//更新本地缓存的考生数据
												   updateKSJSONData(form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), '', true);
												   
												   //刷新打分区为“下一个”考生
												   refreshDfAreaFormPanel(action.result);
												   
												   //刷新其他的HTML文本区域
												   refreshDfHTMLPanel(action.result);
												   
												   //将该“下一个”考生的数据到本地
												   updateKSJSONData(form.findField("BatchID").getValue(), form.findField("KSH_BMBID").getValue(), action.result, false);
												   
												   //更新全局缓存，进行局部刷新
													getPartBatchDataByBatchId(evaluateObject.baokaoDirectionID,null,{applicantBaomingbiaoID:form.findField("KSH_BMBID").getValue()},'RFH');
											   
											   		//若返回报文中有提示信息，则显示该提示信息，否则显示"提交成功！"
													if($.trim(action.result.error_decription)!=""){
														Ext.Msg.alert('提示', action.result.error_decription + "<br />请给下一个考生打分！");
													}else{
														Ext.Msg.alert('提示', "保存成功！请给下一个考生打分！");
													}
													
												}else{
													// unmask window
													unmaskWindow();
									
													Ext.Msg.alert('失败', action.result.error_decription);
												}
											}
											catch(e1){
												alert('保存考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据，并获取下一个考生时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']');
												
												// unmask window
												unmaskWindow();
											}
										},
										failure: function(form, action) {
											// unmask window
											unmaskWindow();
												
											switch (action.failureType) {
												case Ext.form.action.Action.CLIENT_INVALID:
													Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
													break;
												case Ext.form.action.Action.CONNECT_FAILURE:
													Ext.Msg.alert('Failure', 'Ajax communication failed');
													break;
												case Ext.form.action.Action.SERVER_INVALID:
												   Ext.Msg.alert('Failure', action.result.msg);
										   }
										}
									});
									*>
								} catch(eformsubmit){
									alert('保存考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据，并获取下一个考生时发生错误，请与系统管理员联系：错误的JSON数据[' + eformsubmit + ']');
								}
							}
						}
					}),
					*/
					new Ext.button.Button({
						text	: '返回评审界面',
						width	: 100,
						height	: 30,
						margin	: 10,
						handler : function() {
									Ext.Msg.confirm('提示', '确定要返回吗？', function(button) {
										if (button === 'yes') {
											showPreviousEvaluatePage(1,false);	
										} 
									});
								}
					})
				]
		};
		
		allDfAreaFormPanelFieldContainer_config[tmpBatchId] = allDfAreaFormPanelFieldContainer_config[tmpBatchId].concat(allBtn);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight;
	}


	/**
	* 功能：创建考生个人信息、报名表下载、报名表新开窗口浏览、考生动态信息TIPS 的FieldContainer
	* 参数：
	*		ksinfoJSON	当前考生的JSON数据
	*/
	function createKSinfoFieldContainer(ksinfoJSON){
		var thisFieldContainerHeight = 0;
		
		var field_name = "ksinfo_" + tmpBatchId;
		
		//生成考生基本信息的HTML内容
		var show_ksinfohtml = genKSInfoHTML(userpoints);
		
		/*var parentFieldContainer = new Ext.form.FieldContainer({
											fieldLabel		: '',
											hideLabel		: true,
											combineErrors	: false,
											height			: thisFieldContainerHeight,
											defaults		: {hideLabel: true},
											items			: [{
																xtype: 'displayfield',
																name : field_name,
																value: '<div style="margin:0;" id="show_ksinfo_div'+tmpBatchId+'">'+show_ksinfohtml+'</div>'
															  }]
									});*/

		var parentFieldContainer = new Ext.container.Container({
			combineErrors	: false,
			//height			: thisFieldContainerHeight,
			height          : '100%',
			name            : field_name,
			html            : '<div style="margin:0;width: 793px;" id="show_ksinfo_div_'+tmpBatchId+'">'+show_ksinfohtml+'</div>'
		});

		allDfAreaFormPanelFieldContainer_config[tmpBatchId].push(parentFieldContainer);
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + thisFieldContainerHeight + 100;
	} 


	/**
	* 生成打分区域的Panel
	*/
	if(window.dfAreaPanel == null)
	{
		window.dfAreaPanel = {};
	}
	function createDfAreaFormPanel(doAddFieldContainer){
		var FieldContainerData = new Array();
		if(doAddFieldContainer) FieldContainerData = allDfAreaFormPanelFieldContainer_config[tmpBatchId];
		
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + 103;
		dfAreaPanel[tmpBatchId] = Ext.create("Ext.FormPanel",{
						//title		: '打分区',
			            header		: false,
						collapsible	: true,
						//hideCollapseTool: true,
						overflowY	: 'auto',
						margins		: '5 0 5 5',
						bodyPadding	: 10,
						width		: 992, 
						//minHeight	: 300,
						height		: dfArea_Height[tmpBatchId],
						layout		: 'form',
						items		: FieldContainerData
					});
		dfAreaPanel[tmpBatchId].on({
			expand:function(){
				autoAdjustHeight();
			},
			collapse:function(){
				Ext.fly("tz_evaluation_main").setScrollTop(0);
				autoAdjustHeight();
			}
		});
		dfAreaPanel[tmpBatchId].on({expand:function(){autoAdjustHeight();},collapse:function(){autoAdjustHeight();}});
					
	}


	/**
	* 功能： 生成实际打分区各节点的FieldContainer对象
	* 参数： 树节点的JSON数据
	* 返回： 新生成的树节点的FieldContainer对象
	*/
	function createTreeNodeContainer(node_data,bmb_id){
		
		var newFormField;
		
		if(node_data.itemType=="C"){ //是否评语类型的数据
			newFormField = createLeafCommentFieldContainer(node_data.itemName, node_data.itemLevel, node_data.itemComment, node_data.itemId, node_data.itemDfsm, node_data.itemCkwt,node_data.itemMsff,node_data.itemCkzl, node_data.itemCommentUpperLimit, node_data.itemCommentLowerLimit,node_data.scoreModelId,bmb_id);
		} else {
			if(node_data.itemType=="D"){ //是否下拉框类型的数据
				newFormField = createLeafDropdownFieldContainer(node_data.itemName, node_data.itemLevel, node_data.itemXlkId, node_data.itemId, node_data.itemDfsm, node_data.itemCkwt, node_data.itemMsff,node_data.itemCkzl,node_data.itemOptions,node_data.scoreModelId,bmb_id);
			} else {
				if(node_data.itemIsLeaf=="N"){
					//生成非叶子节点
					newFormField = createParentFieldContainer(node_data.itemName, node_data.itemLevel, node_data.itemValue, node_data.itemId, node_data.itemParentId);	
				} else {
					//生成叶子节点
					newFormField = createLeafPointFieldContainer(node_data.itemName, node_data.itemLevel, node_data.itemValue, node_data.itemId, node_data.itemLowerLimit, node_data.itemUpperLimit, node_data.itemDfsm, node_data.itemCkwt,node_data.itemMsff,node_data.itemCkzl, node_data.itemParentId,node_data.scoreModelId,bmb_id);
				}
			}
		}
		
		return newFormField;
	}


	/**
	* 功能： 刷新打分区域的值
	* 参数： 新的JSON数据
	*/
	function refreshDfAreaFormPanel(DfArea_NewJSON){
		if (Ext.isIE) { //IE浏览器的刷新方法
			refreshDfAreaInIE(DfArea_NewJSON);
		}else{	
			//refreshDfAreaInOthers(DfArea_NewJSON);
			refreshDfAreaInIE(DfArea_NewJSON); // 使用IE的刷新方式也可
		}
	}


	/**
	* 功能： 刷新打分区域的值，IE类浏览器
	* 参数： 新的JSON数据
	*/
	function refreshDfAreaInIE(DfArea_NewJSON){

		//销毁TIPs
		Ext.destroy(dfArea_gzsm_ckwt_extObj[tmpBatchId]);
		dfArea_gzsm_ckwt_extObj[tmpBatchId] = new Array();

		//销毁所有叶子节点增加参考资料TIPS
		for(var li=0;li<allLeavesNodeDataCkzl[tmpBatchId].length;li++) {
			var dfarea_leaftips_data_ckzl = allLeavesNodeDataCkzl[tmpBatchId][li];

			$("#"+ dfarea_leaftips_data_ckzl[0]).qtip('destroy',true);
		}

		allLeavesNodeDataCkzl[tmpBatchId] = new Array();


		//将更新父节点值得标识位改为N，即不更新；等赋值完后再改回Y
		doChangeFieldValue[tmpBatchId]="N";
		
		//重新生成打分区域的各个元素
		var item_data = DfArea_NewJSON.scoreContent;
		var nowForm = dfAreaPanel[tmpBatchId].getForm();
		
		for(var item_i=0;item_i<item_data.length;item_i++){

			var node_data = item_data[item_i];
			var nowFormField = nowForm.findField(node_data.itemId);
			if(nowFormField!=null){ //判断是否存在该字段，若存在则更新数值
				var setTreeNodeValue = $.trim(node_data.itemValue);
				if(node_data.itemIsLeaf=="N"){

					if(setTreeNodeValue==""){
						setTreeNodeValue="--";
					}else{
						if(node_data.itemLevel=='1' && item_i==0){
							setTreeNodeValue = '<span style="color:red;font-weight:bold;">' + setTreeNodeValue + '</span>';
						}
					}
				}
				else if(node_data.itemType=="B"){
					//数字成绩录入项
					if(setTreeNodeValue==""){
						if(node_data.itemUpperLimit<0)
							setTreeNodeValue="0";
						else
							setTreeNodeValue=node_data.itemLowerLimit;
					}
				} else if(node_data.itemType=="C"){
					//评语类型
					setTreeNodeValue = $.trim(node_data.itemComment);
					setTreeNodeValue = setTreeNodeValue.replace('\\n',"\n");
				} else if(node_data.itemType=="D") {
					//下拉框类型
					setTreeNodeValue = node_data.itemXlkId;
				}

				nowFormField.setValue(setTreeNodeValue);

				var bmb_id = DfArea_NewJSON.bmbId;
				var pointckzl_id = "pointckzl_"+ node_data.itemId + tmpBatchId;  //参考资料的ID
				var ckzl_content = node_data.itemCkzl;
				var scoreModel = node_data.scoreModelId;
				var cjxId = node_data.itemId;

				if(ckzl_content==null||ckzl_content=="") {
				} else {
					var ckzl_content_url = ContextPath + "/refMaterial/onload?classId="+tmpClassId+"&batchId="+tmpBatchId+"&appInsId="+bmb_id+"&model="+scoreModel+"&cjxId="+cjxId;
					//var ckzl_content_url = ContextPath + "/refMaterial/onload?classId=122&batchId=47&appInsId=200001&model=TZ_CLPS_MODEL&cjxId=XXHDJL";
					var ckzl_content = "<iframe src='"+ ckzl_content_url +"' frameborder='0' width='820' height='552'></iframe>";
					var ary3 = new Array(pointckzl_id, ckzl_content, "参考资料");
					allLeavesNodeDataCkzl[tmpBatchId].push(ary3);
				}

			}else{ //若不存在，则创建一个新的字段
				var newFormField = createTreeNodeContainer(node_data,DfArea_NewJSON.bmbId);
			}
		}

		//给所有叶子节点增加参考资料TIPS
		for(var li=0;li<allLeavesNodeDataCkzl[tmpBatchId].length;li++) {
			var dfarea_leaftips_data_ckzl = allLeavesNodeDataCkzl[tmpBatchId][li];

			//createQTips(dfarea_leaftips_data_ckzl);
			tzCreateTips(dfarea_leaftips_data_ckzl);
		}

			
		//更新隐藏域中的值
		nowForm.findField("LanguageCd").setValue("ZHS");
		nowForm.findField("OperationType").setValue("");
		nowForm.findField("ClassID").setValue(DfArea_NewJSON.classId);
		nowForm.findField("BatchID").setValue(DfArea_NewJSON.applyBatchId);
		nowForm.findField("KSH_BMBID").setValue(DfArea_NewJSON.bmbId);
		
		
		//重新生成TIPs
		Ext.each(dfArea_gzsm_ckwt_config[tmpBatchId], function(config) {
			dfArea_gzsm_ckwt_extObj[tmpBatchId].push(Ext.create('Ext.tip.ToolTip', config));
		});
	
		//将更新父节点值得标识位改为Y
		doChangeFieldValue[tmpBatchId]="Y";
		
		var library_main_evalute_page_ks_grid_idx = window.library_main_evalute_page_ks_grid[tmpClassId+"_"+tmpBatchId].getSelectionModel().getSelection();
		if(library_main_evalute_page_ks_grid_idx != null && library_main_evalute_page_ks_grid_idx != 'undefined')
		{
			if(library_main_evalute_page_ks_grid_idx[0] != null && library_main_evalute_page_ks_grid_idx[0] != 'undefined')
			{
				autoHighlightRow(dfPageWest_grid[tmpBatchId],'ps_ksh_bmbid',library_main_evalute_page_ks_grid_idx[0].get('ps_ksh_bmbid'));
			}
		}
	}


	/**
	* 功能： 刷新打分区域的值，非IE类浏览器
	* 参数： 新的JSON数据
	*/
	/*
	function refreshDfAreaInOthers(DfArea_NewJSON){
		//清空各容器的变量
		allDfAreaFormPanelFieldContainer_config[tmpBatchId] = new Array();
		dfArea_gzsm_ckwt_config[tmpBatchId] = new Array();
		dfArea_Height[tmpBatchId] = 0;
	
		//清空打分区域的内容
		var remove_return = dfAreaPanel[tmpBatchId].removeAll();
		//销毁TIPs
		Ext.destroy(dfArea_gzsm_ckwt_extObj[tmpBatchId]);
		dfArea_gzsm_ckwt_extObj[tmpBatchId] = new Array();
		
		dfAreaPanel[tmpBatchId].doLayout();
	
		//重新生成打分区域的各个元素
		var item_data = DfArea_NewJSON.scoreContent;
		//生成计算总分按钮
		//createCalbuttonConfig();
		
		for(var item_i=0;item_i<item_data.length;item_i++){
			var node_data = item_data[item_i];	
			var newFormField = createTreeNodeContainer(node_data);
		}
	
		//重新生成打分区域的底部按钮
		createSavebuttonConfig();
		//重新生成打分区的隐藏域
		createDfAreaHiddenField(userpoints.classId,userpoints.applyBatchId, userpoints.bmbId);
		//重新生成打分区域的Panel，不增加元素
		//createDfAreaFormPanel(false);
		//将重新生成的元素加载到打分区域中
		dfAreaPanel[tmpBatchId].add(allDfAreaFormPanelFieldContainer_config[tmpBatchId]);
		//调整打分区域的高度
		dfArea_Height[tmpBatchId] = dfArea_Height[tmpBatchId] + 108;
		dfAreaPanel[tmpBatchId].setHeight(dfArea_Height[tmpBatchId]);
		//使修改立即生效
		dfAreaPanel[tmpBatchId].doLayout();	
		
		//重新生成TIPs
		Ext.each(dfArea_gzsm_ckwt_config[tmpBatchId], function(config) {
			dfArea_gzsm_ckwt_extObj[tmpBatchId].push(Ext.create('Ext.tip.ToolTip', config));
		});
		
		var library_main_evalute_page_ks_grid_idx = window.library_main_evalute_page_ks_grid[tmpBatchId].getSelectionModel().getSelection();
		if(library_main_evalute_page_ks_grid_idx != null && library_main_evalute_page_ks_grid_idx != 'undefined')
		{
			if(library_main_evalute_page_ks_grid_idx[0] != null && library_main_evalute_page_ks_grid_idx[0] != 'undefined')
			{
				autoHighlightRow(dfPageWest_grid[tmpBatchId],'ps_ksh_bmbid',library_main_evalute_page_ks_grid_idx[0].get('ps_ksh_bmbid'));
			}
		}
		
	}
	*/


	/**
	* 功能： 刷新打分区与考生相关的HTMl内容区域，包括刷新右侧报名表
	* 参数： userpoints  从服务器端获取的JSON数据
	*/
	function refreshDfHTMLPanel(userpoints){
		
		//更新基本信息
		var show_ksinfohtml=genKSInfoHTML(userpoints);
		$("#show_ksinfo_div_"+tmpBatchId).html(show_ksinfohtml);
		
		//更新报名表
		//每个考生一个IFRAME
		var tmpBatchIdBMBID = tmpBatchId + '_' + userpoints.bmbId;
		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+userpoints.bmbId+'","TZ_APP_TPL_ID":"'+userpoints.psBmbTplId+'","isReview":"Y"}}';
		var bmb_url = ContextPath + "/dispatcher" + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl); 
		
		if($('#bmb_iframe_'+tmpBatchIdBMBID)[0]!=null && $('#bmb_iframe_'+tmpBatchIdBMBID)[0]!="undefined"){
			$("#bmb_iframe_div_"+tmpBatchId+" iframe").hide();
			$('#bmb_iframe_'+tmpBatchIdBMBID).show();
		}else{
			$("#bmb_iframe_div_"+tmpBatchId+" iframe").hide();
			$("#bmb_iframe_div_"+tmpBatchId).append('<iframe id="bmb_iframe_' + tmpBatchIdBMBID + '" name="bmb_iframe_' + tmpBatchIdBMBID + '" src="'+ bmb_url +'" frameborder="0" width="100%" height="100%" onload="unmaskWindow()"></iframe>');
		}
	}


	/**
	* 功能： 生成打分区域的panel实例
	* 参数： userpoints  从服务器端获取的JSON数据
	*/
	function createDfAreaPanel(userpoints){
	
		var item_data = userpoints.scoreContent;
		
		//创建考生的基本信息
		createKSinfoFieldContainer(userpoints);
	
		//生成计算总分按钮
		//createCalbuttonConfig();
		
		for(var item_i=0;item_i<item_data.length;item_i++){
			var node_data = item_data[item_i];
			//生成成绩项树形结构打分区
			var newFormField = createTreeNodeContainer(node_data,userpoints.bmbId);
		}
		
		//生成打分区域的底部按钮
		if(item_data!=null && item_data!="") {
			createSavebuttonConfig();
		}
		
		//生成打分区的隐藏域
		createDfAreaHiddenField(userpoints.classId, userpoints.applyBatchId, userpoints.bmbId);
		
		//生成打分区域的FormPanel
		createDfAreaFormPanel(true);
	
	}	
	/*********************************************************************************
	调用JSON数据，动态生成打分区 ----- END
	*********************************************************************************/	



	/***************************************************************************
	创建中间报名表Panel ----- BEGIN
	****************************************************************************/
	/**
	* 报名表服务器地址（测试直接返回HTML用）
	*/
	var KS_BMB_URL = "dafen.bmb.html";
	/**
	* 本地缓存报名表链接地址
	*/
	var KS_BMB_HTML_DATA = new Array();
	/**
	* 本地缓存报名表内容
	*/
	if(window.KS_BMB_INNERHTML_DATA==null){
		window.KS_BMB_INNERHTML_DATA = {};
	}
	var KS_BMB_HTML_PANEL;
	/**
	* 报名表链接地址，IFRAME用
	*/
	var show_BMB_html = "";
	function createBMBPanel(east_ksbmb_url, df_bmbid,psBmbTplId){
		// 采用IFrame方式
		var tzParamsBmbUrl='{"ComID":"TZ_ONLINE_REG_COM","PageID":"TZ_ONLINE_APP_STD","OperateType":"HTML","comParams":{"TZ_APP_INS_ID":"'+df_bmbid+'","TZ_APP_TPL_ID":"'+psBmbTplId+'","isReview":"Y"}}';
		var bmb_url = ContextPath + "/dispatcher" + "?tzParams=" + encodeURIComponent(tzParamsBmbUrl);
		show_BMB_html = bmb_url;
	}
	/***************************************************************************
	创建中间报名表Panel ----- END
	****************************************************************************/



	/***************************************************************************
	页面参数初始化完成后，展示整个页面-----BEGIN
	****************************************************************************/
	function createDfAreaExtPage(ksinfoJSON){
		var evaluatePage = Ext.create('Ext.panel.Panel', {
			layout: 'border',
			width: '100%',
			items: [
			{
				region: 'north',
				title: '搜索考生',
				collapsible: true,
				collapsed: true,
				hideCollapseTool: false,
				layout:'fit',
				split: true,
				width: '100%',
				//height:152,
				items: dfPageWest_KsSearch[tmpBatchId],
				listeners:{
					collapse: function(p){
						//调整浮动窗口位置---zhanglang
						TZShowTipsWin.autoFixPosition();
					},
					expand: function(p){
						//调整浮动窗口位置---zhanglang
						TZShowTipsWin.autoFixPosition();
					},
					resize: function(p, width, height, oldWidth, oldHeight){
						//调整浮动窗口位置---zhanglang
						TZShowTipsWin.autoFixPosition();
					}
				}
			},
			{
				region: 'west',
				collapsible: true,
				collapsed: true,
				hideCollapseTool: false,
				layout:'fit',
				split: true,
				title: '考生列表',
				width: '30%',
				items: dfPageWest_grid[tmpBatchId],
				listeners:{
					collapse:function(){
						Ext.fly("tz_evaluation_main").setScrollTop(0);
					}
				}
			},
			/*{
				region: 'south',
				title: '打分总体说明区',
				collapsible: true,
				collapsed: true,
				layout:'fit',
				html: '<div id="dfArea_ps_pysm_info_' + tmpBatchId + '">' + ksinfoJSON.ps_pysm_info + '</div>',
				split: true,
				minHeight: 100
			},*/
			{
				region: 'east',
				title: '【点击这里给该考生打分】',
				collapsible: true,
				collapsed: true,
				hideCollapseTool: false,
				//layout:'fit',
				layout: {type	: 'table',columns: 1},
				split: true,
				width: '70%',
				//items: [ksInfoPanel[tmpBatchId],xxtzPanel[tmpBatchId],dfAreaPanel[tmpBatchId]]
				items: [dfAreaPanel[tmpBatchId]],
				listeners:{
					collapse:function(){
						Ext.fly("tz_evaluation_main").setScrollTop(0);
					},
					resize: function(p, width, height, oldWidth, oldHeight){
						TZShowTipsWin.autoFixPosition(); //调整浮动窗口位置---zhanglang
					}
				}
			}, {
				region: 'center',
				autoScroll	: false,
				layout		: {type	: 'table',columns: 1},
				html		: '<div style="width:100%;height:100%;" id="bmb_iframe_div_'+ tmpBatchId +'"></div>'
				//html		: '<iframe id="bmb_iframe_' + tmpBatchId + '" name="bmb_iframe_' + tmpBatchId + '" src="'+ show_BMB_html +'" frameborder="0" width="100%" height="100%"></iframe>'
			}]
		});
		
		
		var toolbar3 = Ext.create('Ext.toolbar.Toolbar');
		toolbar3.add(
					{
						text: '返回批次评审主页面',
						tooltip:'单击此按钮返回批次评审主页面。',
						width: 150,
						glyph:'xf04a@FontAwesome',
						handler: function(item,pressed){showPreviousEvaluatePage(1);},
						pressed: true
					},
					'<b>' + ksinfoJSON['className'] + " " + ksinfoJSON['applyBatchName'] + '</b>'
				);

		
		var tmpEvaluatePanel = Ext.create('Ext.panel.Panel',
				{
					title: '考生评审打分',
					header: false,
					//id: 'KSMSPSYM_ID'+tmpBatchId,
					margin:'0 0 0 0',
					padding:'0 0 0 0',
					layout:'fit',
					overflowY:'hidden',
					collapsible:false,
					width: Ext.getBody().getWidth()-17,
					height:600,
					items: evaluatePage
				});
		
		try
		{
			var batchId = tmpEvaluateObject.baokaoPcID;
			var tmpDivObject = $('<div/>');
	
			window.myPageSlider[0].divPage.show();
	
			tmpDivObject.appendTo($('#tz_applicant_evaluate_page'));
			mainPageObjectArray['ApplicantEvaluatePageList'][batchId] = tmpDivObject;
	
			toolbar3.render(tmpDivObject[0]);
			tmpEvaluatePanel.render(tmpDivObject[0]);
		}
		catch(e1)
		{
			var msg = '加载考生评审主页面时发生错误：' + e1;
			Ext.Msg.alert('提示', msg);
		}
		
		window.evaluatePanel[tmpBatchId] = tmpEvaluatePanel;
		window.setTimeout(autoAdjustHeight,100);
		
		
		//绑定打分区域的Tips
		Ext.each(dfArea_gzsm_ckwt_config[tmpBatchId], function(config) {
			dfArea_gzsm_ckwt_extObj[tmpBatchId].push(Ext.create('Ext.tip.ToolTip', config));
		});
		
		//每个考生一个IFRAME
		var tmpBatchIdBMBID = tmpBatchId + '_' + ksinfoJSON.bmbId;
		
		if($('#bmb_iframe_'+tmpBatchIdBMBID)[0]!=null && $('#bmb_iframe_'+tmpBatchIdBMBID)[0]!="undefined"){
			$("#bmb_iframe_div_"+tmpBatchId+" iframe").hide();
			$('#bmb_iframe_'+tmpBatchIdBMBID).show();
		}else{
			$("#bmb_iframe_div_"+tmpBatchId).append('<iframe id="bmb_iframe_' + tmpBatchIdBMBID + '" name="bmb_iframe_' + tmpBatchIdBMBID + '" src="'+ show_BMB_html +'" frameborder="0" width="100%" height="100%" onload="unmaskWindow()"></iframe>');
		}	
	}
	/***************************************************************************
	页面参数初始化完成后，展示整个页面-----END
	****************************************************************************/



	/**
	* 打分区域初始化读取数据的URL
	*/
	//var dfArea_initLoad_URL = "http://crmtst.sem.tsinghua.edu.cn:8000/psc/CRMTST/EMPLOYEE/CRM/s/WEBLIB_TZ_PSXT.TZ_ZILIAO_PS.FieldFormula.IScript_ReadKSHEvaluateData?languageCd=ZHS";
	//var dfArea_initLoad_URL = "dafen.json.example.html";
	//var dfArea_initLoad_URL = '/dispatcher';
	var dfArea_initLoad_URL = window.scoreUrl;
	/*本地调试代码*/
	/*
	if(evaluateObject.applicantInterviewID == '201003187')
	{
		dfArea_initLoad_URL = "/dafen.json.example.html";
		evaluateObject.baokaoDirectionID = '1';
		evaluateObject.applicantBaomingbiaoID = '1';
	}
	*/


	/**
	* 打分区域提交数据的URL
	*/
	//var dfArea_Submit_URL = "http://crmtst.sem.tsinghua.edu.cn:8000/psc/CRMTST/EMPLOYEE/CRM/s/WEBLIB_TZ_PSXT.TZ_ZILIAO_PS.FieldFormula.IScript_SubmitKSHEvaluateData?languageCd=ZHS";
	//var dfArea_Submit_URL = "dafen.json.submit.html";
	//var dfArea_Submit_URL = '/dispatcher';
	var dfArea_Submit_URL = window.scoreUrl;
	
	//页面初始化，Ajax获取服务器端生成的JSON数据，根据数据生成打分区每一行的 ExtJs对象 数组
	if(window.KSINFO_JSON_DATA == null)
	{
		window.KSINFO_JSON_DATA = {};
	}
	if(window.KSINFO_JSON_DATA[tmpBatchId] == null)
	{
		window.KSINFO_JSON_DATA[tmpBatchId] = {};
	}


	/*将考生评审主页面考生列表中的选中考生与批次评审主页面的考生列表中的选中状态进行同步并高亮显示*/
	function highlightSelectedRowInApplicantList()
	{
		//------ highlight the selected row 
		var library_main_evalute_page_ks_grid_idx = window.library_main_evalute_page_ks_grid[tmpClassId+"_"+tmpBatchId].getSelectionModel().getSelection();
		if(library_main_evalute_page_ks_grid_idx != null && library_main_evalute_page_ks_grid_idx != 'undefined')
		{
			if(library_main_evalute_page_ks_grid_idx[0] != null && library_main_evalute_page_ks_grid_idx[0] != 'undefined')
			{
				autoHighlightRow(dfPageWest_grid[tmpBatchId],'ps_ksh_bmbid',library_main_evalute_page_ks_grid_idx[0].get('ps_ksh_bmbid'));
			}
		}
	}


	function getKSJSONData(df_classid, df_batchid, df_bmbid, actType)
	{
		if(KSINFO_JSON_DATA[tmpBatchId][df_bmbid]!="undefined" && KSINFO_JSON_DATA[tmpBatchId][df_bmbid]!=null && KSINFO_JSON_DATA[tmpBatchId][df_bmbid]!="")
		{
			var batchId = df_batchid;
			
			
			if(mainPageObjectArray['PreviousBatchId2'] != batchId)
			{
				if(mainPageObjectArray['PreviousBatchId2'] == '' || mainPageObjectArray['PreviousBatchId2'] == null || mainPageObjectArray['PreviousBatchId2'] == 'undefined')
				{
					mainPageObjectArray['PreviousBatchId2'] = batchId;
				}
				else
				{
					var pBatchId = mainPageObjectArray['PreviousBatchId2'];
					
					mainPageObjectArray['PreviousBatchId2'] = batchId;
					mainPageObjectArray['ApplicantEvaluatePageList'][pBatchId].hide();
				}
			}
			
			
			if(mainPageObjectArray['ApplicantEvaluatePageList'][batchId] == null)
			{
				createDfAreaALLPanel(KSINFO_JSON_DATA[tmpBatchId][df_bmbid], df_bmbid);
			}
			else
			{
				mainPageObjectArray['ApplicantEvaluatePageList'][batchId].show();
				
				refreshDfAreaFormPanel(KSINFO_JSON_DATA[tmpBatchId][df_bmbid]);
				refreshDfHTMLPanel(KSINFO_JSON_DATA[tmpBatchId][df_bmbid]);
			}
	
			//------ highlight the selected row 
			highlightSelectedRowInApplicantList();
			
			//--切换考生后，刷新浮动框的位置---zhanglang
			TZShowTipsWin.autoFixPosition();
			
			//回调指定的函数来显示考生评审页面
			callBackFunction(tipCount,scrollBackTagId);
		}
		else
		{
			var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"QF","comParams":{"classId":"'+df_classid+'","applyBatchId":"'+df_batchid+'","bmbId":"'+df_bmbid+'"}}';
			
			Ext.Ajax.request({
				url		: dfArea_initLoad_URL,
				params	: {tzParams:tzParams},
				success	: function(response)
				{
					var jsonObject = Ext.decode(response.responseText);
					
					userpoints = jsonObject.comContent;
					
					if(userpoints.messageCode != '0')
					{
						var msg = '读取考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据时发生错误：' + userpoints.message ;
						Ext.Msg.alert('提示', msg);

						//unmask window
						unmaskWindow();
					}
					else
					{

						if(userpoints.message!=null && userpoints.message!="") {
							Ext.Msg.alert('提示', userpoints.message);
						}

						var batchId = df_batchid;
						
						
						if(mainPageObjectArray['PreviousBatchId2'] != batchId)
						{
							if(mainPageObjectArray['PreviousBatchId2'] == '' || mainPageObjectArray['PreviousBatchId2'] == null || mainPageObjectArray['PreviousBatchId2'] == 'undefined')
							{
								mainPageObjectArray['PreviousBatchId2'] = batchId;
							}
							else
							{
								var pBatchId = mainPageObjectArray['PreviousBatchId2'];
								
								mainPageObjectArray['PreviousBatchId2'] = batchId;
								mainPageObjectArray['ApplicantEvaluatePageList'][pBatchId].hide();
							}
						}
							
							
						if(mainPageObjectArray['ApplicantEvaluatePageList'][batchId] == null)
						{
							createDfAreaALLPanel(userpoints, df_bmbid);
						}
						else
						{
							mainPageObjectArray['ApplicantEvaluatePageList'][batchId].show();
							
							refreshDfAreaFormPanel(userpoints);
							refreshDfHTMLPanel(userpoints);
						}
							
						//将数据JSON缓存到本地
						KSINFO_JSON_DATA[tmpBatchId][df_bmbid] = userpoints;
						
						//------ highlight the selected row 
						highlightSelectedRowInApplicantList();
						
						//--切换考生后，刷新浮动框的位置---zhanglang
						TZShowTipsWin.autoFixPosition();
						
						//回调指定的函数来显示考生评审页面
						callBackFunction(tipCount,scrollBackTagId);

						//报名表加载完成前不能操作,显示mask窗口
						maskWindow();
					}
				},
				failure: function(response, opts) {
					var msg ='读取考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据失败，请与系统管理员联系：' + response.responseText;
					Ext.Msg.alert('提示', msg);

					//unmask window
					unmaskWindow();
				}
				
			});
		}
	}


	/**
	* 提交、保存等操作后，更新本地缓存的JSON数据
	*/
	function updateKSJSONData(df_classid, df_batchid, df_bmbid, userpoints, getAgain){
		if(getAgain){
			
			var tzParams = '{"ComID":"TZ_EVA_INTERVIEW_COM","PageID":"TZ_MSPS_DF_STD","OperateType":"QF","comParams":{"classId":"'+df_classid+'","applyBatchId":"'+df_batchid+'","bmbId":"'+df_bmbid+'"}}';

			Ext.Ajax.request({	
				url		: dfArea_initLoad_URL,
				params	: {tzParams:tzParams},
				success	: function(responseData){
					try
					{
						var jsonObject = Ext.decode(response.responseText);
						
						userpoints = jsonObject.comContent;
						
						if(userpoints.messageCode!="0"){
							Ext.Msg.alert(userpoints.message);
							return ;
						}
						
						//将数据JSON缓存到本地
						KSINFO_JSON_DATA[tmpBatchId][df_bmbid] = userpoints;
						
						//unmask window
						unmaskWindow();
					}
					catch(e1){
						var msg ='读取考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据时发生错误，请与系统管理员联系：错误的JSON数据[' + e1 + ']';
						Ext.Msg.alert('提示', msg);
						//unmask window
						unmaskWindow();
					}
					
				},
				failure: function(response, opts) {
					var msg ='读取考生[' + tmpEvaluateObject.applicantName + '(' + tmpEvaluateObject.applicantInterviewID + ')]数据失败，请与系统管理员联系：' + response.responseText;
					Ext.Msg.alert('提示', msg);
					//unmask window
					unmaskWindow();
				}
				
			});
			
		}else{
		
			KSINFO_JSON_DATA[tmpBatchId][df_bmbid] = userpoints;
		}
	}



	/**
	* 生成所有的Panel，画出页面
	*/
	function createDfAreaALLPanel(ksjson_data, df_bmbid){
		//生成上侧搜索考生FORM
		createKsSearchForm(ksjson_data);
		//生成左侧考生列表
		createExamineeGrid(ksjson_data);
		//生成中间考生相关信息
		//createKSInfoPanel(ksjson_data);
		//生成右侧打分区域
		createDfAreaPanel(ksjson_data);
		//生成中间图表，暂没用
		//createDfAreaCharts();
		//生成中间考生报名表
		createBMBPanel("", df_bmbid,ksjson_data.psBmbTplId);
		//生成页面
		createDfAreaExtPage(ksjson_data);
		
		//调整一些特殊的样式
		//调整右侧文字大小
		//$("tspan:contains('点击这里给该考生打分')").css("font-size","13px");
		//$("tspan:contains('考生列表')").css("font-size","13px");
		
		
		//给所有叶子节点增加TIPS
		for(var li=0;li<allLeavesNodeData[tmpBatchId].length;li++){
			var dfarea_leaftips_data = allLeavesNodeData[tmpBatchId][li];	
			//createQTips(dfarea_leaftips_data);
			tzCreateTips(dfarea_leaftips_data);
		}

		//给所有叶子节点增加参考资料TIPS
		for(var li2=0;li2<allLeavesNodeDataCkzl[tmpBatchId].length;li2++) {
			var dfarea_leaftips_data_ckzl = allLeavesNodeDataCkzl[tmpBatchId][li2];

			//createQTips(dfarea_leaftips_data_ckzl);
			tzCreateTips(dfarea_leaftips_data_ckzl);
		}
	}


	/**
	* 给所有叶子节点增加TIPS
	*/
	function createQTips(dfarea_leaftips_data){
		$("#"+ dfarea_leaftips_data[0]).qtip(
		{
			content: {
				// Set the text to an image HTML string with the correct src URL to the loading image you want to use
				text: "<div style='width:100%'>"+dfarea_leaftips_data[1]+"</div>",
				title: {
					text: dfarea_leaftips_data[2], // Give the tooltip a title using each elements text
					button: "关闭"
				}
			},
			position: {
				at: 'top center', // Position the tooltip above the link
				my: 'bottom right',
				//viewport: $(window), // Keep the tooltip on-screen at all times
				viewport:$(".main"),
				effect: false // Disable positioning animation
			},
			show: {
				event: 'click',
				solo: true // Only show one tooltip at a time
			},
			//hide: 'unfocus',
			hide: false,
			style: {
				classes: 'ui-tooltip-wiki ui-tooltip-tipped ui-tooltip-shadow'
			}
		});	
	}
	
	
	/**
	 * 显示标准、说明、参考资料(新)
	 * 张浪
	 * 2017-05-23
	 */
	function tzCreateTips(dfarea_leaftips_data){
		try{
			TZShowTipsWin.init({
				type: "1",
				id: dfarea_leaftips_data[0],
				title: dfarea_leaftips_data[2],
				content: {
					text: dfarea_leaftips_data[1],
					okBtn: {
						text: '确定',
						callback: function(){
							//alert("bingo");
						}
					},
					cancelBtn: {
						text: '取消'
					}
				},
				position: {
					target: $("#tz_evaluation_main")
				}
			});
		}catch(ex){
		}
	}
	
	
	//初始化
	getKSJSONData(evaluateObject.baokaoClassID,evaluateObject.baokaoPcID,evaluateObject.applicantBaomingbiaoID,"create");
	
}
