Ext.define('KitchenSink.view.basicData.resData.translate.transDetailPanel', {
    extend: 'Ext.window.Window',
    xtype: 'transDetail',
	requires: [
		'KitchenSink.view.common.store.appTransStore'
	],
    title: '转换值定义', 
	reference: 'transDetailWindow',
    width: 600,
    minWidth: 300,
    minHeight: 380,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'hide',
	actType: 'add',
	
	initComponent: function(){
		var store = new KitchenSink.view.common.store.appTransStore("TZ_EFF_STATUS");
		Ext.apply(this,{
			items: [{
				xtype: 'form',
				layout: {
					type: 'vbox',
					align: 'stretch'
				},
				border: false,
				bodyPadding: 10,
				
				
				fieldDefaults: {
					msgTarget: 'side',
					labelWidth: 100,
					labelStyle: 'font-weight:bold'
				},
		
				items: [{
					xtype: 'textfield', 
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.transSetID","转换值集合编号"),
					name: 'transSetID',
					hidden: true
				},{
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.transID","转换值编号"),
					name: 'transID',
					afterLabelTextTpl: [
						'<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
					],
					allowBlank: false
				}, {
					xtype: 'datefield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.effeDate","生效日期"),
					format: 'Y-m-d',
					name: 'effeDate'
				},{
					xtype: 'combo',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.isEffe","有效状态"),
					name: 'isEffe',
					emptyText:'请选择',
					valueField: 'TValue',
					displayField: 'TSDesc',
					queryMode:'local',
					store: store			
				},
				{ 
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.shortDesc","短描述"),
					name: 'shortDesc'
				},{ 
					xtype: 'textfield',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.longDesc","长描述"),
					name: 'longDesc'
				},{ 
					xtype: 'hidden',
					fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSXX_STD.language","语言"),
					name: 'language'
				}]
			}]		  
		});
		this.callParent();
	},
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onTransValSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onTransValEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onTransValClose'
	}]
});
