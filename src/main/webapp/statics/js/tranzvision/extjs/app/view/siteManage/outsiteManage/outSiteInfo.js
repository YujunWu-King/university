Ext.define('KitchenSink.view.siteManage.outsiteManage.outSiteInfo', {
    extend: 'Ext.panel.Panel',
    xtype: 'siteTemplateInfoGL', 
	controller: 'outsiteBasicB',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
        'KitchenSink.view.siteManage.outsiteManage.outsiteController'
	],
    title: '站点基本信息', 
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',// 默认新增
    items: [{
        xtype: 'form',
        reference: 'siteAccountForm',
		layout: {
            type: 'vbox',
            align: 'stretch'
        },
        // frame: true,
        border: false,
        bodyPadding: 10,
		bodyStyle:'overflow-y:auto;overflow-x:hidden',
		buttonAlign: 'center',
        fieldDefaults: {
            msgTarget: 'side',
            labelWidth: 170,
            labelStyle: 'font-weight:bold'
        },
        items: [{
            xtype: 'textfield',
            readOnly:true,
			fieldStyle:'background:#F4F4F4',
            fieldLabel: '站点编号',
			name: 'siteId',
			value: 'NEXT'
        }, {
            xtype: 'combobox',
            fieldLabel: '启用状态',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'enabled',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_SITEM_ENABLE"),
    		afterLabelTextTpl: [
    	                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	                    ]
        }, {
			xtype: 'combobox',
            fieldLabel: '站点语言',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'siteLanguage',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_GD_LANGUAGE"),
    		afterLabelTextTpl: [
    	                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	    ]
		},{
            xtype: 'textfield',
            fieldLabel: '站点名称',
			name: 'siteName',
			afterLabelTextTpl: [
		                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
		                    ]
        },{
			xtype: 'combobox',
            fieldLabel: '站点类型',
            editable:false,
            emptyText:'请选择',
            queryMode: 'remote',
			name: 'siteType',
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_OUTSITE_TYPE"),
    		afterLabelTextTpl: [
    	                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
    	                    ]
		},{  
            xtype: 'textareafield',  
            grow: true,
            name: 'siteIntroduce',  
            fieldLabel: '站点说明',
            preventScrollbars : false
        },{
            xtype: 'textfield',
            fieldLabel: '站点路径',
			name: 'sitePath',
			afterLabelTextTpl: [
		                        '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
		                    ]
        },{
            xtype: 'textfield',
            fieldLabel: '图片存放的服务器文件夹',
			name: 'picPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '附件存放的服务器文件夹',
			name: 'attPrefix'
        },{
            xtype: 'textfield',
            fieldLabel: '视频存放的服务器文件夹',
			name: 'viewPrefix'
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