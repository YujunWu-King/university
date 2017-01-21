Ext.define('KitchenSink.view.scoreModelManagement.scoreModelTreeNodeWin', {
    extend: 'Ext.window.Window',
    xtype: 'scoreModelTreeNodeWin', 
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.scoreModelManagement.scoreModelTreeManagerController'
	],
	controller: 'scoreModelTreeManagerController',
	reference: 'scoreModelTreeNodeWin',
	
	width: 800,
	height: 500,
	minWidth: 600,
	minHeight: 400,
  	resizable: false,
	modal: true,
	closeAction: 'destroy',
	
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.scoreItemInfo","成绩项详细设置"),
	pageY:80, 
	
	constructor: function(config, callback){
		this.actType = config.actType;
		this.reloadGrid = callback;
		
		this.callParent();	
	},
	
	initComponent: function () {
		var me = this;
		
		//成绩项类型Store
		var itemTypestore = Ext.create('Ext.data.Store', {
			 fields: [{
				 	name:'itemType'
			 	},{
			 		name:'itemTypeDesc'
			 	}],
             data: [{
            	 	itemType: 'A', 
            	 	itemTypeDesc: '数字成绩汇总项'
             	},{
             		itemType: 'B', 
             		itemTypeDesc: '数字成绩录入项'
             	},{
             		itemType: 'C', 
             		itemTypeDesc: '评语'
             	},{
             		itemType: 'D', 
             		itemTypeDesc: '下拉框'
             	}]
		 });
	
        Ext.apply(this, {
		    items: [{        
		        xtype: 'form',
		        reference: 'scoreItemForm',
		        bodyPadding: 10,
				//bodyStyle:'overflow-y:auto;overflow-x:hidden',
				layout: {
					type: 'vbox',
					align: 'stretch'
		        },
		        border: false,
		        fieldDefaults: {
		            msgTarget: 'side',
		            labelWidth: 100,
		            labelStyle: 'font-weight:bold'
		        },
		        items: [{
		            xtype: 'hiddenfield',
					name: 'orgId'
		        },{
		           	xtype: 'displayfield',
					fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.treeName","树名称"),
					name: 'treeName',
					allowBlank: false
		        },{
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.itemId","成绩项ID"),
					name: 'itemId',
		            allowBlank: false
		        },{
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.itemName","成绩项名称"),
					name: 'itemName',
		            allowBlank: false
		        },{
		           	xtype: 'combo',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.itemType","类型"),
					name: 'itemType',
		            queryMode: 'local',
		            editable:false,
		            valueField: 'itemType',
		    		displayField: 'itemTypeDesc',
					store: itemTypestore,
					allowBlank: false,
					listeners:{
                        change:function(combo){
                            combo.findParentByType('form').down('[reference=scoreItemTypeA]').setHidden(combo.value!="A");
                            combo.findParentByType('form').down('[reference=scoreItemTypeB]').setHidden(combo.value!="B");
                            combo.findParentByType('form').down('[reference=scoreItemTypeC]').setHidden(combo.value!="C");
                            combo.findParentByType('form').down('[reference=scoreItemTypeD]').setHidden(combo.value!="D");
                        }
                    }
		        },{
		        	xtype: 'fieldcontainer',
		        	reference: 'scoreItemTypeA',
		        	layout: {
						type: 'vbox',
						align: 'stretch'
			        },
			        style:'margin-left:105px;',
			        fieldDefaults: {
			        	labelWidth: 140
			        },
		        	items:[{
		        		xtype: 'textfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.UpHzXs","向上级成绩汇总系数"),
						name: 'UpHzXs',
		        	}]
		        },{
		        	xtype: 'fieldcontainer',
		        	reference: 'scoreItemTypeB',
		        	layout: {
						type: 'vbox',
						align: 'stretch'
			        },
			        style:'margin-left:105px;',
			        fieldDefaults: {
			        	labelWidth: 90
			        },
		        	items:[{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.weight","权重"),
						name: 'weightA',
		        	},{
		        		xtype: 'combo',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.lowerOperator","下限操作符"),
						name: 'lowerOperator',
		        	},{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.lowerLimit","分值下限"),
						name: 'lowerLimit',
		        	},{
		        		xtype: 'combo',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.upperOperator","上限操作符"),
						name: 'upperOperator',
		        	},{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.upperLimit","分值上限"),
						name: 'upperLimit',
		        	}]
		        },{
		        	xtype: 'fieldcontainer',
		        	reference: 'scoreItemTypeC',
		        	layout: {
						type: 'vbox',
						align: 'stretch'
			        },
			        style:'margin-left:105px;',
			        fieldDefaults: {
			        	labelWidth: 80
			        },
		        	items:[{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.wordLowerLimit","字数下限"),
						name: 'wordLowerLimit',
		        	},{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.wordUpperLimit","字数上限"),
						name: 'wordUpperLimit',
		        	}]
		        },{
		        	xtype: 'fieldcontainer',
		        	reference: 'scoreItemTypeD',
		        	layout: {
						type: 'vbox',
						align: 'stretch'
			        },
			        style:'margin-left:105px;',
			        fieldDefaults: {
			        	labelWidth: 140
			        },
		        	items:[{
		        		xtype: 'combo',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.xlTransScore","下拉选项转换为分值"),
						name: 'xlTransScore',
		        	},{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.weight","权重"),
						name: 'weightD',
		        	},{
		        		xtype: 'grid',
		        		height: 315, 
						frame: true,
						columnLines: true,
						name: 'comboTypeGrid',
						store: "",
						plugins: {
							ptype: 'cellediting',
							pluginId: 'TypeDCellediting',
							clicksToEdit: 1
						},
						columns: [{
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.optId","选项编号"),
							dataIndex: 'optId',
							width:60
						},{
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.optName","选项名称"),
							dataIndex: 'optName',
							width:60
						},{
							xtype: 'numbercolumn',
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.optScore","选项分值"),
							dataIndex: 'optScore',
							width:60
						},{
							xtype: 'checkcolumn',
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.isDefault","初始默认值"),
							dataIndex: 'isDefault',
							width:60
						},{
							menuDisabled: true,
							sortable: false,
							width:60,
							xtype: 'actioncolumn',
							align: 'center',
							items:[
								{iconCls: 'add', handler: 'addRowAfterCurrent'},
								{iconCls: 'remove', handler: 'deleteCurrentRow'}
							]
						}]
		        	}]
		        },{
		        	 xtype: 'textfield',
			         fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.refDataSet","参考资料设置"),
					 name: 'refDataSet',
					 editable: false,
                     triggers: {
                         clear: {
                             cls: 'x-form-clear-trigger',
                             handler: 'clearPmtSearch'
                         },
                         search: {
                             cls: 'x-form-search-trigger',
                             handler: "pmtSearchConRef"
                         }
                     }
		        },{
		        	xtype: 'ueditor',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.standard","标准"),
		            height: 200,
		            zIndex: 900,
					name: 'standard'
		        },{
		        	xtype: 'ueditor',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.descr","说明"),
		            height: 200,
		            zIndex: 900,
					name: 'descr'
		        },{
		        	xtype: 'ueditor',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.interviewMethod","面试方法"),
		            height: 200,
		            zIndex: 900,
					name: 'interviewMethod'
		        }]
		    }]
        });
		
        this.callParent();
    },
    buttons: [{
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.save","保存"),
		iconCls:"save",
		closePanel: 'N',
		handler: 'onScoreModelSave'
	}, {
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.ensure","确定"),
		iconCls:"ensure",
		closePanel: 'Y',
		handler: 'onScoreModelEnsure'
	}, {
		text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.close","关闭"),
		iconCls:"close",
		handler: 'onScoreModelClose'
	}]
})
