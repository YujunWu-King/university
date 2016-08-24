Ext.define('KitchenSink.view.siteManage.simpleSiteManage.siteSimpleTemInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'siteSimpleTemInfoGL', 
	controller: 'siteSimpleTemController',
	requires: [
	    'Ext.data.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.simpleSiteManage.siteSimpleTemController'
        
	],
    title: '站点基本信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType:'',
    items: [{
        xtype: 'form',
        reference: 'siteSimpTmpForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        //frame: true,
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		buttonAlign: 'center',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 100,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            readOnly:true,
            fieldLabel: '机构编号',
			name: 'orgId',
			cls:'lanage_1'
        },{
            xtype: 'textfield',
            fieldLabel: '站点编号',
			name: 'siteId',
			readOnly: true,
			cls:'lanage_1'
        },{
            xtype: 'combobox',
            fieldLabel: '站点语言',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'siteLanguage',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_GD_LANGUAGE"),
			allowBlank: false
        },{
			xtype:'component',
			hideLabel: true,
			itemId: 'languageIntroduce',
			style:'color:#ff0000;font-size:14px;',
			margin: '-5 0 0 105',
			html:['站点语言一旦选定，您将无法自行切换修改，如果需要修改，请联系平台管理员进行切换修改，并且修改语言会导致所有历史网站的信息与数据丢失。'],
			hidden:true
		},{
            xtype: 'textfield',
            fieldLabel: '站点名称',
			name: 'siteName',
			allowBlank:false,
			maxLength: 100
        }]
    }],
    buttons: [{
		text: '保存',
		iconCls:"save",
		handler: 'onFormSave'
	},{
		text: '关闭',
		iconCls:"close",
		handler: 'onFormClose'
	}]
});