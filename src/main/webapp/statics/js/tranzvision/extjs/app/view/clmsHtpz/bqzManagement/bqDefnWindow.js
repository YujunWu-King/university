Ext.define('KitchenSink.view.clmsHtpz.bqzManagement.bqDefnWindow', {
    extend: 'Ext.window.Window',
    xtype: 'bqDefnWindow', 
    title: '标签定义', 
	reference: 'bqDefnWindow',
    width: 600,
    //height: 300,
    minWidth: 300,
    //minHeight: 300,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'destroy',
	actType: 'add',
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 300,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 100,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQ_DEFN_STD.bqID","标签组ID"),
			name: 'bqzID',
			hidden: true
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQ_DEFN_STD.bqID","标签ID"),
			name: 'bqID',
			hidden: true
		}, {
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQ_DEFN_STD.bqName","标签名称"),
			name: 'bqName'
		}, {
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQ_DEFN_STD.bqDesc","标签说明"),
			name: 'bqDesc'
		}, {
			xtype: 'checkboxfield',
			fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQ_DEFN_STD.csOut","初筛淘汰"),
			name: 'csOut',
			inputValue: 'Y'
		},{
			xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_BIAOQZ_COM.TZ_BIAOQ_DEFN_STD.java","java类"),
			name: 'java'
		}]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onbqDefnSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onbqDefnEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onbqDefnClose'
	}]
});
