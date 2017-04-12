Ext.define('KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementInfoList', {
    extend: 'Ext.panel.Panel',
    xtype: 'ckzlManagementInfoList', 
	controller: 'ckzlManagementController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.clmsHtpz.ckzlManagement.ckzlManagementStore'
	],
    title: '参考资料详细信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'ckzlManagementInfoListForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		//heigth: 600,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: "参考资料编号",
			name: 'ckzlid',
			allowBlank: false,
            readOnly:true,
            fieldStyle:'background:#F4F4F4',
            value: 'NEXT'
        }, {
            xtype: 'textfield',
            fieldLabel: "参考资料名称",
            allowBlank: false,
			name: 'ckzlName'
        }, {
            xtype: 'textfield',
            fieldLabel: "机构名称",
			name: 'jgName',
			value:Ext.tzOrgID,
			hidden:true
        }, {
            xtype: 'textfield',
            fieldLabel: "Java类",
			name: 'java'
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onComRegSave',
		reference:'ckzlSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onComRegEnsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onComRegClose'
	}]
});
