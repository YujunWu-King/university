Ext.define('KitchenSink.view.basicData.resData.translate.changeLanguage', {
    extend: 'Ext.window.Window',
    xtype: 'changeLanguage', 
	controller: 'tranSetMg',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.basicData.resData.translate.transSetController'
	],
    title: '切换语言',
    width:450,
    height:200,
    modal:true,
    ignoreChangesFlag: true,
    reference:'changeLanguage',
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
            labelWidth: 80,
            labelStyle: 'font-weight:bold',
           // padding:'40 40 0 20'
		   	margin:'30 40 0 20'
        },
        items: [{
            xtype: 'hiddenfield',
        	//xtype: 'textfield',
            fieldLabel:Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.transSetID","消息集合编号"),
			name: 'transSetID'
        },{
            xtype: 'combobox',
            fieldLabel: Ext.tzGetResourse("TZ_GD_TRANSLATE_COM.TZ_GD_TRANSDY_STD.language","语言名称"), 
			name: 'language',
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
		handler: 'onLanguageFormEnsure'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onLanguageFormClose'
	}]
});
