Ext.define('KitchenSink.view.basicData.resData.appClass.appClassPanel', {
    extend: 'Ext.panel.Panel',
    xtype: 'appClassPanel', 
	controller: 'appClassController',
	actType:'',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
	   'KitchenSink.view.basicData.resData.appClass.appClassController'
	],
    title: '应用程序类定义', 
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
    items: [{
        xtype: 'form',
        reference: 'appClassForm',
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
            fieldLabel: Ext.tzGetResourse("TZ_APP_CLS_COM.TZ_APP_CLSINF_STD.appClassId","应用程序类ID"),
						name: 'appClassId',
            afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
            allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_APP_CLS_COM.TZ_APP_CLSINF_STD.appClassDesc","类方法描述"),
						name: 'appClassDesc',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_APP_CLS_COM.TZ_APP_CLSINF_STD.appClassName","类名称"),
						name: 'appClassName',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_APP_CLS_COM.TZ_APP_CLSINF_STD.appClassPath","类路径"),
						name: 'appClassPath',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }, {
            xtype: 'textfield',
						fieldLabel: Ext.tzGetResourse("TZ_APP_CLS_COM.TZ_APP_CLSINF_STD.appClassMehtod","类方法"),
						name: 'appClassMehtod',
						afterLabelTextTpl: [
                '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
            ],
						allowBlank: false
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onAppClassSave'
	}, {
		text: '确定',
		iconCls:"ensure",
		handler: 'onAppClassEsure'
	}, {
		text: '关闭',
		iconCls:"close",
		handler: 'onAppClassClose'
	}]
});
