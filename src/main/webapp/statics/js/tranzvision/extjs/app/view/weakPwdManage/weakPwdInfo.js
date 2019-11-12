Ext.define('KitchenSink.view.weakPwdManage.weakPwdInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'weakPwdInfo', 
	controller: 'weakPwdController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager'
	],
    title: '弱密码信息',
	bodyStyle:'overflow-y:auto;overflow-x:hidden', 
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'weakPwdInfoForm',
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
            fieldLabel: Ext.tzGetResourse("TZ_WEAK_PWD_COM.TZ_WEAK_PWD_STD.TZ_PWD_ID","弱密码ID"),
			name: 'TZ_PWD_ID',
			maxLength: 20,
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_WEAK_PWD_COM.TZ_WEAK_PWD_STD.TZ_PWD_VAL","弱密码"),
			name: 'TZ_PWD_VAL',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }]
	}],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'saveWeakPwdInfo'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'ensureWeakPwdInfo'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'closeWeakPwdInfo'
	}]
});
