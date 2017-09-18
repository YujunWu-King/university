Ext.define('KitchenSink.view.sendEmailAndSMS.emailTxType.emailTxTypePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'emailTxTypePanel', 
	controller: 'emailTxTypeController',
	requires: [
		'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.sendEmailAndSMS.emailTxType.txTypeRuleStore'
	],
    title: '退信类别定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	
	
	constructor: function(config, callback){
		this.actType = config.actType;
		this.reloadGrid = config.reloadGrid;
		
		this.callParent();	
	},
	
	initComponent: function () {
		var IDTypeReg = /^[0-9a-zA-Z_]+$/;
		Ext.apply(Ext.form.field.VTypes, {
            IdValType: function(val, field) {
                var bolFlag;
                bolFlag = IDTypeReg.test(val);
                return bolFlag;
            },
            IdValTypeText: '只能输入字母、数字和下划线'
        });
		
		var actType = this.actType;
		
		var gridHide = false;
		if(actType == "add"){
			gridHide = true;
		}
		
		
		Ext.apply(this, {
		    items: [{
		        xtype: 'form',
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
		            xtype: 'textfield',
		            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txTypeId','退信类别ID'),
					name: 'txTypeId',
					vtype: 'IdValType',
					allowBlank: false
		        },{
		        	xtype: 'textfield',
		        	fieldLabel: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txTypeName','退信类别名称'),
		        	name: 'txTypeName',
		        	allowBlank: false
		        },{
		            xtype: 'combobox',
		            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txType','退信类型'),
					forceSelection: true,
					editable: false,
					valueField: 'TValue',
		            displayField: 'TSDesc',
		            queryMode: 'local',
					name: 'txType',
		            allowBlank: false,
		            store: new KitchenSink.view.common.store.appTransStore("TZ_EMLTX_TYPE")
		        },{
		        	xtype: 'combobox',
		            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.isValid','有效状态'),
					forceSelection: true,
					editable: false,
					valueField: 'TValue',
		            displayField: 'TSDesc',
		            queryMode: 'local',
					name: 'isValid',
		            allowBlank: false,
		            store: new KitchenSink.view.common.store.appTransStore("TZ_IS_USED"),
		            value: 'Y'
		        },{
		            xtype: 'textarea',
		            fieldLabel: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txTypeDesc','描述'),
					name:'txTypeDesc'
				},{
					xtype: 'grid',
					title: '退信条件',
					frame: true,
					columnLines: true,
					minHeight: 350,
					reference: 'txTypeRuleGrid',
					multiSelect: true,
					selModel: {
						type: 'checkboxmodel'
					},
					hidden: gridHide,
					
					plugins: {
						ptype: 'cellediting',
						pluginId: 'txTypeRuleCellediting',
						clicksToEdit: 1
					},
					dockedItems:[{
						xtype:"toolbar",
						items:[
							{text:"新增",tooltip:"新增退信条件",iconCls: "add",handler:'addTxRule'},"-",
							{text:"删除",tooltip:"删除退信条件",iconCls: "remove",handler:'removeTxRule'}
						]
					}],
					columns: [{
						text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txRuleId','退信条件ID'),
						dataIndex: 'txRuleId',
						minWidth: 140,
						flex:1,
						editor: {
							xtype: 'textfield',
							editable: false,
							triggers: {
		                        search: {
		                            cls: 'x-form-search-trigger',
		                            handler: "selectTzRule"
		                        }
		                    }
						}
					},{
						text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.txRuleName','退信条件名称'),
						dataIndex: 'txRuleName',
						minWidth: 140,
						flex:1
					},/*{
						text: Ext.tzGetResourse('TZ_EMLTX_TYPE_COM.TZ_EMLTX_TYPE_STD.matchType','匹配类型'),
						dataIndex: 'matchType',
						width: 140
					},*/{
						menuDisabled: true,
						sortable: false,
						width:60,
						xtype: 'actioncolumn',
						align: 'center',
						items:[
							{iconCls: 'remove',tooltip: "删除", handler: 'deleteCurrentRow'}
						]
					}],
					store: {
						type: 'txTypeRuleStore'
					},
					bbar: {
						xtype: 'pagingtoolbar',
						pageSize: 20,
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
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onFormEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});
