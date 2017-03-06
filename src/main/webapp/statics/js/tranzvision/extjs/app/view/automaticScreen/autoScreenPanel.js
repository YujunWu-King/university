﻿Ext.define('KitchenSink.view.automaticScreen.autoScreenPanel', {
    extend: 'Ext.panel.Panel',
	requires: [
        'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'tranzvision.extension.grid.column.Link',
        'KitchenSink.view.automaticScreen.autoScreenStore',
        'KitchenSink.view.automaticScreen.autoScreenController'
    ],
    xtype: 'autoScreen',
	controller: 'autoScreenController',
	
	title: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.autoScreen","自动初筛"),
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	
	listeners:{
		resize:function( panel, width, height, oldWidth, oldHeight, eOpts ){
			var buttonHeight = 36;
			var grid = panel.child('form').child('grid');
			if(grid) grid.setHeight( height-buttonHeight- 52);
		}
	},
	
	constructor: function(config){
		var className,
			itemColumns;
		this.paramsConfig = config;
		this.classId = config.classId;
		this.batchId = config.batchId;
		
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
			xtype:'rownumberer'
		},{ 
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.appId","报名表编号"),
			dataIndex: 'appId',
			width:90
		},{ 
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.name","姓名"),
			dataIndex: 'name',
			width:90
		},{ 
			text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.msApplyId","面试申请号"),
			dataIndex: 'msApplyId',
			width:100
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
			width:80
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.ranking","排名"),
			dataIndex: 'ranking',
			width:80
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.negativeList","负面清单"),
			dataIndex: 'negativeList',
			minWidth:140,
			flex:1
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.autoLabel","自动标签"),
			dataIndex: 'autoLabel',
			minWidth:140,
			flex:1
    	},{
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.manualLabel","手工标签"),
			dataIndex: 'manualLabel',
			minWidth:140,
			flex:1
    	},{
    		xtype:'checkcolumn',
    		text: Ext.tzGetResourse("TZ_AUTO_SCREEN_COM.TZ_AUTO_SCREEN_STD.status","是否淘汰"),
			dataIndex: 'status',
			width:80
    	},{
			menuDisabled: true,
			sortable: false,
			width:60,
			xtype: 'actioncolumn',
			align: 'center',
			items:[
				{iconCls: 'preview',tooltip: '查看报名表', handler: 'showApplicationForm'},
				{iconCls: ' edit',tooltip: '编辑', handler: 'editStuScreenDetails',
					isDisabled:function(view ,rowIndex ,colIndex ,item,record ){
						var scoreInsId = record.get('scoreInsId');
						if(scoreInsId == "0"){
							return true;
						}else{
							return false;
						}
					}
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
					fieldLabel: '班级',
					readOnly: true,
					cls: 'lanage_1',
					value: this.className
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
	    						text:'批量设置进入材料评审状态',
	    						iconCls:  'set',
	    						menu:[{
	    							text:'进入材料评审',iconCls:"check",handler:'setScreenPass'
	    						},{
	    							text:'淘汰',iconCls:"close",handler:'setScreenNoPass'
	    						}]
	    					},
	    					'->',
	    					{
	    						xtype:'splitbutton',
	    						text:'更多操作',
	    						iconCls:  'list',
	    						glyph: 61,
	    						menu:[{
	    							text:'查看自动初筛进程运行详情',iconCls:"view",handler:'showBatchProcessInfo'
	    						},{
	    							text:'根据名次批量淘汰',iconCls:"set",handler:'setWeedOutByRank'
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
	    				pageSize: 10,
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