Ext.define('KitchenSink.view.automaticScreen.autoScreenPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.automaticScreen.autoScreenStore',
        'KitchenSink.view.automaticScreen.autoScreenController',
        'KitchenSink.view.automaticScreen.export.exportExcelWindow',
        'KitchenSink.view.automaticScreen.export.exportExcelController'
    ],
    xtype: 'autoScreen',
	controller: 'autoScreenController',
	//multiColumnSort: true,
	
	title: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.autoScreen","自动初筛"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 36;
			var grid = panel.child('form').child('grid');
			if(grid) grid.setHeight( height-buttonHeight- 80);
		}
	},
	
	constructor: function(config){
		var className,
			itemColumns = [];
		this.paramsConfig = config;
		this.classId = config.classId;
		this.batchId = config.batchId;
		this.batchName = config.batchName;
		className = config.className;
		
		var tzParams ='{"ComID":"TZ_AUTO_SCREEN_COM","PageID":"TZ_AUTO_SCREEN_STD","OperateType":"queryScoreColumns","comParams":{"classId":"'+ config.classId +'"}}';
		Ext.tzLoadAsync(tzParams,function(respData){
			className = respData.className;
			itemColumns = respData.columns;
		});
		
		this.className = className;
		this.itemColumns = itemColumns;
		
		this.callParent();	
	},
	
	
    initComponent: function () {   
    	var me = this;
    	var config = me.paramsConfig;
    	config.itemColumns = me.itemColumns;
    	var store = new KitchenSink.view.automaticScreen.autoScreenStore(config);
    	
    	//定义grid的columns
    	var columns = [{
			xtype:'rownumberer',
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.order","序号"),
			width:40
		}/*,{ 
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.appId","报名表编号"),
			dataIndex: 'appId',
			width:90
		}*/,{ 
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.msApplyId","面试申请号"),
			dataIndex: 'msApplyId',
			menuDisabled: true,
			sortable: false,
			hideable:false,
			width:100
		},{ 
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.name","姓名"),
			dataIndex: 'name',
			menuDisabled: true,
			sortable: false,
			hideable:false,
			width:90
		}]
    	
    	//成绩项分值列
    	var itemColumns = this.itemColumns;
    	for(var i=0; i<itemColumns.length; i++){
    		var colWidth = 100;
    		var descr = itemColumns[i].columnDescr;
    		var strLen = descr.length;
    		if(strLen > 0){
    			colWidth = strLen*15 + 20;
    			if(colWidth<90) colWidth = 90;
    			if(colWidth>140) colWidth = 140;
    		}
    		
    		columns.push({
    			xtype: 'linkcolumn',
    			text: descr,
				dataIndex: itemColumns[i].columnId,
				width:colWidth,
				menuDisabled: true,
				sortable: false,
				hideable:false,
				items:[{
					getText: function(v, meta, rec) {
						return v;
					},
					handler: 'onClickNumber'
				}]
    		});
    	}
    	
    	columns.push({
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.total","总分"),
			dataIndex: 'total',
			width:80,
			hideable:false
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.ranking","排名"),
			dataIndex: 'ranking',
			width:80,
			hideable:false
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.negativeList","负面清单"),
			dataIndex: 'negativeList',
			minWidth:200,
			menuDisabled: true,
			sortable: false,
			hideable:false,
			xtype: 'templatecolumn',
			tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
				labels: function(values){
					var labels = "";
					var val = values.negativeList;
					if(val.trim() != ""){
						var labelArr = val.split("|");
						for(var i=0;i<labelArr.length;i++){
							labels = labels 
							+ '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">' 
							+ labelArr[i] 
							+ '</span>';
						}
					}
					return labels;
				}
			}),
			flex:1
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.autoLabel","自动标签"),
			dataIndex: 'autoLabel',
			minWidth:200,
			menuDisabled: true,
			sortable: false,
			hideable:false,
			xtype: 'templatecolumn',
			tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
				labels: function(values){
					var labels = "";
					var val = values.autoLabel;
					if(val.trim() != ""){
						var labelArr = val.split("|");
						for(var i=0;i<labelArr.length;i++){
							labels = labels 
							+ '<span style="margin:0px 2px;padding:3px 5px;background:#CCC7C7;border-radius:5px;">' 
							+ labelArr[i] 
							+ '</span>';
						}
					}
					return labels;
				}
			}),
			flex:1
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.manualLabel","手工标签"),
			dataIndex: 'manualLabel',
			minWidth:200,
			menuDisabled: true,
			sortable: false,
			hideable:false,
			xtype: 'templatecolumn',
			tpl: Ext.create('Ext.XTemplate','{[this.labels(values)]}',{
				labels: function(values){
					var labels = "";
					var val = values.manualLabel;
					if(val.trim() != ""){
						var labelArr = val.split("|");
						for(var i=0;i<labelArr.length;i++){
							labels = labels 
							+ '<span style="margin:0px 2px;padding: 3px 5px;background:#CCC7C7;border-radius:5px;">' 
							+ labelArr[i] 
							+ '</span>';
						}
					}
					return labels;
				}
			}),
			flex:1
    	},{
    		xtype:'checkcolumn',
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.status","是否淘汰"),
			dataIndex: 'status',
			menuDisabled: true,
			sortable: false,
			hideable:false,
			width:80
    	},{
			menuDisabled: true,
			sortable: false,
			hideable:false,
			width:60,
			xtype: 'actioncolumn',
			align: 'center',
			items:[
				{iconCls: 'preview',tooltip: '查看报名表', handler: 'showApplicationForm'},
				{iconCls: 'edit',tooltip: '编辑', handler: 'editStuScreenDetails',
					/*isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
						var scoreInsId = record.get('scoreInsId');
						if(scoreInsId == "0"){
							return true;
						}else{
							return false;
						}
					}*/
				}
			]
		});
    	
    	Ext.apply(this, {
    	   items: [{        
		        xtype: 'form',
		        reference: 'autoScreenForm',
				layout: {
					type: 'vbox',
					align: 'stretch'
		        },
		        border: false,
				bodyPadding: 10,
				bodyStyle:'overflow-y:auto;overflow-x:hidden',
		        
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 100,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'hiddenfield',
					name: 'classId',
					value: this.classId
		        },{
		        	xtype: 'hiddenfield',
					name: 'batchId',
					value: this.batchId
		        },{
		        	xtype: 'textfield',
					name: 'className',
					fieldLabel: '报考方向',
					readOnly: true,
					cls: 'lanage_1',
					value: this.className
		        },{
		        	xtype: 'textfield',
					name: 'batchName',
					fieldLabel: '申请批次',
					readOnly: true,
					cls: 'lanage_1',
					value: this.batchName
		        },{
		        	xtype:'grid',
		        	
	    		    selModel: {
	    		        type: 'checkboxmodel'
	    		    },
	    		    multiSelect: true,
	    		    columnLines: true,
	    		    viewConfig: {
	    		        enableTextSelection: true
	    		    },
	    			frame: true,
	    			
	    		    dockedItems:[
	    				{
	    				xtype:"toolbar",
	    				items:[
	    					{text:"查询",tooltip:"查询数据",iconCls: "query",handler:"searchAutoScreenStu"},"-",
	    					{text:"运行自动初筛",tooltip:"运行自动初筛",iconCls:"set",handler:"runAutoScreenEngine"},"-",
	    					{
	    						xtype:'button',
	    						text:'设置淘汰状态',
	    						iconCls:  'set',
	    						menu:[{
	    							text:'将所选考生设置为淘汰',handler:'setScreenNoPass'
	    						},{
	    							text:'将所选考生取消淘汰',handler:'setScreenPass'
	    						},{
	    							text:'将查询结果所有考生设置为淘汰',handler:'setSearchScreenNoPass'
	    						},{
	    							text:'将查询结果所有考生取消淘汰',handler:'setSearchScreenPass'
	    						},{
	    							text:'根据名次批量淘汰',handler:'setWeedOutByRank'
	    						}]
	    					},
	    					'->',
	    					{
	    						xtype:'splitbutton',
	    						text:'更多操作',
	    						iconCls:  'list',
	    						glyph: 61,
	    						menu:[{
	    							text:'查看自动初筛进程运行详情',iconCls:"view",handler:'showBatchProcessInfo',
	    						},{
	    							text: '导出自动初筛结果',iconCls:"excel",
	    							menu:[{
	    								text:'导出选中考生的自动初筛结果',handler:'exportSelectedZdcsResult',
	    							},{
	    								text:'导出搜索结果考生的自动初筛结果',handler:'exportSearchZdcsResult'
	    							}]
	    						},{
	    							text:'查看并下载导出结果',iconCls:"download", handler:'downloadExportFile'
	    						}]
	    					}
	    				]
	    			}],
	    			
	    			plugins: [
	    				Ext.create('Ext.grid.plugin.CellEditing',{
	    					clicksToEdit: 1
	    				})
	    			],
	    			store: store,
	    			columns: columns,
	    			bbar: {
	    				xtype: 'pagingtoolbar',
	    				pageSize: 50,
	    				listeners:{
	    					afterrender: function(pbar){
	    						var grid = pbar.findParentByType("grid");
	    						pbar.setStore(grid.store);
	    					}
	    				},
	    				plugins: new Ext.ux.ProgressBarPager()
	    			}	
	    	   }]
    	   }]
		});
	   
	 	this.callParent();
    },
	buttons: [{
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.save","保存"),
		iconCls:"save",
		handler: 'onAutoScreenSave'
	}, {
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.ensure","确定"),
		iconCls:"ensure",
		closePanel:'Y',
		handler: 'onAutoScreenEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.close","关闭"),
		iconCls:"close",
		handler: 'onAutoScreenClose'
	}]
})
