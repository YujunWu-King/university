Ext.define('KitchenSink.view.basicData.resData.hardCode.hardCodePanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'hardCodeInfo', 
	controller: 'hardCode',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	   'KitchenSink.view.basicData.resData.hardCode.hardCodeController'
	],
    title: 'hardCode点定义', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'hardCodeForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 130,
            labelStyle: 'font-weight:bold'
        },
		
        items: [{
            xtype: 'textfield',
            fieldLabel: Ext.tzGetResourse("TZ_GD_HARDCODE_COM.TZ_GD_HARDCODE_STD.hardCodeName","hardCode点名称"),
			name: 'hardCodeName',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_HARDCODE_COM.TZ_GD_HARDCODE_STD.hardCodeDesc","描述"),
			name: 'hardCodeDesc',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_HARDCODE_COM.TZ_GD_HARDCODE_STD.hardCodeValue","取值"),
			name: 'hardCodeValue',
			afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
			allowBlank: false
        }, {
            xtype: 'textfield',
			fieldLabel: Ext.tzGetResourse("TZ_GD_HARDCODE_COM.TZ_GD_HARDCODE_STD.hardCodeDetailDesc","详细描述"),
			name: 'hardCodeDetailDesc'
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onHardCodeSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onHardCodesure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onHardCodeClose'
	}]
});
