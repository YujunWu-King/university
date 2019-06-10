Ext.define('KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryZw', {
    extend: 'Ext.window.Window',
    xtype: 'enrollmentClueSmsHistoryZw', 
    title: '短信历史正文', 
	reference: 'enrollmentClueSmsHistoryZw',
    width: 400,
    height: 300,
    minWidth: 200,
    minHeight: 200,
    layout: 'fit',
    resizable: true,
    modal: true,
    closeAction: 'destroy',
	items: [{
		xtype: 'form',	
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		//heigth: 600,
	
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 100,
			labelStyle: 'font-weight:bold'
		},
		items: [{
			xtype: 'textarea',
			fieldLabel: Ext.tzGetResourse("TZ_XSXS_ZSXS_COM.TZ_XSXS_SMSZW_STD.zw","正文内容"),
			name: 'zw',
			width: 300,
			height: 200
		}]
	}],
    buttons: [{
		text: '关闭',
		iconCls:"close",
		handler: 'smsHisClose'
	}]
});
