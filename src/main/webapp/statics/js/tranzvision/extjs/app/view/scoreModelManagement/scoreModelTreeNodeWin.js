Ext.define('KitchenSink.view.scoreModelManagement.scoreModelTreeNodeWin', {
    extend: 'Ext.window.Window',
    xtype: 'scoreModelTreeNodeWin', 
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.scoreModelManagement.scoreModelTreeManagerController',
        'KitchenSink.view.scoreModelManagement.scoreItemOptionsStore'
	],
	controller: 'scoreModelTreeManagerController',
	reference: 'scoreModelTreeNodeWin',
	
	width: 800,
	height: 520,
  	resizable: false,
	modal: true,
	closeAction: 'destroy',
	
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	title: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.scoreItemInfo","成绩项详细设置"),
	pageY:50, 
	
	constructor: function(config, callback){
		this.opeConfig = config;
		this.treeReload = callback;
		
		this.callParent();	
	},
	
	initComponent: function () {
		var me = this;
		
		var IDTypeReg = /^[0-9a-zA-Z_]+$/;
		Ext.apply(Ext.form.field.VTypes, {
            IdValType: function(val, field) {
                var bolFlag;
                bolFlag = IDTypeReg.test(val);
                return bolFlag;
            },
            IdValTypeText: '只能输入字母、数字和下划线'
        });
		
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
		
		
		//上限操作符
		var upLimitStore = Ext.create('Ext.data.Store', {
			 fields: [{
				 	name:'value'
			 	},{
			 		name:'descr'
			 	}],
             data: [{
            	 	value: '<', 
            	 	descr: '小于'
             	},{
             		value: '<=', 
             		descr: '小于等于'
             	}]
		 });
		
		//下限操作符
		var downLimitStore = Ext.create('Ext.data.Store', {
			 fields: [{
				 	name:'value'
			 	},{
			 		name:'descr'
			 	}],
             data: [{
            	 	value: '>', 
            	 	descr: '大于'
             	},{
             		value: '>=', 
             		descr: '大于等于'
             	}]
		 });
		
		//下拉选项转换为分值Store
		var optTransStore = Ext.create('Ext.data.Store', {
			 fields: [{
				 	name:'value'
			 	},{
			 		name:'descr'
			 	}],
             data: [{
            	 	value: 'Y', 
            	 	descr: '是'
             	},{
             		value: 'N', 
             		descr: '否'
             	}]
		 });
		
		//打分规则Store
		var ScoringRulesStore = new KitchenSink.view.common.store.comboxStore({
            recname:'PS_TZ_ZDCS_DFGZ_T',
            condition:{},
            result:'TZ_ZDCSGZ_ID,TZ_ZDCSGZ_NAME'
        });
		
		var itemOptionStore = new KitchenSink.view.scoreModelManagement.scoreItemOptionsStore();
		
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
		           	xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.treeName","树名称"),
					name: 'treeName',
					allowBlank: false,
					readOnly: true,
					cls: 'lanage_1'
		        },{
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.itemId","成绩项ID"),
					name: 'itemId',
		            allowBlank: false,
		            vtype: 'IdValType'
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
		        		layout: {
							type: 'column'
						},
						items:[{
							columnWidth: 1,
							xtype: 'numberfield',
				            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.UpHzXs","向上级成绩汇总系数"),
							name: 'UpHzXs',
							decimalPrecision: 2,
							decimalSeparator: '.',
							minValue: 0,
							maxValue: 100
						},{
							xtype:'displayfield',
							value: '%'
						}]
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
		        		layout: {
							type: 'column'
						},
						items:[{
							columnWidth: 1,
			        		xtype: 'numberfield',
				            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.weight","权重"),
							name: 'weightA',
							decimalPrecision: 2,
							decimalSeparator: '.',
							minValue: 0,
							maxValue: 100
						},{
							xtype:'displayfield',
							value: '%'
						}]
		        	},{
		        		xtype: 'combo',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.lowerOperator","下限操作符"),
						name: 'lowerOperator',
						store: downLimitStore,
						queryMode: 'local',
			            editable:false,
			            valueField: 'value',
			    		displayField: 'descr'
		        	},{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.lowerLimit","分值下限"),
						name: 'lowerLimit',
		        	},{
		        		xtype: 'combo',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.upperOperator","上限操作符"),
						name: 'upperOperator',
						store: upLimitStore,
						queryMode: 'local',
			            editable:false,
			            valueField: 'value',
			    		displayField: 'descr'
		        	},{
		        		xtype: 'numberfield',
			            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.upperLimit","分值上限"),
						name: 'upperLimit',
		        	},{
		        		layout: {
							type: 'column'
						},
						items:[{
							width: 95,
			        		xtype: 'checkbox',
			        		name: 'autoScreen',
			        		reference:'autoScreenCheck',
			        		inputValue: 'Y',
			        		boxLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.autoScreen","自动初筛")
						},{
							columnWidth: 1,
							xtype:'combo',
							name: 'scoringRules',
							hideLabel: true,
							emptyText: '请选择成绩项打分规则',
							store: ScoringRulesStore,
							queryMode: 'local',
				            editable:false,
				            valueField: 'TZ_ZDCSGZ_ID',
				    		displayField: 'TZ_ZDCSGZ_NAME',
				    		bind: {
				    			hidden: '{!autoScreenCheck.checked}'
				    		}
						}]
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
						store: optTransStore,
						queryMode: 'local',
			            editable:false,
			            valueField: 'value',
			    		displayField: 'descr'
		        	},{
		        		layout: {
							type: 'column'
						},
						items:[{
							columnWidth: 1,
			        		xtype: 'numberfield',
				            fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.weight","权重"),
							name: 'weightD',
							decimalPrecision: 2,
							decimalSeparator: '.',
							minValue: 0,
							maxValue: 100
						},{
							xtype:'displayfield',
							value: '%'
						}]
		        	},{
		        		xtype: 'grid',
		        		height: 300, 
						frame: true,
						columnLines: true,
						name: 'comboTypeGrid',
						store: itemOptionStore,
						selModel:{
							type: 'checkboxmodel'
						},
						plugins: {
							ptype: 'cellediting',
							pluginId: 'TypeDCellediting',
							clicksToEdit: 1
						},
						dockedItems:[{
							xtype:"toolbar",
							items:[
								{
									text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.add","新增"),
									iconCls:"add",
									handler:"addScoreItemOption"
								},"-",
								{
									text:Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.remove","删除"),
									iconCls:"remove",
									handler:'deleteScoreItemOptions'
								}
							]
						}],
						columns: [{
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.optId","选项编号"),
							dataIndex: 'optId',
							width:100,
							flex: 1,
							editor: {
								xtype: 'textfield'	
							}
						},{
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.optName","选项名称"),
							dataIndex: 'optName',
							width:100,
							flex: 1,
							editor: {
								xtype: 'textfield'	
							}
						},{
							xtype: 'numbercolumn',
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.optScore","选项分值"),
							dataIndex: 'optScore',
							width:100,
							flex: 1,
							editor: {
								xtype: 'numberfield'	
							}
						},{
							xtype: 'checkcolumn',
							text: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.isDefault","初始默认值"),
							dataIndex: 'isDefault',
							width:100
						},{
							menuDisabled: true,
							sortable: false,
							width:60,
							xtype: 'actioncolumn',
							align: 'center',
							items:[
								/*{iconCls: 'add', handler: 'addRowAfterCurrent'},*/
								{iconCls: 'remove', handler: 'deleteCurrentRow'}
							]
						}]
		        	}]
		        },{
		        	layout: {
						type: 'column'
					},
					items:[{
						columnWidth: 1, 
						xtype: 'textfield',
				        fieldLabel: Ext.tzGetResourse("TZ_SCORE_MOD_COM.TZ_TREE_NODE_STD.refDataSet","参考资料设置"),
					    name: 'refDataSet',
						editable: false,
	                    triggers: {
	                         clear: {
	                             cls: 'x-form-clear-trigger',
	                             hidden: true,
	             				 handler: function(field){
	             					field.setValue("");
	             					field.getTrigger('clear').hide();
	             					field.findParentByType('form').getForm().findField('refDataDescr').setValue('');
	             				 }
	                         },
	                         search: {
	                             cls: 'x-form-search-trigger',
	                             handler: "ckzlPmtSearch"
	                         }
	                     },
	                     listeners: {
	                    	 change: function(field, newValue, oldValue) {
	                    		 field.getTrigger('clear')[(newValue.length > 0) ? 'show' : 'hide']();
	                         }
	                     }
					},{
						xtype: 'displayfield',
						name: 'refDataDescr',
						style: 'margin-left:20px;'
					}]
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
