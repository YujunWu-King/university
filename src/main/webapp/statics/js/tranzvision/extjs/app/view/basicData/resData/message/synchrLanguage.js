Ext.define('KitchenSink.view.basicData.resData.message.synchrLanguage', {
    extend: 'Ext.window.Window',
    xtype: 'synchrLanguage', 
	controller: 'msgInfoController',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.message.msgInfoController'
	],
    title: '同步资源',
    width:450,
    height:250,
    modal:true,
    ignoreChangesFlag: true,
    reference:'synchrLanguage',
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
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
            labelStyle: 'font-weight:bold',
            padding:'20 40 0 20'
        },
        items: [{
            xtype: 'hiddenfield',
            fieldLabel: '机构编号',
            name: 'orgId'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '消息集合编号',
			name: 'msgSetID'
        },{
            xtype: 'combobox',
            allowBlank:false,
            fieldLabel: '*源语言',
			name: 'sourceLanage',
			queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_LANGUAGE_ID")
        },{
            xtype: 'combobox',
            allowBlank:false,
            fieldLabel: '*目标语言',
			name: 'targetLanage',
			queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_LANGUAGE_ID")
        }]
    }],
    buttons: [{
		text: '确定',
		iconCls:"ensure",
		handler: 'onSynchrFormEnsure'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onSynchrFormClose'
	}]
});
