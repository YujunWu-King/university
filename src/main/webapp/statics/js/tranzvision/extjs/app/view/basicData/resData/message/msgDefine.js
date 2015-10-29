Ext.define('KitchenSink.view.basicData.resData.message.msgDefine', {
    extend: 'Ext.window.Window',
    xtype: 'msgDefine', 
	controller: 'msgDefineController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.message.msgDefineController'
	],
    title: '消息定义',
    width:600,
    modal:true,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
	reference:'msgDefine',
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
            labelWidth: 80,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '消息集合编号',
			name: 'msgSetID'
        },{
			xtype: 'hiddenfield',
            fieldLabel: '对应机构',
			name: 'orgId'	
		},{
            xtype: 'textfield',
            fieldLabel: '*消息编号',
            allowBlank:false,
			name: 'msgId'
        },{
            xtype: 'combobox',
            fieldLabel: '*语言',
			name: 'msgLanage',
			queryMode: 'remote',
            editable:false,
            readOnly:true,
            cls:'lanage_1',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_LANGUAGE_ID")
        },{
            xtype: 'textfield',
            fieldLabel: '消息文本',
			name: 'msgTest'
        },{
            xtype: 'textfield',
            fieldLabel: '标签ID',
			name: 'markId'
        },{
            xtype: 'textfield',
            fieldLabel: '关键字',
			name: 'keyWord'
        },{
            xtype: 'textareafield',
            fieldLabel: '消息描述',
            height:120,
			name: 'msgDesc'
        }]
    }],
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
