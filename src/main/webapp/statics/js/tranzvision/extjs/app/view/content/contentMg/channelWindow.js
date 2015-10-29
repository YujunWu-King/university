Ext.define('KitchenSink.view.content.contentMg.channelWindow', {
    extend: 'Ext.window.Window',
    xtype: 'channelWindow', 
	controller: 'channelCtl',
	requires: [
	    'Ext.data.*',
        'Ext.grid.*',
        'Ext.util.*',
        'Ext.toolbar.Paging',
        'Ext.ux.ProgressBarPager',
		'KitchenSink.view.content.contentMg.channelController'
	],
    title: '栏目设置',
    reference:'channelWindow',
    width:500,
	bodyStyle:'overflow-y:auto;overflow-x:hidden',
	actType: 'add',//默认新增
    items: [{
        xtype: 'form',
        reference: 'userAccountForm',
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
            xtype: 'hiddenfield',
            fieldLabel: '站点ID',
			name: 'siteId'
        },{
            xtype: 'hiddenfield',
            fieldLabel: '栏目ID',
			name: 'lm_id'
        },{
            xtype: 'textfield',
            fieldLabel: '栏目名称',
			name: 'lm_name'
        }/*,{
            xtype: 'combobox',
            fieldLabel: '栏目类型',
			name: 'lm_lx',
			queryMode: 'remote',
            editable:false,
			valueField: 'TValue',
    		displayField: 'TSDesc',
    		store: new KitchenSink.view.common.store.appTransStore("TZ_ZDLM_LX")
    		
        },{
            xtype: 'combobox',
            fieldLabel: '栏目模板',
			name: 'lm_mb',
			queryMode: 'remote',
            editable:false,
			valueField: 'TZ_TEMP_ID',
    		displayField: 'TZ_TEMP_NAME'
        },{
            xtype: 'combobox',
            fieldLabel: '内容类型',
			//forceSelection: true,
            valueField: 'TValue',
            displayField: 'TSDesc',
            editable:false,
            queryMode: 'remote',
			name: 'lm_nrlx',
			store: new KitchenSink.view.common.store.appTransStore("TZ_ZD_NRLX")
			
        },{
            xtype: 'combobox',
            fieldLabel: '内容模板',
			name: 'lm_nrmb',
			queryMode: 'remote',
            editable:false,
    		valueField: 'TZ_TEMP_ID',
    		displayField: 'TZ_TEMP_NAME'
        }*/]
    }],
    buttons: [
	{
		text: '删除该栏目',
		iconCls:"save",
		handler: 'onDeleteChanl'
	},{
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
