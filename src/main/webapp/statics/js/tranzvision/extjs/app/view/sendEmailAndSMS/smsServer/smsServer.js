Ext.define('KitchenSink.view.sendEmailAndSMS.smsServer.smsServer', {
    extend: 'Ext.panel.Panel',
    xtype: 'messageSet', 
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.sendEmailAndSMS.smsServer.smsServerInfoMth'
	],
    title: '短信网关设置', 
	controller: 'smsServerInfoMth',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	items: [{
		xtype: 'form',
		reference: 'smsServerForm',
		layout: {
			type: 'vbox',
			align: 'stretch'
		},
		border: false,
		bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
		listeners: {
			afterrender:'loadSmsServInfo'
		},
		
		fieldDefaults: {
			msgTarget: 'side',
			labelWidth: 120,
			labelStyle: 'font-weight:bold'
		},
		
		items: [{
			xtype: 'textfield',
			fieldLabel: '服务器编号',
			name: 'smssevid',
			allowBlank: false,
			fieldStyle:'background:#F4F4F4'
		},{
			xtype: 'textfield',
			fieldLabel: '短信服务器名称',
			name: 'smssevname',
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: '登录用户名',
			name: 'usrname',
			allowBlank: false
		}, {
			xtype: 'textfield',
			fieldLabel: '密码',
			name: 'usrpwd',
			inputType: 'password',
			allowBlank: false
		},{
			xtype: 'textfield',
			fieldLabel: '识别来源子号码',
			name:'sourceno'
		},{
			xtype: 'textarea',
			fieldLabel: '描述',
			name:'desc'
		}]
	}],
	buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	},{
        text: '确定',
        iconCls:"ensure",
        handler: 'onFormEnsure'
    },{
        text: '关闭',
        iconCls:"close",
        handler: 'onFormClose'
    }]
});
